package gov.va.med.lom.javaBroker.rpc.patient;

import java.util.ArrayList;
import java.util.Vector;
import java.text.ParseException;

import gov.va.med.lom.javaBroker.rpc.*;
import gov.va.med.lom.javaBroker.rpc.patient.models.*;
import gov.va.med.lom.javaBroker.util.FMDateUtils;
import gov.va.med.lom.javaBroker.util.StringUtils;
import gov.va.med.lom.javaBroker.util.DateUtils;

public class PostingsRpc extends AbstractRpc {
  
  // FIELDS
  private PostingsList postingsList;
  
  // CONSTRUCTORS
  public PostingsRpc() throws BrokerException {
    super();
  }
  
  public PostingsRpc(RpcBroker rpcBroker) throws BrokerException {
    super(rpcBroker);
  }
  
  // RPC API
  public synchronized PostingsList getPostings(String dfn) throws BrokerException {
    if (setContext("OR CPRS GUI CHART")) {
      setDfn(dfn);
      postingsList = new PostingsList();
      ArrayList list = lCall("ORQQPP LIST", dfn);
      if (returnRpcResult) {
        StringBuffer sb = new StringBuffer();
        for(int i = 0; i < list.size(); i++)
          sb.append((String)list.get(i) + "\n");
        postingsList.setRpcResult(sb.toString().trim());
      }    
      // Patient Posting IEN^acronym^category^modifier^date/time
      Vector postingsVect = new Vector(); 
      for(int i = 0; i < list.size(); i++) {
        String x = (String)list.get(i);
        if ((x.trim().length() > 0)) {
          Posting posting = new Posting();
          if (returnRpcResult)
            posting.setRpcResult(x);
          posting.setDfn(dfn);
          posting.setIen(StringUtils.piece(x, 1));
          posting.setName(StringUtils.mixedCase(StringUtils.piece(x, 2)));
          posting.setDate(FMDateUtils.fmDateTimeToDate(StringUtils.piece(x, 3)));
          try {
            posting.setDateStr(DateUtils.toEnglishDate(posting.getDate()));
          } catch(ParseException pe) {}
          postingsVect.add(posting);
        }
      }
      Posting[] postings = new Posting[postingsVect.size()];
      for(int i = 0; i < postings.length; i++)
        postings[i] = (Posting)postingsVect.get(i);
      postingsList.setPostings(postings);
      return postingsList;
    } else
      throw getCreateContextException("OR CPRS GUI CHART");
  } 
  
  public synchronized String getDetailPosting(String dfn, String id) throws BrokerException {
    if (setContext("OR CPRS GUI CHART")) {
      setDfn(dfn);
      if ((id.length() == 0) || id.equals("A")) {
        ArrayList list = lCall("ORQQAL LIST REPORT", dfn);
        StringBuffer sb = new StringBuffer();
        for(int i = 0; i < list.size(); i++)
          sb.append((String)list.get(i) + "\n");
        return sb.toString();
      } else {
        TiuNoteRpc tiuNoteRpc = new TiuNoteRpc(getRpcBroker());
        TiuNote tiuNote = tiuNoteRpc.getTiuNote(id);
        return tiuNote.getText();
      }
    } else
      throw getCreateContextException("OR CPRS GUI CHART");
  }   
  
}
