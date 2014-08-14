import java.util.List;
import java.util.ArrayList;
import java.util.ResourceBundle;

import gov.va.med.lom.javaBroker.rpc.*;
import gov.va.med.lom.javaBroker.rpc.user.*;
import gov.va.med.lom.javaBroker.rpc.user.models.*;
import gov.va.med.lom.javaBroker.rpc.patient.*;
import gov.va.med.lom.javaBroker.rpc.patient.models.*;
import gov.va.med.lom.javaBroker.rpc.BrokerException;
import gov.va.med.lom.javaBroker.rpc.RpcBroker;

public class ExampleNewOrderRpc {
  
  public static void main(String[] args) {
    // If user didn't pass the patient's ssn and name of the auth properties file, then print usage and exit
    String server = null;
    int port = 0;
    String access = null;
    String verify = null;
    if (args.length != 1) {
      System.out.println("Usage: java ExampleNewOrderRpc AUTH_PROPS");
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
        
        // Order a lab test
        NewOrder newOrder = null;
        OrderInfo orderInfo = null;
        
        /*
        LabTest labTest = ordersRpc.getLabTestOrderData("1769"); 
        newOrder = new NewOrder();
        newOrder.setOrderId("202");
        newOrder.setDialogName("LR OTHER LAB TESTS");
        newOrder.setFillerId("LR");
        newOrder.setOrderable(new OrderField(labTest.getTestId(), labTest.getTestName()));
        newOrder.setPatientDfn("10108339");
        newOrder.setEncProvider("13243");
        newOrder.setEncLocation("396");
        newOrder.setPatientSex("M");
        newOrder.setPatientAge(67);
        newOrder.setScPercent(0);
        orderInfo = ordersRpc.putNewOrder(newOrder);
        System.out.println(orderInfo.getId());
        */
        
        // text order
        newOrder = new NewOrder();
        newOrder.setOrderId("49");
        newOrder.setDialogName("OR GXTEXT WORD PROCESSING ORDER");
        newOrder.setFillerId("OR");
        newOrder.setPatientDfn("10128436");
        newOrder.setEncProvider("13243");
        newOrder.setEncLocation("5542");
        newOrder.setPatientSex("M");
        newOrder.setPatientAge(63);
        //newOrder.setScPercent(0);
        newOrder.setComment(new OrderField(OrdersRpc.TX_WPTYPE, "This patient was seen for eyeglasses on 11/09/2012 10:45 and will be seen" +
        		"again on 05/03/2013 10:30.  A new consult is NOT necessary.  Please have the patient call the Eye Care Center directly at  909-825-7084 x5324 to" +
        		"schedule their glasses appointment."));
        newOrder.setStart(new OrderField("NOW","NOW"));
        newOrder.setStop(new OrderField("T+30","T+30"));
        orderInfo = ordersRpc.putNewOrder(newOrder);
        System.out.println(orderInfo.getId());
        
        /*
        // medication order
        newOrder = new NewOrder();
        newOrder.setOrderId("147");
        newOrder.setDialogName("PSO OERR");
        newOrder.setFillerId("PSO");
        newOrder.setPatientDfn("10108339");
        newOrder.setEncProvider("13243");
        newOrder.setEncLocation("396");
        newOrder.setPatientSex("M");
        newOrder.setPatientAge(67);
        newOrder.setScPercent(0);
        newOrder.setOrderable(new OrderField("3239", "SIMVASTATIN TAB"));
        newOrder.setInstr(new OrderField("5MG", "5MG"));
        newOrder.setDrug(new OrderField("4346", ""));
        newOrder.setDose(new OrderField("5&MG&1&TABLET&5MG&4346&5&MG", ""));
        newOrder.setStrength(new OrderField("5MG", "5MG"));
        newOrder.setRoute(new OrderField("1", "PO"));
        newOrder.setSchedule(new OrderField("QHS", "QHS"));
        newOrder.setUrgency(new OrderField("9", ""));
        newOrder.setSupply(new OrderField("30", "30"));
        newOrder.setQty(new OrderField("1", "1"));
        newOrder.setRefills(new OrderField("2", "2"));
        newOrder.setSc(new OrderField("",""));
        newOrder.setPickup(new OrderField("M", ""));
        newOrder.setPi(new OrderField(OrdersRpc.TX_WPTYPE, "FOR HIGH CHOLESTEROL"));
        newOrder.setSig(new OrderField(OrdersRpc.TX_WPTYPE, "TAKE ONE TABLET BY MOUTH AT BEDTIME"));
        orderInfo = ordersRpc.putNewOrder(newOrder);
        System.out.println(orderInfo.getId());
        */
        
        /*
        // consult order
        newOrder = new NewOrder();
        newOrder.setOrderId("548");
        newOrder.setDialogName("GMRCOR CONSULT");
        newOrder.setFillerId("GMRC");
        newOrder.setPatientDfn("10108339");
        newOrder.setEncProvider("13243");
        newOrder.setEncLocation("396");
        newOrder.setPatientSex("M");
        newOrder.setPatientAge(67);
        newOrder.setScPercent(0);
        newOrder.setOrderable(new OrderField("100", "Cardiology"));
        newOrder.setUrgency(new OrderField("9", "ROUTINE"));
        newOrder.setSetting(new OrderField("0", "OUTPATIENT"));
        newOrder.setPlace(new OrderField("C", "Consultant's Choice"));
        newOrder.setEarliest(new OrderField("TODAY", "TODAY"));
        newOrder.setComment(new OrderField(OrdersRpc.TX_WPTYPE, "This is a test."));
        orderInfo = ordersRpc.putNewOrder(newOrder);
        System.out.println(orderInfo.getId());
        */
        
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