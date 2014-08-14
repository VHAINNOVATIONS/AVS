package gov.va.med.lom.avs.util;

import java.io.*;
import java.sql.*;
import java.util.ResourceBundle;
import java.net.URI;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import gov.va.med.lom.javaUtils.misc.JdbcConnection;
import gov.va.med.lom.javaUtils.misc.StringUtils;

public class UploadVistaImaging {

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
  
  static final String SERVER_HOST = "r01dvrcom14.r01.med.va.gov";
  static final String START_DATE = "2014-07-01 00:00:00";
  static final String STOP_DATE = "2014-07-10 00:00:00";
  
  public static void main(String[] args) throws IOException {
    
    CloseableHttpClient httpclient = null;
    JdbcConnection jdbcConnection = null;
    
    StringBuilder url = new StringBuilder("patientDfn=%s&datetime=%s&locationIen=%s&comments=%s&fontClass=%s");
    url.append("&language=en&labDateRange=%s&sections=%s&charts=%s&remoteVaMedicationsHtml=%s&remoteNonVaMedicationsHtml=%s");
    url.append("&tiuNoteIen=%s&customContent=%s&locked=false&printAllServiceDescriptions=false");
    url.append("&selectedServiceDescriptions=&initialRequest=false");
    
    StringBuilder sql1 = new StringBuilder("SELECT facilityNo, userDuz, patientDfn, locationIen, encounterDatetime, data ");
    sql1.append("FROM ckoUsageLog WHERE action = 'Create TIU Note' ");
    sql1.append("AND dateCreated >= ? ");
    sql1.append("AND dateCreated <= ? ");
    sql1.append("AND facilityNo <> 664 AND facilityNo <> 605");
    
    StringBuilder sql2 = new StringBuilder("SELECT instructions, customContent, fontClass, ");
    sql2.append("labDateRange, sections, charts, remoteMedications, remoteNonVaMedications ");
    sql2.append("FROM ckoEncounterCache WHERE facilityNo=? "); 
    sql2.append("AND patientDfn=? ");
    sql2.append("AND locationIen=? ");
    sql2.append("AND encounterDatetime=? ");
    
    int count = 0;
    
    try {
      try {
        jdbcConnection = new JdbcConnection(DB_URL, DB_DRIVER);
        jdbcConnection.connect(DB_USER, DB_PASS);
        
        Connection connection = jdbcConnection.getConnection();
        PreparedStatement pstmt1 = connection.prepareStatement(sql1.toString());
        pstmt1.setString(1, START_DATE);
        pstmt1.setString(2, STOP_DATE);
        
        ResultSet rs1 = pstmt1.executeQuery();
        while (rs1.next()) {
          String facilityNo = rs1.getString(1);
          String userDuz = rs1.getString(2);
          String patientDfn = rs1.getString(3);
          String locationIen = rs1.getString(4);
          String encounterDatetime = rs1.getString(5);
          String data = rs1.getString(6);
          
          PreparedStatement pstmt2 = connection.prepareStatement(sql2.toString());
          pstmt2.setString(1, facilityNo);
          pstmt2.setString(2, patientDfn);
          pstmt2.setString(3, locationIen);
          pstmt2.setString(4, encounterDatetime);
          
          ResultSet rs2 = pstmt2.executeQuery();
          if (rs2.next()) {
            String instructions = rs2.getString(1);
            String customContent = rs2.getString(2);
            String fontClass = rs2.getString(3);
            String labDateRange = rs2.getString(4);
            String sections = rs2.getString(5);
            String charts = rs2.getString(6);
            String remoteMedications = rs2.getString(7);
            String remoteNonVaMedications = rs2.getString(8);
            String tiuNoteIen = StringUtils.piece(StringUtils.piece(data, ',', 2), '=', 2);
            
            instructions = instructions != null ? instructions : "";
            customContent = customContent != null ? customContent : "";
            remoteMedications = remoteMedications != null ? remoteMedications : "";
            remoteNonVaMedications = remoteNonVaMedications != null ? remoteNonVaMedications : "";
            labDateRange = labDateRange != null ? labDateRange : "";
            
            try {
              
              httpclient = HttpClientBuilder.create().build();
              
              System.out.println("-----------------------------------------------");
              System.out.println("Current Count: " + ++count);
              System.out.println("Facility #: " + facilityNo);
              System.out.println("User DUZ: " + userDuz);
              System.out.println("TIU Note IEN: " + tiuNoteIen);
              
              // Login
              CloseableHttpResponse response = null;
              try {
                URI uri = new URI("http", SERVER_HOST, "/avs/w/login/LoginController",
                    String.format("institution=%s&userDuz=%s", facilityNo, userDuz), null);
                HttpGet httpGet = new HttpGet();
                httpGet.setURI(uri);
                response = httpclient.execute(httpGet);
                HttpEntity entity = response.getEntity();
                InputStream instream = entity.getContent();
                try {
                  try {
                    instream.read();
                    String result = convertStreamToString(instream);
                    System.out.println(result);
                  } catch(Exception e) {
                    e.printStackTrace();
                  }
                } finally {
                  try { 
                    instream.close(); 
                  } catch (Exception ignore) {}
                }
              } finally {
                response.close();
              }
              // Initiate VI upload 
              if ((response != null) && (response.getStatusLine().getStatusCode() == 200)) {
                try {
                  URI uri = new URI("http", SERVER_HOST, "/avs/w/s/avs/vistaImaging.action",
                      String.format(url.toString(), patientDfn, encounterDatetime, locationIen, instructions,
                      fontClass, labDateRange, sections, charts, remoteMedications, remoteNonVaMedications,
                      tiuNoteIen, customContent), null);
                  HttpPost httpPost = new HttpPost();
                  httpPost.setURI(uri);
                  response = httpclient.execute(httpPost);
                  HttpEntity entity = response.getEntity();
                  InputStream instream = entity.getContent();
                  try {
                    instream.read();
                    String result = convertStreamToString(instream);
                    System.out.println("RESULT: " + result);
                  } finally {
                    try { 
                      instream.close(); 
                    } catch (Exception ignore) {}
                  }
                } finally {
                  response.close();
                }
                
                // Logout
                try {
                  URI uri = new URI("http", SERVER_HOST, "/avs/w/login/logout", null, null);
                  HttpPost httpPost = new HttpPost();
                  httpPost.setURI(uri);
                  response = httpclient.execute(httpPost);
                } finally {
                  response.close();
                }
              }
              
            } finally {
              httpclient.close();
            }              
          }
        }
        
      } catch(Exception e) {
        System.err.println("JDBC Error, exiting: " + e.getMessage());
        System.exit(1);
      }
    } finally {
      try {
        jdbcConnection.disconnect();
      } catch(Exception e) {}
    }
    System.out.println("-----------------------------------------------");
    System.out.println("# Uploads: " + count);
  }
  
  private static String convertStreamToString(InputStream is) throws Exception {
    
    if (is != null) {
      Writer writer = new StringWriter();
      char[] buffer = new char[1024];
      try {
        Reader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
        int n;
        while ((n = reader.read(buffer)) != -1) {
          writer.write(buffer, 0, n);
        }
      } finally {
        is.close();
      }
      return writer.toString();
    } else {        
      return "";
    }    
    
  }
  
}
