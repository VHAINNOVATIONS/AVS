package gov.va.med.lom.vistabroker.patient.data;

import java.io.Serializable;

public class PatientLocation implements Serializable {

  private String ien;
  private String name;
  private String service;
  
  public PatientLocation() {
    this.ien = null;
    this.name = null;
    this.service = null;
  }
  
  public String getIen() {
    return ien;
  }

  public void setIen(String ien) {
    this.ien = ien;
  }

  public String getName() {
    return name;
  }
  
  public void setName(String name) {
    this.name = name;
  }
  
  public String getService() {
    return service;
  }
  
  public void setService(String service) {
    this.service = service;
  }
  
}
