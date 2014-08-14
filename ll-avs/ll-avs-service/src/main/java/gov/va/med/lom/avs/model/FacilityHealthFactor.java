package gov.va.med.lom.avs.model;

import java.io.Serializable;
import javax.persistence.*;

@Entity
@Table(name="ckoHealthFactors")
public class FacilityHealthFactor extends BaseModel implements Serializable  {

  private String stationNo;
  private String type;
  private String ien;
  private String value;
  
  public String getStationNo() {
    return stationNo;
  }
  public void setStationNo(String stationNo) {
    this.stationNo = stationNo;
  }
  public String getType() {
    return type;
  }
  public void setType(String type) {
    this.type = type;
  }
  public String getIen() {
    return ien;
  }
  public void setIen(String ien) {
    this.ien = ien;
  }
  public String getValue() {
    return value;
  }
  public void setValue(String value) {
    this.value = value;
  }
  
}
