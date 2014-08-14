package gov.va.med.lom.avs.client.model;

import java.io.Serializable;
import java.util.List;

public class AllergyJson implements Serializable {

  private String allergen;
  private List<String> reactions;
  private String severity;
  private String site;
  private String stationNo;
  private String type;
  private String verifiedDate;
  
  public String getAllergen() {
    return allergen;
  }
  public void setAllergen(String allergen) {
    this.allergen = allergen;
  }
  public List<String> getReactions() {
    return reactions;
  }
  public void setReactions(List<String> reactions) {
    this.reactions = reactions;
  }
  public String getSeverity() {
    return severity;
  }
  public void setSeverity(String severity) {
    this.severity = severity;
  }
  public String getSite() {
    return site;
  }
  public void setSite(String site) {
    this.site = site;
  }
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
  public String getVerifiedDate() {
    return verifiedDate;
  }
  public void setVerifiedDate(String verifiedDate) {
    this.verifiedDate = verifiedDate;
  }
  
}
