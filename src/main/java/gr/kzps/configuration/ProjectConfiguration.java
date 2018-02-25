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

import java.util.List;

/**
 * Class representing per Project Zeppelin configuration as it is stored in
 * the database
 */
public class ProjectConfiguration {
  private final int id;
  private final int projectId;
  private final String projectName;
  private String interpreterConfiguration;
  private List<String> lines;
  private boolean updated;
  
  /**
   * Constructor for Zeppelin configuration
   * @param id Configuration ID
   * @param projectId ID of the project associated with the configuration
   * @param projectName Name of the project
   * @param interpreterConfiguration Zeppelin interpreter configuration
   */
  public ProjectConfiguration(int id, int projectId, String projectName,
      String interpreterConfiguration) {
    this.id = id;
    this.projectId = projectId;
    this.projectName = projectName;
    this.interpreterConfiguration = interpreterConfiguration;
    updated = false;
  }
  
  public int getId() {
    return id;
  }
  
  public int getProjectId() {
    return projectId;
  }
  
  public String getProjectName() {
    return projectName;
  }
  
  public String getInterpreterConfiguration() {
    return interpreterConfiguration;
  }
  
  public void setInterpreterConfiguration(String interpreterConfiguration) {
    this.interpreterConfiguration = interpreterConfiguration;
  }
  
  /**
   * Get the configuration as a list of lines
   * @return
   */
  public List<String> getLines() {
    return lines;
  }
  
  /**
   * Set the configuration as a list of lines
   * @param lines
   */
  public void setLines(List<String> lines) {
    this.lines = lines;
  }
  
  /**
   * Set to true when there has been a change in the configuration
   */
  public void update() {
    updated = true;
  }
  
  public boolean isUpdated() {
    return updated;
  }
  
  @Override
  public String toString() {
    return "Conf ID: " + id + " Project ID: " + projectId + " Project name: "
        + projectName;
  }
}
