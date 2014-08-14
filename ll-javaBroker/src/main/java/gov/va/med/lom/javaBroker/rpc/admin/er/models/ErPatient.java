package gov.va.med.lom.javaBroker.rpc.admin.er.models;

import java.util.Date;
import java.io.Serializable;

public class ErPatient implements Serializable {

  private String patientId;
  private String complaint;
  private String location;
  private String md;
  private String rn;
  private String acuity;
  private Date timeIn;
  
  public String getAcuity() {
    return acuity;
  }
  public void setAcuity(String acuity) {
    this.acuity = acuity;
  }
  public String getComplaint() {
    return complaint;
  }
  public void setComplaint(String complaint) {
    this.complaint = complaint;
  }
  public String getLocation() {
    return location;
  }
  public void setLocation(String location) {
    this.location = location;
  }
  public String getMd() {
    return md;
  }
  public void setMd(String md) {
    this.md = md;
  }
  public String getPatientId() {
    return patientId;
  }
  public void setPatientId(String patientId) {
    this.patientId = patientId;
  }
  public String getRn() {
    return rn;
  }
  public void setRn(String rn) {
    this.rn = rn;
  }
  public Date getTimeIn() {
    return timeIn;
  }
  public void setTimeIn(Date timeIn) {
    this.timeIn = timeIn;
  }
  
}
