package gov.va.med.lom.vistabroker.patient.data;

import java.io.Serializable;

public class ClinicalProcedure implements Serializable {

  private String dfn;
  private String orderNumber;
  private String tiuNoteIen;
  private String consultIen;
  private String orderIen;
  private String rpcResult;
  
  public ClinicalProcedure() {
    this.dfn = null;
    this.orderNumber = null;
    this.tiuNoteIen = null;
    this.consultIen = null;
    this.orderIen = null;
    this.rpcResult = null;
  }
  
  public String getDfn() {
    return dfn;
  }

  public void setDfn(String dfn) {
    this.dfn = dfn;
  }

  public String getConsultIen() {
    return consultIen;
  }

  public void setConsultIen(String consultIen) {
    this.consultIen = consultIen;
  }
  
  public String getOrderIen() {
    return orderIen;
  }
  
  public void setOrderIen(String orderIen) {
    this.orderIen = orderIen;
  }
  
  public String getOrderNumber() {
    return orderNumber;
  }

  public void setOrderNumber(String orderNumber) {
    this.orderNumber = orderNumber;
  }
  
  public String getTiuNoteIen() {
    return tiuNoteIen;
  }
  
  public void setTiuNoteIen(String tiuNoteIen) {
    this.tiuNoteIen = tiuNoteIen;
  }

  public String getRpcResult() {
    return rpcResult;
  }

  public void setRpcResult(String rpcResult) {
    this.rpcResult = rpcResult;
  }
  
}
