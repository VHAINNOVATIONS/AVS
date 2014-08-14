package gov.va.med.lom.vistabroker.exception;

public class VistaBrokerIllegalArgumentException extends VistaBrokerException {

  public static final String VB_ILLEGAL_ARGUMENT_EXCEPTION = "vistabroker.illegal.argument.exception";
  
  // constructors
  public VistaBrokerIllegalArgumentException() {
    super();
  }
  
  public VistaBrokerIllegalArgumentException(Exception e) {
    super(e);
  }
  
  public VistaBrokerIllegalArgumentException(String msg, Exception e) {
    super(msg,e);
  }
  
  public VistaBrokerIllegalArgumentException(String msg) {
    super(msg);
  }
  
  public VistaBrokerIllegalArgumentException(Exception e, int code, String message, String rpc,
                                             String context, String division, String userId) {
    super(e, code, message, rpc, context, division, userId);
  }
  
  // override to return this exception's key 
  public String getKey() {
    return VB_ILLEGAL_ARGUMENT_EXCEPTION;
  }
    
}
