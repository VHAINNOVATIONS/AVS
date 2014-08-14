package gov.va.med.lom.avs.util;

import java.util.*;
import java.sql.*;

/*
 * Thread that wakes up periodically and reaps any disconnected brokers.
 */
class ConnectionReaper extends Thread {

  private DBConnectionPool pool;
  private long delay = 30000;  // msec
  private boolean stopped = false;

  ConnectionReaper(DBConnectionPool pool, long delay) {
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

class ConnectionWrapper {

  private Connection connection;
  private java.util.Date lastLeased;
  private boolean leased;
  
  public ConnectionWrapper(Connection connection) {
    this.connection = connection;
    lastLeased = new java.util.Date();
  }
  
  public Connection lease() {
    this.lastLeased = new java.util.Date();
    this.leased = true;
    return connection;
  }
  
  public void expireLease() {
    this.leased = false;
  }
  
  public long getLastLeased() {
    return lastLeased.getTime();
  }
  
  public void close() {
    try {
      this.connection.close();
    } catch(Exception e) {}
  }
  
  public boolean isAvailable() {
    return !this.isLeased() && 
           !this.isClosed() && 
           this.testConnection();
  }
  
  public boolean isClosed() {
    try {
      return this.connection.isClosed();
    } catch(Exception e) {
      return true;
    }
  }
  
  public boolean testConnection() {
    try {
      Statement stmt = this.connection.createStatement();
      stmt.executeQuery("SELECT 1");
    } catch(Exception e) {
      return false;
    }
    return true;
  }
  
  public boolean isLeased() {
    return leased;
  }
  
  public String toString() {
    return this.connection.toString();
  }
  
}

/*
 * Class that maintains a pool of RPC broker connections. 
 */
public class DBConnectionPool {

   private Hashtable<String, ConnectionWrapper> connections;
   private int minPoolSize = 10;
   private String dbServer = null;
   private int dbPort = 0;
   private String dbName = null;
   private String domain = null;
   private String username = null;
   private String password = null;
   private String driver = null;
   private String drivertype = null;
   private String database = null;
   private long inactiveTimeout = 0;
   private ConnectionReaper reaper = null;

   public DBConnectionPool(String drivertype, String database, String dbServer, int dbPort, 
       String dbName, String domain, String username, String password, String driver, int minPoolSize, 
       long reapDelay, long inactiveTimeout) throws Exception {
     
     this.drivertype = drivertype;
     this.database = database;
     this.dbServer = dbServer;
     this.dbPort = dbPort;
     this.dbName = dbName;
     this.domain = domain;
     this.username = username;
     this.password = password;
     this.driver = driver;
     
     if (minPoolSize > 0)
       this.minPoolSize = minPoolSize;
     this.inactiveTimeout = inactiveTimeout;
     connections = new Hashtable<String, ConnectionWrapper>();
     // Start the reaper thread
     reaper = new ConnectionReaper(this, reapDelay);
     reaper.start();
   }   

   /*
    * Public methods that maintain the broker connection pool.
    */
   public synchronized void reapConnections() throws Exception {
     List<ConnectionWrapper> list = this.getConnections();
     for (ConnectionWrapper connWrapper : list ) {
       // Reap the connection if the connection is closed or if the
       // connection has been inactive for more than the timeout period.
       if (connWrapper.isClosed() || ((inactiveTimeout > 0) && 
            ((new java.util.Date().getTime() - connWrapper.getLastLeased() >  inactiveTimeout)))) {
         removeConnection(connWrapper);
         // Maintain the minimum pool size
         if (getPoolSize() < minPoolSize)
           createConnection();
       }
     }
   }

   public synchronized void closeConnections()  {
     List<ConnectionWrapper> list = this.getConnections();
     for (ConnectionWrapper connWrapper : list ) {
       connWrapper.close();
       removeConnection(connWrapper);
     }
   }
   
   public synchronized Connection getConnection() throws Exception {
     // find a connection that is not being used
     List<ConnectionWrapper> list = this.getConnections();
     for (ConnectionWrapper connWrapper : list ) {
       if (connWrapper.isAvailable()) {
         return connWrapper.lease();
       } else
         removeConnection(connWrapper);
     }
     // couldn't find a connection so create a new one
     return createConnection().lease();
  } 

   public synchronized void returnConnection(Connection connection) {
     if (connection != null) {
       ConnectionWrapper connWrapper = connections.get(connection.toString());
       if (connWrapper != null) {
         connWrapper.expireLease();
       }
     }
   }
   
   public synchronized void destroyConnection(Connection connection) {
     if (connection != null) {
       connections.remove(connection.toString());
     }
   }   
   
   public int getPoolSize() {
     return connections.size();
   }
   
   public int getNumLeasedConnections() {
     Enumeration<String> keys = connections.keys();
     int n = 0; 
     while (keys.hasMoreElements()) {
       String key = keys.nextElement();
       ConnectionWrapper connWrapper = connections.get(key);
       if (connWrapper.isLeased())
         n++;
     }
     return n;
   }
   
   /*
    * Close all connections and stop the reaper thread.
    */
   public void terminate() {
     closeConnections();
     reaper.terminate();
   }

   /*
    * Private methods for maintaining the connection pool.
    */
   private List<ConnectionWrapper> getConnections() {
     List<ConnectionWrapper> list = new ArrayList<ConnectionWrapper>();
     Enumeration<String> keys = connections.keys();
     while (keys.hasMoreElements()) {
       String key = keys.nextElement();
       list.add(connections.get(key));
     }
     return list;
   }
   
   private synchronized void removeConnection(ConnectionWrapper connWrapper) {
     connWrapper.close();
     connections.remove(connWrapper);
   }

   private synchronized ConnectionWrapper createConnection() throws Exception {
     Class.forName(this.driver).newInstance();
     String url = "jdbc:" + this.drivertype + ":" + this.database + "://" + this.dbServer + ":" + this.dbPort + "/" +
       this.dbName + ";domain=" + this.domain; 
     Connection connection = DriverManager.getConnection(url, this.username, this.password);
     return addConnection(new ConnectionWrapper(connection));
   }

   private synchronized ConnectionWrapper addConnection(ConnectionWrapper connWrapper) {
     connections.put(connWrapper.toString(), connWrapper);
     return connWrapper;
   }
}
