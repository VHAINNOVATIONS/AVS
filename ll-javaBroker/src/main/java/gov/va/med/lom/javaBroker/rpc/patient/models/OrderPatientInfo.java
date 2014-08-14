package gov.va.med.lom.javaBroker.rpc.patient.models;

import gov.va.med.lom.javaBroker.rpc.BaseBean;

import java.io.Serializable;

public class OrderPatientInfo extends BaseBean implements Serializable {

  private String orderId;
  private String patientDfn;
  private String encProvider;
  private String encLocation;
  private String patientSpecialty;
  private double encDatetime;
  private boolean inpatient;
  private String patientSex;
  private int patientAge;
  private int scPercent;
  
  public OrderPatientInfo() {
    this.orderId = "";
    this.patientDfn = "'";
    this.encProvider = "";
    this.encLocation = "";
    this.patientSpecialty = "'";
    this.patientSex = "";
  }

  public String getOrderId() {
    return orderId;
  }

  public void setOrderId(String orderId) {
    this.orderId = orderId;
  }

  public String getPatientDfn() {
    return patientDfn;
  }

  public void setPatientDfn(String patientDfn) {
    this.patientDfn = patientDfn;
  }

  public String getEncProvider() {
    return encProvider;
  }

  public void setEncProvider(String encProvider) {
    this.encProvider = encProvider;
  }

  public String getEncLocation() {
    return encLocation;
  }

  public void setEncLocation(String encLocation) {
    this.encLocation = encLocation;
  }

  public String getPatientSpecialty() {
    return patientSpecialty;
  }

  public void setPatientSpecialty(String patientSpecialty) {
    this.patientSpecialty = patientSpecialty;
  }

  public double getEncDatetime() {
    return encDatetime;
  }

  public void setEncDatetime(double encDatetime) {
    this.encDatetime = encDatetime;
  }

  public boolean isInpatient() {
    return inpatient;
  }

  public void setInpatient(boolean inpatient) {
    this.inpatient = inpatient;
  }

  public String getPatientSex() {
    return patientSex;
  }

  public void setPatientSex(String patientSex) {
    this.patientSex = patientSex;
  }

  public int getPatientAge() {
    return patientAge;
  }

  public void setPatientAge(int patientAge) {
    this.patientAge = patientAge;
  }

  public int getScPercent() {
    return scPercent;
  }

  public void setScPercent(int scPercent) {
    this.scPercent = scPercent;
  }

}
