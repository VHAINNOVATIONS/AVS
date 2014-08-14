/*
 * ExampleInsuranceRpc.java
 * 
 * Author: Rob Durkin (rob.durkin@med.va.gov)
 * Version 1.0 (08/22/2006)
 *  
 * Returns insurance information for the patient.
 * Note: Requires the custom 'ALSI INS COM' rpc 
 *       and the 'ALS CPRS COM OBJECT RPCS' context.
 *
 * Usage: java ExampleAddressRpc DFN AUTH_PROPS
 * where SSN is a patient's DFN.
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
import gov.va.med.lom.javaBroker.rpc.RpcBroker;
import gov.va.med.lom.javaBroker.rpc.BrokerException;

public class ExampleInsuranceRpc {
  
  /*
   * Prints the address for the first patient matching the specified ssn. 
   */
  public static void main(String[] args) {
    // If user didn't pass the patient's ssn and name of the auth properties file, then print usage and exit
   // String ssn = null;
    String dfn = null;
    String server = null;
    int port = 0;
    String access = null;
    String verify = null;
    if (args.length != 2) {
      System.out.println("Usage: java ExampleInsuranceRpc DFN AUTH_PROPS");
      System.out.println("where DFN is a patient's DFN.");
      System.out.println("      AUTH_PROPS is the name of a properties file containing VistA sign-on info.");
      System.exit(1);
    } else {
      //ssn = args[0];
      dfn = args[0];
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
        //long dfn = ExamplePatientSelectionRpc.getPatientDfnBySsn(rpcBroker, ssn);
        if (dfn != null) {
          System.out.println("Insurance Info");
          // Create an insurance rpc object and invoke the rpc
          InsuranceRpc insuranceRpc = new InsuranceRpc(rpcBroker);
          String result = insuranceRpc.getInsuranceInfo(dfn);
          System.out.println(result);
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