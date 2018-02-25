/**
 Copyright 2018 Antonios Kouzoupis <kouzoupis.ant@gmail.com>
 
 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at
 
 http://www.apache.org/licenses/LICENSE-2.0
 
 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 */
package gr.kzps.configuration;

import org.apache.commons.configuration2.XMLConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.io.FileUtils;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;

public class ZsakBaseTest {
  protected static final File BASE_DIR = Paths.get(System.getProperty(
      "test.build.dir", "target/test-dir"),
      TestConfiguration.class.getSimpleName()).toFile();
  protected static final File CONF_FILE = Paths.get(BASE_DIR.toString(),
      "zsak-conf.xml").toFile();
  
  @BeforeClass
  public static void setup() throws Exception {
    if (!BASE_DIR.exists()) {
      BASE_DIR.mkdirs();
    }
  }
  
  @AfterClass
  public static void destroy() throws Exception {
    FileUtils.deleteQuietly(BASE_DIR);
  }
  
  @Before
  public void initConfFile() throws IOException {
    prepareConfFile();
  }
  
  protected FileBasedConfigurationBuilder<XMLConfiguration> getConfigurationBuilder() {
    Parameters builderParams = new Parameters();
    
    FileBasedConfigurationBuilder<XMLConfiguration> builder =
        new FileBasedConfigurationBuilder<XMLConfiguration>(XMLConfiguration.class)
            .configure(builderParams.xml()
                .setFile(CONF_FILE)
                .setValidating(false));
    return builder;
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
}
