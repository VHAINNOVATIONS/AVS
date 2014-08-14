package gov.va.med.lom.vistabroker.test;
/*
 * TestAllergiesReactantsRpc.java
 * 
 * Author: Rob Durkin (rob.durkin@med.va.gov)
 * Version 1.0 (03/12/2007)
 *  
 */
import gov.va.med.lom.vistabroker.patient.data.AllergiesReactants;
import gov.va.med.lom.vistabroker.patient.data.AllergyReactant;
import gov.va.med.lom.vistabroker.security.ISecurityContext;
import gov.va.med.lom.vistabroker.security.SecurityContextFactory;
import gov.va.med.lom.vistabroker.service.PatientVBService;
import gov.va.med.lom.vistabroker.util.VistaBrokerServiceFactory;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class TestAllergiesReactantsRpc {
  
  private static final Log log = LogFactory.getLog(TestAllergiesReactantsRpc.class);
  
  static void printUsage() {
    System.out.println("Usage: java TestAllergiesReactantsRpc DIVISION DUZ DFN");
  }
  
  /*
   * Prints the allergies/reactants/symptoms for the patient. 
   */
  public static void main(String[] args) {
    
    PatientVBService patientRpcs = VistaBrokerServiceFactory.getPatientVBService();
    
    String division = null;
    String duz = null;
    String dfn = null;
    if (args.length != 3) {
      printUsage();
      System.exit(1);
    } else {
      division = args[0];
      duz = args[1];
      dfn = args[2];
    }
    try {
      // Set security context
      ISecurityContext securityContext = SecurityContextFactory.createDuzSecurityContext(division, duz);
  
      // Create an allergies/reactants rpc object and invoke the rpc
      AllergiesReactants allergiesReactants = patientRpcs.getAllergiesReactants(securityContext, dfn).getPayload();
      List<AllergyReactant> allergiesReactantsList = allergiesReactants.getAllergiesReactants();
      // Print the list of allergies/reactants
      if ((allergiesReactantsList != null) && (allergiesReactantsList.size() > 0)) {
        for(AllergyReactant allergy : allergiesReactantsList) {
          System.out.println("Allergen/Reactant: " + allergy.getAllergenReactant());
          String[] reactionsSymptoms = allergy.getReactionsSymptoms();
          for(int j = 0; j < reactionsSymptoms.length; j++)
            System.out.println("Symptom " + (j+1) + ": " + reactionsSymptoms[j]);
          System.out.println("Severity: " + allergy.getSeverity() + "\n");
          String detail = patientRpcs.getAllergyDetail(securityContext, dfn, allergy.getIen()).getPayload();
          System.out.println("Detail");
          System.out.println(detail);
        }
      } else {
        if (allergiesReactants.getNoKnownAllergies())
          System.out.println("No Known Allergies");
        if (allergiesReactants.getNoAllergyAssessment())
          System.out.println("No Allergy Assessment");
      }
    } catch(Exception e) {
      System.err.println(e.getMessage());
      log.error("Error occurred while calling RPC: ", e);
    }     
  }

}