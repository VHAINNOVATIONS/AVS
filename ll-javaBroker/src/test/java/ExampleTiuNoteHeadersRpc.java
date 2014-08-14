/*
 * ExampleTiuNoteHeadersRpc.java
 * 
 * Author: Rob Durkin (rob.durkin@med.va.gov)
 * Version 1.3 (07/13/2006)
 *  
 * Prints the list of TIU note headers for the patient.
 *
 * Usage: java ExampleTiuNoteHeadersRpc AUTH_PROPS
 * where AUTH_PROPS is the name of a properties file containing VistA sign-on info.
 * 
 * Required Libraries:
 *   javaBroker.jar 
 *   
 */
import java.util.*;
import java.text.*;

import gov.va.med.lom.javaBroker.rpc.user.*;
import gov.va.med.lom.javaBroker.rpc.user.models.*;
import gov.va.med.lom.javaBroker.rpc.patient.*;
import gov.va.med.lom.javaBroker.rpc.patient.models.*;
import gov.va.med.lom.javaBroker.rpc.*;
import gov.va.med.lom.javaBroker.util.DateUtils;
import gov.va.med.lom.javaBroker.util.Console;

public class ExampleTiuNoteHeadersRpc {
  
  public static void printUnsignedTiuNoteHeaders(RpcBroker rpcBroker, String authorDuz) throws BrokerException {
    
    TiuNoteHeadersRpc tiuNoteHeadersRpc = new TiuNoteHeadersRpc(rpcBroker);
    tiuNoteHeadersRpc.setReturnRpcResult(false);
    
    System.out.println("----------------------------------------------------");
    UnsignedNoteHeaders unsignedNoteHeaders = tiuNoteHeadersRpc.getUnsignedNoteHeaders(authorDuz, 50, true, true);
    int totNumDocs = 0;
    TiuNoteHeader[] tiuNoteHeaders = null;
    for (int a = 0; a <= 8; a++) {
      switch(a) {
        case 0 : tiuNoteHeaders = unsignedNoteHeaders.getUnsignedProgressNotes();
                 System.out.println("UNSIGNED PROGRESS NOTES (" + tiuNoteHeaders.length + ")");
                 System.out.println("-----------------------");
                 break;
        case 1 : tiuNoteHeaders = unsignedNoteHeaders.getUncosignedProgressNotes();
                 System.out.println("UNCOSIGNED PROGRESS NOTES (" + tiuNoteHeaders.length + ")");
                 System.out.println("-------------------------");
                 break;
        case 2 : tiuNoteHeaders = unsignedNoteHeaders.getUnsignedDischargeSummaries();
                 System.out.println("UNSIGNED DISCHARGE SUMMARIES (" + tiuNoteHeaders.length + ")");
                 System.out.println("----------------------------");
                 break;
        case 3 : tiuNoteHeaders = unsignedNoteHeaders.getUncosignedDischargeSummaries();
                 System.out.println("UNCOSIGNED DISCHARGE SUMMARIES (" + tiuNoteHeaders.length + ")");
                 System.out.println("------------------------------");
                  break;
        case 4 : tiuNoteHeaders = unsignedNoteHeaders.getUnsignedSurgicalReports();
                 System.out.println("UNSIGNED SURGICAL REPORTS (" + tiuNoteHeaders.length + ")");
                 System.out.println("-------------------------");
                 break;
        case 5 : tiuNoteHeaders = unsignedNoteHeaders.getUncosignedSurgicalReports();
                 System.out.println("UNCOSIGNED SURGICAL REPORTS (" + tiuNoteHeaders.length + ")");
                 System.out.println("---------------------------");
                 break;
        case 6 : tiuNoteHeaders = unsignedNoteHeaders.getUnsignedClinicalProcedures();
                 System.out.println("UNSIGNED CLINICAL PROCEDURES (" + tiuNoteHeaders.length + ")");
                 System.out.println("----------------------------");
                 break;
        case 7 : tiuNoteHeaders = unsignedNoteHeaders.getUncosignedClinicalProcedures();
                 System.out.println("UNCOSIGNED CLINICAL PROCEDURES (" + tiuNoteHeaders.length + ")");
                 System.out.println("------------------------------");
                 break;   
        case 8 : tiuNoteHeaders = unsignedNoteHeaders.getUnsignedByExpectedAdditonalSignerNotes();
                 System.out.println("UNSIGNED BY EXPECTED ADDITIONAL SIGNER");
                 System.out.println("--------------------------------------");
                 break;                    
      }
      totNumDocs += tiuNoteHeaders.length;
      // Print the list of tiu note headers
      for(int i = 0; i < tiuNoteHeaders.length; i++) {
        System.out.println("IEN: " + tiuNoteHeaders[i].getIen());
        System.out.println("Title: " + tiuNoteHeaders[i].getTitle());
        try {
          System.out.println("Reference Date/Time: " + DateUtils.toEnglishDateTime(tiuNoteHeaders[i].getReferenceDatetimeStr()));
        } catch(ParseException pe) {}
        System.out.println("Author DUZ: " + tiuNoteHeaders[i].getAuthorDuz());
        System.out.println("Author: " + tiuNoteHeaders[i].getAuthorName());
        System.out.println("Patient DFN: " + tiuNoteHeaders[i].getDfn());
        System.out.println("Patient: " + tiuNoteHeaders[i].getPatientName());      
        System.out.println("Subject: " + tiuNoteHeaders[i].getSubject());
        System.out.println("Hospital Location: " + tiuNoteHeaders[i].getHospitalLocation());
        System.out.println("Signature Status: " + tiuNoteHeaders[i].getSignatureStatus());
        try {
          System.out.println("Visit Date/Time: " + DateUtils.toEnglishDateTime(tiuNoteHeaders[i].getVisitDatetimeStr()));
          System.out.println("Discharge Date/Time: " + DateUtils.toEnglishDateTime(tiuNoteHeaders[i].getDischargeDatetimeStr()));
        } catch(ParseException pe) {}
        System.out.println("Request IEN: " + tiuNoteHeaders[i].getRequestIen());
        System.out.println("# Associated Images: " + tiuNoteHeaders[i].getNumAssociatedImages());
        System.out.println("Has Children: " + tiuNoteHeaders[i].getHasChildren());
        System.out.println("Parent Ien: " + tiuNoteHeaders[i].getParentIen() + "\n");
      }
      System.out.println("# documents = " + tiuNoteHeaders.length);
      System.out.println("\n\n");
    }
    System.out.println("Total # documents = " + totNumDocs);
  }
  
