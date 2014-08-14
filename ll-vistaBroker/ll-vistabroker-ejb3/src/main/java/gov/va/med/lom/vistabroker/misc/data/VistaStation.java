package gov.va.med.lom.vistabroker.misc.data;

import java.io.Serializable;

public class VistaStation implements Serializable {

  private String ien;
  private String name;
  
  public VistaStation() {
    this.ien = null;
    this.name = null;
  }
  
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
