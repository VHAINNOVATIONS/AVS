package gov.va.med.lom.vistabroker.test;
/*
 * TestEncountersRpc.java
 * 
 * Author: Rob Durkin (rob.durkin@med.va.gov)
 * Version 1.0 (03/12/2007)
 *  
 */
import gov.va.med.lom.vistabroker.patient.data.Encounter;
import gov.va.med.lom.vistabroker.patient.data.EncounterAppointment;
import gov.va.med.lom.vistabroker.security.ISecurityContext;
import gov.va.med.lom.vistabroker.security.SecurityContextFactory;
import gov.va.med.lom.vistabroker.service.PatientVBService;
import gov.va.med.lom.vistabroker.util.VistaBrokerServiceFactory;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class TestEncountersRpc2 {
  
  private static final Log log = LogFactory.getLog(TestEncountersRpc2.class);
  
  static void printUsage() {
    System.out.println("Usage: java TestEncountersRpc DFN AUTH_PROPS");
    System.out.println("where DFN is the DFN of the patient.");
    System.out.println("      AUTH_PROPS is the name of a properties file containing VistA connection info.");
  }
  
  /*
   * Prints the list of appointments for the patient and encounter data for each appointment.
   */
  public static void main(String[] args) {
    PatientVBService patientRpcsRemote = VistaBrokerServiceFactory.getPatientVBService();
    
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
  
      // Create a medications rpc object and invoke the rpc
      Date date = new Date();
      GregorianCalendar startTime = new GregorianCalendar();
      startTime.setTime(date);
      startTime.add(Calendar.DATE, -30*36);
      GregorianCalendar endTime = new GregorianCalendar();
      endTime.setTime(date);
      endTime.add(Calendar.DATE, 30);
      List<EncounterAppointment> encounterAppointments = (List<EncounterAppointment>)patientRpcsRemote.getOutpatientEncounters(securityContext, dfn, startTime, endTime).getCollection();
      //List<EncounterAppointment> encounters = patientRpcsRemote.getAllEncounters2(securityContext, dfn, startTime, endTime, true);
      //List<EncounterAppointment> encounterAppointments = filterEncounters(encounters, null, "I");

      //appointments.length
      System.out.println("DatetimeStr,Datetime,Dfn,LocationIen,LocationName,Status,Title,Type,Standalone,LocationAbbr,LocationText,ProviderDuz,ProviderName,RoomBed,Type,TypeId,VisitCat,VisitStr");

      //for(EncounterAppointment appointment : encountersAppointments) {
      for(EncounterAppointment encounterAppointment : encounterAppointments) {

          System.out.print("\"" + encounterAppointment.getDatetimeStr() + "\",");
          System.out.print("\"" + encounterAppointment.getDatetime() + "\",");
          System.out.print("\"" + encounterAppointment.getDfn() + "\",");
          System.out.print("\"" + encounterAppointment.getLocationIen() + "\",");
          System.out.print("\"" + encounterAppointment.getLocationName() + "\",");
          System.out.print("\"" + encounterAppointment.getStatus() + "\",");
          System.out.print("\"" + encounterAppointment.getTitle() + "\",");
          System.out.print("\"" + encounterAppointment.getType() + "\",");
          System.out.print("\"" + encounterAppointment.getStandalone() + "\",");

          // Retrieve encounter for the appointment
          Encounter encounter = new Encounter();
          encounter.setDfn(dfn);
          encounter.setLocationIen(encounterAppointment.getLocationIen());
          encounter.setDatetimeStr(encounterAppointment.getDatetimeStr());
          encounter = patientRpcsRemote.getEncounterDetails(securityContext, encounter, false).getPayload();

          
          // Print the encounter data
          System.out.print("\"" + encounter.getLocationAbbr() + "\",");
          System.out.print("\"" + encounter.getLocationText() + "\",");
          System.out.print("\"" + encounter.getProviderDuz() + "\",");
          System.out.print("\"" + encounter.getProviderName() + "\",");
          System.out.print("\"" + encounter.getRoomBed() + "\",");
          System.out.print("\"" + encounter.getType() + "\",");
          System.out.print("\"" + encounter.getTypeId() + "\",");
          System.out.print("\"" + encounter.getVisitCat() + "\",");
          System.out.println("\"" + encounter.getVisitStr() + "\"");

    	  /*
    	  System.out.println("###########");
        // Print appointment info
        System.out.println("datetimeStr: " + appointment.getDatetimeStr());
        System.out.println("dfn: " + appointment.getDfn());
        System.out.println("locationIen: " + appointment.getLocationIen());
        System.out.println("locationName: " + appointment.getLocationName());
        System.out.println("status: " + appointment.getStatus());
        System.out.println("title: " + appointment.getTitle());
        System.out.println("type: " + appointment.getType());
        System.out.println("datetime: " + appointment.getDatetime());
        System.out.println("standalone: " + appointment.getStandalone());
        System.out.println("-----------");
        // Retrieve encounter for the appointment
        Encounter encounter = new Encounter();
        encounter.setDfn(dfn);
        encounter.setLocationIen(appointment.getLocationIen());
        encounter.setDatetimeStr(appointment.getDatetimeStr());
        encounter = patientRpcsRemote.getEncounterDetails(securityContext, encounter, false);
        // Print the encounter data
        System.out.println("datetimeStr: " + encounter.getDatetimeStr());
        System.out.println("dfn: " + encounter.getDfn());
        System.out.println("locationAbbr: " + encounter.getLocationAbbr());
        System.out.println("locationIen: " + encounter.getLocationIen());
        System.out.println("locationName: " + encounter.getLocationName());
        System.out.println("locationText: " + encounter.getLocationText());
        System.out.println("providerDuz: " + encounter.getProviderDuz());
        System.out.println("providerName: " + encounter.getProviderName());
        System.out.println("roomBed: " + encounter.getRoomBed());
        System.out.println("type: " + encounter.getType());
        System.out.println("typeId: " + encounter.getTypeId());
        System.out.println("visitCat: " + encounter.getVisitCat());
        System.out.println("visitStr: " + encounter.getVisitStr());
        System.out.println();
        */
      }    
      
      
      EncounterAppointment latest = getLatestEncounter(securityContext, patientRpcsRemote, dfn);
      
      System.out.println();
      if (latest == null) {
    	  System.out.println("No encounters found!");
      } else {
    	  System.out.println("Latest encounter: " + latest.getDatetimeStr());
      }
      System.out.println();
      
    } catch(Exception e) {
      System.err.println(e.getMessage());
      log.error("Error occurred while calling RPC: ", e);
    }     
  }

  /**
   * VistaBroker's gov.va.med.lom.vistabroker.patient.dao.PatientEncounterDao.filterEncounters method is broken!  So we emulate it here.
   */
  @SuppressWarnings("unused")
  private static List<EncounterAppointment> filterEncounters(List<EncounterAppointment> encounterList, String includeType, String excludeType) {
    List<EncounterAppointment> encounters = new ArrayList<EncounterAppointment>();
    for (EncounterAppointment e : encounterList) {
      if (((includeType == null) || (e.getType().equals(includeType))) &&
          ((excludeType == null) || (!e.getType().equals(excludeType))))
        encounters.add(e);
    }
    return encounters;
  }


	@SuppressWarnings("unchecked")
    public static EncounterAppointment getLatestEncounter(ISecurityContext securityContext, PatientVBService patientRpcs, String patientDfn) {
		
		int periodsAgo = 0;
		int daysInPeriod = 90;
		
		// Create a medications rpc object and invoke the rpc
		Date date = new Date();
		GregorianCalendar startTime = new GregorianCalendar();
		startTime.setTime(date);
		startTime.add(Calendar.DATE, -(periodsAgo + 1) * daysInPeriod);
		GregorianCalendar endTime = new GregorianCalendar();
		endTime.setTime(date);
		endTime.add(Calendar.DATE, -periodsAgo * daysInPeriod);

		EncounterAppointment encounter = null;

		/**
		 * Need to select only the last outpatient encounter, but there doesn't seem to be an easy RPC for that.  So we improvise, searching over increments
		 * of daysInPeriod at a time (for performance's sake), going farther back in time until we find one or until we get tired of looking.
		 */
		while (encounter == null && periodsAgo < 8) {
						
			periodsAgo++;
			startTime.add(Calendar.DATE, -daysInPeriod);
			endTime.add(Calendar.DATE, -daysInPeriod);
			
			//List<EncounterAppointment> encounters = patientRpcsRemote.getOutpatientEncounters(securityContext, dfn, startTime, endTime);
			List<EncounterAppointment> encounters = (List<EncounterAppointment>)patientRpcs.getAllEncounters2(securityContext, patientDfn, startTime, endTime, true);
	
			// find the latest outpatient encounter (is there a VistaBroker method for this?)
			GregorianCalendar latestDate = new GregorianCalendar();
			latestDate.setTime(date);
			latestDate.add(Calendar.DATE, -365*120);
			GregorianCalendar testDate = new GregorianCalendar();
			for (EncounterAppointment thisEncounter : encounters) {
				testDate.setTime(thisEncounter.getDatetime());
				//System.out.println(thisEncounter.getDatetimeStr() + " " + thisEncounter.getType());
				if (testDate.after(latestDate) && !thisEncounter.getType().equals("I")) {
					encounter = thisEncounter;
					latestDate.setTime(thisEncounter.getDatetime());
					//System.out.println("It's the latest!");
				}
			}
		}
		
		return encounter;
	}
}