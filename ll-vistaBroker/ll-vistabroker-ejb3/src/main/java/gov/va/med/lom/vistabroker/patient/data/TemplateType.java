package gov.va.med.lom.vistabroker.patient.data;

import java.io.Serializable;

public class TemplateType implements Serializable {

  private String ien;
  private String code;
  private String title;
  
  public String getIen() {
    return ien;
  }
  public void setIen(String ien) {
    this.ien = ien;
  }
  public String getCode() {
    return code;
  }
  public void setCode(String code) {
    this.code = code;
  }
  public String getTitle() {
    return title;
  }
  public void setTitle(String title) {
    this.title = title;
  }
  
}
