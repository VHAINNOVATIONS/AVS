package gov.va.med.lom.avs.client.model;

import java.io.Serializable;

public class VitalSignJson implements Serializable {

  private String type;
  private String value;
  private String date;
  private String qualifiers;
  
  public String getType() {
    return type;
  }
  public void setType(String type) {
    this.type = type;
  }
  public String getValue() {
    return value;
  }
  public void setValue(String value) {
    this.value = value;
  }
  public String getDate() {
    return date;
  }
  public void setDate(String date) {
    this.date = date;
  }
  public String getQualifiers() {
    return qualifiers;
  }
  public void setQualifiers(String qualifiers) {
    this.qualifiers = qualifiers;
  }

}
