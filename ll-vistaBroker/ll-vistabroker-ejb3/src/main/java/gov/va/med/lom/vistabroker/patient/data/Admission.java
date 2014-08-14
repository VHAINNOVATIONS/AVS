package gov.va.med.lom.vistabroker.patient.data;

import java.io.Serializable;
import java.util.Date;

public class Admission implements Serializable {

  private String dfn;
  private Date admitDate;
  private String admitDateStr;
  private Date dischargeDate;
  private String dischargeDateStr;
  private String locationIen;
  private String location;
  private String type;
  private String rpcResult;
  
  public Admission() {
    this.dfn = null;
    this.admitDate = null;
    this.admitDateStr = null;
    this.dischargeDate = null;
    this.dischargeDateStr = null;
    this.locationIen = null;
    this.location = null;
    this.type = null;
    this.rpcResult = null;
  }

  public String getDfn() {
    return dfn;
  }

  public void setDfn(String dfn) {
    this.dfn = dfn;
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

  public String getRpcResult() {
    return rpcResult;
  }

  public void setRpcResult(String rpcResult) {
    this.rpcResult = rpcResult;
  }
  
}
