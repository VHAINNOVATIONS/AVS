/*
 * ExampleUnsignedTiuNotesForServiceRpc.java
 * 
 * Author: Rob Durkin (rob.durkin@med.va.gov)
 * Version 1.0 (12/07/2006)
 *  
 * Retrieves a report of unsigned notes for all notes for a service.
 *
 * Usage: java ExampleUnsignedTiuNotesForServiceRpc AUTH_PROPS SERVICE_ABBR
 * where AUTH_PROPS is the name of a properties file containing VistA sign-on info.
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
import gov.va.med.lom.javaBroker.util.Console;
import gov.va.med.lom.javaBroker.util.StringUtils;

public class ExampleUnsignedTiuNotesForServiceRpc {
  
  /*
   * Retrieves a report of unsigned notes for all notes for a service.
   */
  public static void main(String[] args) {
    // If user didn't pass the auth properties file and service abbreviation, then print usage and exit
    String service = null;
    String handle = null;
    boolean delete = false;
    String server = null;
    int port = 0;
    String access = null;
    String verify = null;
    if (args.length != 2) {
      System.out.println("Usage: java ExampleUnsignedTiuNotesForServiceRpc AUTH_PROPS (-title=SERVICE_ABBR | -handle=HANDLE | -delete=HANDLE)");
      System.out.println("where AUTH_PROPS is the name of a properties file containing VistA sign-on info.");
      System.out.println("      SERVICE_ABBR is the note title abbreviation for the service (e.g. MED).");
      System.out.println("      HANDLE is handle returned from the queue RPC.");
      System.exit(1);
    } else {
      ResourceBundle res = ResourceBundle.getBundle(args[0]);
      if (args[1].startsWith("-title"))
        service = StringUtils.piece(args[1], '=', 2);
      else if (args[1].startsWith("-handle"))
        handle = StringUtils.piece(args[1], '=', 2);
      else if (args[1].startsWith("-delete")) {
        handle = StringUtils.piece(args[1], '=', 2);
        delete = true;
      }
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
        TiuNoteHeadersRpc tiuNoteHeadersRpc = 
          new TiuNoteHeadersRpc(rpcBroker);
        if (delete) {
          boolean deleted = tiuNoteHeadersRpc.deleteUnsignedByServiceReport(handle);
          if (deleted)
            System.out.println(handle + " report deleted.");
          else
            System.out.println(handle + " report not deleted.");
        } else {
          // Get queue handle for the note title abbreviation
          if (service != null) {
            QueueHandle queueHandle = tiuNoteHeadersRpc.getUnsignedByServiceQueueHandle(service);
            handle = queueHandle.getHandle();
            System.out.println("Task No. = " + queueHandle.getTaskNo());
          }
          System.out.println("Queue handle = " + handle);
          // Call the "report" rpc until the report is returned
          while (true) {
            String x = tiuNoteHeadersRpc.getUnsignedByServiceReport(handle, false);
            if (x != null) {
              if (StringUtils.piece(x, 1).equals("-1")) {
                // Check if report file not yet found or if report compilation in progress,
                // Otherwise, print results and break.
                if (!StringUtils.piece(x, 2).equals("Report file not found") && 
                    !StringUtils.piece(x, 2).equals("Report compilation in progress")) {
                  System.out.println(StringUtils.piece(x, 2));
                  break;
                } else {
                  try {
                    Thread.sleep(10000);
                  } catch(InterruptedException ie) {}
                }
              } else {
                TiuNoteHeadersList tiuNoteHeadersList = tiuNoteHeadersRpc.getTiuNoteHeadersList();
                if ((tiuNoteHeadersList != null) && (tiuNoteHeadersList.getTiuNoteHeaders() != null)) {
                  TiuNoteHeader[] tiuNoteHeaders = tiuNoteHeadersList.getTiuNoteHeaders();
                  System.out.println("Author                          Patient                         Title                                     Date/Time         Status             Expected Signer");
                  System.out.println("------------------------------  ------------------------------  ----------------------------------------  ----------------  -----------------  ---------------");
                  for (int i = 0; i < tiuNoteHeaders.length; i++) {
                    if (tiuNoteHeaders[i].getSignatureStatus().equals("unsigned") ||
                        tiuNoteHeaders[i].getSignatureStatus().equals("uncosigned")) {
                      Console.paddedPrint(tiuNoteHeaders[i].getAuthorName(), 32);
                      Console.paddedPrint(tiuNoteHeaders[i].getPatientName(), 32);
                      Console.paddedPrint(tiuNoteHeaders[i].getTitle(), 42);
                      Console.paddedPrint(tiuNoteHeaders[i].getReferenceDatetimeStr(), 18);
                      Console.paddedPrint(tiuNoteHeaders[i].getSignatureStatus(), 19);
                      Console.println(String.valueOf(tiuNoteHeaders[i].getExpectedSignerDuz()));
                    }
                  }
                }
                break;
              }
            } else
              break;
          }
          /*
          boolean deleted = tiuNoteHeadersRpc.deleteUnsignedByServiceReport(handle);
          if (deleted)
            System.out.println(handle + " report deleted.");
          else
            System.out.println(handle + " report not deleted.");
          */
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