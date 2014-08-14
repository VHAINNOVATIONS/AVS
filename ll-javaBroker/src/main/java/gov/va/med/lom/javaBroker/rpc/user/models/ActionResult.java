package gov.va.med.lom.javaBroker.rpc.user.models;

import java.io.Serializable;

import gov.va.med.lom.javaBroker.rpc.BaseBean;

public class ActionResult extends BaseBean implements Serializable {
  
  private boolean success;
  private String message;
  
  public ActionResult() {
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
