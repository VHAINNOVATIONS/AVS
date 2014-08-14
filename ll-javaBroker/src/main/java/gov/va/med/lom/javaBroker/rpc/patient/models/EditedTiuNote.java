package gov.va.med.lom.javaBroker.rpc.patient.models;

import java.io.Serializable;

import gov.va.med.lom.javaBroker.rpc.BaseBean;

public class EditedTiuNote extends BaseBean implements Serializable {

  private int docType;
  private boolean newNote;
  private String titleIen;
  private String titleName;
  private double fmDateTime;
  private String authorDuz;
  private String authorName;
  private String cosignerDuz;
  private String cosignerName;
  private String subject;
  private String locationIen;
  private String locationName;
  private double fmVisitDate;
  private String packageRef;
  private String packageIen;
  private String packagePointer;
  private boolean needCPT;
  private int addend;
  private String lastCosignerDuz;
  private String lastCosignerName;
  private String parentId;
  private int clinicalProcedureSummaryCode;
  private double clinicalProcedureFMDateTime;
  private String text;
  
  public EditedTiuNote() {
    this.docType = 0;
    this.newNote = false;
    this.titleIen = null;
    this.titleName = null;
    this.fmDateTime = 0.0;
    this.authorDuz = null;
    this.authorName = null;
    this.cosignerDuz = null;
    this.cosignerName = null;
    this.subject = null;
    this.locationIen = null;
    this.locationName = null;
    this.fmVisitDate = 0.0;
    this.packageRef = null;
    this.packageIen = null;
    this.packagePointer = null;
    this.needCPT = false;
    this.addend = 0;
    this.lastCosignerDuz = null;
    this.lastCosignerName = null;
    this.parentId = null;
    this.clinicalProcedureSummaryCode = 0;
    this.clinicalProcedureFMDateTime = 0.0;
    this.text = null;
  }

  public int getAddend() {
    return addend;
  }

  public void setAddend(int addend) {
    this.addend = addend;
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

  public double getClinicalProcedureFMDateTime() {
    return clinicalProcedureFMDateTime;
  }

  public void setClinicalProcedureFMDateTime(double clinicalProcedureFMDateTime) {
    this.clinicalProcedureFMDateTime = clinicalProcedureFMDateTime;
  }

  public int getClinicalProcedureSummaryCode() {
    return clinicalProcedureSummaryCode;
  }

  public void setClinicalProcedureSummaryCode(int clinicalProcedureSummaryCode) {
    this.clinicalProcedureSummaryCode = clinicalProcedureSummaryCode;
  }

  public String getCosignerDuz() {
    return cosignerDuz;
  }

  public void setCosignerDuz(String cosignerDuz) {
    this.cosignerDuz = cosignerDuz;
  }

  public String getCosignerName() {
    return cosignerName;
  }

  public void setCosignerName(String cosignerName) {
    this.cosignerName = cosignerName;
  }

  public int getDocType() {
    return docType;
  }

  public void setDocType(int docType) {
    this.docType = docType;
  }

  public double getFmDateTime() {
    return fmDateTime;
  }

  public void setFmDateTime(double fmDateTime) {
    this.fmDateTime = fmDateTime;
  }

  public double getFmVisitDate() {
    return fmVisitDate;
  }

  public void setFmVisitDate(double fmVisitDate) {
    this.fmVisitDate = fmVisitDate;
  }

  public boolean getNewNote() {
    return newNote;
  }

  public void setNewNote(boolean newNote) {
    this.newNote = newNote;
  }

  public String getLastCosignerDuz() {
    return lastCosignerDuz;
  }

  public void setLastCosignerDuz(String lastCosignerDuz) {
    this.lastCosignerDuz = lastCosignerDuz;
  }

  public String getLastCosignerName() {
    return lastCosignerName;
  }

  public void setLastCosignerName(String lastCosignerName) {
    this.lastCosignerName = lastCosignerName;
  }

  public String getLocationIen() {
    return locationIen;
  }

  public void setLocationIen(String locationIen) {
    this.locationIen = locationIen;
  }

  public String getLocationName() {
    return locationName;
  }

  public void setLocationName(String locationName) {
    this.locationName = locationName;
  }

  public boolean getNeedCPT() {
    return needCPT;
  }

  public void setNeedCPT(boolean needCPT) {
    this.needCPT = needCPT;
  }

  public String getPackageIen() {
    return packageIen;
  }

  public void setPackageIen(String packageIen) {
    this.packageIen = packageIen;
  }

  public String getPackagePointer() {
    return packagePointer;
  }

  public void setPackagePointer(String packagePointer) {
    this.packagePointer = packagePointer;
  }

  public String getPackageRef() {
    return packageRef;
  }

  public void setPackageRef(String packageRef) {
    this.packageRef = packageRef;
  }

  public String getParentId() {
    return parentId;
  }

  public void setParentId(String parentId) {
    this.parentId = parentId;
  }

  public String getSubject() {
    return subject;
  }

  public void setSubject(String subject) {
    this.subject = subject;
  }

  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }

  public String getTitleIen() {
    return titleIen;
  }

  public void setTitleIen(String titleIen) {
    this.titleIen = titleIen;
  }

  public String getTitleName() {
    return titleName;
  }

  public void setTitleName(String titleName) {
    this.titleName = titleName;
  }
  
}
