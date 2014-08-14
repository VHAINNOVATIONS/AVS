package gov.va.med.lom.avs.model;

import java.util.List;
import java.io.Serializable;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

@Entity("remoteAppointments")
public class RemoteAppointments implements Serializable {

  @Id private ObjectId id;
  private String date;
  private String patientDfn;
  private String stationNo;
  @Embedded private List<RemoteAppointment> appointments;
  
  public ObjectId getId() {
    return id;
  }
  public void setId(ObjectId id) {
    this.id = id;
  }
  public String getPatientDfn() {
    return patientDfn;
  }
  public void setPatientDfn(String patientDfn) {
    this.patientDfn = patientDfn;
  }
  public String getStationNo() {
    return stationNo;
  }
  public void setStationNo(String stationNo) {
    this.stationNo = stationNo;
  }
  public String getDate() {
    return date;
  }
  public void setDate(String date) {
    this.date = date;
  }
  public List<RemoteAppointment> getAppointments() {
    return appointments;
  }
  public void setAppointments(List<RemoteAppointment> appointments) {
    this.appointments = appointments;
  }
  
}
