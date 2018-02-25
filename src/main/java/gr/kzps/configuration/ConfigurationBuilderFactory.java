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
import org.apache.commons.configuration2.io.BasePathLocationStrategy;
import org.apache.commons.configuration2.io.ClasspathLocationStrategy;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Factory to get Configuration builders
 */
public class ConfigurationBuilderFactory {
  private final static Logger LOG = LogManager.getLogger
      (ConfigurationBuilderFactory.class);
  
  /**
   * Configuration builder to load the configuration file from the classpath
   * @return Configuration builder
   */
  public static FileBasedConfigurationBuilder<XMLConfiguration> getInstance() {
    LOG.debug("Creating FileBasedConfigurationBuilder with Classpath " +
        "location strategy");
    Parameters buildParameters = new Parameters();
    FileBasedConfigurationBuilder<XMLConfiguration> builder =
        new FileBasedConfigurationBuilder<XMLConfiguration>(XMLConfiguration.class)
        .configure(buildParameters.xml()
          .setFileName("zsak-conf.xml")
          .setLocationStrategy(new ClasspathLocationStrategy())
          .setValidating(false));
    
    return builder;
  }
  
  /**
   * Configuration builder to load the configuration file from a specific path
   * @param basePath Path the directory
   * @param fileName Name of the configuration file
   * @return Configuration builder
   */
  public static FileBasedConfigurationBuilder<XMLConfiguration> getInstance
      (String basePath, String fileName) {
    LOG.debug("Creating FileBasedConfigurationBuilder with BasePath location" +
        " strategy");
    Parameters builderParameters = new Parameters();
    FileBasedConfigurationBuilder<XMLConfiguration> builder =
        new FileBasedConfigurationBuilder<XMLConfiguration>(XMLConfiguration.class)
        .configure(builderParameters.xml()
          .setBasePath(basePath)
          .setFileName(fileName)
          .setLocationStrategy(new BasePathLocationStrategy())
          .setValidating(false));
    
    return builder;
  }
  
}
