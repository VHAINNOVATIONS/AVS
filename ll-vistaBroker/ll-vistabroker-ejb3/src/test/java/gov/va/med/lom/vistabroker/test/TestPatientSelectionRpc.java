package gov.va.med.lom.vistabroker.test;
/*
 * TestPatientSelectionRpc.java
 * 
 * Author: Rob Durkin (rob.durkin@med.va.gov)
 * Version 1.0 (03/12/2007)
 *  
 */
import gov.va.med.lom.javaUtils.misc.DateUtils;
import gov.va.med.lom.vistabroker.patient.data.Patient;
import gov.va.med.lom.vistabroker.patient.data.PatientInfo;
import gov.va.med.lom.vistabroker.patient.data.SimilarRecordsStatus;
import gov.va.med.lom.vistabroker.security.ISecurityContext;
import gov.va.med.lom.vistabroker.security.SecurityContextFactory;
import gov.va.med.lom.vistabroker.service.PatientVBService;
import gov.va.med.lom.vistabroker.util.VistaBrokerServiceFactory;

import java.util.List;
import java.util.ResourceBundle;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class TestPatientSelectionRpc {
  
  private static final Log log = LogFactory.getLog(TestPatientSelectionRpc.class);
  
  static void printUsage() {
    System.out.println("Usage: java TestPatientSelectionRpc SSN|LAST5 AUTH_PROPS");
    System.out.println("where SSN is a patient's Social Security Number and Last 5");
    System.out.println("      is the first initial of the patient's last name");  
    System.out.println("      plus the last 4 digits of the patient's ssn.");
    System.out.println("      AUTH_PROPS is the name of a properties file containing VistA sign-on info.");
  }
  
  /*
   * Prints the dfn, name, ssn, and birthdate for the first patient 
   * matching the specified ssn, or for all patients with the matching 'last 5'. 
   */
  public static void main(String[] args) {
    PatientVBService patientRpcs = VistaBrokerServiceFactory.getPatientVBService();
    
    String division = "605";
    String duz = "9276";
    /*
    @SuppressWarnings("unused")
    String securityId = null;
    String ssn = null;
    String last5 = null;
    if (args.length != 2) {
      printUsage();
      System.exit(1);
    } else {
      if (args[0].length() == 5) 
        last5 = args[0];  
      else
        ssn = args[0];
      ResourceBundle res = ResourceBundle.getBundle(args[1]);
      division = res.getString("division");
      duz = res.getString("duz");
      securityId = res.getString("securityID");
    }
    */
    try {
        // Set security context
        ISecurityContext securityContext = SecurityContextFactory.createDuzSecurityContext(division, duz);
      String last5 = "F7804";
      String ssn = "";
      List<Patient> patients = null;
      if (last5 != null) {
        // Passed in value is the 'last 5', list patients with this last5
        patients = (List<Patient>)patientRpcs.listPtByLast5(securityContext, last5).getCollection();
      } else {
        // Passed in value is the ssn, list first patient with this ssn         
        patients = (List<Patient>)patientRpcs.listPtByFullSSN(securityContext, ssn).getCollection();
      }
      //patients = (List<Patient>)patientRpcs.listPtByWard(securityContext, "104").getCollection();
      for(Patient patient : patients) {
        // Print patient list results
        System.out.println("DFN: " + patient.getDfn());
        System.out.println("Name: " + patient.getName());
        System.out.println("Location: " + patient.getLocation());
        /*
        // Get and print patient info
        PatientInfo patientInfo = patientRpcs.getPatientInfo(securityContext, patient.getDfn()).getPayload(); 
        System.out.println("Name: " + patientInfo.getName());
        System.out.println("SSN: " + patientInfo.getSsn());
        try {
          System.out.println("Birthdate: " + DateUtils.toEnglishDate(patient.getDate()));
        } catch(Exception pe) {}
        System.out.println("Age: " + patientInfo.getAge());
        System.out.println("Sex: " + patientInfo.getSex());
        System.out.println("Location: " + patientInfo.getLocation());
        System.out.println("Room/Bed: " + patientInfo.getRoomBed());
        System.out.println("SC (%): " + patientInfo.getScPct());
        try {
          System.out.println("Deceased: " + DateUtils.toEnglishDate(patientInfo.getDeceasedDate()));
        } catch(Exception pe) {}            
        System.out.println("Veteran: " + patientInfo.getVeteran());
        // Check similar record status for this patient
        SimilarRecordsStatus similarRecordsStatus = patientRpcs.getSimilarRecordsFound(securityContext, patient.getDfn()).getPayload();
        if (similarRecordsStatus.getExists()) { 
          System.out.println("Similar Last Name Alert!");
          System.out.println(similarRecordsStatus.getMessage());
        }
        */
      }
    } catch(Exception e) {
      System.err.println(e.getMessage());
      log.error("Error occurred while calling RPC: ", e);
    }       
  }

}