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

import gr.kzps.processors.ProcessorAbstr;
import org.apache.commons.configuration2.Configuration;

import java.sql.Connection;

public class DbProcessorTest extends ProcessorAbstr {
  private boolean connectionSet = false;
  private boolean configurationSet = false;
  private boolean processCalled = false;
  private boolean cleanupCalled = false;
  
  @Override
  public void setConnection(Connection connection) {
    connectionSet = true;
  }
  
  @Override
  public void setConfiguration(Configuration configuration) {
    configurationSet = true;
    super.setConfiguration(configuration);
  }
  
  @Override
  public void process() {
    processCalled = true;
  }
  
  @Override
  public void cleanup() {
    cleanupCalled = true;
  }
  
  public Connection getConnection() {
    return super.getConnection();
  }
  
  public boolean isConnectionSet() {
    return connectionSet;
  }
  
  public boolean isConfigurationSet() {
    return configurationSet;
  }
  
  public boolean isProcessCalled() {
    return processCalled;
  }
  
  public boolean isCleanupCalled() {
    return cleanupCalled;
  }
}
