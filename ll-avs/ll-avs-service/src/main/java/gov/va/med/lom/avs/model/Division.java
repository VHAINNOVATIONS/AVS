package gov.va.med.lom.avs.model;

public class Division {

  private String insitutionId;
  private String facilityNo;
  private String name;
  
  public Division(String institutionId, String facilityNo, String name) {
    this.insitutionId = institutionId;
    this.facilityNo = facilityNo;
    this.name = name;
  }
  
  public String getName() {
    return name;
  }
  public void setName(String name) {
    this.name = name;
  }

  public String getInsitutionId() {
    return insitutionId;
  }

  public void setInsitutionId(String insitutionId) {
    this.insitutionId = insitutionId;
  }

  public String getFacilityNo() {
    return facilityNo;
  }

  public void setFacilityNo(String facilityNo) {
    this.facilityNo = facilityNo;
  }
  
}
