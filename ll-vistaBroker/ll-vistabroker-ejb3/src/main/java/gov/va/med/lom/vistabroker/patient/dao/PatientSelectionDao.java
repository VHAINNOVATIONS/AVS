package gov.va.med.lom.vistabroker.patient.dao;

import gov.va.med.lom.javaUtils.misc.DateUtils;
import gov.va.med.lom.javaUtils.misc.StringUtils;
import gov.va.med.lom.vistabroker.dao.BaseDao;
import gov.va.med.lom.vistabroker.patient.data.Patient;
import gov.va.med.lom.vistabroker.patient.data.SimilarRecordsStatus;
import gov.va.med.lom.vistabroker.util.FMDateUtils;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;

public class PatientSelectionDao extends BaseDao {
	
  // CONSTRUCTORS
  public PatientSelectionDao() {
    super();
  }
  
  public PatientSelectionDao(BaseDao baseDao) {
    super(baseDao);
  }
  
  // RPC API
	// Returns a list of patients (for use in a long list box)
  public List<Patient> listAllPatients() throws Exception {
    setDefaultContext("OR CPRS GUI CHART");
    setDefaultRpcName("ORWPT LIST ALL");
  	String[] params = {"", "1"};
  	List<String> list = lCall(params);
    return getItems(list, false, true);
  }
  
  // Returns a list of patients (for use in a long list box)
  public List<Patient> getSubSetOfPatients(String startFrom, int direction) throws Exception {
    setDefaultContext("OR CPRS GUI CHART");
    setDefaultRpcName("ORWPT LIST ALL");
    String[] params = {startFrom, String.valueOf(direction)};
  	List<String> list = lCall(params);
    return getItems(list, false, true);
  }
  
  // Lists all patients associated with a given provider: DFN^Patient Name
  public List<Patient> listPtByProvider(String ien) throws Exception {
    setDefaultContext("OR CPRS GUI CHART");
    setDefaultRpcName("ORQPT PROVIDER PATIENTS");
    List<String> list = lCall(ien);
    return getItems(list, false, true);
  }
  
  // Lists all patients associated with a given specialty: DFN^Patient Name
  public List<Patient> listPtBySpecialty(String ien) throws Exception {
    setDefaultContext("OR CPRS GUI CHART");
    setDefaultRpcName("ORQPT SPECIALTY PATIENTS");
    List<String> list = lCall(ien);
    return getItems(list, false, true);
  }
  
  // Lists all patients associated with a given team: DFN^Patient Name
  public List<Patient> listPtByTeam(String ien) throws Exception {
    setDefaultContext("OR CPRS GUI CHART");
    setDefaultRpcName("ORQPT TEAM PATIENTS");
    List<String> list = lCall(ien);
    return getItems(list, false, true);
  }  
  
  // Lists all patients associated with a given clinic: DFN^Patient Name^App't
  public List<Patient> listPtByClinic(String ien, Date firstDate, Date lastDate) throws Exception {
    setDefaultContext("OR CPRS GUI CHART");
    setDefaultRpcName("ORQPT CLINIC PATIENTS");
    double date1 = FMDateUtils.dateToFMDate(firstDate);
    double date2 = FMDateUtils.dateToFMDate(lastDate);
    String[] params = {String.valueOf(ien), String.valueOf(date1), String.valueOf(date2)};
    List<String> list = lCall(params);
    List<Patient> patients = new ArrayList<Patient>();
    for (String s : list) {
      if ((s.length() > 0) && (StringUtils.piece(s, 1).length() > 0)) {
        Patient patient = new Patient();
        patient.setDfn(StringUtils.piece(s, 1));
        patient.setName(StringUtils.mixedCase(StringUtils.piece(s, 2)));
        patient.setLocation(StringUtils.piece(s, 3));
        patient.setDate(FMDateUtils.fmDateTimeToDate(StringUtils.piece(s, 4)));
        try {
          patient.setDateStr(DateUtils.toEnglishDate(patient.getDate()));
        } catch(ParseException pe) {}
        patients.add(patient);
      } 
    }
    return patients;
  }
  
  // Lists all patients associated with a given ward: DFN^Patient Name^Room/Bed
  public List<Patient> listPtByWard(String ien) throws Exception {
    setDefaultContext("OR CPRS GUI CHART");
    setDefaultRpcName("ORWPT BYWARD");
    List<String> list = lCall(ien);
    List<Patient> patients = new ArrayList<Patient>();
    for (String s : list) {
      if ((s.length() > 0) && (StringUtils.piece(s, 1).length() > 0)) {
        Patient patient = new Patient();
        patient.setDfn(StringUtils.piece(s, 1));
        patient.setName(StringUtils.piece(s, 2));
        patient.setLocation(StringUtils.piece(s, 3));
        patients.add(patient);
      }
    }
    return patients;
  }
  
