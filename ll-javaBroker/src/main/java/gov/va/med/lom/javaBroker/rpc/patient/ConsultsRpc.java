package gov.va.med.lom.javaBroker.rpc.patient;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

import gov.va.med.lom.javaBroker.rpc.*;
import gov.va.med.lom.javaBroker.rpc.patient.models.*;
import gov.va.med.lom.javaBroker.util.DateUtils;
import gov.va.med.lom.javaBroker.util.FMDateUtils;
import gov.va.med.lom.javaBroker.util.StringUtils;

public class ConsultsRpc extends AbstractRpc {

  // FIELDS
  private ConsultRequestsList consultRequestsList;
  
  // CONSTRUCTORS
  public ConsultsRpc() throws BrokerException {
    super();
  }
  
  public ConsultsRpc(RpcBroker rpcBroker) throws BrokerException {
    super(rpcBroker);
  }
  
  // RPC API
  public synchronized ConsultRequestsList getDefaultConsultRequests(String dfn) throws BrokerException {
    ConsultRequestsSelection consultRequestsSelection = new ConsultRequestsSelection();
    consultRequestsSelection.setService("");
    consultRequestsSelection.setStartDate(null);
    consultRequestsSelection.setStopDate(null);
    return getConsultRequests(dfn, consultRequestsSelection);
  }
  
  public synchronized ConsultRequestsList getConsultRequests(String dfn, ConsultRequestsSelection consultRequestsSelection) throws BrokerException {
    if (setContext("OR CPRS GUI CHART")) {
      setDfn(dfn);
      consultRequestsList = new ConsultRequestsList();
      Object[] params = null;
      double fmDate1 = 0;
      if (consultRequestsSelection.getStartDate() != null)
        fmDate1 = FMDateUtils.dateToFMDate(consultRequestsSelection.getStartDate());
      double fmDate2 = 0;
      if (consultRequestsSelection.getStopDate() != null)
        fmDate2 = FMDateUtils.dateToFMDate(consultRequestsSelection.getStopDate());
      if ((fmDate1 == 0) && (fmDate2 == 0) && (consultRequestsSelection.getService().equals(""))) {
        params = new Object[1];
        params[0] = dfn;
      } else {
        params = new Object[4];
        params[0] = dfn;
        params[1] = String.valueOf(fmDate1);
        params[2] = String.valueOf(fmDate2);
        params[3] = consultRequestsSelection.getService();
      }
      ArrayList list = lCall("ORQQCN LIST", params);
      // Consult id^date/time of request^status^consulting service^procedure
      ConsultRequest[] consultRequests = new ConsultRequest[list.size()];
      StringBuffer sb = new StringBuffer();
      for(int i = 0; i < list.size(); i++) {
        String x = (String)list.get(i);
        if (returnRpcResult) 
          sb.append((String)list.get(i) + "\n");
        if ((x.trim().length() > 0) && (StringUtils.piece(x, 2).length() > 0)) {
          String ien = StringUtils.piece(x, 1);
          Date dt = FMDateUtils.fmDateTimeToDate(StringUtils.toDouble(StringUtils.piece(x, 2), 0));
          String status = StringUtils.piece(x, 3);
          String service = StringUtils.piece(x, 4);
          String consultNumber = "Consult #: " + ien;
          String text = StringUtils.piece(x, 6) + StringUtils.piece(x, 7);
          String parentId = StringUtils.piece(x, 8);
          String type = StringUtils.piece(x, 9);
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
          consultRequests[i] = new ConsultRequest();
          if (returnRpcResult)
            consultRequests[i].setRpcResult(x); 
          consultRequests[i].setDfn(dfn);
          consultRequests[i].setIen(ien);
          consultRequests[i].setStatus(status);
          consultRequests[i].setDatetime(dt);
          try {
            consultRequests[i].setDatetimeStr(DateUtils.toEnglishDateTime(dt));
          } catch(ParseException pe) {}
          consultRequests[i].setService(service);
          consultRequests[i].setText(text);
          consultRequests[i].setConsultNumber(consultNumber);
          consultRequests[i].setParentId(parentId);
          consultRequests[i].setType(type);
          consultRequests[i].setTypeStr(typeStr);
        }
      }
      consultRequestsList.setConsultRequests(consultRequests);
      if (returnRpcResult) 
        consultRequestsList.setRpcResult(sb.toString().trim());
      return consultRequestsList;
    } else
      throw getCreateContextException("OR CPRS GUI CHART");
  }
  
