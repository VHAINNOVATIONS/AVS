package gov.va.med.lom.javaBroker.rpc;

/**
 * Classes that utilize the RpcBroker can handle events that RpcBroker generates by
 * implementing this interface.
 */
public interface IRpcBrokerEventHandler {
  
  /**
   * Occurs just after the connection to the server is opened.
   */
  public void onRpcBrokerConnect();
  
  /**
   * Occurs just after the connection to the server is closed.
   */
  public void onRpcBrokerDisconnect();
  
  /**
   * Occurs just before the client socket writes information to the socket connection.
   * @param data the data to be sent to the server.
   */
  public void onRpcBrokerSend(char[] data);
  
  /**
   * Occurs after a socket receives data, but before it passes the data to the application.
   * @param data the data received from the server.
   */
  public void onRpcBrokerReceive(char[] data);
  
  /**
   * Occurs when the socket fails in making, using, or shutting down a connection.
   * @param BrokerException the broker exception that was thrown.
   */
  public void onRpcBrokerError(BrokerException be);
  
  /**
   * Occurs when the broker context has been successfulloy created.
   * @param context the broker context that was created. 
   */
  public void onRpcBrokerCreateContext(String context);  
  
  /**
   * Occurs when the client has successfully signed the user on to the broker server. 
   */
  public void onRpcBrokerSignon(String duz);

}
