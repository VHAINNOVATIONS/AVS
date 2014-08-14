package gov.va.med.lom.javaBroker.rpc.patient.models;

import java.io.Serializable;

import gov.va.med.lom.javaBroker.rpc.BaseBean;

public class ConsultRequestsList extends BaseBean implements Serializable {

  private ConsultRequest[] consultRequests;
  
  public ConsultRequestsList() {
    this.consultRequests = null;
  }
  
  public ConsultRequest[] getConsultRequests() {
    return consultRequests;
  }
  
  public void setConsultRequests(ConsultRequest[] consultRequests) {
    this.consultRequests = consultRequests;
  }
}
