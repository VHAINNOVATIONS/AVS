package gov.va.med.lom.javaBroker.rpc.patient;

import java.util.ArrayList;

import gov.va.med.lom.javaBroker.rpc.*;
import gov.va.med.lom.javaBroker.rpc.patient.models.*;
import gov.va.med.lom.javaBroker.util.StringUtils;

public class TiuNoteRpc extends AbstractRpc {
  
  // FIELDS
  private TiuNote tiuNote;
  
  // CONSTRUCTORS
  public TiuNoteRpc() throws BrokerException {
    super();
  }
  
  public TiuNoteRpc(RpcBroker rpcBroker) throws BrokerException {
    super(rpcBroker);
  }
  
  // RPC API 
  public synchronized TiuNote getTiuNote(String ien) throws BrokerException {
    if (setContext("OR CPRS GUI CHART")) {
      tiuNote = new TiuNote();
      String text = sCall("TIU GET RECORD TEXT", ien);
      if (returnRpcResult)
        tiuNote.setRpcResult(text);
      tiuNote.setIen(ien);
      tiuNote.setText(text);
      return tiuNote;
    } else
      throw getCreateContextException("OR CPRS GUI CHART");
  }
  
  public synchronized TiuNote getTiuNoteForEdit(String ien) throws BrokerException {
    if (setContext("OR CPRS GUI CHART")) {
      tiuNote = new TiuNote();       
      String[] params = {ien, ".01;.06;.07;1301;1204;1208;1701;1205;1405;2101;70201;70202"};
      StringBuffer text = new StringBuffer(sCall("TIU LOAD RECORD FOR EDIT", params));
      int index =text.indexOf("$TXT");
      if (index < 0) {
        tiuNote.setIen(null);
        tiuNote.setMessage(text.delete(text.indexOf("~"), 2).toString());
      } else {
        text.delete(0, text.indexOf("$TXT") + 6);
        tiuNote.setIen(ien);
        tiuNote.setText(text.toString());
      }
      if (returnRpcResult)
        tiuNote.setRpcResult(text.toString());
      return tiuNote;
    } else
      throw getCreateContextException("OR CPRS GUI CHART");
  }
  
  public synchronized String getTIUListItem(String docIen) throws BrokerException {
    if (setContext("OR CPRS GUI CHART")) {
      String x = sCall("ORWTIU GET LISTBOX ITEM", docIen);
      return x;
    } else
      throw getCreateContextException("OR CPRS GUI CHART");
  } 
  
  public synchronized boolean isSurgeryTitle(String titleIen) throws BrokerException {
    if (setContext("OR CPRS GUI CHART")) {
      String x = sCall("TIU IS THIS A SURGERY", titleIen);
      return x.equals("1");
    } else
      throw getCreateContextException("OR CPRS GUI CHART");
  }
  
  public synchronized boolean isConsultTitle(String titleIen) throws BrokerException {
    if (setContext("OR CPRS GUI CHART")) {
      String x = sCall("TIU IS THIS A CONSULT", titleIen);
      return x.equals("1");
    } else
      throw getCreateContextException("OR CPRS GUI CHART");
  }   
  
  public synchronized boolean authorHasSigned(String titleIen, String userDuz) throws BrokerException {
    if (setContext("OR CPRS GUI CHART")) {
      String[] params = {titleIen, userDuz};
      String x = sCall("TIU HAS AUTHOR SIGNED?", params);
      return x.equals("1");
    } else
      throw getCreateContextException("OR CPRS GUI CHART");
  }  
  
  public synchronized String getTitleIenForNote(String ien) throws BrokerException {
    if (setContext("OR CPRS GUI CHART")) {
      return sCall("TIU GET DOCUMENT TITLE", ien);
    } else
      throw getCreateContextException("OR CPRS GUI CHART");
  }     
  
