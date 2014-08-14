package gov.va.med.lom.vistabroker.patient.data;

import java.io.Serializable;
import java.util.Date;

public class OrderDetails implements Serializable {
  
  private String labOrderNum;
  private String orderText;
  private String natureOfOrder;
  private String enteredBy;
  private String releasedBy;
  private String signedBy;
  private String orderedBy;
  private String orderNum;
  private String currentData;
  private String treatingSpecialty;
  private String orderingLocation;
  private Date startDatetime;
  private Date stopDatetime;
  private String currentStatus;
  private String labTest;
  private String collectedBy;
  private String collectionSample;
  private String specimen;
  private Date collectionDatetime;
  private String urgency;
  private String howOften;
  
  public String getCollectedBy() {
    return collectedBy;
  }
  public void setCollectedBy(String collectedBy) {
    this.collectedBy = collectedBy;
  }
  public Date getCollectionDatetime() {
    return collectionDatetime;
  }
  public void setCollectionDatetime(Date collectionDatetime) {
    this.collectionDatetime = collectionDatetime;
  }
  public String getCollectionSample() {
    return collectionSample;
  }
  public void setCollectionSample(String collectionSample) {
    this.collectionSample = collectionSample;
  }
  public String getCurrentData() {
    return currentData;
  }
  public void setCurrentData(String currentData) {
    this.currentData = currentData;
  }
  public String getCurrentStatus() {
    return currentStatus;
  }
  public void setCurrentStatus(String currentStatus) {
    this.currentStatus = currentStatus;
  }
  public String getEnteredBy() {
    return enteredBy;
  }
  public void setEnteredBy(String enteredBy) {
    this.enteredBy = enteredBy;
  }
  public String getHowOften() {
    return howOften;
  }
  public void setHowOften(String howOften) {
    this.howOften = howOften;
  }
  public String getLabOrderNum() {
    return labOrderNum;
  }
  public void setLabOrderNum(String labOrderNum) {
    this.labOrderNum = labOrderNum;
  }
  public String getLabTest() {
    return labTest;
  }
  public void setLabTest(String labTest) {
    this.labTest = labTest;
  }
  public String getNatureOfOrder() {
    return natureOfOrder;
  }
  public void setNatureOfOrder(String natureOfOrder) {
    this.natureOfOrder = natureOfOrder;
  }
  public String getOrderedBy() {
    return orderedBy;
  }
  public void setOrderedBy(String orderedBy) {
    this.orderedBy = orderedBy;
  }
  public String getOrderingLocation() {
    return orderingLocation;
  }
  public void setOrderingLocation(String orderingLocation) {
    this.orderingLocation = orderingLocation;
  }
  public String getOrderNum() {
    return orderNum;
  }
  public void setOrderNum(String orderNum) {
    this.orderNum = orderNum;
  }
  public String getOrderText() {
    return orderText;
  }
  public void setOrderText(String orderText) {
    this.orderText = orderText;
  }
  public String getReleasedBy() {
    return releasedBy;
  }
  public void setReleasedBy(String releasedBy) {
    this.releasedBy = releasedBy;
  }
  public String getSignedBy() {
    return signedBy;
  }
  public void setSignedBy(String signedBy) {
    this.signedBy = signedBy;
  }
  public String getSpecimen() {
    return specimen;
  }
  public void setSpecimen(String specimen) {
    this.specimen = specimen;
  }
  public Date getStartDatetime() {
    return startDatetime;
  }
  public void setStartDatetime(Date startDatetime) {
    this.startDatetime = startDatetime;
  }
  public Date getStopDatetime() {
    return stopDatetime;
  }
  public void setStopDatetime(Date stopDatetime) {
    this.stopDatetime = stopDatetime;
  }
  public String getTreatingSpecialty() {
    return treatingSpecialty;
  }
  public void setTreatingSpecialty(String treatingSpecialty) {
    this.treatingSpecialty = treatingSpecialty;
  }
  public String getUrgency() {
    return urgency;
  }
  public void setUrgency(String urgency) {
    this.urgency = urgency;
  }
  
  
}
