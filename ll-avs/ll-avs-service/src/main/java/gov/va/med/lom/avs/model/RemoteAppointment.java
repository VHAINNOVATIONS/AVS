package gov.va.med.lom.avs.model;

import java.util.Date;

public class RemoteAppointment {

  private String id;
  private Date datetime;
  private double fmdatetime;
  private String location;
  private String site;
  private String stationNo;
  private String patientDfn;
  
  public Date getDatetime() {
    return datetime;
  }
  public void setDatetime(Date datetime) {
    this.datetime = datetime;
  }
  public double getFmdatetime() {
    return fmdatetime;
  }
  public void setFmdatetime(double fmdatetime) {
    this.fmdatetime = fmdatetime;
  }
  public String getLocation() {
    return location;
  }
  public void setLocation(String location) {
    this.location = location;
  }
  public String getSite() {
    return site;
  }
  public void setSite(String site) {
    this.site = site;
  }
  public String getStationNo() {
    return stationNo;
  }
  public void setStationNo(String stationNo) {
    this.stationNo = stationNo;
  }
  public String getId() {
    return id;
  }
  public void setId(String id) {
    this.id = id;
  }
  public String getPatientDfn() {
    return patientDfn;
  }
  public void setPatientDfn(String patientDfn) {
    this.patientDfn = patientDfn;
  }
  
}
