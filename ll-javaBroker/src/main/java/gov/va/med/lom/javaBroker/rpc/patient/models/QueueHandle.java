package gov.va.med.lom.javaBroker.rpc.patient.models;

import gov.va.med.lom.javaBroker.rpc.BaseBean;

import java.io.Serializable;

public class QueueHandle extends BaseBean implements Serializable {

  private String handle;
  private int taskNo;
  
  public QueueHandle() {
    this.handle = null;
    this.taskNo = 0;
  }

  public String getHandle() {
    return handle;
  }

  public void setHandle(String handle) {
    this.handle = handle;
  }

  public int getTaskNo() {
    return taskNo;
  }

  public void setTaskNo(int taskNo) {
    this.taskNo = taskNo;
  }
  
}
