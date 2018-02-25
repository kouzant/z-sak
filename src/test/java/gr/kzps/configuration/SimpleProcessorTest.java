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

import gr.kzps.processors.Processor;
import org.apache.commons.configuration2.Configuration;

public class SimpleProcessorTest implements Processor {
  private boolean configurationSet = false;
  private boolean processCalled = false;
  private boolean cleanupCalled = false;
  
  @Override
  public void setConfiguration(Configuration configuration) {
    configurationSet = true;
  }
  
  @Override
  public void process() {
    processCalled = true;
  }
  
  @Override
  public void cleanup() {
    cleanupCalled = true;
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
