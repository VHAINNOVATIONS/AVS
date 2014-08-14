package gov.va.med.lom.vistabroker.patient.data;

import java.io.Serializable;

public class AllergyReactant implements Serializable {
  
  private String dfn;
  private String ien;
  private String allergenReactant;
  private String[] reactionsSymptoms;
  private String severity;
  
  public AllergyReactant() {
    this.dfn = null;
    this.ien = null;
    this.allergenReactant = null;
    this.reactionsSymptoms = null;
    this.severity = null;
  }
  
  public String getDfn() {
    return dfn;
  }

  public void setDfn(String dfn) {
    this.dfn = dfn;
  }

  public String getIen() {
    return ien;
  }

  public void setIen(String ien) {
    this.ien = ien;
  }

  public String getAllergenReactant() {
    return allergenReactant;
  }

  public void setAllergenReactant(String allergy) {
    this.allergenReactant = allergy;
  }
  
  public String[] getReactionsSymptoms() {
    return reactionsSymptoms;
  }
  
  public void setReactionsSymptoms(String[] reactionsSymptoms) {
    this.reactionsSymptoms = reactionsSymptoms;
  }
  
  public String getSeverity() {
    return severity;
  }

  public void setSeverity(String severity) {
    this.severity = severity;
  }
  
}
