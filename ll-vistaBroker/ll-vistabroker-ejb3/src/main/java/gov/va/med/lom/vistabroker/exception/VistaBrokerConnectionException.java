package gov.va.med.lom.vistabroker.exception;

public class VistaBrokerConnectionException extends VistaBrokerException {

  public static final String VB_CONNECTION_EXCEPTION = "vistabroker.connection.exception";
  
  // constructors
  public VistaBrokerConnectionException() {
    super();
  }
  
  public VistaBrokerConnectionException(Exception e) {
    super(e);
  }
  
  public VistaBrokerConnectionException(String msg, Exception e) {
    super(msg,e);
  }
  
  public VistaBrokerConnectionException(String msg) {
    super(msg);
  }
  
  public VistaBrokerConnectionException(Exception e, int code, String message, String rpc,
                                        String context, String division, String userId) {
    super(e, code, message, rpc, context, division, userId);
  }
  
  // override to return this exception's key 
  public String getKey() {
    return VB_CONNECTION_EXCEPTION;
  }
   
}
