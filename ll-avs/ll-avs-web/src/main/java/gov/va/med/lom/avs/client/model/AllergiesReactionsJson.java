package gov.va.med.lom.avs.client.model;

import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;

public class AllergiesReactionsJson implements Serializable {

  private List<AllergyJson> allergies;
  private boolean noKnownAllergies;
  private boolean noAllergyAssessment;
  
  public AllergiesReactionsJson() {
    allergies = new ArrayList<AllergyJson>();
  }
  
  public List<AllergyJson> getAllergies() {
    return allergies;
  }
  
  public void setAllergies(List<AllergyJson> allergies) {
    this.allergies = allergies;
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
