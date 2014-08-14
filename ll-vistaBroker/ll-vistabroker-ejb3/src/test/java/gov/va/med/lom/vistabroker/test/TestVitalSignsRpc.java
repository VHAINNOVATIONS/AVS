package gov.va.med.lom.vistabroker.test;
/*
 * TestVitalSignsRpc.java
 * 
 * Author: Rob Durkin (rob.durkin@med.va.gov)
 * Version 1.0 (03/12/2007)
 *  
 */
import gov.va.med.lom.javaUtils.misc.DateUtils;
import gov.va.med.lom.vistabroker.patient.data.VitalSigns;
import gov.va.med.lom.vistabroker.security.ISecurityContext;
import gov.va.med.lom.vistabroker.security.SecurityContextFactory;
import gov.va.med.lom.vistabroker.service.PatientVBService;
import gov.va.med.lom.vistabroker.util.VistaBrokerServiceFactory;

import java.util.ResourceBundle;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class TestVitalSignsRpc {
  
  static void printUsage() {
    System.out.println("Usage: java TestVitalSignsRpc DFN AUTH_PROPS");
    System.out.println("where DFN is the DFN of the patient.");
    System.out.println("      AUTH_PROPS is the name of a properties file containing VistA connection info.");
  }
  
  private static final Log log = LogFactory.getLog(TestVitalSignsRpc.class);
  
  /*
   * Prints the vital signs for the patient. 
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
  
      VitalSigns vitalSigns = patientRpcs.getVitalSigns(securityContext, dfn).getPayload();
      // Print out the vital signs
      try {
        System.out.println("---------- VITAL SIGNS ----------");
        System.out.println("Temperature: " + vitalSigns.getTemperature() + 
                           " (" + DateUtils.toEnglishDate(vitalSigns.getTemperatureDate()) + ")");
        System.out.println("Pulse: " + vitalSigns.getPulse() + 
                           " (" + DateUtils.toEnglishDate(vitalSigns.getPulseDate()) + ")");
        System.out.println("Respirations: " + vitalSigns.getRespirations() + 
                           " (" + DateUtils.toEnglishDate(vitalSigns.getRespirationsDate()) + ")");
        System.out.println("BP: " + vitalSigns.getBpSystolic() + "/" + vitalSigns.getBpDiastolic() +
                           " (" + DateUtils.toEnglishDate(vitalSigns.getBpDate()) + ")");
        System.out.println("Height: " + vitalSigns.getHeight() + 
                           " (" + DateUtils.toEnglishDate(vitalSigns.getHeightDate()) + ")");
        System.out.println("Weight: " + vitalSigns.getWeight() + 
                           " (" + DateUtils.toEnglishDate(vitalSigns.getWeightDate()) + ")");   
        System.out.println("Pain Index: " + vitalSigns.getPainIndex() + 
                           " (" + DateUtils.toEnglishDate(vitalSigns.getPainIndexDate()) + ")");
      } catch(Exception pe) {}
    } catch(Exception e) {
      System.err.println(e.getMessage());
      log.error("Error occurred while calling RPC: ", e);
    }      
  }

}