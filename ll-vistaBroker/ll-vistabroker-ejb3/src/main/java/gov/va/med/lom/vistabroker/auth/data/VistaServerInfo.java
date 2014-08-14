package gov.va.med.lom.vistabroker.auth.data;

public class VistaServerInfo implements java.io.Serializable {
  
  private boolean serverAvailable;
  private String server;
	private String volume;
	private String uci;
	private String port;
	private boolean deviceLocked = false;
	private boolean signonRequired = true;
  private String introMessage;

  public VistaServerInfo() {
    this.serverAvailable = false;
    this.server = null;
    this.volume = null;
    this.uci = null;
    this.port = null;
    this.deviceLocked = false;
    this.signonRequired = false;
    this.introMessage = null;
  }

  public boolean getServerAvailable() {
    return serverAvailable;
  }

  public void setServerAvailable(boolean serverAvailable) {
    this.serverAvailable = serverAvailable;
  }
  
  public boolean getDeviceLocked() {
      return deviceLocked;
  }
  
  public void setDeviceLocked(boolean deviceLocked) {
      this.deviceLocked = deviceLocked;
  }
  
  public String getPort() {
      return port;
  }
  
  public void setPort(String port) {
      this.port = port;
  }
  
  public String getServer() {
      return server;
  }
  
  public void setServer(String server) {
      this.server = server;
  }
  
  public boolean getSignonRequired() {
      return signonRequired;
  }
  
  public void setSignonRequired(boolean signonRequired) {
      this.signonRequired = signonRequired;
  }
  
  public String getUci() {
      return uci;
  }
  
  public void setUci(String uci) {
      this.uci = uci;
  }
  
  public String getVolume() {
      return volume;
  }
  
  public void setVolume(String volume) {
      this.volume = volume;
  }
  
  public String getIntroMessage() {
    return introMessage;
  }
  
  public void setIntroMessage(String introMessage) {
    this.introMessage = introMessage;
  }
  
}