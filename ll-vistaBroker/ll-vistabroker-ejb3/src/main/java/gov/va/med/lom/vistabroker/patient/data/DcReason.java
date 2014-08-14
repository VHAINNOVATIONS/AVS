package gov.va.med.lom.vistabroker.patient.data;

import java.io.Serializable;

public class DcReason implements Serializable {
  
  private String ien;
  private String name;
  
  public String getIen() {
    return ien;
  }
  public void setIen(String ien) {
    this.ien = ien;
  }
  public String getName() {
    return name;
  }
  public void setName(String name) {
    this.name = name;
  }
  
}
