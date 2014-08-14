package gov.va.med.lom.avs.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="ckoRole")
public class Role extends BaseModel implements Serializable {

  private String facilityNo;
  private String name;
  private String description;
  
  public String getName() {
    return name;
  }
  public void setName(String name) {
    this.name = name;
  }
  public String getDescription() {
    return description;
  }
  public void setDescription(String description) {
    this.description = description;
  }
  public String getFacilityNo() {
    return facilityNo;
  }
  public void setFacilityNo(String facilityNo) {
    this.facilityNo = facilityNo;
  }
  
}
