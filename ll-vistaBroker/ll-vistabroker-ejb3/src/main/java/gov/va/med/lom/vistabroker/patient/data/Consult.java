package gov.va.med.lom.vistabroker.patient.data;

import java.io.Serializable;
import java.util.Date;

public class Consult implements Serializable {
  
  private String ien;
  private Date datetime;
  private String datetimeStr;
  private String status;
  private String text;
  private int num;
  private String orderIfn;
  private String parentIen;
  private String type;
  private String service;
  
  public Consult() {
    this.ien = null;
    this.datetime = null;
    this.datetimeStr = null;
    this.status = null;
    this.service = null;
    this.text = null;
    this.num = 0;
    this.orderIfn = null;
    this.parentIen = null;
    this.type = null;
    this.service = null;
  }
  
  public String getIen() {
    return ien;
  }

  public void setIen(String ien) {
    this.ien = ien;
  }

  public Date getDatetime() {
    return datetime;
  }

  public void setDatetime(Date datetime) {
    this.datetime = datetime;
  }
  
  public String getDatetimeStr() {
    return datetimeStr;
  }

  public void setDatetimeStr(String dateTime) {
    this.datetimeStr = dateTime;
  }
  
  public String getText() {
    return text;
  }
  
  public void setText(String procedure) {
    this.text = procedure;
  }
  
  public String getService() {
    return service;
  }
  
  public void setService(String service) {
    this.service = service;
  }
  
  public String getStatus() {
    return status;
  }
  
  public void setStatus(String status) {
    this.status = status;
  }
  
  public int getNum() {
    return num;
  }

  public void setNum(int num) {
    this.num = num;
  }
  
  public String getOrderIfn() {
    return orderIfn;
  }
  
  public void setOrderIfn(String orderIfn) {
    this.orderIfn = orderIfn;
  }
  
  public String getParentIen() {
    return parentIen;
  }
  
  public void setParentIen(String parentIen) {
    this.parentIen = parentIen;
  }
  
  public String getType() {
    return type;
  }
  
  public void setType(String type) {
    this.type = type;
  }
}
