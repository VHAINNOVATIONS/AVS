package gov.va.med.lom.javaBroker.rpc.patient.models;

import gov.va.med.lom.javaBroker.rpc.BaseBean;

import java.io.Serializable;

public class PostingsList extends BaseBean implements Serializable {

  private Posting[] postings;
  
  public PostingsList() {
    this.postings = null;
  }
  
  public Posting[] getPostings() {
    return postings;
  }
  
  public void setPostings(Posting[] postings) {
    this.postings = postings;
  }
}
