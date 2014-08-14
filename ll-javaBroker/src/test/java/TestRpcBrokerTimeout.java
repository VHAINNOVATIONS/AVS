/*
 * TestRpcBrokerTimeout.java
 * 
 * Author: Rob Durkin (rob.durkin@med.va.gov)
 * Version 1.0 (05/217/2006)
 *  
 * Tests the inactive timeout functionality of RpcBroker.
 *
 * Usage: java TestRpcBrokerTimeout AUTH_PROPS TIMEOUT
 * where AUTH_PROPS is the name of a properties file containing VistA sign-on info.
 *       TIMEOUT is the duration of inactivity after which the RpcBroker will disconnect.
 * 
 * Required Libraries:
 *   javaBroker.jar 
 *   
 */
import java.util.ResourceBundle;
import java.util.Date;
 
import gov.va.med.lom.javaBroker.util.Console;

import gov.va.med.lom.javaBroker.rpc.user.models.*;
import gov.va.med.lom.javaBroker.rpc.RpcBroker;
import gov.va.med.lom.javaBroker.rpc.BrokerException;
import gov.va.med.lom.javaBroker.rpc.user.VistaSignonRpc;
import gov.va.med.lom.javaBroker.rpc.user.VistaUserRpc;

public class TestRpcBrokerTimeout {
  
  public static void main(String[] args) {
    // If user didn't pass the name of the auth properties file, then print usage and exit
    String server = null;
    int port = 0;
    String access = null;
    String verify = null;
    int timeout = 0;
    if (args.length != 2) {
      System.out.println("Usage: java TestRpcBrokerTimeout AUTH_PROPS");
      System.out.println("where AUTH_PROPS is the name of a properties file containing VistA sign-on info.");
      System.out.println("      TIMEOUT is the duration of inactivity after which the RpcBroker will disconnect.");
      System.exit(1);
    } else {
      ResourceBundle res = ResourceBundle.getBundle(args[0]);
      server = res.getString("server");
      port = Integer.valueOf(res.getString("port")).intValue();
      access = res.getString("accessCode");
      verify = res.getString("verifyCode");
      timeout = Integer.valueOf(args[1]).intValue();
    } 
    try {    
      // Call the static signon method and get an instance of the vista signon rpc
      VistaSignonRpc vistaSignonRpc = ExampleVistaSignonRpc.doVistaSignon(server, port, access, verify);
      // Get the vista signon result and check if signon was successful
      VistaSignonResult vistaSignonResult = vistaSignonRpc.getVistaSignonResult(); 
      if (vistaSignonResult.getSignonSucceeded()) {
        RpcBroker rpcBroker = vistaSignonRpc.getRpcBroker();
        // Set the inactive timeout
        rpcBroker.setInactiveTimeout(timeout);
        VistaUserRpc vistaUserRpc = new VistaUserRpc(rpcBroker);
        System.out.println("RpcBroker will timout after " + timeout + " msec of inactivity.");
        while (true) {
          Console.readLine("\nHit enter to invoke an RPC...");
          Date now = new Date();
          Date last = rpcBroker.getLastMessageTime();
          Console.println("Current date/time is " + now);
          Console.println("Last message sent at " + last);
          Console.println("Was inactive for " + (now.getTime() - last.getTime()) + " msec");
          if (rpcBroker.isConnected()) {
            Console.println("RpcBroker is still connected.");
            vistaUserRpc.getVistaUser();
          } else {
            Console.println("RpcBroker was disconnected due to inactivity.");
            String response = Console.readLine("Would you like to re-signon (y/n)?");
            if (response.equalsIgnoreCase("y")) {
              rpcBroker.doSignon();
              if (rpcBroker.isConnected())
                Console.println("RpcBroker has been reconnected.");
              else {
                Console.println("RpcBroker could not be reconnected.");
                break;
              }
            } else
              break;
          }
        }

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