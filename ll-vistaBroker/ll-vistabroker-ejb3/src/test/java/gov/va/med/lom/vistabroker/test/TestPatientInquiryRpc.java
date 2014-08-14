package gov.va.med.lom.vistabroker.test;
/*
 * TestPatientInquiryRpc.java
 * 
 * Author: Rob Durkin (rob.durkin@med.va.gov)
 * Version 1.0 (03/24/2007)
 *  
 */
import gov.va.med.lom.vistabroker.security.ISecurityContext;
import gov.va.med.lom.vistabroker.security.SecurityContextFactory;
import gov.va.med.lom.vistabroker.service.PatientVBService;
import gov.va.med.lom.vistabroker.util.VistaBrokerServiceFactory;

import java.util.ResourceBundle;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class TestPatientInquiryRpc {
  
  private static final Log log = LogFactory.getLog(TestPatientInquiryRpc.class);
  
  static void printUsage() {
    System.out.println("Usage: java TestPatientInquiryRpc DFN AUTH_PROPS");
    System.out.println("where DFN is the DFN of the patient.");
    System.out.println("      AUTH_PROPS is the name of a properties file containing VistA connection info.");
  }
  
  /*
   * Prints the patient inquiry for the patient. 
   */
  public static void main(String[] args) {
    PatientVBService patientRpcs = VistaBrokerServiceFactory.getPatientVBService();
    
    String division = null;
    String duz = null;
    String dfn = null;
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
      securityId = res.getString("securityID");
    }
    try {
      // Set security context
      ISecurityContext securityContext = SecurityContextFactory.createDuzSecurityContext(division, duz);
      //patientRpcs.setDefaultContext("ALSI CPRS COM OBJ");
      
      // Invoke the patient inquiry RPC
      System.out.println(patientRpcs.getPatientInquiry(securityContext, dfn));
    } catch(Exception e) {
      System.err.println(e.getMessage());
      log.error("Error occurred while calling RPC: ", e);
    }   
  }

}