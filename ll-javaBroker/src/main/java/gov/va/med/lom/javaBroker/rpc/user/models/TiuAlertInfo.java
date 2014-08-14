package gov.va.med.lom.javaBroker.rpc.user.models;

import gov.va.med.lom.javaBroker.rpc.BaseBean;

import java.io.Serializable;

public class TiuAlertInfo extends BaseBean implements Serializable {

  private int tiuda;
  private String guiTabIndicator;
  
  public TiuAlertInfo() {
    this.tiuda = 0;
    this.guiTabIndicator = null;
  }
  
  public String getGuiTabIndicator() {
    return guiTabIndicator;
  }
  
  public void setGuiTabIndicator(String guiTabIndicator) {
    this.guiTabIndicator = guiTabIndicator;
  }
  
  public int getTiuda() {
    return tiuda;
  }
  
  public void setTiuda(int tiuda) {
    this.tiuda = tiuda; 
  }
}
