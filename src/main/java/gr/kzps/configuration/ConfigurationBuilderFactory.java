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

import org.apache.commons.configuration2.XMLConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.io.BasePathLocationStrategy;
import org.apache.commons.configuration2.io.ClasspathLocationStrategy;

public class ConfigurationBuilderFactory {
  public static FileBasedConfigurationBuilder<XMLConfiguration> getInstance() {
    Parameters buildParameters = new Parameters();
    FileBasedConfigurationBuilder<XMLConfiguration> builder =
        new FileBasedConfigurationBuilder<XMLConfiguration>(XMLConfiguration.class)
        .configure(buildParameters.xml()
          .setFileName("zsak-conf.xml")
          .setLocationStrategy(new ClasspathLocationStrategy())
          .setValidating(false));
    
    return builder;
  }
  
  public static FileBasedConfigurationBuilder<XMLConfiguration> getInstance
      (String basePath, String fileName) {
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
