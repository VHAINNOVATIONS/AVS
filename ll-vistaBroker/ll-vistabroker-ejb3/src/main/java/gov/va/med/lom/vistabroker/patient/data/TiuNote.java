package gov.va.med.lom.vistabroker.patient.data;

import java.io.Serializable;

public class TiuNote implements Serializable {

  private String ien;
  private String dfn;
  private String text;
  private String message;
  
  public TiuNote() {
    this.ien = null;
    this.dfn = null;
    this.text = null;
    this.message = null;
  }
  
  public String getIen() {
    return ien;
  }

  public void setIen(String ien) {
    this.ien = ien;
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
  
  public String getText() {
    return text;
  }
  
  public void setText(String text) {
    this.text = text;
  }
  
}
