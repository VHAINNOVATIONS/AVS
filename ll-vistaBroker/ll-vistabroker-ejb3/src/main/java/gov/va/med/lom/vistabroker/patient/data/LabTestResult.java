package gov.va.med.lom.vistabroker.patient.data;

import java.io.Serializable;
import java.util.Date;

public class LabTestResult implements Serializable {

  private String ien;
  private String dfn;
  private Date date;
  private String dateStr;
  private String name;
  private String status;
  private String result;
  
  public LabTestResult() {
    this.ien = null;
    this.dfn = null;
    this.date = null;
    this.dateStr = null;
    this.name = null;
    this.status = null;
    this.result = null;
  }
  
  public String getDfn() {
    return dfn;
  }

  public void setDfn(String dfn) {
    this.dfn = dfn;
  }

  public String getIen() {
    return ien;
  }

  public void setIen(String ien) {
    this.ien = ien;
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
  
  public String getName() {
    return name;
  }
  
  public void setName(String name) {
    this.name = name;
  }
  
  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }
  
  public String getResult() {
    return result;
  }
  
  public void setResult(String result) {
    this.result = result;
  }
  
}
