package gov.va.med.lom.avs.client.model;

import java.io.Serializable;

public class LanguageJson implements Serializable  {

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
