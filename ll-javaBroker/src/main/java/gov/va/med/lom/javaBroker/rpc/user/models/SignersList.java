package gov.va.med.lom.javaBroker.rpc.user.models;

import gov.va.med.lom.javaBroker.rpc.BaseBean;

import java.io.Serializable;

public class SignersList extends BaseBean implements Serializable {

  private Signer[] signers;
  
  public SignersList() {
    this.signers = null;
  }
  
  public Signer[] getSigners() {
    return signers;
  }
  
  public void setSigners(Signer[] signers) {
    this.signers = signers;
  }
}
