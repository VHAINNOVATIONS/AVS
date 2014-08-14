package gov.va.med.lom.vistabroker.auth.data;

public class VistaSignonResult implements java.io.Serializable {

  private String duz;
  private String vpid;
  private boolean deviceLocked;
  private boolean changeVerifyCode;
  private boolean signonSucceeded;
  private String message;
  private String greeting;
  
  public VistaSignonResult() {
    this.duz = null;
    this.vpid = null;
    this.deviceLocked = false;
    this.changeVerifyCode = false;
    this.signonSucceeded = false;
    this.greeting = null;
    this.message = null;
  }
  
  public String getDuz() {
    return duz;
  }

  public void setDuz(String duz) {
    this.duz = duz;
  }

  public String getVpid() {
    return vpid;
  }

  public void setVpid(String vpid) {
    this.vpid = vpid;
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
  
}
