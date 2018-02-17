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
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.io.FileUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;

import static org.junit.Assert.*;

public class TestConfiguration {
  private static final File BASE_DIR = Paths.get(System.getProperty(
      "test.build.dir", "target/test-dir"),
      TestConfiguration.class.getSimpleName()).toFile();
  private static final File CONF_FILE = Paths.get(BASE_DIR.toString(),
      "zsak-conf.xml").toFile();
  
  @BeforeClass
  public static void setup() throws Exception {
    if (!BASE_DIR.exists()) {
      BASE_DIR.mkdirs();
    }
    prepareConfFile();
  }
  
  @AfterClass
  public static void destroy() throws Exception {
    FileUtils.deleteQuietly(BASE_DIR);
  }
  
  private static void prepareConfFile() throws IOException {
    StringBuilder sb = new StringBuilder();
    sb
        .append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>").append("\n")
        .append("<zsak>").append("\n")
        .append("</zsak>").append("\n");
    try (FileWriter fw = new FileWriter(CONF_FILE, false)) {
      fw.write(sb.toString());
    }
  }
  
  private FileBasedConfigurationBuilder<XMLConfiguration> getConfigurationBuilder() {
    Parameters builderParams = new Parameters();
  
    FileBasedConfigurationBuilder<XMLConfiguration> builder =
        new FileBasedConfigurationBuilder<XMLConfiguration>(XMLConfiguration.class)
            .configure(builderParams.xml()
                .setFile(CONF_FILE)
                .setValidating(false));
    return builder;
  }
  
  @Test
  public void testDatabaseConnectionProperties() throws Exception {
    FileBasedConfigurationBuilder<XMLConfiguration> builder = getConfigurationBuilder();
    Configuration conf = builder.getConfiguration();
    
    String host = "some.host.se";
    int port = 3306;
    String user = "userA";
    String password = "secure_password";
    
    conf.addProperty(ZsakConfiguration.DATABASE_HOST_KEY, host);
    conf.addProperty(ZsakConfiguration.DATABASE_PORT_KEY, port);
    conf.addProperty(ZsakConfiguration.DATABASE_USER_KEY, user);
    conf.addProperty(ZsakConfiguration.DATABASE_PASSWORD_KEY, password);
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
  }
}
