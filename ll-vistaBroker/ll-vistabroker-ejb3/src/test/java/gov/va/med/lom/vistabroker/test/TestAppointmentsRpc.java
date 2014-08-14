package gov.va.med.lom.vistabroker.test;
/*
 * TestAppointmentsRpc.java
 * 
 * Author: Rob Durkin (rob.durkin@med.va.gov)
 * Version 1.0 (03/12/2007)
 *  
 */
import gov.va.med.lom.javaUtils.misc.DateUtils;
import gov.va.med.lom.vistabroker.patient.data.Appointment;
import gov.va.med.lom.vistabroker.security.ISecurityContext;
import gov.va.med.lom.vistabroker.security.SecurityContextFactory;
import gov.va.med.lom.vistabroker.service.PatientVBService;
import gov.va.med.lom.vistabroker.util.VistaBrokerServiceFactory;

import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class TestAppointmentsRpc {
  
  private static final Log log = LogFactory.getLog(TestAppointmentsRpc.class);
  
  static void printUsage() {
    System.out.println("Usage: java TestAppointmentsRpc DIVISION DUZ DFN");
  }
  
  /*
   * Prints the appointments over the last 6 months and through the
   * next 30 days for the patient. 
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
  
      Date fromDate = DateUtils.subtractDaysFromDate(new Date(), 365*3);
      Date throughDate = DateUtils.addDaysToDate(new Date(), 30);
      List<Appointment> appointments = (List<Appointment>)patientRpcs.getAppointments(securityContext, dfn, fromDate, throughDate).getCollection(); 
      
            System.out.println("DatetimeStr,Dfn,FmDatetime,Header,Id,InverseDate,Location");
      // Print the list of appointments
      for(Appointment appt : appointments) {
    	System.out.print("\"" + appt.getDatetimeStr() + "\",");
        //System.out.print("\"" + appt.getDatetime() + "\",");
        System.out.print("\"" + appt.getDfn() + "\",");
        System.out.print("\"" + appt.getFmDatetime() + "\",");
        System.out.print("\"" + appt.getHeader() + "\",");
        System.out.print("\"" + appt.getId() + "\",");
        System.out.print("\"" + appt.getInverseDate() + "\",");
        System.out.println("\"" + appt.getLocation() + "\"");
      }
    } catch(Exception e) {
      System.err.println(e.getMessage());
      log.error("Error occurred while calling RPC: ", e);
    }       
  }

}