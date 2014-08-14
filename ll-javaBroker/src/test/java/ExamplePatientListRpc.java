/*
 * ExamplePatientSelectionRpc.java
 * 
 * Author: Rob Durkin (rob.durkin@med.va.gov)
 * Version 1.0 (09/14/2006)
 *  
 * Lists all patients for a particular clinic.
 *
 * Usage: java ExamplePatientListRpc AUTH_PROPS
 * where AUTH_PROPS is the name of a properties file containing VistA sign-on info.
 * 
 * Required Libraries:
 *   javaBroker.jar 
 *   
 */
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

import gov.va.med.lom.javaBroker.util.Console;

public class ExamplePatientListRpc {
  
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
   * Prints the dfn, name, ssn, and birthdate for each of the patients returned for the clinic. 
   */
  public static void main(String[] args) {
    String server = null;
    int port = 0;
    String access = null;
    String verify = null;
    if (args.length != 1) {
      System.out.println("Usage: java ExamplePatientSelectionRpc AUTH_PROPS");
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
        // Create a patient selection rpc object
        PatientSelectionRpc patientSelectionRpc = new PatientSelectionRpc(rpcBroker); 
        patientSelectionRpc.setReturnRpcResult(true);
        PatientList patientList = null;
        PatientListItem[] patientListItems = null;
        String clinicIen = Console.readLine("Clinic IEN: ");
        //patientList = patientSelectionRpc.listPtByClinic(clinicIen, new Date(), new Date());
        patientList = patientSelectionRpc.listPtByWard(clinicIen);
        patientListItems = patientList.getPatientListItems();
        System.out.println("# Patients: " + patientListItems.length);
        if (patientListItems.length > 0) {
          for(int i = 0; i < patientListItems.length; i++) {
            // Print patient list results
            System.out.println("DFN: " + patientListItems[i].getDfn());
            System.out.println("Name: " + patientListItems[i].getName());
            System.out.println("Location: " + patientListItems[i].getLocation());
            System.out.println("--------------");
            /*
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
            */
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