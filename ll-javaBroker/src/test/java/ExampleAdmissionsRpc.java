/*
 * ExampleAdmissionsRpc.java
 * 
 * Author: Rob Durkin (rob.durkin@med.va.gov)
 * Version 1.3 (07/05/2005)
 *  
 * Prints the list of admissions for the patient.
 *
 * Usage: java ExampleAdmissionsRpc SSN AUTH_PROPS
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

public class ExampleAdmissionsRpc {
  
  /*
   * Prints the admissions for the first patient matching the specified ssn. 
   */
  public static void main(String[] args) {
    // If user didn't pass the patient's ssn and name of the auth properties file, then print usage and exit
    String ssn = null;
    String server = null;
    int port = 0;
    String access = null;
    String verify = null;
    if (args.length != 2) {
      System.out.println("Usage: java ExampleAdmissionsRpc SSN AUTH_PROPS");
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
          // Create an admissions rpc object and invoke the rpc
          AdmissionsRpc admissionsRpc = new AdmissionsRpc(rpcBroker);
          admissionsRpc.setReturnRpcResult(true);
          AdmissionsList admissionsList = admissionsRpc.getAdmissions(dfn);
          Admission[] admissions = admissionsList.getAdmissions();
          // Print the list of admissions
          for(int i = 0; i < admissions.length; i++) {
            try {
              System.out.println("Admission Date: " + DateUtils.toEnglishDate(admissions[i].getAdmitDate()));
              System.out.println("Discharge Date: " + DateUtils.toEnglishDate(admissions[i].getDischargeDate()));
            } catch(ParseException pe) {}
            System.out.println("Location IEN: " + admissions[i].getLocationIen());
            System.out.println("Location: " + admissions[i].getLocation());
            System.out.println("Type: " + admissions[i].getType() + "\n");
          }
          System.out.println("RPC Result: " + admissionsList.getRpcResult());
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