/*
 * ExampleRpcBrokerApp.java
 * 
 * Author: Rob Durkin (rob.durkin@med.va.gov)
 * Version 1.2 (06/22/2005)
 *  
 * This example RPC broker app connects the Java client broker to the 
 * VistA broker server and does the following:
 *
 * 1. Connects to the broker server
 * 2. Prints the intro message
 * 3. Prints signon setup results (server info)
 * 4. Logs the user in, then prints the user greeting
 * 5. Prints info about the user
 * 6. Selects a test patient by SSN
 * 7. Prints basic info about the patient
 * 8. Prints the patient's problem list
 * 9. Prints the patient inquiry text 
 *
 * Usage: java ExampleRpcBrokerApp SSN AUTH_PROPS
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

public class ExampleRpcBrokerApp {
  
  public static void main(String[] args) {
    // If user didn't pass the patient's ssn and name of the auth properties file, then print usage and exit
    String ssn = null;
    String server = null;
    int port = 0;
    String access = null;
    String verify = null;
    if (args.length != 2) {
      System.out.println("Usage: java ExampleRpcBrokerApp SSN AUTH_PROPS");
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
      // Create an RPC broker and connect to the server
      RpcBroker rpcBroker = new RpcBroker(server, port);
      rpcBroker.connect();
   
      // Do the signon setup and print the intro message and server info
      VistaSignonSetupRpc vistaSignonSetupRpc = new VistaSignonSetupRpc(rpcBroker);
      VistaSignonSetupResult vistaSignonSetupResult = vistaSignonSetupRpc.doVistaSignonSetup();
      System.out.println("---------- VistA Sign-On Setup Info ----------");
      System.out.println("Server: " + vistaSignonSetupResult.getServer());
      System.out.println("Volume: " + vistaSignonSetupResult.getVolume());
      System.out.println("UCI: " + vistaSignonSetupResult.getUci());
      System.out.println("Port: " + vistaSignonSetupResult.getPort() + "\n");
      System.out.println(vistaSignonSetupResult.getIntroMessage() + "\n");
  
      // Do signon 
      VistaSignonRpc vistaSignonRpc = new VistaSignonRpc(rpcBroker);
      VistaSignonResult vistaSignonResult = vistaSignonRpc.doVistaSignon(access, verify); 
      if (vistaSignonResult.getSignonSucceeded()) {
  
        System.out.println("---------- VistA Sign-On ----------"); 
        // Print the user greeting    
        System.out.println(vistaSignonResult.getGreeting());
         
        // Get and print Vista user info
        VistaUserRpc vistaUserRpc = new VistaUserRpc(rpcBroker);
        VistaUser vistaUser = vistaUserRpc.getVistaUser();
        System.out.println("\n--------- User Info ---------");
        System.out.println("DUZ: " + vistaUser.getDuz());
        System.out.println("Name: " + vistaUser.getName());
        System.out.println("Title: " + vistaUser.getTitle());
  
        // Select a patient by SSN
        PatientSelectionRpc patientSelectionRpc = new PatientSelectionRpc(rpcBroker);
        PatientList patientList = patientSelectionRpc.listPtByFullSSN(ssn);
        PatientListItem[] patientListItems = patientList.getPatientListItems();
        if (patientListItems.length > 0) {
          String dfn = patientListItems[0].getDfn();
       
          // Get and print basic info for the patient
          PatientInfoRpc patientInfoRpc = new PatientInfoRpc(rpcBroker);
          PatientInfo patientInfo = patientInfoRpc.getPatientInfo(dfn);
          System.out.println("\n----------- Patient Info ----------");
          System.out.println("DFN: " + patientInfo.getDfn());
          System.out.println("Name: " + patientInfo.getName());
          System.out.println("Sex: " + patientInfo.getSex());
          System.out.println("SSN: " + patientInfo.getSsn());
          try {
            System.out.println("DOB: " + DateUtils.toEnglishDate(patientInfo.getDob()));
          } catch(ParseException pe) {}
          System.out.println("Age: " + patientInfo.getAge());
  
          // Get and print patient's problem list
          ProblemsRpc problemsRpc = new ProblemsRpc(rpcBroker);
          ProblemsList problemsList = problemsRpc.getProblems(dfn, ProblemsRpc.ALL);
          Problem[] problems = problemsList.getProblems();
          System.out.println("\n---------- Problem List ----------");
          for(int i = 0; i < problems.length; i++) {
            System.out.println((i+1) + ". " + problems[i].getDescription() + 
                               "  Status: " + problems[i].getStatus() + ")");
          }
  
          // Print the patient inquiry
          PatientInquiryRpc patientInquiryRpc = new PatientInquiryRpc(rpcBroker); 
          System.out.println("\n---------- Patient Inquiry ----------");
          System.out.println(patientInquiryRpc.getPatientInquiry(dfn)); 
        } else
          System.out.println("Cannot find patient with the supplied SSN.");
        
      } else {
        // Signon failed, print reason
        System.out.println(vistaSignonResult.getMessage());
      }
      // Close the connection to the broker
      rpcBroker.disconnect();
    } catch(BrokerException be) {
      System.err.println(be.getMessage());
      System.err.println("Action: " + be.getAction());
      System.err.println("Code: " + be.getCode());
      System.err.println("Mnemonic: " + be.getMnemonic());
    }      
  }

}