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

/**
 * Static configuration names
 */
public class ZsakConfiguration {
  private static final String DATABASE_PREFIX = "database.";
  public static final String DATABASE_DBMS_KEY = DATABASE_PREFIX + "dbms";
  public static final String DATABASE_DBMS_DEFAULT = "mysql";
  
  public static final String DATABASE_HOST_KEY = DATABASE_PREFIX + "host";
  public static final String DATABASE_PORT_KEY = DATABASE_PREFIX + "port";
  public static final String DATABASE_USER_KEY = DATABASE_PREFIX + "user";
  public static final String DATABASE_PASSWORD_KEY = DATABASE_PREFIX + "password";
  
  public static final String DATABASE_SCHEMA_KEY = DATABASE_PREFIX + "schema";
  public static final String DATABASE_SCHEMA_DEFAULT = "hopsworks";
  
  public static final String DATABASE_SSL_KEY = DATABASE_PREFIX + "ssl";
  public static final String DATABASE_SSL_DEFAULT = "false";
  
  private static final String ZEPPELIN_TABLE_PREFIX = "zeppelin_table.";
  public static final String ZEPPELIN_TABLE_NAME_KEY = ZEPPELIN_TABLE_PREFIX + "name";
  public static final String ZEPPELIN_TABLE_NAME_DEFAULT =
      "zeppelin_interpreter_confs";
  
  public static final String ZEPPELIN_CONF_ID_NAME_KEY = ZEPPELIN_TABLE_PREFIX +
      "conf_id_name";
  public static final String ZEPPELIN_CONF_ID_NAME_DEFAULT = "id";
  
  public static final String ZEPPELIN_CONF_PROJECT_ID_NAME_KEY = ZEPPELIN_TABLE_PREFIX +
      "project_id_name";
  public static final String ZEPPELIN_CONF_PROJECT_ID_NAME_DEFAULT =
      "project_id";
  
  public static final String ZEPPELIN_CONF_LAST_UPDATE_NAME_KEY = ZEPPELIN_TABLE_PREFIX +
      "last_update_name";
  public static final String ZEPPELIN_CONF_LAST_UPDATE_NAME_DEFAULT =
      "last_update";
  
  public static final String ZEPPELIN_CONF_INTERPRETER_CONF_NAME_KEY =
      ZEPPELIN_TABLE_PREFIX + "interpreter_conf_name";
  public static final String ZEPPELIN_CONF_INTERPRETER_CONF_NAME_DEFAULT =
      "interpreter_conf";
  
  private static final String PROJECT_TABLE_PREFIX = "project_table.";
  public static final String PROJECT_TABLE_NAME_KEY = PROJECT_TABLE_PREFIX +
      "name";
  public static final String PROJECT_TABLE_NAME_DEFAULT = "project";
  
  public static final String PROJECT_ID_NAME_KEY = PROJECT_TABLE_PREFIX +
      "id_name";
  public static final String PROJECT_ID_NAME_DEFAULT = "id";
  
  public static final String PROJECT_NAME_NAME_KEY = PROJECT_TABLE_PREFIX +
      "project_name_name";
  public static final String PROJECT_NAME_NAME_DEFAULT = "projectname";
  
  public static final String PROCESSOR_CLASS_NAME_KEY = "processors.processor";
}
