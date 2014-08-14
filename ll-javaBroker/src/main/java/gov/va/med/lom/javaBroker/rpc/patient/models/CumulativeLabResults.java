package gov.va.med.lom.javaBroker.rpc.patient.models;

import gov.va.med.lom.javaBroker.rpc.BaseBean;

import java.io.Serializable;

public class CumulativeLabResults extends BaseBean implements Serializable {
  
  private LabResultTestType[] labResultTestTypes;
  private String text;
  
  public CumulativeLabResults() {
    this.labResultTestTypes = null;
    this.text = null;
  }
  
  public String getText() {
    return text;
  }
  
  public void setText(String text) {
    this.text = text;
  }
  
  public LabResultTestType[] getLabResultTestTypes() {
    return labResultTestTypes;
  }
  
  public void setLabResultTestTypes(LabResultTestType[] labResultTestTypes) {
    this.labResultTestTypes = labResultTestTypes;
  }
}
