/*
 * ExampleLabsRpc.java
 * 
 * Author: Rob Durkin (rob.durkin@med.va.gov)
 * Version 1.2 (06/22/2005)
 *  
 * Prints the list of lab results for the patient.
 *
 * Usage: java ExampleLabsRpc SSN AUTH_PROPS
 * where SSN is a patient's Social Security Number.
 *       AUTH_PROPS is the name of a properties file containing VistA sign-on info.
 * 
 * Required Libraries:
 *   javaBroker.jar 
 *   
 */
import java.util.ResourceBundle;
import java.text.ParseException;

import gov.va.med.lom.javaBroker.rpc.user.*;
import gov.va.med.lom.javaBroker.rpc.user.models.*;
import gov.va.med.lom.javaBroker.rpc.patient.*;
import gov.va.med.lom.javaBroker.rpc.patient.models.*;
import gov.va.med.lom.javaBroker.rpc.BrokerException;
import gov.va.med.lom.javaBroker.rpc.RpcBroker;
import gov.va.med.lom.javaBroker.util.DateUtils;

public class ExampleLabsRpc {
  
  /*
   * Prints lab results for the first patient matching the specified ssn. 
   */
  public static void main(String[] args) {
    // If user didn't pass the patient's ssn and name of the auth properties file, then print usage and exit
    String ssn = null;
    String server = null;
    int port = 0;
    String access = null;
    String verify = null;
    if (args.length != 1) {
      System.out.println("Usage: java ExampleRecentLabsRpc SSN AUTH_PROPS");
      System.out.println("where SSN is a patient's Social Security Number.");
      System.out.println("      AUTH_PROPS is the name of a properties file containing VistA sign-on info.");
      System.exit(1);
    } else {
      //ssn = args[0];
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
        // Get the patient's dfn from the ssn
        String dfn = "10226344";//ExamplePatientSelectionRpc.getPatientDfnBySsn(rpcBroker, ssn);
        if (dfn != null) {
          // Create a labs rpc object and invoke the rpc
          LabsRpc labsRpc = new LabsRpc(rpcBroker);
          labsRpc.setReturnRpcResult(false);
          /*
          LabTestResultsList recentLabsList = labsRpc.getRecentLabs(dfn);
          LabTestResult[] recentLabs = recentLabsList.getLabTestResults();
          System.out.println("---------- RECENT LAB RESULTS ------------");
          for (int i = 0; i < recentLabs.length; i++) {
            System.out.println("Name: " + recentLabs[i].getName());
            try {
              System.out.println("Date: " + DateUtils.toEnglishDateTime(recentLabs[i].getDate()));
            } catch(ParseException pe) {}            
            System.out.println("Status: " + recentLabs[i].getStatus());
            System.out.println("Result: " + recentLabs[i].getResult() + "\n");
          }
          System.out.println("RPC Result: " + recentLabsList.getRpcResult());
          System.out.println("\n\n---------- CUMULATIVE LAB RESULTS ------------");
          CumulativeLabResults cumulativeLabResults = labsRpc.getCumulativeLabResults(dfn, 183, null, null);
          LabResultTestType[] labResultTestTypes = cumulativeLabResults.getLabResultTestTypes();
          for (int i = 0; i < labResultTestTypes.length; i++) {
            System.out.println(labResultTestTypes[i].getName() + "(Start: " + labResultTestTypes[i].getStart() + ")");
          }
          System.out.println(cumulativeLabResults.getText());
          */
          
          String results = labsRpc.getTestsByDate(dfn, 90, null, null);
          System.out.println(results);
          
        } else {
          System.out.println("No matching patient for the specified ssn.");
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