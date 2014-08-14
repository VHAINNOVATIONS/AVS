package gov.va.med.lom.vistabroker.exception;

public class VistaBrokerSecurityException extends VistaBrokerException {

  public static final String VB_SECURITY_EXCEPTION = "vistabroker.security.exception";

  // constructors
  public VistaBrokerSecurityException() {
    super();
  }
  
  public VistaBrokerSecurityException(Exception e) {
    super(e);
  }
  
  public VistaBrokerSecurityException(String msg, Exception e) {
    super(msg,e);
  }
  
  public VistaBrokerSecurityException(String msg) {
    super(msg);
  }
  
  public VistaBrokerSecurityException(Exception e, int code, String message, String rpc,
                                      String context, String division, String userId) {
    super(e, code, message, rpc, context, division, userId);
  }
  
  // override to return this exception's key 
  public String getKey() {
    return VB_SECURITY_EXCEPTION;
  }
    
}
