package gov.va.med.lom.javaBroker.rpc.patient.models;

import gov.va.med.lom.javaBroker.rpc.BaseBean;

import java.io.Serializable;

public class LabTestResultsList extends BaseBean implements Serializable {

  private LabTestResult[] labTestResults;
  
  public LabTestResultsList() {
    this.labTestResults = null;
  }
  
  public LabTestResult[] getLabTestResults() {
    return labTestResults;
  }
  
  public void setLabTestResults(LabTestResult[] labTestResults) {
    this.labTestResults = labTestResults;
  }
}
