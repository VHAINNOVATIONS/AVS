package gov.va.med.lom.javaBroker.rpc.patient.models;

import gov.va.med.lom.javaBroker.rpc.BaseBean;

import java.util.List;
import java.io.Serializable;

public class LabTest extends BaseBean implements Serializable {

  private String testId;
  private String testName;
  private String itemId;
  private String labSubscript;
  private String lastReqComment;
  private String testReqComment;
  private boolean uniqueCollSamp;
  private String dftlCollSamp;
  private List<String> specimenList;
  private List<String> urgencyList;
  private List<String> curWardComment;
  
  public String getTestId() {
    return testId;
  }
  public void setTestId(String testId) {
    this.testId = testId;
  }
  public String getTestName() {
    return testName;
  }
  public void setTestName(String testName) {
    this.testName = testName;
  }
  public String getItemId() {
    return itemId;
  }
  public void setItemId(String itemId) {
    this.itemId = itemId;
  }
  public String getLastReqComment() {
    return lastReqComment;
  }
  public void setLastReqComment(String lastReqComment) {
    this.lastReqComment = lastReqComment;
  }
  public String getTestReqComment() {
    return testReqComment;
  }
  public void setTestReqComment(String testReqComment) {
    this.testReqComment = testReqComment;
  }
  public boolean isUniqueCollSamp() {
    return uniqueCollSamp;
  }
  public void setUniqueCollSamp(boolean uniqueCollSamp) {
    this.uniqueCollSamp = uniqueCollSamp;
  }
  public String getDftlCollSamp() {
    return dftlCollSamp;
  }
  public void setDftlCollSamp(String dftlCollSamp) {
    this.dftlCollSamp = dftlCollSamp;
  }
  public List<String> getSpecimenList() {
    return specimenList;
  }
  public void setSpecimenList(List<String> specimenList) {
    this.specimenList = specimenList;
  }
  public List<String> getUrgencyList() {
    return urgencyList;
  }
  public void setUrgencyList(List<String> urgencyList) {
    this.urgencyList = urgencyList;
  }
  public List<String> getCurWardComment() {
    return curWardComment;
  }
  public void setCurWardComment(List<String> curWardComment) {
    this.curWardComment = curWardComment;
  }
  public String getLabSubscript() {
    return labSubscript;
  }
  public void setLabSubscript(String labSubscript) {
    this.labSubscript = labSubscript;
  }
  
}
