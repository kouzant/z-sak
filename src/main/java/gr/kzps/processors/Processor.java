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
package gr.kzps.processors;

import gr.kzps.exceptions.ZeppelinConfException;
import org.apache.commons.configuration2.Configuration;

/**
 * Interface for every processor
 */
public interface Processor {
  /**
   * Sets z-sak configuration
   * @param configuration Configuration object
   */
  void setConfiguration(Configuration configuration);
  
  /**
   * Method called to perform the update to Zeppelin configurations
   * @throws ZeppelinConfException
   */
  void process() throws ZeppelinConfException;
  
  /**
   * Method called after process to perform any necessary cleanup
   */
  void cleanup();
}
