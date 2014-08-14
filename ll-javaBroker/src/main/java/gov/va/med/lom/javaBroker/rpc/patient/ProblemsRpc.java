package gov.va.med.lom.javaBroker.rpc.patient;

import java.util.ArrayList;
import java.util.Vector;
import java.text.ParseException;

import gov.va.med.lom.javaBroker.rpc.*;
import gov.va.med.lom.javaBroker.rpc.patient.models.*;
import gov.va.med.lom.javaBroker.util.DateUtils;
import gov.va.med.lom.javaBroker.util.FMDateUtils;
import gov.va.med.lom.javaBroker.util.StringUtils;

public class ProblemsRpc extends AbstractRpc {
  
  // PROBLEM STATUS
  public static final String ACTIVE   = "A";  // Active Problems
  public static final String INACTIVE = "I";  // Inactive Problems
  public static final String ALL      = "B";  // All Problems
  public static final String REMOVED  = "R";  // Removed Problems
    
  // FIELDS
  private ProblemsList problemsList;
  
  // CONSTRUCTORS
  public ProblemsRpc() throws BrokerException {
    super();
  }
  
  public ProblemsRpc(RpcBroker rpcBroker) throws BrokerException {
    super(rpcBroker);
  }
  
  // RPC API
  public synchronized ProblemsList getProblems(String dfn, String status) throws BrokerException {
    if (setContext("OR CPRS GUI CHART")) {
      setDfn(dfn);
      problemsList = new ProblemsList();
      problemsList.setStatus(status);
      if ((status == null) || 
          (status.length() == 0) && 
          ((!status.equals(ACTIVE)) ||
           (!status.equals(INACTIVE)) || 
           (!status.equals(ALL)) || 
           (!status.equals(REMOVED)))) {
        status = ACTIVE;
      }
      String[] params = {dfn, status};
      // IEN^Status^Description^Code^Onset Date^Last Updated^Service Connected Status^(detailRPC?)^Transcribed('T')^
      // Location IEN;Location^Location Type^Provider IEN;Provider^Service IEN;Service^prio('A')^Has Comments('1')^
      // (?)^Service-Connected Conditions^#=Inactive ICD Code Stored with Problem
      ArrayList list = lCall("ORQQPL PROBLEM LIST", params);
      if (returnRpcResult) {
        StringBuffer sb = new StringBuffer();
        for(int i = 0; i < list.size(); i++)
          sb.append((String)list.get(i) + "\n");
        problemsList.setRpcResult(sb.toString().trim());
      }     
      Vector problemsVect = new Vector(); 
      for(int i = 0; i < list.size(); i++) {
        String x = (String)list.get(i);
        if (StringUtils.piece(x,2).length() > 0) {
          if ((x.trim().length() > 0) && (StringUtils.piece(x, 1).length() > 0)) {
            Problem problem = new Problem();
            if (returnRpcResult)
              problem.setRpcResult(x);  
            problem.setDfn(dfn);
            problem.setIen(StringUtils.piece(x, 1));
            problem.setDescription(StringUtils.piece(x, 3));
            problem.setStatus(StringUtils.piece(x, 2));
            problem.setCode(StringUtils.piece(x, 4));
            try {
              problem.setOnsetDate(FMDateUtils.fmDateTimeToDate(StringUtils.piece(x, 5)));
              problem.setOnsetDateStr(DateUtils.toEnglishDate(problem.getOnsetDate()));
            } catch(ParseException pe) {}
            try {
              problem.setLastUpdated(FMDateUtils.fmDateTimeToDate(StringUtils.piece(x, 6)));
              problem.setLastUpdatedStr(DateUtils.toEnglishDate(problem.getLastUpdated()));
            } catch(ParseException pe) {}
            problem.setScStatus(StringUtils.piece(x, 7));
            problem.setScConditions(StringUtils.piece(x, 17));
            problem.setDetailRpc(StringUtils.piece(x, 8));
            problem.setTranscribed(StringUtils.strToBool(StringUtils.piece(x, 9), "T"));
            problem.setLocationIen(StringUtils.toLong(StringUtils.piece(StringUtils.piece(x, 10), ';', 1), 0));
            problem.setLocation(StringUtils.piece(StringUtils.piece(x, 10), ';', 2));
            problem.setLocationType(StringUtils.piece(x, 11));
            problem.setProviderIen(StringUtils.toLong(StringUtils.piece(StringUtils.piece(x, 12), ';', 1), 0));
            problem.setProvider(StringUtils.piece(StringUtils.piece(x, 12), ';', 2));        
            problem.setServiceIen(StringUtils.toLong(StringUtils.piece(StringUtils.piece(x, 13), ';', 1), 0));
            problem.setService(StringUtils.piece(StringUtils.piece(x, 13), ';', 2)); 
            String ver = null;
            String prio = null;
            char c = status.charAt(0);
            if (StringUtils.piece(x, 18).equals("#") && ((c == 'A') || (c == 'B') || (c == 'R'))) 
              ver = "#";
            else if (problem.getTranscribed())
              ver = "(u)";
            else
              ver = "   ";
            if (StringUtils.piece(x, 14).equals("A"))
              prio = " * ";
            else
              prio = "   ";        
             String version = problem.getStatus() + prio + ver;   
            problem.setVersion(version);
            boolean hasComments = StringUtils.strToBool(StringUtils.piece(x, 15), "1");
            if (hasComments) {
              ArrayList commentsList = lCall("ORQQPL PROB COMMENTS", problem.getIen());
              String[] comments = new String[commentsList.size()];
              for (int j = 0; j < commentsList.size(); j++) {
                String s = (String)commentsList.get(j);
                comments[j] = s;
              }
              problem.setComments(comments);
            }
            problemsVect.add(problem);
          }
        }
      }
      Problem[] problems = new Problem[problemsVect.size()];
      for (int i = 0; i < problems.length; i++)
        problems[i] = (Problem)problemsVect.get(i);
      problemsList.setProblems(problems);
      return problemsList;
    } else
      throw getCreateContextException("OR CPRS GUI CHART");
  } 
  
  public synchronized String getProblemDetail(String dfn, String ien) throws BrokerException {
    if (setContext("OR CPRS GUI CHART")) {     
      setDfn(dfn);
      Object[] params = {dfn, ien, ""};
      ArrayList list = lCall("ORQQPL DETAIL", params);
      StringBuffer sb = new StringBuffer();
      for(int i = 0; i < list.size(); i++)
        sb.append((String)list.get(i) + "\n");
      return sb.toString();
    } else
      throw getCreateContextException("OR CPRS GUI CHART");
  }
  
}
