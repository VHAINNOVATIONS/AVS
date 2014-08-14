package gov.va.med.lom.javaBroker.rpc.user.models;

import gov.va.med.lom.javaBroker.rpc.RpcBroker;

public class RemoteSignon implements java.io.Serializable {

  private String remoteUserDuz;
  private boolean testSystem;
  private RpcBroker remoteBroker;
  
  public String getRemoteUserDuz() {
    return remoteUserDuz;
  }
  
  public void setRemoteUserDuz(String remoteUserDuz) {
    this.remoteUserDuz = remoteUserDuz;
  }
  
  public boolean isTestSystem() {
    return testSystem;
  }
  
  public void setTestSystem(boolean testSystem) {
    this.testSystem = testSystem;
  }
  
  public RpcBroker getRemoteBroker() {
    return remoteBroker;
  }
  
  public void setRemoteBroker(RpcBroker remoteBroker) {
    this.remoteBroker = remoteBroker;
  }
  
}
