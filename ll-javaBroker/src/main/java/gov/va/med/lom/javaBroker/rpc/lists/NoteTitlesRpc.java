package gov.va.med.lom.javaBroker.rpc.lists;

import java.util.ArrayList;

import gov.va.med.lom.javaBroker.rpc.*;
import gov.va.med.lom.javaBroker.rpc.lists.models.*;
import gov.va.med.lom.javaBroker.util.StringUtils;

public class NoteTitlesRpc extends AbstractRpc {
	
  // Note Classes
  public static final int CLASS_PROGRESS_NOTES = 3;
  public static final int CLASS_DISCHARGE_SUMMARIES = 244;  

  // CONSTRUCTORS
  public NoteTitlesRpc() throws BrokerException {
    super();
  }
  
  public NoteTitlesRpc(RpcBroker rpcBroker) throws BrokerException {
    super(rpcBroker);
  }
  
  // RPC API
  public synchronized NoteTitlesList getTitlesForClass(long classIen, String startFrom, int direction) throws BrokerException {
    if (setContext("OR CPRS GUI CHART")) {
      Object[] params = {classIen, startFrom, String.valueOf(direction)};
      ArrayList list = lCall("TIU LONG LIST OF TITLES", params);
      NoteTitle[] noteTitles = new NoteTitle[list.size()];
      for(int i = 0; i < list.size(); i++) {
        String x = (String)list.get(i);  
        noteTitles[i] = new NoteTitle();
        noteTitles[i].setIen(StringUtils.piece(x, 1));
        noteTitles[i].setTitle(StringUtils.piece(x, 2));
        if (returnRpcResult)
          noteTitles[i].setRpcResult(x);
      }
      NoteTitlesList noteTitleList = new NoteTitlesList();
      noteTitleList.setNoteTitles(noteTitles);
      return noteTitleList;
    } else 
      throw getCreateContextException("OR CPRS GUI CHART");
  }
  
  public synchronized NoteTitlesList subsetOfProgressNoteTitles(String startFrom, int direction) throws BrokerException {
    return getTitlesForClass(CLASS_PROGRESS_NOTES, startFrom, direction);
  }
  
  public synchronized NoteTitlesList subSetOfDCSummaryTitles(String startFrom, int direction) throws BrokerException {
    return getTitlesForClass(CLASS_DISCHARGE_SUMMARIES, startFrom, direction);
  }    
  
  /**
   * Returns a pointer to a list of clinical procedures titles (for use in a long list box).
   * @param startFrom
   * @param direction
   * @param idNoteTitlesOnly
   * @return
   * @throws BrokerException
   */
  public synchronized NoteTitlesList subSetOfClinProcTitles(String startFrom, int direction, boolean idNoteTitlesOnly) throws BrokerException {
    if (setContext("OR CPRS GUI CHART")) {
      Object[] params = {startFrom, String.valueOf(direction), StringUtils.boolToStr(idNoteTitlesOnly, "true", "false")};
      ArrayList list = lCall("TIU LONG LIST CLINPROC TITLES", params);
      NoteTitle[] noteTitles = new NoteTitle[list.size()];
      for(int i = 0; i < list.size(); i++) {
        String x = (String)list.get(i);      
        noteTitles[i] = new NoteTitle();
        noteTitles[i].setIen(StringUtils.piece(x, 1));
        noteTitles[i].setTitle(StringUtils.piece(x, 2));
        if (returnRpcResult)
          noteTitles[i].setRpcResult(x);
      }
      NoteTitlesList noteTitleList = new NoteTitlesList();
      noteTitleList.setNoteTitles(noteTitles);
      return noteTitleList;
    } else 
      throw getCreateContextException("OR CPRS GUI CHART");
  }
  
}
