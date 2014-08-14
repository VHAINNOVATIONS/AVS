/*
 * ExampleDemographicsRpc.java
 * 
 * Author: Rob Durkin (rob.durkin@med.va.gov)
 * Version 1.3 (02/07/2006)
 *  
 * Prints the patient's demographics and inpatient information.
 *
 * Usage: java ExampleDemographicsRpc -ssn=SSN | -dfn=DFN AUTH_PROPS
 * where SSN is a patient's Social Security Number.
 *       DFN is a patient's VistA internal entry number.
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
import gov.va.med.lom.javaBroker.util.StringUtils;
import gov.va.med.lom.javaBroker.util.DateUtils;

public class ExampleDemographicsRpc {
  
  static void printUsage() {
    System.out.println("Usage: java ExampleDemographicsRpc -ssn=SSN | -dfn=DFN AUTH_PROPS");
    System.out.println("where SSN is a patient's Social Security Number.");
    System.out.println("      DFN is a patient's VistA internal entry number.");
    System.out.println("      AUTH_PROPS is the name of a properties file containing VistA sign-on info.");
  }
  
  /*
   * Prints demographic and inpatient info for the first patient matching the specified ssn. 
   */
  public static void main(String[] args) {
    // If user didn't pass the patient's ssn and name of the auth properties file, then print usage and exit
    String dfn = null;
    String ssn = null;
    String server = null;
    int port = 0;
    String access = null;
    String verify = null;
    if (args.length != 1) {
      printUsage();
      System.exit(1);
    } else {
      /*
      if (args[0].startsWith("-ssn"))
        ssn = StringUtils.piece(args[0], '=', 2);
      else if (args[0].startsWith("-dfn"))
        dfn = StringUtils.piece(args[0], '=', 2);
      else {
        printUsage();
        System.exit(1);
      }
      */  
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
        if (ssn != null) {
          // Get the patient's dfn from the ssn
          dfn = ExamplePatientSelectionRpc.getPatientDfnBySsn(rpcBroker, ssn);
        }
        
        dfn = "10128436";
        
        if (dfn != null) {
          // Create a demographics rpc object and invoke the rpc
          DemographicsRpc demographicsRpc = new DemographicsRpc(rpcBroker);
          demographicsRpc.setReturnRpcResult(true);
          Demographics demographics = demographicsRpc.getDemographics(dfn); 
          // Print the patient's demographics
          System.out.println("---------- PATIENT DEMOGRAPHICS ------------");
          System.out.println("Name: " + demographics.getName());
          System.out.println("Sex: " + demographics.getSex());
          System.out.println("Sensitive: " + demographics.getSensitive());
          System.out.println("Restricted: " + demographics.getRestricted());
          System.out.println("ICN: " + demographics.getIcn());
          System.out.println("SSN: " + demographics.getSsn());
          try {
            System.out.println("DOB: " + DateUtils.toEnglishDate(demographics.getDob()));
          } catch(ParseException pe) {}
          try {
            System.out.println("Deceased Date: " + DateUtils.toEnglishDate(demographics.getDeceasedDate()));
          } catch(ParseException pe) {}
          System.out.println("Age: " + demographics.getAge());
          System.out.println("Location IEN: " + demographics.getLocationIen());
          System.out.println("Location: " + demographics.getLocation());
          System.out.println("Ward/Service: " + demographics.getSpecialty());
          System.out.println("Room/Bed: " + demographics.getRoomBed());
          System.out.println("Specialty IEN: " + demographics.getSpecialtyIen());
          System.out.println("CWAD: " + demographics.getCwad());
          try {
            System.out.println("Admit Time: " + DateUtils.toEnglishDateTime(demographics.getAdmitTime()));
          } catch(ParseException pe) {}
          System.out.println("Service Connected: " + demographics.getServiceConnected());
          System.out.println("Service Connected %: " + demographics.getServiceConnectedPercent());
          System.out.println("Primary Team: " + demographics.getPrimaryTeam());
          System.out.println("Primary Provider: " + demographics.getPrimaryProvider());
          System.out.println("Attending: " + demographics.getAttending());
          System.out.println("RPC Result: " + demographics.getRpcResult());
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