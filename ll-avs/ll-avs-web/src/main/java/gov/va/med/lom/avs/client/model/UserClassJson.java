package gov.va.med.lom.avs.client.model;

public class UserClassJson {

  private String ien;
  private String userClass;
  
  public UserClassJson() {}
  
  public UserClassJson(String ien, String userClass) {
    this.ien = ien;
    this.userClass = userClass;
  }
  
  public String getIen() {
    return ien;
  }
  public void setIen(String ien) {
    this.ien = ien;
  }
  public String getUserClass() {
    return userClass;
  }
  public void setUserClass(String userClass) {
    this.userClass = userClass;
  }

}