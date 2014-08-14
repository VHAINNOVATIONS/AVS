package gov.va.med.lom.javaBroker.rpc.lists.models;

import gov.va.med.lom.javaBroker.rpc.BaseBean;

import java.io.Serializable;

public class NoteTitle extends BaseBean implements Serializable {
  
  private String title;
  
  public NoteTitle() {
    this.title = null;
  }
  
  public String getTitle() {
    return title;
  }
  
  public void setTitle(String title) {
    this.title = title;
  }
}
