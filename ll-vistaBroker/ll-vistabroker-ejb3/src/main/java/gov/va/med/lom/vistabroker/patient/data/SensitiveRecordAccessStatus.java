package gov.va.med.lom.vistabroker.patient.data;

import java.io.Serializable;

public class SensitiveRecordAccessStatus implements Serializable {

  private String dfn;
  private int result;
  private String message; 
  
  public SensitiveRecordAccessStatus() {
    this.dfn = null;
    this.result = 0;
    this.message = null;
  }
  
  public String getDfn() {
    return dfn;
  }

  public void setDfn(String dfn) {
    this.dfn = dfn;
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
