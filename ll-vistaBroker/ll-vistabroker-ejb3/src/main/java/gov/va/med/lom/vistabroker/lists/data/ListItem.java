package gov.va.med.lom.vistabroker.lists.data;

import java.io.Serializable;

public class ListItem implements Serializable {

  private String ien;
  private String name;
  private String value;
  private String rpcResult;
  
  public ListItem() {
    this.ien = null;
    this.name = null;
    this.value = null;
    this.rpcResult = null;
  }
  
  public String getIen() {
    return ien;
  }

  public void setIen(String ien) {
    this.ien = ien;
  }

  public String getName() {
    return name;
  }
  
  public void setName(String name) {
    this.name = name;
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
