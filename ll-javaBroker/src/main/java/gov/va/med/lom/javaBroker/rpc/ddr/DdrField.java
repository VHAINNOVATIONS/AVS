package gov.va.med.lom.javaBroker.rpc.ddr;

public class DdrField {

  private String fmNumber;
  private boolean fExternal;
  private String val;
  private String externalVal;
  
  public DdrField() {
  }
  
  public DdrField(String fmNumber, boolean fExternal) {
    this.fmNumber = fmNumber;
    this.fExternal = fExternal;
  }

  public String getFmNumber() {
    return fmNumber;
  }

  public void setFmNumber(String fmNumber) {
    this.fmNumber = fmNumber;
  }

  public boolean isFExternal() {
    return fExternal;
  }

  public void setFExternal(boolean external) {
    fExternal = external;
  }

  public String getVal() {
    return val;
  }

  public void setVal(String val) {
    this.val = val;
  }

  public String getExternalVal() {
    return externalVal;
  }

  public void setExternalVal(String externalVal) {
    this.externalVal = externalVal;
  }
  
}
