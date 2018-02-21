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
package gr.kzps.configuration;

import gr.kzps.Zsak;
import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.XMLConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Test;

import java.nio.file.Paths;

import static org.junit.Assert.*;

public class TestConfiguration extends ZsakBaseTest {
  
  @Test
  public void testDatabaseConnectionProperties() throws Exception {
    FileBasedConfigurationBuilder<XMLConfiguration> builder = getConfigurationBuilder();
    Configuration conf = builder.getConfiguration();
    
    String host = "some.host.se";
    int port = 3306;
    String user = "userA";
    String password = "secure_password";
    String schema = "my_hopsworks";
    
    conf.addProperty(ZsakConfiguration.DATABASE_HOST_KEY, host);
    conf.addProperty(ZsakConfiguration.DATABASE_PORT_KEY, port);
    conf.addProperty(ZsakConfiguration.DATABASE_USER_KEY, user);
    conf.addProperty(ZsakConfiguration.DATABASE_PASSWORD_KEY, password);
    conf.addProperty(ZsakConfiguration.DATABASE_SCHEMA_KEY, schema);
    builder.save();
    
    FileBasedConfigurationBuilder<XMLConfiguration> zsakBuilder =
        ConfigurationBuilderFactory.getInstance(BASE_DIR.toString(),
            "zsak-conf.xml");
    Zsak zsak = new Zsak();
    zsak.setConfigBuilder(zsakBuilder);
    zsak.init();
    Configuration zsakConf = zsak.getConfiguration();
    assertEquals(host, zsakConf.getString(ZsakConfiguration.DATABASE_HOST_KEY));
    assertEquals(port, zsakConf.getInt(ZsakConfiguration.DATABASE_PORT_KEY));
    assertEquals(user, zsakConf.getString(ZsakConfiguration.DATABASE_USER_KEY));
    assertEquals(password, zsakConf.getString(ZsakConfiguration.DATABASE_PASSWORD_KEY));
    assertEquals(schema, zsakConf.getString(
        ZsakConfiguration.DATABASE_SCHEMA_KEY,
        ZsakConfiguration.DATABASE_SCHEMA_DEFAULT));
  }
  
  @Test
  public void testSQLQueriesSubstitution() throws Exception {
    FileBasedConfigurationBuilder<XMLConfiguration> builder =
        getConfigurationBuilder();
    Configuration conf = builder.getConfiguration();
    conf.addProperty(ZsakConfiguration.ZEPPELIN_TABLE_NAME_KEY,
        "zeppelin_interpreter_confs");
    conf.addProperty(ZsakConfiguration.PROJECT_NAME_NAME_KEY, "projectname");
    conf.addProperty(ZsakConfiguration.PROJECT_TABLE_NAME_KEY, "project");
    conf.addProperty(ZsakConfiguration.PROJECT_ID_NAME_KEY, "id");
    
    conf.addProperty(ZsakConfiguration
        .ZEPPELIN_CONF_INTERPRETER_CONF_NAME_KEY, "interpreter_conf");
    conf.addProperty(ZsakConfiguration.ZEPPELIN_CONF_ID_NAME_KEY, "id");
    builder.save();
    
    DbProcessorTest processorTest = new DbProcessorTest();
    processorTest.setConfiguration(conf);
    
    String allZeppelingConfsSQL = "SELECT * FROM zeppelin_interpreter_confs";
    String projectNameSQL = "SELECT projectname FROM project WHERE id = ?";
    String updateConfSQL = "UPDATE zeppelin_interpreter_confs SET " +
        "interpreter_conf = ? WHERE id = ?";
    assertTrue(processorTest.isConfigurationSet());
    assertEquals(allZeppelingConfsSQL, processorTest.getAllZeppelinConfsSQL());
    assertEquals(projectNameSQL, processorTest.getGetProjectNameSQL());
    assertEquals(updateConfSQL, processorTest.getUpdateConfSQL());
  }
  
