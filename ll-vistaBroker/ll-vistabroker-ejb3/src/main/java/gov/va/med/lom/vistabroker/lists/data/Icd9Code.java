package gov.va.med.lom.vistabroker.lists.data;

import java.io.Serializable;

public class Icd9Code implements Serializable {

  private String ien;
  private String code;
  private String diagnosis;
  private String rpcResult;
  
  public Icd9Code() {
    this.ien = null;
    this.code = null;
    this.diagnosis = null;
    this.rpcResult = null;
  }
  
  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }
  
  public String getDiagnosis() {
    return diagnosis;
  }

  public void setDiagnosis(String diagnosis) {
    this.diagnosis = diagnosis;
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
