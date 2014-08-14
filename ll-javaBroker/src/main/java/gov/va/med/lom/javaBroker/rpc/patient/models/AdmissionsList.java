package gov.va.med.lom.javaBroker.rpc.patient.models;

import java.io.Serializable;

import gov.va.med.lom.javaBroker.rpc.BaseBean;

public class AdmissionsList extends BaseBean implements Serializable {

  private Admission[] admissions;
  
  public AdmissionsList() {
    this.admissions = null;
  }
  
  public Admission[] getAdmissions() {
    return admissions;
  }
  
  public void setAdmissions(Admission[] admissions) {
    this.admissions = admissions;
  }
}
