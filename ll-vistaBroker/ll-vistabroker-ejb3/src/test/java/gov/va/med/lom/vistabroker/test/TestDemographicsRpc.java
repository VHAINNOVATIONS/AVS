package gov.va.med.lom.vistabroker.test;
/*
 * TestDemographicsRpc.java
 * 
 * Author: Rob Durkin (rob.durkin@med.va.gov)
 * Version 1.0 (03/08/2007)
 *  
 * Connects to VistaBroker J2EE service and retrieves and prints
 * the patient's demographics and inpatient information.
 *
 * Usage: java TestDemographicsRpc DFN AUTH_PROPS
 * where DFN is the patient's VistA internal entry number.
 *       AUTH_PROPS is the name of a properties file containing VistA connection info.
 * 
 * Required Libraries:
 *   javaUtils.jar  (http://svn.lom.med.va.gov/repos/llvamc/branches/durkin/javaUtils)
 */

import gov.va.med.lom.vistabroker.patient.data.Demographics;
import gov.va.med.lom.vistabroker.security.ISecurityContext;
import gov.va.med.lom.vistabroker.security.SecurityContextFactory;
import gov.va.med.lom.vistabroker.service.PatientVBService;
import gov.va.med.lom.vistabroker.util.VistaBrokerServiceFactory;

import java.util.ResourceBundle;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class TestDemographicsRpc {
  
  private static final Log log = LogFactory.getLog(TestDemographicsRpc.class);
  
  static void printUsage() {
    System.out.println("Usage: java TestDemographicsRpc DFN AUTH_PROPS");
    System.out.println("where DFN is the patient's VistA internal entry number.");
    System.out.println("      AUTH_PROPS is the name of a properties file containing VistA connection info.");
  }
  
  /*
   * Prints demographic and inpatient info for the patient. 
   */
  public static void main(String[] args) {
    PatientVBService patientRpcs = VistaBrokerServiceFactory.getPatientVBService();
    
    String dfn = null;
    String division = null;
    String duz = null;
    @SuppressWarnings("unused")
    String securityId = null;
    if (args.length != 2) {
      printUsage();
      System.exit(1);
    } else {
      dfn = args[0];
      ResourceBundle res = ResourceBundle.getBundle(args[1]);
      division = res.getString("division");
      duz = res.getString("duz");
    }
    try {
      // Set security context
      ISecurityContext securityContext = SecurityContextFactory.createDuzSecurityContext(division, duz);
  
      //System.out.println(new java.util.Date().getTime());
      Demographics demographics = patientRpcs.getDemographics(securityContext, dfn).getPayload(); 
      //System.out.println(new java.util.Date().getTime());
      // Print the patient's demographics
      System.out.println("---------- PATIENT DEMOGRAPHICS ------------");
      
      System.out.println("dfn: " + demographics.getDfn());
      System.out.println("name: " + demographics.getName());
      System.out.println("sex: " + demographics.getSex());
      System.out.println("sensitive: " + demographics.getSensitive());
      System.out.println("restricted: " + demographics.getRestricted());
      System.out.println("icn: " + demographics.getIcn());
      System.out.println("ssn: " + demographics.getSsn());
      System.out.println("dob: " + demographics.getDob());
      System.out.println("dobStr: " + demographics.getDobStr());
      System.out.println("deceasedDate: " + demographics.getDeceasedDate());
      System.out.println("deceasedDateStr: " + demographics.getDeceasedDateStr());
      System.out.println("age: " + demographics.getAge());
      System.out.println("locationIen: " + demographics.getLocationIen());
      System.out.println("location: " + demographics.getLocation());
      System.out.println("specialty: " + demographics.getSpecialty());
      System.out.println("roomBed: " + demographics.getRoomBed());
      System.out.println("specialtyIen: " + demographics.getSpecialtyIen());
      System.out.println("cwad: " + demographics.getCwad());
      System.out.println("admitTime: " + demographics.getAdmitTime());
      System.out.println("admitTimeStr: " + demographics.getAdmitTimeStr());
      System.out.println("serviceConnected: " + demographics.getServiceConnected());
      System.out.println("serviceConnectedPercent: " + demographics.getServiceConnectedPercent());
      System.out.println("primaryTeam: " + demographics.getPrimaryTeam());
      System.out.println("primaryProvider: " + demographics.getPrimaryProvider());
      System.out.println("attending: " + demographics.getAttending());  

     

    } catch(Exception e) {
      System.err.println(e.getMessage());
      log.error("Error occurred while calling RPC: ", e);
    }      
  }

}