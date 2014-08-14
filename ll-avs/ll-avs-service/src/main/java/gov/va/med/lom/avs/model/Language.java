package gov.va.med.lom.avs.model;

import java.io.Serializable;
import javax.persistence.*;

@Entity
@Table(name="ckoLanguage")
public class Language extends BaseModel implements Serializable  {

  private String name;
  private String abbreviation;
  
  public String getName() {
    return name;
  }
  public void setName(String name) {
    this.name = name;
  }
  public String getAbbreviation() {
    return abbreviation;
  }
  public void setAbbreviation(String abbreviation) {
    this.abbreviation = abbreviation;
  }
  
}
