package gov.va.med.lom.vistabroker.patient.dao;

import gov.va.med.lom.javaUtils.misc.DateUtils;
import gov.va.med.lom.javaUtils.misc.StringUtils;
import gov.va.med.lom.vistabroker.dao.BaseDao;
import gov.va.med.lom.vistabroker.patient.data.ClinicalReminder;
import gov.va.med.lom.vistabroker.util.FMDateUtils;

import java.util.ArrayList;
import java.util.List;

public class ClinicalRemindersDao extends BaseDao {

  // CONSTRUCTORS
  public ClinicalRemindersDao() {
    super();
  }
  
  public ClinicalRemindersDao(BaseDao baseDao) {
    super(baseDao);
  }
  
  // RPC API
  public List<ClinicalReminder> getClinicalReminders(String dfn) throws Exception {
    setDefaultContext("OR CPRS GUI CHART");
    setDefaultRpcName("ORQQPX REMINDERS LIST");
    List<ClinicalReminder> clinicalReminders = new ArrayList<ClinicalReminder>();
    List<String> list = lCall(dfn);
    for(String s : list) {
      if (s.trim().length() > 0) {
        String due = StringUtils.piece(s, 3);
        if (!due.equals("DUE NOW"))
          due = DateUtils.formatDate(FMDateUtils.fmDateTimeToDate(due), DateUtils.ENGLISH_DATE_FORMAT);
        String occur = DateUtils.formatDate(FMDateUtils.fmDateTimeToDate(StringUtils.piece(s, 4)), DateUtils.ENGLISH_DATE_FORMAT);
        ClinicalReminder reminder = new ClinicalReminder();
        // IEN^PRINT NAME^DUE DATE/TIME^LAST OCCURRENCE DATE/TIME
        reminder.setDfn(dfn);
        reminder.setIen(StringUtils.piece(s, 1)); 
        reminder.setWhenDue(due);
        reminder.setLastOccurrence(occur);
        reminder.setName(StringUtils.mixedCase(StringUtils.piece(s, 2)));
        clinicalReminders.add(reminder);
      }
    }
    return clinicalReminders;
  } 
  
  public String getClinicalReminderDetails(String dfn, String ien) throws Exception {
    setDefaultContext("OR CPRS GUI CHART");
    setDefaultRpcName("ORQQPXRM REMINDER DETAIL");
    String[] params = {dfn, ien};
    return sCall(params);
  }
  
}