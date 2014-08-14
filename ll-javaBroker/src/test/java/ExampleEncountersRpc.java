/*
 * ExampleEncountersRpc.java
 * 
 * Author: Rob Durkin (rob.durkin@med.va.gov)
 * Version 1.3 (07/05/2005)
 *  
 * Prints the list of appointments and encounter data for each.
 *
 * Usage: java ExampleEncountersRpc SSN PROVIDER_IEN AUTH_PROPS
 * where SSN is a patient's Social Security Number.
 *       PROVIDER_IEN is a provider's internal entry number.
 *       AUTH_PROPS is the name of a properties file containing VistA sign-on info..
 * 
 * Required Libraries:
 *   javaBroker.jar 
 *   
 */
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.ResourceBundle;
import java.text.ParseException;

import gov.va.med.lom.javaBroker.rpc.user.*;
import gov.va.med.lom.javaBroker.rpc.user.models.*;
import gov.va.med.lom.javaBroker.rpc.patient.*;
import gov.va.med.lom.javaBroker.rpc.patient.models.*;
import gov.va.med.lom.javaBroker.rpc.BrokerException;
import gov.va.med.lom.javaBroker.rpc.RpcBroker;

import gov.va.med.lom.javaBroker.util.DateUtils;

public class ExampleEncountersRpc {
  
  /*
   * Prints the list of appointments for the first patient matching the 
   * specified ssn and encounter data for each appointment
   */
  public static void main(String[] args) {
    // If user didn't pass the patient's ssn and name of the auth properties file, then print usage and exit
    String ssn = null;
    String providerDuz = null;
    String server = null;
    int port = 0;
    String access = null;
    String verify = null;
    if (args.length != 3) {
      System.out.println("Usage: java ExampleEncountersRpc SSN PROVIDER_DUZ AUTH_PROPS");
      System.out.println("where SSN is a patient's Social Security Number.");
      System.out.println("      PROVIDER_DUZ is a provider's internal entry number.");
      System.out.println("      AUTH_PROPS is the name of a properties file containing VistA sign-on info.");
      System.exit(1);
    } else {
      ssn = args[0];
      providerDuz = args[1];
      ResourceBundle res = ResourceBundle.getBundle(args[2]);
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
        String dfn = "24574";//ExamplePatientSelectionRpc.getPatientDfnBySsn(rpcBroker, ssn);
        if (dfn != null) {
          // Create a medications rpc object and invoke the rpc
          PatientEncounterRpc patientEncounterRpc = new PatientEncounterRpc(rpcBroker);
          patientEncounterRpc.setReturnRpcResult(false);
          Date date = new Date();
          GregorianCalendar startTime = new GregorianCalendar();
          startTime.setTime(date);
          //startTime.add(Calendar.DATE, -180);
          GregorianCalendar endTime = new GregorianCalendar();
          endTime.setTime(date);
          endTime.add(Calendar.DATE, 90);
          //EncounterAppointmentsList encounterAppointmentsList = patientEncounterRpc.getInpatientEncounters(dfn, startTime, endTime);
          EncounterAppointmentsList encounterAppointmentsList = patientEncounterRpc.getOutpatientEncounters(dfn, startTime, endTime);
          EncounterAppointment[] appointments = encounterAppointmentsList.getEncounterAppointments();
          for(int i = 0; i < appointments.length; i++) {
            // Print appointment info
            try {
              System.out.println("Date/Time: " + DateUtils.toEnglishDateTime(appointments[i].getDatetimeStr()));
            } catch(ParseException pe) {}
            System.out.println("Status: " + appointments[i].getStatus());
            System.out.println("Standalone: " + appointments[i].getStandalone());
            System.out.println("Title: " + appointments[i].getTitle());
            System.out.println("Date/Time: " + appointments[i].getDatetime());
            System.out.println("Location: " + appointments[i].getLocationName());
            System.out.println("Location IEN: " + appointments[i].getLocationIen());
            /*
            // Retrieve encounter for the appointment
            Encounter encounter = new Encounter();
            encounter.setDfn(dfn);
            encounter.setLocationIen(appointments[i].getLocationIen());
            encounter.setProviderDuz(providerDuz);
            encounter.setDatetimeStr(appointments[i].getDatetimeStr());
            encounter = patientEncounterRpc.getEncounterDetails(encounter, false);
            // Print the encounter data
            System.out.println("Location Name: " + encounter.getLocationName());
            System.out.println("Location Abbr: " + encounter.getLocationAbbr());
            System.out.println("Location Text: " + encounter.getLocationText());
            System.out.println("Room/Bed: " + encounter.getRoomBed());
            System.out.println("Provider: " + encounter.getProviderName());
            System.out.println("Visit Category: " + encounter.getVisitCat());
            System.out.println("Visit Str: " + encounter.getVisitStr());
            */
            System.out.println();
          }
          //System.out.println("RPC Result: " + encounterAppointmentsList.getRpcResult());
          //System.out.println("XML: " + encounterAppointmentsList.getXml());
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