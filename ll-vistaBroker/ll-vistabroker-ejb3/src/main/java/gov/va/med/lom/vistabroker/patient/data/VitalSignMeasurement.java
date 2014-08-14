package gov.va.med.lom.vistabroker.patient.data;

import java.io.Serializable;
import java.util.Date;

/**
 * Vital Sign Measurement
 * 
 * Generic container for holding any type of vital sign measurement.
 */
public class VitalSignMeasurement implements Serializable {

  private String ien;
  private String typeAbbr;
  private String rawValue;
  private Date date;
  private String englishValue;
  private String metricValue;
  private String qualifiers;

  public String getIen() {
    return this.ien;
  }
    
  public void setIen(String value) {
    this.ien = value;
  }
 
  public String getTypeAbbr() {
    return this.typeAbbr;
  }
    
  public void setTypeAbbr(String value) {
    this.typeAbbr = value;
  }

  public String getRawValue() {
    return this.rawValue;
  }
    
  public void setRawValue(String value) {
    this.rawValue = value;
  }

  public Date getDate() {
    return this.date;
  }
  
  public void setDate(Date value) {
    this.date = value;
  }

  public String getEnglishValue() {
    return this.englishValue;
  }
    
  public void setEnglishValue(String value) {
    this.englishValue = value;
  }

  public String getMetricValue() {
    return this.metricValue;
  }
    
  public void setMetricValue(String value) {
    this.metricValue = value;
  }
  
  public String getQualifiers() {
    return this.qualifiers;
  }
    
  public void setQualifiers(String value) {
    this.qualifiers = value;
  }

}
