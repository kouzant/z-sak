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

import java.util.List;

public class ProjectConfiguration {
  private final int id;
  private final int projectId;
  private final String projectName;
  private String interpreterConfiguration;
  private List<String> lines;
  private boolean updated;
  
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
  
  public List<String> getLines() {
    return lines;
  }
  
  public void setLines(List<String> lines) {
    this.lines = lines;
  }
  
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
