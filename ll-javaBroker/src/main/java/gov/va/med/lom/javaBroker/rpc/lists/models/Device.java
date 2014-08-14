package gov.va.med.lom.javaBroker.rpc.lists.models;

import gov.va.med.lom.javaBroker.rpc.BaseBean;

import java.io.Serializable;

public class Device extends BaseBean implements Serializable {

  private String name;
  private String displayName;
  private String location;
  private int rightMargin;
  private int pageLength;
  
  public String getName() {
    return name;
  }
  public void setName(String name) {
    this.name = name;
  }
  public String getDisplayName() {
    return displayName;
  }
  public void setDisplayName(String displayName) {
    this.displayName = displayName;
  }
  public String getLocation() {
    return location;
  }
  public void setLocation(String location) {
    this.location = location;
  }
  public int getRightMargin() {
    return rightMargin;
  }
  public void setRightMargin(int rightMargin) {
    this.rightMargin = rightMargin;
  }
  public int getPageLength() {
    return pageLength;
  }
  public void setPageLength(int pageLength) {
    this.pageLength = pageLength;
  }
  
}
