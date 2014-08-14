package gov.va.med.lom.javaBroker.rpc.user.models;

import gov.va.med.lom.javaBroker.rpc.BaseBean;

import java.io.Serializable;

public class Signer extends BaseBean implements Serializable {

  private String name;
  private String comment;
  
  public Signer() {
    this.name = null;
    this.comment = null; 
  }
  
  public String getName() {
    return name;
  }
  
  public void setName(String name) {
    this.name = name;
  }
  
  public String getComment() {
    return comment;
  }

  public void setComment(String comment) {
    this.comment = comment;
  }
}
