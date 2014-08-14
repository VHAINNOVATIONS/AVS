package gov.va.med.lom.javaBroker.rpc.patient.models;

import gov.va.med.lom.javaBroker.rpc.BaseBean;

import java.io.Serializable;

public class AllergyReaction extends BaseBean implements Serializable {
  
  private String allergy;
  private String[] reactionsSymptoms;
  private String severity;
  
  public AllergyReaction() {
    this.allergy = null;
    this.reactionsSymptoms = null;
    this.severity = null;
  }
  
  public String getAllergy() {
    return allergy;
  }

  public void setAllergy(String allergy) {
    this.allergy = allergy;
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
