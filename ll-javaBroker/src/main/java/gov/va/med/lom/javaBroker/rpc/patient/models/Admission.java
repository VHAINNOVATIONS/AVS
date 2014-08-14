package gov.va.med.lom.javaBroker.rpc.patient.models;

import java.io.Serializable;
import java.util.Date;

import gov.va.med.lom.javaBroker.rpc.BaseBean;

public class Admission extends BaseBean implements Serializable {

  private Date admitDate;
  private String admitDateStr;
  private Date dischargeDate;
  private String dischargeDateStr;
  private String locationIen;
  private String location;
  private String type;
  
  public Admission() {
    this.admitDate = null;
    this.admitDateStr = null;
    this.dischargeDate = null;
    this.dischargeDateStr = null;
    this.locationIen = null;
    this.location = null;
    this.type = null;
  }
  
  public Date getAdmitDate() {
    return admitDate;
  }
  
  public void setAdmitDate(Date admitDate) {
    this.admitDate = admitDate;
  }
  
  public Date getDischargeDate() {
    return dischargeDate;
  }
  
  public void setDischargeDate(Date dischargeDate) {
    this.dischargeDate = dischargeDate;
  }
  
  public String getAdmitDateStr() {
    return admitDateStr;
  }

  public void setAdmitDateStr(String admitDateStr) {
    this.admitDateStr = admitDateStr;
  }
  
  public String getDischargeDateStr() {
    return dischargeDateStr;
  }
  
  public void setDischargeDateStr(String dischargeDateStr) {
    this.dischargeDateStr = dischargeDateStr;
  }
  
  public String getLocation() {
    return location;
  }
  
  public void setLocation(String location) {
    this.location = location;
  }
  
  public String getLocationIen() {
    return locationIen;
  }
  
  public void setLocationIen(String locationIen) {
    this.locationIen = locationIen;
  }
  
  public String getType() {
    return type;
  }
  
  public void setType(String type) {
    this.type = type;
  }
  
}
