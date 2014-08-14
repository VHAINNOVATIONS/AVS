package gov.va.med.lom.vistabroker.patient.data;

import java.io.Serializable;
import java.util.Date;

public class Encounter implements Serializable {

  private String dfn;
  private boolean needVisit;
  private double fmdatetime;
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
  private String tiuNoteIen;
  
  public String getDfn() {
    return dfn;
  }

  public void setDfn(String dfn) {
    this.dfn = dfn;
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

  public double getFmdatetime() {
    return fmdatetime;
  }

  public void setFmdatetime(double fmdatetime) {
    this.fmdatetime = fmdatetime;
  }

  public String getTiuNoteIen() {
    return tiuNoteIen;
  }

  public void setTiuNoteIen(String tiuNoteIen) {
    this.tiuNoteIen = tiuNoteIen;
  }

}
