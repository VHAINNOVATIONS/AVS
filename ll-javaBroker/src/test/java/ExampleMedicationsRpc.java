/*
 * ExampleMedicationsRpc.java
 * 
 * Author: Rob Durkin (rob.durkin@med.va.gov)
 * Version 1.3 (07/05/2005)
 *  
 * Prints the list of medications for the patient.
 *
 * Usage: java ExampleMedicationsRpc SSN AUTH_PROPS
 * where SSN is a patient's Social Security Number.
 *       AUTH_PROPS is the name of a properties file containing VistA sign-on info..
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

public class ExampleMedicationsRpc {
  
  /*
   * Prints the medications for the first patient matching the specified ssn. 
   */
  public static void main(String[] args) {
    // If user didn't pass the patient's ssn and name of the auth properties file, then print usage and exit
    //String dfn = null;
    String server = null;
    int port = 0;
    String access = null;
    String verify = null;
    if (args.length != 2) {
      System.out.println("Usage: java ExampleMedicationsRpc DFN AUTH_PROPS");
      System.out.println("where DFN is a patient's DFN.");
      System.out.println("      AUTH_PROPS is the name of a properties file containing VistA sign-on info.");
      System.exit(1);
    } else {
      //dfn = args[0];
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
        
        /*
        rpcBroker.createContext("OR CPRS GUI CHART");
        Object[] params = {"10224045","38311744"};
        String s = rpcBroker.call("ORQQPS DETAIL", params);
        System.out.println(s);
        */
        
        // Get the patient's dfn from the ssn
        //long dfn = ExamplePatientSelectionRpc.getPatientDfnBySsn(rpcBroker, ssn);
        String dfn = "10310988";//10093849;
        if (dfn != null) {
          // Create a medications rpc object and invoke the rpc
          MedicationsRpc medicationsRpc = new MedicationsRpc(rpcBroker);
          medicationsRpc.setReturnRpcResult(false);
          MedicationsList medicationsList = medicationsRpc.getCoverSheetMeds(dfn); //PharmID^DrugName^OrderID^StatusName
          Medication[] medications = medicationsList.getMedications(); 
          // Print the list of medications
          for(int i = 0; i < medications.length; i++) {
            System.out.println("Pharm ID: " + medications[i].getPharmId());
            System.out.println("Name: " + medications[i].getName());
            System.out.println("Order ID: " + medications[i].getOrderId());
            System.out.println("Status: " + medications[i].getStatus());
            System.out.println("------------------------------------");
          }
          
          /*
          MedicationsList medicationsList = medicationsRpc.getMedications(dfn);  //.getCoverSheetMeds(dfn);
          Medication[] medications = medicationsList.getMedications(); 
          // Print the list of medications
          for(int i = 0; i < medications.length; i++) {
            //if (medications[i].getStatus().equals("Active")) {
              System.out.println("Name: " + medications[i].getName());
              System.out.println("Type: " + medications[i].getType());
              System.out.println("Status: " + medications[i].getStatus());
              System.out.println("Refills: " + medications[i].getRefills());
              System.out.println("Sig: " + medications[i].getSig());
              System.out.println("Order ID: " + medications[i].getOrderId());
              System.out.println("Pharm ID: " + medications[i].getPharmId());
              System.out.println("getDateExpiresStr: " + medications[i].getDateExpiresStr());
              System.out.println("getDateLastFilledStr: " + medications[i].getDateLastFilledStr());
              //String details = medicationsRpc.getMedDetails(dfn, medications[i].getPharmId());
              //System.out.println(details);
              System.out.println("------------------------------------");
            //}
          }
          */
          //System.out.println("RPC Result: " + medicationsList.getRpcResult());
          //System.out.println("XML: " + medicationsList.getXml());
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