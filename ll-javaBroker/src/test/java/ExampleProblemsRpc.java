/*
 * ExampleProblemsRpc.java
 * 
 * Author: Rob Durkin (rob.durkin@med.va.gov)
 * Version 1.3 (07/05/2005)
 *  
 * Prints the list of problems for the patient.
 *
 * Usage: java ExampleProblemsRpc SSN AUTH_PROPS
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

public class ExampleProblemsRpc {
  
  /*
   * Prints the problems for the first patient matching the specified ssn. 
   */
  public static void main(String[] args) {
    // If user didn't pass the patient's ssn and name of the auth properties file, then print usage and exit
    String server = null;
    int port = 0;
    String access = null;
    String verify = null;
    if (args.length != 1) {
      System.out.println("Usage: java ExampleProblemsRpc AUTH_PROPS");
      System.out.println(" where AUTH_PROPS is the name of a properties file containing VistA sign-on info.");
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
        // Get the patient's dfn from the ssn
        String dfn = "10014774";
        if (dfn != null) {
          // Create a problems rpc object and invoke the rpc
          ProblemsRpc problemsRpc = new ProblemsRpc(rpcBroker);
          problemsRpc.setReturnRpcResult(true);
          // Retrieve all problems (active and inactive)
          ProblemsList problemsList = problemsRpc.getProblems(dfn, ProblemsRpc.ALL);
          Problem[] problems = problemsList.getProblems();
          // Print the list of problems
          for(int i = 0; i < problems.length; i++) {
            System.out.println("Description: " + problems[i].getDescription());
            System.out.println("Code: " + problems[i].getCode());
            System.out.println("Status: " + problems[i].getStatus());
            System.out.println("Onset Date: " + problems[i].getOnsetDateStr());
            System.out.println("Last Updated: " + problems[i].getLastUpdatedStr());
            System.out.println("SC Status: " + problems[i].getScStatus());
            System.out.println("SC Conditions: " + problems[i].getScConditions());
            System.out.println("Transcribed: " + problems[i].getTranscribed());
            System.out.println("Location IEN: " + problems[i].getLocationIen());
            System.out.println("Location: " + problems[i].getLocation());
            System.out.println("Location Type: " + problems[i].getLocationType());
            System.out.println("Provider IEN: " + problems[i].getProviderIen());
            System.out.println("Provider: " + problems[i].getProvider());
            System.out.println("Service IEN: " + problems[i].getServiceIen());
            System.out.println("Service: " + problems[i].getService());
            System.out.println("Version: " + problems[i].getVersion());
            if (problems[i].getComments() != null) {
              System.out.println("Comments:");
              for (int j = 0; j < problems[i].getComments().length; j++)
                System.out.println(problems[i].getComments()[j]);
            }
            String detail = problemsRpc.getProblemDetail(dfn, problems[i].getIen());
            System.out.println("Detail:");
            System.out.println(detail);
            System.out.println();
          }
          System.out.println("RPC Result: " + problemsList.getRpcResult());
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