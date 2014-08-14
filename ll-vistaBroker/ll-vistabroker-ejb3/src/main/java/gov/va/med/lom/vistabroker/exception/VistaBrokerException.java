package	gov.va.med.lom.vistabroker.exception;

/*
 * Exception indicating that a broker or network error occurred.
 */
public abstract class VistaBrokerException extends Exception {
  
  protected Exception e;
  protected int code;
  protected String rpc;
  protected String context;
  protected String division;
  protected String userId;
  protected String key;
  
  public VistaBrokerException() {
	  super();
  }
  
  public VistaBrokerException(Exception e) {
	  super(e);
	  this.e = e;
  }
  
  public VistaBrokerException(String msg, Exception e) {
	  super(msg,e);
	  this.e = e;
  }
  
  public VistaBrokerException(String msg) {
	  super(msg);
  }
  
  /*
   * Construct with an action, code, mnemonic, and message.
   */
  public VistaBrokerException(Exception e, int code, String message, String rpc,
                              String context, String division, String userId) {
    super(message, e);
    this.e = e;
    this.code = code;
    this.rpc = rpc;
    this.context = context;
    this.division = division;
    this.userId = userId;
  }
  
  public int getCode() {
  	return this.code;
  }
  
  public String getRpc() {
    return this.rpc;
  }
  
  public String getContext() {
    return this.context;
  }

  public String getDivision() {
    return this.division;
  }

  public Exception getException() {
    return this.e;
  }

  public String getUserId() {
    return this.userId;
  }

  public String getMessage() {
    String exMsg = null;
    if (e != null)
      exMsg = e.getMessage();
    else
      exMsg = "";
    StringBuffer msg = new StringBuffer();
    msg.append("Exception: " + exMsg + "\n");
    msg.append("Code: " + code + "\n");
    msg.append("Message: " + super.getMessage() + "\n");
    msg.append("RPC: " + rpc + "\n");
    msg.append("Context: " + context + "\n");
    msg.append("Division: " + division + "\n");
    msg.append("User ID: " + userId);
    return msg.toString();
  }
  
  public String getKey() {
    return key;
  }
  
}
