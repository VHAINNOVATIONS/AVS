package gov.va.med.lom.javaBroker.rpc.patient;

import java.text.ParseException;
import java.util.ArrayList;

import gov.va.med.lom.javaBroker.rpc.*;
import gov.va.med.lom.javaBroker.rpc.patient.models.*;
import gov.va.med.lom.javaBroker.util.DateUtils;
import gov.va.med.lom.javaBroker.util.FMDateUtils;
import gov.va.med.lom.javaBroker.util.StringUtils;

public class NewTiuNoteRpc extends AbstractRpc {
  
  // FIELDS
  private TiuNote tiuNote;
  
  // CONSTRUCTORS
  public NewTiuNoteRpc() throws BrokerException {
    super();
  }
  
  public NewTiuNoteRpc(RpcBroker rpcBroker) throws BrokerException {
    super(rpcBroker);
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
  
  public synchronized TiuNote createTiuNote(NewTiuNote newTiuNote) throws BrokerException {
    if (setContext("OR CPRS GUI CHART")) {
      setDfn(newTiuNote.getDfn());
      tiuNote = new TiuNote();    
      Mult mult = new Mult();
      mult.setMultiple(".11", StringUtils.boolToStr(newTiuNote.getCpt(), "1", "0"));
      mult.setMultiple("1202", newTiuNote.getAuthorDuz());
      if ((newTiuNote != null) && (newTiuNote.getRefDate().length() > 0)) {
        try {
          mult.setMultiple("1301", String.valueOf(FMDateUtils.ansiDateTimeToFMDateTime(newTiuNote.getRefDate())));
        } catch (ParseException pe) {
          try {
            mult.setMultiple("1301", String.valueOf(FMDateUtils.ansiDateTimeToFMDateTime(DateUtils.getCurrentDateTime())));
          } catch (ParseException pe2) {}
        }
      }
      if (newTiuNote.getDictDate() != null) {
        try {
          mult.setMultiple("1307", String.valueOf(FMDateUtils.ansiDateTimeToFMDateTime(newTiuNote.getDictDate())));
        } catch (ParseException pe) {
          try {
            mult.setMultiple("1307", String.valueOf(FMDateUtils.ansiDateTimeToFMDateTime(DateUtils.getCurrentDateTime())));
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
      if (newTiuNote.getVisitLocationIen() != null)
        mult.setMultiple("1205", newTiuNote.getVisitLocationIen()); 
      if (newTiuNote.getCosignerDuz() != null)
        mult.setMultiple("1208", newTiuNote.getCosignerDuz());  
      if (newTiuNote.getSubject() != null)
        mult.setMultiple("1701", newTiuNote.getSubject());  
      if (newTiuNote.getParentNoteIen() != null)
        mult.setMultiple("2101", newTiuNote.getParentNoteIen());  
      mult.setMultiple("1405", newTiuNote.getPackageRef()); 
      ArrayList list = StringUtils.getArrayList(newTiuNote.getText());
      for (int i = 0; i < list.size(); i++)
        mult.setMultiple("\"TEXT\"," + String.valueOf(i+1) + ",0", (String)list.get(i));
      Object[] params = {newTiuNote.getDfn(), newTiuNote.getTitleIen(),
                         String.valueOf(visitDate), newTiuNote.getVisitLocationIen(), "", 
                         mult, newTiuNote.getVisitStr(),
                         StringUtils.boolToStr(newTiuNote.getSuppress(), "1", "0")};
      String x = sCall("TIU CREATE RECORD", params);
      String ien = StringUtils.piece(x, 1);
      tiuNote.setIen(ien);
      tiuNote.setDfn(getDfn());
      tiuNote.setText(newTiuNote.getText());
      if (ien == null)
        tiuNote.setMessage(StringUtils.piece(x, 2));
      return tiuNote;
    } else
      throw getCreateContextException("OR CPRS GUI CHART");
  }
  
  public synchronized TiuNote updateTiuNote(EditedTiuNote editedTiuNote) throws BrokerException {
    if (setContext("OR CPRS GUI CHART")) {
      tiuNote = new TiuNote();
      Mult mult = new Mult();
      if ((editedTiuNote.getAddend() == 0) && (editedTiuNote.getTitleIen() != null))
        mult.setMultiple(".01", editedTiuNote.getTitleIen());
      if (editedTiuNote.getAuthorDuz() != null)
        mult.setMultiple("1202", editedTiuNote.getAuthorDuz());
      if (editedTiuNote.getCosignerDuz() != null)
        mult.setMultiple("1208", editedTiuNote.getCosignerDuz());
      if ((editedTiuNote.getPackageRef() != null) && (editedTiuNote.getPackageRef().length() > 0))
        mult.setMultiple("1405", editedTiuNote.getPackageRef()); 
      if (editedTiuNote.getFmDateTime() > 0)
        mult.setMultiple("1301", String.valueOf(editedTiuNote.getFmDateTime()));
      if ((editedTiuNote.getSubject() != null) && (editedTiuNote.getSubject().length() > 0))
      mult.setMultiple("1701", editedTiuNote.getSubject()); 
      if (editedTiuNote.getClinicalProcedureSummaryCode() > 0)
        mult.setMultiple("70201", String.valueOf(editedTiuNote.getClinicalProcedureSummaryCode()));      
      if (editedTiuNote.getClinicalProcedureFMDateTime() > 0)
        mult.setMultiple("70202", String.valueOf(editedTiuNote.getClinicalProcedureFMDateTime()));
      ArrayList list = StringUtils.getArrayList(editedTiuNote.getText());
      for (int i = 0; i < list.size(); i++)
        mult.setMultiple("\"TEXT\"," + String.valueOf(i+1) + ",0", (String)list.get(i));
      Object[] params = {editedTiuNote.getIen(), mult};
      String x = sCall("TIU UPDATE RECORD", params);
      String ien = StringUtils.piece(x, 1);      
      tiuNote.setIen(ien);
      tiuNote.setDfn(getDfn());
      tiuNote.setText(editedTiuNote.getText());
      if (ien == null)
        tiuNote.setMessage(StringUtils.piece(x, 2));
      return tiuNote;
    } else
      throw getCreateContextException("OR CPRS GUI CHART");
  }
  
  public synchronized TiuNote createAddendum(NewTiuNote newTiuNote, String addendumTo) throws BrokerException {
    if (setContext("OR CPRS GUI CHART")) {
      setDfn(newTiuNote.getDfn());
      tiuNote = new TiuNote();    
      Mult mult = new Mult();
      mult.setMultiple("1202", newTiuNote.getAuthorDuz());
      if (newTiuNote.getRefDate().length() > 0) {
        try {
          mult.setMultiple("1301", String.valueOf(FMDateUtils.ansiDateTimeToFMDateTime(newTiuNote.getRefDate())));
        } catch (ParseException pe) {}
      }
      if (newTiuNote.getCosignerDuz() != null)
        mult.setMultiple("1208", newTiuNote.getCosignerDuz());  
      ArrayList list = StringUtils.getArrayList(newTiuNote.getText());
      for (int i = 0; i < list.size(); i++)
        mult.setMultiple("\"TEXT\"," + String.valueOf(i+1) + ",0", (String)list.get(i));
      Object[] params = {addendumTo, mult};
      String x = sCall("TIU CREATE ADDENDUM RECORD", params);
      String ien = StringUtils.piece(x, 1);
      tiuNote.setIen(ien);
      tiuNote.setDfn(getDfn());
      tiuNote.setText(newTiuNote.getText());
      if (ien == null)
        tiuNote.setMessage(StringUtils.piece(x, 2));
      return tiuNote;
    } else
      throw getCreateContextException("OR CPRS GUI CHART");
  }

}
