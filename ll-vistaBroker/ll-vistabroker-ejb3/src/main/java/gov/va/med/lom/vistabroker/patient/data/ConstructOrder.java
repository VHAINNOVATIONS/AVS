package gov.va.med.lom.vistabroker.patient.data;

import java.util.List;

import java.io.Serializable;

public class ConstructOrder implements Serializable {

  private String dialogName;
  private String leadText;
  private String trailText;
  private String dgroup;
  private String orderItem;
  private char delayEvent;
  private String ptEventPtr; // ptr to #100.2
  private String eventPtr;   // ptr to #100.5
  private String specialty;
  private double effective;  // fm datetime
  private double logTime;    // fm datetime
  private List<String> ocList;
  private String digSig;
  private List responseList;
  private boolean isImoDialog;
  private int isEventDefaultOr;
  
  public ConstructOrder() {
    this.dialogName = "";
    this.leadText = "";
    this.trailText = "";
    this.dgroup = "";
    this.orderItem = "";
    this.ptEventPtr = "";
    this.eventPtr = "";
    this.specialty = "";
    this.digSig = "";
  }
  
  public String getDialogName() {
    return dialogName;
  }
  public void setDialogName(String dialogName) {
    this.dialogName = dialogName;
  }
  public String getLeadText() {
    return leadText;
  }
  public void setLeadText(String leadText) {
    this.leadText = leadText;
  }
  public String getTrailText() {
    return trailText;
  }
  public void setTrailText(String trailText) {
    this.trailText = trailText;
  }
  public String getDgroup() {
    return dgroup;
  }
  public void setDgroup(String dgroup) {
    this.dgroup = dgroup;
  }
  public String getOrderItem() {
    return orderItem;
  }
  public void setOrderItem(String orderItem) {
    this.orderItem = orderItem;
  }
  public char getDelayEvent() {
    return delayEvent;
  }
  public void setDelayEvent(char delayEvent) {
    this.delayEvent = delayEvent;
  }
  public String getPtEventPtr() {
    return ptEventPtr;
  }
  public void setPtEventPtr(String ptEventPtr) {
    this.ptEventPtr = ptEventPtr;
  }
  public String getEventPtr() {
    return eventPtr;
  }
  public void setEventPtr(String eventPtr) {
    this.eventPtr = eventPtr;
  }
  public String getSpecialty() {
    return specialty;
  }
  public void setSpecialty(String specialty) {
    this.specialty = specialty;
  }
  public double getEffective() {
    return effective;
  }
  public void setEffective(double effective) {
    this.effective = effective;
  }
  public List<String> getOcList() {
    return ocList;
  }
  public void setOcList(List<String> ocList) {
    this.ocList = ocList;
  }
  public String getDigSig() {
    return digSig;
  }
  public void setDigSig(String digSig) {
    this.digSig = digSig;
  }
  public List getResponseList() {
    return responseList;
  }
  public void setResponseList(List responseList) {
    this.responseList = responseList;
  }
  public boolean isImoDialog() {
    return isImoDialog;
  }
  public void setImoDialog(boolean isImoDialog) {
    this.isImoDialog = isImoDialog;
  }
  public int getIsEventDefaultOr() {
    return isEventDefaultOr;
  }
  public void setIsEventDefaultOr(int isEventDefaultOr) {
    this.isEventDefaultOr = isEventDefaultOr;
  }
  public double getLogTime() {
    return logTime;
  }
  public void setLogTime(double logTime) {
    this.logTime = logTime;
  }
  
}
