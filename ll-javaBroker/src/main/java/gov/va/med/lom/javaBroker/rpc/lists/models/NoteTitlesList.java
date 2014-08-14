package gov.va.med.lom.javaBroker.rpc.lists.models;

import java.io.Serializable;

import gov.va.med.lom.javaBroker.rpc.BaseBean;

public class NoteTitlesList extends BaseBean implements Serializable {

  private NoteTitle[] noteTitles;
  
  public NoteTitlesList() {
    this.noteTitles = null;
  }
  
  public NoteTitle[] getNoteTitles() {
    return noteTitles;
  }
  
  public void setNoteTitles(NoteTitle[] noteTitles) {
    this.noteTitles = noteTitles;
  }
}
