package gov.va.med.lom.javaBroker.rpc.patient.models;

import gov.va.med.lom.javaBroker.rpc.BaseBean;

import java.io.Serializable;

public class ProblemsList extends BaseBean implements Serializable {

  private Problem[] problems;
  private String status;
  
  public ProblemsList() {
    this.problems = null;
    this.status = null;
  }
  
  public Problem[] getProblems() {
    return problems;
  }
  
  public void setProblems(Problem[] problems) {
    this.problems = problems;
  }
  
  public String getStatus() {
    return status;
  }
  
  public void setStatus(String status) {
    this.status = status;
  }
}
