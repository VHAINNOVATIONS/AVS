package gov.va.med.lom.avs.client.model;

public class PatientInfoJson {

  private String dfn;
  private String name;
  private String sex;
  private String ssn;
  private String dob;
  private String deceasedDate;
  private int age;
  private boolean veteran;
  private int scPct;
  private String location;
  private String roomBed;
  private boolean inpatient;
  private String smokingStatus;
  private String smokingStatusDate;
  private String smokingStatusCode;
  private String languagePreference;
  
  public String getDfn() {
    return dfn;
  }
  public void setDfn(String dfn) {
    this.dfn = dfn;
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
  public String getDob() {
    return dob;
  }
  public void setDob(String dob) {
    this.dob = dob;
  }
  public String getDeceasedDate() {
    return deceasedDate;
  }
  public void setDeceasedDate(String deceasedDate) {
    this.deceasedDate = deceasedDate;
  }
  public int getAge() {
    return age;
  }
  public void setAge(int age) {
    this.age = age;
  }
  public boolean isVeteran() {
    return veteran;
  }
  public void setVeteran(boolean veteran) {
    this.veteran = veteran;
  }
  public int getScPct() {
    return scPct;
  }
  public void setScPct(int scPct) {
    this.scPct = scPct;
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
  public boolean isInpatient() {
    return inpatient;
  }
  public void setInpatient(boolean inpatient) {
    this.inpatient = inpatient;
  }
  public String getSmokingStatus() {
    return smokingStatus;
  }
  public void setSmokingStatus(String smokingStatus) {
    this.smokingStatus = smokingStatus;
  }
  public String getSmokingStatusDate() {
    return smokingStatusDate;
  }
  public void setSmokingStatusDate(String smokingStatusDate) {
    this.smokingStatusDate = smokingStatusDate;
  }
  public String getSmokingStatusCode() {
    return smokingStatusCode;
  }
  public void setSmokingStatusCode(String smokingStatusCode) {
    this.smokingStatusCode = smokingStatusCode;
  }
  public String getLanguagePreference() {
    return languagePreference;
  }
  public void setLanguagePreference(String languagePreference) {
    this.languagePreference = languagePreference;
  }
  
}
