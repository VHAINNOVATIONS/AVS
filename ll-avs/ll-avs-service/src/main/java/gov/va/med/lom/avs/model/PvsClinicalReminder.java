package gov.va.med.lom.avs.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="ckoPvsClinicalReminders")
public class PvsClinicalReminder extends BaseModel implements Serializable {

  private String stationNo;
  private String reminderIen;
  private String reminderName;
  
  public String getStationNo() {
    return stationNo;
  }
  public void setStationNo(String stationNo) {
    this.stationNo = stationNo;
  }
  public String getReminderIen() {
    return reminderIen;
  }
  public void setReminderIen(String reminderIen) {
    this.reminderIen = reminderIen;
  }
  public String getReminderName() {
    return reminderName;
  }
  public void setReminderName(String reminderName) {
    this.reminderName = reminderName;
  }
  
}
