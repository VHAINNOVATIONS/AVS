package gov.va.med.lom.vistabroker.patient.dao;

import gov.va.med.lom.javaUtils.misc.DateUtils;
import gov.va.med.lom.javaUtils.misc.StringUtils;
import gov.va.med.lom.vistabroker.dao.BaseDao;
import gov.va.med.lom.vistabroker.patient.data.Consult;
import gov.va.med.lom.vistabroker.patient.data.ConsultRec;
import gov.va.med.lom.vistabroker.patient.data.ConsultRequest;
import gov.va.med.lom.vistabroker.patient.data.ConsultRequestsSelection;
import gov.va.med.lom.vistabroker.patient.data.EditResubmit;
import gov.va.med.lom.vistabroker.patient.data.LockDocumentResult;
import gov.va.med.lom.vistabroker.util.FMDateUtils;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ConsultsDao extends BaseDao {

  // CONSTRUCTORS
  public ConsultsDao() {
    super();
  }
  
  public ConsultsDao(BaseDao baseDao) {
    super(baseDao);
  }
  
  // RPC API
  public List<ConsultRequest> getDefaultConsultRequests(String dfn) throws Exception {
    ConsultRequestsSelection consultRequestsSelection = new ConsultRequestsSelection();
    consultRequestsSelection.setService("");
    consultRequestsSelection.setStartDate(null);
    consultRequestsSelection.setStopDate(null);
    return getConsultRequests(dfn, consultRequestsSelection);
  }
  
  public List<ConsultRequest> getConsultRequests(String dfn, ConsultRequestsSelection consultRequestsSelection) throws Exception {
    setDefaultContext("OR CPRS GUI CHART");
    Object[] params = null;
    if (consultRequestsSelection == null) {
      consultRequestsSelection = new ConsultRequestsSelection();
    }
    double fmDate1 = 0;
    if (consultRequestsSelection.getStartDate() != null)
      fmDate1 = FMDateUtils.dateToFMDate(consultRequestsSelection.getStartDate());
    double fmDate2 = 0;
    if (consultRequestsSelection.getStopDate() != null)
      fmDate2 = FMDateUtils.dateToFMDate(consultRequestsSelection.getStopDate());
    if ((fmDate1 == 0) && (fmDate2 == 0) && ((consultRequestsSelection.getService() == null) || 
        consultRequestsSelection.getService().equals(""))) {
      params = new Object[1];
      params[0] = String.valueOf(dfn);
    } else {
      params = new Object[4];
      params[0] = String.valueOf(dfn);
      params[1] = String.valueOf(fmDate1);
      params[2] = String.valueOf(fmDate2);
      params[3] = consultRequestsSelection.getService();
    }
    
    List<ConsultRequest> consultRequests = new ArrayList<ConsultRequest>();
    setDefaultRpcName("ORQQCN LIST");
    List<String> list = lCall(params);
    // Consult id^date/time of request^status^consulting service^procedure
    for(String s : list) {
      if ((s.trim().length() > 0) && (StringUtils.piece(s, 2).length() > 0)) {
        String ien = StringUtils.piece(s, 1);
        Date dt = FMDateUtils.fmDateTimeToDate(StringUtils.piece(s, 2));
        String status = StringUtils.piece(s, 3);
        String service = StringUtils.piece(s, 4);
        String consultNumber = "Consult #: " + ien;
        String text = StringUtils.piece(s, 6) + StringUtils.piece(s, 7);
        String parentId = StringUtils.piece(s, 8);
        String type = StringUtils.piece(s, 9);
        String typeStr = null;
        char c = type.charAt(0);
        switch (c) { 
          case 'C':  typeStr = "Consult"; break;
          case 'P':  typeStr = "Procedure"; break;
          case 'M':  typeStr = "Procedure"; break;
          case 'I':  typeStr = "Consult - Interfacility"; break;
          case 'R':  typeStr = "Procedure - Interfacility"; break;
          default : typeStr = "";
        }
        ConsultRequest consultRequest = new ConsultRequest();
        consultRequest.setDfn(dfn);
        consultRequest.setIen(ien);
        consultRequest.setStatus(status);
        consultRequest.setDatetime(dt);
        try {
          consultRequest.setDatetimeStr(DateUtils.toEnglishDateTime(dt));
        } catch(ParseException pe) {}
        consultRequest.setService(service);
        consultRequest.setText(text);
        consultRequest.setConsultNumber(consultNumber);
        consultRequest.setParentId(parentId);
        consultRequest.setType(type);
        consultRequest.setTypeStr(typeStr);
        consultRequests.add(consultRequest);
      }
    }
    return consultRequests;
  }
  
  public String getConsultDetail(String ien) throws Exception {
    setDefaultContext("OR CPRS GUI CHART");
    setDefaultRpcName("ORQQCN DETAIL");
    return sCall(ien);
  }
  
  /**
   * Returns a display of Medicine Package results, followed by any TIU results.
   * @param ien
   * @return
   * @throws Exception
   */
  public String getMedResults(String ien) throws Exception {
    setDefaultContext("OR CPRS GUI CHART");
    setDefaultRpcName("ORQQCN MED RESULTS");
    return sCall(ien);
  }
  
  public ConsultRec getConsultRec(String ien, boolean showAddenda) throws Exception {
    setDefaultContext("OR CPRS GUI CHART");
    setDefaultRpcName("ORQQCN GET CONSULT");
    Object[] params = {StringUtils.boolToStr(showAddenda, "true", "false")};
    List<String> list = lCall(params);
    ConsultRec consultRec = new ConsultRec();
    consultRec.setIen(ien);
    if (list.size() > 0) {
      String x = (String)list.get(0);
      if (!StringUtils.piece(x, 1).equals("-1")) {
        Date dt = FMDateUtils.fmDateTimeToDate(StringUtils.toDouble(StringUtils.piece(x, 1), 0));
        consultRec.setEntryDate(dt);
        try {
          consultRec.setEntryDateStr(DateUtils.toEnglishDate(dt));
        } catch(ParseException pe) {}
        consultRec.setOrFileNumber(StringUtils.toInt(StringUtils.piece(x, 3), 0));
        consultRec.setPatientLocationIen(StringUtils.piece(x, 4));
        consultRec.setOrderingFacilityIen(StringUtils.piece(x, 21));
        consultRec.setForeignConsultFileNum(StringUtils.toInt(StringUtils.piece(x, 22), 0));
        consultRec.setToServiceIen(StringUtils.piece(x, 5));
        consultRec.setFromIen(StringUtils.piece(x, 6));
        dt = FMDateUtils.fmDateTimeToDate(StringUtils.toDouble(StringUtils.piece(x, 7), 0));
        consultRec.setRequestDate(dt);
        try {
          consultRec.setRequestDateStr(DateUtils.toEnglishDate(dt));
        } catch(ParseException pe) {}
        consultRec.setConsultProcedure(StringUtils.piece(x, 8));
        consultRec.setPlaceOfConsultIen(StringUtils.piece(x, 10));
        consultRec.setAttention(StringUtils.toInt(StringUtils.piece(x, 11), 0));
        consultRec.setOrStatus(StringUtils.toInt(StringUtils.piece(x, 12), 0));
        consultRec.setLastAction(StringUtils.toInt(StringUtils.piece(x, 13), 0));
        consultRec.setSendingProviderDfn(StringUtils.piece(StringUtils.piece(x, 14), ';', 1));
        consultRec.setSendingProviderName(StringUtils.piece(StringUtils.piece(x, 14), ';', 2));
        consultRec.setResult(StringUtils.piece(x, 15));
        consultRec.setModeOfEntry(StringUtils.piece(x, 16));
        consultRec.setRequestType(StringUtils.toInt(StringUtils.piece(x, 17), 0));
        consultRec.setInOut(StringUtils.piece(x, 18));
        consultRec.setFindings(StringUtils.piece(x, 19));
        consultRec.setTiuResultNarrative(StringUtils.toInt(StringUtils.piece(x, 20), 0));
        list.remove(0);
        StringBuffer tiuDocuments = new StringBuffer();
        StringBuffer medResults = new StringBuffer();
        if (list.size() > 0) {
          StringUtils.sortByPiece(list, 3);
          for (int i = 0; i < list.size(); i++) {
            String y = (String)list.get(i);
            if (StringUtils.piece(StringUtils.piece(y, 1), ';', 2).substring(1, 5).equalsIgnoreCase("mcar"))
              medResults.append(y + "\n");
            else
              tiuDocuments.append(y + "\n");
          }
        }
      }
    }
    return consultRec;
  }
  
  public Consult findConsult(String consultIen) throws Exception {
    setDefaultContext("OR CPRS GUI CHART");
    setDefaultRpcName("ORQQCN FIND CONSULT");
    String x = sCall(consultIen);
    Consult consult = new Consult();
    consult.setIen(StringUtils.piece(x, 1));
    Date dt = FMDateUtils.fmDateTimeToDate(StringUtils.toDouble(StringUtils.piece(x, 11), 0));
    consult.setDatetime(dt);
    try {
      consult.setDatetimeStr(DateUtils.toEnglishDate(dt));
    } catch(ParseException pe) {}    
    consult.setStatus(StringUtils.piece(x, 3));
    consult.setText(StringUtils.piece(x, 4));
    consult.setNum(StringUtils.toInt(StringUtils.piece(x, 5), 0));
    consult.setOrderIfn(StringUtils.piece(x, 6));
    consult.setParentIen(StringUtils.piece(x, 8));
    char c = StringUtils.piece(x, 9).charAt(0);
    switch (c) {
      case 'C' : consult.setType("Consult"); break;
      case 'P' : consult.setType("Procedure"); break;
      case 'M' : consult.setType("Procedure"); break;
      case 'I' : consult.setType("Consult - Interfacility"); break;
      case 'R' : consult.setType("Procedure - Interfacility"); break;
    }
    consult.setService(StringUtils.piece(x, 10));
    return consult;
  }
  
  public String getConsultOrderIen(String consultIen) throws Exception {
    setDefaultContext("OR CPRS GUI CHART");
    setDefaultRpcName("ORQQCN GET ORDER NUMBER");
    return sCall(consultIen);
  }  
  
  public String getProcedureIen(String orderIen) throws Exception {
    setDefaultContext("OR CPRS GUI CHART");
    setDefaultRpcName("ORQQCN GET PROC IEN");
    return sCall(orderIen);
  }   
  
  public String getCPRequests(String dfn) throws Exception {
    setDefaultContext("OR CPRS GUI CHART");
    setDefaultRpcName("MAG4 CP GET REQUESTS");
    return sCall(dfn);
  } 
  
  public String denyConsult(String orderIen, String userDuz, Date dcDate, String comments) throws Exception {
    setDefaultContext("OR CPRS GUI CHART");
    setDefaultRpcName("ORQQCN DISCONTINUE");
    if (dcDate == null)
      dcDate = new Date();
    double fmDate = FMDateUtils.dateTimeToFMDateTime(dcDate);
    Object[] params = {orderIen, userDuz, String.valueOf(fmDate), "DY", comments};
    return sCall(params);
  }
  
  public String discontinueConsult(String orderIen, String userDuz, Date dcDate, String comments) throws Exception {
    setDefaultContext("OR CPRS GUI CHART");
    setDefaultRpcName("ORQQCN DISCONTINUE");
    if (dcDate == null)
      dcDate = new Date();
    double fmDate = FMDateUtils.dateTimeToFMDateTime(dcDate);
    Object[] params = {orderIen, userDuz, String.valueOf(fmDate), "DC", comments};
    return sCall(params);
  }
  
  public String addConsultComment(String consultId, List<String> comments, boolean alert, String alertTo, double actionDate) throws Exception {
    setDefaultContext("OR CPRS GUI CHART");
    setDefaultRpcName("ORQQCN ADDCMT");
    Object[] params = {consultId, comments, "1", alertTo, actionDate};
    return sCall(params);
  }  
  
  public EditResubmit loadConsultForEdit(String consultIen) throws Exception {
    setDefaultContext("OR CPRS GUI CHART");
    setDefaultRpcName("ORQQCN LOAD FOR EDIT");
    List<String> list = lCall(consultIen);
    EditResubmit editRec = new EditResubmit();
    editRec.setChanged(false);
    editRec.setIen(consultIen);
    editRec.setToService(StringUtils.piece(StringUtils.extractDefault(list, "SERVICE"), 2));
    editRec.setRequestType(StringUtils.piece(StringUtils.extractDefault(list, "TYPE"), 3));
    editRec.setOrderableItem(StringUtils.piece(StringUtils.extractDefault(list, "PROCEDURE"), 1));
    editRec.setConsultProc(StringUtils.piece(StringUtils.extractDefault(list, "PROCEDURE"), 3));
    editRec.setConsultProcName(StringUtils.piece(StringUtils.extractDefault(list, "PROCEDURE"), 2));
    editRec.setUrgency(StringUtils.piece(StringUtils.extractDefault(list, "URGENCY"), 3));
    editRec.setUrgencyName(StringUtils.piece(StringUtils.extractDefault(list, "URGENCY"), 2));
    editRec.setEarliestDate(StringUtils.toDouble(StringUtils.piece(StringUtils.extractDefault(list, "EARLIEST"), 2), 0));
    editRec.setPlace(StringUtils.piece(StringUtils.extractDefault(list, "PLACE"), 1));
    editRec.setPlaceName(StringUtils.piece(StringUtils.extractDefault(list, "PLACE"), 2));
    editRec.setAttention(StringUtils.piece(StringUtils.extractDefault(list, "ATTENTION"), 1));
    editRec.setAttentionName(StringUtils.piece(StringUtils.extractDefault(list, "ATTENTION"), 2));
    editRec.setInpOutp(StringUtils.piece(StringUtils.extractDefault(list, "CATEGORY"), 1));
    editRec.setProvDiagnosis(StringUtils.piece(StringUtils.extractDefault(list, "DIAGNOSIS"), 1));
    editRec.setProvDxCode(StringUtils.piece(StringUtils.extractDefault(list, "DIAGNOSIS"), 2));
    editRec.setProvDxCodeInactive(StringUtils.piece(StringUtils.extractDefault(list, "DIAGNOSIS"), 3).equals("1"));
    editRec.setRequestReason(StringUtils.extractText(list, "REASON"));
    editRec.setDenyComments(StringUtils.extractText(list, "DENY COMMENT"));
    editRec.setNewComments(StringUtils.extractText(list, "ADDED COMMENT"));
    return editRec;
  } 
  
  public LockDocumentResult lockConsultRequest(String ien) throws Exception {
    String orderIen = getConsultOrderIen(ien);
    if (!orderIen.equals("0")) {
      OrdersDao ordersDao = new OrdersDao(this);
      return ordersDao.lockOrder(ien);
    } else
      return null;
  }   
  
  public String unlockConsult(String noteIen) throws Exception {
    TiuNoteDao tiuNoteDao = new TiuNoteDao(this);
    String consultIen = tiuNoteDao.getConsultIenforNote(noteIen);
    if (!consultIen.equals("0")) {
      OrdersDao ordersDao = new OrdersDao(this);
      String orderIen = getConsultOrderIen(consultIen);
      if (!orderIen.equals("0"))
        return ordersDao.unlockOrder(orderIen);
      else
        return "";
    }
    else
      return "";
  }   
  
}
