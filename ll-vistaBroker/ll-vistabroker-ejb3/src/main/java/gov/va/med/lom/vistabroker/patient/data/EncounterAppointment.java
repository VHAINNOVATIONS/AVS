package gov.va.med.lom.vistabroker.patient.data;

import java.io.Serializable;
import java.util.Date;

public class EncounterAppointment implements Serializable {
  
  private String dfn;
  private double fmdatetime;
  private Date datetime;
  private String datetimeStr;
  private String locationIen;
  private String locationName;
  private String status;
  private boolean standalone;
  private String title;
  private String type;
  private String rpcResult;
  
  public EncounterAppointment() {
    this.dfn = null;
    this.datetimeStr = null;
    this.datetime = null;
    this.locationIen = null;
    this.locationName = null;
    this.status = null;    
    this.standalone = false;
    this.title = null;
    this.type = null;
    this.rpcResult = null;
  }
  
  public String getDfn() {
    return dfn;
  }

  public void setDfn(String dfn) {
    this.dfn = dfn;
  }

  public Date getDatetime() {
    return datetime;
  }

  public void setDatetime(Date datetime) {
    this.datetime = datetime;
  }
  
  public String getDatetimeStr() {
    return datetimeStr;
  }
  
  public void setDatetimeStr(String dateTime) {
    this.datetimeStr = dateTime;
  }
  
  public String getLocationIen() {
    return locationIen;
  }
  
  public void setLocationIen(String locationIen) {
    this.locationIen = locationIen;
  }
  
  public String getLocationName() {
    return locationName;
  }
  
  public void setLocationName(String locationName) {
    this.locationName = locationName;
  }
  
  public String getStatus() {
    return status;
  }
  
  public void setStatus(String status) {
    this.status = status;
  }
  
  public boolean getStandalone() {
    return standalone;
  }
  
  public void setStandalone(boolean standalone) {
    this.standalone = standalone;
  }
  
  public String getTitle() {
    return title;
  }
  
  public void setTitle(String title) {
    this.title = title;
  }

  public String getType() {
    return this.type;
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

  public double getFmdatetime() {
    return fmdatetime;
  }

  public void setFmdatetime(double fmdatetime) {
    this.fmdatetime = fmdatetime;
  }
  
}
