/*
 * ExampleVistaUserRpc.java
 * 
 * Author: Rob Durkin (rob.durkin@med.va.gov)
 * Version 1.2 (06/22/2005)
 *  
 * Retrieves information from VistA about the user who is signed on.
 *
 * Usage: java ExampleVistaUserRpc AUTH_PROPS
 * where AUTH_PROPS is the name of a properties file containing VistA sign-on info.
 * 
 * Required Libraries:
 *   javaBroker.jar 
 *   
 */
import java.util.ResourceBundle;
 
import gov.va.med.lom.javaBroker.rpc.BrokerException;
import gov.va.med.lom.javaBroker.rpc.user.*;
import gov.va.med.lom.javaBroker.rpc.user.models.*;

public class ExampleVistaUserRpc {
  
  public static void main(String[] args) {
    // If user didn't pass the name of the auth properties file, then print usage and exit
    String server = null;
    int port = 0;
    String access = null;
    String verify = null;
    if (args.length != 1) {
      System.out.println("Usage: java ExampleVistaUserRpc AUTH_PROPS");
      System.out.println("where AUTH_PROPS is the name of a properties file containing VistA sign-on info.");
      System.exit(1);
    } else {
      ResourceBundle res = ResourceBundle.getBundle(args[0]);
      server = res.getString("server");
      port = Integer.valueOf(res.getString("port")).intValue();
      access = res.getString("accessCode");
      verify = res.getString("verifyCode");
    } 
    try {    
      // Call the static signon method and get an instance of the vista signon rpc
      VistaSignonRpc vistaSignonRpc = ExampleVistaSignonRpc.doVistaSignon(server, port, access, verify);
      // Get the vista signon result and check if signon was successful
      VistaSignonResult vistaSignonResult = vistaSignonRpc.getVistaSignonResult(); 
      if (vistaSignonResult.getSignonSucceeded()) {
        // Get Employee object by DUZ
        System.out.println("---------- EMPLOYEE INFO ---------");
        VistaUserRpc vistaUserRpc = new VistaUserRpc(vistaSignonRpc.getRpcBroker());
        String duz = vistaSignonRpc.getRpcBroker().getUserDuz();
        Employee employee = vistaUserRpc.getEmployeeByDuz(duz);
        
        System.out.println("Employee Name: " + employee.getName());
        System.out.println("Employee Station No: " + employee.getStationNo());
        System.out.println("Employee Title: " + employee.getTitle());
        System.out.println("Employee Ext: " + employee.getExtension());
        System.out.println("Employee Pager: " + employee.getPager());
        System.out.println("Employee Room: " + employee.getRoom());
        System.out.println("Employee SSN: " + employee.getSsn());
        System.out.println("Employee Last Sign-On: " + employee.getLastSignon());
        System.out.println("Employee Service IEN: " + employee.getServiceIen());
        
        /*
        // Get and print Vista user info
        vistaUserRpc.setReturnRpcResult(true);
        vistaUserRpc.setReturnXml(true);
        VistaUser vistaUser = vistaUserRpc.getVistaUser();
        System.out.println("---------- VISTA USER INFO ---------");
        System.out.println("DUZ: " + vistaUser.getDuz());
        System.out.println("Name: " + vistaUser.getName());
        System.out.println("Standard Name: " + vistaUser.getStandardName());
        System.out.println("Title: " + vistaUser.getTitle());
        System.out.println("Station IEN: " + vistaUser.getStationIen());
        System.out.println("Station: " + vistaUser.getStation());
        System.out.println("Station Number: " + vistaUser.getStationNumber());
        System.out.println("User Class: " + vistaUser.getUserClass());
        System.out.println("Can Sign Orders: " + vistaUser.getCanSignOrders());
        System.out.println("Order Role: " + vistaUser.getOrderRole());
        System.out.println("Is Provider: " + vistaUser.getIsProvider());
        System.out.println("No Ordering: " + vistaUser.getNoOrdering());
        System.out.println("DTime: " + vistaUser.getDTime());
        System.out.println("Count Down: " + vistaUser.getCountDown());
        System.out.println("Enable Verify: " + vistaUser.getEnableVerify());
        System.out.println("Notify Apps WM: " + vistaUser.getNotifyAppsWM());
        System.out.println("Pt Msg Hang: " + vistaUser.getPtMsgHang());
        System.out.println("Domain: " + vistaUser.getDomain());
        System.out.println("Service: " + vistaUser.getService());
        System.out.println("Auto Save: " + vistaUser.getAutoSave());
        System.out.println("Initial Tab: " + vistaUser.getInitialTab());
        System.out.println("Use Last Tab: " + vistaUser.getUseLastTab());
        System.out.println("Web Access: " + vistaUser.getWebAccess());
        System.out.println("Disable Hold: " + vistaUser.getDisableHold());
        System.out.println("Is RPL: " + vistaUser.getIsRPL());
        System.out.println("RPL List: " + vistaUser.getRplList());
        System.out.println("Has Core Tabs: " + vistaUser.getHasCorTabs());
        System.out.println("Has Rpt Tab: " + vistaUser.getHasRptTab());
        System.out.println("Is Reports Only: " + vistaUser.getIsReportsOnly());
        System.out.println("Service Section: " + vistaUser.getServiceSection());
        System.out.println("Language: " + vistaUser.getLanguage());
        System.out.println("RPC Result: " + vistaUser.getRpcResult());
        System.out.println("XML: " + vistaUser.getXml());
        */
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