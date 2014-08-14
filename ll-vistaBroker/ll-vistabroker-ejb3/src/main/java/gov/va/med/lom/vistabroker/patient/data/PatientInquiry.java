package gov.va.med.lom.vistabroker.patient.data;

import java.io.Serializable;

public class PatientInquiry implements Serializable {
  
  private String text;
  
  public PatientInquiry() {
    this.text = null;
  }
  
  public String getText() {
    return text;
  }
  
  public void setText(String text) {
    this.text = text;
  }
  
}
