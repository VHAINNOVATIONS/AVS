package gov.va.med.lom.javaBroker.rpc;

import gov.va.med.lom.javaBroker.log.LogChannel;
import gov.va.med.lom.javaBroker.log.Logger;
import gov.va.med.lom.javaBroker.util.StringUtils;

import java.util.ArrayList;

public abstract class AbstractRpc {
  
  /*
   * FIELDS
   */
  protected RpcBroker rpcBroker;
  protected String api;                 // the rpc api
  protected String context;             // the rpc context
  protected String dfn;                 // the patient dfn
  protected String duz;                 // the user duz
  protected String currentContext;      // the saved context of the rpc broker
  protected boolean returnRpcResult;    // flag to return raw RPC results from VistA
  
  /*
   * CONSTRUCTORS
   */
  public AbstractRpc() {
    super();
    this.rpcBroker = null;
    this.api = null;
    this.context = null;
    this.dfn = null;
    this.duz = null;
    this.currentContext = null;
    this.returnRpcResult = false;
  }
  
  public AbstractRpc(RpcBroker rpcBroker) throws BrokerException {
    this();
    this.rpcBroker = rpcBroker;
    if (rpcBroker != null) {
      // Save the current RPC broker context 
      currentContext = rpcBroker.getCurrentContext();
      // Check if the rpc broker is connected/signed on
      // and re-signon if not.
      if (!rpcBroker.isConnected())
        rpcBroker.doSignon();
    }
  }
  
  public AbstractRpc(RpcBroker rpcBroker, String api, String context) throws BrokerException {
    this(rpcBroker);
    this.api = api;
    this.context = context;
    rpcBroker.createContext(context);
  }
  
  public AbstractRpc(RpcBroker rpcBroker, String api, 
                     String context, String dfn, String duz) throws BrokerException {
    this(rpcBroker, api, context);
    this.dfn = dfn;
    this.duz = duz;
  }
  
  /*
   * RPC BROKER METHODS
   */
  // Public Methods
  public boolean connect() throws BrokerException {
    return rpcBroker.connect();
  }
  
  public boolean connect(String server, int port) throws BrokerException {
    if (rpcBroker == null)
      rpcBroker = new RpcBroker(server, port);
    else {
      rpcBroker.setServer(server);
      rpcBroker.setListenerPort(port);
    }
    return rpcBroker.connect();
  }  
  
  public boolean disconnect() throws BrokerException {
    return rpcBroker.disconnect();
  }  
  
  // Protected Methods
  protected void clearParameters() {
    if (rpcBroker != null)
      rpcBroker.doClearParams();
  }
  
  protected void revertContext() throws BrokerException {
    // Revert back to the previous RPC broker context
    if (rpcBroker != null)
      rpcBroker.setCurrentContext(currentContext);   
  } 
   
  protected ArrayList lCall() throws BrokerException {
    String results = rpcBroker.call(this.api);
    return StringUtils.getArrayList(results);
  }

  protected ArrayList lCall(String api) throws BrokerException {
    this.api = api;    
    String results = rpcBroker.call(api);
    return StringUtils.getArrayList(results);
  }  
  
  protected ArrayList lCall(String api, Object[] paramArray) throws BrokerException {
    this.api = api;
    String results = rpcBroker.call(api, paramArray);
    return StringUtils.getArrayList(results);
  }

  protected ArrayList lCall(String api, String ien) throws BrokerException {
    this.api = api;
    String results = rpcBroker.call(api, ien);
    return StringUtils.getArrayList(results);    
  }
    
  protected String sCall() throws BrokerException {
    return rpcBroker.call(this.api);
  }

  protected String sCall(String api) throws BrokerException {
    this.api = api;
    return rpcBroker.call(api);
  }  
  
  protected String call(Object[] paramArray) throws BrokerException {
    return rpcBroker.call(this.api, paramArray);
  }  

  protected String sCall(String api, Object[] paramArray) throws BrokerException {
    this.api = api;
    return rpcBroker.call(api, paramArray);
  }

  protected String sCall(String api, String ien) throws BrokerException {
    this.api = api;
    return rpcBroker.call(api, ien);
  }

  /*
   * LOGGING METHODS
   */
  protected void logInfo(String msg) {
    log(Logger.INFO, msg);
  }
  
  protected void logDebug(String msg) {
    log(Logger.DEBUG, msg);
  }  
  
  protected void logError(String msg) {
    log(Logger.ERROR, msg);
  }
  
  protected void logWarning(String msg) {
    log(Logger.WARNING, msg);
  }
  
  protected void log(int level, String msg) {
    rpcBroker.log(level, msg);
  }
  
  /*
   * PROPERTY ACCESSORS
   */
  // Protected Methods
  protected String getApi() {
    return api;
  }
  
  protected void setApi(String api) {
    this.api = api;
    if (rpcBroker != null) {
      rpcBroker.setRemoteProcedure(api);
    }
  }
  
  protected String getContext() {
    return context;
  }
  
  protected boolean setContext(String context) throws BrokerException {
    this.context = context;
    if (rpcBroker != null)
      return rpcBroker.createContext(context);
    return false;
  }
  
  protected String getDfn() {
    return dfn;
  }
  
  protected void setDfn(String dfn) {
    this.dfn = dfn;
  }
  
  protected String getDuz() {
    return duz;
  }

  protected void setDuz(String duz) {
    this.duz = duz;
  }

  protected String getCurrentContext() {
    return currentContext;
  }
  
  protected void setCurrentContext(String currentContext) {
    this.currentContext = currentContext;
  }  
  
  // Public Methods
  public  boolean getReturnRpcResult() {
    return returnRpcResult;
  }
  
  public void setReturnRpcResult(boolean returnRpcResult) {
    this.returnRpcResult = returnRpcResult;
  }  
  
  public RpcBroker getRpcBroker() {
    return rpcBroker;
  }
  
  public void setRpcBroker(RpcBroker rpcBroker) {
    this.rpcBroker = rpcBroker;
  }
  
  public static LogChannel getLogChannel() {
    return RpcBroker.getLogChannel();
  }
  
  public static void setLogChannel(LogChannel logChannel) {
    RpcBroker.setLogChannel(logChannel);
  }
  
  public BrokerException getCreateBrokerException(Exception e, String action, int code) {
    return rpcBroker.createBrokerException(e, action, code);
  }  
  
  public BrokerException getCreateContextException(String context) {
    Exception e = new Exception("Could not create RPC context: " + context);
    if (rpcBroker != null)
      return rpcBroker.createBrokerException(e, "create context", IRpcProtocol.XWB_UNABLE_TO_CREATE_CONTEXT);
    else
      return new BrokerException(e, "create context", IRpcProtocol.XWB_UNABLE_TO_CREATE_CONTEXT, 
                                 null, null, null, null, null);
  }
  
}
