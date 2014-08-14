package gov.va.med.lom.vistabroker.patient.dao;

import gov.va.med.lom.javaUtils.misc.StringUtils;
import gov.va.med.lom.vistabroker.dao.BaseDao;
import gov.va.med.lom.vistabroker.patient.data.SensitiveRecordAccessStatus;

import java.util.List;

/*
  RESULT(1) = -1-RPC/API failed 
  Required variable not defined
  0-No display/action required
  Not an employee, not sensitive or not accessing own Patient
  record
  1-Display warning message
  Sensitive - inpatient or a DG SENSITIVITY key holder
  or Employee and DG SECURITY OFFICER key holder
  2-Display warning message, require OK to continue and call
  DG SENSITIVE RECORD BULLETIN RPC to update DG Security Log
  file and generate Sensitive Record Access mail message.
  Sensitive - not an inpatient and not a key holder
  or Employee/not a DG SECURITY OFFICER key holder
  3-Access to record denied
  Accessing own Patient file record
  4-Access to Patient file (#2) records denied
  SSN not defined
  
  RESULT(2-n) = error message or warning/Privacy Act message.  Error and
  warning messages will begin in RESULT(2) array.  The Privacy Act message
  is the longest message and will utilize RESULT(2)- RESULT(8).
  
  If RESULT(1)=1, the DG Security Log file is updated.
  If RESULT(1)=2, the user must acknowledge they want to access the
  restricted record and the application must call the DG SENSITIVE RECORD
  BULLETIN RPC to update the DG Security Log file and generate the Sensitive
  Record Access mail message.
*/

public class SensitiveRecordAccessDao extends BaseDao {
  
  public static final int RPC_FAILED = -1;
  public static final int NO_ACTION_REQUIRED = 0;
  public static final int INPATIENT_OR_EMPLOYEE = 1;
  public static final int SENSITIVE_RECORD = 2;
  public static final int ACCESSING_OWN_PATIENT_RECORD = 4;
  public static final int SSN_NOT_DEFINED = 5;
  
  // CONSTRUCTORS
  public SensitiveRecordAccessDao() {
    super();
  }
  
  public SensitiveRecordAccessDao(BaseDao baseDao) {
    super(baseDao);
  }
  
  // RPC API  
  public SensitiveRecordAccessStatus getSensitiveRecordAccess(String dfn) throws Exception {
    setDefaultContext("OR CPRS GUI CHART");
    setDefaultRpcName("DG SENSITIVE RECORD ACCESS");
      SensitiveRecordAccessStatus sensitiveRecordAccessStatus = new SensitiveRecordAccessStatus();    
      List<String> list = lCall(dfn);
      sensitiveRecordAccessStatus.setDfn(dfn);
      sensitiveRecordAccessStatus.setResult(Integer.valueOf((String)list.get(0)).intValue());
      StringBuffer sb = new StringBuffer();
      for (String s : list)
        sb.append(s + '\n');
      sensitiveRecordAccessStatus.setMessage(sb.toString().trim());
      return sensitiveRecordAccessStatus;
  }   
  
  public boolean logSensitiveRecordAccess(String dfn) throws Exception {
    setDefaultContext("OR CPRS GUI CHART");
    setDefaultRpcName("DG SENSITIVE RECORD BULLETIN");
    String x = sCall(dfn);
    return StringUtils.strToBool(StringUtils.piece(x, 1), "1");
  }
  
}