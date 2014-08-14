package gov.va.med.lom.avs.model;

import java.io.Serializable;

public class VistaPrinter implements Serializable {	

  private String ien;
  private String name;
  private String displayName;
  private String ipAddress;
  private String location;
  private int rightMargin;
  private int pageLength;
  
  public String getName() {
    return name;
  }
  public void setName(String name) {
    this.name = name;
  }
  public String getIpAddress() {
    return ipAddress;
  }
  public void setIpAddress(String ipAddress) {
    this.ipAddress = ipAddress;
  }
  public String getIen() {
    return ien;
  }
  public void setIen(String ien) {
    this.ien = ien;
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
