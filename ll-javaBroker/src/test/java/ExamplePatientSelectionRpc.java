/*
 * ExamplePatientSelectionRpc.java
 * 
 * Author: Rob Durkin (rob.durkin@med.va.gov)
 * Version 1.3 (07/05/2005)
 *  
 * Prints the dfn, name, ssn, and birthdate of patients 
 * matching the specified ssn or 'last 5'.
 *
 * Usage: java ExamplePatientSelectionRpc SSN|LAST5 AUTH_PROPS
 * where SSN is a patient's Social Security Number and Last 5
 *       is the first initial of the patient's last name  
 *       plus the last 4 digits of the patient's ssn.
 *       AUTH_PROPS is the name of a properties file containing VistA sign-on info.
 * 
 * Required Libraries:
 *   javaBroker.jar 
 *   
 */
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Date;
import java.util.ResourceBundle;
import java.text.ParseException;

import gov.va.med.lom.javaBroker.rpc.user.*;
import gov.va.med.lom.javaBroker.rpc.user.models.*;
import gov.va.med.lom.javaBroker.rpc.patient.*;
import gov.va.med.lom.javaBroker.rpc.patient.models.*;
import gov.va.med.lom.javaBroker.rpc.BrokerException;
import gov.va.med.lom.javaBroker.rpc.RpcBroker;
import gov.va.med.lom.javaBroker.util.DateUtils;

public class ExamplePatientSelectionRpc {
  
  /*
   * Returns the DFN of the first patient in the list (if any) with the specified ssn.
   */
  public static String getPatientDfnBySsn(RpcBroker rpcBroker, String ssn) throws BrokerException {
    // Create a patient selection rpc and list patients with the specified ssn 
    PatientSelectionRpc patientSelectionRpc = new PatientSelectionRpc(rpcBroker);
    PatientList patientList = patientSelectionRpc.listPtByFullSSN(ssn);
    PatientListItem[] patientListItems = patientList.getPatientListItems();
    String dfn = null;
    if (patientListItems.length > 0)
      dfn = patientListItems[0].getDfn();
    return dfn;
  }  
  
  /*
   * Prints the dfn, name, ssn, and birthdate for the first patient 
   * matching the specified ssn, or for all patients with the matching 'last 5'. 
   */
  public static void main(String[] args) {
    // If user didn't pass the patient's ssn/last5 and name of the auth properties file, then print usage and exit
    String ssn = null;
    String server = null;
    int port = 0;
    String access = null;
    String verify = null;
    if (args.length != 2) {
      System.out.println("Usage: java ExamplePatientSelectionRpc SSN|LAST5 AUTH_PROPS");
      System.out.println("where SSN is a patient's Social Security Number and Last 5");
      System.out.println("      is the first initial of the patient's last name");  
      System.out.println("      plus the last 4 digits of the patient's ssn.");
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
        // Create a patient selection rpc object
        PatientSelectionRpc patientSelectionRpc = new PatientSelectionRpc(rpcBroker); 
        patientSelectionRpc.setReturnRpcResult(true);
        PatientList patientList = null;
        PatientListItem[] patientListItems = null;
        /*
        if (args[0].length() == 5) {
          // Passed in value is the 'last 5', list patients with this last5
          patientList = patientSelectionRpc.listPtByLast5(ssn);
        } else {
          // Passed in value is the ssn, list first patient with this ssn         
          patientList = patientSelectionRpc.listPtByFullSSN(ssn);
        }
        */
        
        Date today = new Date();
        Calendar startDate = Calendar.getInstance();
        startDate.set(Calendar.DAY_OF_MONTH, 30);
        Calendar endDate = Calendar.getInstance();
        endDate.set(Calendar.DAY_OF_MONTH, 30);
        patientList = patientSelectionRpc.listPtByClinic("4795", startDate.getTime(), endDate.getTime());
        
        patientListItems = patientList.getPatientListItems();
        if (patientListItems.length > 0) {
          for(int i = 0; i < patientListItems.length; i++) {
            // Print patient list results
            System.out.println("DFN: " + patientListItems[i].getDfn());
            // Get and print patient info
            PatientInfoRpc patientInfoRpc = new PatientInfoRpc(rpcBroker);
            PatientInfo patientInfo = patientInfoRpc.getPatientInfo(patientListItems[i].getDfn()); 
            System.out.println("Name: " + patientInfo.getName());
            System.out.println("SSN: " + patientInfo.getSsn());
            try {
              System.out.println("Birthdate: " + DateUtils.toEnglishDate(patientListItems[i].getDate()));
            } catch(ParseException pe) {}
            System.out.println("Age: " + patientInfo.getAge());
            System.out.println("Sex: " + patientInfo.getSex());
            System.out.println("Location: " + patientInfo.getLocation());
            System.out.println("Room/Bed: " + patientInfo.getRoomBed());
            System.out.println("SC (%): " + patientInfo.getScPct());
            try {
              System.out.println("Deceased: " + DateUtils.toEnglishDate(patientInfo.getDeceasedDate()));
            } catch(ParseException pe) {}            
            System.out.println("Veteran: " + patientInfo.getVeteran());
            // Check similar record status for this patient
            SimilarRecordsStatus similarRecordsStatus = patientSelectionRpc.getSimilarRecordsFound(patientListItems[i].getDfn());
            if (similarRecordsStatus.getExists()) { 
              System.out.println("Similar Last Name Alert!");
              System.out.println(similarRecordsStatus.getMessage());
            }
          }
          System.out.println("RPC Result: " + patientList.getRpcResult());
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