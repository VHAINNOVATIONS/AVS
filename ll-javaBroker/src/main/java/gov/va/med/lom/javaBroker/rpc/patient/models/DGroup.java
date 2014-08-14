package gov.va.med.lom.javaBroker.rpc.patient.models;

import gov.va.med.lom.javaBroker.rpc.BaseBean;

import java.io.Serializable;

public class DGroup extends BaseBean implements Serializable {

  private String item;
  private String subitem;
  
  public DGroup() {
    this.item = null;
    this.subitem = null;
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
