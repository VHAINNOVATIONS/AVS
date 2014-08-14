package gov.va.med.lom.vistabroker.test;
/*
 * TestPatientLookupRpc.java
 * 
 * Author: Rob Durkin (rob.durkin@med.va.gov)
 * Version 1.0 (03/12/2007)
 *  
 */
import gov.va.med.lom.javaUtils.misc.Console;
import gov.va.med.lom.vistabroker.patient.data.Patient;
import gov.va.med.lom.vistabroker.security.ISecurityContext;
import gov.va.med.lom.vistabroker.security.SecurityContextFactory;
import gov.va.med.lom.vistabroker.service.PatientVBService;
import gov.va.med.lom.vistabroker.util.VistaBrokerServiceFactory;

import java.util.List;
import java.util.ResourceBundle;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class TestPatientLookupRpc {
  
  private static final Log log = LogFactory.getLog(TestPatientLookupRpc.class);
  
  static void printUsage() {
    System.out.println("Usage: java TestPatientLookupRpc AUTH_PROPS");
    System.out.println("where AUTH_PROPS is the name of a properties file containing VistA connection info.");
  }
  
  /*
   * Prints the dfn, name, and ssn for patients 
   * matching the given search string. 
   */
  public static void main(String[] args) {
    PatientVBService patientRpcs = VistaBrokerServiceFactory.getPatientVBService();
    
    String division = null;
    String duz = null;
    @SuppressWarnings("unused")
    String securityId = null;
    if (args.length != 1) {
      printUsage();
      System.exit(1);
    } else {
      ResourceBundle res = ResourceBundle.getBundle(args[0]);
      division = res.getString("division");
      duz = res.getString("duz");
      securityId = res.getString("securityID");
    }
    try {
        // Set security context
        ISecurityContext securityContext = SecurityContextFactory.createDuzSecurityContext(division, duz);
  
      while (true) {
        String str = Console.readLine("Search string: "); 
        List<Patient> patients = (List<Patient>)patientRpcs.getSubSetOfPatients(securityContext, str, 1).getCollection();
        for(Patient patient : patients)
          System.out.println(patient.getName() + " (" + patient.getDfn() + ")");
      }
    } catch(Exception e) {
      System.err.println(e.getMessage());
      log.error("Error occurred while calling RPC: ", e);
    }      
  }

}