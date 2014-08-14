package gov.va.med.lom.vistabroker.patient.data;

import java.util.List;
import java.io.Serializable;

public class EditResubmit implements Serializable {

  private boolean changed;
  private String ien;
  private String requestType;
  private String orderableItem;
  private String toService;
  private String toServiceName;
  private String consultProc;
  private String consultProcName;
  private String urgency;
  private String urgencyName;
  private double earliestDate;
  private String place;
  private String placeName;
  private String attention;
  private String attentionName;
  private String inpOutp;
  private List<String> requestReason;
  private String provDiagnosis;
  private String provDxCode;
  private boolean provDxCodeInactive;
  private List<String> denyComments;
  private List<String> otherComments;
  private List<String> newComments;
  
  public boolean isChanged() {
    return changed;
  }
  public void setChanged(boolean changed) {
    this.changed = changed;
  }
  public String getIen() {
    return ien;
  }
  public void setIen(String ien) {
    this.ien = ien;
  }
  public String getRequestType() {
    return requestType;
  }
  public void setRequestType(String requestType) {
    this.requestType = requestType;
  }
  public String getToService() {
    return toService;
  }
  public void setToService(String toService) {
    this.toService = toService;
  }
  public String getToServiceName() {
    return toServiceName;
  }
  public void setToServiceName(String toServiceName) {
    this.toServiceName = toServiceName;
  }
  public String getConsultProc() {
    return consultProc;
  }
  public void setConsultProc(String consultProc) {
    this.consultProc = consultProc;
  }
  public String getConsultProcName() {
    return consultProcName;
  }
  public void setConsultProcName(String consultProcName) {
    this.consultProcName = consultProcName;
  }
  public String getUrgency() {
    return urgency;
  }
  public void setUrgency(String urgency) {
    this.urgency = urgency;
  }
  public String getUrgencyName() {
    return urgencyName;
  }
  public void setUrgencyName(String urgencyName) {
    this.urgencyName = urgencyName;
  }
  public double getEarliestDate() {
    return earliestDate;
  }
  public void setEarliestDate(double earliestDate) {
    this.earliestDate = earliestDate;
  }
  public String getPlace() {
    return place;
  }
  public void setPlace(String place) {
    this.place = place;
  }
  public String getPlaceName() {
    return placeName;
  }
  public void setPlaceName(String placeName) {
    this.placeName = placeName;
  }
  public String getAttention() {
    return attention;
  }
  public void setAttention(String attention) {
    this.attention = attention;
  }
  public String getAttentionName() {
    return attentionName;
  }
  public void setAttentionName(String attentionName) {
    this.attentionName = attentionName;
  }
  public String getInpOutp() {
    return inpOutp;
  }
  public void setInpOutp(String inpOutp) {
    this.inpOutp = inpOutp;
  }
  public List<String> getRequestReason() {
    return requestReason;
  }
  public void setRequestReason(List<String> requestReason) {
    this.requestReason = requestReason;
  }
  public String getProvDiagnosis() {
    return provDiagnosis;
  }
  public void setProvDiagnosis(String provDiagnosis) {
    this.provDiagnosis = provDiagnosis;
  }
  public String getProvDxCode() {
    return provDxCode;
  }
  public void setProvDxCode(String provDxCode) {
    this.provDxCode = provDxCode;
  }
  public boolean getProvDxCodeInactive() {
    return provDxCodeInactive;
  }
  public void setProvDxCodeInactive(boolean provDxCodeInactive) {
    this.provDxCodeInactive = provDxCodeInactive;
  }
  public List<String> getDenyComments() {
    return denyComments;
  }
  public void setDenyComments(List<String> denyComments) {
    this.denyComments = denyComments;
  }
  public List<String> getOtherComments() {
    return otherComments;
  }
  public void setOtherComments(List<String> otherComments) {
    this.otherComments = otherComments;
  }
  public List<String> getNewComments() {
    return newComments;
  }
  public void setNewComments(List<String> newComments) {
    this.newComments = newComments;
  }
  public String getOrderableItem() {
    return orderableItem;
  }
  public void setOrderableItem(String orderableItem) {
    this.orderableItem = orderableItem;
  }
  
}
