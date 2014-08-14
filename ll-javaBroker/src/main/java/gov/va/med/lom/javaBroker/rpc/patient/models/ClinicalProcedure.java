package gov.va.med.lom.javaBroker.rpc.patient.models;

import gov.va.med.lom.javaBroker.rpc.BaseBean;

import java.io.Serializable;

public class ClinicalProcedure extends BaseBean implements Serializable {

  private String orderNumber;
  private String tiuNoteIen;
  private String consultIen;
  private String orderIen;
  
  public ClinicalProcedure() {
    this.orderNumber = null;
    this.tiuNoteIen = null;
    this.consultIen = null;
    this.orderIen = null;
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
  
}
