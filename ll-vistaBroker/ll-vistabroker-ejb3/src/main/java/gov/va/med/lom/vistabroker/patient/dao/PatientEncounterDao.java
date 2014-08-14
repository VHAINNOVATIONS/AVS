package gov.va.med.lom.vistabroker.patient.dao;

import gov.va.med.lom.javaUtils.misc.DateUtils;
import gov.va.med.lom.javaUtils.misc.StringUtils;
import gov.va.med.lom.vistabroker.dao.BaseDao;
import gov.va.med.lom.vistabroker.misc.dao.MiscRpcsDao;
import gov.va.med.lom.vistabroker.patient.data.Encounter;
import gov.va.med.lom.vistabroker.patient.data.EncounterAppointment;
import gov.va.med.lom.vistabroker.util.FMDateUtils;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class PatientEncounterDao extends BaseDao {
  
  private static final String SKIP_ADMITS = "1";
  
  // CONSTRUCTORS
  public PatientEncounterDao() {
    super();
  }
  
  public PatientEncounterDao(BaseDao baseDao) {
    super(baseDao);
  }
  
  // RPC API
  public List<EncounterAppointment> getTodaysEncounterAppointments(String dfn) throws Exception {
    MiscRpcsDao miscRpcsDao = new MiscRpcsDao(this);
    long fmDate = miscRpcsDao.fmToday();
    Date today = FMDateUtils.fmDateTimeToDate(fmDate);
    GregorianCalendar fromCal = new GregorianCalendar();
    fromCal.setTime(today);
    fromCal.set(Calendar.HOUR_OF_DAY, 0);
    fromCal.set(Calendar.MINUTE, 0);
    fromCal.set(Calendar.SECOND, 1);            
    GregorianCalendar toCal = new GregorianCalendar();
    toCal.setTime(today);
    toCal.set(Calendar.HOUR_OF_DAY, 23);
    toCal.set(Calendar.MINUTE, 59);
    toCal.set(Calendar.SECOND, 59); 
    return getOutpatientEncounters(dfn, fromCal, toCal);
  }
  
  public List<EncounterAppointment> getOutpatientEncounters(String dfn, Calendar fromDate, Calendar toDate) throws Exception {
    List<EncounterAppointment> encounterList = getAllEncounters(dfn, fromDate, toDate);
    return filterEncounters(encounterList, null, "I");
  }
  
  public List<EncounterAppointment> getInpatientEncounters(String dfn, Calendar fromDate, Calendar toDate) throws Exception {
    List<EncounterAppointment> encounterList = getAllEncounters(dfn, fromDate, toDate);
    return filterEncounters(encounterList, "I", null);
  }
  
  public List<EncounterAppointment> getAllEncounters(String dfn, Calendar fromDate, Calendar toDate) throws Exception {
    return getAllEncounters(dfn, fromDate, toDate, false);
  }
  
  public List<EncounterAppointment> getAllEncounters(String dfn, Calendar fromDate, Calendar toDate, boolean skipAdmits) throws Exception {
    setDefaultContext("OR CPRS GUI CHART");
    setDefaultRpcName("ORWCV VST");
    MiscRpcsDao miscRpcsDao = new MiscRpcsDao(this);
    long fmDate = miscRpcsDao.fmToday();
    Date today = FMDateUtils.fmDateTimeToDate(fmDate);
    Date date1 = null;
    Date date2 = null;
    if (fromDate == null)
      date1 = DateUtils.subtractDaysFromDate(today, 365);
    else
      date1 = fromDate.getTime();
    if (toDate == null)
      date2 = DateUtils.addDaysToDate(today, 30);  
    else
      date2 = toDate.getTime();
    // Convert to FM date/time format and add 1 second to the time if 
    // the time is 00:00:00 (to avoid a VistA error).
    double fmDate1 = FMDateUtils.dateTimeToFMDateTime(date1);
    if (fmDate1 - ((int)fmDate1) == 0)
      fmDate1 += 0.000001;
    double fmDate2 = FMDateUtils.dateTimeToFMDateTime(date2);
    if (fmDate2 - ((int)fmDate2) == 0)
      fmDate2 += 0.000001;
    String param4 = null;
    if (skipAdmits)
      param4 = SKIP_ADMITS;
    else
      param4 = "0";
    Object[] params = {dfn, fmDate1, fmDate2, param4};
    List<String> list = lCall(params);
    List<EncounterAppointment> encounterList = new ArrayList<EncounterAppointment>();
    for (String s : list) {
      EncounterAppointment appointment = new EncounterAppointment();
      appointment.setRpcResult(s);
      String t = StringUtils.piece(s, 1);
      appointment.setDfn(dfn);
      String type = StringUtils.piece(t, ';', 1);
      appointment.setType(type);
      appointment.setLocationIen(StringUtils.piece(t, ';', 3));
      double fmDt = StringUtils.toDouble(StringUtils.piece(s, 2), 0);
      appointment.setFmdatetime(fmDt);
      if (fmDt > 0) {
        try {
          appointment.setDatetimeStr(FMDateUtils.fmDateTimeToEnglishDateTime(fmDt));
          appointment.setDatetime(FMDateUtils.fmDateTimeToDate(fmDt));
        } catch(ParseException pe) {}
      }
      if (type.length() > 0) {
        appointment.setStandalone(type.charAt(0) == 'V');
        if (type.equals("I")) {
          appointment.setLocationName(StringUtils.piece(s, 3) + " (" + StringUtils.piece(s, 4) + ")");
          appointment.setStatus(StringUtils.piece(s, 5));
        } else {
          appointment.setLocationName(StringUtils.piece(s, 3));
          appointment.setStatus(StringUtils.piece(s, 4));
        }
      } else {
        appointment.setStandalone(true);
        appointment.setLocationName(StringUtils.piece(s, 3));
        appointment.setStatus(StringUtils.piece(s, 4));
      }      
      String dts = null;
      try {
        if (fmDt > 0)
          dts = FMDateUtils.fmDateToEnglishDate(fmDt);
        else
          dts = "";
      } catch(ParseException pe) {
        dts = "";
      }
      appointment.setTitle(appointment.getLocationName() + "  " + dts);
      encounterList.add(appointment);
    }
    return encounterList;    
  }
  
  public Encounter getEncounterDetails(Encounter anEncounter, boolean inpatient) throws Exception {   
    setDefaultContext("OR CPRS GUI CHART");
    setDefaultRpcName("ORWPT ENCTITL");
      Encounter encounter = new Encounter();
      encounter.setDfn(anEncounter.getDfn());
      encounter.setFmdatetime(anEncounter.getFmdatetime());
      encounter.setDatetime(anEncounter.getDatetime());
      encounter.setDatetimeStr(anEncounter.getDatetimeStr());
      encounter.setLocationIen(anEncounter.getLocationIen());
      encounter.setProviderDuz(anEncounter.getProviderDuz());
      encounter.setTypeId(anEncounter.getTypeId());
      encounter.setType(anEncounter.getType());
      encounter.setTiuNoteIen(anEncounter.getTiuNoteIen());
      // Encounter Location/Provider
      Object[] params = {anEncounter.getDfn(), anEncounter.getLocationIen(), 
                         anEncounter.getProviderDuz()};
      String x = sCall(params);
      encounter.setLocationName(StringUtils.piece(x, 1));
      encounter.setLocationAbbr(StringUtils.piece(x, 2));
      encounter.setRoomBed(StringUtils.piece(x, 3));
      encounter.setProviderName(StringUtils.piece(x, 4));
      // Get visit category
      String c = getVisitCategory("A", anEncounter.getLocationIen(), inpatient);
      encounter.setVisitCat(c);
      encounter.setStandalone((c.equals("A")) || (c.equals("V")));
      encounter.setVisitStr(encounter.getLocationIen() + ";" + anEncounter.getFmdatetime() + ";" +
                            encounter.getVisitCat());
      encounter.setNeedVisit((anEncounter.getFmdatetime() == 0) &&
                             (encounter.getLocationIen().equals("0")));
      String loc = null;
      if (encounter.getLocationAbbr().length() > 0)
        loc = encounter.getLocationAbbr();
      else
        loc = encounter.getLocationName();
      if (encounter.getLocationName().length() > 0) {
        if (encounter.getVisitCat().equals("H"))
          encounter.setLocationText(loc + " " + encounter.getRoomBed());
        else {
          String dtTm = null;
          try {
            dtTm = DateUtils.formatDate(DateUtils.toAnsiDateTime(FMDateUtils.fmDateTimeToDate(anEncounter.getFmdatetime())));
          } catch(ParseException pe) {
            dtTm = "";
          }
          encounter.setLocationText(loc + " " + dtTm);
        }
      } else
        encounter.setLocationText("");
      return encounter;
  } 
  
  public String getVisitCategory(String initialCat, String locationIen, boolean inpatient) throws Exception {
    setDefaultContext("OR CPRS GUI CHART");
    setDefaultRpcName("ORWPCE GETSVC");
    Object[] params = {initialCat, locationIen, inpatient};
    String y = sCall(params);
    if (y.length() > 0)
      return y;
    else
      return initialCat;
  }
  
  public String getEncFutureDays() throws Exception {
    setDefaultContext("OR CPRS GUI CHART");
    setDefaultRpcName("ORWTPD1 GETEAFL");
    return sCall();
  }  
  
  public String savePceData(List<String> pceList, String noteIen, String locationIen) throws Exception {
    setDefaultContext("OR CPRS GUI CHART");
    setDefaultRpcName("ORWPCE SAVE");
    Object[] params = {pceList, noteIen, locationIen};
    return sCall(params);
  }
  
  private List<EncounterAppointment> filterEncounters(List<EncounterAppointment> encounterList, String includeType, String excludeType) {
    List<EncounterAppointment> encounters = new ArrayList<EncounterAppointment>();
    for (EncounterAppointment e : encounterList) {
      if (((includeType == null) || (e.getType().equals(includeType))) &&
          ((excludeType == null) || (!e.getType().equals(excludeType))))
        encounters.add(e);
    }
    return encounters;
  }
  
}
