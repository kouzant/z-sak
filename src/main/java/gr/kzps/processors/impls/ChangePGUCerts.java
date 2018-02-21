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
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ChangePGUCerts extends ProcessorAbstr {
  private final Logger LOG = LogManager.getLogger(ChangePGUCerts.class);
  
  private final List<String> projectNames;
  private final String PROJECT_GENERIC_USER = "__PROJECTGENERICUSER";
  
  /*
  <processors>
    <processor config-class="gr.kzps.processors.impls.ChangePGUCerts">
      <config-constrarg config-value="projectA,projectB,projectC"/>
    </processor>
  </processors>
   */
  public ChangePGUCerts(String projectNames) {
    this.projectNames = Arrays.asList(projectNames.split(","));
  }
  
  @Override
  public void process() throws ZeppelinConfException {
    try {
      LOG.info("Filtering projects");
      List<ProjectConfiguration> projectConfs = filterProjects();
      LOG.info("Filtered {} projects", projectConfs.size());
      projectConfs.stream()
          .forEach(pc -> LOG.debug("Fix project conf: {}", pc));
      LOG.info("Updating configurations");
      List<ProjectConfiguration> updatedConfs = fixPGUCerts(projectConfs);
      LOG.info("Updated {} configurations", updatedConfs.size());
      LOG.info("Persisting in DB");
      updateDatabase(updatedConfs);
      LOG.info("Finished persisting in DB");
    } catch (SQLException ex) {
      LOG.error("Error in process", ex);
      throw new ZeppelinConfException("Error in process", ex);
    }
  }
  
  @Override
  public void cleanup() {
    LOG.info("Nothing to cleanup");
  }
  
  private List<ProjectConfiguration> fixPGUCerts(List<ProjectConfiguration>
      projectConfs) {
    LOG.debug("Fixing...");
    return projectConfs.stream().filter(pc -> {
      String predicate = pc.getProjectName() + PROJECT_GENERIC_USER;
      return !pc.getInterpreterConfiguration().contains(predicate);
    }).map(pc -> {
      String oneLine = pc.getInterpreterConfiguration();
      List<String> lines = Arrays.asList(oneLine.split("\\r?\\n"));
      pc.setLines(lines);
      return pc;
    })
        .map(this::changeToPGU)
        .filter(pc -> pc.isUpdated())
        .collect(Collectors.toList());
  }
  
  private ProjectConfiguration changeToPGU(ProjectConfiguration projectConf) {
    String pgu = projectConf.getProjectName() + PROJECT_GENERIC_USER;
    StringBuilder newConf = new StringBuilder();
    projectConf.getLines().stream()
        .forEach(l -> {
          // Replace path to HDFS and certificates filename
          if (l.contains("k_certificate")) {
            LOG.debug("Replacing local resources");
            l = l.replaceAll(projectConf.getProjectName(), pgu);
            projectConf.update();
          }
          
          // Add material password if missing
          if (l.contains("k_certificate")
              && !l.contains("material_passwd")) {
            LOG.debug("Adding material_passwd");
            l = l.substring(0, l.length() - 2);
            l = l + ",hdfs:///user/glassfish/kafkacerts/" + pgu + "/" + pgu + "__cert.key#material_passwd" + "\",";
            projectConf.update();
          }
          
          // HDFS_USER_NAME and HDFS_USER values
          if (l.contains("\"value\": \"" + projectConf.getProjectName() + "\"")) {
            LOG.debug("Changing HDFS_USER_NAME and HDFS_USER");
            l = l.replaceAll(projectConf.getProjectName(), pgu);
            projectConf.update();
          }
          
          // -Dhopsworks.projectuser
          if (l.contains("-Dhopsworks.projectuser")) {
            LOG.debug("Replacing hopsworks.projectuser");
            l = l.replaceAll("-Dhopsworks.projectuser=" + projectConf.getProjectName(), "-Dhopsworks.projectuser=" +
                projectConf.getProjectName() + PROJECT_GENERIC_USER);
            projectConf.update();
          }
          
          newConf.append(l).append("\n");
        });
    projectConf.setInterpreterConfiguration(newConf.toString());
    return projectConf;
  }
  
  private List<ProjectConfiguration> filterProjects() throws SQLException {
    List<ProjectConfiguration> allProjects = getAllZeppelinConfs();
    List<ProjectConfiguration> filtered = allProjects.stream()
        .filter(pc -> projectNames.contains(pc.getProjectName()))
        .collect(Collectors.toList());
    return filtered;
  }
}
