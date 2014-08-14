package gov.va.med.lom.javaBroker.rpc.patient.models;

import gov.va.med.lom.javaBroker.rpc.BaseBean;

import java.io.Serializable;
import java.util.Date;

public class TiuNoteHeader extends BaseBean implements Serializable {

  private String title;
  private String referenceDatetimeStr;
  private Date referenceDatetime;
  private String patientName;
  private String authorDuz;
  private String authorName;
  private String hospitalLocation;
  private String signatureStatus;
  private String visitDatetimeStr;
  private Date visitDatetime;
  private String dischargeDatetimeStr;
  private Date dischargeDatetime;
  private String requestIen;
  private int numAssociatedImages;
  private String subject;
  private boolean hasChildren;
  private String parentIen;
  private String documentClassIen;
  private String documentClass;
  private String expectedSignerDuz;
  
  public TiuNoteHeader() {
    this.title = null;
    this.referenceDatetimeStr = null;
    this.referenceDatetime = null;
    this.patientName = null;
    this.authorDuz = null;
    this.authorName = null;
    this.hospitalLocation = null;
    this.signatureStatus = null;
    this.visitDatetimeStr = null;
    this.visitDatetime = null;
    this.dischargeDatetimeStr = null;
    this.dischargeDatetime = null;
    this.requestIen = null;
    this.numAssociatedImages = 0;
    this.subject = null;
    this.hasChildren = false;
    this.parentIen = null;
    this.documentClassIen = null;
    this.documentClass = null;
    this.expectedSignerDuz = null;
  }
  
  public String getAuthorDuz() {
    return authorDuz;
  }
  
  public void setAuthorDuz(String authorDuz) {
    this.authorDuz = authorDuz;
  }
  
  public String getAuthorName() {
    return authorName;
  }
  
  public void setAuthorName(String authorName) {
    this.authorName = authorName;
  }
  
  public String getDischargeDatetimeStr() {
    return dischargeDatetimeStr;
  }
  
  public void setDischargeDatetimeStr(String dischargeDateTime) {
    this.dischargeDatetimeStr = dischargeDateTime;
  }
  
  public boolean getHasChildren() {
    return hasChildren;
  }
  
  public void setHasChildren(boolean hasChildren) {
    this.hasChildren = hasChildren;
  }
  
  public String getHospitalLocation() {
    return hospitalLocation;
  }
  
  public void setHospitalLocation(String hospitalLocation) {
    this.hospitalLocation = hospitalLocation;
  }
  
  public int getNumAssociatedImages() {
    return numAssociatedImages;
  }
  
  public void setNumAssociatedImages(int numAssociatedImages) {
    this.numAssociatedImages = numAssociatedImages;
  }
  
  public String getParentIen() {
    return parentIen;
  }
  
  public void setParentIen(String parentIen) {
    this.parentIen = parentIen;
  }
  
  public String getPatientName() {
    return patientName;
  }
  
  public void setPatientName(String patientName) {
    this.patientName = patientName;
  }
  
  public String getReferenceDatetimeStr() {
    return referenceDatetimeStr;
  }
  
  public void setReferenceDatetimeStr(String referenceDateTime) {
    this.referenceDatetimeStr = referenceDateTime;
  }
  
  public String getRequestIen() {
    return requestIen;
  }
  
  public void setRequestIen(String requestIen) {
    this.requestIen = requestIen;
  }
  
  public String getSignatureStatus() {
    return signatureStatus;
  }
  
  public void setSignatureStatus(String signatureStatus) {
    this.signatureStatus = signatureStatus;
  }
  
  public String getSubject() {
    return subject;
  }
  
  public void setSubject(String subject) {
    this.subject = subject;
  }
  
  public String getTitle() {
    return title;
  }
  
  public void setTitle(String title) {
    this.title = title;
  }
  
  public String getVisitDatetimeStr() {
    return visitDatetimeStr;
  }
  
  public void setVisitDatetimeStr(String visitDateTime) {
    this.visitDatetimeStr = visitDateTime;
  }
  
  public Date getDischargeDatetime() {
    return dischargeDatetime;
  }

  public void setDischargeDatetime(Date dischargeDatetime) {
    this.dischargeDatetime = dischargeDatetime;
  }
  
  public Date getReferenceDatetime() {
    return referenceDatetime;
  }
  
  public void setReferenceDatetime(Date referenceDatetime) {
    this.referenceDatetime = referenceDatetime;
  }
  
  public Date getVisitDatetime() {
    return visitDatetime;
  }
  
  public void setVisitDatetime(Date visitDatetime) {
    this.visitDatetime = visitDatetime;
  }
  

  public String getDocumentClassIen() {
    return documentClassIen;
  }

  public void setDocumentClassIen(String documentClassIen) {
    this.documentClassIen = documentClassIen;
  }

  public String getDocumentClass() {
    return documentClass;
  }

  public void setDocumentClass(String documentClass) {
    this.documentClass = documentClass;
  }

  public String getExpectedSignerDuz() {
    return expectedSignerDuz;
  }

  public void setExpectedSignerDuz(String expectedSignerDuz) {
    this.expectedSignerDuz = expectedSignerDuz;
  }
  
}