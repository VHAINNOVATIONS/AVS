package gov.va.med.lom.javaBroker.rpc.lists.models;

import gov.va.med.lom.javaBroker.rpc.BaseBean;

import java.io.Serializable;

public class LabReportList extends BaseBean implements Serializable {

  private LabReport[] labReports;
  
  public LabReportList() {
    this.labReports = null;
  }

  public LabReport[] getLabReports() {
    return labReports;
  }
  
  public void setLabReports(LabReport[] labReports) {
    this.labReports = labReports;
  }
}
