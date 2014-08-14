package gov.va.med.lom.vistabroker.auth.data;

import java.io.Serializable;

public class ChangeVerifyCodeResult implements Serializable {
  
  private boolean success;
  private String message;
  
  public ChangeVerifyCodeResult() {
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
