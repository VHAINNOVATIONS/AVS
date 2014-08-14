package gov.va.med.lom.javaUtils.misc;

import java.sql.*;
import java.io.*;

public class AccessJdbcConnector {

  private static final String accessDBURLPrefix = "jdbc:odbc:Driver={Microsoft Access Driver (*.mdb)};DBQ=";
  private static final String accessDBURLSuffix = ";DriverID=22;READONLY=true}";
  
  static {
    // Load Sun JDBC/ODBC Bridge Driver
    try {
      Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
    } catch(ClassNotFoundException e) {
      System.err.println("JdbcOdbc Bridge Driver not found!");
    }
    // Load ObjectWeb RMI/JDBC Driver
    try {
      Class.forName("org.objectweb.rmijdbc.Driver");
    } catch(ClassNotFoundException e) {
      System.err.println("ObjectWeb RMI/JDBC Driver not found!");
    }          
  }
  
  /** 
   * Creates a connection to a Access database on a Windows machine 
   */
  public static java.sql.Connection getWinAccessDBConnection(String filename) throws SQLException {
    filename = filename.replace('\\', '/').trim();
    String databaseURL = accessDBURLPrefix + filename + accessDBURLSuffix;
    return DriverManager.getConnection(databaseURL, "", "");
  }  
  
  /** 
   * Creates a connection to a Access database from a non-Windows machine via RMI 
   */
  public static java.sql.Connection getRmiAccessDBConnection(String rmiHost, String filename) throws SQLException {
    String databaseURL = "jdbc:rmi://" + rmiHost + "/" + accessDBURLPrefix + filename + accessDBURLSuffix;
    return DriverManager.getConnection(databaseURL, "", "");
  }  
  
  /** 
   * Prints ResultSet to PrintStream 
   */
  public static void printResultSet(PrintStream p, ResultSet rs, String title) throws SQLException {
    if(rs != null) {
      ResultSetMetaData metaData = rs.getMetaData();
      int cols = metaData.getColumnCount();
      p.println("\n--------------------------\n" + title + "\n--------------------------");
      for(int i = 1;i <= cols;i++) {
        p.print(metaData.getColumnLabel(i) + "\t");
      }
      p.println("\n--------------------------");
      int count = 0;
      while(rs.next()) {
        for(int i = 1;i <= cols;i++) {
          p.print(rs.getString(i) + "\t");
        }
        p.println("\n--------------------------");
        count++;
      }
      p.println("Rows: " + count);
       }
    } 
 
    /*
   * Connects to an Access database using a local JDBC/ODBC connection or via an RMI server. 
   */
  public static void main(String args[]) {
    if((args.length < 2) || (args.length > 3)) {
      System.out.println("Example: java AccessJdbcConnector [rmi_host] database_name.mdb \"database query\"");
      System.exit(1);
    }
    java.sql.Connection conn = null;
    String sql = null;
    try {
      if (args.length == 2) {
        conn = AccessJdbcConnector.getWinAccessDBConnection(args[0]);
        sql = args[1];
      } else { 
        conn = AccessJdbcConnector.getRmiAccessDBConnection(args[0], args[1]);
        sql = args[2];
      }
      Statement stmt = conn.createStatement();
      if (stmt.execute(sql)) {
        printResultSet(System.out, stmt.getResultSet(), "Query Result");
      } else {
        System.out.println("DDL executed successfully");
      }
    } catch(SQLException s) {
      System.out.println(s);
    } finally {
      if(conn != null) {
        try {
          conn.close();
        } catch(SQLException ignore) {}
      }
    }
 }
     
}
