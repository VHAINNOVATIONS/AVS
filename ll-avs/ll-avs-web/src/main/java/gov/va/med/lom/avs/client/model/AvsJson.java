package gov.va.med.lom.avs.client.model;

import java.util.List;
import java.io.Serializable;

public class AvsJson implements Serializable {	

  private String content;
  private String instructions;
  private String fontClass;
  private String language;
  private String labDateRange;
  private String sections;
  private boolean printAllServiceDescriptions;
  private String selectedServiceDescriptions;
  private String charts;
  private boolean locked;
  private boolean userIsProvider;
  private String lastRefreshed;
  private boolean contentEdited;
  private List<MedicationJson> remoteVaMeds;
  private List<MedicationJson> remoteNonVaMeds;
  private String diagnosisCodes;
  
  public String getContent() {
    return content;
  }
  public void setContent(String content) {
    this.content = content;
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
  public boolean getPrintAllServiceDescriptions() {
    return printAllServiceDescriptions;
  }
  public void setPrintAllServiceDescriptions(boolean printAllServiceDescriptions) {
    this.printAllServiceDescriptions = printAllServiceDescriptions;
  }
  public String getCharts() {
    return charts;
  }
  public void setCharts(String charts) {
    this.charts = charts;
  }
  public boolean getLocked() {
    return locked;
  }
  public void setLocked(boolean locked) {
    this.locked = locked;
  }
  public String getLastRefreshed() {
    return lastRefreshed;
  }
  public void setLastRefreshed(String lastRefreshed) {
    this.lastRefreshed = lastRefreshed;
  }
  public boolean getUserIsProvider() {
    return userIsProvider;
  }
  public void setUserIsProvider(boolean userIsProvider) {
    this.userIsProvider = userIsProvider;
  }
  public boolean isContentEdited() {
    return contentEdited;
  }
  public void setContentEdited(boolean contentEdited) {
    this.contentEdited = contentEdited;
  }
  public String getSelectedServiceDescriptions() {
    return selectedServiceDescriptions;
  }
  public void setSelectedServiceDescriptions(String selectedServiceDescriptions) {
    this.selectedServiceDescriptions = selectedServiceDescriptions;
  }
  public List<MedicationJson> getRemoteVaMeds() {
    return remoteVaMeds;
  }
  public void setRemoteVaMeds(List<MedicationJson> remoteVaMeds) {
    this.remoteVaMeds = remoteVaMeds;
  }
  public List<MedicationJson> getRemoteNonVaMeds() {
    return remoteNonVaMeds;
  }
  public void setRemoteNonVaMeds(List<MedicationJson> remoteNonVaMeds) {
    this.remoteNonVaMeds = remoteNonVaMeds;
  }
  public String getLanguage() {
    return language;
  }
  public void setLanguage(String language) {
    this.language = language;
  }
  public String getDiagnosisCodes() {
    return diagnosisCodes;
  }
  public void setDiagnosisCodes(String diagnosisCodes) {
    this.diagnosisCodes = diagnosisCodes;
  }
  
}
