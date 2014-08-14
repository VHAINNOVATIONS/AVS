package gov.va.med.lom.javaBroker.rpc.patient.models;

import gov.va.med.lom.javaBroker.rpc.BaseBean;

import java.io.Serializable;
import java.util.Date;

public class Demographics extends BaseBean implements Serializable {

  private String name;
  private String sex;
  private boolean sensitive;
  private boolean restricted;
  private String icn;
  private String ssn;
  private Date dob;
  private String dobStr;
  private Date deceasedDate;
  private String deceasedDateStr;
  private int age;
  private String locationIen;
  private String location;
  private String specialty;
  private String roomBed;
  private String specialtyIen;
  private String cwad;
  private Date admitTime;
  private String admitTimeStr;
  private boolean serviceConnected;
  private int serviceConnectedPercent;
  private String primaryTeam;
  private String primaryProvider;
  private String attending;
  
  public Demographics() {
    this.name = null;
    this.sex = null;
    this.sensitive = false;
    this.restricted = false;
    this.icn = null;
    this.ssn = null;
    this.dob = null;
    this.dobStr = null;
    this.deceasedDate = null;
    this.deceasedDateStr = null;
    this.age = 0;
    this.locationIen = null;
    this.location = null;
    this.specialty = null;
    this.roomBed = null;
    this.specialtyIen = null;
    this.cwad = null;
    this.admitTime = null;
    this.admitTimeStr = null;
    this.serviceConnected = false;
    this.serviceConnectedPercent = 0;
    this.primaryTeam = null;
    this.primaryProvider = null;
    this.attending = null;    
  }
  
  public Date getAdmitTime() {
    return admitTime;
  }
  
  public void setAdmitTime(Date admitTime) {
    this.admitTime = admitTime;
  }
  
  public int getAge() {
    return age;
  }
  
  public void setAge(int age) {
    this.age = age;
  }
  
  public String getAttending() {
    return attending;
  }
  
  public void setAttending(String attending) {
    this.attending = attending;
  }
  
  public String getCwad() {
    return cwad;
  }
  
  public void setCwad(String cwad) {
    this.cwad = cwad;
  }
  
  public Date getDeceasedDate() {
    return deceasedDate;
  }
  
  public void setDeceasedDate(Date deceasedDate) {
    this.deceasedDate = deceasedDate;
  }
  
  public Date getDob() {
    return dob;
  }
  
  public void setDob(Date dob) {
    this.dob = dob;
  }
  
  public String getIcn() {
    return icn;
  }
  
  public void setIcn(String icn) {
    this.icn = icn;
  }
  
  public String getLocation() {
    return location;
  }
  
  public void setLocation(String location) {
    this.location = location;
  }
  
  public String getLocationIen() {
    return locationIen;
  }
  
  public void setLocationIen(String locationIem) {
    this.locationIen = locationIem;
  }
  
  public String getName() {
    return name;
  }
  
  public void setName(String name) {
    this.name = name;
  }
  
  public String getPrimaryProvider() {
    return primaryProvider;
  }
  
  public void setPrimaryProvider(String primaryProvider) {
    this.primaryProvider = primaryProvider;
  }
  
  public String getPrimaryTeam() {
    return primaryTeam;
  }
  
  public void setPrimaryTeam(String primaryTeam) {
    this.primaryTeam = primaryTeam;
  }
  
  public boolean getRestricted() {
    return restricted;
  }
  
  public void setRestricted(boolean restricted) {
    this.restricted = restricted;
  }
  
  public String getRoomBed() {
    return roomBed;
  }
  
  public void setRoomBed(String roomBed) {
    this.roomBed = roomBed;
  }
  
  public boolean getSensitive() {
    return sensitive;
  }
  
  public void setSensitive(boolean sensitive) {
    this.sensitive = sensitive;
  }
  
  public boolean getServiceConnected() {
    return serviceConnected;
  }
  
  public void setServiceConnected(boolean serviceConnected) {
    this.serviceConnected = serviceConnected;
  }
  
  public int getServiceConnectedPercent() {
    return serviceConnectedPercent;
  }
  
  public void setServiceConnectedPercent(int serviceConnectedPercent) {
    this.serviceConnectedPercent = serviceConnectedPercent;
  }
  
  public String getSex() {
    return sex;
  }
  
  public void setSex(String sex) {
    this.sex = sex;
  }
  
  public String getSpecialtyIen() {
    return specialtyIen;
  }
  
  public void setSpecialtyIen(String specialtyIen) {
    this.specialtyIen = specialtyIen;
  }
  
  public String getSsn() {
    return ssn;
  }
  
  public void setSsn(String ssn) {
    this.ssn = ssn;
  }
  
  public String getSpecialty() {
    return specialty;
  }
  
  public void setSpecialty(String specialty) {
    this.specialty = specialty;
  }
  
  public String getAdmitTimeStr() {
    return admitTimeStr;
  }

  public void setAdmitTimeStr(String admitTimeStr) {
    this.admitTimeStr = admitTimeStr;
  }
  
  public String getDeceasedDateStr() {
    return deceasedDateStr;
  }
  
  public void setDeceasedDateStr(String deceasedDateStr) {
    this.deceasedDateStr = deceasedDateStr;
  }
  
  public String getDobStr() {
    return dobStr;
  }
  
  public void setDobStr(String dobStr) {
    this.dobStr = dobStr;
  }
}
