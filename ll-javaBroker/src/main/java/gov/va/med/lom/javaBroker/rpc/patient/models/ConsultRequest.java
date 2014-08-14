package gov.va.med.lom.javaBroker.rpc.patient.models;

import gov.va.med.lom.javaBroker.rpc.BaseBean;

import java.io.Serializable;
import java.util.Date;

public class ConsultRequest extends BaseBean implements Serializable {
  
  private Date datetime;
  private String datetimeStr;
  private String status;
  private String service;
  private String text;
  private String consultNumber;
  private String parentId;
  private String type;
  private String typeStr;
  
  public ConsultRequest() {
    this.datetime = null;
    this.datetimeStr = null;
    this.status = null;
    this.service = null;
    this.text = null;
    this.consultNumber = null;
    this.parentId = null;
    this.type = null; 
    this.typeStr = null;
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
  
  public String getConsultNumber() {
    return consultNumber;
  }
  
  public void setConsultNumber(String consultNumber) {
    this.consultNumber = consultNumber;
  }
  
  public String getParentId() {
    return parentId;
  }
  
  public void setParentId(String parentId) {
    this.parentId = parentId;
  }
  
  public String getText() {
    return text;
  }
  
  public void setText(String text) {
    this.text = text;
  }
  
  public String getType() {
    return type;
  }
  
  public void setType(String type) {
    this.type = type;
  }
  
  public String getTypeStr() {
    return typeStr;
  }
  
  public void setTypeStr(String typeStr) {
    this.typeStr = typeStr;
  }
}
