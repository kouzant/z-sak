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
package gr.kzps.DbConnection;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Connection factory for MySQL DBMS
 */
public class MySQLConnectionFactory implements ConnectionFactory {
  private final static Logger LOG = LogManager.getLogger
      (MySQLConnectionFactory.class);
  
  @Override
  public Connection createConnection(DatabaseConnectionFactory.Builder builder)
      throws SQLException {
    Properties connectionProps = new Properties();
    connectionProps.put("user", builder.getUser());
    connectionProps.put("password", builder.getPassword());
    connectionProps.put("useSSL", builder.getSSL());
  
    LOG.debug("Connection string: {}@jdbc:mysql://{}:{}/{}", builder.getUser(),
        builder.getHost(), builder.getPort(), builder.getSchema());
    return DriverManager.getConnection(
        "jdbc:mysql://" + builder.getHost() + ":" + builder.getPort() + "/" +
            builder.getSchema() + "?serverTimezone=UTC", connectionProps);
  }
}
