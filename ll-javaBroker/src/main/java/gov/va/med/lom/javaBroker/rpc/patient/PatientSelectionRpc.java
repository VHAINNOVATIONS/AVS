package gov.va.med.lom.javaBroker.rpc.patient;

import java.util.List;
import java.util.ArrayList;
import java.util.Date;
import java.util.Vector;
import java.text.ParseException;

import gov.va.med.lom.javaBroker.rpc.*;
import gov.va.med.lom.javaBroker.rpc.patient.models.*;
import gov.va.med.lom.javaBroker.util.FMDateUtils;
import gov.va.med.lom.javaBroker.util.StringUtils;
import gov.va.med.lom.javaBroker.util.DateUtils;

public class PatientSelectionRpc extends AbstractRpc {
	
  // FIELDS
  private PatientList patientList;

  // CONSTRUCTORS
  public PatientSelectionRpc() throws BrokerException {
    super();
  }
  
  public PatientSelectionRpc(RpcBroker rpcBroker) throws BrokerException {
    super(rpcBroker);
  }
  
  // RPC API
	// Returns a list of patients (for use in a long list box)
  public synchronized PatientList listAllPatients() throws BrokerException {
    if (setContext("OR CPRS GUI CHART")) {
    	String[] params = {"", "1"};
    	ArrayList list = lCall("ORWPT LIST ALL", params);
      patientList = getItems(list, false, true);
      return patientList;
    } else
      throw getCreateContextException("OR CPRS GUI CHART");
  }
  
  // Returns a list of patients (for use in a long list box)
  public synchronized PatientList getSubSetOfPatients(String startFrom, int direction) throws BrokerException {
    if (setContext("OR CPRS GUI CHART")) {
      String[] params = {startFrom, String.valueOf(direction)};
    	ArrayList list = lCall("ORWPT LIST ALL", params);
      patientList = getItems(list, false, true);
      return patientList;
    } else
      throw getCreateContextException("OR CPRS GUI CHART");
  }
  
  // Lists all patients associated with a given provider: DFN^Patient Name
  public synchronized PatientList listPtByProvider(String ien) throws BrokerException {
    if (setContext("OR CPRS GUI CHART")) {
      ArrayList list = lCall("ORQPT PROVIDER PATIENTS", ien);
      patientList = getItems(list, false, true);
      return patientList;
    } else
      throw getCreateContextException("OR CPRS GUI CHART");
  }
  
  // Lists all patients associated with a given specialty: DFN^Patient Name
  public synchronized PatientList listPtBySpecialty(String ien) throws BrokerException {
    if (setContext("OR CPRS GUI CHART")) {
      ArrayList list = lCall("ORQPT SPECIALTY PATIENTS", ien);
      patientList = getItems(list, false, true);
      return patientList;
    } else
      throw getCreateContextException("OR CPRS GUI CHART");
  }
  
  // Lists all patients associated with a given team: DFN^Patient Name
  public synchronized PatientList listPtByTeam(String ien) throws BrokerException {
    if (setContext("OR CPRS GUI CHART")) {
      ArrayList list = lCall("ORQPT TEAM PATIENTS", ien);
      patientList = getItems(list, false, true);
      return patientList;
    } else
      throw getCreateContextException("OR CPRS GUI CHART");
  }  
  
  // Lists all patients associated with a given clinic: DFN^Patient Name^App't
  public synchronized PatientList listPtByClinic(String ien, Date firstDate, Date lastDate) throws BrokerException {
    if (setContext("OR CPRS GUI CHART")) {
      patientList = new PatientList();
      double date1 = FMDateUtils.dateToFMDate(firstDate);
      double date2 = FMDateUtils.dateToFMDate(lastDate);
      String[] params = {ien, String.valueOf(date1), String.valueOf(date2)};
      ArrayList list = lCall("ORQPT CLINIC PATIENTS", params);
      if (returnRpcResult) {
        StringBuffer sb = new StringBuffer();
        for(int i = 0; i < list.size(); i++)
          sb.append((String)list.get(i) + "\n");
        patientList.setRpcResult(sb.toString().trim());
      }     
      Vector vect = new Vector();
      for (int i = 0; i < list.size(); i++) {
        String x = (String)list.get(i);
        if ((x.length() > 0) && (StringUtils.piece(x, 1).length() > 0)) {
          PatientListItem patientListItem = new PatientListItem();
          if (returnRpcResult)
            patientListItem.setRpcResult(x);        
          patientListItem.setDfn(StringUtils.piece(x, 1));
          patientListItem.setName(StringUtils.mixedCase(StringUtils.piece(x, 2)));
          patientListItem.setDate(FMDateUtils.fmDateTimeToDate(StringUtils.piece(x, 3)));
          try {
            patientListItem.setDateStr(DateUtils.toEnglishDate(patientListItem.getDate()));
          } catch(ParseException pe) {}
          patientListItem.setLocation(StringUtils.piece(x, 4));
          vect.add(patientListItem);
        } 
      }
      PatientListItem[] patientListItems = new PatientListItem[vect.size()];
      for(int i = 0; i < patientListItems.length; i++) 
        patientListItems[i] = (PatientListItem)vect.get(i);
      patientList.setPatientListItems(patientListItems);
      return patientList;
    } else
      throw getCreateContextException("OR CPRS GUI CHART");
  }
  
