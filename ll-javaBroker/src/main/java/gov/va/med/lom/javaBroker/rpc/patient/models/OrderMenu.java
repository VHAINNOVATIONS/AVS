package gov.va.med.lom.javaBroker.rpc.patient.models;

import java.util.List;

import gov.va.med.lom.javaBroker.rpc.BaseBean;

import java.io.Serializable;

public class OrderMenu extends BaseBean implements Serializable {

  private int numCols;
  private String title;
  private String keyVars;
  private List<OrderMenuItem> menuItems;
  
  public int getNumCols() {
    return numCols;
  }
  public void setNumCols(int numCols) {
    this.numCols = numCols;
  }
  public String getTitle() {
    return title;
  }
  public void setTitle(String title) {
    this.title = title;
  }
  public String getKeyVars() {
    return keyVars;
  }
  public void setKeyVars(String keyVars) {
    this.keyVars = keyVars;
  }
  public List<OrderMenuItem> getMenuItems() {
    return menuItems;
  }
  public void setMenuItems(List<OrderMenuItem> menuItems) {
    this.menuItems = menuItems;
  }

}
