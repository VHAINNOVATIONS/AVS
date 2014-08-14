package gov.va.med.lom.javaBroker.rpc.patient;

import java.util.ArrayList;
import java.util.Vector;
import java.text.ParseException;

import gov.va.med.lom.javaBroker.rpc.*;
import gov.va.med.lom.javaBroker.rpc.patient.models.*;
import gov.va.med.lom.javaBroker.util.FMDateUtils;
import gov.va.med.lom.javaBroker.util.StringUtils;
import gov.va.med.lom.javaBroker.util.DateUtils;

public class AdmissionsRpc extends AbstractRpc {
  
  // FIELDS
  private AdmissionsList admissionsList;
  
  // CONSTRUCTORS
  public AdmissionsRpc() {
    super();
  }
  
  public AdmissionsRpc(RpcBroker rpcBroker) throws BrokerException {
    super(rpcBroker);
  }
  
  // RPC API
  public synchronized AdmissionsList getAdmissions(String dfn) throws BrokerException {
    if (setContext("OR CPRS GUI CHART")) {
      setDfn(dfn);
      admissionsList = new AdmissionsList();
      ArrayList list = lCall("ORWPT ADMITLST", dfn);
      Vector admissionsVect = new Vector(); 
      for(int i = 0; i < list.size(); i++) {
        String x = (String)list.get(i);
        if ((x.trim().length() > 0)  && (StringUtils.piece(x, 1).length() > 0)) {
          double admit = StringUtils.toDouble(StringUtils.piece(x, 1), 0);
          String[] params = {String.valueOf(dfn), String.valueOf(admit)};
          String y = sCall("ORWPT DISCHARGE", params);
          Admission admission = new Admission();
          if (returnRpcResult)
            admission.setRpcResult(x + '^' + y);
          admission.setDfn(dfn);
          admission.setAdmitDate(FMDateUtils.fmDateTimeToDate(StringUtils.piece(x, 1)));
          try {
            admission.setAdmitDateStr(DateUtils.toEnglishDate(admission.getAdmitDate()));
          } catch(ParseException pe) {}
          admission.setDischargeDate(FMDateUtils.fmDateTimeToDate(StringUtils.piece(y, 1)));
          try {
            admission.setDischargeDateStr(DateUtils.toEnglishDate(admission.getDischargeDate()));
          } catch(ParseException pe) {}        
          admission.setLocationIen(StringUtils.piece(x, 2));
          admission.setLocation(StringUtils.piece(x, 3));
          admission.setType(StringUtils.piece(x, 4));
          admissionsVect.add(admission);
        }
      }
      Admission[] admissions = new Admission[admissionsVect.size()];
      for (int i = 0; i < admissionsVect.size(); i++)
        admissions[i] = (Admission)admissionsVect.get(i);
      admissionsList.setAdmissions(admissions);
      return admissionsList;
    } else
      throw getCreateContextException("OR CPRS GUI CHART");
  } 
  
}