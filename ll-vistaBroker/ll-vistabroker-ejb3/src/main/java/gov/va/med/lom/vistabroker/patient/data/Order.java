package gov.va.med.lom.vistabroker.patient.data;

import java.io.Serializable;

public class Order implements Serializable {

  private String icd9Code;
  private String id;
  private String dGroup;
  private double orderTime;
  private String startTime;
  private String stopTime;
  private int status;
  private int signature;
  private String verNurse;
  private String verClerk;
  private String chartRev;
  private String providerDuz;
  private String providerName;
  private String providerDEA;
  private String providerVa;
  private String digSigReq;
  private String xmlText;
  private String text;
  private String dGroupSeq;
  private String dGroupName;
  private boolean flagged;
  private boolean retrieved;
  private String editOf;
  private String actionOn;
  private String eventPtr;
  private String eventName;
  private String orderLocIen;
  private String orderLocName;
  private String parentId;
  private int enteredInError;
  
  public String getIcd9Code() {
    return icd9Code;
  }
  public void setIcd9Code(String icd9Code) {
    this.icd9Code = icd9Code;
  }
  public String getId() {
    return id;
  }
  public void setId(String id) {
    this.id = id;
  }
  public String getDGroup() {
    return dGroup;
  }
  public void setDGroup(String dGroup) {
    this.dGroup = dGroup;
  }
  public double getOrderTime() {
    return orderTime;
  }
  public void setOrderTime(double orderTime) {
    this.orderTime = orderTime;
  }
  public String getStartTime() {
    return startTime;
  }
  public void setStartTime(String startTime) {
    this.startTime = startTime;
  }
  public String getStopTime() {
    return stopTime;
  }
  public void setStopTime(String stopTime) {
    this.stopTime = stopTime;
  }
  public int getStatus() {
    return status;
  }
  public void setStatus(int status) {
    this.status = status;
  }
  public int getSignature() {
    return signature;
  }
  public void setSignature(int signature) {
    this.signature = signature;
  }
  public String getVerNurse() {
    return verNurse;
  }
  public void setVerNurse(String verNurse) {
    this.verNurse = verNurse;
  }
  public String getVerClerk() {
    return verClerk;
  }
  public void setVerClerk(String verClerk) {
    this.verClerk = verClerk;
  }
  public String getChartRev() {
    return chartRev;
  }
  public void setChartRev(String chartRev) {
    this.chartRev = chartRev;
  }
  public String getProviderDuz() {
    return providerDuz;
  }
  public void setProviderDuz(String providerDuz) {
    this.providerDuz = providerDuz;
  }
  public String getProviderName() {
    return providerName;
  }
  public void setProviderName(String providerName) {
    this.providerName = providerName;
  }
  public String getProviderDEA() {
    return providerDEA;
  }
  public void setProviderDEA(String providerDEA) {
    this.providerDEA = providerDEA;
  }
  public String getProviderVa() {
    return providerVa;
  }
  public void setProviderVa(String providerVa) {
    this.providerVa = providerVa;
  }
  public String getDigSigReq() {
    return digSigReq;
  }
  public void setDigSigReq(String digSigReq) {
    this.digSigReq = digSigReq;
  }
  public String getXmlText() {
    return xmlText;
  }
  public void setXmlText(String xmlText) {
    this.xmlText = xmlText;
  }
  public String getText() {
    return text;
  }
  public void setText(String text) {
    this.text = text;
  }
  public String getDGroupSeq() {
    return dGroupSeq;
  }
  public void setDGroupSeq(String dGroupSeq) {
    this.dGroupSeq = dGroupSeq;
  }
  public String getDGroupName() {
    return dGroupName;
  }
  public void setDGroupName(String dGroupName) {
    this.dGroupName = dGroupName;
  }
  public boolean isFlagged() {
    return flagged;
  }
  public void setFlagged(boolean flagged) {
    this.flagged = flagged;
  }
  public boolean isRetrieved() {
    return retrieved;
  }
  public void setRetrieved(boolean retrieved) {
    this.retrieved = retrieved;
  }
  public String getEditOf() {
    return editOf;
  }
  public void setEditOf(String editOf) {
    this.editOf = editOf;
  }
  public String getActionOn() {
    return actionOn;
  }
  public void setActionOn(String actionOn) {
    this.actionOn = actionOn;
  }
  public String getEventPtr() {
    return eventPtr;
  }
  public void setEventPtr(String eventPtr) {
    this.eventPtr = eventPtr;
  }
  public String getEventName() {
    return eventName;
  }
  public void setEventName(String eventName) {
    this.eventName = eventName;
  }
  public String getOrderLocIen() {
    return orderLocIen;
  }
  public void setOrderLocIen(String orderLocIen) {
    this.orderLocIen = orderLocIen;
  }
  public String getOrderLocName() {
    return orderLocName;
  }
  public void setOrderLocName(String orderLocName) {
    this.orderLocName = orderLocName;
  }
  public String getParentId() {
    return parentId;
  }
  public void setParentId(String parentId) {
    this.parentId = parentId;
  }
  public int getEnteredInError() {
    return enteredInError;
  }
  public void setEnteredInError(int enteredInError) {
    this.enteredInError = enteredInError;
  }
  
}
