package gov.va.med.lom.javaBroker.rpc.patient;

import java.util.ArrayList;
import java.util.Vector;
import java.util.Date;
import java.text.ParseException;

import gov.va.med.lom.javaBroker.rpc.*;
import gov.va.med.lom.javaBroker.rpc.patient.models.*;
import gov.va.med.lom.javaBroker.util.FMDateUtils;
import gov.va.med.lom.javaBroker.util.StringUtils;
import gov.va.med.lom.javaBroker.util.DateUtils;

public class LabsRpc extends AbstractRpc {
  
  // FIELDS
  private LabTestResultsList labTestResultsList;
    
  // CONSTRUCTORS
  public LabsRpc() throws BrokerException {
    super();
  }
  
  public LabsRpc(RpcBroker rpcBroker) throws BrokerException {
    super(rpcBroker);
  }
  
  // RPC API  
  public synchronized LabTestResultsList getRecentLabs(String dfn) throws BrokerException {
    if (setContext("OR CPRS GUI CHART")) {
      setDfn(dfn);
      labTestResultsList = new LabTestResultsList();
      ArrayList list = lCall("ORWCV LAB", dfn);
      if (returnRpcResult) {
        StringBuffer sb = new StringBuffer();
        for(int i = 0; i < list.size(); i++)
          sb.append((String)list.get(i) + "\n");
        labTestResultsList.setRpcResult(sb.toString().trim());
      }     
      Vector recentLabsVect = new Vector(); 
      for(int i = 0; i < list.size(); i++) {
        String x = (String)list.get(i);
        if ((x.trim().length() > 0)  && (StringUtils.piece(x, 1).length() > 0)) {
          String labIen = StringUtils.piece(StringUtils.piece(x, 1), ';', 1);
          Object[] params = {dfn, labIen};
          String y = sCall("ORQQLR DETAIL", params);
          LabTestResult recentLab = new LabTestResult();
          if (returnRpcResult)
            recentLab.setRpcResult(x);       
          recentLab.setDfn(dfn);
          recentLab.setIen(labIen);
          recentLab.setName(StringUtils.mixedCase(StringUtils.piece(x, 2)));
          recentLab.setDate(FMDateUtils.fmDateTimeToDate(StringUtils.piece(x, 3)));
          try {
            recentLab.setDateStr(DateUtils.toEnglishDate(recentLab.getDate()));
          } catch(ParseException pe) {}
          recentLab.setStatus(StringUtils.mixedCase(StringUtils.piece(x, 4)));
          recentLab.setResult(StringUtils.mixedCase(y));
          recentLabsVect.add(recentLab);
        }
      }
      LabTestResult[] recentLabs = new LabTestResult[recentLabsVect.size()];
      for(int i = 0; i < recentLabs.length; i++)
        recentLabs[i] = (LabTestResult)recentLabsVect.get(i);
      labTestResultsList.setLabTestResults(recentLabs);
      return labTestResultsList;
    } else
      throw getCreateContextException("OR CPRS GUI CHART");
  } 
  
  public synchronized String[] getLabTestData(String labTestIen) throws BrokerException {
    if (setContext("OR CPRS GUI CHART")) {
      ArrayList list = lCall("ORWDLR32 LOAD", labTestIen);
      String[] results = new String[list.size()];
      for (int i = 0; i < results.length; i++)
        results[i] = (String)list.get(i);
      return results;
    } else
      throw getCreateContextException("OR CPRS GUI CHART");
  }
  
  public synchronized CumulativeLabResults getCumulativeLabResults(String patientDfn, int daysBack, Date date1, Date date2) throws BrokerException {
    if (setContext("OR CPRS GUI CHART")) {
      double fmDate1 = 0;
      double fmDate2 = 0;
      if (date1 == null)
        fmDate1 = MiscRPCs.fmNow(rpcBroker);
      else
        fmDate1 = FMDateUtils.dateToFMDate(date1);
      fmDate1 +=  0.2359;
      if (date2 == null)
        fmDate2 = FMDateUtils.dateToFMDate(DateUtils.subtractDaysFromDate(new Date(), daysBack));   
      else
        fmDate2 = FMDateUtils.dateToFMDate(date2);
      Object[] params = {patientDfn, String.valueOf(daysBack), String.valueOf(fmDate1), String.valueOf(fmDate2)};
      ArrayList list = lCall("ORWLR CUMULATIVE REPORT", params);
      CumulativeLabResults cumulativeLabResults = new CumulativeLabResults();
      Vector v = new Vector();
      int index = 0;
      for(; index < list.size(); index++) {
        String x = (String)list.get(index); 
        if (!x.startsWith("[HIDDEN TEXT]") && !x.startsWith("[REPORT TEXT]"))
          v.add(x);
        else if (x.startsWith("[REPORT TEXT]"))
          break;
      }
      LabResultTestType[] labResultTestTypes = new LabResultTestType[v.size()];
      for (int i = 0; i < v.size(); i++) {
        String y = (String)v.get(i);
        labResultTestTypes[i] = new LabResultTestType();
        labResultTestTypes[i].setStart(StringUtils.toInt(StringUtils.piece(y, 1), 0));
        labResultTestTypes[i].setName(StringUtils.piece(y, 2));
        if (returnRpcResult)
          labResultTestTypes[i].setRpcResult(y);
      }
      cumulativeLabResults.setLabResultTestTypes(labResultTestTypes);
      StringBuffer results = new StringBuffer();
      for(int i = index+1; i < list.size(); i++) {
        String x = (String)list.get(i); 
        results.append(x + "\n");
      }
      cumulativeLabResults.setText(results.toString());
      return cumulativeLabResults;
    } else
      throw getCreateContextException("OR CPRS GUI CHART");
  }
  
  public synchronized String getTestsByDate(String patientDfn, int daysBack, Date date1, Date date2) throws BrokerException {
    if (setContext("OR CPRS GUI CHART")) {
      double fmDate1 = 0;
      double fmDate2 = 0;
      if (date1 == null)
        fmDate1 = MiscRPCs.fmNow(rpcBroker);
      else
        fmDate1 = FMDateUtils.dateToFMDate(date1);
      fmDate1 +=  0.2359;
      if (date2 == null)
        fmDate2 = FMDateUtils.dateToFMDate(DateUtils.subtractDaysFromDate(new Date(), daysBack));   
      else
        fmDate2 = FMDateUtils.dateToFMDate(date2);
      Object[] params = {patientDfn, String.valueOf(fmDate1), String.valueOf(fmDate2)};
      StringBuffer sb = new StringBuffer();
      ArrayList<String> list = lCall("ORWLRR INTERIM", params);
      for(Object s : list) {
        sb.append(s);
        sb.append("\n");
      }
      return sb.toString();
    } else
      throw getCreateContextException("OR CPRS GUI CHART");
  }
}