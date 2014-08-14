package gov.va.med.lom.avs.model;

import java.util.List;

public class RemoteAllergy {

  private String allergenReactant;
  private String severity;
  private String site;
  private String stationNo;
  private String type;
  private List<String> reactionsSymptoms;
  
  public String getAllergenReactant() {
    return allergenReactant;
  }
  public void setAllergenReactant(String allergenReactant) {
    this.allergenReactant = allergenReactant;
  }
  public String getSeverity() {
    return severity;
  }
  public void setSeverity(String severity) {
    this.severity = severity;
  }
  public List<String> getReactionsSymptoms() {
    return reactionsSymptoms;
  }
  public void setReactionsSymptoms(List<String> reactionsSymptoms) {
    this.reactionsSymptoms = reactionsSymptoms;
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
  
}
