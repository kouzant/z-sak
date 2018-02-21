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
package gr.kzps.processors.impls;

import gr.kzps.configuration.ProjectConfiguration;
import gr.kzps.exceptions.ZeppelinConfException;
import gr.kzps.processors.ProcessorAbstr;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.SQLException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class ChangeHadoopVersionProcessor extends ProcessorAbstr {
  private static final Logger LOG = LogManager.getLogger
      (ChangeHadoopVersionProcessor.class);
  private final Pattern VERSION_PATTERN = Pattern.compile(".*2\\.8\\.2\\.[12].*");
  
  @Override
  public void process() throws ZeppelinConfException {
    try {
      List<ProjectConfiguration> projectConfs = getAllZeppelinConfs();
      fixHadoopVersion(projectConfs);
    } catch (SQLException ex) {
      LOG.error("Error while processing", ex);
      throw new ZeppelinConfException("Error while processing", ex);
    }
  }
  
  private void fixHadoopVersion(List<ProjectConfiguration> projectConfs)
    throws SQLException {
    LOG.info("Fixing...");
    List<ProjectConfiguration> updatedConfs = projectConfs.stream()
        .map(pc -> {
          LOG.info("Project: {}", pc.getProjectName());
          String conf = pc.getInterpreterConfiguration();
          Matcher versionMatcher = VERSION_PATTERN.matcher(conf);
          if (versionMatcher.find()) {
            conf = conf.replaceAll("2\\.8\\.2\\.1", "2.8.2.3");
            conf = conf.replaceAll("2\\.8\\.2\\.2", "2.8.2.3");
            pc.setInterpreterConfiguration(conf);
            pc.update();
          }
          return pc;
        }).filter(pc -> pc.isUpdated())
        .collect(Collectors.toList());
    updateDatabase(updatedConfs);
  }
  
  @Override
  public void cleanup() {
    LOG.info("Nothing to cleanup");
  }
}
