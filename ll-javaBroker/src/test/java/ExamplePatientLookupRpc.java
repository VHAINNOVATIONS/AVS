/*
 * ExamplePatientLookupRpc.java
 * 
 * Author: Rob Durkin (rob.durkin@med.va.gov)
 * Version 1.0 (05/15/2006)
 *  
 * List patients based on a partial name look-up.
 *
 * Usage: java ExamplePatientLookupRpc AUTH_PROPS
 * where AUTH_PROPS is the name of a properties file containing VistA sign-on info.
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

import gov.va.med.lom.javaBroker.util.Console;

public class ExamplePatientLookupRpc {
  
  /*
   * Prints the dfn, name, and ssn for patients 
   * matching the given search string. 
   */
  public static void main(String[] args) {
    // If user didn't pass the patient's ssn/last5 and name of the auth properties file, then print usage and exit
    String server = null;
    int port = 0;
    String access = null;
    String verify = null;
    if (args.length != 1) {
      System.out.println("Usage: java ExampleListPatientRpc AUTH_PROPS");
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
        while (true) {
          String str = Console.readLine("Search string: "); 
          if (str.length() > 0) {
            PatientSelectionRpc patientSelectionRpc = new PatientSelectionRpc(rpcBroker); 
            patientSelectionRpc.setReturnRpcResult(true);
            PatientList patientList = null;
            PatientListItem[] patientListItems = null;
            patientList = patientSelectionRpc.getSubSetOfPatients(str, 1);
            patientListItems = patientList.getPatientListItems();
            if (patientListItems.length > 0) {
              for(int i = 0; i < patientListItems.length; i++) {
                System.out.println(patientListItems[i].getName() +  
                                   " (" + patientListItems[i].getDfn() + ")");
              }
            } else {
              System.out.println("No matching patient for the specified search string.");
            }
          } else
            break;
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