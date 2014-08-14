package gov.va.med.lom.avs.model;

public class EncounterLocation {

  private String locationIen;
  private String locationName;
  
  public EncounterLocation() {}
  
  public EncounterLocation(String locationIen, String locationName) {
    this.locationIen = locationIen;
    this.locationName = locationName; 
  }
  
  public String getLocationIen() {
    return locationIen;
  }
  public void setLocationIen(String locationIen) {
    this.locationIen = locationIen;
  }
  public String getLocationName() {
    return locationName;
  }
  public void setLocationName(String locationName) {
    this.locationName = locationName;
  }
  
}