  @Test
  public void testZeppelinTableProperties() throws Exception {
    FileBasedConfigurationBuilder<XMLConfiguration> builder = getConfigurationBuilder();
    Configuration conf = builder.getConfiguration();
    String zeppelinTableName = "zeppelin_inter";
    String zeppelinConfIdName = "conf_id";
    String zeppelinProjectIdName = "project_id";
    String zeppelinLastUpdateName = "last_update_time";
    String zeppelinInterConfName = "inter_conf";
    conf.addProperty(ZsakConfiguration.ZEPPELIN_TABLE_NAME_KEY, zeppelinTableName);
    conf.addProperty(ZsakConfiguration.ZEPPELIN_CONF_ID_NAME_KEY,
        zeppelinConfIdName);
    conf.addProperty(ZsakConfiguration.ZEPPELIN_CONF_PROJECT_ID_NAME_KEY, zeppelinProjectIdName);
    conf.addProperty(ZsakConfiguration.ZEPPELIN_CONF_LAST_UPDATE_NAME_KEY,
        zeppelinLastUpdateName);
    conf.addProperty(ZsakConfiguration
        .ZEPPELIN_CONF_INTERPRETER_CONF_NAME_KEY, zeppelinInterConfName);
    
    String projectTableName = "proj_table";
    String projectIdName = "id";
    String projectNameName = "project_name";
    conf.addProperty(ZsakConfiguration.PROJECT_TABLE_NAME_KEY, projectTableName);
    conf.addProperty(ZsakConfiguration.PROJECT_ID_NAME_KEY, projectIdName);
    conf.addProperty(ZsakConfiguration.PROJECT_NAME_NAME_KEY, projectNameName);
    builder.save();
  
    FileBasedConfigurationBuilder<XMLConfiguration> zsakBuilder =
        ConfigurationBuilderFactory.getInstance(BASE_DIR.toString(),
            "zsak-conf.xml");
    Zsak zsak = new Zsak();
    zsak.setConfigBuilder(zsakBuilder);
    zsak.init();
    Configuration zsakConf = zsak.getConfiguration();
    assertEquals(zeppelinTableName, zsakConf.getString(
        ZsakConfiguration.ZEPPELIN_TABLE_NAME_KEY,
        ZsakConfiguration.ZEPPELIN_TABLE_NAME_DEFAULT));
    assertEquals(zeppelinConfIdName, zsakConf.getString(
        ZsakConfiguration.ZEPPELIN_CONF_ID_NAME_KEY,
        ZsakConfiguration.ZEPPELIN_CONF_ID_NAME_DEFAULT));
    assertEquals(zeppelinProjectIdName, zsakConf.getString(
        ZsakConfiguration.ZEPPELIN_CONF_PROJECT_ID_NAME_KEY,
        ZsakConfiguration.ZEPPELIN_CONF_PROJECT_ID_NAME_DEFAULT));
    assertEquals(zeppelinLastUpdateName, zsakConf.getString(
        ZsakConfiguration.ZEPPELIN_CONF_LAST_UPDATE_NAME_KEY,
        ZsakConfiguration.ZEPPELIN_CONF_LAST_UPDATE_NAME_DEFAULT));
    assertEquals(zeppelinInterConfName, zsakConf.getString(
        ZsakConfiguration.ZEPPELIN_CONF_INTERPRETER_CONF_NAME_KEY,
        ZsakConfiguration.ZEPPELIN_CONF_INTERPRETER_CONF_NAME_DEFAULT));
    assertEquals(projectTableName, zsakConf.getString(
        ZsakConfiguration.PROJECT_TABLE_NAME_KEY,
        ZsakConfiguration.PROJECT_TABLE_NAME_DEFAULT));
    assertEquals(projectIdName, zsakConf.getString(
        ZsakConfiguration.PROJECT_ID_NAME_KEY,
        ZsakConfiguration.PROJECT_ID_NAME_DEFAULT));
    assertEquals(projectNameName, zsakConf.getString(
        ZsakConfiguration.PROJECT_NAME_NAME_KEY,
        ZsakConfiguration.PROJECT_NAME_NAME_DEFAULT));
  }
}
