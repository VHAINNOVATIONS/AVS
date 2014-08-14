package gov.va.med.lom.avs.client.action;

import java.util.List;
import java.util.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;
import java.util.HashMap;

import gov.va.med.lom.login.struts.session.SessionUtil;
import gov.va.med.lom.javaUtils.misc.DateUtils;
import gov.va.med.lom.javaUtils.misc.StringUtils;

import gov.va.med.lom.vistabroker.patient.data.Patient;
import gov.va.med.lom.vistabroker.patient.data.PatientInfo;
import gov.va.med.lom.vistabroker.patient.data.EncounterAppointment;
import gov.va.med.lom.vistabroker.util.FMDateUtils;
import gov.va.med.lom.vistabroker.service.PatientVBService;
import gov.va.med.lom.vistabroker.util.VistaBrokerServiceFactory;

import gov.va.med.lom.avs.client.model.*;
import gov.va.med.lom.avs.service.ServiceFactory;
import gov.va.med.lom.avs.service.SettingsService;
import gov.va.med.lom.avs.model.FacilityPrefs;
import gov.va.med.lom.foundation.service.response.ServiceResponse;

public class PatientAction extends BaseCardAction {

  private PatientVBService patientVBService;
  private SettingsService settingsService;
  private String facilityNo;
  private String userDuz;
  private String patientDfn;
  private String query;
  
  /*
   * Action Methods
   */  
  public void prepare() throws Exception {
    
    super.prepare();    
    if (!SessionUtil.isActiveSession(super.request))
      return;
    try {
      patientVBService = VistaBrokerServiceFactory.getPatientVBService();
      settingsService = ServiceFactory.getSettingsService();
    } catch(Exception e) {
      log.error("error creating service", e);
    }
    
  }  
  
	public String patientList() {
	  List<PatientJson> patientsJson = new ArrayList<PatientJson>();
	  List<Patient> patients = (List<Patient>)patientVBService.getSubSetOfPatients(super.securityContext, query, 1).getCollection();
    for(Patient patient : patients) {
      PatientJson patientJson = new PatientJson();   
      patientJson.setDfn(patient.getDfn());
      patientJson.setName(patient.getName());
      patientJson.setSsn(patient.getSsn());
      patientsJson.add(patientJson);
    }
	  return writeJson(patientsJson);
	}
	
	public String patientInfo() {
	  PatientInfo patientInfo = patientVBService.getPatientInfo(super.securityContext, patientDfn).getPayload();
	  PatientInfoJson patJson = new PatientInfoJson();
	  patJson.setAge(SheetAction.IS_DEMO ? patientInfo.getAge() : SheetAction.DEMO_PT_AGE);
	  patJson.setDfn(patientInfo.getDfn());
	  patJson.setName(SheetAction.IS_DEMO ? patientInfo.getName() : SheetAction.DEMO_PT_NAME);
	  patJson.setSex(patientInfo.getSex());
	  patJson.setSsn(SheetAction.IS_DEMO ? patientInfo.getSsn() : SheetAction.DEMO_PT_SSN);
	  patJson.setDob(SheetAction.IS_DEMO ? patientInfo.getDobStr() : SheetAction.DEMO_PT_DOB);
	  patJson.setDeceasedDate(patientInfo.getDeceasedDateStr());
	  patJson.setVeteran(patientInfo.getVeteran());
	  patJson.setScPct(patientInfo.getScPct());
	  patJson.setLocation(patientInfo.getLocation());
	  patJson.setRoomBed(patientInfo.getRoomBed());
	  patJson.setInpatient(patientInfo.getInpatient());
	  return writeJson(patJson);
	}
	
