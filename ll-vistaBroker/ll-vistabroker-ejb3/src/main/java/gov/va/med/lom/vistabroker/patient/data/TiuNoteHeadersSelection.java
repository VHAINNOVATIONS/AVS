package gov.va.med.lom.vistabroker.patient.data;

import java.io.Serializable;
import java.util.Date;

public class TiuNoteHeadersSelection implements Serializable {

  private String noteClass; 
  private int noteStatus;
  private Date beginDate;
  private Date endDate;
  private String authorDuz; 
  private int limit;
  private boolean ascending;
  private boolean showAddenda;
  
  public TiuNoteHeadersSelection() {
    this.noteClass = null;
    this.noteStatus = 0;
    this.beginDate = null;
    this.endDate = null;
    this.authorDuz = null;
    this.limit = 0;
    this.ascending = false;
    this.showAddenda = false;
  }
  
  public boolean getAscending() {
    return ascending;
  }
  
  public void setAscending(boolean ascending) {
    this.ascending = ascending;
  }
  
  public String getAuthorDuz() {
    return authorDuz;
  }
  
  public void setAuthorDuz(String authorDuz) {
    this.authorDuz = authorDuz;
  }
  
  public Date getBeginDate() {
    return beginDate;
  }
  
  public void setBeginDate(Date beginDate) {
    this.beginDate = beginDate;
  }
  
  public Date getEndDate() {
    return endDate;
  }
  
  public void setEndDate(Date endDate) {
    this.endDate = endDate;
  }
  
  public int getLimit() {
    return limit;
  }
  
  public void setLimit(int limit) {
    this.limit = limit;
  }
  
  public int getNoteStatus() {
    return noteStatus;
  }
  
  public void setNoteStatus(int noteStatus) {
    this.noteStatus = noteStatus;
  }
  
  public String getNoteClass() {
    return noteClass;
  }
  
  public void setNoteClass(String noteClass) {
    this.noteClass = noteClass;
  }
  
  public boolean getShowAddenda() {
    return showAddenda;
  }
  
  public void setShowAddenda(boolean showAddenda) {
    this.showAddenda = showAddenda;
  }
}
