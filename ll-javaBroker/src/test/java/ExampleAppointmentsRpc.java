/*
 * ExampleAppointmentsRpc.java
 * 
 * Author: Rob Durkin (rob.durkin@med.va.gov)
 * Version 1.3 (07/05/2005)
 *  
 * Prints the list of appointments for the patient.
 *
 * Usage: java ExampleAppointmentsRpc SSN AUTH_PROPS
 * where SSN is a patient's Social Security Number.
 *       AUTH_PROPS is the name of a properties file containing VistA sign-on info.
 * 
 * Required Libraries:
 *   javaBroker.jar 
 *   
 */

import java.text.ParseException;
import java.util.ResourceBundle;
import java.util.Date;

import gov.va.med.lom.javaBroker.rpc.user.*;
import gov.va.med.lom.javaBroker.rpc.user.models.*;
import gov.va.med.lom.javaBroker.rpc.patient.*;
import gov.va.med.lom.javaBroker.rpc.patient.models.*;
import gov.va.med.lom.javaBroker.rpc.BrokerException;
import gov.va.med.lom.javaBroker.rpc.RpcBroker;
import gov.va.med.lom.javaBroker.util.DateUtils;

public class ExampleAppointmentsRpc {
  
  /*
   * Prints the appointments over the last 6 months and through the
   * next 30 days for the first patient matching the specified ssn. 
   */
  public static void main(String[] args) {
    // If user didn't pass the patient's ssn and name of the auth properties file, then print usage and exit
    String ssn = null;
    String server = null;
    int port = 0;
    String access = null;
    String verify = null;
    if (args.length != 2) {
      System.out.println("Usage: java ExampleAppointmentsRpc SSN AUTH_PROPS");
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
          // Create an appointments rpc object and invoke the rpc
          AppointmentsRpc appointmentsRpc = new AppointmentsRpc(rpcBroker);
          appointmentsRpc.setReturnRpcResult(true);
          Date fromDate = DateUtils.subtractDaysFromDate(new Date(), 0);
          Date throughDate = DateUtils.addDaysToDate(new Date(), 90);
          AppointmentsList appointmentsList = appointmentsRpc.getAppointments(dfn, fromDate, throughDate); 
          Appointment[] appointments = appointmentsList.getAppointments();
          try {
            System.out.println("From: " + DateUtils.toEnglishDate(appointmentsList.getFromDate())); 
            System.out.println("Through: " + DateUtils.toEnglishDate(appointmentsList.getThroughDate()) + "\n");
          } catch(ParseException pe) {}
          // Print the list of appointments
          for(int i = 0; i < appointments.length; i++) {
            System.out.println("ID: " + appointments[i].getId());
            System.out.println("Location: " + appointments[i].getLocation());
            System.out.println("Header: " + appointments[i].getHeader());
            System.out.println("Date: " + appointments[i].getDatetimeStr());
            System.out.println("Inverse Date: " + appointments[i].getInverseDate() + "\n");
          }
          System.out.println("RPC Result: " + appointmentsList.getRpcResult());
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