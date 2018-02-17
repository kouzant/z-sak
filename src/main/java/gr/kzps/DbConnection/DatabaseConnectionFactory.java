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

public class DatabaseConnectionFactory {
  private final static Logger LOG = LogManager.getLogger
      (DatabaseConnectionFactory.class);
  
  public static Builder builder() {
    return new Builder();
  }
  
  private static Connection createConnection(Builder builder) throws
      SQLException {
    Properties connectionProps = new Properties();
    connectionProps.put("user", builder.user);
    connectionProps.put("password", builder.password);
    
    LOG.debug("Connection string: {}@jdbc:mysql://{}:{}/{}", builder.user,
        builder.host, builder.port, builder.schema);
    return DriverManager.getConnection(
        "jdbc:mysql://" + builder.host + ":" + builder.port + "/" + builder
            .schema, connectionProps);
  }
  
  public static class Builder {
    private String host;
    private int port;
    private String user;
    private String password;
    private String schema;
    
    public Builder setHost(String host) {
      this.host = host;
      return this;
    }
    
    public Builder setPort(int port) {
      this.port = port;
      return this;
    }
    
    public Builder setUser(String user) {
      this.user = user;
      return this;
    }
    
    public Builder setPassword(String password) {
      this.password = password;
      return this;
    }
    
    public Builder setSchema(String schema) {
      this.schema = schema;
      return this;
    }
    
    public Connection build() throws SQLException {
      return createConnection(this);
    }
  }
}