  public static void printTiuNoteHeaders(RpcBroker rpcBroker, String dfn) throws BrokerException {
    // Create a tiu note headers rpc object and invoke the rpc
    TiuNoteHeadersRpc tiuNoteHeadersRpc = new TiuNoteHeadersRpc(rpcBroker);
    tiuNoteHeadersRpc.setReturnRpcResult(false);
    TiuNoteHeadersSelection tiuNoteHeadersSelection = new TiuNoteHeadersSelection();
    //tiuNoteHeadersSelection.setAuthorDuz(rpcBroker.getUserDuz());  
    //tiuNoteHeadersSelection.setNoteClass(TiuNoteHeadersRpc.CLASS_PROGRESS_NOTES);
    //tiuNoteHeadersSelection.setNoteStatus(TiuNoteHeadersRpc.UNSIGNED_DOCUMENTS);
    tiuNoteHeadersSelection.setNoteClass(TiuNoteHeadersRpc.CLASS_PROGRESS_NOTES);
    tiuNoteHeadersSelection.setNoteStatus(TiuNoteHeadersRpc.SIGNED_DOCUMENTS_ALL);
    tiuNoteHeadersSelection.setLimit(20);
    Date today = new Date();
    Date lastWeek = DateUtils.subtractDaysFromDate(today, 7);
    tiuNoteHeadersSelection.setBeginDate(lastWeek);
    tiuNoteHeadersSelection.setEndDate(today);
    
    /*
    GregorianCalendar cal1 = new GregorianCalendar();
    cal1.add(Calendar.YEAR, -1);
    GregorianCalendar cal2 = new GregorianCalendar();
    cal2.add(Calendar.DAY_OF_YEAR, 1);
    tiuNoteHeadersSelection.setBeginDate(null); //cal1.getTime()
    tiuNoteHeadersSelection.setEndDate(null); //cal2.getTime()
    */
    
    
    TiuNoteHeadersList tiuNoteHeadersList = tiuNoteHeadersRpc.getTiuNoteHeaders(dfn, tiuNoteHeadersSelection);
    TiuNoteHeader[] tiuNoteHeaders = tiuNoteHeadersList.getTiuNoteHeaders();
    // Print the list of tiu note headers
    System.out.println("---------- TIU NOTE HEADERS ----------");
    for(int i = 0; i < tiuNoteHeaders.length; i++) {
      System.out.println("IEN: " + tiuNoteHeaders[i].getIen());
      System.out.println("Title: " + tiuNoteHeaders[i].getTitle());
      try {
        System.out.println("Reference Date/Time: " + DateUtils.toEnglishDateTime(tiuNoteHeaders[i].getReferenceDatetimeStr()));
      } catch(ParseException pe) {}
      System.out.println("Author DUZ: " + tiuNoteHeaders[i].getAuthorDuz());
      System.out.println("Author: " + tiuNoteHeaders[i].getAuthorName());
      System.out.println("Patient DFN: " + tiuNoteHeaders[i].getDfn());
      System.out.println("Patient: " + tiuNoteHeaders[i].getPatientName());      
      System.out.println("Subject: " + tiuNoteHeaders[i].getSubject());
      System.out.println("Hospital Location: " + tiuNoteHeaders[i].getHospitalLocation());
      System.out.println("Signature Status: " + tiuNoteHeaders[i].getSignatureStatus());
      try {
        System.out.println("Visit Date/Time: " + DateUtils.toEnglishDateTime(tiuNoteHeaders[i].getVisitDatetimeStr()));
        System.out.println("Discharge Date/Time: " + DateUtils.toEnglishDateTime(tiuNoteHeaders[i].getDischargeDatetimeStr()));
      } catch(ParseException pe) {}
      System.out.println("Request IEN: " + tiuNoteHeaders[i].getRequestIen());
      System.out.println("# Associated Images: " + tiuNoteHeaders[i].getNumAssociatedImages());
      System.out.println("Has Children: " + tiuNoteHeaders[i].getHasChildren());
      System.out.println("Parent Ien: " + tiuNoteHeaders[i].getParentIen() + "\n");
    }
    System.out.println("RPC Result: " + tiuNoteHeadersList.getRpcResult());
  }
  
