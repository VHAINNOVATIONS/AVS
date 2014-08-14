package gov.va.med.lom.vistabroker.patient.dao;

import gov.va.med.lom.javaUtils.misc.DateUtils;
import gov.va.med.lom.javaUtils.misc.StringUtils;
import gov.va.med.lom.vistabroker.dao.BaseDao;
import gov.va.med.lom.vistabroker.patient.data.Posting;
import gov.va.med.lom.vistabroker.patient.data.TiuNote;
import gov.va.med.lom.vistabroker.util.FMDateUtils;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class PostingsDao extends BaseDao {
  
  // CONSTRUCTORS
  public PostingsDao() {
    super();
  }
  
  public PostingsDao(BaseDao baseDao) {
    super(baseDao);
  }
  
  // RPC API
  public List<Posting> getPostings(String dfn) throws Exception {
    setDefaultContext("OR CPRS GUI CHART");
    setDefaultRpcName("ORQQPP LIST");
    List<String> list = lCall(dfn);
    // Patient Posting IEN^acronym^category^modifier^date/time
    List<Posting> postings = new ArrayList<Posting>();
    for(String s : list) {
      if ((s.trim().length() > 0)) {
        Posting posting = new Posting();
        posting.setDfn(dfn);
        posting.setIen(StringUtils.piece(s, 1));
        posting.setName(StringUtils.mixedCase(StringUtils.piece(s, 2)));
        posting.setDate(FMDateUtils.fmDateTimeToDate(StringUtils.piece(s, 3)));
        try {
          posting.setDateStr(DateUtils.toEnglishDate(posting.getDate()));
        } catch(ParseException pe) {}
        postings.add(posting);
      }
    }
    return postings;
  } 
  
  public String getDetailPosting(String dfn, String id) throws Exception {
    setDefaultContext("OR CPRS GUI CHART");
    setDefaultRpcName("ORQQAL LIST REPORT");
    if ((id.length() == 0) || id.equals("A")) {
      List<String> list = lCall(dfn);
      StringBuffer sb = new StringBuffer();
      for(String s : list)
        sb.append(s + "\n");
      return sb.toString().trim();
    } else {
      TiuNoteDao tiuNoteRpc = new TiuNoteDao(this);
      TiuNote tiuNote = tiuNoteRpc.getTiuNote(id);
      return tiuNote.getText();
    }
  }   
  
}
