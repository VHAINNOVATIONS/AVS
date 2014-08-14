package gov.va.med.lom.avs.util;

import java.io.*;
import java.util.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;

import gov.va.med.lom.javaUtils.misc.JdbcConnection;
import gov.va.med.lom.javaUtils.misc.StringUtils;

public class FacilityHealthFactorsConfig {

  static String DB_USER;
  static String DB_PASS;
  static String DB_DRIVER;
  static String DB_URL;
  
  static {
    ResourceBundle res = ResourceBundle.getBundle("gov.va.med.lom.avs.db");
    String dbServer = res.getString("db.server");
    String dbPort = res.getString("db.port");
    String dbName = res.getString("db.name");
    String dbDriverType = res.getString("db.drivertype");
    String dbDatabase = res.getString("db.database");
    DB_USER = res.getString("db.username");
    DB_PASS = res.getString("db.password");
    DB_DRIVER = res.getString("db.driver");
    DB_URL = "jdbc:" + dbDriverType + ":" + dbDatabase + "://" + 
             dbServer + ":" + dbPort + ";DatabaseName=" + dbName;
  }
  
  public static void main(String[] args) throws IOException {

    String stationNo = "691";
    List<String> healthFactors = new ArrayList<String>();
    healthFactors.add("language^534226^ENGLISH");
    healthFactors.add("language^321^OTHER LANGUAGE");
    healthFactors.add("language^693650^ENGLISH");
    healthFactors.add("language^693649^OTHER");
    healthFactors.add("language^694485^UNABLE TO DESIGNATE");
    healthFactors.add("smoking^1^");
    healthFactors.add("smoking^33^");
    healthFactors.add("smoking^37^");
    healthFactors.add("smoking^38^");
    healthFactors.add("smoking^39^");
    healthFactors.add("smoking^523285^");
    healthFactors.add("smoking^534148^");
    healthFactors.add("smoking^549003^");
    healthFactors.add("smoking^549170^");
    healthFactors.add("smoking^557003^");
    healthFactors.add("smoking^557067^");
    healthFactors.add("smoking^557155^");
    healthFactors.add("smoking^558008^");
    healthFactors.add("smoking^558015^");
    healthFactors.add("smoking^558118^");
    healthFactors.add("smoking^558119^");
    healthFactors.add("smoking^578068^");
    healthFactors.add("smoking^578069^");
    healthFactors.add("smoking^580001^");
    healthFactors.add("smoking^580145^");
    healthFactors.add("smoking^580251^");
    healthFactors.add("smoking^580284^");
    healthFactors.add("smoking^593170^");
    healthFactors.add("smoking^612120^");
    healthFactors.add("smoking^612191^");
    healthFactors.add("smoking^612601^");
    healthFactors.add("smoking^612602^");
    healthFactors.add("smoking^612603^");
    healthFactors.add("smoking^612604^");
    healthFactors.add("smoking^612728^");
    healthFactors.add("smoking^615315^");
    healthFactors.add("smoking^615316^");
    healthFactors.add("smoking^619323^");
    healthFactors.add("smoking^648166^");
    healthFactors.add("smoking^650074^");
    healthFactors.add("smoking^650075^");
    healthFactors.add("smoking^650076^");
    healthFactors.add("smoking^650126^");
    healthFactors.add("smoking^664127^");
    healthFactors.add("smoking^671209^");
    healthFactors.add("smoking^691045^");
    healthFactors.add("smoking^691046^");
    healthFactors.add("smoking^691047^");
    healthFactors.add("smoking^691049^");
    healthFactors.add("smoking^691050^");
    healthFactors.add("smoking^691154^");
    healthFactors.add("smoking^692608^");
    healthFactors.add("smoking^692610^");
    healthFactors.add("smoking^692611^");
    healthFactors.add("smoking^692612^");
    healthFactors.add("smoking^692613^");
    healthFactors.add("smoking^693797^");
    healthFactors.add("smoking^693798^");
    healthFactors.add("smoking^693799^");
    healthFactors.add("smoking^693800^");
    healthFactors.add("smoking^693801^");
    healthFactors.add("smoking^695129^");
    
    try {
      JdbcConnection jdbcConnection = new JdbcConnection(DB_URL, DB_DRIVER);
      jdbcConnection.connect(DB_USER, DB_PASS);
      
      StringBuilder sql = new StringBuilder("INSERT INTO ckoHealthFactors (stationNo, type, ien, value) VALUES (?,?,?,?)");
      
      Connection connection = jdbcConnection.getConnection();
      PreparedStatement pstmt = connection.prepareStatement(sql.toString(), Statement.RETURN_GENERATED_KEYS);
      
      for (String healthFactor : healthFactors) {
        pstmt.setString(1, stationNo);
        for (int i = 1; i <= 3; i++) {
          pstmt.setString(i+1, StringUtils.piece(healthFactor, i));
        }
        pstmt.executeUpdate();
      }
      
    } catch(Exception e) {
      System.err.println("JDBC Error, exiting: " + e.getMessage());
      System.exit(1);
    }
    
  }
  
}
