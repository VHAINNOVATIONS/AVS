package gov.va.med.lom.javaBroker.rpc.patient;

import java.text.ParseException;
import java.util.HashMap;

import gov.va.med.lom.javaBroker.rpc.*;
import gov.va.med.lom.javaBroker.rpc.patient.models.*;
import gov.va.med.lom.javaBroker.rpc.lists.SpecialtiesTeamsRpc;
import gov.va.med.lom.javaBroker.rpc.lists.models.SpecialtiesTeamsList;
import gov.va.med.lom.javaBroker.rpc.lists.models.SpecialtyTeam;
import gov.va.med.lom.javaBroker.util.FMDateUtils;
import gov.va.med.lom.javaBroker.util.StringUtils;
import gov.va.med.lom.javaBroker.util.DateUtils;

public class DemographicsRpc extends AbstractRpc {
  
  // GENDER STRINGS
  public static final String SEX_MALE = "Male";
  public static final String SEX_FEMALE = "Female";
  public static final String SEX_UNKNOWN = "Unknown";
  
  // FIELDS
  private Demographics demographics;
  private static HashMap specialtiesMap;
  
  // CONSTRUCTORS
  public DemographicsRpc() throws BrokerException {
    super();
  }
  
  public DemographicsRpc(RpcBroker rpcBroker) throws BrokerException {
    super(rpcBroker);
  }
  
  // RPC API
  public synchronized Demographics getDemographics(String dfn) throws BrokerException {
    if (setContext("OR CPRS GUI CHART")) {
      setDfn(dfn);
      demographics = new Demographics();
      String x = sCall("ORWPT SELECT", dfn);
      StringBuffer sb = null;
      if (returnRpcResult) {
        sb = new StringBuffer();
        sb.append(x + "\n");
      }
      //  NAME^SEX^DOB^SSN^LOCIEN^LOCNM^RMBD^CWAD^SENSITIVE^ADMITTED^CONV^SC^SC%^ICN
      if (!StringUtils.piece(x, 1).equals("-1")) {
        demographics.setDfn(dfn);
        demographics.setName(StringUtils.mixedCase(StringUtils.piece(x, 1)));   
        demographics.setSensitive(StringUtils.piece(x, 9).equals("1"));
        demographics.setIcn(StringUtils.piece(x, 14));
        demographics.setSsn(StringUtils.formatSSN(StringUtils.piece(x, 4)));
        demographics.setSex(StringUtils.piece(x, 2));
        demographics.setLocationIen(StringUtils.piece(x, 5));
        demographics.setLocation(StringUtils.piece(x, 6));
        demographics.setRoomBed(StringUtils.piece(x, 7));
        demographics.setSpecialtyIen(StringUtils.piece(x, 16));
        demographics.setCwad(StringUtils.piece(x, 8));
        demographics.setRestricted(StringUtils.piece(x, 9).equals("1"));
        demographics.setAdmitTime(FMDateUtils.fmDateTimeToDate(StringUtils.toDouble(StringUtils.piece(x, 10), 0.0)));
        try {
          demographics.setAdmitTimeStr(DateUtils.toEnglishDate(demographics.getAdmitTime()));
        } catch(ParseException pe) {}
        demographics.setServiceConnected(StringUtils.piece(x, 12).equals("1"));
        demographics.setServiceConnectedPercent(StringUtils.toInt(StringUtils.piece(x, 13), 0));
        demographics.setDob(FMDateUtils.fmDateTimeToDate(StringUtils.toDouble(StringUtils.piece(x, 3), 0.0)));
        try {
          demographics.setDobStr(DateUtils.toEnglishDate(demographics.getDob()));
        } catch(ParseException pe) {}      
        demographics.setAge(DateUtils.calcAge(demographics.getDob()));
        x = sCall("ORWPT DIEDON", dfn);
        if (returnRpcResult)
          sb.append(x + "\n");
        demographics.setDeceasedDate(FMDateUtils.fmDateTimeToDate(Double.valueOf(x).doubleValue()));
        try {
          demographics.setDeceasedDateStr(DateUtils.toEnglishDate(demographics.getDeceasedDate()));
        } catch(ParseException pe) {}            
        x = sCall("ORWPT1 PRCARE", dfn);   
        if (returnRpcResult)
          sb.append(x + "\n");      
        demographics.setPrimaryTeam(StringUtils.piece(x, 1));
        demographics.setPrimaryProvider(StringUtils.piece(x, 2));
        if (demographics.getLocation().length() > 0)
          demographics.setAttending(StringUtils.piece(x, 3));
        else 
          demographics.setAttending("");
        // Get the specialties list
        if (specialtiesMap == null){
        	specialtiesMap = new HashMap();
        }
        if(!specialtiesMap.containsKey(rpcBroker.getStationNo())){
          SpecialtiesTeamsRpc specialtiesTeamsRpc = new SpecialtiesTeamsRpc(this.rpcBroker);
          SpecialtiesTeamsList specTeamsList = specialtiesTeamsRpc.listAllSpecialties();
          SpecialtyTeam[] specialties = specTeamsList.getSpecialtiesTeams();
          HashMap map = new HashMap();
          for (int i = 0; i < specialties.length; i++)
            map.put(new Long(specialties[i].getIen()), specialties[i].getName());
          specialtiesMap.put(rpcBroker.getStationNo(), map);
        }
        if (demographics.getSpecialtyIen() != null){
          HashMap map = (HashMap)specialtiesMap.get(rpcBroker.getStationNo());
          demographics.setSpecialty((String)map.get(new Long(demographics.getSpecialtyIen())));
        }else{
          demographics.setSpecialty("");
        }
      }
      if (returnRpcResult)
        demographics.setRpcResult(sb.toString());
      return demographics;
    } else
      throw getCreateContextException("OR CPRS GUI CHART");
  }
  
}
