package gov.va.med.lom.javaBroker.rpc.patient.models;

import gov.va.med.lom.javaBroker.rpc.BaseBean;

import java.io.Serializable;

public class ClinicalReminder extends BaseBean implements Serializable {

  private String whenDue;
  private String lastOccurrence;
  private String name;
  
  public ClinicalReminder() {
    this.whenDue = null;
    this.lastOccurrence = null;
    this.name = null;
  }
  
  public String getLastOccurrence() {
    return lastOccurrence;
  }
  
  public void setLastOccurrence(String lastOccurrence) {
    this.lastOccurrence = lastOccurrence;
  }
  
  public String getName() {
    return name;
  }
  
  public void setName(String name) {
    this.name = name;
  }
  
  public String getWhenDue() {
    return whenDue;
  }
  
  public void setWhenDue(String whenDue) {
    this.whenDue = whenDue;
  }
  
}
