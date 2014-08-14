/*
 * BrokerConnection.java
 * Author: Rob Durkin (rob.durkin@med.va.gov)
 * Version 1.0.1 (08/30/2005)
 * 
 * Wrapper around a RpcBroker object.  
 * Used by BrokerConnectionPool.java
 * 
 */
package gov.va.med.lom.javaBroker.rpc;

import java.util.Date;

public class BrokerConnection {
  
  private RpcBroker rpcBroker = null;
  private boolean leased = false;
  private long lastUsed = new Date().getTime();
  
  /*
   * Constructor: Create a RPC broker connection
   */
  public BrokerConnection(String server, int port) throws BrokerException {
    // Create a rpc broker instance
    rpcBroker = new RpcBroker(server, port);
  }
  
  /*
   * Constructor: Create a RPC broker connection and signon using the supplied A/V codes.
   */
  public BrokerConnection(String server, int port, 
                          String accessCode, String verifyCode) throws BrokerException {
    // Create a rpc broker instance
    rpcBroker = new RpcBroker(server, port);
    // Signon if connected 
    if (isConnected())
      doSignon(accessCode, verifyCode);
  }  

  /*
   * Methods to set or expire or check the status of the lease of a broker connection.
   */
  public void lease() {
    this.leased = true;
  }

  public boolean isLeased() {
    return leased;
  }

  public void expireLease() {
    this.leased = false;
  }

  /*
   * RPC broker methods (add additional methods as needed).
   */ 
  public boolean isConnected() {
    return rpcBroker.isConnected();
  }

  public boolean connect() throws BrokerException {
    updateLastUsed();
    return rpcBroker.connect();
  }  
  
  public boolean disconnect() throws BrokerException {
    return rpcBroker.disconnect();
  }
  
  public String doSignon(String accessCode, String verifyCode) throws BrokerException {
    updateLastUsed();
    return rpcBroker.doSignon(accessCode, verifyCode);
  }
  
  public boolean createContext(String context) throws BrokerException {
    return rpcBroker.createContext(context);
  }
  
  public String call(String api, Object[] paramArray) throws BrokerException {
    updateLastUsed();
    return rpcBroker.call(api, paramArray);
  }    
  
  // Returns the RPC broker instance
  public RpcBroker getRpcBroker() {
    return rpcBroker;
  }
  
  // Returns the time the connection was last used 
  public Date getLastUsed() {
    return new Date(lastUsed);
  }
  
  // Returns the time (in msec) the connection has been inactive.
  public long getInactiveTime() {
    return new Date().getTime() - lastUsed;
  }
  
  // Updates the 'last used' to the current time
  private void updateLastUsed() {
    lastUsed = new Date().getTime();
  }
}
