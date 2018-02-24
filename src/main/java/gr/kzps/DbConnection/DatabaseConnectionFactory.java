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
import java.sql.SQLException;

/**
 * Factory for database connections
 */
public class DatabaseConnectionFactory {
  private final static Logger LOG = LogManager.getLogger
      (DatabaseConnectionFactory.class);
  
  /**
   * Helper class to create a connection to a database
   * @param dbms DBMS type
   * @return Builder to create the connection
   */
  public static Builder builder(String dbms) {
    return new Builder(dbms);
  }
  
  public static class Builder {
    private final String dbms;
    private String host;
    private int port;
    private String user;
    private String password;
    private String schema;
    private String ssl;
  
    public Builder(String dbms) {
      this.dbms = dbms;
    }
    
    public String getHost() {
      return host;
    }
  
    public int getPort() {
      return port;
    }
  
    public String getUser() {
      return user;
    }
  
    public String getPassword() {
      return password;
    }
  
    public String getSchema() {
      return schema;
    }
    
    public String getSSL() {
      return ssl;
    }
  
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
    
    public Builder setSSL(String ssl) {
      this.ssl = ssl;
      return this;
    }
    
    public Connection build() throws SQLException {
      ConnectionFactory connectionFactory;
      if (dbms.equals("mysql")) {
        LOG.debug("Creating MySQL connection");
        connectionFactory = new MySQLConnectionFactory();
        return connectionFactory.createConnection(this);
      } else if (dbms.equals("none")) {
        LOG.debug("Creating None connection");
        connectionFactory = new NoneConnectionFactory();
        return connectionFactory.createConnection(this);
      }
      
      throw new SQLException("Unsupported DBMS " + dbms);
    }
  }
}
