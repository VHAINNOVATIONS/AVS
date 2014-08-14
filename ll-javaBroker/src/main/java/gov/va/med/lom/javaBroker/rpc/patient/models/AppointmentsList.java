package gov.va.med.lom.javaBroker.rpc.patient.models;

import java.io.Serializable;
import java.util.Date;

import gov.va.med.lom.javaBroker.rpc.BaseBean;

public class AppointmentsList extends BaseBean implements Serializable {

  private Appointment[] appointments;
  private Date fromDate;
  private Date throughDate;  
  
  public AppointmentsList() {
    this.appointments = null;
  }
  
  public Appointment[] getAppointments() {
    return appointments;
  }
  
  public void setAppointments(Appointment[] appointments) {
    this.appointments = appointments;
  }
  
  public Date getFromDate() {
    return fromDate;
  }
  
  public void setFromDate(Date fromDate) {
    this.fromDate = fromDate;
  }
  
  public Date getThroughDate() {
    return throughDate;
  }
  
  public void setThroughDate(Date throughDate) {
    this.throughDate = throughDate;
  }    
}
