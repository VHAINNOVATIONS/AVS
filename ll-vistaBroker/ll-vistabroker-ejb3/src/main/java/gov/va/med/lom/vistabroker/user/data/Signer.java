package gov.va.med.lom.vistabroker.user.data;

import java.io.Serializable;

public class Signer implements Serializable {

  private String duz;
  private String name;
  private String comment;
  private String rpcResult;
  
  public Signer() {
    this.duz = null;
    this.name = null;
    this.comment = null;
    this.rpcResult = null;
  }
  
  public String getDuz() {
    return duz;
  }

  public void setDuz(String duz) {
    this.duz = duz;
  }

  public String getName() {
    return name;
  }
  
  public void setName(String name) {
    this.name = name;
  }
  
  public String getComment() {
    return comment;
  }

  public void setComment(String comment) {
    this.comment = comment;
  }

  public String getRpcResult() {
    return rpcResult;
  }

  public void setRpcResult(String rpcResult) {
    this.rpcResult = rpcResult;
  }
  
}
