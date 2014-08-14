package gov.va.med.lom.javaBroker.rpc.lists.models;

import java.io.Serializable;

import gov.va.med.lom.javaBroker.rpc.BaseBean;

public class StatusUrgencyList extends BaseBean implements Serializable {

  private StatusUrgency[] statusUrgencies;
  
  public StatusUrgencyList() {
    this.statusUrgencies = null;
  }
  
  public StatusUrgency[] getStatusUrgencies() {
    return statusUrgencies;
  }
  
  public void setStatusUrgencies(StatusUrgency[] statusUrgencies) {
    this.statusUrgencies = statusUrgencies;
  }
}
