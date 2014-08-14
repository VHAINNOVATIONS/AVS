package gov.va.med.lom.vistabroker.admin.data;

import java.io.Serializable;

public class GenFormListParams implements Serializable {

  private boolean shortFormFlag;
  private String fileIen;
  private String iens;
  private String fields;
  private String flags;
  private String from;
  private String part;
  private String index;
  private String screen;
  private String identifier;
  private int numResults;
  
  public String getFields() {
    return fields;
  }
  public void setFields(String fields) {
    this.fields = fields;
  }
  public String getFileIen() {
    return fileIen;
  }
  public void setFileIen(String fileIen) {
    this.fileIen = fileIen;
  }
  public String getFlags() {
    return flags;
  }
  public void setFlags(String flags) {
    this.flags = flags;
  }
  public String getFrom() {
    return from;
  }
  public void setFrom(String from) {
    this.from = from;
  }
  public String getIdentifier() {
    return identifier;
  }
  public void setIdentifier(String identifier) {
    this.identifier = identifier;
  }
  public String getIens() {
    return iens;
  }
  public void setIens(String iens) {
    this.iens = iens;
  }
  public String getIndex() {
    return index;
  }
  public void setIndex(String index) {
    this.index = index;
  }
  public String getPart() {
    return part;
  }
  public void setPart(String part) {
    this.part = part;
  }
  public String getScreen() {
    return screen;
  }
  public void setScreen(String screen) {
    this.screen = screen;
  }
  public boolean isShortFormFlag() {
    return shortFormFlag;
  }
  public void setShortFormFlag(boolean shortFormFlag) {
    this.shortFormFlag = shortFormFlag;
  }
  public int getNumResults() {
    return numResults;
  }
  public void setNumResults(int numResults) {
    this.numResults = numResults;
  }
  
}
