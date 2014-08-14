package gov.va.med.lom.avs.model;

import java.util.List;
import java.io.Serializable;

import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Embedded;
import org.bson.types.ObjectId;

@Entity("remoteAllergies")
public class RemoteAllergies implements Serializable {

  @Id private ObjectId id;
  private String stationNo; 
  private String patientDfn;
  private String date;
  private boolean noKnownAllergies;
  private boolean noAllergyAssessment;
  private String stationName; 
  @Embedded private List<RemoteAllergy> allergies;
  
  public ObjectId getId() {
    return id;
  }
  public void setId(ObjectId id) {
    this.id = id;
  }
  public String getStationNo() {
    return stationNo;
  }
  public void setStationNo(String stationNo) {
    this.stationNo = stationNo;
  }
  public String getPatientDfn() {
    return patientDfn;
  }
  public void setPatientDfn(String patientDfn) {
    this.patientDfn = patientDfn;
  }
  public String getDate() {
    return date;
  }
  public void setDate(String date) {
    this.date = date;
  }
  public boolean isNoKnownAllergies() {
    return noKnownAllergies;
  }
  public void setNoKnownAllergies(boolean noKnownAllergies) {
    this.noKnownAllergies = noKnownAllergies;
  }
  public boolean isNoAllergyAssessment() {
    return noAllergyAssessment;
  }
  public void setNoAllergyAssessment(boolean noAllergyAssessment) {
    this.noAllergyAssessment = noAllergyAssessment;
  }
  public String getStationName() {
    return stationName;
  }
  public void setStationName(String stationName) {
    this.stationName = stationName;
  }
  public List<RemoteAllergy> getAllergies() {
    return allergies;
  }
  public void setAllergies(List<RemoteAllergy> allergies) {
    this.allergies = allergies;
  }
  
}