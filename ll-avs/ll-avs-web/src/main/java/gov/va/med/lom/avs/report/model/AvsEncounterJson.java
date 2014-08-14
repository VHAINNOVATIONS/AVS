package gov.va.med.lom.avs.report.model;

import java.io.Serializable;

public class AvsEncounterJson implements Serializable {

  private Long id;
	private String provider;
	private String location;
	private String title;
	private String encounterDatetime;
	private String fontSize;
	private String labDaysBack;
	private String hasInstructions;
	private String hasCustomContent;
	private String hasRemoteVaMeds;
	private String hasRemoteNonVaMeds;
	private String hasClinicalServices;
	private String hasCharts;
	private String locked;
	private String printed;
	
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getProvider() {
    return provider;
  }

  public void setProvider(String provider) {
    this.provider = provider;
  }

  public String getLocation() {
    return location;
  }

  public void setLocation(String location) {
    this.location = location;
  }

  public String getEncounterDatetime() {
    return encounterDatetime;
  }

  public void setEncounterDatetime(String encounterDatetime) {
    this.encounterDatetime = encounterDatetime;
  }

  public String getFontSize() {
    return fontSize;
  }

  public void setFontSize(String fontSize) {
    this.fontSize = fontSize;
  }

  public String isHasInstructions() {
    return hasInstructions;
  }

  public void setHasInstructions(String hasInstructions) {
    this.hasInstructions = hasInstructions;
  }

  public String isHasCustomContent() {
    return hasCustomContent;
  }

  public void setHasCustomContent(String hasCustomContent) {
    this.hasCustomContent = hasCustomContent;
  }

  public void setLabDaysBack(String labDaysBack) {
    this.labDaysBack = labDaysBack;
  }

  public String isHasRemoteVaMeds() {
    return hasRemoteVaMeds;
  }

  public void setHasRemoteVaMeds(String hasRemoteVaMeds) {
    this.hasRemoteVaMeds = hasRemoteVaMeds;
  }

  public String isHasRemoteNonVaMeds() {
    return hasRemoteNonVaMeds;
  }

  public void setHasRemoteNonVaMeds(String hasRemoteNonVaMeds) {
    this.hasRemoteNonVaMeds = hasRemoteNonVaMeds;
  }

  public String isHasClinicalServices() {
    return hasClinicalServices;
  }

  public void setHasClinicalServices(String hasClinicalServices) {
    this.hasClinicalServices = hasClinicalServices;
  }

  public String isLocked() {
    return locked;
  }

  public void setLocked(String locked) {
    this.locked = locked;
  }

  public String isPrinted() {
    return printed;
  }

  public void setPrinted(String printed) {
    this.printed = printed;
  }

  public String isHasCharts() {
    return hasCharts;
  }

  public void setHasCharts(String hasCharts) {
    this.hasCharts = hasCharts;
  }

  public String getLabDaysBack() {
    return labDaysBack;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

}