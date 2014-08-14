package gov.va.med.lom.javaBroker.rpc.lists.models;

import gov.va.med.lom.javaBroker.rpc.BaseBean;

import java.io.Serializable;

public class LabReport extends BaseBean implements Serializable {

  private String name;
  private String rpc;
  
  public LabReport() {
    this.name = null;
    this.rpc = null;
  }
  
  public String getName() {
    return name;
  }
  
  public void setName(String name) {
    this.name = name;
  }
  
  public String getRpc() {
    return rpc;
  }
  
  public void setRpc(String rpc) {
    this.rpc = rpc;
  }
}
