package gov.va.med.lom.vistabroker.patient.data;

import java.io.Serializable;

public class MeansTestStatus implements Serializable {

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
