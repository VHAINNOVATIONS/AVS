package gov.va.med.lom.javaBroker.rpc.patient.models;

import gov.va.med.lom.javaBroker.rpc.BaseBean;

import java.io.Serializable;
import java.util.Date;

public class Encounter extends BaseBean implements Serializable {

  private boolean needVisit;
  private Date datetime;
  private String datetimeStr;
  private String locationIen;
  private String locationName;
  private String locationAbbr;
  private String locationText;
  private String roomBed;
  private String providerDuz;
  private String providerName;
  private String visitCat;
  private String visitStr;
  private boolean standalone;
  private int typeId;
  private String type;
  
  public Encounter() {
    this.needVisit = false;
    this.datetimeStr = null;
    this.datetime = null;
    this.locationIen = null;
    this.locationName = null;
    this.locationAbbr = null;
    this.locationText = null;
    this.roomBed = null;
    this.providerDuz = null;
    this.providerName = null;
    this.visitCat = null;
    this.visitStr = null;
    this.standalone = true;
    this.typeId = 0;
    this.type = null;
  }
  
  public String getDatetimeStr() {
    return datetimeStr;
  }

  public void setDatetimeStr(String dateTime) {
    this.datetimeStr = dateTime;
  }
  
  public Date getDatetime() {
    return datetime;
  }

  public void setDatetime(Date datetime) {
    this.datetime = datetime;
  }
  
  public String getLocationIen() {
    return locationIen;
  }
  
  public void setLocationIen(String locationIen) {
    this.locationIen = locationIen;
  }
  
  public String getLocationName() {
    return locationName;
  }
  
  public void setLocationName(String locationName) {
    this.locationName = locationName;
  }
  
  public String getLocationText() {
    return locationText;
  }
  
  public void setLocationText(String locationText) {
    this.locationText = locationText;
  }
  
  public boolean getNeedVisit() {
    return needVisit;
  }
  
  public void setNeedVisit(boolean needVisit) {
    this.needVisit = needVisit;
  }
  
  public String getProviderDuz() {
    return providerDuz;
  }
  
  public void setProviderDuz(String providerIen) {
    this.providerDuz = providerIen;
  }
  
  public String getProviderName() {
    return providerName;
  }
  
  public void setProviderName(String providerName) {
    this.providerName = providerName;
  }
  
  public String getVisitCat() {
    return visitCat;
  }
  
  public void setVisitCat(String visitCat) {
    this.visitCat = visitCat;
  }
  
  public String getVisitStr() {
    return visitStr;
  }
  
  public void setVisitStr(String visitStr) {
    this.visitStr = visitStr;
  }
  
  public String getLocationAbbr() {
    return locationAbbr;
  }
  
  public void setLocationAbbr(String locationAbbr) {
    this.locationAbbr = locationAbbr;
  }
  
  public String getRoomBed() {
    return roomBed;
  }
  
  public void setRoomBed(String roomBed) {
    this.roomBed = roomBed;
  }
  
  public boolean getStandalone() {
    return standalone;
  }
  
  public void setStandalone(boolean standalone) {
    this.standalone = standalone;
  }
  
  public String getType() {
    return type;
  }
  
  public void setType(String type) {
    this.type = type;
  }
  
  public int getTypeId() {
    return typeId;
  }
  
  public void setTypeId(int typeId) {
    this.typeId = typeId;
  }    
}
