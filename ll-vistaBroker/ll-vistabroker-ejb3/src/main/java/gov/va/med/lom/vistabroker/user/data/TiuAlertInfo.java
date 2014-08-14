package gov.va.med.lom.vistabroker.user.data;

import java.io.Serializable;

public class TiuAlertInfo implements Serializable {

  private String dfn;
  private String tiuda;
  private String guiTabIndicator;
  
  public TiuAlertInfo() {
    this.dfn = null;
    this.tiuda = null;
    this.guiTabIndicator = null;
  }
  
  public String getDfn() {
    return dfn;
  }

  public void setDfn(String dfn) {
    this.dfn = dfn;
  }

  public String getGuiTabIndicator() {
    return guiTabIndicator;
  }
  
  public void setGuiTabIndicator(String guiTabIndicator) {
    this.guiTabIndicator = guiTabIndicator;
  }
  
  public String getTiuda() {
    return tiuda;
  }
  
  public void setTiuda(String tiuda) {
    this.tiuda = tiuda; 
  }
}
