package gov.va.med.lom.vistabroker.patient.dao;

import gov.va.med.lom.javaUtils.misc.DateUtils;
import gov.va.med.lom.javaUtils.misc.StringUtils;
import gov.va.med.lom.vistabroker.dao.BaseDao;
import gov.va.med.lom.vistabroker.patient.data.Problem;
import gov.va.med.lom.vistabroker.util.FMDateUtils;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class ProblemsDao extends BaseDao {
  
  // PROBLEM STATUS
  public static final String ACTIVE   = "A";  // Active Problems
  public static final String INACTIVE = "I";  // Inactive Problems
  public static final String ALL      = "B";  // All Problems
  public static final String REMOVED  = "R";  // Removed Problems
    
  // CONSTRUCTORS
  public ProblemsDao() {
    super();
  }
  
  public ProblemsDao(BaseDao baseDao) {
    super(baseDao);
  }
  
  // RPC API
  public List<Problem> getProblems(String dfn, String status) throws Exception {
    setDefaultContext("OR CPRS GUI CHART");
    setDefaultRpcName("ORQQPL PROBLEM LIST");
    if ((status == null) || 
        (status.length() == 0) && 
        ((!status.equals(ACTIVE)) ||
         (!status.equals(INACTIVE)) || 
         (!status.equals(ALL)) || 
         (!status.equals(REMOVED)))) {
      status = ACTIVE;
    }
    String[] params = {String.valueOf(dfn), status};
    // IEN^Status^Description^Code^Onset Date^Last Updated^Service Connected Status^(detailRPC?)^Transcribed('T')^
    // Location IEN;Location^Location Type^Provider IEN;Provider^Service IEN;Service^prio('A')^Has Comments('1')^
    // (?)^Service-Connected Conditions^#=Inactive ICD Code Stored with Problem
    List<String> list = lCall(params);
    List<Problem> problems = new ArrayList<Problem>();
    for (String s : list) {
      if (StringUtils.piece(s,2).length() > 0) {
        if ((s.trim().length() > 0) && (StringUtils.piece(s, 1).length() > 0)) {
          Problem problem = new Problem();
          problem.setDfn(dfn);
          problem.setIen(StringUtils.piece(s, 1));
          problem.setDescription(StringUtils.piece(s, 3));
          problem.setStatus(StringUtils.piece(s, 2));
          problem.setCode(StringUtils.piece(s, 4));
          try {
            problem.setOnsetDate(FMDateUtils.fmDateTimeToDate(StringUtils.piece(s, 5)));
            problem.setOnsetDateStr(DateUtils.toEnglishDate(problem.getOnsetDate()));
          } catch(ParseException pe) {}
          try {
            problem.setLastUpdated(FMDateUtils.fmDateTimeToDate(StringUtils.piece(s, 6)));
            problem.setLastUpdatedStr(DateUtils.toEnglishDate(problem.getLastUpdated()));
          } catch(ParseException pe) {}
          problem.setScStatus(StringUtils.piece(s, 7));
          problem.setScConditions(StringUtils.piece(s, 17));
          problem.setDetailRpc(StringUtils.piece(s, 8));
          problem.setTranscribed(StringUtils.strToBool(StringUtils.piece(s, 9), "T"));
          problem.setLocationIen(StringUtils.toLong(StringUtils.piece(StringUtils.piece(s, 10), ';', 1), 0));
          problem.setLocation(StringUtils.piece(StringUtils.piece(s, 10), ';', 2));
          problem.setLocationType(StringUtils.piece(s, 11));
          problem.setProviderIen(StringUtils.piece(StringUtils.piece(s, 12), ';', 1));
          problem.setProvider(StringUtils.piece(StringUtils.piece(s, 12), ';', 2));        
          problem.setServiceIen(StringUtils.piece(StringUtils.piece(s, 13), ';', 1));
          problem.setService(StringUtils.piece(StringUtils.piece(s, 13), ';', 2)); 
          String ver = null;
          String prio = null;
          char c = status.charAt(0);
          if (StringUtils.piece(s, 18).equals("#") && ((c == 'A') || (c == 'B') || (c == 'R'))) 
            ver = "#";
          else if (problem.getTranscribed())
            ver = "(u)";
          else
            ver = "   ";
          if (StringUtils.piece(s, 14).equals("A"))
            prio = " * ";
          else
            prio = "   ";        
           String version = problem.getStatus() + prio + ver;   
          problem.setVersion(version);
          boolean hasComments = StringUtils.strToBool(StringUtils.piece(s, 15), "1");
          if (hasComments) {
            setDefaultRpcName("ORQQPL PROB COMMENTS");
            List<String> comments = lCall(problem.getIen());
            problem.setComments(comments);
          }
          problems.add(problem);
        }
      }
    }
    return problems;
  } 
  
  public String getProblemDetail(String dfn, String ien) throws Exception {
    setDefaultContext("OR CPRS GUI CHART"); 
    setDefaultRpcName("ORQQPL DETAIL");
    Object[] params = {dfn, ien, ""};
    List<String> list = lCall(params);
    StringBuffer sb = new StringBuffer();
    for(String s : list)
      sb.append(s + "\n");
    return sb.toString().trim();
  }
  
}
