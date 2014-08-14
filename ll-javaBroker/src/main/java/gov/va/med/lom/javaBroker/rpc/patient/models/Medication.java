package gov.va.med.lom.javaBroker.rpc.patient.models;

import gov.va.med.lom.javaBroker.rpc.BaseBean;

import java.io.Serializable;
import java.util.Date;

public class Medication extends BaseBean implements Serializable {
  
  private String type;
  private String pharmId;
  private String orderId;
  private String infRate;
  private String name;
  private String status;
  private Date dateExpires;
  private String dateExpiresStr;
  private String dateLastFilledStr;
  private Date dateLastFilled;
  private int refills;
  private String totalDose;
  private String unitDose;
  private String sig;
  
  public Medication() {
    this.type = null;
    this.pharmId = null;
    this.orderId = null;
    this.infRate = null;
    this.name = null;
    this.status = null;
    this.dateExpires = null;
    this.dateExpiresStr = null;
    this.dateLastFilledStr = null;
    this.dateLastFilled = null;
    this.refills = 0;
    this.totalDose = null;
    this.unitDose = null;
    this.sig = null;
  }
  
  public String getType() {
    return type;
  }
  
  public void setType(String type) {
    this.type = type;
  }
  
  public String getName() {
    return name;
  }
  
  public void setName(String name) {
    this.name = name;
  }
  
  public int getRefills() {
    return refills;
  }
  
  public void setRefills(int refills) {
    this.refills = refills;
  }
  
  public String getSig() {
    return sig;
  }
  
  public void setSig(String sig) {
    this.sig = sig;
  }
  
  public String getStatus() {
    return status;
  }
  
  public void setStatus(String status) {
    this.status = status;
  }
  
  public Date getDateExpires() {
    return dateExpires;
  }

  public void setDateExpires(Date dateExpires) {
    this.dateExpires = dateExpires;
  }
  
  public String getDateExpiresStr() {
    return dateExpiresStr;
  }
  
  public void setDateExpiresStr(String expires) {
    this.dateExpiresStr = expires;
  }
  
  public Date getDateLastFilled() {
    return dateLastFilled;
  }

  public void setDateLastFilled(Date dateLastFilled) {
    this.dateLastFilled = dateLastFilled;
  }
  
  public String getDateLastFilledStr() {
    return dateLastFilledStr;
  }
  
  public void setDateLastFilledStr(String lastFilled) {
    this.dateLastFilledStr = lastFilled;
  }
  
  public String getInfRate() {
    return infRate;
  }

  public void setInfRate(String infRate) {
    this.infRate = infRate;
  }
  
  public String getOrderId() {
    return orderId;
  }
  
  public void setOrderId(String orderId) {
    this.orderId = orderId;
  }
  
  public String getPharmId() {
    return pharmId;
  }
  
  public void setPharmId(String pharmId) {
    this.pharmId = pharmId;
  }
  
  public String getTotalDose() {
    return totalDose;
  }
  
  public void setTotalDose(String totalDose) {
    this.totalDose = totalDose;
  }
  
  public String getUnitDose() {
    return unitDose;
  }
  
  public void setUnitDose(String unitDose) {
    this.unitDose = unitDose;
  }
}
