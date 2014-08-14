package gov.va.med.lom.javaUtils.classloader;

import java.io.*;
import java.sql.*;

public class ClassLoaderUtil {

  public static String FILE_SYSTEM_CLASS_LOADER = "FILE_SYSTEM_CLASS_LOADER";
  public static String HASH_TABLE_CLASS_LOADER = "HASH_TABLE_CLASS_LOADER";
  public static String JDBC_CLASS_LOADER = "JDBC_CLASS_LOADER";
  public static String SOCKET_CLASS_LOADER = "SOCKET_CLASS_LOADER";
  public static String JAR_CLASS_LOADER = "JAR_CLASS_LOADER";

  public static ClassLoaderStrategy getClassLoader(String strategy, String arg) throws Exception {
    String[] args = {arg};
    return getClassLoader(strategy, args);
  }

  public static ClassLoaderStrategy getClassLoader(String strategy, String[] args) throws Exception {

    ClassLoaderStrategy cl = null;
    if (strategy.equals(FILE_SYSTEM_CLASS_LOADER)) {

      /*
       * args[0]: Source root directory
       */
      try {
        if (args.length == 0)
          cl = new FileSystemClassLoader();
        else
          cl = new FileSystemClassLoader(args[0]);
      } catch (Exception e) {
        System.err.println("Unable to find directory " + args[0]);
      }

    } else if (strategy.equals(HASH_TABLE_CLASS_LOADER)) {

      /*
       * args[0]..args[n-1]: Classes to load into hashtable
       */
        cl = new HashtableClassLoader();
        for (int i = 0; i < args.length; i++) {
          try {
            FileInputStream fis = new FileInputStream(args[i]);
            int ct = fis.available();
            byte[] bytes = new byte[ct];
            fis.read(bytes);
            ((HashtableClassLoader)cl).putClass(args[i], bytes);
          } catch (Exception e) {
            System.err.println("Unable to find file " + args[i]);
            throw e;
          }
        }

    } else if (strategy.equals(JDBC_CLASS_LOADER)) {

      /*
       * args[0]: JDBC driver (e.g. "com.mysql.jdbc.Driver")
       * args[1]: connection URL (e.g. "jdbc:mysql://localhost/test?user=blah&password=blah")
       * args[2]: SQL to retrieve class bytecode (e.g. "SELECT bytecode FROM class_tbl
       *                                          WHERE classname = 'MyClass'")
       */
      // Load the JDBC driver
      try {
        Class.forName(args[0]).newInstance();
      } catch (Exception e) {
        System.err.println("Unable to load DB driver.");
        e.printStackTrace();
        throw e;
      }

      try {
        // Do the JDBC Connection
        java.util.Properties p = new java.util.Properties();
        Connection conn = DriverManager.getConnection(args[1], p);

        // Create the ClassLoader around the SQL statement
        // to retrieve the bytecode
        cl = new JDBCClassLoader(conn, args[2]);
      } catch (SQLException e) {
        System.err.println("SQLException: " + e.getMessage());
        System.err.println("SQLState:     " + e.getSQLState());
        System.err.println("VendorError:  " + e.getErrorCode());
        throw e;
      }

    } else if (strategy.equals(SOCKET_CLASS_LOADER)) {

      /*
       * args[0]: Host address
       * args[1]: Port
       */
      cl = new SocketClassLoader(args[0], Integer.valueOf(args[1]).intValue());

    } else if (strategy.equals(JAR_CLASS_LOADER)) {

      /*
       * args[0]: Jar file path
       */
      try {
        cl = new JarClassLoader(args[0]);
      } catch (Exception e) {
        System.err.println("Unable to find directory " + args[0]);
        throw e;
      }

    }
    return cl;
  }

  public static void main(String[] args) {
    try {
      ClassLoaderStrategy cl;
      cl = ClassLoaderUtil.getClassLoader(JAR_CLASS_LOADER, args);
      Class c = ((ClassLoader)cl).loadClass("gov.va.med.lom.javaUtils.FileSystemClassLoader");
      System.out.println(c.toString());
    } catch(Exception e) {
      e.getMessage();
    }
  }
}