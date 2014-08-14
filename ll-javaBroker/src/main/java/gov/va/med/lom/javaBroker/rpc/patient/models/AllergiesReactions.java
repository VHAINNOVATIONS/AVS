package gov.va.med.lom.javaBroker.rpc.patient.models;

import gov.va.med.lom.javaBroker.rpc.BaseBean;

import java.io.Serializable;

public class AllergiesReactions extends BaseBean implements Serializable {
  
  private AllergyReaction[] allergiesReactions;
  private boolean noKnownAllergies;
  private boolean noAllergyAssessment;
  
  public AllergiesReactions() {
    this.allergiesReactions = null;
    this.noKnownAllergies = false;
    this.noAllergyAssessment = false;
  }
  
  public AllergyReaction[] getAllergiesReactions() {
    return allergiesReactions;
  }
  
  public void setAllergiesReactions(AllergyReaction[] allergiesReactions) {
    this.allergiesReactions = allergiesReactions;
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
