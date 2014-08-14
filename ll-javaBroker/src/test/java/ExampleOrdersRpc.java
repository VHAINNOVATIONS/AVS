/*
 * ExampleOrdersRpc.java
 * 
 * Author: Rob Durkin (rob.durkin@med.va.gov)
 * Version 1.3 (07/05/2005)
 *  
 * Prints the list of orders for the patient.
 *
 * Usage: java ExampleOrdersRpc SSN AUTH_PROPS
 * where SSN is a patient's Social Security Number.
 *       AUTH_PROPS is the name of a properties file containing VistA sign-on info
 * 
 * Required Libraries:
 *   javaBroker.jar 
 *   
 */
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.ResourceBundle;

import gov.va.med.lom.javaBroker.rpc.user.*;
import gov.va.med.lom.javaBroker.rpc.user.models.*;
import gov.va.med.lom.javaBroker.rpc.patient.*;
import gov.va.med.lom.javaBroker.rpc.patient.models.*;
import gov.va.med.lom.javaBroker.rpc.BrokerException;
import gov.va.med.lom.javaBroker.rpc.RpcBroker;

import gov.va.med.lom.javaBroker.util.Console;
import gov.va.med.lom.javaBroker.util.FMDateUtils;

public class ExampleOrdersRpc {
  
  static double ORDERS_END_DATE;
  static double ORDERS_START_DATE;
  
  static {
    GregorianCalendar date = new GregorianCalendar();
    date.setTime(new Date());
    ORDERS_END_DATE = FMDateUtils.dateToFMDate(date.getTime());
    date.add(Calendar.DAY_OF_YEAR, -1);
    ORDERS_START_DATE = FMDateUtils.dateToFMDate(date.getTime());
  }
  
  /*
   * Prints the orders for the first patient matching the specified ssn. 
   */
  public static void main(String[] args) {
    // If user didn't pass the patient's ssn and name of the auth properties file, then print usage and exit
    String server = null;
    int port = 0;
    String access = null;
    String verify = null;
    if (args.length != 1) {
      System.out.println("Usage: java ExampleOrdersRpc AUTH_PROPS");
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
        RpcBroker rpcBroker = vistaSignonRpc.getRpcBroker();
        OrdersRpc ordersRpc = new OrdersRpc(rpcBroker);
        
        /*
        String[] dgroup = ordersRpc.getDGroupMap();
        for (int i = 0; i < dgroup.length; i++) {
          System.out.println(dgroup[i]);
        }
        */
        
        //String ssn = Console.readLine("SSN: ");
        //long dfn = ExamplePatientSelectionRpc.getPatientDfnBySsn(rpcBroker, ssn);

        String dfn = "10290918";
        
        // Create a orders rpc object and invoke the rpc
        OrderView orderView = ordersRpc.getOrderViewDefault();
        ordersRpc.setReturnRpcResult(true);
        //orderView.setByService(true);
        //orderView.setDGroup("55");
        //orderView.setFmTimeFrom(ORDERS_START_DATE);
        //orderView.setFmTimeThru(ORDERS_END_DATE);
        OrdersInfoList ordersInfoList = ordersRpc.getOrdersList(dfn, orderView, null, false);
        OrderInfo[] ordersInfo = ordersInfoList.getOrdersInfo();
        System.out.println("# orders = " + ordersInfo.length);
        for (int i = 0; i < ordersInfo.length; i++) {
          System.out.println(ordersInfo[i].getId() + " - " + ordersInfo[i].getText() + 
              ", (" + ordersInfo[i].getStatusIen() + ", Dgroup=" + ordersInfo[i].getDGroup() + " [" + ordersInfo[i].getDGroupName() + "]) - " + 
              ordersInfo[i].getStartTimeStr() + "," + ordersInfo[i].getStopTimeStr() + "," + ordersInfo[i].getOrderDatetimeStr());
          System.out.println(ordersInfo[i].getRpcResult());
          System.out.println("-----------------------------------------------------------");
        }

        /*
        long dfn = Console.readLong("Patient DFN: ");
        long orderId = Console.readLong("Order ID: ");
        long consultId = Console.readLong("Consult ID: ");
        // Get the patient's dfn from the ssn
        //long dfn = ExamplePatientSelectionRpc.getPatientDfnBySsn(rpcBroker, ssn);
        //if (dfn > 0) {
          // Create a orders rpc object and invoke the rpc
          ConsultsRpc consultsRpc = new ConsultsRpc(rpcBroker);
          System.out.println(consultsRpc.getCPRequests(dfn));
           //DFN: 10217577
           //Order IEN: 16110137
           //Consult IEN: 919855
           //Placer Order Num: 3060215 150047
          */
        
        /*
          String orderId = "41174998;1";
          String orderDetails = ordersRpc.getOrderDetails(orderId, dfn);
          System.out.println("---- ORDER DETAILS ----");
          System.out.println(orderDetails);
        */
        
          /*
          String consultDetails = consultsRpc.getConsultDetail(consultId);
          System.out.println("\n\n---- CONSULT DETAILS ----");
          System.out.println(consultDetails);
          */ 
        //} else {
        //  System.out.println("No matching patient for the specified ssn.");
        //}
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