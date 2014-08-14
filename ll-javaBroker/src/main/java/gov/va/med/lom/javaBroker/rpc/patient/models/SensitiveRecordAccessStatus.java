package gov.va.med.lom.javaBroker.rpc.patient.models;

import gov.va.med.lom.javaBroker.rpc.BaseBean;

import java.io.Serializable;

public class SensitiveRecordAccessStatus extends BaseBean implements Serializable {

  private int result;
  private String message; 
  
  public SensitiveRecordAccessStatus() {
    this.result = 0;
    this.message = null;
  }
  
  public String getMessage() {
    return message;
  }
  
  public void setMessage(String message) {
    this.message = message;
  }
  
  public int getResult() {
    return result;
  }
  
  public void setResult(int result) {
    this.result = result;
  }
  
}
