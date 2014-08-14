package gov.va.med.lom.vistabroker.patient.data;

import java.io.Serializable;

public class ClinicalReminder implements Serializable {

  private String ien;
  private String dfn;
  private String whenDue;
  private String lastOccurrence;
  private String name;
  
  public ClinicalReminder() {
    this.ien = null;
    this.dfn = null;
    this.whenDue = null;
    this.lastOccurrence = null;
    this.name = null;
  }
  
  public String getIen() {
    return ien;
  }

  public void setIen(String ien) {
    this.ien = ien;
  }

  public String getDfn() {
    return dfn;
  }

  public void setDfn(String dfn) {
    this.dfn = dfn;
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
