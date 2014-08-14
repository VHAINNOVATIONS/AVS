/*
 * BrokerConnectionPool.java
 * Author: Rob Durkin (rob.durkin@med.va.gov)
 * Version 1.0.1 (08/30/2005)
 * 
 * Implements a pool of RPC broker connections.
 * 
 */
package gov.va.med.lom.javaBroker.rpc;

import java.util.*;

/*
 * Thread that wakes up periodically and reaps any disconnected brokers.
 */
class ConnectionReaper extends Thread {

  private BrokerConnectionPool pool;
  private long delay = 30000;  // msec
  private boolean stopped = false;

  ConnectionReaper(BrokerConnectionPool pool, long delay) {
    if (delay > 0)
      this.delay = delay;
    this.pool=pool;
  }

  public void run() {
    while(true) {
      try {
        if (!stopped) {
          sleep(delay);
          pool.reapConnections();
        } else
          break;
      } catch(Exception e) { }
    }
  }
  
  public void terminate() {
    stopped = true;
  }
}

/*
 * Class that maintains a pool of RPC broker connections. 
 */
public class BrokerConnectionPool {

   private Vector<BrokerConnection> connections;
   private int minPoolSize = 10;
   private String host = null;
   private int port = 0;
   private String accessCode = null;
   private String verifyCode = null;
   private long inactiveTimeout = 0; 
   private ConnectionReaper reaper = null;

   /*
    * Constructor: Create a pool of broker objects without automatically signing them on.
    *              Set inactiveTimeout to the length of time (in msec) for which an inactive connection
    *              should be reaped (or 0 to prevent reaper from removing inactive connections).
    */
   public BrokerConnectionPool(String host, int port, int minPoolSize, 
                               long reapDelay, long inactiveTimeout) throws BrokerException {
     this(host, port, null, null, minPoolSize, reapDelay, inactiveTimeout);
   }
   
   /*
    * Constructor: Create a pool of broker objects and automatically sign them on.
    */
   public BrokerConnectionPool(String host, int port, String accessCode, String verifyCode,
                               int minPoolSize, long reapDelay, long inactiveTimeout) throws BrokerException {
     this.host = host;
     this.port = port;
     this.accessCode = accessCode;
     this.verifyCode = verifyCode;     
     if (minPoolSize > 0)
       this.minPoolSize = minPoolSize;
    this.inactiveTimeout = inactiveTimeout;
     connections = new Vector<BrokerConnection>(minPoolSize);
     // Create the broker connection pool
     for (int i = 0; i < minPoolSize; i++) {
       createConnection();
     }
     // Start the reaper thread
     reaper = new ConnectionReaper(this, reapDelay);
     reaper.start();
   }   

   /*
    * Public methods that maintain the broker connection pool.
    */
   public synchronized void reapConnections() throws BrokerException {
     Enumeration<BrokerConnection> connlist = connections.elements();
     while((connlist != null) && (connlist.hasMoreElements())) {
       BrokerConnection conn = (BrokerConnection)connlist.nextElement();
       // Reap the connection if the broker is disconnected or if the
       // broker has been inactive for more than the timeout period.
       if (!conn.isConnected() || 
           ((inactiveTimeout > 0) && (conn.getInactiveTime() > inactiveTimeout))) {
         removeConnection(conn);
         // Maintain the minimum pool size
         if (getPoolSize() < minPoolSize)
           createConnection();
       }
     }
   }

   public synchronized void closeConnections() throws BrokerException {
     Enumeration<BrokerConnection> connlist = connections.elements();
     while((connlist != null) && (connlist.hasMoreElements())) {
       BrokerConnection conn = (BrokerConnection)connlist.nextElement();
       removeConnection(conn);
     }
   }
   
   public synchronized BrokerConnection getConnection() throws BrokerException {
     // find a connection that is not being used
     BrokerConnection conn;
     for(int i = 0; i < connections.size(); i++) {
       conn = (BrokerConnection)connections.elementAt(i);
       System.out.println("leased=" + conn.isLeased() + ", connected=" + conn.isConnected() + ", test=" + testConnection(conn));
       if (!conn.isLeased() && conn.isConnected() && testConnection(conn)) {
         conn.lease();
         return conn;
       } else
         removeConnection(conn);
     }
     // couldn't find a connection so create a new one
     conn = createConnection();
     conn.lease();
     return conn;
  } 

   public synchronized void returnConnection(BrokerConnection conn) {
     conn.expireLease();
   }
   
   public int getPoolSize() {
     return connections.size();
   }
   
   public int getNumLeasedConnections() {
     int n = 0; 
     BrokerConnection conn;
     for(int i = 0; i < connections.size(); i++) {
       conn = (BrokerConnection)connections.elementAt(i);
       if (conn.isLeased())
         n++;
     }
     return n;
   }
   
   /*
    * Close all broker connections and stop the reaper thread.
    */
   public void terminate() throws BrokerException {
     closeConnections();
     reaper.terminate();
   }

   /*
    * Private methods for maintaining the connection pool.
    */
   private synchronized void removeConnection(BrokerConnection conn) {
     try {
       conn.disconnect();
       connections.removeElement(conn);
     } catch(BrokerException be) {}
   }
   
   private synchronized boolean testConnection(BrokerConnection conn) {
     RpcBroker rpcBroker = conn.getRpcBroker();
     IRpcProtocol rpcProtocol = rpcBroker.getRpcProtocol();
     try {
       rpcProtocol.setRpcVersion("1.106");
       rpcProtocol.call(RpcBroker.XWB_IM_HERE, new Params());
     } catch(BrokerException be) {
       return false;
     }
     return true;
     
   }

   private synchronized BrokerConnection createConnection() throws BrokerException {
     BrokerConnection conn = null;
     if ((accessCode != null) && (verifyCode != null))
       conn = new BrokerConnection(host, port, accessCode, verifyCode);
     else
       conn = new BrokerConnection(host, port);
     addConnection(conn);
     return conn;
   }

   private synchronized void addConnection(BrokerConnection conn) {
     connections.add(conn);
   }
}
