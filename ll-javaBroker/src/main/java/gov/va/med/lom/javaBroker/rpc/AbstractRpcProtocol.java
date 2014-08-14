package gov.va.med.lom.javaBroker.rpc;

import java.io.IOException;

import gov.va.med.lom.javaBroker.net.SocketConnection;

public abstract class AbstractRpcProtocol implements IRpcProtocol {

    // Instance Fields
    protected int protocolVersion;
    protected String rpcVersion;
    protected SocketConnection socket;
    protected IRpcBrokerEventHandler eventHandler;
    protected String securitySegment;
    protected String applicationSegment;
    protected String dataSegment;
    protected String errorMessage;
    protected BrokerException brokerException;
    protected String currentRemoteProcedure;
    protected String currentSentData;
    protected String currentReceivedData;
    
    // Constructor
    public AbstractRpcProtocol(int protocolVersion) {
        this.protocolVersion = protocolVersion;
        this.rpcVersion = RPC_VERSION;
        this.errorMessage = "";
        this.socket = null;
    }
    
    // Property Accessor Methods
    public int getProtocolVersion() {
      return protocolVersion;
    }    
    
    public String getRpcVersion() {
        return rpcVersion;
    }
    
    public void setRpcVersion(String rpcVersion) {
        this.rpcVersion = rpcVersion;
    }    
    
    public String getSecuritySegment() {
        return securitySegment;
    }

    public String getApplicationSegment() {
        return applicationSegment;
    }

    public String getDataSegment() {
        return dataSegment;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
    
    public BrokerException getBrokerException() {
        return brokerException;
    }
    
    public boolean isConnected(){
        if (socket != null)
          return socket.isConnected();
        else
          return false;
    }

    public IRpcBrokerEventHandler getEventHandler() {
        return eventHandler;
    }
    
    public void setEventHandler(IRpcBrokerEventHandler eventHandler) {
        this.eventHandler = eventHandler;
    }    
    
    // Utility Methods
    public BrokerException createBrokerException(Exception e, String action, int code) {
        String mnemonic = null;
        String message = null;
        if (code == 0) {
            code = 0;
            if (e != null)
                message = e.getMessage();
            if (message == null)
                message = "";
            mnemonic = "";
        } else {
            switch (code) {
                case IRpcProtocol.XWB_NO_HEAP                  : mnemonic = "Insufficient Heap"; break;
                case IRpcProtocol.XWB_M_REJECT                 : mnemonic = "M Error - Use ^XTER"; break;
                case IRpcProtocol.XWB_BAD_SIGN_ON              : mnemonic = "Sign-on was not completed."; break;
                case IRpcProtocol.XWB_RPC_NOT_REG              : mnemonic = "Remote procedure not registered to application."; break;
                case IRpcProtocol.XWB_BLD_CONNECT_LIST         : mnemonic = "BrokerConnections list could not be created"; break;
                case IRpcProtocol.XWB_NULL_RPC_VER             : mnemonic = "RpcVersion cannot be empty.\nDefault is 0 (zero)."; break;
                case IRpcProtocol.XWB_UNABLE_TO_CREATE_CONTEXT : mnemonic = "Unable to create context";
                case IRpcProtocol.XWB_CONNECT_REFUSED          : mnemonic = "Connection was refused."; break;
                case IRpcProtocol.XWB_DISCONNECTED_WITH_BYE    : mnemonic = "VistA Broker server disconnected with #BYE#.";
                case IRpcProtocol.XWB_NO_DATA_RECEIVED         : mnemonic = "No data received from VistA.";
                case IRpcProtocol.XWB_NO_CONNECTION_TO_SERVER  : mnemonic = "No connection to server.";
                default                                        : mnemonic = String.valueOf(code);
            }
            message = "Error encountered.\nFunction was: " + action + "\n" + "Error was: " + mnemonic;
        }
        if (action == null)
            action = "";
        BrokerException be = null;
        if (socket != null)
          be = new BrokerException(e, action, code, mnemonic, message, 
                                   currentRemoteProcedure, currentSentData, 
                                   currentReceivedData, socket.getRemoteHostAddress(), 
                                   socket.getPort());
        else
          be = new BrokerException(e, action, code, mnemonic, message, 
                                   currentRemoteProcedure, currentSentData, 
                                   currentReceivedData);
        return be;
    }
    
    protected String getServerPacket() throws BrokerException {
        try {
            int c = -1;
            c = socket.read();
            if (c <= 0)
                return null;
            char[] cbuf = new char[c];
            socket.read(cbuf, 0, c);
            return new String(cbuf); 
        } catch(IOException ioe) {
            BrokerException brokerException = 
                    createBrokerException(socket.getLastException(), "receive", 0);
            if (eventHandler != null)
                eventHandler.onRpcBrokerError(brokerException);
            throw brokerException;
        }
    }     
    
    // Abstract methods (implemented by extending classes)
    public abstract boolean connect(String server, int port) throws BrokerException;
    
    public String call(String remoteProcedure, Params params) throws BrokerException {
        this.currentReceivedData = null;
        this.currentSentData = null;
        this.currentRemoteProcedure = remoteProcedure;  
        return null;
    }
    
    // Default implementation of socket disconnect
    public boolean disconnect() throws BrokerException {
        try {
          return socket.close();
        } catch(IOException ioe) {
            throw createBrokerException(ioe, "socket disconnect", 0);
        }
    }
    
    // ISocketEventsCallback interface implementation
    public void onSocketConnect() {
        if (eventHandler != null)
            eventHandler.onRpcBrokerConnect();
    }
      
    public void onSocketDisconnect() {
        // Force the socket to be recreated if reconnected
        socket = null;
        if (eventHandler != null)
            eventHandler.onRpcBrokerDisconnect();
    }
      
    public void onSocketException(Exception e) {
        if (eventHandler != null) {
            BrokerException be = null;
            if ( !(e instanceof BrokerException))
              be = new BrokerException(e, "Error encountered.");
            eventHandler.onRpcBrokerError(be);
        }
    }    
}