  // Lists all patients found in the BS and BS5 xrefs that match Last5
  public List<Patient> listPtByLast5(String last5) throws Exception {
    setDefaultContext("OR CPRS GUI CHART");
    setDefaultRpcName("ORWPT LAST5");
    String[] params = {String.valueOf(last5.toUpperCase())};
    List<String> list = lCall(params);
    return getItems(list, false, false);
  }
  
  // Lists all patients found in the BS and BS5 xrefs that match Last5
  public List<Patient> listPtByRPLLast5(String last5) throws Exception {
    setDefaultContext("OR CPRS GUI CHART");
    setDefaultRpcName("ORWPT LAST5 RPL");
    String[] params = {String.valueOf(last5.toUpperCase())};
    List<String> list = lCall(params);
    return getItems(list, false, false);
  }
  
  // Lists all patients found in the SSN xref that match FullSSN
  public List<Patient> listPtByFullSSN(String ssn) throws Exception {
    setDefaultContext("OR CPRS GUI CHART");
    setDefaultRpcName("ORWPT FULLSSN");
    ssn = StringUtils.unformatSSN(ssn);
    String[] params = {String.valueOf(ssn.toUpperCase())};
    List<String> list = lCall(params);
    return getItems(list, false, false);
  }
  
  // Lists all patients found in the SSN xref that match FullSSN
  public List<Patient> listPtByRPLFullSSN(String ssn) throws Exception {
    setDefaultContext("OR CPRS GUI CHART");
    setDefaultRpcName("ORWPT FULLSSN RPL");
    ssn = StringUtils.unformatSSN(ssn);
    String[] params = {String.valueOf(ssn.toUpperCase())};
    List<String> list = lCall(params);
    return getItems(list, false, false);
  }
  
  // Returns a list of patients (for use in a long list box)
  public List<Patient> readRPLPtList(String rplJobNumber, String startFrom, int direction) throws Exception {
    setDefaultContext("OR CPRS GUI CHART");
    setDefaultRpcName("ORQPT READ RPL");
    String[] params = {rplJobNumber, startFrom, String.valueOf(direction)};
    List<String> list = lCall(params);
    return getItems(list, false, true);
  }
  
  // Loads the default patient list
  public List<Patient> listPtByDflt() throws Exception {
    setDefaultContext("OR CPRS GUI CHART");
    setDefaultRpcName("ORQPT DEFAULT PATIENT LIST");
    String sort = getDfltSort();
    List<String> list = lCall();
    char s = getDfltPtListSrc();
    if (s == 'C') {
      // Clinics
      if (sort.equals("P")) {
        // Appointments sort
        list = StringUtils.sortByPiece(list, 4);
      } else {
        list = StringUtils.sortByPiece(list, 2);
      }
    } else {
      int sourceType = 0; // default
      if (sort.equals("M")) {
        sourceType = 1; // combinations
      } else if (sort.equals("W")) {
        sourceType = 2; // wards
      }
      switch (sourceType) {
        case 1 : if (sort.equals("S")) {
                   // "Source" sort
                   list = StringUtils.sortByPieces(list, new int[] {3, 8, 2});
                 } else if (sort.equals("P")) {
                   // "Appointment" sort
                   list = StringUtils.sortByPieces(list, new int[] {8, 2});
                 } else if (sort.equals("T")) {
                   // "Terminal Digit" sort
                   list = StringUtils.sortByPiece(list, 5);
                 } else {
                   // "Alphabetical" (default) sort
                   list = StringUtils.sortByPiece(list, 2);
                 }
                 break;
         case 2 : if (sort.equals("R")) {
                    list = StringUtils.sortByPieces(list, new int[] {3, 2});
                  } else {
                    list = StringUtils.sortByPiece(list, 2);
                  }
                  break;
      }
    }
    list = StringUtils.mixedCaseList(list);
    List<Patient> patients = new ArrayList<Patient>();
    for (String x : list) {
      if ((x.length() > 0) && (StringUtils.piece(x, 1).length() > 0)) {
        Patient patient = new Patient();
        patient.setDfn(StringUtils.piece(x, 1));
        patient.setName(StringUtils.piece(x, 2));
        patient.setDate(FMDateUtils.fmDateTimeToDate(StringUtils.piece(x, 3)));
        try {
          patient.setDateStr(DateUtils.toEnglishDate(patient.getDate()));
        } catch(ParseException pe) {}        
        patient.setLocation(StringUtils.piece(x, 4));
        patients.add(patient);
      }
    }    
    return patients;
  }  
  
  // Returns the last patient selected
  public Patient listPtTop() throws Exception {
    setDefaultContext("OR CPRS GUI CHART");
    setDefaultRpcName("ORWPT TOP");
    Patient patient = null;
    String x = StringUtils.mixedCase(sCall());
    if ((x.length() > 0) && (StringUtils.piece(x, 1).length() > 0)) {
      patient = new Patient();
      patient.setDfn(StringUtils.piece(x, 1));
      patient.setName(StringUtils.mixedCase(StringUtils.piece(x, 2)));
    }
    return patient;
  }  
  
