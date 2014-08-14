/*
 * ExampleTiuNotesRpc.java
 * 
 * Author: Rob Durkin (rob.durkin@med.va.gov)
 * Version 1.2 (06/22/2005)
 *  
 * Prints the list of TIU notes for the patient.
 * Limits selection to the first 3 signed progress notes.
 *
 * Usage: java ExampleTiuNotesRpc SSN AUTH_PROPS
 * where SSN is a patient's Social Security Number.
 *       AUTH_PROPS is the name of a properties file containing VistA sign-on info.
 * 
 * Required Libraries:
 *   javaBroker.jar 
 *   
 */
import java.util.*;

import gov.va.med.lom.javaBroker.rpc.user.*;
import gov.va.med.lom.javaBroker.rpc.user.models.*;
import gov.va.med.lom.javaBroker.rpc.patient.*;
import gov.va.med.lom.javaBroker.rpc.patient.models.*;
import gov.va.med.lom.javaBroker.rpc.BrokerException;
import gov.va.med.lom.javaBroker.rpc.RpcBroker;

import gov.va.med.lom.javaBroker.util.StringUtils;

public class ExampleTiuNotesRpc {
  
  /*
   * Prints the TIU notes for the first patient matching the specified ssn. 
   * Limits selection to the first 10 signed progress notes.
   */
  public static void main(String[] args) throws Exception {
    // If user didn't pass the patient's ssn and name of the auth properties file, then print usage and exit
    String ssn = null;
    String server = null;
    int port = 0;
    String access = null;
    String verify = null;
    if (args.length != 2) {
      System.out.println("Usage: java ExampleTiuNotesRpc SSN AUTH_PROPS");
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
        //long dfn = ExamplePatientSelectionRpc.getPatientDfnBySsn(rpcBroker, ssn);
        long dfn = 99999;
        if (dfn > 0) {
          /*
          // Create a tiu note headers rpc object and invoke the rpc
          TiuNoteHeadersRpc tiuNoteHeadersRpc = new TiuNoteHeadersRpc(rpcBroker);
          // Limit selection to the first 3 signed progress notes
          TiuNoteHeadersSelection tiuNoteHeadersSelection = new TiuNoteHeadersSelection();
          tiuNoteHeadersSelection.setLimit(3);
          tiuNoteHeadersSelection.setNoteStatus(TiuNoteHeadersRpc.UNSIGNED_DOCUMENTS);
          tiuNoteHeadersSelection.setNoteClass(TiuNoteHeadersRpc.CLASS_PROGRESS_NOTES);
          TiuNoteHeadersList tiuNoteHeadersList = tiuNoteHeadersRpc.getTiuNoteHeaders(dfn, tiuNoteHeadersSelection);
          TiuNoteHeader[] tiuNoteHeaders = tiuNoteHeadersList.getTiuNoteHeaders(); 
          // Print the list of tiu note headers and associated note text
          System.out.println("---------- TIU NOTES ----------");
          for(int i = 0; i < tiuNoteHeaders.length; i++) {
          }
            */
          
            /*
            System.out.println("Title: " + tiuNoteHeaders[i].getTitle());
            try {
              System.out.println("Reference Date/Time: " + DateUtils.toEnglishDateTime(tiuNoteHeaders[i].getReferenceDatetimeStr()));
            } catch(ParseException pe) {}
            System.out.println("Author: " + tiuNoteHeaders[i].getAuthorName());
            System.out.println("Subject: " + tiuNoteHeaders[i].getSubject());
            System.out.println("Hospital Location: " + tiuNoteHeaders[i].getHospitalLocation());
            System.out.println("Signature Status: " + tiuNoteHeaders[i].getSignatureStatus());
            */
            // Create a tiu note rpc object and invoke the rpc to get the text
            TiuNoteRpc tiuNoteRpc = new TiuNoteRpc(rpcBroker);
            tiuNoteRpc.setReturnRpcResult(false);

            TiuNote tiuNote = null;
            
            /*
            List<String> noteIens = new ArrayList<String>();
            noteIens.add("22146087");
            
            for (String noteIen : noteIens) {
              //System.out.println("Retrieving note ien " + noteIen);
              tiuNote = tiuNoteRpc.getTiuNote(noteIen);
              System.out.println(tiuNote.getText());
             }
             */
            String noteIen = "29768099";
            /*
            tiuNote = tiuNoteRpc.getTiuNote(noteIen);
            System.out.println("Text: " + tiuNote.getText());
            String details = tiuNoteRpc.getTiuDetails(noteIen);
            System.out.println("Details: " + details);
            String visitStr = tiuNoteRpc.getVisitStrForNote(noteIen);
            System.out.println("Visit String: " + visitStr);
            */
            String[] pceData = tiuNoteRpc.getGetPCEDataForNote(null, "10043495", "6504;3140708.083;A");
            for (int i = 0; i < pceData.length; i++) {
              System.out.println(pceData[i]);
              //if (pceData[i].startsWith("VST^PT^")) {
              //  break;
              //}
            }
            
            /*
            for (int i = 0; i < pceData.length; i++) {
              String s = pceData[i];
              String s4 = s.substring(0, 4);
              String s7 = s.substring(0, 7);
              if (s4.equals("HDR^")) {
                String t = StringUtils.piece(s, '^', 4);
                locIen = StringUtils.piece(t, ';', 1);
                encSvcCat = StringUtils.piece(t, ';', 3);
                encDateTime = Double.valueOf(StringUtils.piece(t, ';', 2)).doubleValue();
                Calendar cal = DateUtils.fmDateTimeToDateTime(encDateTime);
                sb2.append("Encounter Date/Time: ");
                sb2.append(DateUtils.toEnglishDateTime(cal.getTime()));
                sb2.append("\n");
                visitTypesByLoc = notesManagerBean.listVisitTypeByLoc(locIen, encDateTime);
              } else if (s7.equals("VST^HL^")) {
                sb2.append("Encounter Location: ");
                sb2.append(StringUtils.piece(s, '^', 5));
                sb2.append("\n");
              } else if (s7.equals("VST^PS^")) {
                sb2.append("Inpatient: ");
                sb2.append(sccValue(s));
                sb2.append("\n");
              } else if ((s.substring(0, 3).equals("PRV") && s.charAt(3) != '-')) {
                providers.add(StringUtils.piece(s, '^', 5));
                providersCodes.add(StringUtils.piece(s, '^', 2));
              } else if ((s.substring(0, 3).equals("POV") && s.charAt(3) != '-')) {
                diagnoses.add(StringUtils.piece(s, '^', 4));     
                diagnosesCodes.add(StringUtils.piece(s, '^', 2));
              } else if ((s.substring(0, 3).equals("CPT") && s.charAt(3) != '-')) {
                procedures.add(StringUtils.piece(s, '^', 4));
                proceduresCodes.add(StringUtils.piece(s, '^', 2));
                boolean isVisit = false;
                for (String visitType : visitTypesByLoc) {
                  String pcs = StringUtils.piece(s, "^", 2, 4);
                  isVisit = pcs.equals(visitType);
                  if (isVisit && (visitTypeCode == null))
                    visitTypeCode = StringUtils.piece(s, '^', 2);
                }
              } else if ((s.substring(0, 3).equals("IMM") && s.charAt(3) != '-')) {
                immunizations.add(StringUtils.piece(s, '^', 4));
                immunizationsCodes.add(StringUtils.piece(s, '^', 2) + ";AUTTIMM(");
              } else if ((s.substring(0, 2).equals("SK") && s.charAt(3) != '-')) {
                skinTests.add(StringUtils.piece(s, '^', 4));
                skinTestsCodes.add(StringUtils.piece(s, '^', 2) + ";AUTTSK(");
              } else if ((s.substring(0, 3).equals("PED") && s.charAt(3) != '-')) {
                education.add(StringUtils.piece(s, '^', 4));
                educationCodes.add(StringUtils.piece(s, '^', 2) + ";AUTTEDT(");
              } else if ((s.substring(0, 2).equals("HF") && s.charAt(3) != '-')) {
                healthFactors.add(StringUtils.piece(s, '^', 4));
                healthFactorsCodes.add(StringUtils.piece(s, '^', 2) + ";AUTTHF(");
              } else if ((s.substring(0, 3).equals("XAM") && s.charAt(3) != '-')) {
                exams.add(StringUtils.piece(s, '^', 4));
                examsCodes.add(StringUtils.piece(s, '^', 2) + ";AUTTEXAM(");
              }
            }
            */
            
            /*
            tiuNote = tiuNoteRpc.getTiuNote(noteIen);
            String text = StringUtils.replaceChar(tiuNote.getText(), '\r', '+');
            text = StringUtils.deleteChar(text, '\n');
            System.out.println(text);
            */
            
          }
        //} else {
        //  System.out.println("No matching patient for the specified ssn.");
        //}
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