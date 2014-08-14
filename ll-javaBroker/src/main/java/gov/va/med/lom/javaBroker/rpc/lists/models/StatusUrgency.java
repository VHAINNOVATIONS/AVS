package gov.va.med.lom.javaBroker.rpc.lists.models;

import gov.va.med.lom.javaBroker.rpc.BaseBean;

import java.io.Serializable;

public class StatusUrgency extends BaseBean implements Serializable {
  
  private String text;
  
  public StatusUrgency() {
    this.text = null;
  }
  
  public String getText() {
    return text;
  }
  
  public void setText(String text) {
    this.text = text;
  }
}
