package gov.va.med.lom.javaBroker.rpc.lists.models;

import gov.va.med.lom.javaBroker.rpc.BaseBean;

import java.io.Serializable;

public class CoverSheetRpc extends BaseBean implements Serializable {
  
  private String name;
  private String rpc;
  private boolean mixedCase;
  private boolean invert;
  private String datePiece;
  private String dateFormat;
  private String textColor;
  private String status;
  private String param1;
  private String id;
  private String qualifier;
  private String tabPos;
  private String piece;
  private String detail;
  private String ifn;
  
  public CoverSheetRpc() {
    this.name = null;
    this.rpc = null;
    this.mixedCase = false;
    this.invert = false;
    this.name = datePiece;
    this.name = dateFormat;
    this.name = textColor;
    this.name = status;
    this.name = param1;
    this.name = id;
    this.name = qualifier;
    this.name = tabPos;
    this.name = piece;
    this.name = detail;
    this.name = ifn;
  }
  
  public String getDateFormat() {
    return dateFormat;
  }
  
  public void setDateFormat(String dateFormat) {
    this.dateFormat = dateFormat;
  }
  
  public String getDatePiece() {
    return datePiece;
  }
  
  public void setDatePiece(String datePiece) {
    this.datePiece = datePiece;
  }
  
  public String getDetail() {
    return detail;
  }
  
  public void setDetail(String detail) {
    this.detail = detail;
  }
  
  public String getId() {
    return id;
  }
  
  public void setId(String id) {
    this.id = id;
  }
  
  public String getIfn() {
    return ifn;
  }
  
  public void setIfn(String ifn) {
    this.ifn = ifn;
  }
  
  public boolean getInvert() {
    return invert;
  }
  
  public void setInvert(boolean invert) {
    this.invert = invert;
  }
  
  public boolean getMixedCase() {
    return mixedCase;
  }
  
  public void setMixedCase(boolean mixedCase) {
    this.mixedCase = mixedCase;
  }
  
  public String getName() {
    return name;
  }
  
  public void setName(String name) {
    this.name = name;
  }
  
  public String getParam1() {
    return param1;
  }
  
  public void setParam1(String param1) {
    this.param1 = param1;
  }
  
  public String getPiece() {
    return piece;
  }
  
  public void setPiece(String piece) {
    this.piece = piece;
  }
  
  public String getQualifier() {
    return qualifier;
  }
  
  public void setQualifier(String qualifier) {
    this.qualifier = qualifier;
  }
  
  public String getRpc() {
    return rpc;
  }
  
  public void setRpc(String rpc) {
    this.rpc = rpc;
  }
  
  public String getStatus() {
    return status;
  }
  
  public void setStatus(String status) {
    this.status = status;
  }
  
  public String getTabPos() {
    return tabPos;
  }
  
  public void setTabPos(String tabPos) {
    this.tabPos = tabPos;
  }
  
  public String getTextColor() {
    return textColor;
  }
  
  public void setTextColor(String textColor) {
    this.textColor = textColor;
  }
}