  public synchronized String getPackageRefForNote(String ien) throws BrokerException {
    if (setContext("OR CPRS GUI CHART")) {
      String x = sCall("TIU GET REQUEST", ien);
      return x;
    } else
      throw getCreateContextException("OR CPRS GUI CHART");
  }   
  
  public synchronized String getConsultIenforNote(String ien) throws BrokerException {
    if (setContext("OR CPRS GUI CHART")) {
      String x = sCall("TIU GET REQUEST", ien);
      if (StringUtils.piece(x, ';', 2) != "GMR(123,")
        return "-1";
      else
        return StringUtils.piece(x, ';', 1);
    } else
      throw getCreateContextException("OR CPRS GUI CHART");
  } 
  
  public synchronized String getVisitStrForNote(String ien) throws BrokerException {
    if (setContext("OR CPRS GUI CHART")) {
      String x = sCall("ORWPCE NOTEVSTR", ien);
      return x;
    } else
      throw getCreateContextException("OR CPRS GUI CHART");
  }   
  
  public synchronized String[] getGetPCEDataForNote(String ien, String patientDfn, String visitStr) throws BrokerException {
    if (setContext("OR CPRS GUI CHART")) {
      ArrayList list = null; 
      if (ien != null) {
        list =  lCall("ORWPCE PCE4NOTE", ien);
      } else {
        String[] params = {"", patientDfn, visitStr};
        list = lCall("ORWPCE PCE4NOTE", params);
      }
      String[] strings = new String[list.size()];
      for(int i = 0; i < list.size(); i++)
        strings[i] = (String)list.get(i);
      return strings;
    } else
      throw getCreateContextException("OR CPRS GUI CHART");
  }   
  
  public synchronized String getTiuDetails(String ien) throws BrokerException {
    if (setContext("OR CPRS GUI CHART")) {
      String text = sCall("TIU DETAILED DISPLAY", ien);
      return text;
    } else
      throw getCreateContextException("OR CPRS GUI CHART");
  }
  
  public synchronized String loadTiuRecordForEdit(String ien) throws BrokerException {
    if (setContext("OR CPRS GUI CHART")) {
      String text = sCall("TIU LOAD RECORD FOR EDIT", ien);
      return text;
    } else
      throw getCreateContextException("OR CPRS GUI CHART");
  }  
  
  public synchronized LockDocumentResult lockDocument(String ien) throws BrokerException {
    if (setContext("OR CPRS GUI CHART")) {
      LockDocumentResult lockDocumentResult = new LockDocumentResult();
      String x = sCall("TIU LOCK RECORD", ien);
      if (x.charAt(0) == '0') {
        lockDocumentResult.setSuccess(true);
        lockDocumentResult.setMessage("");
      } else {
        lockDocumentResult.setSuccess(false);
        lockDocumentResult.setMessage(StringUtils.piece(x, 2));      
      }
      return lockDocumentResult;
    } else
      throw getCreateContextException("OR CPRS GUI CHART");
  }   
  
  public synchronized void unlockDocument(String ien) throws BrokerException {
    if (setContext("OR CPRS GUI CHART"))
      sCall("TIU UNLOCK RECORD", ien);
    else
      throw getCreateContextException("OR CPRS GUI CHART");
  }     
  
  
  public boolean deleteDocument(String ien, String reason) throws BrokerException {
    String[] params = {String.valueOf(ien), reason};
    if (setContext("OR CPRS GUI CHART")) {
      String x = sCall("TIU DELETE RECORD", params);
      return StringUtils.piece(x, 1).equals("0");
    } else
      throw getCreateContextException("OR CPRS GUI CHART");    
  }
  
  public boolean justifyDocumentDelete(String ien) throws BrokerException {
    if (setContext("OR CPRS GUI CHART")) {
      String x = sCall("TIU JUSTIFY DELETE?", ien);
      return x.equals("1");
    } else
      throw getCreateContextException("OR CPRS GUI CHART");        
  }
  
}
