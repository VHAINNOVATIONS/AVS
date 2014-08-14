package gov.va.med.lom.vistabroker.patient.data;

import java.io.Serializable;
import java.util.Date;

public class Patient implements Serializable {

  private String dfn;
  private String name;
  private boolean alias;
  private String nonAliasedName;
  private Date date;
  private String dateStr;
  private String ssn;
  private String location;
  
  public Patient() {
    this.dfn = null;
    this.name = null;
    this.alias = false;
    this.nonAliasedName = null;
    this.date = null;
    this.dateStr = null;
    this.ssn = null;
    this.location = null;
  }
  
  public String getDfn() {
    return dfn;
  }

  public void setDfn(String dfn) {
    this.dfn = dfn;
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

  public boolean isAlias() {
    return alias;
  }

  public void setAlias(boolean alias) {
    this.alias = alias;
  }
  
}
