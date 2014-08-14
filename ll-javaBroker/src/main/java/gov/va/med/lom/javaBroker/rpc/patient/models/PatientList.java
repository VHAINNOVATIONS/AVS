package gov.va.med.lom.javaBroker.rpc.patient.models;

import java.io.Serializable;

import gov.va.med.lom.javaBroker.rpc.BaseBean;

public class PatientList extends BaseBean implements Serializable {

  private PatientListItem[] patientListItems;
  
  public PatientList() {
    this.patientListItems = null;
  }
  
  public PatientListItem[] getPatientListItems() {
    return patientListItems;
  }
  
  public void setPatientListItems(PatientListItem[] patientListItems) {
    this.patientListItems = patientListItems;
  }
}
