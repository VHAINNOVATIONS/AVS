package gov.va.med.lom.vistabroker.lists.data;

import java.io.Serializable;

public class CptCode implements Serializable {

  private String ien;
  private String code;
  private String description;
  private String rpcResult;
  
  public CptCode() {
    this.ien = null;
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

  public String getIen() {
    return ien;
  }

  public void setIen(String ien) {
    this.ien = ien;
  }

  public String getRpcResult() {
    return rpcResult;
  }

  public void setRpcResult(String rpcResult) {
    this.rpcResult = rpcResult;
  }
  
}
