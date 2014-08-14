package gov.va.med.lom.javaBroker.rpc.patient.models;

import java.io.Serializable;

import gov.va.med.lom.javaBroker.rpc.BaseBean;

public class MedicationsList extends BaseBean implements Serializable {

  private Medication[] medications;
  
  public MedicationsList() {
    this.medications = null;
  }
  
  public Medication[] getMedications() {
    return medications;
  }
  
  public void setMedications(Medication[] medications) {
    this.medications = medications;
  }
}
