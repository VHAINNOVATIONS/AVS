package gov.va.med.lom.javaBroker.rpc.patient.models;

import gov.va.med.lom.javaBroker.rpc.BaseBean;

import java.io.Serializable;

public class LabResultTestType extends BaseBean implements Serializable {

  private int start;
  private String name;
  
  public LabResultTestType() {
    this.start = 0;
    this.name = null;
  }
  
  public int getStart() {
    return start;
  }
  
  public void setStart(int length) {
    this.start = length;
  }
  
  public String getName() {
    return name;
  }
  
  public void setName(String name) {
    this.name = name;
  }
}
