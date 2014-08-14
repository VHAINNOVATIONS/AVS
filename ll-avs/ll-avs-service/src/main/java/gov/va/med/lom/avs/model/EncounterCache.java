package gov.va.med.lom.avs.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="ckoEncounterCache")
public class EncounterCache extends BaseModel implements Serializable {

  private static final long serialVersionUID = 0;

  private String facilityNo;
  private String providerDuz;
  private String providerName;
  private String providerTitle;
  private String patientDfn;
  private String locationIen;
  private String locationName;
  private double encounterDatetime;
  private String encounterNoteIen;
  private String instructions;
  private String customContent;
  private String fontClass;
  private String labDateRange;
  private String sections;
  private String remoteVaMedications;
  private String remoteNonVaMedications;
  private boolean printServiceDescriptions;
  private String selectedServiceDescriptions;
  private String charts;
  private boolean locked;
  private boolean printed;
  private String language;
  private String docType;
  private String pdfFilename;
  
  public String getFacilityNo() {
    return facilityNo;
  }
  public void setFacilityNo(String facilityNo) {
    this.facilityNo = facilityNo;
  }
  public String getPatientDfn() {
    return patientDfn;
  }
  public void setPatientDfn(String patientDfn) {
    this.patientDfn = patientDfn;
  }
  public String getLocationIen() {
    return locationIen;
  }
  public void setLocationIen(String locationIen) {
    this.locationIen = locationIen;
  }
  public double getEncounterDatetime() {
    return encounterDatetime;
  }
  public void setEncounterDatetime(double encounterDatetime) {
    this.encounterDatetime = encounterDatetime;
  }
  public String getInstructions() {
    return instructions;
  }
  public void setInstructions(String instructions) {
    this.instructions = instructions;
  }
  public String getFontClass() {
    return fontClass;
  }
  public void setFontClass(String fontClass) {
    this.fontClass = fontClass;
  }
  public String getLabDateRange() {
    return labDateRange;
  }
  public void setLabDateRange(String labDateRange) {
    this.labDateRange = labDateRange;
  }
  public String getSections() {
    return sections;
  }
  public void setSections(String sections) {
    this.sections = sections;
  }
  public boolean isPrintServiceDescriptions() {
    return printServiceDescriptions;
  }
  public void setPrintServiceDescriptions(boolean printServiceDescriptions) {
    this.printServiceDescriptions = printServiceDescriptions;
  }
  public String getProviderDuz() {
    return providerDuz;
  }
  public void setProviderDuz(String providerDuz) {
    this.providerDuz = providerDuz;
  }
  public String getCharts() {
    return charts;
  }
  public void setCharts(String charts) {
    this.charts = charts;
  }
  public boolean isLocked() {
    return locked;
  }
  public void setLocked(boolean locked) {
    this.locked = locked;
  }
  public String getCustomContent() {
    return customContent;
  }
  public void setCustomContent(String customContent) {
    this.customContent = customContent;
  }
  public String getSelectedServiceDescriptions() {
    return selectedServiceDescriptions;
  }
  public void setSelectedServiceDescriptions(String selectedServiceDescriptions) {
    this.selectedServiceDescriptions = selectedServiceDescriptions;
  }
  public String getEncounterNoteIen() {
    return encounterNoteIen;
  }
  public void setEncounterNoteIen(String encounterNoteIen) {
    this.encounterNoteIen = encounterNoteIen;
  }
  
  @Column(name="remoteMedications")
  public String getRemoteVaMedications() {
    return remoteVaMedications;
  }
  public void setRemoteVaMedications(String remoteVaMedications) {
    this.remoteVaMedications = remoteVaMedications;
  }
  public String getRemoteNonVaMedications() {
    return remoteNonVaMedications;
  }
  public void setRemoteNonVaMedications(String remoteNonVaMedications) {
    this.remoteNonVaMedications = remoteNonVaMedications;
  }
  public boolean isPrinted() {
    return printed;
  }
  public void setPrinted(boolean printed) {
    this.printed = printed;
  }
  public String getProviderName() {
    return providerName;
  }
  public void setProviderName(String providerName) {
    this.providerName = providerName;
  }
  public String getProviderTitle() {
    return providerTitle;
  }
  public void setProviderTitle(String providerTitle) {
    this.providerTitle = providerTitle;
  }
  public String getLocationName() {
    return locationName;
  }
  public void setLocationName(String locationName) {
    this.locationName = locationName;
  }
  public String getPdfFilename() {
    return pdfFilename;
  }
  public void setPdfFilename(String pdfFilename) {
    this.pdfFilename = pdfFilename;
  }
  public String getLanguage() {
    return language;
  }
  public void setLanguage(String language) {
    this.language = language;
  }
  public String getDocType() {
    return docType;
  }
  public void setDocType(String docType) {
    this.docType = docType;
  }
  
}