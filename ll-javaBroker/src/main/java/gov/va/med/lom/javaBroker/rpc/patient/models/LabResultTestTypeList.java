package gov.va.med.lom.javaBroker.rpc.patient.models;

import gov.va.med.lom.javaBroker.rpc.BaseBean;

import java.io.Serializable;

public class LabResultTestTypeList extends BaseBean implements Serializable {

  private LabResultTestType[] labResultTestTypes;
  
  public LabResultTestTypeList() {
    this.labResultTestTypes = null;
  }

  public LabResultTestType[] getLabResultTestTypes() {
    return labResultTestTypes;
  }
  
  public void setLabResultTestTypes(LabResultTestType[] labResultTestTypes) {
    this.labResultTestTypes = labResultTestTypes;
  }
}
