package gov.va.med.lom.javaUtils.misc;

import java.sql.*;

/**
 * <p>The <code>JdbcConnection</code> class provides a connection to a database via JDBC.</p>
 *
 * <p>Create an instance of  <code>JdbcConnection</code> by passing the database connection URL and
 * the fully-qualified driver class name.  Using an instance of <code>JdbcConnection</code>, an client
 * can connect to a database by simply calling the <code>connect</code> method passing the username and 
 * password. Queries and updates may then be executed on the connection by passing SQL statements
 * to the <code>doQuery</code> and <code>doUpdate</code> methods.</p>
 *
 * The following code creates a JDBC connection object, connects to a database, 
 * performs a query, and then disconnects:
 * <p><hr><blockquote><pre>
 *   try {
 *     JdbcConnection jdbcConnection = 
 *       new JdbcConnection("jdbc:jtds:sqlserver://DBSERVER;DatabaseName=contacts", 
 *                          "net.sourceforge.jtds.jdbc.Driver");
 *     jdbcConnection.connect("user", "pass");
 *     ResultSet rs = jdbcConnection.doQuery("select * on customers");
 *     while (rs.next()) {
 *       System.out.println(rs.getString("fname") + " " + rs.getString("lname"));
 *     }
 *   } catch(SQLException e) {
 *     // handle exception
 *   }
 * </pre></blockquote><hr>
 *
 * @author        Robert M. Durkin (rob.durkin@va.gov)<br>
 * @version       1.0
 * Date Created:  Feb 6, 2007
 * Date Modified: Dec 6, 2007
 */

public class JdbcConnection {

  /* FIELDS */
  private Connection conn;
  private Statement stmt;
  private String url;
  private String driver;

  /* CONSTRUCTORS */
  
  /**
   * Constructs an instance of JdbcConnection with no connection params.
   */   
  public JdbcConnection() {
    conn = null;
    stmt = null;
    url = null;
    driver = null;
  }
  
  /**
   * Constructs an instance of JdbcConnection with a connection URL and driver class.
   *
   * @param url  the database connection URL
   * @param driver  the fully-qualified driver class
   * @throws Exception
   */   
  public JdbcConnection(String url, String driver) throws Exception {
    this();
    setUrl(url);
    setDriver(driver);
  }

  /* PUBLIC METHODS */
  
  /**
   *  Connect to the database and logon with the specified username and password.
   *  @param username the account username
   *  @param password the account password
   */
  public void connect(String username, String password) throws SQLException {
    conn = DriverManager.getConnection(url, username, password);
    stmt = conn.createStatement();
  }
  
  /**
   *  Disconnect from the database.
   */
  public void disconnect() {
    try {
      if (conn != null)
        conn.close();
    } catch(SQLException e) {
      // ignore
    }
  }
  
  /**
   *  Retrieves a prepared statement for the given sql.
   *  
   *  @param sql the SQL statement to prepare
   *  @return  the prepared statement
   */
  public PreparedStatement getPreparedStatement(String sql) throws SQLException { 
    return conn.prepareStatement(sql);
  }
  
  /**
   *  Executes the given SQL statement, which may be an INSERT, UPDATE, or DELETE.
   *  
   *  @param sql the SQL statement to execute
   *  @return  the number of rows updated or -1 if no success
   */
  public int doUpdate(String sql) throws SQLException {
    return stmt.executeUpdate(sql);
  }
  
  /**
   *  Executes the given prepared statement, which may be an INSERT, UPDATE, or DELETE.
   *  
   *  @param sql the prepared statement to execute
   *  @return  the number of rows updated or -1 if no success
   */
  public int doUpdate(PreparedStatement pstmt) throws SQLException {
    return pstmt.executeUpdate();
  }

  /**
   *  Executes the given SQL statement.
   *  
   *  @param sql the SQL statement to execute
   *  @return  a single ResultSet object
   */
  public ResultSet doQuery(String sql) throws SQLException {
    return stmt.executeQuery(sql);
  }
  
  /**
   *  Executes the given prepared statement.
   *  
   *  @param pstmt the prepared statement to execute
   *  @return  a single ResultSet object
   */
  public ResultSet doQuery(PreparedStatement pstmt) throws SQLException {
    return pstmt.executeQuery();
  }
  
  /* PROPERTY ACCESSORS */  
 
  /**
   *  Get the database driver class name.
   *  
   *  @return  the driver class name
   */ 
  public String getDriver() {
    return driver;
  }
  
  /**
   * Set the database driver class name.
   * 
   * @param  the fully-qualified driver class
   */
  public void setDriver(String driver) throws Exception {
    this.driver = driver;
    Class.forName(driver).newInstance();
  }
  
  /** 
   *  Get the database URL.
   *  
   *  @return  the database connection URL
   */
  public String getUrl() {
    return url;
  }
  
  /**
   * Set the database URL.
   * 
   * @param  the database connection URL
   */
  public void setUrl(String url) {
    this.url = url;
  }
  
  /** 
   *  Get the Connection object.
   *  
   *  @return  the Connection object
   */
  public Connection getConnection() {
    return conn;
  }
  
}


