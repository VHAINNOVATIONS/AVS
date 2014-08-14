package gov.va.med.lom.vistabroker.patient.data;

import java.io.Serializable;

public class LockDocumentResult implements Serializable {
  
  private boolean success;
  private String message;
  
  public LockDocumentResult() {
    this.success = false;
    this.message = null;
  }
  
  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }
  
  public boolean getSuccess() {
    return success;
  }
  
  public void setSuccess(boolean success) {
    this.success = success;
  }
  
}
