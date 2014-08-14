package gov.va.med.lom.javaBroker.rpc.patient.models;

import gov.va.med.lom.javaBroker.rpc.BaseBean;

import java.io.Serializable;

public class MeansTestStatus extends BaseBean implements Serializable {

  private boolean required;
  private String message;
  
  public MeansTestStatus() {
    this.required = false;
    this.message = null;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }
  
  public boolean getRequired() {
    return required;
  }
  
  public void setRequired(boolean required) {
    this.required = required;
  }
  
}
