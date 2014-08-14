package gov.va.med.lom.vistabroker.exception;

public class VistaBrokerIllegalStateException extends VistaBrokerException {

    public static final String VB_ILLEGAL_STATE_EXCEPTION = "vistabroker.illegal.state.exception";
  
    // constructors
    public VistaBrokerIllegalStateException() {
      super();
    }
    
    public VistaBrokerIllegalStateException(Exception e) {
      super(e);
    }
    
    public VistaBrokerIllegalStateException(String msg, Exception e) {
      super(msg,e);
    }
    
    public VistaBrokerIllegalStateException(String msg) {
      super(msg);
    }
    
    public VistaBrokerIllegalStateException(Exception e, int code, String message, String rpc,
                                            String context, String division, String userId) {
      super(e, code, message, rpc, context, division, userId);
    }
    
    // override to return this exception's key 
    public String getKey() {
      return VB_ILLEGAL_STATE_EXCEPTION;
    }
}
