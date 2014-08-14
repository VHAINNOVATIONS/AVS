package gov.va.med.lom.javaBroker.rpc.lists.models;

import gov.va.med.lom.javaBroker.rpc.BaseBean;

import java.io.Serializable;

public class ProceduresList extends BaseBean implements Serializable {

  private Procedure[] procedures;
  
  public ProceduresList() {
    this.procedures = null;
  }

  public Procedure[] getProcedures() {
    return procedures;
  }
  
  public void setProcedures(Procedure[] procedures) {
    this.procedures = procedures;
  }
}
