package gov.va.med.lom.javaBroker.rpc.patient.models;

import java.io.Serializable;

import gov.va.med.lom.javaBroker.rpc.BaseBean;

public class ClinicalRemindersList extends BaseBean implements Serializable {

  private ClinicalReminder[] clinicalReminders;
  
  public ClinicalRemindersList() {
    this.clinicalReminders = null;
  }
  
  public ClinicalReminder[] getClinicalReminders() {
    return clinicalReminders;
  }
  
  public void setClinicalReminders(ClinicalReminder[] clinicalReminders) {
    this.clinicalReminders = clinicalReminders;
  }
}
