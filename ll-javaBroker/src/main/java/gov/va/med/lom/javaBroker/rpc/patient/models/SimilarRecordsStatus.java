package gov.va.med.lom.javaBroker.rpc.patient.models;

import gov.va.med.lom.javaBroker.rpc.BaseBean;

import java.io.Serializable;

public class SimilarRecordsStatus extends BaseBean implements Serializable {

  private boolean exists;
  private String message;
  
  public SimilarRecordsStatus() {
    this.exists = false;
    this.message = null;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }
  
  public boolean getExists() {
    return exists;
  }
  
  public void setExists(boolean exists) {
    this.exists = exists;
  }
  
}
