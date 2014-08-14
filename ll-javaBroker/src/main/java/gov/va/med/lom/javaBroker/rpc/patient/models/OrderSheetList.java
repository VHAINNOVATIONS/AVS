package gov.va.med.lom.javaBroker.rpc.patient.models;

import java.io.Serializable;

import gov.va.med.lom.javaBroker.rpc.BaseBean;

public class OrderSheetList extends BaseBean implements Serializable {

  private OrderSheet[] orderSheets;
  
  public OrderSheetList() {
    this.orderSheets = null;
  }
  
  public OrderSheet[] getOrderSheets() {
    return orderSheets;
  }
  
  public void setOrderSheets(OrderSheet[] orderSheets) {
    this.orderSheets = orderSheets;
  }
}
