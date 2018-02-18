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
package gr.kzps;

import gr.kzps.DbConnection.DatabaseConnectionFactory;
import gr.kzps.configuration.ConfigurationBuilderFactory;
import gr.kzps.configuration.ZsakConfiguration;
import gr.kzps.processors.DbProcessor;
import gr.kzps.processors.Processor;
import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.XMLConfiguration;
import org.apache.commons.configuration2.beanutils.BeanDeclaration;
import org.apache.commons.configuration2.beanutils.BeanHelper;
import org.apache.commons.configuration2.beanutils.XMLBeanDeclaration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;


public class Zsak {
  private static final Logger LOG = LogManager.getLogger(Zsak.class);
  
  private Configuration configuration;
  private FileBasedConfigurationBuilder<XMLConfiguration> configBuilder;
  private Connection connection;
  private Processor processor;
  
  // Visible for testing
  public void setConfigBuilder(
      FileBasedConfigurationBuilder<XMLConfiguration> configBuilder) {
    this.configBuilder = configBuilder;
  }
  
  // Visible for testing
  public Processor getProcessor() {
    return processor;
  }
  
  public void init() throws ConfigurationException {
    LOG.info("Initializing...");
    if (configBuilder == null) {
      configBuilder = ConfigurationBuilderFactory.getInstance();
    }
    LOG.debug("Reading configuration file from {}",
        configBuilder.getFileHandler().getFile().toString());
    configuration = configBuilder.getConfiguration();
  }
  
  public Configuration getConfiguration() {
    return configuration;
  }
  
  
  public void start() throws SQLException, IOException {
    LOG.info("Starting...");
    try {
      processor = loadProcessor();
      processor.setConfiguration(configuration);
      if (processor instanceof DbProcessor) {
        String dbms = configuration.getString(ZsakConfiguration
            .DATABASE_DBMS_KEY, ZsakConfiguration.DATABASE_DBMS_DEFAULT);
        connection = DatabaseConnectionFactory.builder(dbms)
            .setHost(
                configuration.getString(ZsakConfiguration.DATABASE_HOST_KEY))
            .setPort(configuration.getInt(ZsakConfiguration.DATABASE_PORT_KEY))
            .setUser(
                configuration.getString(ZsakConfiguration.DATABASE_USER_KEY))
            .setPassword(configuration.getString(ZsakConfiguration
                .DATABASE_PASSWORD_KEY))
            .setSchema(configuration.getString(
                ZsakConfiguration.DATABASE_SCHEMA_KEY,
                ZsakConfiguration.DATABASE_SCHEMA_DEFAULT))
            .build();
        ((DbProcessor) processor).setConnection(connection);
      }
    } catch (SQLException ex) {
      throw ex;
    } catch (Exception ex) {
      LOG.error("Error while loading processor. Exiting...", ex);
      throw new IOException(ex);
    }
  }
  
  private Processor loadProcessor() {
    BeanDeclaration beanDeclaration = new XMLBeanDeclaration(
        (XMLConfiguration) configuration, ZsakConfiguration
        .PROCESSOR_CLASS_NAME_KEY);
    return (Processor) BeanHelper.INSTANCE.createBean(beanDeclaration);
  }
  
  public void apply() {
    processor.process();
  }
  
  public void cleanup() throws Exception {
    processor.cleanup();
    if (connection != null) {
      connection.close();
    }
  }
  
  public static void main(String[] argv) {
    LOG.info("Starting Zeppelin swiss army knife");
    
    Zsak zsak = new Zsak();
    try {
      zsak.init();
      zsak.start();
      zsak.apply();
      zsak.cleanup();
    } catch (ConfigurationException ex) {
      LOG.error("Error while parsing configuration file. Exiting...", ex);
      System.exit(1);
    } catch (SQLException ex) {
      LOG.error("Database error. Trying to cleanup", ex);
      try {
        zsak.cleanup();
      } catch (Exception cuex) {
        LOG.error("Error while cleaning up", cuex);
      }
      System.exit(2);
    } catch (IOException ex) {
      LOG.error("Processor error", ex);
      System.exit(3);
    } catch (Exception ex) {
      // TODO(Antonis) Fix exception granularity
      LOG.error("General error", ex);
      System.exit(4);
    }
  }
}
