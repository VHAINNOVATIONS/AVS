package gov.va.med.lom.vistabroker.patient.data;

import java.io.Serializable;
import java.util.Date;

public class PatientInfo implements Serializable {
  
  private String dfn;
  private String name;
  private String sex;
  private String ssn;
  private Date dob;
  private String dobStr;
  private Date deceasedDate;
  private String deceasedDateStr;
  private int age;
  private boolean veteran;
  private int scPct;
  private String location;
  private String roomBed;
  private boolean inpatient;
  
  public PatientInfo() {
    this.dfn = null;
    this.name = null;
    this.sex = null;
    this.ssn = null;
    this.dob = null;
    this.dobStr = null;
    this.deceasedDate = null;
    this.deceasedDateStr = null;
    this.age = 0;    
    this.veteran = false;
    this.scPct = 0;
    this.location = null;
    this.roomBed = null;
    this.inpatient = false;
  }

  public String getDfn() {
    return dfn;
  }

  public void setDfn(String dfn) {
    this.dfn = dfn;
  }

  public int getAge() {
    return age;
  }
  
  public void setAge(int age) {
    this.age = age;
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
  
  public String getName() {
    return name;
  }
  
  public void setName(String name) {
    this.name = name;
  }
  
  public String getSex() {
    return sex;
  }
  
  public void setSex(String sex) {
    this.sex = sex;
  }
  
  public String getSsn() {
    return ssn;
  }
  
  public void setSsn(String ssn) {
    this.ssn = ssn;
  }
  
  public String getLocation() {
    return location;
  }
  
  public void setLocation(String location) {
    this.location = location;
  }
  
  public String getRoomBed() {
    return roomBed;
  }
  
  public void setRoomBed(String roomBed) {
    this.roomBed = roomBed;
  }
  
  public int getScPct() {
    return scPct;
  }
  
  public void setScPct(int scPct) {
    this.scPct = scPct;
  }
  
  public boolean getVeteran() {
    return veteran;
  }
  
  public void setVeteran(boolean veteran) {
    this.veteran = veteran;
  }
  
  public boolean getInpatient() {
    return inpatient;
  }
  
  public void setInpatient(boolean inpatient) {
    this.inpatient = inpatient;
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
