package gov.va.med.lom.vistabroker.patient.data;

import java.io.Serializable;

public class SimilarRecordsStatus implements Serializable {

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
