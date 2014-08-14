package gov.va.med.lom.vistabroker.patient.data;

import java.io.Serializable;
import java.util.Calendar;

public class OrderInfo implements Serializable {

  private String icd9Code;
  private String id;
  private String dGroup;
  private Calendar orderDatetime;
  private String orderDatetimeStr;
  private double fmOrderDatetime;
  private String startTimeStr;
  private String stopTimeStr;
  private String statusIen;
  private String status;
  private int signature;
  private String verNurse;
  private String verClerk;
  private String chartRev;
  private String providerDuz;
  private String providerName;
  private String providerDEA;
  private String providerVA;
  private String digSigReq;
  private String xmlText;
  private String text;
  private String dGroupSeq;
  private String dGroupName;
  private boolean flagged;
  private String eventPtr;
  private String eventName;
  private String urgency;
  private String orderLocIen;
  private String orderLocName;
  private String parentId;
  private int enteredInError;
  private boolean dcOriginalOrder;
  private boolean isOrderPendDc;
  private boolean isDelayOrder;
  
  public OrderInfo() {
    this.id = null;
    this.dGroup = null;
    this.orderDatetime = null;
    this.orderDatetimeStr = null;
    this.fmOrderDatetime = 0;
    this.startTimeStr = null;
    this.stopTimeStr = null;
    this.statusIen = null;
    this.status = null;
    this.signature = 0;
    this.verNurse = null;
    this.verClerk = null;
    this.chartRev = null;
    this.providerDuz = null;
    this.providerName = null;
    this.providerDEA = null;
    this.providerVA = null;
    this.digSigReq = null;
    this.xmlText = null;
    this.text = null;
    this.dGroupSeq = null;
    this.dGroupName = null;
    this.flagged = false;
    this.eventPtr = null;
    this.eventName = null;
    this.urgency = null;
  }
  
  public String getChartRev() {
    return chartRev;
  }
  
  public void setChartRev(String chartRev) {
    this.chartRev = chartRev;
  }
  
  public String getDGroup() {
    return dGroup;
  }
  
  public void setDGroup(String group) {
    dGroup = group;
  }
  
  public String getDGroupName() {
    return dGroupName;
  }
  
  public void setDGroupName(String groupName) {
    dGroupName = groupName;
  }
  
  public String getDGroupSeq() {
    return dGroupSeq;
  }
  
  public void setDGroupSeq(String groupSeq) {
    dGroupSeq = groupSeq;
  }
  
  public String getDigSigReq() {
    return digSigReq;
  }
  
  public void setDigSigReq(String digSigReq) {
    this.digSigReq = digSigReq;
  }
  
  public String getEventName() {
    return eventName;
  }
  
  public void setEventName(String eventName) {
    this.eventName = eventName;
  }
  
  public String getEventPtr() {
    return eventPtr;
  }
  
  public void setEventPtr(String eventPtr) {
    this.eventPtr = eventPtr;
  }
  
  public boolean getFlagged() {
    return flagged;
  }
  
  public void setFlagged(boolean flagged) {
    this.flagged = flagged;
  }
  
  public double getFmOrderDatetime() {
    return fmOrderDatetime;
  }
  
  public void setFmOrderDatetime(double fmOrderDatetime) {
    this.fmOrderDatetime = fmOrderDatetime;
  }
  
  public String getId() {
    return id;
  }
  
  public void setId(String id) {
    this.id = id;
  }
  
  public Calendar getOrderDatetime() {
    return orderDatetime;
  }
  
  public void setOrderDatetime(Calendar orderDatetime) {
    this.orderDatetime = orderDatetime;
  }
  
  public String getOrderDatetimeStr() {
    return orderDatetimeStr;
  }
  
  public void setOrderDatetimeStr(String orderDatetimeStr) {
    this.orderDatetimeStr = orderDatetimeStr;
  }
  
  public String getProviderDEA() {
    return providerDEA;
  }
  
  public void setProviderDEA(String providerDEA) {
    this.providerDEA = providerDEA;
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
  
  public String getProviderVA() {
    return providerVA;
  }
  
  public void setProviderVA(String providerVA) {
    this.providerVA = providerVA;
  }
  
  public int getSignature() {
    return signature;
  }
  
  public void setSignature(int signature) {
    this.signature = signature;
  }
  
  public String getStartTimeStr() {
    return startTimeStr;
  }
  
  public void setStartTimeStr(String startTimeStr) {
    this.startTimeStr = startTimeStr;
  }
  
  public String getStatusIen() {
    return statusIen;
  }
  
  public void setStatusIen(String statusIen) {
    this.statusIen = statusIen;
  }
  
  public String getStatus() {
    return status;
  }
  
  public void setStatus(String status) {
    this.status = status;
  }
  
  public String getStopTimeStr() {
    return stopTimeStr;
  }
  
  public void setStopTimeStr(String stopTimeStr) {
    this.stopTimeStr = stopTimeStr;
  }
  
  public String getText() {
    return text;
  }
  
  public void setText(String text) {
    this.text = text;
  }
  
  public String getVerClerk() {
    return verClerk;
  }
  
  public void setVerClerk(String verClerk) {
    this.verClerk = verClerk;
  }
  
  public String getVerNurse() {
    return verNurse;
  }
  
  public void setVerNurse(String verNurse) {
    this.verNurse = verNurse;
  }
  
  public String getXmlText() {
    return xmlText;
  }
  
  public void setXmlText(String xmlText) {
    this.xmlText = xmlText;
  }
  
  public String getUrgency() {
    return urgency;
  }

  public void setUrgency(String urgency) {
    this.urgency = urgency;
  }

  public String getIcd9Code() {
    return icd9Code;
  }

  public void setIcd9Code(String icd9Code) {
    this.icd9Code = icd9Code;
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

  public boolean isDcOriginalOrder() {
    return dcOriginalOrder;
  }

  public void setDcOriginalOrder(boolean dcOriginalOrder) {
    this.dcOriginalOrder = dcOriginalOrder;
  }

  public boolean getIsOrderPendDc() {
    return isOrderPendDc;
  }

  public void setIsOrderPendDc(boolean isOrderPendDc) {
    this.isOrderPendDc = isOrderPendDc;
  }

  public boolean getIsDelayOrder() {
    return isDelayOrder;
  }

  public void setIsDelayOrder(boolean isDelayOrder) {
    this.isDelayOrder = isDelayOrder;
  }
  
}