  // Lists all patients associated with a given ward: DFN^Patient Name^Room/Bed
  public synchronized PatientList listPtByWard(String ien) throws BrokerException {
    if (setContext("OR CPRS GUI CHART")) {
      patientList = new PatientList();
      ArrayList list = lCall("ORWPT BYWARD", ien);
      if (returnRpcResult) {
        StringBuffer sb = new StringBuffer();
        for(int i = 0; i < list.size(); i++)
          sb.append((String)list.get(i) + "\n");
        patientList.setRpcResult(sb.toString().trim());
      }     
      Vector vect = new Vector();
      for (int i = 0; i < list.size(); i++) {
        String x = (String)list.get(i);
        if ((x.length() > 0) && (StringUtils.piece(x, 1).length() > 0)) {
          PatientListItem patientListItem = new PatientListItem();
          if (returnRpcResult)
            patientListItem.setRpcResult(x);
          patientListItem.setDfn(StringUtils.piece(x, 1));
          patientListItem.setName(StringUtils.piece(x, 2));
          patientListItem.setLocation(StringUtils.piece(x, 3));
          vect.add(patientListItem);
        }
      }
      PatientListItem[] patientListItems = new PatientListItem[vect.size()];
      for(int i = 0; i < patientListItems.length; i++) 
        patientListItems[i] = (PatientListItem)vect.get(i);
      patientList.setPatientListItems(patientListItems);
      return patientList;
    } else
      throw getCreateContextException("OR CPRS GUI CHART");
  }
  
  // Lists all patients found in the BS and BS5 xrefs that match Last5
  public synchronized PatientList listPtByLast5(String last5) throws BrokerException {
    if (setContext("OR CPRS GUI CHART")) {
      String[] params = {String.valueOf(last5.toUpperCase())};
      ArrayList list = lCall("ORWPT LAST5", params);
      patientList = getItems(list, false, false);
      return patientList;
    } else
      throw getCreateContextException("OR CPRS GUI CHART");
  }
  
  // Lists all patients found in the BS and BS5 xrefs that match Last5
  public synchronized PatientList listPtByRPLLast5(String last5) throws BrokerException {
    if (setContext("OR CPRS GUI CHART")) {
      String[] params = {String.valueOf(last5.toUpperCase())};
      ArrayList list = lCall("ORWPT LAST5 RPL", params);
      patientList = getItems(list, false, false);
      return patientList;
    } else
      throw getCreateContextException("OR CPRS GUI CHART");
  }
  
  // Lists all patients found in the SSN xref that match FullSSN
  public synchronized PatientList listPtByFullSSN(String ssn) throws BrokerException {
    if (setContext("OR CPRS GUI CHART")) {
      ssn = StringUtils.unformatSSN(ssn);
      String[] params = {String.valueOf(ssn.toUpperCase())};
      ArrayList list = lCall("ORWPT FULLSSN", params);
      patientList = getItems(list, false, false);
      return patientList;
    } else
      throw getCreateContextException("OR CPRS GUI CHART");
  }
  
  // Lists all patients found in the SSN xref that match FullSSN
  public synchronized PatientList listPtByRPLFullSSN(String ssn) throws BrokerException {
    if (setContext("OR CPRS GUI CHART")) {
      ssn = StringUtils.unformatSSN(ssn);
      String[] params = {String.valueOf(ssn.toUpperCase())};
      ArrayList list = lCall("ORWPT FULLSSN RPL", params);
      patientList = getItems(list, false, false);
      return patientList;
    } else
      throw getCreateContextException("OR CPRS GUI CHART");
  }
  
  // Returns a list of patients (for use in a long list box)
  public synchronized PatientList readRPLPtList(String rplJobNumber, String startFrom, int direction) throws BrokerException {
    if (setContext("OR CPRS GUI CHART")) {
      String[] params = {rplJobNumber, startFrom, String.valueOf(direction)};
      ArrayList list = lCall("ORQPT READ RPL", params);
      patientList = getItems(list, false, true);
      return patientList;
    } else
      throw getCreateContextException("OR CPRS GUI CHART");
  }
  
