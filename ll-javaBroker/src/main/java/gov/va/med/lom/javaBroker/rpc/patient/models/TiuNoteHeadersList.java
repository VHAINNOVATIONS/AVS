package gov.va.med.lom.javaBroker.rpc.patient.models;

import java.io.Serializable;

import gov.va.med.lom.javaBroker.rpc.BaseBean;

public class TiuNoteHeadersList extends BaseBean implements Serializable {

  private TiuNoteHeader[] tiuNoteHeaders;
  
  public TiuNoteHeadersList() {
    this.tiuNoteHeaders = null;
  }
  
  public TiuNoteHeader[] getTiuNoteHeaders() {
    return tiuNoteHeaders;
  }
  
  public void setTiuNoteHeaders(TiuNoteHeader[] tiuNoteHeaders) {
    this.tiuNoteHeaders = tiuNoteHeaders;
  }
}
