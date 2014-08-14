package gov.va.med.lom.javaBroker.rpc.patient.models;

import gov.va.med.lom.javaBroker.rpc.BaseBean;

import java.io.Serializable;
import java.util.Date;

public class ConsultRec extends BaseBean implements Serializable {
  
  private Date entryDate;
  private String entryDateStr;
  private String orFileNumber;
  private String patientLocationIen;
  private String orderingFacilityIen;
  private String foreignConsultFileNum;
  private String toServiceIen;
  private String fromIen;
  private Date requestDate;
  private String requestDateStr;
  private String consultProcedure;
  private int urgency;
  private String placeOfConsultIen;
  private int attention;
  private int orStatus;
  private int lastAction;
  private String sendingProviderDfn;
  private String sendingProviderName;
  private String result;
  private String modeOfEntry;
  private int requestType;
  private String inOut;
  private String findings;
  private int tiuResultNarrative;
 
  public ConsultRec() {
    this.entryDate = null;
    this.entryDateStr = null;
    this.orFileNumber = null;
    this.patientLocationIen = null;
    this.orderingFacilityIen = null;
    this.foreignConsultFileNum = null;
    this.toServiceIen = null;
    this.fromIen = null;
    this.requestDate = null;
    this.requestDateStr = null;
    this.consultProcedure = null;
    this.urgency = 0;
    this.placeOfConsultIen = null;
    this.attention = 0;
    this.orStatus = 0;
    this.lastAction = 0;
    this.lastAction = 0;
    this.sendingProviderDfn = null;
    this.sendingProviderName = null;
    this.result = null;
    this.modeOfEntry = null;
    this.requestType = 0;
    this.inOut = null;
    this.findings = null;
    this.tiuResultNarrative = 0;
  }

  public int getAttention() {
    return attention;
  }

  public void setAttention(int attention) {
    this.attention = attention;
  }
  
  public String getConsultProcedure() {
    return consultProcedure;
  }
  
  public void setConsultProcedure(String consultProcedure) {
    this.consultProcedure = consultProcedure;
  }
  
  public Date getEntryDate() {
    return entryDate;
  }

  public void setEntryDate(Date entryDate) {
    this.entryDate = entryDate;
  }
  
  public Date getRequestDate() {
    return requestDate;
  }
  
  public void setRequestDate(Date requestDate) {
    this.requestDate = requestDate;
  }
  
  public String getEntryDateStr() {
    return entryDateStr;
  }
  
  public void setEntryDateStr(String entryDate) {
    this.entryDateStr = entryDate;
  }
  
  public String getFindings() {
    return findings;
  }
  
  public void setFindings(String findings) {
    this.findings = findings;
  }
  
  public String getForeignConsultFileNum() {
    return foreignConsultFileNum;
  }

  public void setForeignConsultFileNum(String foreignConsultingFileNum) {
    this.foreignConsultFileNum = foreignConsultingFileNum;
  }
  
  public String getFromIen() {
    return fromIen;
  }
  
  public void setFromIen(String fromIen) {
    this.fromIen = fromIen;
  }
  
  public String getInOut() {
    return inOut;
  }
  
  public void setInOut(String inOut) {
    this.inOut = inOut;
  }
  
  public int getLastAction() {
    return lastAction;
  }
  
  public void setLastAction(int lastAction) {
    this.lastAction = lastAction;
  }
  
  public String getModeOfEntry() {
    return modeOfEntry;
  }
  
  public void setModeOfEntry(String modeOfEntry) {
    this.modeOfEntry = modeOfEntry;
  }
  
  public String getOrderingFacilityIen() {
    return orderingFacilityIen;
  }
  
  public void setOrderingFacilityIen(String orderingFacilityIen) {
    this.orderingFacilityIen = orderingFacilityIen;
  }
  
  public String getOrFileNumber() {
    return orFileNumber;
  }
  
  public void setOrFileNumber(String orFileNumber) {
    this.orFileNumber = orFileNumber;
  }
  
  public int getOrStatus() {
    return orStatus;
  }
  
  public void setOrStatus(int orStatus) {
    this.orStatus = orStatus;
  }
  
  public String getPatientLocationIen() {
    return patientLocationIen;
  }
  
  public void setPatientLocationIen(String patientLocationIen) {
    this.patientLocationIen = patientLocationIen;
  
  }
  
  public String getPlaceOfConsultIen() {
    return placeOfConsultIen;
  }
  
  public void setPlaceOfConsultIen(String placeOfConsultIen) {
    this.placeOfConsultIen = placeOfConsultIen;
  }
  
  public String getRequestDateStr() {
    return requestDateStr;
  }
  
  public void setRequestDateStr(String requestDate) {
    this.requestDateStr = requestDate;
  }
  
  public int getRequestType() {
    return requestType;
  }
  
  public void setRequestType(int requestType) {
    this.requestType = requestType;
  }
  
  public String getResult() {
    return result;
  }
  
  public void setResult(String result) {
    this.result = result;
  }
  
  public String getSendingProviderDfn() {
    return sendingProviderDfn;
  }
  
  public void setSendingProviderDfn(String sendingProviderDfn) {
    this.sendingProviderDfn = sendingProviderDfn;
  }
  
  public String getSendingProviderName() {
    return sendingProviderName;
  }
  
  public void setSendingProviderName(String sendingProviderName) {
    this.sendingProviderName = sendingProviderName;
  }
  
  public int getTiuResultNarrative() {
    return tiuResultNarrative;
  }
  
  public void setTiuResultNarrative(int tiuResultNarrative) {
    this.tiuResultNarrative = tiuResultNarrative;
  }
  
  public String getToServiceIen() {
    return toServiceIen;
  }
  
  public void setToServiceIen(String toServiceIen) {
    this.toServiceIen = toServiceIen;
  }
  
  public int getUrgency() {
    return urgency;
  }
  
  public void setUrgency(int urgency) {
    this.urgency = urgency;
  }
}
