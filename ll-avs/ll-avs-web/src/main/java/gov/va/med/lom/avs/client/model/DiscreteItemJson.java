package gov.va.med.lom.avs.client.model;

import java.io.Serializable;

public class DiscreteItemJson implements Serializable {

  private String datetime;
  private double fmDate;
  private String value;
  private String alert;
  private String sampleType;
  private String referenceRange;
  
  public String getDatetime() {
    return datetime;
  }

  public void setDatetime(String datetime) {
    this.datetime = datetime;
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

