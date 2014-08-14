package gov.va.med.lom.avs.util;

import java.io.*;
import java.util.*;
import java.sql.*;

import gov.va.med.lom.javaBroker.rpc.MiscRPCs;
import gov.va.med.lom.javaBroker.rpc.RpcBroker;
import gov.va.med.lom.javaBroker.rpc.user.VistaRemoteSignonRpc;
import gov.va.med.lom.javaBroker.rpc.user.models.RemoteSignon;
import gov.va.med.lom.javaUtils.misc.JdbcConnection;
import gov.va.med.lom.javaUtils.misc.StringUtils;

public class EncounterCacheUpdate {

  static String LOCAL_STN = "605";
  static String DB_USER;
  static String DB_PASS;
  static String DB_DRIVER;
  static String DB_URL;
  static String VISTA_SERVER;
  static int VISTA_PORT;
  static String VISTA_AV;
  
  static HashMap<String, String> providers;
  //static HashMap<String, String> locations;
  static HashMap<String, RpcBroker> remoteRpcBrokers;
  
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
    res = ResourceBundle.getBundle("gov.va.med.lom.avs.vista");
    VISTA_SERVER = res.getString("vista.vistaServer");
    VISTA_PORT = Integer.valueOf(res.getString("vista.vistaPort")).intValue();
    VISTA_AV = res.getString("vista.vistaAV");
    
