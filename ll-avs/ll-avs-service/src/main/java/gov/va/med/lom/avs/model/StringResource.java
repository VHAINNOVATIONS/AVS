package gov.va.med.lom.avs.model;

import java.io.Serializable;
import javax.persistence.*;

@Entity
@Table(name="ckoStringResources")
public class StringResource extends BaseModel implements Serializable  {

  private String stationNo;
  private Language language;
  private String name;
  private String value;
  
  public String getStationNo() {
    return stationNo;
  }
  public void setStationNo(String stationNo) {
    this.stationNo = stationNo;
  }
  public String getValue() {
    return value;
  }
  public void setValue(String value) {
    this.value = value;
  }
  @ManyToOne
  @JoinColumn(name="languageId") 
  public Language getLanguage() {
    return language;
  }
  public void setLanguage(Language language) {
    this.language = language;
  }
  public String getName() {
    return name;
  }
  public void setName(String name) {
    this.name = name;
  }
 
}
