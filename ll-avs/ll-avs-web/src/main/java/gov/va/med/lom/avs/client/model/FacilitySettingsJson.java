package gov.va.med.lom.avs.client.model;

public class FacilitySettingsJson {

  private String facilityNo;
  private String timeZone;
  private boolean tiuNoteEnabled;
  private boolean kramesEnabled;
  private boolean servicesEnabled;
  private int refreshFrequency;
  
  public String getFacilityNo() {
    return facilityNo;
  }
  public void setFacilityNo(String facilityNo) {
    this.facilityNo = facilityNo;
  }
  public String getTimeZone() {
    return timeZone;
  }
  public void setTimeZone(String timeZone) {
    this.timeZone = timeZone;
  }
  public boolean getTiuNoteEnabled() {
    return tiuNoteEnabled;
  }
  public void setTiuNoteEnabled(boolean tiuNoteEnabled) {
    this.tiuNoteEnabled = tiuNoteEnabled;
  }
  public boolean getKramesEnabled() {
    return kramesEnabled;
  }
  public void setKramesEnabled(boolean kramesEnabled) {
    this.kramesEnabled = kramesEnabled;
  }
  public boolean getServicesEnabled() {
    return servicesEnabled;
  }
  public void setServicesEnabled(boolean servicesEnabled) {
    this.servicesEnabled = servicesEnabled;
  }
  public int getRefreshFrequency() {
    return refreshFrequency;
  }
  public void setRefreshFrequency(int refreshFrequency) {
    this.refreshFrequency = refreshFrequency;
  }
  
}