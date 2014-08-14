/*
 * DatabaseConnection.java
 * 
 * Author: Rob Durkin (rob.durkin@med.va.gov)
 *  
 * Encapsulates a connection to a database.
 *
 */

package gov.va.med.lom.javaUtils.misc; 

import java.sql.*;
import java.io.Serializable;
import java.util.Properties;

public class DatabaseConnection implements Serializable {

  /* FIELDS */
  private java.sql.Statement stmt;
  private String url;
  private String dbDriver;
  private String prefix;
  private String serverHost;
  private String database;
  private String username;
  private String password;
  private Properties dbProperties;
  private int connectionMethod;

  /* CONSTRUCTORS */
  public DatabaseConnection() {
    super();
    connectionMethod = 0;
  }
  
  public DatabaseConnection(String dbDriver) throws Exception {
    this();
    setDbDriver(dbDriver);
  }

  /* PUBLIC METHODS */
  public void initialize(String url) throws SQLException {
    this.connectionMethod = 1;
    this.url = url;
    stmt = doCreateStatement(url);
  }
  
  public void initialize() throws SQLException {
    this.connectionMethod = 2;
    this.url = prefix + "://" + serverHost + "/" + database;
    stmt = doCreateStatement(url, username, password);
  }  

  public void initialize(String url, String username, 
                         String password) throws SQLException {
    this.connectionMethod = 2;
    this.url = url;
    this.username = username;
    this.password = password;
    stmt = doCreateStatement(url, username, password);
  }

  public void initialize(String url, String username, 
                         String password, String database) throws SQLException {
    this.connectionMethod = 2;
    this.url += "/" + database;
    this.username = username;
    this.password = password;
    this.database = database;
    stmt = doCreateStatement(url, username, password);
  }
  
  public void initialize(String prefix, String serverHost, String database,
                         String username, String password) throws SQLException {
    this.prefix = prefix;
    this.serverHost = serverHost;
    this.database = database;
    this.username = username;
    this.password = password;
    initialize();
  }

  public void initialize(String prefix, String serverHost,
                         String database, Properties dbProperties) throws SQLException {
    this.connectionMethod = 3;
    this.url = prefix + "://" + serverHost + "/" + database;
    this.prefix = prefix;
    this.serverHost = serverHost;
    this.database = database;
    this.dbProperties = dbProperties;
    stmt = doCreateStatement(url, dbProperties);
  }
  
  public boolean logoff() throws SQLException {
    if (this.stmt != null) {
      Connection conn = stmt.getConnection();
      if (conn != null) {
        conn.close();
        return true;
      }
    }
    return false;      
  }

  /**
   *  Executes the given SQL statement, which may be an INSERT, UPDATE, or DELETE.
   *  Returns the number of rows updated or -1 if no success.
   */
  public int doUpdate(String sql) throws SQLException {
    if (checkConnection()) 
      return stmt.executeUpdate(sql);
    else
      return 0;
  }

  /**
   *  Executes the given SQL statement.
   *  Returns a single ResultSet object.
   */
  public ResultSet doQuery(String sql) throws SQLException {
    if (checkConnection())
      return stmt.executeQuery(sql);
    else
      return null;
  }
  
  /* PROPERTY ACCESSORS */
  public String getDatabase() {
    return database;
  }
  
  public void setDatabase(String database) {
    this.database = database;
  }
  
  public String getDbDriver() {
    return dbDriver;
  }
  
  public void setDbDriver(String dbDriver) throws Exception {
    this.dbDriver = dbDriver;
    Class.forName(dbDriver).newInstance();
  }
  
  public String getPassword() {
    return password;
  }
  
  public void setPassword(String password) {
    this.password = password;
  }
  
  public String getPrefix() {
    return prefix;
  }
  
  public void setPrefix(String prefix) {
    this.prefix = prefix;
  }
  
  public String getServerHost() {
    return serverHost;
  }
  
  public void setServerHost(String serverHost) {
    this.serverHost = serverHost;
  }
  
  public String getUsername() {
    return username;
  }
  
  public void setUsername(String username) {
    this.username = username;
  }
  
  /* PRIVATE METHODS */
  private boolean checkConnection() throws SQLException {
    if (this.stmt != null) {
      Connection conn = stmt.getConnection();
      if (conn.isClosed()) {
        if (connectionMethod == 1)
          this.stmt = doCreateStatement(this.url);
        else if (connectionMethod == 2)
          this.stmt = doCreateStatement(this.url, this.username, this.password);
        else if (connectionMethod == 3)
          this.stmt = doCreateStatement(this.url, this.dbProperties);
        if ((this.stmt == null) || (stmt.getConnection().isClosed()))
          return false;
      }
      return true;
    }
    return false;
  }
  
  private static Statement doCreateStatement(String url) throws SQLException {
    Connection conn = DriverManager.getConnection(url);
    Statement stmt = conn.createStatement();
    return stmt;
  }
  
  private static Statement doCreateStatement(String url, String username, String password) throws SQLException {
    Connection conn = DriverManager.getConnection(url, username, password);
    Statement stmt = conn.createStatement();
    return stmt;
  }
  
  private static Statement doCreateStatement(String url, Properties dbProperties) throws SQLException {
    Connection conn = DriverManager.getConnection(url, dbProperties);
    Statement stmt = conn.createStatement();
    return stmt;
  }  
  
}



