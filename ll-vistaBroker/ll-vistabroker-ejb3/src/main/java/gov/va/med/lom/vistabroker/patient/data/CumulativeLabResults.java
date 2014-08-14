package gov.va.med.lom.vistabroker.patient.data;

import java.io.Serializable;
import java.util.List;

public class CumulativeLabResults implements Serializable {
  
  private List<LabResultTestType> labResultTestTypes;
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
  
  public List<LabResultTestType> getLabResultTestTypes() {
    return labResultTestTypes;
  }
  
  public void setLabResultTestTypes(List<LabResultTestType> labResultTestTypes) {
    this.labResultTestTypes = labResultTestTypes;
  }
}
