package gov.va.med.lom.javaBroker.rpc.patient;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.Vector;
import java.text.ParseException;

import gov.va.med.lom.javaBroker.util.DateUtils;
import gov.va.med.lom.javaBroker.rpc.*;
import gov.va.med.lom.javaBroker.rpc.patient.models.*;
import gov.va.med.lom.javaBroker.util.FMDateUtils;
import gov.va.med.lom.javaBroker.util.StringUtils;

public class PatientEncounterRpc extends AbstractRpc {
  
  // FIELDS
  private Encounter encounter;

  private static final String SKIP_ADMITS = "1";
  
  // CONSTRUCTORS
  public PatientEncounterRpc() throws BrokerException {
    super();
  }
  
  public PatientEncounterRpc(RpcBroker rpcBroker) throws BrokerException {
    super(rpcBroker);
  }
  
  private EncounterAppointmentsList filterEncounters(EncounterAppointmentsList encounterList, String includeType, String excludeType) {
    EncounterAppointment[] encounters = encounterList.getEncounterAppointments();
    Vector v = new Vector();
    for (int i = 0; i < encounters.length; i++) {
      if (((includeType == null) || (encounters[i].getType().equals(includeType))) &&
          ((excludeType == null) || (!encounters[i].getType().equals(excludeType))))
        v.add(encounters[i]);
    }
    encounters = new EncounterAppointment[v.size()]; 
    for (int i = 0; i < v.size(); i++)
      encounters[i] = (EncounterAppointment)v.get(i);
    encounterList.setEncounterAppointments(encounters);  
    return encounterList;
  }
  
