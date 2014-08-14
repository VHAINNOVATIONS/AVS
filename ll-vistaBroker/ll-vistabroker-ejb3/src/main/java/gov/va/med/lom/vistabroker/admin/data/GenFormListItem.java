package gov.va.med.lom.vistabroker.admin.data;

import java.io.Serializable;

public class GenFormListItem implements Serializable {

  private String ien;
  private String value;
  private String rpcResult;
  
  public GenFormListItem() {
    this.ien = null;
    this.value = null;
    this.rpcResult = null;
  }
  
  public String getIen() {
    return ien;
  }

  public void setIen(String ien) {
    this.ien = ien;
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  public String getRpcResult() {
    return rpcResult;
  }

  public void setRpcResult(String rpcResult) {
    this.rpcResult = rpcResult;
  }
  
}
