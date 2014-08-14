package gov.va.med.lom.json.model;

import java.util.Collection;

public class BaseJson {
  
  private boolean success;
  private int totalCount;
  @SuppressWarnings("rawtypes")
  private Collection root;
  
  public BaseJson() {
    this.success = true;
  }
  
  public BaseJson(Collection root) {
    this();
    this.root = root;
    if (root != null)
      this.totalCount = root.size();
  }
  
  public BaseJson(Collection root, int totalCount) {
    this();
    this.root = root;
    this.totalCount = totalCount;
  }
  
  @SuppressWarnings("rawtypes")
  public Collection getRoot() {
    return root;
  }
  
  @SuppressWarnings("rawtypes")
  public void setRoot(Collection root) {
    this.root = root;
  }

  public boolean isSuccess() {
    return success;
  }

  public void setSuccess(boolean success) {
    this.success = success;
  }

  public int getTotalCount() {
    return totalCount;
  }

  public void setTotalCount(int totalCount) {
    this.totalCount = totalCount;
  }  
  
}