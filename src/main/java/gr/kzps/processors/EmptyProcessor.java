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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Dummy processor for testing
 */
public class EmptyProcessor implements Processor {
  private final static Logger LOG = LogManager.getLogger(EmptyProcessor.class);
  
  @Override
  public void setConfiguration(Configuration configuration) {
    LOG.info("I am an empty processor, I don't need configuration");
  }
  
  @Override
  public void process() throws ZeppelinConfException {
    LOG.info("I am an empty processor, nothing to process");
  }
  
  @Override
  public void cleanup() {
    LOG.info("I am an empty processor, nothing to cleanup");
  }
}
