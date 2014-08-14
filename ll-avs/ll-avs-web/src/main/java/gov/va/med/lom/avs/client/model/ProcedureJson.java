package gov.va.med.lom.avs.client.model;

import java.io.Serializable;

public class ProcedureJson implements Serializable {

  private String site;
  private String date;
  private String name;
  private String code;
  
  public String getSite() {
    return site;
  }
  public void setSite(String site) {
    this.site = site;
  }
  public String getDate() {
    return date;
  }
  public void setDate(String date) {
    this.date = date;
  }
  public String getName() {
    return name;
  }
  public void setName(String name) {
    this.name = name;
  }
  public String getCode() {
    return code;
  }
  public void setCode(String code) {
    this.code = code;
  }
  
}
