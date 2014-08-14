package gov.va.med.lom.vistabroker.admin.data;

import java.io.Serializable;

public class GenFormData implements Serializable {
    
  private String id;
  private String fileIen;
  private String entryIen;
  private String fieldIen;
  private String dataType;
  private String intFormat;
  private String extFormat;
  private String diNumIen;
  private String flags;
  private String message;
  
  public String getId() {
    return id;
  }
  public void setId(String id) {
    this.id = id;
  }
  public String getDataType() {
    return dataType;
  }
  public void setDataType(String dataType) {
    this.dataType = dataType;
  }
  public String getEntryIen() {
    return entryIen;
  }
  public void setEntryIen(String entryIen) {
    this.entryIen = entryIen;
  }
  public String getFieldIen() {
    return fieldIen;
  }
  public void setFieldNumber(String fieldIen) {
    this.fieldIen = fieldIen;
  }
  public String getFileIen() {
    return fileIen;
  }
  public void setFileIen(String fileIen) {
    this.fileIen = fileIen;
  }
  public String getExtFormat() {
    return extFormat;
  }
  public void setExtFormat(String extFormat) {
    this.extFormat = extFormat;
  }
  public String getIntFormat() {
    return intFormat;
  }
  public void setIntFormat(String intFormat) {
    this.intFormat = intFormat;
  }
  public String getDiNumIen() {
    return diNumIen;
  }
  public void setDiNumIen(String diNumIen) {
    this.diNumIen = diNumIen;
  }
  public String getFlags() {
    return flags;
  }
  public void setFlags(String flags) {
    this.flags = flags;
  }
  public String getMessage() {
    return message;
  }
  public void setMessage(String message) {
    this.message = message;
  }
  
}
