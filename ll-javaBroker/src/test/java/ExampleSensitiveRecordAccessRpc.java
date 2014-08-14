/*
 * ExampleSensitiveRecordAccessRpc.java
 * 
 * Author: Rob Durkin (rob.durkin@med.va.gov)
 * Version 1.2 (06/22/2005)
 *  
 * Prints the sensitive record access status for the patient.
 *
 * Usage: java ExampleSensitiveRecordAccessRpc SSN AUTH_PROPS
 * where SSN is a patient's Social Security Number.
 *       AUTH_PROPS is the name of a properties file containing VistA sign-on info.
 * 
 * Required Libraries:
 *   javaBroker.jar 
 *   
 */
import java.util.ResourceBundle;

import gov.va.med.lom.javaBroker.rpc.user.*;
import gov.va.med.lom.javaBroker.rpc.user.models.*;
import gov.va.med.lom.javaBroker.rpc.patient.*;
import gov.va.med.lom.javaBroker.rpc.patient.models.*;
import gov.va.med.lom.javaBroker.rpc.BrokerException;
import gov.va.med.lom.javaBroker.rpc.RpcBroker;

public class ExampleSensitiveRecordAccessRpc {
  
  /*
   * Prints sensitive records status info for the first patient matching the specified ssn. 
   */
  public static void main(String[] args) {
    // If user didn't pass the patient's ssn and name of the auth properties file, then print usage and exit
    String ssn = null;
    String server = null;
    int port = 0;
    String access = null;
    String verify = null;
    if (args.length != 2) {
      System.out.println("Usage: java ExampleSensitiveRecordAccessRpc SSN AUTH_PROPS");
      System.out.println("where SSN is a patient's Social Security Number.");
      System.out.println("      AUTH_PROPS is the name of a properties file containing VistA sign-on info.");
      System.exit(1);
    } else {
      ssn = args[0];
      ResourceBundle res = ResourceBundle.getBundle(args[1]);
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
        String dfn = ExamplePatientSelectionRpc.getPatientDfnBySsn(rpcBroker, ssn);
        if (dfn != null) {
          // Create a sensitive record access rpc object and invoke the rpc
          SensitiveRecordAccessRpc sensitiveRecordAccessRpc = new SensitiveRecordAccessRpc(rpcBroker);
          sensitiveRecordAccessRpc.setReturnRpcResult(true);
          SensitiveRecordAccessStatus sensitiveRecordAccessStatus = sensitiveRecordAccessRpc.getSensitiveRecordAccess(dfn); 
          // Print the patient's sensitive record status
          System.out.println("---------- SENSITIVE RECORD ACCESS STATUS ----------");
          System.out.println("DFN: " + sensitiveRecordAccessStatus.getDfn());
          int result = sensitiveRecordAccessStatus.getResult();
          System.out.print("Result: " + result);
          if (result == SensitiveRecordAccessRpc.RPC_FAILED)
            System.out.println(" (RPC Failed)");
          else if (result == SensitiveRecordAccessRpc.NO_ACTION_REQUIRED)
            System.out.println(" (No Action Required)");
          else if (result == SensitiveRecordAccessRpc.INPATIENT_OR_EMPLOYEE)
            System.out.println(" (Inpatient or Employee)");
          else if (result == SensitiveRecordAccessRpc.SENSITIVE_RECORD)
            System.out.println(" (Sensitive Record)");
          else if (result == SensitiveRecordAccessRpc.ACCESSING_OWN_PATIENT_RECORD)
            System.out.println(" (Accessing Own Patient Record)");
          else if (result == SensitiveRecordAccessRpc.SSN_NOT_DEFINED)
            System.out.println(" (SSN Not Defined)");
          System.out.println("Message: " + sensitiveRecordAccessStatus.getMessage());
          System.out.println("RPC Result: " + sensitiveRecordAccessStatus.getRpcResult());
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