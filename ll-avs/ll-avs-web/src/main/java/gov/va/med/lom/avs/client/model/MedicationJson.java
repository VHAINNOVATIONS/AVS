package gov.va.med.lom.avs.client.model;

public class MedicationJson {

  private String id;
  private String name;
  private String type;
  private String sig;
  private int totalNumRefills;
  private int refillsRemaining;
  private String startDate;
  private String stopDate; 
  private String dateExpires;
  private String dateLastFilled;
  private String dateLastReleased;
  private String site;
  private String stationNo;
  private String provider;
  private String description;
  private String ndc;
  private String status;
  
  public String getId() {
    return id;
  }
  public void setId(String id) {
    this.id = id;
  }
  public String getName() {
    return name;
  }
  public void setName(String name) {
    this.name = name;
  }
  public String getSig() {
    return sig;
  }
  public void setSig(String sig) {
    this.sig = sig;
  }
  public int getRefillsRemaining() {
    return refillsRemaining;
  }
  public void setRefillsRemaining(int refillsRemaining) {
    this.refillsRemaining = refillsRemaining;
  }
  public String getDateExpires() {
    return dateExpires;
  }
  public void setDateExpires(String dateExpires) {
    this.dateExpires = dateExpires;
  }
  public String getDateLastFilled() {
    return dateLastFilled;
  }
  public void setDateLastFilled(String dateLastFilled) {
    this.dateLastFilled = dateLastFilled;
  }
  public String getSite() {
    return site;
  }
  public void setSite(String site) {
    this.site = site;
  }
  public String getProvider() {
    return provider;
  }
  public void setProvider(String provider) {
    this.provider = provider;
  }
  public String getDescription() {
    return description;
  }
  public void setDescription(String description) {
    this.description = description;
  }
  public String getStartDate() {
    return startDate;
  }
  public void setStartDate(String startDate) {
    this.startDate = startDate;
  }
  public String getStopDate() {
    return stopDate;
  }
  public void setStopDate(String stopDate) {
    this.stopDate = stopDate;
  }
  public String getType() {
    return type;
  }
  public void setType(String type) {
    this.type = type;
  }
  public String getDateLastReleased() {
    return dateLastReleased;
  }
  public void setDateLastReleased(String dateLastReleased) {
    this.dateLastReleased = dateLastReleased;
  }
  public String getNdc() {
    return ndc;
  }
  public void setNdc(String ndc) {
    this.ndc = ndc;
  }
  public String getStatus() {
    return status;
  }
  public void setStatus(String status) {
    this.status = status;
  }
  public String getStationNo() {
    return stationNo;
  }
  public void setStationNo(String stationNo) {
    this.stationNo = stationNo;
  }
  public int getTotalNumRefills() {
    return totalNumRefills;
  }
  public void setTotalNumRefills(int totalNumRefills) {
    this.totalNumRefills = totalNumRefills;
  }
  
}
