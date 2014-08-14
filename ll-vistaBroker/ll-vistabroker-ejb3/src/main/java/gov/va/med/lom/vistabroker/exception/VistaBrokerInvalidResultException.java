package gov.va.med.lom.vistabroker.exception;

public class VistaBrokerInvalidResultException extends VistaBrokerException {

  public static final String VB_INVALID_RESULT_EXCEPTION = "vistabroker.invalid.result.exception";
  
  // constructors
  public VistaBrokerInvalidResultException() {
    super();
  }
  
  public VistaBrokerInvalidResultException(Exception e) {
    super(e);
  }
  
  public VistaBrokerInvalidResultException(String msg, Exception e) {
    super(msg,e);
  }
  
  public VistaBrokerInvalidResultException(String msg) {
    super(msg);
  }
  
  public VistaBrokerInvalidResultException(Exception e, int code, String message, String rpc,
                                           String context, String division, String userId) {
    super(e, code, message, rpc, context, division, userId);
  }
  
  // override to return this exception's key 
  public String getKey() {
    return VB_INVALID_RESULT_EXCEPTION;
  }
    
}
