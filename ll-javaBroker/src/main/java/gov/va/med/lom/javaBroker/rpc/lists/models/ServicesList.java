package gov.va.med.lom.javaBroker.rpc.lists.models;

import gov.va.med.lom.javaBroker.rpc.BaseBean;

import java.io.Serializable;

public class ServicesList extends BaseBean implements Serializable {

  private Service[] services;
  
  public ServicesList() {
    this.services = null;
  }

  public Service[] getServices() {
    return services;
  }
  
  public void setServices(Service[] services) {
    this.services = services;
  }
}
