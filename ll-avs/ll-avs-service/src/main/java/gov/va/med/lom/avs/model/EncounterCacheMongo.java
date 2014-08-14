package gov.va.med.lom.avs.model;

import java.util.List;
import java.util.ArrayList;

import org.mongodb.morphia.annotations.Entity;

@Entity("encounterCache")
public class EncounterCacheMongo extends BaseEntity {

  private String facilityNo;
  private String patientDfn;
  private List<Encounter> encounters;
  private String instructions;
  private String customContent;
  private String fontClass;
  private int labDateRange;
  private List<String> sections;
  private String remoteVaMedications;
  private String remoteNonVaMedications;
  private boolean printServiceDescriptions;
  private String selectedServiceDescriptions;
  private List<String> charts;
  private boolean locked;
  private boolean printed;
  private String language;
  private String docType;
  private String pdfFilename;
  private String userDuz;
  private String userName;
  
  public EncounterCacheMongo() {
    this.encounters = new ArrayList<Encounter>();
    this.sections = new ArrayList<String>();
    this.charts = new ArrayList<String>();
  }
  
  public void addEncounter(Encounter encounter) {
    this.encounters.add(encounter);
  }
  
  public void addSection(String section) {
    this.sections.add(section);
  }
  
  public void addChart(String chart) {
    this.charts.add(chart);
  }
  
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
  public int getLabDateRange() {
    return labDateRange;
  }
  public void setLabDateRange(int labDateRange) {
    this.labDateRange = labDateRange;
  }
  public boolean isPrintServiceDescriptions() {
    return printServiceDescriptions;
  }
  public void setPrintServiceDescriptions(boolean printServiceDescriptions) {
    this.printServiceDescriptions = printServiceDescriptions;
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
  public String getRemoteVaMedications() {
    return remoteVaMedications;
  }
  public List<Encounter> getEncounters() {
    return encounters;
  }
  public void setEncounters(List<Encounter> encounters) {
    this.encounters = encounters;
  }
  public List<String> getSections() {
    return sections;
  }
  public void setSections(List<String> sections) {
    this.sections = sections;
  }
  public List<String> getCharts() {
    return charts;
  }
  public void setCharts(List<String> charts) {
    this.charts = charts;
  }

  public String getUserDuz() {
    return userDuz;
  }

  public void setUserDuz(String userDuz) {
    this.userDuz = userDuz;
  }

  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

}