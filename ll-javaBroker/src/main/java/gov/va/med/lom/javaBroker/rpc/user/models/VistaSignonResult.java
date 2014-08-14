package gov.va.med.lom.javaBroker.rpc.user.models;

import gov.va.med.lom.javaBroker.rpc.BaseBean;

public class VistaSignonResult extends BaseBean implements java.io.Serializable {

  private boolean deviceLocked;
  private boolean changeVerifyCode;
  private boolean signonSucceeded;
  private String message;
  private String greeting;
  private String host;
  private int port;
  
  public VistaSignonResult() {
    this.deviceLocked = false;
    this.changeVerifyCode = false;
    this.signonSucceeded = false;
    this.greeting = null;
    this.message = null;
    this.host = null;
    this.port = 0;
  }
  
  public boolean getChangeVerifyCode() {
    return changeVerifyCode;
  }

  public void setChangeVerifyCode(boolean changeVerifyCode) {
    this.changeVerifyCode = changeVerifyCode;
  }
  
  public boolean getDeviceLocked() {
    return deviceLocked;
  }
  
  public void setDeviceLocked(boolean deviceLocked) {
    this.deviceLocked = deviceLocked;
  }
  
  public String getGreeting() {
    return greeting;
  }
  
  public void setGreeting(String error) {
    this.greeting = error;
  }
  
  public String getMessage() {
    return message;
  }
  
  public void setMessage(String message) {
    this.message = message;
  }
  
  public boolean getSignonSucceeded() {
    return signonSucceeded;
  }
  
  public void setSignonSucceeded(boolean signonSucceeded) {
    this.signonSucceeded = signonSucceeded;
  }
  
  public String getHost() {
    return host;
  }
  
  public void setHost(String host) {
    this.host = host;
  }
  
  public int getPort() {
    return port;
  }
  
  public void setPort(int port) {
    this.port = port;
  }
}
