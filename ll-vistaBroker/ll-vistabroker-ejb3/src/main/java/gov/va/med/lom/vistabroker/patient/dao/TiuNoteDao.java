package gov.va.med.lom.vistabroker.patient.dao;

import gov.va.med.lom.javaUtils.misc.StringUtils;
import gov.va.med.lom.vistabroker.dao.BaseDao;
import gov.va.med.lom.vistabroker.patient.data.LockDocumentResult;
import gov.va.med.lom.vistabroker.patient.data.TiuNote;

import java.util.List;

public class TiuNoteDao extends BaseDao {
  
  // CONSTRUCTORS
  public TiuNoteDao() {
    super();
  }
  
  public TiuNoteDao(BaseDao baseDao) {
    super(baseDao);
  }
  
  // RPC API 
  public TiuNote getTiuNote(String ien) throws Exception {
    setDefaultContext("OR CPRS GUI CHART");
    setDefaultRpcName("TIU GET RECORD TEXT");
    TiuNote tiuNote = new TiuNote();
    String text = sCall(ien);
    tiuNote.setIen(ien);
    tiuNote.setText(text);
    return tiuNote;
  }
  
  public TiuNote getTiuNoteForEdit(String ien) throws Exception {
    setDefaultContext("OR CPRS GUI CHART");
    setDefaultRpcName("TIU LOAD RECORD FOR EDIT");
    TiuNote tiuNote = new TiuNote();       
    String[] params = {String.valueOf(ien), ".01;.06;.07;1301;1204;1208;1701;1205;1405;2101;70201;70202"};
    StringBuffer text = new StringBuffer(sCall(params));
    int index = text.indexOf("$TXT");
    if (index < 0) {
      tiuNote.setIen(null);
      tiuNote.setMessage(text.delete(text.indexOf("~"), 2).toString());
    } else {
      text.delete(0, index + 4);
      tiuNote.setIen(ien);
      tiuNote.setText(text.toString().trim());
    }
    return tiuNote;
  }
  
  public String getTiuListItem(String docIen) throws Exception {
    setDefaultContext("OR CPRS GUI CHART");
    setDefaultRpcName("ORWTIU GET LISTBOX ITEM");
    String x = sCall(docIen);
    return x;
  } 
  
  public boolean isSurgeryTitle(String titleIen) throws Exception {
    setDefaultContext("OR CPRS GUI CHART");
    setDefaultRpcName("TIU IS THIS A SURGERY");
    String x = sCall(titleIen);
    return x.equals("1");
  }
  
  public boolean isConsultTitle(String titleIen) throws Exception {
    setDefaultContext("OR CPRS GUI CHART");
    setDefaultRpcName("TIU IS THIS A CONSULT");
    String x = sCall(titleIen);
    return x.equals("1");
  }   
  
  public boolean authorHasSigned(String titleIen, String userDuz) throws Exception {
    setDefaultContext("OR CPRS GUI CHART");
    setDefaultRpcName("TIU HAS AUTHOR SIGNED?");
    Object[] params = {titleIen, userDuz};
    String x = sCall(params);
    return x.equals("1");
  }  
  
  public String getTitleIenForNote(String ien) throws Exception {
    setDefaultContext("OR CPRS GUI CHART");
    setDefaultRpcName("TIU GET DOCUMENT TITLE");
    return sCall(ien);
  }     
  
  public String getPackageRefForNote(String  ien) throws Exception {
    setDefaultContext("OR CPRS GUI CHART");
    setDefaultRpcName("TIU GET REQUEST");
    return sCall(ien);
  }   
  
  public String getConsultIenforNote(String  ien) throws Exception {
    setDefaultContext("OR CPRS GUI CHART");
    setDefaultRpcName("TIU GET REQUEST");
    String x = sCall(ien);
    if (StringUtils.piece(x, ';', 2) != "GMR(123,")
      return "-1";
    else
      return StringUtils.piece(x, ';', 1);
  } 
  
  public String getVisitStrForNote(String ien) throws Exception {
    setDefaultContext("OR CPRS GUI CHART");
    setDefaultRpcName("ORWPCE NOTEVSTR");
    return sCall(ien);
  }   
  
  public List<String> getPCEDataForNote(String ien, String patientDfn, String visitStr) throws Exception {
    setDefaultContext("OR CPRS GUI CHART");
    setDefaultRpcName("ORWPCE PCE4NOTE");
    if (ien != null) {
      return lCall(ien);
    } else {
      Object[] params = {"", patientDfn, visitStr};
      return lCall(params);
    }
  }   
  
  public String getTiuDetails(String ien) throws Exception {
    setDefaultContext("OR CPRS GUI CHART");
    setDefaultRpcName("TIU DETAILED DISPLAY");
    return sCall(ien);
  }
  
  public LockDocumentResult lockDocument(String ien) throws Exception {
    setDefaultContext("OR CPRS GUI CHART");
    setDefaultRpcName("TIU LOCK RECORD");
    LockDocumentResult lockDocumentResult = new LockDocumentResult();
    String x = sCall(ien);
    if (x.charAt(0) == '0') {
      lockDocumentResult.setSuccess(true);
      lockDocumentResult.setMessage("");
    } else {
      lockDocumentResult.setSuccess(false);
      lockDocumentResult.setMessage(StringUtils.piece(x, 2));      
    }
    return lockDocumentResult;
  }   
  
  public void unlockDocument(String ien) throws Exception {
    setDefaultContext("OR CPRS GUI CHART");
    setDefaultRpcName("TIU UNLOCK RECORD");
    sCall(ien);
  }     
  
  public boolean deleteDocument(String ien, String reason) throws Exception {
    String[] params = {String.valueOf(ien), reason};
    setDefaultContext("OR CPRS GUI CHART");
    setDefaultRpcName("TIU DELETE RECORD");
    String x = sCall(params);
    return StringUtils.piece(x, 1).equals("0");
  }
  
  public boolean justifyDocumentDelete(String ien) throws Exception {
    setDefaultContext("OR CPRS GUI CHART");
    setDefaultRpcName("TIU JUSTIFY DELETE?");
    String x = sCall(ien);
    return x.equals("1");
  }
  
}