  public synchronized String getConsultDetail(String ien) throws BrokerException {
    if (setContext("OR CPRS GUI CHART"))
      return sCall("ORQQCN DETAIL", ien);
    else
      throw getCreateContextException("OR CPRS GUI CHART");
  }
  
  /**
   * Returns a display of Medicine Package results, followed by any TIU results.
   * @param ien
   * @return
   * @throws BrokerException
   */
  public synchronized String getMedResults(String ien) throws BrokerException {
    if (setContext("OR CPRS GUI CHART"))
      return sCall("ORQQCN MED RESULTS", ien);
    else
      throw getCreateContextException("OR CPRS GUI CHART");
  }
  
  public synchronized ConsultRec getConsultRec(String ien) throws BrokerException {
    if (setContext("OR CPRS GUI CHART")) {
      boolean showAddenda = true;
      Object[] params = {StringUtils.boolToStr(showAddenda, "true", "false")};
      ArrayList list = lCall("ORQQCN GET CONSULT", params);
      ConsultRec consultRec = new ConsultRec();
      consultRec.setIen(ien);
      if (returnRpcResult) {
        StringBuffer sb = new StringBuffer();
        for(int i = 0; i < list.size(); i++)
          sb.append((String)list.get(i) + "\n");
        consultRec.setRpcResult(sb.toString().trim());
      }    
      if (list.size() > 0) {
        String x = (String)list.get(0);
        if (!StringUtils.piece(x, 1).equals("-1")) {
          Date dt = FMDateUtils.fmDateTimeToDate(StringUtils.toDouble(StringUtils.piece(x, 1), 0));
          consultRec.setEntryDate(dt);
          try {
            consultRec.setEntryDateStr(DateUtils.toEnglishDate(dt));
          } catch(ParseException pe) {}
          consultRec.setOrFileNumber(StringUtils.piece(x, 3));
          consultRec.setPatientLocationIen(StringUtils.piece(x, 4));
          consultRec.setOrderingFacilityIen(StringUtils.piece(x, 21));
          consultRec.setForeignConsultFileNum(StringUtils.piece(x, 22));
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
    } else
      throw getCreateContextException("OR CPRS GUI CHART");
  }
  
  public synchronized Consult findConsult(String consultIen) throws BrokerException {
    if (setContext("OR CPRS GUI CHART")) {
      String x = sCall("ORQQCN FIND CONSULT", consultIen);
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
    } else
      throw getCreateContextException("OR CPRS GUI CHART");
  }
  
  public synchronized String getConsultOrderIen(String consultIen) throws BrokerException {
    if (setContext("OR CPRS GUI CHART"))
      return sCall("ORQQCN GET ORDER NUMBER", consultIen);
    else
      throw getCreateContextException("OR CPRS GUI CHART");
  }  
  
  public synchronized String getProcedureIen(String orderIen) throws BrokerException {
    if (setContext("OR CPRS GUI CHART"))
      return sCall("ORQQCN GET PROC IEN", orderIen);
    else                               
      throw getCreateContextException("OR CPRS GUI CHART");
  }   
  
  public synchronized String getCPRequests(String dfn) throws BrokerException {
    if (setContext("OR CPRS GUI CHART")) {
      String s = sCall("MAG4 CP GET REQUESTS", dfn);
      return s;
  } else
      throw getCreateContextException("OR CPRS GUI CHART");
  } 
  
  public synchronized LockDocumentResult lockConsultRequest(String ien) throws BrokerException {
    String orderIen = getConsultOrderIen(ien);
    if (!orderIen.equals("0")) {
      OrdersRpc ordersRpc = new OrdersRpc(this.rpcBroker);
      return ordersRpc.lockOrder(ien);
    } else
      return new LockDocumentResult();
  }   
  
  public synchronized String unlockConsult(String noteIen) throws BrokerException {
    TiuNoteRpc tiuNoteRpc = new TiuNoteRpc(this.rpcBroker);
    String consultIen = tiuNoteRpc.getConsultIenforNote(noteIen);
    if (consultIen != null) {
      OrdersRpc ordersRpc = new OrdersRpc(this.rpcBroker);
      String orderIen = getConsultOrderIen(consultIen);
      if (orderIen != null)
        return ordersRpc.unlockOrder(orderIen);
      else
        return "";
    }
    else
      return "";
  }   
  
}
