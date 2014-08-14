package gov.va.med.lom.javaBroker.rpc.patient.models;

import gov.va.med.lom.javaBroker.rpc.BaseBean;

import java.io.Serializable;

public class OrderDialogPrompt extends BaseBean implements Serializable {

  private String id;
  private double sequence;
  private String fmtCode;
  private String omit;
  private String leading;
  private String trailing;
  private boolean newLine;
  private boolean wrapWp;
  private String children;
  private boolean isChild;
  public String getId() {
    return id;
  }
  public void setId(String id) {
    this.id = id;
  }
  public double getSequence() {
    return sequence;
  }
  public void setSequence(double sequence) {
    this.sequence = sequence;
  }
  public String getFmtCode() {
    return fmtCode;
  }
  public void setFmtCode(String fmtCode) {
    this.fmtCode = fmtCode;
  }
  public String getOmit() {
    return omit;
  }
  public void setOmit(String omit) {
    this.omit = omit;
  }
  public String getLeading() {
    return leading;
  }
  public void setLeading(String leading) {
    this.leading = leading;
  }
  public String getTrailing() {
    return trailing;
  }
  public void setTrailing(String trailing) {
    this.trailing = trailing;
  }
  public boolean isNewLine() {
    return newLine;
  }
  public void setNewLine(boolean newLine) {
    this.newLine = newLine;
  }
  public boolean isWrapWp() {
    return wrapWp;
  }
  public void setWrapWp(boolean wrapWp) {
    this.wrapWp = wrapWp;
  }
  public String getChildren() {
    return children;
  }
  public void setChildren(String children) {
    this.children = children;
  }
  public boolean getIsChild() {
    return isChild;
  }
  public void setIsChild(boolean isChild) {
    this.isChild = isChild;
  }
  
}
