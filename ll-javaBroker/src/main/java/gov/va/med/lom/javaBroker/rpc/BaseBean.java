package gov.va.med.lom.javaBroker.rpc;

import java.io.Serializable;

public class BaseBean implements Serializable {

  private String dfn;
  private String ien;
  private String duz;
  private String rpcResult;

  public BaseBean() {
    this.dfn = null;
    this.ien = null;
    this.duz = null;
    this.rpcResult = null;
  }

  public String getIen() {
    return ien;
  }

  public void setIen(String ien) {
    this.ien = ien;
  }
  
  public String getDfn() {
    return dfn;
  }

  public void setDfn(String dfn) {
    this.dfn = dfn;
  }  
  
  public String getDuz() {
    return duz;
  }

  public void setDuz(String duz) {
    this.duz = duz;
  }    
  
  public String getRpcResult() {
    return rpcResult;
  }
  
  public void setRpcResult(String rpcResult) {
    this.rpcResult = rpcResult;
  }  
  
}
