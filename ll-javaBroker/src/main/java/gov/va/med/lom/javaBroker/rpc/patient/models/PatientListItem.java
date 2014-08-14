package gov.va.med.lom.javaBroker.rpc.patient.models;

import gov.va.med.lom.javaBroker.rpc.BaseBean;

import java.io.Serializable;
import java.util.Date;

public class PatientListItem extends BaseBean implements Serializable {

  private String name;
  private boolean isAlias;
  private String nonAliasedName;
  private Date date;
  private String dateStr;
  private String ssn;
  private String location;
  
  public PatientListItem() {
    this.name = null;
    this.isAlias = false;
    this.nonAliasedName = null;
    this.date = null;
    this.dateStr = null;
    this.ssn = null;
    this.location = null;
  }
  
  public Date getDate() {
    return date;
  }
  
  public void setDate(Date date) {
    this.date = date;
  }
  
  public String getDateStr() {
    return dateStr;
  }

  public void setDateStr(String dateStr) {
    this.dateStr = dateStr;
  }
  
  public String getNonAliasedName() {
    return nonAliasedName;
  }

  public void setNonAliasedName(String nonAliasedName) {
    this.nonAliasedName = nonAliasedName;
  }

  public String getName() {
    return name;
  }
  
  public void setName(String name) {
    this.name = name;
  }

  public String getLocation() {
    return location;
  }
  
  public void setLocation(String location) {
    this.location = location;
  }
  
  public String getSsn() {
    return ssn;
  }
  
  public void setSsn(String ssn) {
    this.ssn = ssn;
  }

  public boolean getIsAlias() {
    return isAlias;
  }

  public void setIsAlias(boolean isAlias) {
    this.isAlias = isAlias;
  }
  
}
