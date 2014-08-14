package gov.va.med.lom.javaBroker.rpc.user.models;

import gov.va.med.lom.javaBroker.rpc.BaseBean;

import java.io.Serializable;
import java.util.Date;

public class Notification extends BaseBean implements Serializable {
  
  private String info;
  private String patient;
  private String location;
  private String urgency;
  private Date alertDateTime;
  private String alertDateTimeStr;
  private String text;
  private String forwardingInfo;
  private String xqaid;
  private String comment;
  private String orderType;
  private String orderInfo;
  private int followUp;
  private int recordId;
  
  public Notification() {
    this.info = null;
    this.patient = null;
    this.location = null;
    this.urgency = null;
    this.alertDateTime = null;
    this.alertDateTimeStr = null;
    this.text = null;
    this.forwardingInfo = null;
    this.xqaid = null;
    this.comment = null;
    this.orderType = null;
    this.orderInfo = null;
    this.followUp = 0;
    this.recordId = 0;
  }
  
  public Date getAlertDateTime() {
    return alertDateTime;
  }

  public void setAlertDateTime(Date alertDateTime) {
    this.alertDateTime = alertDateTime;
  }
  
  public String getAlertDateTimeStr() {
    return alertDateTimeStr;
  }
  
  public void setAlertDateTimeStr(String alertDateTime) {
    this.alertDateTimeStr = alertDateTime;
  }
  
  public String getComment() {
    return comment;
  }
  
  public void setComment(String comment) {
    this.comment = comment;
  }
  
  public int getFollowUp() {
    return followUp;
  }
  
  public void setFollowUp(int followUp) {
    this.followUp = followUp;
  }
  
  public String getForwardingInfo() {
    return forwardingInfo;
  }
  
  public void setForwardingInfo(String forwardingInfo) {
    this.forwardingInfo = forwardingInfo;
  }
  
  public String getInfo() {
    return info;
  }
  
  public void setInfo(String info) {
    this.info = info;
  }
  
  public String getLocation() {
    return location;
  }
  
  public void setLocation(String location) {
    this.location = location;
  }
  
  public String getPatient() {
    return patient;
  }
  
  public void setPatient(String patient) {
    this.patient = patient;
  }

  public int getRecordId() {
    return recordId;
  }
  
  public void setRecordId(int recordId) {
    this.recordId = recordId;
  }
  
  public String getText() {
    return text;
  }
  
  public void setText(String text) {
    this.text = text;
  }
  
  public String getUrgency() {
    return urgency;
  }
  
  public void setUrgency(String urgency) {
    this.urgency = urgency;
  }
  
  public String getXqaid() {
    return xqaid;
  }
  
  public void setXqaid(String xqaid) {
    this.xqaid = xqaid;
  }
  
  public String getOrderType() {
    return orderType;
  }
  
  public void setOrderType(String orderType) {
    this.orderType = orderType;
  }  
  
  public String getOrderInfo() {
    return orderInfo;
  }
  
  public void setOrderInfo(String orderInfo) {
    this.orderInfo = orderInfo;
  }
}
