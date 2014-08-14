package gov.va.med.lom.javaBroker.rpc.patient.models;

import java.io.Serializable;

public class NewTiuNote implements Serializable {

  private String dfn;
  private String titleIen;
  private String visitLocationIen;
  private String authorDuz;
  private String cosignerDuz;
  private String parentNoteIen;
  private String packageRef;
  private String visitStr; 
  private String subject; 
  private boolean cpt;
  private String visitDate;
  private String refDate;
  private String dictDate;
  private boolean suppress; 
  private String text;
  
  public NewTiuNote() {
    this.dfn  = null;
    this.titleIen = null;
    this.visitLocationIen = null;
    this.authorDuz = null;
    this.cosignerDuz = null;
    this.parentNoteIen = null;
    this.packageRef = null;
    this.visitStr = null;
    this.subject = null;
    this.visitDate = null;
    this.refDate = null;
    this.dictDate = null;
    this.cpt = false;
    this.suppress = false;
    this.text = null;
  }
  
  public String getAuthorDuz() {
    return authorDuz;
  }

  public void setAuthorDuz(String authorDuz) {
    this.authorDuz = authorDuz;
  }
  
  public String getTitleIen() {
    return titleIen;
  }
  
  public void setTitleIen(String titleIen) {
    this.titleIen = titleIen;
  }
  
  public String getCosignerDuz() {
    return cosignerDuz;
  }
  
  public void setCosignerDuz(String cosignerDuz) {
    this.cosignerDuz = cosignerDuz;
  }
  
  public boolean getCpt() {
    return cpt;
  }
  
  public void setCpt(boolean cpt) {
    this.cpt = cpt;
  }
  
  public String getText() {
    return text;
  }
  
  public void setText(String text) {
    this.text = text;
  }
  
  public String getPackageRef() {
    return packageRef;
  }
  
  public void setPackageRef(String packageRef) {
    this.packageRef = packageRef;
  }
  
  public String getParentNoteIen() {
    return parentNoteIen;
  }
  
  public void setParentNoteIen(String parentNoteIen) {
    this.parentNoteIen = parentNoteIen;
  }
  
  public String getDfn() {
    return dfn;
  }
  
  public void setDfn(String dfn) {
    this.dfn = dfn;
  }
  
  public String getSubject() {
    return subject;
  }
  
  public void setSubject(String subject) {
    this.subject = subject;
  }
  
  public boolean getSuppress() {
    return suppress;
  }
  
  public void setSuppress(boolean suppress) {
    this.suppress = suppress;
  }
  
  public String getVisitLocationIen() {
    return visitLocationIen;
  }
  
  public void setVisitLocationIen(String visitLocationIen) {
    this.visitLocationIen = visitLocationIen;
  }
  
  public String getVisitStr() {
    return visitStr;
  }
  
  public void setVisitStr(String visitStr) {
    this.visitStr = visitStr;
  }
  
  public String getDictDate() {
    return dictDate;
  }

  public void setDictDate(String dictDate) {
    this.dictDate = dictDate;
  }
  
  public String getRefDate() {
    return refDate;
  }
  
  public void setRefDate(String refDate) {
    this.refDate = refDate;
  }
  
  public String getVisitDate() {
    return visitDate;
  }
  
  public void setVisitDate(String visitDate) {
    this.visitDate = visitDate;
  }
  
}
