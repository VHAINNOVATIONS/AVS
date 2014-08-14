package gov.va.med.lom.javaBroker.rpc.lists.models;

import gov.va.med.lom.javaBroker.rpc.BaseBean;

import java.io.Serializable;

public class OrderItemsList extends BaseBean implements Serializable {

  private OrderItem[] orderItems;
  
  public OrderItemsList() {
    this.orderItems = null;
  }

  public OrderItem[] getOrderItems() {
    return orderItems;
  }
  
  public void setOrderItems(OrderItem[] orderItems) {
    this.orderItems = orderItems;
  }
}
