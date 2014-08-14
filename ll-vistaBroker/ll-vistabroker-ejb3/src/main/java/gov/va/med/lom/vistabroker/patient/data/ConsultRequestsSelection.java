package gov.va.med.lom.vistabroker.patient.data;

import java.io.Serializable;
import java.util.Date;

public class ConsultRequestsSelection implements Serializable {

  private Date startDate; 
  private Date stopDate;
  private String service;
  
  public ConsultRequestsSelection() {
    this.startDate = null;
    this.stopDate = null;
    this.service = null;
  }
  
  public String getService() {
    return service;
  }
  
  public void setService(String service) {
    this.service = service;
  }
  
  public Date getStartDate() {
    return startDate;
  }
  
  public void setStartDate(Date startDate) {
    this.startDate = startDate;
  }
  
  public Date getStopDate() {
    return stopDate;
  }
  
  public void setStopDate(Date stopDate) {
    this.stopDate = stopDate;
  }
}
