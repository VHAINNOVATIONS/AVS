package gov.va.med.lom.javaUtils.classloader;

import java.net.URL;
import java.sql.*;
import java.util.Enumeration;

public class JDBCClassLoader extends ClassLoader implements ClassLoaderStrategy {

  // Internal members
  private Connection connection;
  private String sql;

  /*
   * Constructor.
   *
   * The SQL statement must return at least one row, the first
   * column of which will be a BINARY column, and must contain a
   * ? where the name of the fully-qualified classname will appear.
   * Example:
   * "SELECT bytecode FROM class_tbl WHERE class_tbl.name = ?"
   */
  public JDBCClassLoader(Connection conn, String sql) {
    this(JDBCClassLoader.class.getClassLoader(), conn, sql);
  }

  /*
   * Constructor.
   *
   * The SQL statement must return at least one row, the first
   * column of which will be a BINARY column, and must contain a
   * ? where the name of the fully-qualified classname will appear.
   * Example:
   * "SELECT bytecode FROM class_tbl WHERE class_tbl.name = ?"
   */
  public JDBCClassLoader(ClassLoader parent, Connection connection, String sql) {
    // Set parent ClassLoader
    //
    super(parent);

    // Store the JDBC settings
    //
    this.connection = connection;
    this.sql = new String(sql);
  }

  /*
   * Return byte array (which will be turned into a Class instance
   * via ClassLoader.defineClass) for class
   */
  public byte[] findClassBytes(String className) {
    byte[] classBytes = retrieveClass(className);
    return classBytes;
  }

  /*
   * Return URL for resource given by resourceName
   */
  public URL findResourceURL(String resourceName) {
    return null;
  }

  /*
   * Return Enumeration of resources corresponding to resourceName.
   */
  public Enumeration findResourcesEnum(String resourceName) {
    return null;
  }

  /*
   * Return full path to native library given by the name libraryName.
   */
  public String findLibraryPath(String libraryName) {
    return null;
  }

  /*
   * Called by ClassLoader.loadClass when a classname is requested.
   */
  public Class findClass(String className) throws ClassNotFoundException {
    byte[] classBytes = findClassBytes(className);
    if (classBytes==null) {
      throw new ClassNotFoundException();
    }

    return defineClass(className, classBytes, 0, classBytes.length);
  }

  /*
   * Internal method to do the actual SQL-retrieval of the bytecode
   */
  private byte[] retrieveClass(String className) {
    try {
      // Create a SQL Statement
      Statement stmt = null;
      stmt = this.connection.createStatement();

      // Build our SQL statement
      String pre = this.sql.substring(0, this.sql.indexOf("?"));
      String post = this.sql.substring(this.sql.indexOf("?")+1, this.sql.length());
      String sql = pre + className + post;

      // Do the query
      ResultSet rs = stmt.executeQuery(sql);
      if (rs.next()) {
        byte[] bytes = rs.getBytes(1);
        return bytes;
      }
      else
        return null;
    }
    catch (Exception ex) {
      ex.printStackTrace();
      return null;
    }
  }

  public String[] getClassNames() {
    return null; // not yet implemented
  }

  public Class[] getClasses(String classPath) throws ClassNotFoundException {
    return null; // not yet implemented
  }
  
  public Class[] getClasses(String sourcePath, String classPath) throws ClassNotFoundException {
    return null; // not yet implemented
  }  

  /*
   * Test driver routine; assumes an IDB database with the following schema:
   * CREATE TABLE class_tbl (
   *   bytecode binary,
   *   classname varchar(80) primary key
   * );
   */
  public static void main(String[] args) throws Exception {
    // Load the IDB driver
    Class.forName("jdbc.idbDriver").newInstance();

    // Do the JDBC Connection
    java.util.Properties p = new java.util.Properties();
    Connection conn = DriverManager.getConnection("jdbc:idb:sample.prp", p);

    // Create the ClassLoader around the SQL statement to
    // retrieve the bytecode
    JDBCClassLoader jdbcClassLoader = new JDBCClassLoader(conn,
                                          "SELECT bytecode FROM class_tbl " +
                                          "WHERE classname = '?'");

    Class cls = jdbcClassLoader.loadClass("Hello");
    Object h = cls.newInstance();
    // Should print "Hello, world!"
  }
}