package gov.va.med.lom.vistabroker.patient.data;

import java.io.Serializable;
import java.util.List;

public class AllergiesReactants implements Serializable {
  
  private List<AllergyReactant> allergiesReactants;
  private boolean noKnownAllergies;
  private boolean noAllergyAssessment;
  
  public AllergiesReactants() {
    this.allergiesReactants = null;
    this.noKnownAllergies = false;
    this.noAllergyAssessment = false;
  }
  
  public List<AllergyReactant> getAllergiesReactants() {
    return allergiesReactants;
  }
  
  public void setAllergiesReactants(List<AllergyReactant> allergiesReactants) {
    this.allergiesReactants = allergiesReactants;
  }
  
  public boolean getNoAllergyAssessment() {
    return noAllergyAssessment;
  }
  
  public void setNoAllergyAssessment(boolean noAllergyAssessment) {
    this.noAllergyAssessment = noAllergyAssessment;
  }
  
  public boolean getNoKnownAllergies() {
    return noKnownAllergies;
  }
  
  public void setNoKnownAllergies(boolean noKnownAllergies) {
    this.noKnownAllergies = noKnownAllergies;
  }
}
