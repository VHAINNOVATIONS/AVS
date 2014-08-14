package gov.va.med.lom.avs.model;

public class PatientInformation {

  private String name;
  private String dob;
  private int age;
  private String smokingStatus;
  private String smokingStatusDate;
  private String smokingStatusCode;
  private String preferredLanguage;
  
  public String getName() {
    return name;
  }
  public void setName(String name) {
    this.name = name;
  }
  public String getDob() {
    return dob;
  }
  public void setDob(String dob) {
    this.dob = dob;
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
  public String getPreferredLanguage() {
    return preferredLanguage;
  }
  public void setPreferredLanguage(String preferredLanguage) {
    this.preferredLanguage = preferredLanguage;
  }
  public int getAge() {
    return age;
  }
  public void setAge(int age) {
    this.age = age;
  }

}
