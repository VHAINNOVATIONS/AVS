package gov.va.med.lom.vistabroker.patient.data;

import java.io.Serializable;
import java.util.Date;

public class ConsultRequest implements Serializable {
  
  private String ien;
  private String dfn;
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
    this.dfn = null;
    this.ien = null;
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
  
  public String getDfn() {
    return dfn;
  }

  public void setDfn(String dfn) {
    this.dfn = dfn;
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
