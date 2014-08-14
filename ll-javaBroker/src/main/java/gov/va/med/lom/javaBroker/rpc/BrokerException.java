package	gov.va.med.lom.javaBroker.rpc;

/*
 * Exception indicating that a broker or network error occurred.
 */
public class BrokerException extends Exception {
  
  private Exception e;
  private String action;
  private int code;
  private String mnemonic;
  private String rpc;
  private String sent;
  private String received;
  private String host;
  private int port;

  /*
   * Construct with an exception, w/o action, code, mnemonic.
   */
  public BrokerException (Exception e, String message) {
    super(message, e);
    this.action = null;
    this.code = 0;
    this.mnemonic = null;
    this.rpc = null;
    this.sent = null;
    this.received = null;
    this.host = null;
    this.port = 0;
  }
  
  /*
   * Construct with an action, code, mnemonic, and message.
   */
  public BrokerException (Exception e, String action, int code, 
                          String mnemonic, String message,String rpc,
                          String sent, String received) {
    super(message, e);
    this.e = e;
    this.action = action;
    this.code = code;
    this.mnemonic = mnemonic;
    this.rpc = rpc;
    this.sent = sent;
    this.received = received;
  }
  
  /*
   * Construct with an action, code, mnemonic, host/port, and message.
   */
  public BrokerException (Exception e, String action, int code, 
                          String mnemonic, String message,String rpc,
                          String sent, String received, String host, int port) {
    this(e, action, code, mnemonic, message, rpc, sent, received);
    this.host = host;
    this.port = port;
  }
  
  public Exception getException() {
    return e;
  }
  
  public String getAction() {
  	return action;
  }
  
  public int getCode() {
  	return code;
  }
  
  public String getMnemonic() {
    return mnemonic;  
  }
  
  public String getReceived() {
    return received;
  }

  public String getRpc() {
    return rpc;
  }

  public String getSent() {
    return sent;
  }
  
  
  public String getHost() {
    return host;
  }

  public int getPort() {
    return port;
  }

  public String getMessage() {
    String exMsg = null;
    if (e != null)
      exMsg = e.getMessage();
    else
      exMsg = "";
    StringBuffer msg = new StringBuffer();
    msg.append("Exception: " + exMsg + "\n");
    msg.append("Action: " + action + "\n");
    msg.append("Code: " + code + "\n");
    msg.append("Mnemonic: " + mnemonic + "\n");
    msg.append("RPC: " + rpc + "\n");
    //msg.append("Sent Data: " + sent + "\n");
    //msg.append("Received Data: " + received);
    if ((host != null) && (port > 0))
      msg.append("\nVistA Server: " + host + ":" + port);
    return msg.toString();
  }
  
}
