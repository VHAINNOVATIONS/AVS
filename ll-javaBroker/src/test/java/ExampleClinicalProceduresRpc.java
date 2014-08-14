/*
 * ExampleClinicalProceduresRpc.java
 * 
 * Author: Rob Durkin (rob.durkin@med.va.gov)
 * Version 1.0 (02/27/2006)
 *  
 * Prints information about a clinical procedure, including the IEN's of 
 * the order, consult, and TIU note.  Also prints basic patient information
 * and the details of the order and consult and the text of the TIU note.
 *
 * Usage: java ExampleClinicalProceduresRpc AUTH_PROPS
 * where AUTH_PROPS is the name of a properties file containing VistA sign-on info.
 * 
 * Required Libraries:
 *   javaBroker.jar 
 *   
 */
import java.util.ResourceBundle;
 
import gov.va.med.lom.javaBroker.rpc.user.*;
import gov.va.med.lom.javaBroker.rpc.user.models.*;
import gov.va.med.lom.javaBroker.rpc.patient.ClinicalProceduresRpc;
import gov.va.med.lom.javaBroker.rpc.patient.PatientInfoRpc;
import gov.va.med.lom.javaBroker.rpc.patient.TiuNoteRpc;
import gov.va.med.lom.javaBroker.rpc.patient.OrdersRpc;
import gov.va.med.lom.javaBroker.rpc.patient.ConsultsRpc;
import gov.va.med.lom.javaBroker.rpc.patient.models.PatientInfo;
import gov.va.med.lom.javaBroker.rpc.patient.models.ClinicalProcedure;
import gov.va.med.lom.javaBroker.rpc.patient.models.TiuNote;
import gov.va.med.lom.javaBroker.rpc.BrokerException;
import gov.va.med.lom.javaBroker.rpc.RpcBroker;
import gov.va.med.lom.javaBroker.util.Console;

public class ExampleClinicalProceduresRpc {
  
  /*
   * Prints information about a clinical procedure. 
   */
  public static void main(String[] args) {
    // If user didn't pass the name of the auth properties file, then print usage and exit
    String server = null;
    int port = 0;
    String access = null;
    String verify = null;
    if (args.length != 1) {
      System.out.println("Usage: java ExampleListsRpc AUTH_PROPS");
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
        // Create a codes rpc object
        ClinicalProceduresRpc clinicalProceduresRpc = new ClinicalProceduresRpc(rpcBroker);
        while (true) {
          String orderNumber = Console.readLine("Enter Instrument Order Number: ");
          if (!orderNumber.equals("")) {
            ClinicalProcedure clinicalProcedure = clinicalProceduresRpc.getClinicalProcedure(orderNumber);
            if (clinicalProcedure.getDfn() != null) {
            
              PatientInfoRpc patientInfoRpc = new PatientInfoRpc(rpcBroker);
              PatientInfo patientInfo = patientInfoRpc.getPatientInfo(clinicalProcedure.getDfn());
              Console.println("\n----------- Patient Info -----------");
              Console.println("Patient Name: " +  patientInfo.getName());
              Console.println("Patient SSN: " +  patientInfo.getSsn());
              Console.println("Patient Sex: " +  patientInfo.getSex());
              Console.println("Patient DoB: " +  patientInfo.getDobStr());
    
              Console.println("\n----------- Clinical Procedure IEN's -----------");
              Console.println("Order Entry #: " + clinicalProcedure.getOrderNumber());
              Console.println("Order IEN: " + clinicalProcedure.getOrderIen());
              Console.println("Consult IEN: " + clinicalProcedure.getConsultIen());
              Console.println("TIU Note IEN: " + clinicalProcedure.getTiuNoteIen());
    
              OrdersRpc ordersRpc = new OrdersRpc(rpcBroker); 
              String orderDetails = ordersRpc.getOrderDetails(clinicalProcedure.getOrderIen(), clinicalProcedure.getDfn());
              Console.println("\n----------- Order Details -----------");
              Console.println(orderDetails);
              
              ConsultsRpc consultsRpc = new ConsultsRpc(rpcBroker);
              String consultDetails = consultsRpc.getConsultDetail(clinicalProcedure.getConsultIen());
              Console.println("\n----------- Consult Details -----------");
              Console.println(consultDetails);
              
              TiuNoteRpc tiuNoteRpc = new TiuNoteRpc(rpcBroker);
              TiuNote tiuNote = tiuNoteRpc.getTiuNote(clinicalProcedure.getTiuNoteIen());
              Console.println("\n----------- Text of TIU Note -----------");
              Console.println(tiuNote.getText());
            } else {
              Console.println("Invalid Order Entry #");
            }
          } else
            break;
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