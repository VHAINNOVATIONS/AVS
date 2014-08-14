package gov.va.med.lom.javaBroker.rpc.lists.models;

import gov.va.med.lom.javaBroker.rpc.BaseBean;

import java.io.Serializable;

public class LabTestType extends BaseBean implements Serializable {

  private String name;
  
  public LabTestType() {
    this.name = null;
  }
  
  public String getName() {
    return name;
  }
  
  public void setName(String name) {
    this.name = name;
  }
}
