package gov.va.med.lom.vistabroker.patient.data;

import java.io.Serializable;

public class DiscreteItemData implements Serializable {

  private String fileNum;
  private String itemNum;
  private double fmDate;
  private String value;
  private String alert;
  private String sampleType;
  private String referenceRange;
  
  public DiscreteItemData() {
	this.fileNum = null;
	this.itemNum = null;
	this.fmDate = 0;
	this.value = null;
	this.alert = null;
	this.sampleType = null;
	this.referenceRange = null;
  }
  
  public String getFileNum() {
    return fileNum;
  }

  public String getItemNum() {
    return itemNum;
  }
  
  public double getFmDate() {
    return fmDate;
  }
  
  public String getValue() {
    return value;
  }
  
  public String getAlert() {
    return alert;
  }
  
  public String getSampleType() {
    return sampleType;
  }
  
  public String getReferenceRange() {
    return referenceRange;
  }

  public void setFileNum(String fileNum) {
    this.fileNum = fileNum;
  }
  
  public void setItemNum(String itemNum) {
    this.itemNum = itemNum;
  }

  public void setFmDate(double fmDate) {
    this.fmDate = fmDate;
  }

  public void setValue(String value) {
    this.value = value;
  }

  public void setAlert(String alert) {
    this.alert = alert;
  }

  public void setSampleType(String sampleType) {
    this.sampleType = sampleType;
  }

  public void setReferenceRange(String referenceRange) {
    this.referenceRange = referenceRange;
  }

  
}
