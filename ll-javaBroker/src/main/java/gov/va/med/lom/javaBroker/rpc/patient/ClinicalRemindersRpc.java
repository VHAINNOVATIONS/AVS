package gov.va.med.lom.javaBroker.rpc.patient;

import java.util.ArrayList;
import java.util.Vector;

import gov.va.med.lom.javaBroker.rpc.*;
import gov.va.med.lom.javaBroker.rpc.patient.models.*;
import gov.va.med.lom.javaBroker.util.FMDateUtils;
import gov.va.med.lom.javaBroker.util.StringUtils;
import gov.va.med.lom.javaBroker.util.DateUtils;

public class ClinicalRemindersRpc extends AbstractRpc {

  // FIELDS 
  private ClinicalRemindersList clinicalRemindersList;
  
  // CONSTRUCTORS
  public ClinicalRemindersRpc() throws BrokerException {
    super();
  }
  
  public ClinicalRemindersRpc(RpcBroker rpcBroker) throws BrokerException {
    super(rpcBroker);
  }
  
  // RPC API
  public synchronized ClinicalRemindersList getClinicalReminders(String dfn) throws BrokerException {
    if (setContext("OR CPRS GUI CHART")) {
      setDfn(dfn);
      clinicalRemindersList = new ClinicalRemindersList();
      ArrayList list = lCall("ORQQPX REMINDERS LIST", dfn);
      if (returnRpcResult) {
        StringBuffer sb = new StringBuffer();
        for(int i = 0; i < list.size(); i++)
          sb.append((String)list.get(i) + "\n");
        clinicalRemindersList.setRpcResult(sb.toString().trim());
      }     
      Vector remindersVect = new Vector(); 
      for(int i = 0; i < list.size(); i++) {
        String x = (String)list.get(i);
        if (x.trim().length() > 0) {
          String due = StringUtils.piece(x, 3);
          if (!due.equals("DUE NOW"))
            due = DateUtils.formatDate(FMDateUtils.fmDateTimeToDate(due), DateUtils.ENGLISH_DATE_FORMAT);
          String occur = DateUtils.formatDate(FMDateUtils.fmDateTimeToDate(StringUtils.piece(x, 4)), DateUtils.ENGLISH_DATE_FORMAT);
          ClinicalReminder reminder = new ClinicalReminder();
          // IEN^PRINT NAME^DUE DATE/TIME^LAST OCCURRENCE DATE/TIME
          if (returnRpcResult)
            reminder.setRpcResult(x);
          reminder.setDfn(dfn);
          reminder.setIen(StringUtils.piece(x, 1)); 
          reminder.setWhenDue(due);
          reminder.setLastOccurrence(occur);
          reminder.setName(StringUtils.mixedCase(StringUtils.piece(x, 2)));
          remindersVect.add(reminder);
        }
      }
      ClinicalReminder[] clinicalReminders = new ClinicalReminder[remindersVect.size()];
      for (int i = 0; i < clinicalReminders.length; i++)
        clinicalReminders[i] = (ClinicalReminder)remindersVect.get(i);
      clinicalRemindersList.setClinicalReminders(clinicalReminders);
      return clinicalRemindersList;
    } else
      throw getCreateContextException("OR CPRS GUI CHART");
  }
  
  public synchronized String getClinicalReminderDetails(String dfn, String ien) throws Exception {
    if (setContext("OR CPRS GUI CHART")) {
      String[] params = {dfn, ien};
      return sCall("ORQQPXRM REMINDER DETAIL", params);
    } else
      throw getCreateContextException("OR CPRS GUI CHART");
  }
  
}