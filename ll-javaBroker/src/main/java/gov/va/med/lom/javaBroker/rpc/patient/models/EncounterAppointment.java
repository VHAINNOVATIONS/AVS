package gov.va.med.lom.javaBroker.rpc.patient.models;

import gov.va.med.lom.javaBroker.rpc.BaseBean;

import java.io.Serializable;
import java.util.Date;

public class EncounterAppointment extends BaseBean implements Serializable {
  
  private Date datetime;
  private String datetimeStr;
  private String locationIen;
  private String locationName;
  private String status;
  private boolean standalone;
  private String title;
  private String type;
  
  public EncounterAppointment() {
    this.datetimeStr = null;
    this.datetime = null;
    this.locationIen = null;
    this.locationName = null;
    this.status = null;    
    this.standalone = false;
    this.title = null;
    this.type = null;
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
  
}