  // Loads the default patient list
  public synchronized PatientList listPtByDflt() throws BrokerException {
    if (setContext("OR CPRS GUI CHART")) {
      String sort = getDfltSort();
      List<String> list = lCall("ORQPT DEFAULT PATIENT LIST");
      patientList = new PatientList();
      if (returnRpcResult) {
        StringBuffer sb = new StringBuffer();
        for(int i = 0; i < list.size(); i++)
          sb.append((String)list.get(i) + "\n");
        patientList.setRpcResult(sb.toString().trim());
      }     
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
      Vector vect = new Vector();
      for (int i = 0; i < list.size(); i++) {
        String x = (String)list.get(i);
        if ((x.length() > 0) && (StringUtils.piece(x, 1).length() > 0)) {
          PatientListItem patientListItem = new PatientListItem();
          if (returnRpcResult)
            patientListItem.setRpcResult(x);
          patientListItem.setDfn(StringUtils.piece(x, 1));
          patientListItem.setName(StringUtils.piece(x, 2));
          patientListItem.setDate(FMDateUtils.fmDateTimeToDate(StringUtils.piece(x, 3)));
          try {
            patientListItem.setDateStr(DateUtils.toEnglishDate(patientListItem.getDate()));
          } catch(ParseException pe) {}        
          patientListItem.setLocation(StringUtils.piece(x, 4));
          vect.add(patientListItem);
        }
      }    
      PatientListItem[] patientListItems = new PatientListItem[vect.size()];
      for(int i = 0; i < patientListItems.length; i++) 
        patientListItems[i] = (PatientListItem)vect.get(i);
      patientList.setPatientListItems(patientListItems);
      return patientList;
    } else
      throw getCreateContextException("OR CPRS GUI CHART");
  }  
  
  // Returns the last patient selected
  public synchronized PatientListItem listPtTop() throws BrokerException {
    if (setContext("OR CPRS GUI CHART")) {
      patientList = new PatientList();
      String x = StringUtils.mixedCase(sCall("ORWPT TOP"));
      if ((x.length() > 0) && (StringUtils.piece(x, 1).length() > 0)) {
        PatientListItem patientListItem = new PatientListItem();
        if (returnRpcResult)
          patientListItem.setRpcResult(x);
        String dfn = StringUtils.piece(x, 1);
        String name = StringUtils.mixedCase(StringUtils.piece(x, 2));
        patientListItem.setDfn(dfn);
        patientListItem.setName(name);
        return patientListItem;
      } else
        return new PatientListItem();
    } else
      throw getCreateContextException("OR CPRS GUI CHART");
  }  
  
  // Returns the name of the current user's default patient list
  public synchronized String getDfltPtList() throws BrokerException {
    if (setContext("OR CPRS GUI CHART")) {
      String x = sCall("ORQPT DEFAULT LIST SOURCE");
      return StringUtils.piece(x, 2);
    } else
      throw getCreateContextException("OR CPRS GUI CHART");
  }
  
  public synchronized char getDfltPtListSrc() throws BrokerException {
    if (setContext("OR CPRS GUI CHART")) {
      String x = sCall("ORWPT DFLTSRC");
      String s = StringUtils.piece(x, 2);
      if (s.length() > 0)
        return s.charAt(1);
      else
        return 0;
    } else
      throw getCreateContextException("OR CPRS GUI CHART");
  }
  
  public synchronized void savePtListDflt(String x) throws BrokerException {
    if (setContext("OR CPRS GUI CHART")) {
      String[] params = {x};
      sCall("ORWPT SAVDFLT", params);
    } else
      throw getCreateContextException("OR CPRS GUI CHART");
  }
  
  //returns user's default patient list sort order (string character).
  public synchronized String getDfltSort() throws BrokerException {
    if (setContext("OR CPRS GUI CHART")) {
      String x = sCall("ORQPT DEFAULT LIST SOURCE");
      if (x.length() == 0)
        x = "A"; // Default is always "A" for alpha.
      return x;
    } else
      throw getCreateContextException("OR CPRS GUI CHART");
  }  
  
  //Creates "RPL" Restricted Patient List based on Team List info in user's record.
  public synchronized String makeRPLPtList(String rplList) throws BrokerException {
    if (setContext("OR CPRS GUI CHART")) {
      String[] params = {rplList};
      String x = sCall("ORWPT MAKE ALL", params);
      return x;
    } else
      throw getCreateContextException("OR CPRS GUI CHART");
  }  
  