  /*
   * Prints TIU note headers. 
   */
  public static void main(String[] args){
    // If user didn't pass the name of the auth properties file, then print usage and exit
    String server = null;
    int port = 0;
    String access = null;
    String verify = null;
    if (args.length != 1) {
      System.out.println("Usage: java ExampleTiuNoteHeadersRpc AUTH_PROPS");
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
        boolean cont = true;
        while (cont) {
          Console.println("1) Show my TIU note headers for a specific patient.");
          Console.println("2) Show my unsigned progress notes.");
          Console.println("3) Show all my unsigned/uncosigned documents.");
          Console.println("4) Show all unsigned/uncosigned documents for a user.");
          Console.println("5) Exit");
          int option = Console.readInt("\nOption: ");
          String dfn = null;
          String duz = null;
          switch(option) {
            case 1 : // Print TIU notes for patient
                     String ssn = Console.readLine("Enter patient SSN: ");
                     dfn = ExamplePatientSelectionRpc.getPatientDfnBySsn(rpcBroker, ssn);
                     if (dfn != null)
                       printTiuNoteHeaders(rpcBroker, dfn);
                     else
                       Console.println("No matching patient for the specified ssn.");
                     break;
            case 2 : // Print all unsigned TIU notes for user
                     printTiuNoteHeaders(rpcBroker, null); // 10124288  10108340
                     break;
            case 3 : // Print TIU notes for patient
                     printUnsignedTiuNoteHeaders(rpcBroker, rpcBroker.getUserDuz()); 
              break;  
            case 4 : // Print TIU notes for patient
              duz = Console.readLine("Enter user DUZ: ");
                     printUnsignedTiuNoteHeaders(rpcBroker, duz);  
              break; 
            case 5 : cont = false; break;          
          }                     
        }
      } else {
        System.out.println(vistaSignonResult.getMessage());
      }    
      vistaSignonRpc.disconnect();
    } catch(BrokerException be) {
      System.err.println(be.getMessage());
      System.err.println("Action: " + be.getAction());
      System.err.println("Code: " + be.getCode());
      System.err.println("Mnemonic: " + be.getMnemonic());
    } finally {
      // Close the connection to the broker
    }
  }

}