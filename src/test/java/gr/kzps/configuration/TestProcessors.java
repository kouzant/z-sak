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
import gr.kzps.processors.Processor;
import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.XMLConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;

import static org.junit.Assert.*;

public class TestProcessors extends ZsakBaseTest {
  
  @Test
  public void testSimpleProcessor() throws Exception {
    addProcessorClassToConf(SimpleProcessorTest.class);
  
    FileBasedConfigurationBuilder<XMLConfiguration> zsakBuilder =
        ConfigurationBuilderFactory.getInstance(BASE_DIR.toString(),
            "zsak-conf.xml");
    Zsak zsak = new Zsak();
    zsak.setConfigBuilder(zsakBuilder);
    zsak.init();
    zsak.start();
    zsak.apply();
    zsak.cleanup();
    Processor processor = zsak.getProcessor();
    assertTrue(((SimpleProcessorTest) processor).isConfigurationSet());
    assertTrue(((SimpleProcessorTest) processor).isProcessCalled());
    assertTrue(((SimpleProcessorTest) processor).isCleanupCalled());
  }
  
  @Test
  public void testDbProcessor() throws Exception {
    addProcessorClassToConf(DbProcessorTest.class);
    
    FileBasedConfigurationBuilder<XMLConfiguration> zsakBuilder =
        ConfigurationBuilderFactory.getInstance(BASE_DIR.toString(),
            "zsak-conf.xml");
    Configuration conf = zsakBuilder.getConfiguration();
    conf.addProperty(ZsakConfiguration.DATABASE_DBMS_KEY, "none");
  
    conf.addProperty(ZsakConfiguration.DATABASE_HOST_KEY, "host");
    conf.addProperty(ZsakConfiguration.DATABASE_PORT_KEY, 1234);
    conf.addProperty(ZsakConfiguration.DATABASE_USER_KEY, "user");
    conf.addProperty(ZsakConfiguration.DATABASE_PASSWORD_KEY, "password");
    conf.addProperty(ZsakConfiguration.DATABASE_SCHEMA_KEY, "schema");
    zsakBuilder.save();
    Zsak zsak = new Zsak();
    zsak.setConfigBuilder(zsakBuilder);
    zsak.init();
    zsak.start();
    zsak.apply();
    zsak.cleanup();
    Processor processor = zsak.getProcessor();
    assertTrue(((DbProcessorTest) processor).isConfigurationSet());
    assertTrue(((DbProcessorTest) processor).isConnectionSet());
    assertTrue(((DbProcessorTest) processor).isProcessCalled());
    assertTrue(((DbProcessorTest) processor).isCleanupCalled());
    assertNull(((DbProcessorTest) processor).getConnection());
  }
  
  // Hackish way to overwrite the configuration but it works :)
  private void addProcessorClassToConf(Class clazz) throws IOException {
    List<String> lines = FileUtils.readLines(CONF_FILE,
        Charset.defaultCharset());
    
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < lines.size() - 1; i++) {
      sb.append(lines.get(i)).append("\n");
    }
  
    sb.append("<processors>").append("\n");
    sb.append("<processor config-class=\"" + clazz.getCanonicalName() + "\"/>").append("\n");
    sb.append("</processors>").append("\n");
    sb.append(lines.get(lines.size() - 1)).append("\n");
  
    try (FileWriter fw = new FileWriter(CONF_FILE, false)) {
      fw.write(sb.toString());
    }
  }
}