  // RPC API
  public synchronized EncounterAppointmentsList getTodaysEncounterAppointments(String dfn) throws BrokerException {
    long fmDate = MiscRPCs.fmToday(this.rpcBroker);
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
  
  public synchronized EncounterAppointmentsList getOutpatientEncounters(String dfn, Calendar fromDate, Calendar toDate) throws BrokerException {
    EncounterAppointmentsList encounterList = getAllEncounters(dfn, fromDate, toDate);
    return filterEncounters(encounterList, null, "I");
  }
  
  public synchronized EncounterAppointmentsList getInpatientEncounters(String dfn, Calendar fromDate, Calendar toDate) throws BrokerException {
    EncounterAppointmentsList encounterList = getAllEncounters(dfn, fromDate, toDate);
    return filterEncounters(encounterList, "I", null);
  }
  
  public synchronized EncounterAppointmentsList getAllEncounters(String dfn, Calendar fromDate, Calendar toDate) throws BrokerException {
    return getAllEncounters(dfn, fromDate, toDate, false);
  }
  
  public synchronized EncounterAppointmentsList getAllEncounters(String dfn, Calendar fromDate, Calendar toDate, boolean skipAdmits) throws BrokerException {
    if (setContext("OR CPRS GUI CHART")) {
      setDfn(dfn);
      EncounterAppointmentsList encounterAppointmentsList = new EncounterAppointmentsList();
      Date date1 = null;
      Date date2 = null;
      if (fromDate == null)
        date1 = DateUtils.subtractDaysFromDate(new Date(), 365);
      else
        date1 = fromDate.getTime();
      if (toDate == null)
        date2 = DateUtils.addDaysToDate(new Date(), 30);  
      else
        date2 = toDate.getTime();
      // Convert to FM date/time format and add 1 second to the time if 
      // the time is 00:00:00 (to avoid a VistA error).
      double fmDate1 = FMDateUtils.dateTimeToFMDateTime(date1);
      if (fmDate1 - ((int)fmDate1) == 0)
        fmDate1 += 0.0001;
      double fmDate2 = FMDateUtils.dateTimeToFMDateTime(date2);
      if (fmDate2 - ((int)fmDate2) == 0)
        fmDate2 += 0.0001;
      String param4 = null;
      if (skipAdmits)
        param4 = SKIP_ADMITS;
      else
        param4 = "0";
      Object[] params = {dfn, String.valueOf(fmDate1), 
                         String.valueOf(fmDate2), param4};
      ArrayList list = lCall("ORWCV VST", params);
      if (returnRpcResult) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < list.size(); i++)
          sb.append((String)list.get(i) + "\n");
        encounterAppointmentsList.setRpcResult(sb.toString().trim());
      }
      EncounterAppointment[] appointments = new EncounterAppointment[list.size()];
      for(int i = 0; i < list.size(); i++) {
        appointments[i] = new EncounterAppointment();
        String s = (String)list.get(i);
        if (returnRpcResult)
          appointments[i].setRpcResult(s);
        String t = StringUtils.piece(s, 1);
        appointments[i].setDfn(dfn);
        String type = StringUtils.piece(t, ';', 1);
        appointments[i].setType(type);
        appointments[i].setLocationIen(StringUtils.piece(t, ';', 3));
        double fmDt = StringUtils.toDouble(StringUtils.piece(s, 2), 0);
        if (fmDt > 0) {
          try {
            appointments[i].setDatetimeStr(FMDateUtils.fmDateTimeToEnglishDateTime(fmDt));
            appointments[i].setDatetime(DateUtils.toDate(appointments[i].getDatetimeStr(), 
                                                         DateUtils.ENGLISH_SHORT_DATE_TIME_FORMAT2));
          } catch(ParseException pe) {}
        }
        if (type.length() > 0) {
          appointments[i].setStandalone(type.charAt(0) == 'V');
          if (type.equals("I")) {
            appointments[i].setLocationName(StringUtils.piece(s, 3) + " (" + StringUtils.piece(s, 4) + ")");
            appointments[i].setStatus(StringUtils.piece(s, 5));
          } else {
            appointments[i].setLocationName(StringUtils.piece(s, 3));
            appointments[i].setStatus(StringUtils.piece(s, 4));
          }
        } else {
          appointments[i].setStandalone(true);
          appointments[i].setLocationName(StringUtils.piece(s, 3));
          appointments[i].setStatus(StringUtils.piece(s, 4));
        }
        String dts = null;
        try {
          if (appointments[i].getDatetimeStr() != null)
            dts = DateUtils.toEnglishDate(appointments[i].getDatetimeStr());
          else
            dts = "";
        } catch(ParseException pe) {
          dts = "";
        }
        appointments[i].setTitle(appointments[i].getLocationName() + "  " + dts);
      }
      encounterAppointmentsList.setEncounterAppointments(appointments);
      return encounterAppointmentsList;    
    } else
      throw getCreateContextException("OR CPRS GUI CHART");
  }
  
  public synchronized Encounter getEncounterDetails(Encounter anEncounter, boolean inpatient) throws BrokerException {   
    if (setContext("OR CPRS GUI CHART")) {
      setDfn(dfn);
      encounter = new Encounter();
      encounter.setDfn(dfn);
      encounter.setDatetime(anEncounter.getDatetime());
      encounter.setDatetimeStr(anEncounter.getDatetimeStr());
      encounter.setLocationIen(anEncounter.getLocationIen());
      encounter.setProviderDuz(anEncounter.getProviderDuz());
      encounter.setTypeId(anEncounter.getTypeId());
      encounter.setType(anEncounter.getType());
      StringBuffer sb = null;
      // Encounter Location/Provider
      String[] params = {dfn, anEncounter.getLocationIen(), anEncounter.getProviderDuz()};
      String x = sCall("ORWPT ENCTITL", params);
      if (returnRpcResult) {
        sb = new StringBuffer();
        sb.append(x + "\n");
      }
      encounter.setLocationName(StringUtils.piece(x, 1));
      encounter.setLocationAbbr(StringUtils.piece(x, 2));
      encounter.setRoomBed(StringUtils.piece(x, 3));
      encounter.setProviderName(StringUtils.piece(x, 4));
      // Get visit category
      String c = getVisitCategory("A", anEncounter.getLocationIen(), inpatient);
      encounter.setVisitCat(c);
      if (returnRpcResult) {
        sb.append(c);
        encounter.setRpcResult(sb.toString());
      }
      double fmDateTime = 0;
      // Try converting encounter date to FM date/time format
      try {
        fmDateTime = FMDateUtils.dateTimeToFMDateTime(anEncounter.getDatetime());
      } catch(Exception e) {
        // If that fails, try converting date/time string to FM format
        try {
          fmDateTime = FMDateUtils.ansiDateTimeToFMDateTime(anEncounter.getDatetimeStr());
        } catch(ParseException pe) {}
      }
      encounter.setStandalone((c.equals("A")) || (c.equals("V")));
      encounter.setVisitStr(encounter.getLocationIen() + ";" + fmDateTime + ";" +
                            encounter.getVisitCat());
      encounter.setNeedVisit((encounter.getDatetimeStr() == null) &&
                             (encounter.getLocationIen() == null));
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
            dtTm = DateUtils.formatDate(DateUtils.convertAnsiDateTimeStr(encounter.getDatetimeStr()), "MMM dd,yy HH:mm");
          } catch(ParseException pe) {
            dtTm = "";
          }
          encounter.setLocationText(loc + " " + dtTm);
        }
      } else
        encounter.setLocationText("");
      return encounter;
    } else
      throw getCreateContextException("OR CPRS GUI CHART");
  } 
  
  public synchronized String getVisitCategory(String initialCat, String locationIen, boolean inpatient) throws BrokerException {
    if (setContext("OR CPRS GUI CHART")) {
      String[] params = {String.valueOf(initialCat), locationIen, String.valueOf(inpatient)};
      String y = sCall("ORWPCE GETSVC", params);
      if (y.length() > 0)
        return y;
      else
        return initialCat;
    } else
      throw getCreateContextException("OR CPRS GUI CHART");
  }
  
  public synchronized String savePceData(List<String> pceList, String noteIen, String locationIen) throws BrokerException {
    if (setContext("OR CPRS GUI CHART")) {
      Object[] params = {pceList, noteIen, locationIen};
      return sCall("ORWPCE SAVE", params);
    } else
      throw getCreateContextException("OR CPRS GUI CHART");
  }  
  
}
