package gov.va.med.lom.javaBroker.rpc.lists.models;

import gov.va.med.lom.javaBroker.rpc.BaseBean;

import java.io.Serializable;

public class LabTestTypesList extends BaseBean implements Serializable {

  private LabTestType[] labTestTypes;
  
  public LabTestTypesList() {
    this.labTestTypes = null;
  }

  public LabTestType[] getLabTestTypes() {
    return labTestTypes;
  }
  
  public void setLabTestTypes(LabTestType[] labTestTypes) {
    this.labTestTypes = labTestTypes;
  }
}
