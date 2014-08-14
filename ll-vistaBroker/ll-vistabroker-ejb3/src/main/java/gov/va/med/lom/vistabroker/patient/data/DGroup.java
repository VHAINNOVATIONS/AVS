package gov.va.med.lom.vistabroker.patient.data;

import java.io.Serializable;

public class DGroup implements Serializable {

  private String ien;
  private String item;
  private String subitem;
  
  public DGroup() {
    this.ien = null;
    this.item = null;
    this.subitem = null;
  }
  
  public String getIen() {
    return ien;
  }

  public void setIen(String ien) {
    this.ien = ien;
  }

  public String getItem() {
    return item;
  }
  
  public void setItem(String item) {
    this.item = item;
  }
  
  public String getSubitem() {
    return subitem;
  }
  
  public void setSubitem(String subitem) {
    this.subitem = subitem;
  }
}