    providers = new HashMap<String, String>();
    //locations = new HashMap<String, String>();
    remoteRpcBrokers = new HashMap<String, RpcBroker>();
  }
  
  public static void main(String[] args) throws IOException {

    long encCacheId = 0L;
    String facilityNo = null;
    String providerDuz = null;
    //String locationIen = null;
    
    JdbcConnection jdbcConnection = null;
    RpcBroker localRpcBroker = null;
    
    try {
      jdbcConnection = new JdbcConnection(DB_URL, DB_DRIVER);
      jdbcConnection.connect(DB_USER, DB_PASS);
      
      Connection connection = jdbcConnection.getConnection();
       
      StringBuilder sql = new StringBuilder("SELECT id, facilityNo, providerDuz, locationIen FROM ckoEncounterCache ");
      sql.append("WHERE providerName IS NULL OR providerName like '%null%' OR providerName = ''");
      sql.append("AND docType = 'avs'");
      PreparedStatement ps1 = connection.prepareStatement(sql.toString());
      
      sql = new StringBuilder("SELECT location, stationNo, host, port FROM vhaSite WHERE stationNo=?");
      PreparedStatement ps2 = connection.prepareStatement(sql.toString());      
      
      //sql = new StringBuilder("UPDATE ckoEncounterCache SET providerName=?, providerTitle=?, locationName=? WHERE id=?");
      sql = new StringBuilder("UPDATE ckoEncounterCache SET providerName=?, providerTitle=? WHERE id=?");
      PreparedStatement ps3 = connection.prepareStatement(sql.toString());

      localRpcBroker = getRpcBroker();
      
      ResultSet rs = ps1.executeQuery();
      while (rs.next()) {
        
        String providerTitle = null;
        String providerName = null;
        //String locationName = null;
        
        RpcBroker rpcBroker = null;
        encCacheId = rs.getLong(1);
        facilityNo = rs.getString(2);
        providerDuz = rs.getString(3);
        //locationIen = rs.getString(4);
        
        if (facilityNo == null) {
          continue;
        }
        
        if (facilityNo.startsWith(LOCAL_STN)) {
          rpcBroker = localRpcBroker;
        } else {
          String stationNo = facilityNo.substring(0, 3);
          rpcBroker = remoteRpcBrokers.get(stationNo);
          if (rpcBroker == null) {
            try {
              VistaRemoteSignonRpc vistaRemoteSignonRpc = new VistaRemoteSignonRpc(localRpcBroker);
              ps2.setString(1, stationNo);
              ResultSet rs2 = ps2.executeQuery();
              rs2.next();
              String remoteLocation = rs2.getString(1);
              String remoteStationNo = rs2.getString(2);
              System.out.println("Remote Station=" + remoteLocation + " (" + remoteStationNo + ")");
              String remoteHost = rs2.getString(3);
              int remotePort = rs2.getInt(4);
              RemoteSignon remoteSignon = vistaRemoteSignonRpc.getRemoteConnection(remoteHost, remotePort);
              rpcBroker = remoteSignon.getRemoteBroker();
              remoteRpcBrokers.put(stationNo, rpcBroker);
            } catch(Exception e) {
              System.err.println(e.getMessage());
              //System.err.println("Station No=" + facilityNo + ", Encounter ID=" + encCacheId + ", Provider DUZ=" + providerDuz + ", Location IEN=" + locationIen);
            }
          }
        }
        
        StringBuffer sb1 = new StringBuffer();
        StringBuffer sb2 = new StringBuffer();
        if (providerDuz != null) {
          String[] providerDuzs = StringUtils.pieceList(providerDuz, ',');
          for (int i = 0; i < providerDuzs.length; i++) {
            String duz = providerDuzs[i];
            if (providers.containsKey(duz)) {
              String x = providers.get(duz);
              providerName = StringUtils.piece(x, '|', 1);
              providerTitle = StringUtils.piece(x, '|', 2);
            } else {
              try {
                String arg = "@\"^VA(200," + duz + ",0)\"";
                String rtn = MiscRPCs.getVariableValue(rpcBroker, arg);
                providerName = StringUtils.piece(rtn, 1);
                String titleIen = StringUtils.piece(rtn, 9);
                if (!titleIen.isEmpty()) {
                  arg = "@\"^DIC(3.1," + titleIen + ",0)\"";
                  providerTitle = MiscRPCs.getVariableValue(rpcBroker, arg);
                  providerTitle = StringUtils.piece(providerTitle, 1);
                }
              } catch(Exception e) {
                System.err.println(e.getMessage());
                System.err.println("Station No=" + facilityNo + ", Encounter ID=" + encCacheId + ", Provider DUZ=" + providerDuz); //", Location IEN=" + locationIen
              }
            }
            if (i > 0) {
              sb1.append("^");
              sb2.append("^");
            }
            sb1.append(providerName);
            sb2.append(providerTitle);
            providers.put(providerDuz, sb1.toString() + "|" + sb2.toString());
          }
        }
        /*
        if (locations.containsKey(locationIen)) {
          locationName = locations.get(locationIen);
        } else {
          try {
            String arg = "@\"^SC(" + locationIen + ",0)\"";
            String rtn = MiscRPCs.getVariableValue(rpcBroker, arg);
            locationName = StringUtils.piece(rtn, 1);
          } catch(Exception e) {
            locationName = "";
            System.err.println(e.getMessage());
            System.err.println("Station No=" + facilityNo + ", Encounter ID=" + encCacheId + ", Provider DUZ=" + providerDuz + ", Location IEN=" + locationIen);
          }
        }
        locations.put(locationIen, locationName);
        */
        if (sb1.equals("null")) {
          sb1.delete(0, sb1.length());
        }
        if (sb2.equals("null")) {
          sb2.delete(0, sb2.length());
        }        
        //if (locationName.equals("null")) {
        //  locationName = "";
        //}          
        ps3.setString(1, sb1.toString());
        ps3.setString(2, sb2.toString());
        //ps3.setString(3, locationName);
        ps3.setLong(3, encCacheId);
        ps3.executeUpdate();
        
        //System.out.println("Station No=" + facilityNo + ", Provider Name=" + sb1.toString() + ", Provider Title=" + sb2.toString() + 
        //    ", Location=" + locationName + ", Encounter ID=" + encCacheId);
        System.out.println("Station No=" + facilityNo + ", Provider Name=" + sb1.toString() + ", Provider Title=" + sb2.toString() + ", Encounter ID=" + encCacheId);
      }
    } catch(Exception e) {
      System.err.println(e.getMessage());
    } finally {
      try {
        jdbcConnection.disconnect();
      } catch(Exception e) {}
      try {
        localRpcBroker.disconnect();
      } catch (Exception e) {}
      Collection<RpcBroker> remoteBrokers = remoteRpcBrokers.values();
      for (RpcBroker remoteBroker : remoteBrokers) {
        try {
          remoteBroker.disconnect();
        } catch (Exception e) {}
      }
    }
  }
  
  private static RpcBroker getRpcBroker() throws Exception {
    String accessCode = StringUtils.piece(VISTA_AV, ';', 1);
    String verifyCode = StringUtils.piece(VISTA_AV, ';', 2);
    
    // Create a rpc broker instance
    RpcBroker rpcBroker = new RpcBroker(VISTA_SERVER, VISTA_PORT);
    // Signon if connected 
    if (rpcBroker.isConnected()) {
      rpcBroker.doSignon(accessCode, verifyCode);
      return rpcBroker;
    } else {
      System.err.println("Could not sign on to local RPC Broker server: " + VISTA_SERVER + ":" + VISTA_PORT);
    }
    return null;
  }  
  
}
