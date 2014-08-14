/*
 * ExampleVistaSignonSetupRpc.java
 * 
 * Author: Rob Durkin (rob.durkin@med.va.gov)
 * Version 1.3 (07/05/2005)
 *  
 * Connects the Java client broker to the server and does the signon setup.
 *
 * Usage: java ExampleVistaSignonSetupRpc SERVER PORT
 * where SERVER and PORT are the host/IP and port of the VistA server.
 * 
 * Required Libraries:
 *   javaBroker.jar 
 *   
 */

import gov.va.med.lom.javaBroker.rpc.user.*;
import gov.va.med.lom.javaBroker.rpc.user.models.*;
import gov.va.med.lom.javaBroker.rpc.BrokerException;
import gov.va.med.lom.javaBroker.rpc.RpcBroker;

public class ExampleVistaSignonSetupRpc {
  
  /*
   * Connects to the broker server and does the signon setup.
   */
  public static VistaSignonSetupRpc doVistaSignonSetup(String server, int port) throws BrokerException {
    // Create an RPC broker and connect to the server
    RpcBroker rpcBroker = new RpcBroker(server, port);
    rpcBroker.connect();
    // Do the signon setup 
    VistaSignonSetupRpc vistaSignonSetupRpc = new VistaSignonSetupRpc(rpcBroker);
    vistaSignonSetupRpc.setReturnRpcResult(true);
    vistaSignonSetupRpc.doVistaSignonSetup();
    return vistaSignonSetupRpc;
  }
  
  public static void main(String[] args) {
    // If user didn't pass the server and port, then print usage and exit
    String server = null;
    int port = 0;
    if (args.length != 2) {
      System.out.println("Usage: java ExampleVistaSignonSetupRpc SERVER PORT");
      System.out.println("where SERVER and PORT are the host/IP and port of the VistA server.");
      System.exit(1);
    } else {
      server = args[0];
      port = Integer.valueOf(args[1]).intValue();
    }          
    try {      
      // Call the static signon setup method and get an instance of the vista signon setup rpc
      VistaSignonSetupRpc vistaSignonSetupRpc = ExampleVistaSignonSetupRpc.doVistaSignonSetup(server, port);
      // Get the vista signon setup result object
      VistaSignonSetupResult vistaSignonSetupResult = vistaSignonSetupRpc.getVistaSignonSetupResult(); 
         
      // Print the properties of the vista signon setup result object    
      System.out.println("---------- VISTA SIGN-ON SETUP INFO ----------");
      System.out.println("Server Available: " + vistaSignonSetupResult.getServerAvailable());
      System.out.println("Server: " + vistaSignonSetupResult.getServer());
      System.out.println("Volume: " + vistaSignonSetupResult.getVolume());
      System.out.println("UCI: " + vistaSignonSetupResult.getUci());
      System.out.println("Port: " + vistaSignonSetupResult.getPort());
      System.out.println("Device Locked: " + vistaSignonSetupResult.getDeviceLocked());
      System.out.println("Signon Required: " + vistaSignonSetupResult.getSignonRequired());
      System.out.println("Intro Message: " + vistaSignonSetupResult.getIntroMessage());
      System.out.println("RPC Result: " + vistaSignonSetupResult.getRpcResult());
      
      // Close the connection to the broker
      vistaSignonSetupRpc.disconnect();
    } catch(BrokerException be) {
      System.err.println(be.getMessage());
      System.err.println("Action: " + be.getAction());
      System.err.println("Code: " + be.getCode());
      System.err.println("Mnemonic: " + be.getMnemonic());
    }      
  }

}