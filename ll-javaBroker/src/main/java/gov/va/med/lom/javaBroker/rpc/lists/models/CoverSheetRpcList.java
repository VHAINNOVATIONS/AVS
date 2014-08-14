package gov.va.med.lom.javaBroker.rpc.lists.models;

import gov.va.med.lom.javaBroker.rpc.BaseBean;

import java.io.Serializable;

public class CoverSheetRpcList extends BaseBean implements Serializable {

  private CoverSheetRpc[] coverSheetRpcs;
  
  public CoverSheetRpcList() {
    this.coverSheetRpcs = null;
  }

  public CoverSheetRpc[] getCoverSheetRpcs() {
    return coverSheetRpcs;
  }
  
  public void setCoverSheetRpcs(CoverSheetRpc[] coverSheetRpcs) {
    this.coverSheetRpcs = coverSheetRpcs;
  }
}
