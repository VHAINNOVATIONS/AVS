package gov.va.med.lom.vistabroker.test;
/*
 * TestLabsRpc.java
 * 
 * Author: Rob Durkin (rob.durkin@med.va.gov)
 * Version 1.0 (03/12/2007)
 *  
 */
import gov.va.med.lom.javaUtils.misc.DateUtils;
import gov.va.med.lom.vistabroker.patient.data.CumulativeLabResults;
import gov.va.med.lom.vistabroker.patient.data.LabResultTestType;
import gov.va.med.lom.vistabroker.patient.data.LabTestResult;
import gov.va.med.lom.vistabroker.security.ISecurityContext;
import gov.va.med.lom.vistabroker.security.SecurityContextFactory;
import gov.va.med.lom.vistabroker.service.PatientVBService;
import gov.va.med.lom.vistabroker.util.VistaBrokerServiceFactory;

import java.util.List;
import java.util.ResourceBundle;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class TestLabsRpc {
  
  private static final Log log = LogFactory.getLog(TestLabsRpc.class);
  
  static void printUsage() {
    System.out.println("Usage: java TestLabsRpc DFN AUTH_PROPS");
    System.out.println("where DFN is the DFN of the patient.");
    System.out.println("      AUTH_PROPS is the name of a properties file containing VistA connection info.");
  }
  
  /*
   * Prints lab results for the first patient matching the specified ssn. 
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
  
      List<LabTestResult> recentLabsList = (List<LabTestResult>)patientRpcs.getRecentLabs(securityContext, dfn).getCollection();
      System.out.println("---------- RECENT LAB RESULTS ------------");
      for (LabTestResult recentLab : recentLabsList) {
        System.out.println("Name: " + recentLab.getName());
        try {
          System.out.println("Date: " + DateUtils.toEnglishDateTime(recentLab.getDate()));
        } catch(Exception pe) {}            
        System.out.println("Status: " + recentLab.getStatus());
        System.out.println("Result: " + recentLab.getResult() + "\n");
      }
      System.out.println("\n\n---------- CUMULATIVE LAB RESULTS ------------");
      CumulativeLabResults cumulativeLabResults = patientRpcs.getCumulativeLabResults(securityContext, dfn, 183, null, null).getPayload();
      List<LabResultTestType> labResultTestTypes = cumulativeLabResults.getLabResultTestTypes();
      for (LabResultTestType labResultTestType : labResultTestTypes) {
        System.out.println(labResultTestType.getName() + "(Start: " + labResultTestType.getStart() + ")");
      }
      System.out.println(cumulativeLabResults.getText());
    } catch(Exception e) {
      System.err.println(e.getMessage());
      log.error("Error occurred while calling RPC: ", e);
    }      
  }

}