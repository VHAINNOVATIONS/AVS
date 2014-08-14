package gov.va.med.lom.javaBroker.rpc;

public interface IRpcProtocol {
	
	//  NAMED CONSTANTS
    public static final String RPC_VERSION               = "1.108";
    
    // Broker Application Error Constants
    public static final int XWB_BASE_ERR                 = 20000;
    public static final int XWB_NO_HEAP                  = XWB_BASE_ERR + 1;
    public static final int XWB_M_REJECT                 = XWB_BASE_ERR + 2;
    public static final int XWB_BAD_SIGN_ON              = XWB_BASE_ERR + 4;
    public static final int XWB_BLD_CONNECT_LIST         = XWB_BASE_ERR + 5;
    public static final int XWB_NULL_RPC_VER             = XWB_BASE_ERR + 6;
    public static final int XWB_CONNECT_REFUSED          = XWB_BASE_ERR + 200;
    public static final int XWB_RPC_NOT_REG              = XWB_BASE_ERR + 201;
    public static final int XWB_UNABLE_TO_CREATE_CONTEXT = XWB_BASE_ERR + 202;
    public static final int XWB_NO_DATA_RECEIVED         = XWB_BASE_ERR + 203;
    public static final int XWB_DISCONNECTED_WITH_BYE    = XWB_BASE_ERR + 300;
    public static final int XWB_NO_CONNECTION_TO_SERVER  = XWB_BASE_ERR + 400;
    
    // Create a broker exception
    public BrokerException createBrokerException(Exception e, String action, int code);
    
    // Accessor method for getting the protocol version
    public int getProtocolVersion();
    
    // Accessor methods for the broker version
    public String getRpcVersion();
    public void setRpcVersion(String rpcVersion);
    
    // Accessor methods for the broker event handler object
    public IRpcBrokerEventHandler getEventHandler();
    public void setEventHandler(IRpcBrokerEventHandler eventHandler);
    
    // Accessor methods for retrieving the security, application, and data segments
    public String getSecuritySegment();
	public String getApplicationSegment();
    public String getDataSegment();
    
    // Accessor methods for retrieving the last broker exception and error message
    public BrokerException getBrokerException();
	public String getErrorMessage();
    
    // Methods for retrieving and controlling the status of the socket connection
	public boolean isConnected();
	public boolean connect(String server, int port) throws BrokerException;
	public String call(String remoteProcedure, Params params) throws BrokerException;
	public boolean disconnect() throws BrokerException;
	
}
