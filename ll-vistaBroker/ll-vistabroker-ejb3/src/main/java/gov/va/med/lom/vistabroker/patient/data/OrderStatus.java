package gov.va.med.lom.vistabroker.patient.data;

import java.io.Serializable;
import java.util.Date;

public class OrderStatus implements Serializable {
  
  private String orderName;
  private String orderedBy;
  private String orderDatetimeStr;
  private Date orderDatetime;
  private String collectionDatetimeStr;
  private Date collectionDatetime;
  private String signedBy;
  private String status;
  private String urgency;
  
  public OrderStatus() {
    this.orderName = null;
    this.orderedBy = null;
    this.orderDatetimeStr = null;
    this.orderDatetime = null;
    this.collectionDatetimeStr = null;
    this.collectionDatetime = null;
    this.signedBy = null;
    this.status = null;
    this.urgency = null;
  }
  
  public Date getCollectionDatetime() {
    return collectionDatetime;
  }

  public void setCollectionDatetime(Date collectionDatetime) {
    this.collectionDatetime = collectionDatetime;
  }
  
  public Date getOrderDatetime() {
    return orderDatetime;
  }
  
  public void setOrderDatetime(Date orderDatetime) {
    this.orderDatetime = orderDatetime;
  }
  public String getCollectionDatetimeStr() {
    return collectionDatetimeStr;
  }
  
  public void setCollectionDatetimeStr(String collectionDateTime) {
    this.collectionDatetimeStr = collectionDateTime;
  }
  
  public String getOrderDatetimeStr() {
    return orderDatetimeStr;
  }
  
  public void setOrderDatetimeStr(String orderDateTime) {
    this.orderDatetimeStr = orderDateTime;
  }
  
  public String getOrderedBy() {
    return orderedBy;
  }
  
  public void setOrderedBy(String orderedBy) {
    this.orderedBy = orderedBy;
  }
  
  public String getOrderName() {
    return orderName;
  }
  
  public void setOrderName(String orderName) {
    this.orderName = orderName;
  }
  
  public String getSignedBy() {
    return signedBy;
  }
  
  public void setSignedBy(String signedBy) {
    this.signedBy = signedBy;
  }
  
  public String getStatus() {
    return status;
  }
  
  public void setStatus(String status) {
    this.status = status;
  }
   
  public String getUrgency() {
    return urgency;
  }

  public void setUrgency(String urgency) {
    this.urgency = urgency;
  }
}
