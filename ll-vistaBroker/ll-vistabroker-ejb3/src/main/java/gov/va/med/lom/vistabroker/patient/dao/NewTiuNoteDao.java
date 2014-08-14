package gov.va.med.lom.vistabroker.patient.dao;

import gov.va.med.lom.javaUtils.misc.DateUtils;
import gov.va.med.lom.javaUtils.misc.StringUtils;
import gov.va.med.lom.vistabroker.dao.BaseDao;
import gov.va.med.lom.vistabroker.patient.data.EditedTiuNote;
import gov.va.med.lom.vistabroker.patient.data.NewTiuNote;
import gov.va.med.lom.vistabroker.patient.data.TiuNote;
import gov.va.med.lom.vistabroker.util.FMDateUtils;
import gov.va.med.vistalink.rpc.RpcRequest;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;

public class NewTiuNoteDao extends BaseDao {
  
  // CONSTRUCTORS
  public NewTiuNoteDao() {
    super();
  }
  
  public NewTiuNoteDao(BaseDao baseDao) {
    super(baseDao);
  }
  
  // RPC API 
  /*
  TTILE = 1 for DC summaries, 652 for nursing DC notes
  VDT = Encounter Date/Time (FM date format)
  VLOC = Encounter location IEN
  MULT = TIUX
    '.11'  = 1 if CPT required (def. = 0)
    '1202' = Author
    '1301' = Reference Date (def. = NOW in FM date format)
    '1307' = Dictation Date (def. = NOW in FM date format)
    '1205' = Encounter location IEN
    '1208' = Cosigner (attending) IEN
    '1701' = Subject (text description of note)
    '2101' = ID of parent note (def. = '')
    '1405' = Existing package reference (def. = '')
  VSTR = Visit string (visit location;date/time;service category) (def. = '')
  SUPPRESS = 1 if suppress commit logic (def. = 1)
 */  
  
  public TiuNote createTiuNote(NewTiuNote newTiuNote) throws Exception {
    setDefaultContext("OR CPRS GUI CHART");
    setDefaultRpcName("TIU CREATE RECORD");
    HashMap<String, String> map = new HashMap<String, String>();
    map.put(".11", StringUtils.boolToStr(newTiuNote.getCpt(), "1", "0"));
    map.put("1202", newTiuNote.getAuthorDuz());
    if ((newTiuNote != null) && (newTiuNote.getRefDate() != null) && (newTiuNote.getRefDate().length() > 0)) {
      try {
        map.put("1301", String.valueOf(FMDateUtils.ansiDateTimeToFMDateTime(newTiuNote.getRefDate())));
      } catch (ParseException pe) {
        try {
          map.put("1301", String.valueOf(FMDateUtils.ansiDateTimeToFMDateTime(DateUtils.getCurrentDateTime())));
        } catch (ParseException pe2) {}
      }
    }
    if (newTiuNote.getDictDate() != null) {
      try {
        map.put("1307", String.valueOf(FMDateUtils.ansiDateTimeToFMDateTime(newTiuNote.getDictDate())));
      } catch (ParseException pe) {
        try {
          map.put("1307", String.valueOf(FMDateUtils.ansiDateTimeToFMDateTime(DateUtils.getCurrentDateTime())));
        } catch (ParseException pe2) {}
      }
    }
    String visitDate = null;
    if (newTiuNote.getVisitDate() != null) {
      try {
        visitDate = String.valueOf(FMDateUtils.ansiDateTimeToFMDateTime(newTiuNote.getVisitDate()));
      } catch (ParseException pe) {
        visitDate = "";
      }
    }
    String visitStr = null;
    if (newTiuNote.getVisitStr() != null)
      visitStr = newTiuNote.getVisitStr();
    else
      visitStr = "";
    if (!newTiuNote.getVisitLocationIen().equals("0"))
      map.put("1205", String.valueOf(newTiuNote.getVisitLocationIen())); 
    if ((newTiuNote.getCosignerDuz() != null) && !newTiuNote.getCosignerDuz().equals("0"))
        map.put("1208", String.valueOf(newTiuNote.getCosignerDuz()));  
    if (newTiuNote.getSubject() != null)
      map.put("1701", newTiuNote.getSubject());  
    if ((newTiuNote.getParentNoteIen() != null) && !newTiuNote.getParentNoteIen().equals("0"))
        map.put("2101", String.valueOf(newTiuNote.getParentNoteIen()));
    if (newTiuNote.getPackageRef() != null)
      map.put("1405", newTiuNote.getPackageRef()); 
    ArrayList<String> list = StringUtils.getArrayList(newTiuNote.getText());
    for (int i = 0; i < list.size(); i++)
      map.put(RpcRequest.buildMultipleMSubscriptKey("\"TEXT\"," + String.valueOf(i+1) + ",0"), (String)list.get(i));
    Object[] params = {String.valueOf(newTiuNote.getDfn()), String.valueOf(newTiuNote.getTitleIen()),
                       String.valueOf(visitDate), String.valueOf(newTiuNote.getVisitLocationIen()), "", 
                       map, visitStr, StringUtils.boolToStr(newTiuNote.getSuppress(), "1", "0")};
    String x = sCall(params);
    String ien = StringUtils.piece(x, 1);
    TiuNote tiuNote = new TiuNote();   
    tiuNote.setIen(ien);
    tiuNote.setDfn(newTiuNote.getDfn());
    tiuNote.setText(newTiuNote.getText());
    if (ien.equals("0"))
      tiuNote.setMessage(StringUtils.piece(x, 2));
    return tiuNote;
  }
  
