package gov.va.med.lom.vistabroker.exception;

public class VistaBrokerSocketTimeoutException extends VistaBrokerException {

    public static final String VB_SOCKET_TIMEOUT_EXCEPTION = "vistabroker.socket.timeout.exception";
    
    private int timeout;

    // constructors
    public VistaBrokerSocketTimeoutException() {
      super();
    }
    
    public VistaBrokerSocketTimeoutException(Exception e) {
      super(e);
    }
    
    public VistaBrokerSocketTimeoutException(String msg, Exception e) {
      super(msg,e);
    }
    
    public VistaBrokerSocketTimeoutException(String msg) {
      super(msg);
    }
    
    public VistaBrokerSocketTimeoutException(Exception e, int code, String message, String rpc,
                                             String context, String division, String userId) {
      super(e, code, message, rpc, context, division, userId);
    }
    
    // override to return this exception's key 
    public String getKey() {
      return VB_SOCKET_TIMEOUT_EXCEPTION;
    }
      
    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }
    
    
    
}