  // Returns the name of the current user's default patient list
  public String getDfltPtList() throws Exception {
    setDefaultContext("OR CPRS GUI CHART");
    setDefaultRpcName("ORQPT DEFAULT LIST SOURCE");
    return sCall();
  }
  
  public char getDfltPtListSrc() throws Exception {
    setDefaultContext("OR CPRS GUI CHART");
    setDefaultRpcName("ORWPT DFLTSRC");
    String x = sCall();
    String s = StringUtils.piece(x, 2);
    if (s.length() > 0)
      return s.charAt(1);
    else
      return 0;
  }
  
  public void savePtListDflt(String x) throws Exception {
    setDefaultContext("OR CPRS GUI CHART");
    setDefaultRpcName("ORWPT SAVDFLT");
    sCall(x);
  }
  
  //returns user's default patient list sort order (string character).
  public String getDfltSort() throws Exception {
    setDefaultContext("OR CPRS GUI CHART");
    setDefaultRpcName("ORQPT DEFAULT LIST SOURCE");
    String x = sCall();
    if (x.length() == 0)
      x = "A"; // Default is always "A" for alpha.
    return x;
  }  
  
  //Creates "RPL" Restricted Patient List based on Team List info in user's record.
  public String makeRPLPtList(String rplList) throws Exception {
    setDefaultContext("OR CPRS GUI CHART");
    setDefaultRpcName("ORWPT MAKE ALL");
    return sCall(rplList);
  }  
  
  // Kills server global data
  public void killRPLPtList(String rplJobNumber) throws Exception {
    setDefaultContext("OR CPRS GUI CHART");
    setDefaultRpcName("ORQPT KILL RPL");
    String[] params = {rplJobNumber};
    sCall(params);
  }  
  
  public SimilarRecordsStatus getSimilarRecordsFound(String dfn) throws Exception {
    setDefaultContext("OR CPRS GUI CHART");
    setDefaultRpcName("DG CHK BS5 XREF Y/N");
    List<String> list = lCall(dfn);
    SimilarRecordsStatus similarRecordsStatus = new SimilarRecordsStatus();
    String x = (String)list.get(0);
    similarRecordsStatus.setExists(x.equals("1"));
    list.remove(0);
    StringBuffer sb = new StringBuffer();
    for(int i = 0; i < list.size(); i++)
      sb.append((String)list.get(0) + '\n');
    similarRecordsStatus.setMessage(sb.toString());
    return similarRecordsStatus;
  }  
  
  // Returns a list of selectable options from which a user can choose a date
  // range for appointments.
  public List<String> listDateRangeClinic() throws Exception {
    setDefaultContext("OR CPRS GUI CHART");
    setDefaultRpcName("ORWPT CLINRNG");
    return lCall();
  }  
    
  
  // Returns current default date range settings for displaying clinic appointments in patient lookup.
  public String defaultDateRangeClinic() throws Exception {
    setDefaultContext("OR CPRS GUI CHART");
    setDefaultRpcName("ORQPT DEFAULT CLINIC DATE RANG");
    return sCall();
  }    
  
  // Returns a DFN for a given SSN
  public String getAlsiDfnLookup(String ssn) throws Exception{
	  setDefaultContext(null);
	  setDefaultRpcName("ALSI DFN LOOKUP");
	  return sCall(ssn);
  }
  
  
  // Parses the result list from VistA and returns array of patient list item objects
  private List<Patient> getItems(List<String> list, boolean mixedCase, boolean checkAlias) {
    List<Patient> patientList = new ArrayList<Patient>();
    for(String s : list) {
      if ((s.length() > 0) && (StringUtils.piece(s, 1).length() > 0)) {
        Patient patient = new Patient();
        String dfn = StringUtils.piece(s, 1);
        String name;
        if (mixedCase)
          name = StringUtils.mixedCase(StringUtils.piece(s, 2));
        else
          name = StringUtils.piece(s, 2);
        patient.setName(name);
        if (checkAlias) {
          String nonAliasedName;
          if (mixedCase)
            nonAliasedName = StringUtils.mixedCase(StringUtils.piece(s, 6));
          else
            nonAliasedName = StringUtils.piece(s, 6);  
          if (name.compareTo(nonAliasedName) != 0) {
            name += "-- ALIAS ";
            patient.setAlias(true);
          } else
            patient.setAlias(false);
          patient.setNonAliasedName(nonAliasedName);  
        }  else
          patient.setNonAliasedName(name);
        if (StringUtils.piece(s, 3).length() == 0) {
          patient.setDfn(dfn);
        } else {
          Date date = FMDateUtils.fmDateTimeToDate(StringUtils.piece(s, 3));
          String ssn = StringUtils.piece(s, 4);
          patient.setDfn(dfn);
          patient.setDate(date);
          try {
            patient.setDateStr(DateUtils.toEnglishDate(patient.getDate()));
          } catch(ParseException pe) {}          
          patient.setSsn(ssn);
        }
        patientList.add(patient);
      }
    }
    return patientList;
  }    
   
}