  // Kills server global data
  public synchronized void killRPLPtList(String rplJobNumber) throws BrokerException {
    if (setContext("OR CPRS GUI CHART")) {
      String[] params = {rplJobNumber};
      sCall("ORQPT KILL RPL", params);
    } else
      throw getCreateContextException("OR CPRS GUI CHART");
  }  
  
  public synchronized SimilarRecordsStatus getSimilarRecordsFound(String dfn) throws BrokerException {
    if (setContext("OR CPRS GUI CHART")) {
      ArrayList list = lCall("DG CHK BS5 XREF Y/N", dfn);
      SimilarRecordsStatus similarRecordsStatus = new SimilarRecordsStatus();
      String x = (String)list.get(0);
      if (returnRpcResult)
        similarRecordsStatus.setRpcResult(x);    
      similarRecordsStatus.setExists(x.equals("1"));
      list.remove(0);
      StringBuffer sb = new StringBuffer();
      for(int i = 0; i < list.size(); i++)
        sb.append((String)list.get(0) + '\n');
      similarRecordsStatus.setMessage(sb.toString());
      return similarRecordsStatus;
    } else
      throw getCreateContextException("OR CPRS GUI CHART");
  }  
  
  // Returns a list of selectable options from which a user can choose a date
  // range for appointments.
  public synchronized String[] listDateRangeClinic() throws BrokerException {
    if (setContext("OR CPRS GUI CHART")) {
      ArrayList list = lCall("ORWPT CLINRNG");
      String[] strs = new String[list.size()];
      for (int i = 0; i < list.size(); i++)
        strs[i] = (String)list.get(i);
      return strs;
    } else
      throw getCreateContextException("OR CPRS GUI CHART");
  }  
    
  
  // Returns current default date range settings for displaying clinic appointments in patient lookup.
  public synchronized String defaultDateRangeClinic() throws BrokerException {
    if (setContext("OR CPRS GUI CHART"))
      return sCall("ORQPT DEFAULT CLINIC DATE RANG");
    else
      throw getCreateContextException("OR CPRS GUI CHART");
  }    
  
  // Parses the result list from VistA and returns array of patient list item objects
  private synchronized PatientList getItems(ArrayList list, boolean mixedCase, boolean checkAlias) {
    patientList = new PatientList();
    if (returnRpcResult) {
      StringBuffer sb = new StringBuffer();
      for(int i = 0; i < list.size(); i++)
        sb.append((String)list.get(i) + "\n");
      patientList.setRpcResult(sb.toString().trim());
    }    
    Vector vect = new Vector();
    for(int i=0; i < list.size(); i++) {
      String x = (String)list.get(i);
      if ((x.length() > 0) && (StringUtils.piece(x, 1).length() > 0)) {
        PatientListItem patientListItem = new PatientListItem();
        if (returnRpcResult)
          patientListItem.setRpcResult(x);
        String dfn = StringUtils.piece(x, 1);
        String name;
        if (mixedCase)
          name = StringUtils.mixedCase(StringUtils.piece(x, 2));
        else
          name = StringUtils.piece(x, 2);
        patientListItem.setName(name);
        if (checkAlias) {
          String nonAliasedName;
          if (mixedCase)
            nonAliasedName = StringUtils.mixedCase(StringUtils.piece(x, 6));
          else
            nonAliasedName = StringUtils.piece(x, 6);  
          if (name.compareTo(nonAliasedName) != 0) {
            name += "-- ALIAS ";
            patientListItem.setIsAlias(true);
          } else
            patientListItem.setIsAlias(false);
          patientListItem.setNonAliasedName(nonAliasedName);  
        }  else
          patientListItem.setNonAliasedName(name);
        if (StringUtils.piece(x, 3).length() == 0) {
          patientListItem.setDfn(dfn);
        } else {
          Date date = FMDateUtils.fmDateTimeToDate(StringUtils.piece(x, 3));
          String ssn = StringUtils.piece(x, 4);
          patientListItem.setDfn(dfn);
          patientListItem.setDate(date);
          try {
            patientListItem.setDateStr(DateUtils.toEnglishDate(patientListItem.getDate()));
          } catch(ParseException pe) {}          
          patientListItem.setSsn(ssn);
        }
        vect.add(patientListItem);
      }
    }
    PatientListItem[] listItems = new PatientListItem[vect.size()]; 
    for(int i = 0; i < vect.size(); i++)
      listItems[i] = (PatientListItem)vect.get(i);
    if (returnRpcResult) {
      StringBuffer sb = new StringBuffer();
      for(int i = 0; i < list.size(); i++)
        sb.append((String)list.get(i) + "\n");
      patientList.setRpcResult(sb.toString().trim());
    }       
    patientList.setPatientListItems(listItems);
    return patientList;
  }    
   
}