  public TiuNote updateTiuNote(EditedTiuNote editedTiuNote) throws Exception {
    setDefaultContext("OR CPRS GUI CHART");
    setDefaultRpcName("TIU UPDATE RECORD");
    HashMap<String, String> map = new HashMap<String, String>();
    if ((editedTiuNote.getAddend() == 0) && 
        (editedTiuNote.getTitleIen() != null) &&
        (editedTiuNote.getTitleIen().length() > 0) &&
        (!editedTiuNote.getTitleIen().equals("0")))
      map.put(".01", String.valueOf(editedTiuNote.getTitleIen()));
    if ((editedTiuNote.getAuthorDuz() != null) && 
        (editedTiuNote.getAuthorDuz().length() > 0) &&
        (!editedTiuNote.getAuthorDuz().equals("0")))
      map.put("1202", String.valueOf(editedTiuNote.getAuthorDuz()));
    if ((editedTiuNote.getCosignerDuz() != null) &&
        (editedTiuNote.getCosignerDuz().length() > 0) &&
        (!editedTiuNote.getCosignerDuz().equals("0")))
      map.put("1208", String.valueOf(editedTiuNote.getCosignerDuz()));
    if ((editedTiuNote.getPackageRef() != null) && 
        (editedTiuNote.getPackageRef().length() > 0))
      map.put("1405", editedTiuNote.getPackageRef()); 
    if (editedTiuNote.getFmDateTime() > 0)
      map.put("1301", String.valueOf(editedTiuNote.getFmDateTime()));
    if ((editedTiuNote.getSubject() != null) && 
        (editedTiuNote.getSubject().length() > 0))
      map.put("1701", editedTiuNote.getSubject()); 
    if (editedTiuNote.getClinicalProcedureSummaryCode() > 0)
      map.put("70201", String.valueOf(editedTiuNote.getClinicalProcedureSummaryCode()));      
    if (editedTiuNote.getClinicalProcedureFMDateTime() > 0)
      map.put("70202", String.valueOf(editedTiuNote.getClinicalProcedureFMDateTime()));
    ArrayList<String> list = StringUtils.getArrayList(editedTiuNote.getText());
    for (int i = 0; i < list.size(); i++)
      map.put(RpcRequest.buildMultipleMSubscriptKey("\"TEXT\"," + String.valueOf(i+1) + ",0"), (String)list.get(i));
    Object[] params = {editedTiuNote.getIen(), map};
    String x = sCall(params);
    String ien = StringUtils.piece(x, 1);
    TiuNote tiuNote = new TiuNote();
    tiuNote.setIen(ien);
    tiuNote.setDfn(editedTiuNote.getDfn());
    tiuNote.setText(editedTiuNote.getText());
    if (ien.equals("0"))
      tiuNote.setMessage(StringUtils.piece(x, 2));
    return tiuNote;
  }
  
  public TiuNote createAddendum(NewTiuNote newTiuNote, String addendumTo) throws Exception {
    setDefaultContext("OR CPRS GUI CHART");
    setDefaultRpcName("TIU CREATE ADDENDUM RECORD");
    HashMap<String, String> map = new HashMap<String, String>();
    map.put("1202", String.valueOf(newTiuNote.getAuthorDuz()));
    if (newTiuNote.getRefDate().length() > 0) {
      try {
        map.put("1301", String.valueOf(FMDateUtils.ansiDateTimeToFMDateTime(newTiuNote.getRefDate())));
      } catch (ParseException pe) {}
    }
    if (newTiuNote.getCosignerDuz() != null)
      map.put("1208", String.valueOf(newTiuNote.getCosignerDuz()));  
    ArrayList<String> list = StringUtils.getArrayList(newTiuNote.getText());
    for (int i = 0; i < list.size(); i++)
      map.put(RpcRequest.buildMultipleMSubscriptKey("\"TEXT\"," + String.valueOf(i+1) + ",0"), (String)list.get(i));
    Object[] params = {String.valueOf(addendumTo), map};
    String x = sCall(params);
    String ien = StringUtils.piece(x, 1);
    TiuNote tiuNote = new TiuNote();   
    tiuNote.setIen(ien);
    tiuNote.setDfn(newTiuNote.getDfn());
    tiuNote.setText(newTiuNote.getText());
    if (ien.equals("0"))
      tiuNote.setMessage(StringUtils.piece(x, 2));
    return tiuNote;
  }

}
