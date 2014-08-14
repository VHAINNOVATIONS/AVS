package gov.va.med.lom.javaBroker.rpc.patient.models;

import gov.va.med.lom.javaBroker.rpc.BaseBean;

import java.io.Serializable;

public class PatientLocation extends BaseBean implements Serializable {

  private String name;
  private String service;
  
  public PatientLocation() {
    this.name = null;
    this.service = null;
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
