/*
 * ExampleStationRpc.java
 * 
 * Author: Rob Durkin (rob.durkin@med.va.gov)
 * Version 1.0 (01/05/2006)
 *  
 * Retrieves date/time from VistA and other misc. functions.
 *
 * Usage: java ExampleStationRpc AUTH_PROPS STATION_NO
 * where AUTH_PROPS is the name of a properties file containing VistA sign-on info and
 *       STATION_NO is the number of the station for which to retrieve info (e.g. 605).
 * 
 * Required Libraries:
 *   javaBroker.jar 
 *   
 */
import java.util.ResourceBundle;
 
import gov.va.med.lom.javaBroker.rpc.user.models.*;
import gov.va.med.lom.javaBroker.rpc.RpcBroker;
import gov.va.med.lom.javaBroker.rpc.BrokerException;
import gov.va.med.lom.javaBroker.rpc.user.VistaSignonRpc;
import gov.va.med.lom.javaBroker.rpc.user.VistaStationRpc;
import gov.va.med.lom.javaBroker.rpc.user.models.VistaStation;

public class ExampleStationRpc {
  
  public static void main(String[] args) {
    // If user didn't pass the name of the auth properties file, then print usage and exit
    String server = null;
    int port = 0;
    String access = null;
    String verify = null;
    String stationNo = null;
    if (args.length != 2) {
      System.out.println("Usage: java ExampleStationRpc AUTH_PROPS");
      System.out.println("where AUTH_PROPS is the name of a properties file containing VistA sign-on info and");
      System.out.println("      STATION_NO is the number of the station for which to retrieve info.");
      System.exit(1);
    } else {
      ResourceBundle res = ResourceBundle.getBundle(args[0]);
      server = res.getString("server");
      port = Integer.valueOf(res.getString("port")).intValue();
      access = res.getString("accessCode");
      verify = res.getString("verifyCode");
      stationNo = args[1];
    } 
    try {    
      // Call the static signon method and get an instance of the vista signon rpc
      VistaSignonRpc vistaSignonRpc = ExampleVistaSignonRpc.doVistaSignon(server, port, access, verify);
      // Get the vista signon result and check if signon was successful
      VistaSignonResult vistaSignonResult = vistaSignonRpc.getVistaSignonResult(); 
      if (vistaSignonResult.getSignonSucceeded()) {
        RpcBroker rpcBroker = vistaSignonRpc.getRpcBroker();
        VistaStationRpc vistaStationRpc = new VistaStationRpc(rpcBroker);
        VistaStation station = vistaStationRpc.getStation(stationNo);
        System.out.println("Station Name: " + station.getName() + " (IEN=" + station.getIen() + ")");
      } else {
        System.out.println(vistaSignonResult.getMessage());
      }
      // Close the connection to the broker
      vistaSignonRpc.disconnect();
    } catch(BrokerException be) {
      System.err.println(be.getMessage());
      System.err.println("Action: " + be.getAction());
      System.err.println("Code: " + be.getCode());
      System.err.println("Mnemonic: " + be.getMnemonic());
    }      
  }

}