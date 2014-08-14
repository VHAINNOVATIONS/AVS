package gov.va.med.lom.javaBroker.rpc.lists.models;

import gov.va.med.lom.javaBroker.rpc.BaseBean;

import java.io.Serializable;

public class CptCode extends BaseBean implements Serializable {

  private String code;
  private String description;
  private String rpcResult;
  
  public CptCode() {
    this.code = null;
    this.description = null;
    this.rpcResult = null;
  }
  
  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }
  
  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getRpcResult() {
    return rpcResult;
  }

  public void setRpcResult(String rpcResult) {
    this.rpcResult = rpcResult;
  }
  
}
