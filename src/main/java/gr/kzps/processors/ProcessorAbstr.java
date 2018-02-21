/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package gr.kzps.processors;

import gr.kzps.configuration.ProjectConfiguration;
import gr.kzps.configuration.ZsakConfiguration;
import gr.kzps.exceptions.ZeppelinConfException;
import org.apache.commons.configuration2.Configuration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public abstract class ProcessorAbstr implements Processor, DbProcessor {
  private final static Logger LOG = LogManager.getLogger(ProcessorAbstr.class);
  
  // SQL queries
  private String allZeppelinConfsSQL;
  private String getProjectNameSQL;
  private String updateConfSQL;
  
  private Configuration configuration;
  private Connection connection;
  
  protected Connection getConnection() {
    return connection;
  }
  
  @Override
  public void setConnection(Connection connection) {
    this.connection = connection;
  }
  
  protected Configuration getConfiguration() {
    return configuration;
  }
  
  @Override
  public void setConfiguration(Configuration configuration) {
    this.configuration = configuration;
    
    // SELECT * FROM zeppelin_interpreter_confs
    allZeppelinConfsSQL = "SELECT * FROM " + configuration.getString(
        ZsakConfiguration.ZEPPELIN_TABLE_NAME_KEY,
        ZsakConfiguration.ZEPPELIN_TABLE_NAME_DEFAULT);
    
    // SELECT projectname FROM project WHERE id = ?
    getProjectNameSQL = "SELECT " + configuration.getString(
        ZsakConfiguration.PROJECT_NAME_NAME_KEY,
        ZsakConfiguration.PROJECT_NAME_NAME_DEFAULT) +
        " FROM " + configuration.getString(
            ZsakConfiguration.PROJECT_TABLE_NAME_KEY,
            ZsakConfiguration.PROJECT_TABLE_NAME_DEFAULT) +
        " WHERE " + configuration.getString(
            ZsakConfiguration.PROJECT_ID_NAME_KEY,
            ZsakConfiguration.PROJECT_ID_NAME_DEFAULT) +
        " = ?";
    
    // UPDATE zeppelin_interpreter_confs SET interpreter_conf = ? WHERE id = ?
    updateConfSQL = "UPDATE " + configuration.getString(
        ZsakConfiguration.ZEPPELIN_TABLE_NAME_KEY,
        ZsakConfiguration.ZEPPELIN_TABLE_NAME_DEFAULT) + " SET "
        + configuration.getString(
            ZsakConfiguration.ZEPPELIN_CONF_INTERPRETER_CONF_NAME_KEY,
            ZsakConfiguration.ZEPPELIN_CONF_INTERPRETER_CONF_NAME_DEFAULT)
        + " = ? WHERE " + configuration.getString(
            ZsakConfiguration.ZEPPELIN_CONF_ID_NAME_KEY,
            ZsakConfiguration.ZEPPELIN_CONF_ID_NAME_DEFAULT)
        + " = ?";
  }
  
  // Visible for testing
  public String getAllZeppelinConfsSQL() {
    return allZeppelinConfsSQL;
  }
  
  // Visible for testing
  public String getGetProjectNameSQL() {
    return getProjectNameSQL;
  }
  
  // Visible for testing
  public String getUpdateConfSQL() {
    return updateConfSQL;
  }
  
  protected List<ProjectConfiguration> getAllZeppelinConfs() throws SQLException {
    Statement allZeppelinConfsStmt = null;
    try {
      allZeppelinConfsStmt = connection.createStatement();
      ResultSet rs = allZeppelinConfsStmt.executeQuery(allZeppelinConfsSQL);
      List<ProjectConfiguration> projectConfs = new ArrayList<>();
      while (rs.next()) {
        int projectId = rs.getInt(configuration.getString(
            ZsakConfiguration.ZEPPELIN_CONF_PROJECT_ID_NAME_KEY,
            ZsakConfiguration.ZEPPELIN_CONF_PROJECT_ID_NAME_DEFAULT));
        
        String projectName = getProjectNameFromID(projectId);
        ProjectConfiguration projectConf = new ProjectConfiguration(
            rs.getInt(configuration.getString(
                ZsakConfiguration.ZEPPELIN_CONF_ID_NAME_KEY,
                ZsakConfiguration.ZEPPELIN_CONF_ID_NAME_DEFAULT)),
            projectId, projectName,
            rs.getString(configuration.getString(
                ZsakConfiguration.ZEPPELIN_CONF_INTERPRETER_CONF_NAME_KEY,
                ZsakConfiguration.ZEPPELIN_CONF_INTERPRETER_CONF_NAME_DEFAULT)));
        projectConfs.add(projectConf);
      }
      return projectConfs;
    } finally {
      if (allZeppelinConfsStmt != null) {
        allZeppelinConfsStmt.close();
      }
    }
  }
  
  protected void updateDatabase(List<ProjectConfiguration> projectConfigurations)
      throws SQLException {
    PreparedStatement updateStmt = null;
    try {
      connection.setAutoCommit(false);
      updateStmt = connection.prepareStatement(updateConfSQL);
      for (ProjectConfiguration projectConf : projectConfigurations) {
        updateStmt.setString(1, projectConf.getInterpreterConfiguration());
        updateStmt.setInt(2, projectConf.getId());
        updateStmt.addBatch();
        LOG.info("Updating Zeppelin configuration for project: {}",
            projectConf.getProjectName());
      }
      
      updateStmt.executeBatch();
      connection.commit();
    } finally {
      if (updateStmt != null) {
        updateStmt.close();
      }
      connection.setAutoCommit(true);
    }
  }
  
  private String getProjectNameFromID(int projectId) throws SQLException {
    PreparedStatement projectNameStmt = null;
    try {
      projectNameStmt = connection.prepareStatement(getProjectNameSQL);
      projectNameStmt.setInt(1, projectId);
      ResultSet rs = projectNameStmt.executeQuery();
      rs.next();
      return rs.getString(configuration.getString(
          ZsakConfiguration.PROJECT_NAME_NAME_KEY,
          ZsakConfiguration.PROJECT_NAME_NAME_DEFAULT));
    } finally {
      if (projectNameStmt != null) {
        projectNameStmt.close();
      }
    }
  }
  
  public abstract void process() throws ZeppelinConfException;
  public abstract void cleanup();
}
