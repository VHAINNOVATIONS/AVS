package gov.va.med.lom.javaBroker.rpc.patient.models;

import gov.va.med.lom.javaBroker.rpc.BaseBean;

import java.io.Serializable;
import java.util.Date;

public class LabTestResult extends BaseBean implements Serializable {

  private Date date;
  private String dateStr;
  private String name;
  private String status;
  private String result;
  
  public LabTestResult() {
    this.date = null;
    this.dateStr = null;
    this.name = null;
    this.status = null;
    this.result = null;
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