	public String patientEncounters() {
	  // load encounters from 60 days ago until the present date/time + three hours
	  ServiceResponse<FacilityPrefs> response = this.settingsService.getFacilityPrefs(this.facilityNo);
	      FacilityPrefs facilityPrefs = response.getPayload();
	  TimeZone tz = TimeZone.getTimeZone(facilityPrefs.getTimeZone());
    Calendar toDate = Calendar.getInstance(tz);
    toDate.add(Calendar.HOUR, 3);
    long toDateMsec = toDate.getTime().getTime();
	  Calendar fromDate = Calendar.getInstance(tz);
	  fromDate.add(Calendar.DATE, -(facilityPrefs.getEncountersRange()));
	  List<EncounterAppointment> appts = 
	      (List<EncounterAppointment>)patientVBService.getOutpatientEncounters(super.securityContext, patientDfn, fromDate, toDate).getCollection();
	  // find encounter closest to current date/time
	  int apptIndex = 0;
	  long low = Long.MAX_VALUE;
	  for (int i = appts.size()-1; i >= 0; i--) {
	    EncounterAppointment appt = appts.get(i);
	    long diff = toDateMsec - appt.getDatetime().getTime();
	    if ((diff >= 0) && (diff < low)) {
	      low = diff;
	      apptIndex = i;
	    }
	  }
	  
	  Calendar todayCal = Calendar.getInstance();
	  todayCal.setTime(new Date());
	  Calendar yesterdayCal = Calendar.getInstance();
	  yesterdayCal.setTime(DateUtils.addDaysToDate(todayCal.getTime(), -1));
	  
	  List<String> keys = new ArrayList<String>();
	  HashMap<String, List<PatientEncounterJson>> encMap = 
	      new HashMap<String, List<PatientEncounterJson>>();
	  for (int i = appts.size()-1; i >= 0; i--) {
	    EncounterAppointment appt = appts.get(i);
	    Calendar apptCal = Calendar.getInstance();
	    apptCal.setTime(appt.getDatetime());
	    String key = null;
	    if ((todayCal.get(Calendar.DAY_OF_YEAR) == apptCal.get(Calendar.DAY_OF_YEAR)) && 
	        (todayCal.get(Calendar.YEAR) == apptCal.get(Calendar.YEAR))) {
	      key = "Today";
	    } else if ((yesterdayCal.get(Calendar.DAY_OF_YEAR) == apptCal.get(Calendar.DAY_OF_YEAR)) && 
          (yesterdayCal.get(Calendar.YEAR) == apptCal.get(Calendar.YEAR))) {
        key = "Yesterday";
      } else {
        try {
          key = DateUtils.formatDate(appt.getDatetime(), "MMM dd, yyyy");
        } catch(Exception e) {}
      }
      if (!encMap.containsKey(key)) {
        encMap.put(key, new ArrayList<PatientEncounterJson>());
        keys.add(key + "^" + (i == apptIndex));
      }
	    PatientEncounterJson encJson = new PatientEncounterJson();
      encJson.setId(i+1);
      encJson.setDatetime(appt.getDatetimeStr());
      encJson.setFmDatetime(String.valueOf(FMDateUtils.dateTimeToFMDateTime(appt.getDatetime())));
      encJson.setLocationIen(appt.getLocationIen());
      encJson.setLocationName(appt.getLocationName());
      encJson.setStatus(appt.getStatus());
      encJson.setText(String.format("<span id=\"encounter\"><span class=\"location\">%s</span>" +
          "<span class=\"datetime\">Time: %s</span>" +
          "<span class=\"status\">Status: %s</span></span>", 
          appt.getLocationName(), StringUtils.piece(appt.getDatetimeStr(), '@', 2), appt.getStatus()));
      encJson.setIconCls("");
      if (i == apptIndex) {
        encJson.setSelected(true);
        encJson.setChecked(true);
      } else {
        encJson.setSelected(false);
        encJson.setChecked(false);
      }
      
      encJson.setExpanded(false);
      encJson.setLeaf(true);
      List<PatientEncounterJson> encList = encMap.get(key);
      encList.add(encJson);
	  }
	  
	  int i = 100;
	  List<PatientEncounterJson> encountersJson = new ArrayList<PatientEncounterJson>();
	  for (String key : keys) {
	    String text = StringUtils.piece(key, 1);
	    PatientEncounterJson encJson = new PatientEncounterJson();
      encJson.setId(i++);
      encJson.setText(String.format("<span id=\"encounter\"><span class=\"date\">%s</span></span>", text));
      encJson.setExpanded(false);
      encJson.setSelected(null);
      encJson.setChecked(null);
      encJson.setLeaf(false);
      encJson.setChildren(encMap.get(text));
      encountersJson.add(encJson);
    }
    
	  return writeJson(encountersJson);
	}
	
  public String getUserDuz() {
    return userDuz;
  }

  public void setUserDuz(String userDuz) {
    this.userDuz = userDuz;
  }

  public String getPatientDfn() {
    return patientDfn;
  }

  public void setPatientDfn(String patientDfn) {
    this.patientDfn = patientDfn;
  }

  public String getQuery() {
    return query;
  }

  public void setQuery(String query) {
    this.query = query;
  }

  public String getFacilityNo() {
    return facilityNo;
  }

  public void setFacilityNo(String facilityNo) {
    this.facilityNo = facilityNo;
  }
  
}
