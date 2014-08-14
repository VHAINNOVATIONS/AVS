/*
 * ExampleAllergiesReactantsRpc.java
 * 
 * Author: Rob Durkin (rob.durkin@med.va.gov)
 * Version 1.3 (07/05/2005)
 *  
 * Prints the list of allergies/reactants and the symptoms of each for the patient.
 *
 * Usage: java ExampleAllergiesReactantsRpc SSN AUTH_PROPS
 * where SSN is a patient's Social Security Number.
 *       AUTH_PROPS is the name of a properties file containing VistA sign-on info.
 * 
 * Required Libraries:
 *   javaBroker.jar 
 *   
 */
import java.util.ResourceBundle;

import gov.va.med.lom.javaBroker.rpc.user.*;
import gov.va.med.lom.javaBroker.rpc.user.models.*;
import gov.va.med.lom.javaBroker.rpc.patient.*;
import gov.va.med.lom.javaBroker.rpc.patient.models.*;
import gov.va.med.lom.javaBroker.rpc.BrokerException;
import gov.va.med.lom.javaBroker.rpc.RpcBroker;

public class ExampleAllergiesReactantsRpc {
  
  /*
   * Prints the allergies/reactants/symptoms for the first patient matching the specified ssn. 
   */
  public static void main(String[] args) {
    // If user didn't pass the patient's ssn and name of the auth properties file, then print usage and exit
    String ssn = null;
    String server = null;
    int port = 0;
    String access = null;
    String verify = null;
    if (args.length != 2) {
      System.out.println("Usage: java ExampleAllergiesReactantsRpc SSN AUTH_PROPS");
      System.out.println("where SSN is a patient's Social Security Number.");
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
        // Get the patient's dfn from the ssn
        String dfn = ExamplePatientSelectionRpc.getPatientDfnBySsn(rpcBroker, ssn);
        if (dfn != null) {
          // Create an allergies/reactants rpc object and invoke the rpc
          AllergiesReactionsRpc allergiesReactionRpc = new AllergiesReactionsRpc(rpcBroker);
          allergiesReactionRpc.setReturnRpcResult(true);
          AllergiesReactions allergiesReactions = allergiesReactionRpc.getAllergiesReactions(dfn);
          AllergyReaction[] allergiesReactantsArray = allergiesReactions.getAllergiesReactions();
          // Print the list of allergies/reactants
          if (allergiesReactantsArray.length > 0) {
            for(int i = 0; i < allergiesReactantsArray.length; i++) {
              System.out.println("Allergen/Reactant: " + allergiesReactantsArray[i].getAllergy());
              String[] reactionsSymptoms = allergiesReactantsArray[i].getReactionsSymptoms();
              for(int j = 0; j < reactionsSymptoms.length; j++)
                System.out.println("Symptom " + (j+1) + ": " + reactionsSymptoms[j]);
              System.out.println("Severity: " + allergiesReactantsArray[i].getSeverity() + "\n");
              String detail = allergiesReactionRpc.getAllergyDetail(dfn, allergiesReactantsArray[i].getIen());
              System.out.println("Detail");
              System.out.println(detail);
            }
          } else {
            if (allergiesReactions.getNoKnownAllergies())
              System.out.println("No Known Allergies");
            if (allergiesReactions.getNoAllergyAssessment())
              System.out.println("No Allergy Assessment");
          }
          System.out.println("RPC Result: " + allergiesReactions.getRpcResult());
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