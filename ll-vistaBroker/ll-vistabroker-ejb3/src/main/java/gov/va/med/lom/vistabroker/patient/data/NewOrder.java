package gov.va.med.lom.vistabroker.patient.data;

import java.io.Serializable;

public class NewOrder implements Serializable {

  private String orderId;
  private String dialogName;
  private String patientDfn;
  private String encProvider;
  private String encLocation;
  private String patientSpecialty;
  private double encDatetime;
  private boolean inpatient;
  private String patientSex;
  private int patientAge;
  private int scPercent;
  private String startDtTm;
  private String fillerId;
  private String digSig;
  private OrderField orderable;
  private OrderField comment;
  private OrderField start;
  private OrderField stop;
  // med order fields
  private OrderField instr;
  private OrderField drug;
  private OrderField dose;
  private OrderField strength;
  private OrderField route;
  private OrderField schedule;
  private OrderField urgency;
  private OrderField supply;
  private OrderField qty;
  private OrderField refills;
  private OrderField sc;
  private OrderField pickup;
  private OrderField pi;
  private OrderField sig;
  // consult fields
  private OrderField setting;
  private OrderField place;
  private OrderField earliest;
  
  public NewOrder() {
    this.orderId = "";
    this.dialogName = "";
    this.patientDfn = "'";
    this.encProvider = "";
    this.encLocation = "";
    this.patientSpecialty = "";
    this.patientSex = "";
    this.startDtTm = "TODAY";
    this.fillerId = "";
    this.inpatient = false;
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

  public String getDialogName() {
    return dialogName;
  }

  public void setDialogName(String dialogName) {
    this.dialogName = dialogName;
  }

  public String getStartDtTm() {
    return startDtTm;
  }

  public void setStartDtTm(String startDtTm) {
    this.startDtTm = startDtTm;
  }

  public String getFillerId() {
    return fillerId;
  }

  public void setFillerId(String fillerId) {
    this.fillerId = fillerId;
  }

  public String getDigSig() {
    return digSig;
  }

  public void setDigSig(String digSig) {
    this.digSig = digSig;
  }

  public OrderField getOrderable() {
    return orderable;
  }

  public void setOrderable(OrderField orderable) {
    this.orderable = orderable;
  }

  public OrderField getInstr() {
    return instr;
  }

  public void setInstr(OrderField instr) {
    this.instr = instr;
  }

  public OrderField getDrug() {
    return drug;
  }

  public void setDrug(OrderField drug) {
    this.drug = drug;
  }

  public OrderField getDose() {
    return dose;
  }

  public void setDose(OrderField dose) {
    this.dose = dose;
  }

  public OrderField getStrength() {
    return strength;
  }

  public void setStrength(OrderField strength) {
    this.strength = strength;
  }

  public OrderField getRoute() {
    return route;
  }

  public void setRoute(OrderField route) {
    this.route = route;
  }

  public OrderField getSchedule() {
    return schedule;
  }

  public void setSchedule(OrderField schedule) {
    this.schedule = schedule;
  }

  public OrderField getUrgency() {
    return urgency;
  }

  public void setUrgency(OrderField urgency) {
    this.urgency = urgency;
  }

  public OrderField getSupply() {
    return supply;
  }

  public void setSupply(OrderField supply) {
    this.supply = supply;
  }

  public OrderField getQty() {
    return qty;
  }

  public void setQty(OrderField qty) {
    this.qty = qty;
  }

  public OrderField getRefills() {
    return refills;
  }

  public void setRefills(OrderField refills) {
    this.refills = refills;
  }

  public OrderField getSc() {
    return sc;
  }

  public void setSc(OrderField sc) {
    this.sc = sc;
  }

  public OrderField getPickup() {
    return pickup;
  }

  public void setPickup(OrderField pickup) {
    this.pickup = pickup;
  }

  public OrderField getPi() {
    return pi;
  }

  public void setPi(OrderField pi) {
    this.pi = pi;
  }

  public OrderField getSig() {
    return sig;
  }

  public void setSig(OrderField sig) {
    this.sig = sig;
  }

  public OrderField getComment() {
    return comment;
  }

  public void setComment(OrderField comment) {
    this.comment = comment;
  }

  public OrderField getStart() {
    return start;
  }

  public void setStart(OrderField start) {
    this.start = start;
  }

  public OrderField getStop() {
    return stop;
  }

  public void setStop(OrderField stop) {
    this.stop = stop;
  }

  public OrderField getSetting() {
    return setting;
  }

  public void setSetting(OrderField setting) {
    this.setting = setting;
  }

  public OrderField getPlace() {
    return place;
  }

  public void setPlace(OrderField place) {
    this.place = place;
  }

  public OrderField getEarliest() {
    return earliest;
  }

  public void setEarliest(OrderField earliest) {
    this.earliest = earliest;
  }

}
