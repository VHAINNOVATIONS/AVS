package gov.va.med.lom.avs.client.model;

public class DivisionJson {

  private String insitutionId;
  private String facilityNo;
  private String name;
  private boolean isDefault;
  
  public DivisionJson(String institutionId, String facilityNo, String name, boolean isDefault) {
    this.insitutionId = institutionId;
    this.facilityNo = facilityNo;
    this.name = name;
    this.isDefault = isDefault;
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

  public boolean getIsDefault() {
    return isDefault;
  }

  public void setIsDefault(boolean isDefault) {
    this.isDefault = isDefault;
  }

}
