package gov.va.med.lom.vistabroker.patient.dao;

import gov.va.med.lom.javaUtils.misc.DateUtils;
import gov.va.med.lom.javaUtils.misc.StringUtils;
import gov.va.med.lom.vistabroker.dao.BaseDao;
import gov.va.med.lom.vistabroker.lists.dao.SpecialtiesTeamsDao;
import gov.va.med.lom.vistabroker.patient.data.Demographics;
import gov.va.med.lom.vistabroker.util.FMDateUtils;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class DemographicsDao extends BaseDao {
 
  @SuppressWarnings("unused")
  private static final Log log = LogFactory.getLog(DemographicsDao.class);
	
  // GENDER STRINGS
  public static final String SEX_MALE = "Male";
  public static final String SEX_FEMALE = "Female";
  public static final String SEX_UNKNOWN = "Unknown";
  
  /*
   * FIELDS
   */ 
  private static Map<String, Map<String, String>> specialtiesMap;
  
  /*
   * CONSTRUCTORS
   */
  public DemographicsDao()  {
    super();
  }
  
  public DemographicsDao(BaseDao baseDao) {
    super(baseDao);
  }
  
  /*
   * RPC API setDefaultRpcName();
   */ 
  public Demographics getDemographics(String dfn) throws Exception {
    setDefaultContext("OR CPRS GUI CHART");
    Demographics demographics = null;
    setDefaultRpcName("ORWPT SELECT");
    String x = sCall(dfn);
    //  NAME^SEX^DOB^SSN^LOCIEN^LOCNM^RMBD^CWAD^SENSITIVE^ADMITTED^CONV^SC^SC%^ICN
    if(StringUtils.piece(x, 1).equals("-1")) {
    	return null;
    }
    demographics = new Demographics();
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
    setDefaultRpcName("ORWPT DIEDON");
    x = sCall(dfn);
    demographics.setDeceasedDate(FMDateUtils.fmDateTimeToDate(Double.valueOf(x).doubleValue()));
    try {
      demographics.setDeceasedDateStr(DateUtils.toEnglishDate(demographics.getDeceasedDate()));
    } catch(ParseException pe) {} 
    setDefaultRpcName("ORWPT1 PRCARE");
    x = sCall(dfn);   
    demographics.setPrimaryTeam(StringUtils.piece(x, 1));
    demographics.setPrimaryProvider(StringUtils.piece(x, 2));
    if (demographics.getLocation().length() > 0)
      demographics.setAttending(StringUtils.piece(x, 3));
    else 
      demographics.setAttending("");
    // Get the specialties list
    if (specialtiesMap == null){
      specialtiesMap = new HashMap<String, Map<String, String>>();
    }
    if(!specialtiesMap.containsKey(getDivision())){
      SpecialtiesTeamsDao specialtiesTeamsRpc = new SpecialtiesTeamsDao(this);
      specialtiesMap.put(getDivision(), specialtiesTeamsRpc.listAllSpecialtiesMap());
    }
    if (demographics.getSpecialtyIen() != null){
      Map<String, String> map = specialtiesMap.get(getDivision());
      demographics.setSpecialty(map.get(demographics.getSpecialtyIen()));
    }else{
      demographics.setSpecialty("");
    }
    return demographics;
  }
  
  public Demographics getAlsiDemographics(String dfn) throws Exception {
	    
	  	setDefaultContext(null);
	  	setDefaultRpcName("ALSI PATIENT SELECT");
	  	
	  	Demographics demographics = null;
	    
	    String x = sCall(dfn);
	    //   1    2   3   4   5       6    7    8    9          10     11   12  13  14
        //  NAME^SEX^DOB^SSN^LOCIEN^LOCNM^RMBD^CWAD^SENSITIVE^ADMITTED^CONV^SC^SC%^ICN
        //   15   16                       17            18                 19
        //  AGE^SPECIALTY IEN^?NOT SURE WHAT THIS IS?^SPECIALTY TEAM NAME^DIEDON^
        //     20                        21           22
        //  PRIMARY CARE TEAM^PRIMARY CARE PROVIDER^ATTENDING^

	    
	    if(StringUtils.piece(x, 1).equals("-1")) {
	    	return null;
	    }
	    demographics = new Demographics();
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
        demographics.setDeceasedDate(FMDateUtils.fmDateTimeToDate(Double.valueOf(StringUtils.piece(x, 19)).doubleValue()));
        try {
          demographics.setDeceasedDateStr(DateUtils.toEnglishDate(demographics.getDeceasedDate()));
        } catch(ParseException pe) {}
        demographics.setPrimaryTeam(StringUtils.piece(x, 20));
        demographics.setPrimaryProvider(StringUtils.piece(x, 21));
        if (demographics.getLocation().length() > 0)
          demographics.setAttending(StringUtils.piece(x, 22));
        else
          demographics.setAttending("");

        if (demographics.getSpecialtyIen() != null){
          demographics.setSpecialty(StringUtils.piece(x, 18));
        }else{
          demographics.setSpecialty("");
        }
        return demographics;

	  }
  
  
  
}
