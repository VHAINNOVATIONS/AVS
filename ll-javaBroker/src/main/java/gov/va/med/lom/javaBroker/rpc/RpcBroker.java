package gov.va.med.lom.javaBroker.rpc;

import java.util.List;
import java.util.ArrayList;
import java.util.Date;

import gov.va.med.lom.javaBroker.log.LogChannel;
import gov.va.med.lom.javaBroker.log.Logger;
import gov.va.med.lom.javaBroker.util.DateUtils;
import gov.va.med.lom.javaBroker.util.ExceptionUtils;
import gov.va.med.lom.javaBroker.util.Hash;
import gov.va.med.lom.javaBroker.util.ITimerHandler;
import gov.va.med.lom.javaBroker.util.Semaphore;
import gov.va.med.lom.javaBroker.util.StringUtils;
import gov.va.med.lom.javaBroker.util.Timer;

/**
 * <p>The <code>RpcBroker</code> class provides VistA application developers with client/server-related
 * functionality for software written in the Java programming language.</p>
 *
 * <p>Using an instance of <code>RpcBroker</code>, an application can connect to the server by simply
 * calling the <code>connect</code> method passing the server host and port. Remote procedures on the
 * server can be executed by invoking one of the overloaded <code>lCall</code>, <code>tCall</code>, or
 * <code>sCall</code> methods.</p>
 *
 * The following code performs a signon setup, signs the user on to the VistA broker server, and
 * then gets information on the user:
 * <p><hr><blockquote><pre>
 *   RpcBroker rpcBroker = new RpcBroker();
 *   rpcBroker.connect("test.lom.med.va.gov", 9100);
 *   long duz = rpcBroker.doSignon("access", "verify");
 *   if (duz > 0) {
 *     String s = rpcBroker.sCall("OR CPRS GUI CHART, "ORWU USERINFO");
 *   }
 * </pre></blockquote><hr>
 *
 * @author        Robert M. Durkin (rob.durkin@med.va.gov)<br>
 *                Version 2 protocol implemented by Andy McCarty (william.mccarty@med.va.gov)
 * @version       2.0
 * Date Created:  Sep 3, 2004
 * Date Modified: Dec 8, 2005
 * Site Name:     VA Loma Linda Healthcare System
 */

public class RpcBroker implements IPulseHandler, ITimerHandler {
    
    // NAMED CONSTANTS
    public static final String RPC_BROKER       = "RPCBROKER";
    static final String XWB_IM_HERE             = "XWB IM HERE";               // Pulse RPC
    static final double PULSE_PERCENTAGE        = 0.45;                        // % of timeout for pulse frequency.
    static final int DEFAULT_PULSE              = (int)(PULSE_PERCENTAGE *
                                                       (3 * 60 * 1000));       // default = 45% of 3 minutes (in msec).
    static final int TIMEOUT_TIMER              = (int)(1 * 60 * 1000);        // default = 1 minute
    static final int DEFAULT_INACTIVE_TIMEOUT   = (int)(20 * 60 * 1000);       // default = 20 minutes
    
    // FIELDS
    private boolean clearParams;
    private boolean clearResults;
    private boolean connecting;
    private String currentContext;
    private int listenerPort;
    private String server;
    private String remoteProcedure;
    private Params params;
    private String loginHandle;
    private String avCodes;
    private boolean kernelLogin;
    private StringBuffer results;
    private PulseTimer pulseTimer;
    private Timer timeoutTimer;
    private Date lastMessageTime;
    private int inactiveTimeout;
    private int rpcProtocolVersion;
    private IRpcProtocol rpcProtocol;
    private Semaphore callMonitor;
    private static LogChannel logChannel;
    // User data
    private String duz;
    private String userName;
    private String station;
    private String stationIen;
    private String stationNo;
    private String domain;
    private String userClass;
    private String title;
    private String service;
    private String serviceSection;
    
    
    // CONSTRUCTORS
    /**
     * Constructs an instance of RpcBroker using the default version of the
     * RPC broker protocol.
     *
     * @param server  the host/ip of the server
     * @param port  the listening port on the server
     * @throws BrokerException
     */    
    public RpcBroker() throws BrokerException {
        try {
            this.rpcProtocol = RpcProtocolFactory.getDefaultProtocol();
            this.rpcProtocolVersion = rpcProtocol.getProtocolVersion();
            this.inactiveTimeout = DEFAULT_INACTIVE_TIMEOUT;
            init();
        } catch(Exception e) {
            throw new BrokerException(e, "create", 0, "Could not create RPC broker protocol.", 
                                      e.getMessage(), null , null, null); 
        }
    }
    
    /**
     * Constructs an instance of RpcBroker using the version of the protocol specified.
     *
     * @param rpcProtocolVersion  the version of the rpc broker protocol to use
     */    
    public RpcBroker(int rpcProtocolVersion) throws BrokerException {  
        try {
            this.rpcProtocol = RpcProtocolFactory.getProtocol(rpcProtocolVersion);
            this.rpcProtocolVersion = rpcProtocol.getProtocolVersion();
            this.inactiveTimeout = DEFAULT_INACTIVE_TIMEOUT;
            init();
        } catch(Exception e) {
            throw new BrokerException(e, "create", 0, "Could not create RPC broker protocol.", 
                                      e.getMessage(), null , null, null); 
        }
    }
    
    /**
     * Constructs an instance of RpcBroker and connects to the server
     * at the specified server host/ip and port using the Version 2 
     * broker protocol.
     *
     * @param server  the host/ip of the server
     * @param port  the listening port on the server
     * @throws BrokerException
     */
    public RpcBroker(String server, int port) throws BrokerException {
        this();
        setServer(server);
        setListenerPort(port);
        setConnected(true);
    }
    
    /**
     * Constructs an instance of RpcBroker and connects to the server
     * at the specified server host/ip and port using the version of the 
     * broker protocol that is specified .
     *
     * @param server  the host/ip of the server
     * @param port  the listening port on the server
     * @throws BrokerException
     */
    public RpcBroker(int rpcProtocolVersion, String server, int port) throws BrokerException {
        this(rpcProtocolVersion);
        setServer(server);
        setListenerPort(port);
        setConnected(true);
    }    
    
    // Initialization
    private void init() {
      // create objects
      params = new Params();
      // set defaults
      results = new StringBuffer();
      setRpcVersion(IRpcProtocol.RPC_VERSION);
      setClearParameters(true);  // clears parameters list between calls
      setClearResults(true);     // clears results list between calls
      // Create semaphore for synch'ing access to calls
      callMonitor = new Semaphore(1);
    }
    
    
    // PROPERTY ACCESSORS
    
    /**
     * Gets the event handler object that implements IRpcBrokerEventHandler interface.
     */
    public IRpcBrokerEventHandler getEventHandler() {
        return rpcProtocol.getEventHandler();
    }
    
    /**
     * Gets the event handler object.
     * @param eventHandler object that implements IRpcBrokerEventHandler interface.
     */
    public void setEventHandler(IRpcBrokerEventHandler eventHandler) {
        rpcProtocol.setEventHandler(eventHandler);
    }
    
    /**
     * Returns the RPC version
     *
     * @return  the RPC version
     */
    public String getRpcVersion() {
        return rpcProtocol.getRpcVersion();
    }
    
    /**
     * <p>The RPC version property is used to pass the version of the RPC. This can be useful for backward compatibility.</p>
     * <p>As you introduce new functionality into an existing RPC, your RPC can branch into different parts of the code
     * based on the value of the RPC version property. The broker sets the <i>XWBAPVER</i> variable on the server to the
     * contents of the RPC version property. This property cannot be empty and defaults to "0" (zero).</p>
     *
     * @param rpcVersion  the version of the RPC to set.
     */
    public void setRpcVersion(String rpcVersion) {
        rpcProtocol.setRpcVersion(rpcVersion);
    }
    
    /**
     * Tests whether or not to clear the parameter list between calls.
     *
     * @return  <code>true</code> if the list of parameters should be cleared between calls, <code>false</code> otherwise.
     */
    public boolean getClearParams() {
        return clearParams;
    }
    
    /**
     * <p>The clear parameters design-time property gives the developer the option to clear the <code>params</code> property following
     * every invocation of the <i>call</i> methods. Setting clear parameters to <code>true</code> clears the <code>params</code> property.</p>
     * <p>Simple assignment of <code>true</code> to this property clears the <code>Pparam</code> property after every invocation of the
     * <i>call</i> methods. Thus, the parameters need only be prepared for the next call without being concerned about
     * what was remaining from the previous call. By setting clear parameters to <code>false</code>, the programmer can
     * make multiple calls with the same <code>params</code> property. It is also appropriate to set this property to false when a
     * majority of the parameters in the <code>params</code> property should remain the same between calls.
     * However, minor changes to the parameters can still be made.
     *
     * If <code>value</code> is <code>true</code>, clears the parameter list and clears parameters between all calls.
     * Otherwise, if <code>value</code> is <code>false</code>, the parameters will not be cleared between calls.
     * @param value
     */
    public void setClearParameters(boolean value) {
        if (value && (params != null))
            params.clear();
        this.clearParams = value;
    }
    
    /**
     * Clears the <code>params</code> property.
     * @see #setClearParameters(boolean value)
     */
    public void doClearParams() {
        setClearParameters(true);
    }
    
    /**
     * Tests whether the results are to be cleared between calls.
     *
     * @return  <code>true</code> if the results have been cleared, <code>false</code> otherwise.
     * @see #setClearResults(boolean)
     */
    public boolean getClearResults() {
        return clearResults;
    }
    
    /**
     * <p>Setting clear results to <code>true</code> clears the <code>results</code> property.</p>
     * <p>If this property is <code>true</code>, then the <code>results<code> property is cleared before every
     * invocation of the <i>call</i> methods, thus, assuring that only the results of the last call are returned.
     * Conversely, a setting of <code>false</code accumulates the results of multiple calls in the <code>results</code> property.
     *
     * @param value  <code>true</code> if results are to be cleared between calls, <code>false</code> otherwise
     */
    public void setClearResults(boolean value) {
        if (value)
            results.delete(0, results.length());
        this.clearResults = value;
    }
    
    /**
     * Clears the results.
     *
     * @see #setClearResults(boolean)
     */
    public void doClearResults() {
        setClearResults(true);
    }
    
    /**
     * Tests whether the broker client is currently in the process of connecting to the server.
     *
     * @return  <code>true</code> if the broker client is connecting, <code>false</code> otherwise.
     */
    public boolean isConnecting() {
        return connecting;
    }
    
    /**
     * Creates a broker exception.
     *
     * @param e   the exception that this broker exception encapsulates
     * @param action   the action for which this exception occurred
     * @param code   the integer code representing this exception
     *
     * @return  the broker exception.
     */    
    public BrokerException createBrokerException(Exception e, String action, int code) {
      return rpcProtocol.createBrokerException(e, action, code);
    }
    
    /**
     * Gets the last broker exception.
     *
     * @return  the last broker exception.
     */
    public BrokerException getBrokerException() {
        return rpcProtocol.getBrokerException();
    }
    
    /**
     * Gets the current broker context.
     *
     * @return  the current broker context.
     */
    public String getCurrentContext() {
        return currentContext;
    }
    
    /**
     * Sets the current broker context and creates the context on the server.
     *
     * @param currentContext   the current context to set
     * @throws BrokerException
     */
    public boolean setCurrentContext(String currentContext) throws BrokerException {
        return createContext(currentContext);
    }
    
    /**
     * Sets the current context without creating the context on the server.
     *
     * @param context  the context to set
     */
    public void setContext(String context) {
        this.currentContext = context;
    }
    
    /**
     * Tests whether the login type is kernel login.
     *
     * @return <code>true</code> if login type is kernel login, <code>false</code> otherwise.
     */
    public boolean isKernelLogin() {
        return kernelLogin;
    }
    
    /**
     * If <code>kernelLogin</code> is <code>true</code>, sets the login type to kernel login.
     *
     * @param kernelLogin  <code>true</code> if login type is kernel login, <code>false</code> otherwise.
     */
    public void setKernelLogin(boolean kernelLogin) {
        this.kernelLogin = kernelLogin;
    }
    
    /**
     * Gets the listener port on the broker server.
     *
     * @return  the listener port on the broker server
     */
    public int getListenerPort() {
        return listenerPort;
    }
    
    /**
     * Sets the port on which the broker server is listening.
     * @param listenerPort  the port on which the broker server is listening
     */
    public void setListenerPort(int listenerPort) {
        this.listenerPort = listenerPort;
    }
    
    /**
     * <code>Params</code> is an object that holds all of the parameters that the
     * application needs to pass to the remote procedure.
     *
     * @return  the params that the application needs to pass to the remote procedure
     */
    public Params getParams() {
        return params;
    }
    
    /**
     * Sets the <code>Params</code> object, which holds all of the parameters that the
     * application needs to pass to the remote procedure.
     *
     * @param params  the params that the application needs to pass to the remote procedure
     */
    public void setParams(Params params) {
        this.params = params;
    }
    
    /**
     * Gets the remote procedure property to the name of the remote procedure call entry in the REMOTE PROCEDURE file (#8994).
     *
     * @return  the current remote procedure API
     */
    public String getRemoteProcedure() {
        return remoteProcedure;
    }
    
    /**
     * Sets the remote procedure property to the name of the remote procedure call entry in the REMOTE PROCEDURE file (#8994).
     *
     * @param remoteProcedure  the current remote procedure API
     */
    public void setRemoteProcedure(String remoteProcedure) {
        this.remoteProcedure = remoteProcedure;
    }
    
    /**
     * Gets the results as a StringBuffer.
     *
     * @return  the contents of the data returned from the call
     */
    public StringBuffer getResults() {
        return results;
    }
    
    /**
     * Returns the hashed access;verify code pair.
     *
     * @return  avCode  the user's hashed access;verify code pair
     */
    public String getHashedAVCodes() {
        return avCodes;
    }
    
    /**
     * Sets the hashed access;verify code pair for the user.
     *
     * @param avCode  the hashed access;verify code pair
     */
    public void setHashedAVCodes(String avCode) {
        this.avCodes = avCode;
    }
    
    /**
     * Gets the message property associated with the exception or error that was encountered.
     *
     * @return  the text of the last RPC broker error
     */
    public String getErrorMessage() {
        return rpcProtocol.getErrorMessage();
    }
    
    /**
     * Gets the security segment of the last inbound message.
     *
     * @return  the security segment of the message received from VistA
     */      
    public String getSecuritySegment() {
      return rpcProtocol.getSecuritySegment();
    }    
    
    /**
     * Gets the application segment of the last inbound message.
     *
     * @return  the application segment of the message received from VistA
     */    
    public String getApplicationSegment() {
      return rpcProtocol.getApplicationSegment();
    }

    /**
     * Gets the data segment of the last inbound message.
     *
     * @return  the data segment of the message received from VistA
     */      
    public String getDataSegment() {
      return rpcProtocol.getDataSegment();
    }
    
    /**
     * Gets the duz of the signed-on user.
     *
     * @return the signed-on user's duz
     */
    public String getUserDuz() {
        return duz;
    }
    
    /**
     * Sets the duz of the signed-on user.
     *
     * @param duz  signed-on user's duz
     */
    public void setUserDuz(String duz) {
        this.duz = duz;
    }
    
    /**
     * Gets the domain of the signed-on user.
     *
     * @return the signed-on user's domain
     */    
    public String getDomain() {
      return domain;
    }

    /**
     * Sets the domain of the signed-on user.
     *
     * @param domain  signed-on user's domain
     */
    public void setDomain(String domain) {
      this.domain = domain;
    }

    /**
     * Gets the service ID of the signed-on user.
     *
     * @return the signed-on user's service ID
     */
    public String getService() {
      return service;
    }

    /**
     * Sets the service ID of the signed-on user.
     *
     * @param service  signed-on user's service ID
     */
    public void setService(String service) {
      this.service = service;
    }

    /**
     * Gets the service/section of the signed-on user.
     *
     * @return the signed-on user's service/section
     */
    public String getServiceSection() {
      return serviceSection;
    }

    /**
     * Sets the service/section of the signed-on user.
     *
     * @param serviceSection  signed-on user's service/section
     */
    public void setServiceSection(String serviceSection) {
      this.serviceSection = serviceSection;
    }

    /**
     * Gets the station of the signed-on user.
     *
     * @return the signed-on user's station
     */
    public String getStation() {
      return station;
    }

    /**
     * Sets the station of the signed-on user.
     *
     * @param station  signed-on user's station
     */
    public void setStation(String station) {
      this.station = station;
    }
    
    /**
     * Gets the station IEN of the signed-on user.
     *
     * @return the signed-on user's station IEN
     */
    public String getStationIen() {
      return stationIen;
    }

    /**
     * Sets the station IEN of the signed-on user.
     *
     * @param stationIen  signed-on user's station IEN
     */
    public void setStationIen(String stationIen) {
      this.stationIen = stationIen;
    }

    /**
     * Gets the station number of the signed-on user.
     *
     * @return the signed-on user's station number
     */
    public String getStationNo() {
      return stationNo;
    }

    /**
     * Sets the station number of the signed-on user.
     *
     * @param stationNo  signed-on user's station number
     */
    public void setStationNo(String stationNo) {
      this.stationNo = stationNo;
    }

    /**
     * Gets the title of the signed-on user.
     *
     * @return the signed-on user's title
     */
    public String getTitle() {
      return title;
    }

    /**
     * Sets the title of the signed-on user.
     *
     * @param title  signed-on user's title
     */
    public void setTitle(String title) {
      this.title = title;
    }

    /**
     * Gets the class of the signed-on user.
     *
     * @return the signed-on user's class
     */
    public String getUserClass() {
      return userClass;
    }

    /**
     * Sets the class of the signed-on user.
     *
     * @param userClass  signed-on user's class
     */
    public void setUserClass(String userClass) {
      this.userClass = userClass;
    }

    /**
     * Gets the name of the signed-on user.
     *
     * @return the signed-on user's name
     */
    public String getUserName() {
      return userName;
    }

    /**
     * Sets the name of the signed-on user.
     *
     * @param userName  signed-on user's name
     */
    public void setUserName(String userName) {
      this.userName = userName;
    }

    /**
     * The Rpc Protocol is the implementation of the RPC Broker protocol used.
     *
     * @return  the RPC broker protocol used by this RpcBroker
     */    
    public IRpcProtocol getRpcProtocol() {
      return rpcProtocol;
    }

    /**
     * Sets the RPC version number
     *
     * @param rpcProtocol  the Rpc protocol implementation to use
     * @see #getRpcProtocol()
     */    
    public void setRpcProtocol(IRpcProtocol rpcProtocol) {
      this.rpcProtocol = rpcProtocol;
    }

    /**
     * The Rpc protocol version is the version of the RPC protocol used.
     *
     * @return  the RPC broker protocol version used by this RpcBroker
     */       
    public int getRpcProtocolVersion() {
      return rpcProtocolVersion;
    }

    /**
     * Sets the RPC broker protocol version number
     *
     * @param rpcProtocolVersion  the Rpc protocol version being used
     * @see #getRpcProtocolVersion()
     */    
    public void setRpcProtocolVersion(int rpcProtocolVersion) {
      this.rpcProtocolVersion = rpcProtocolVersion;
    }
    
    /**
     * The server property contains the name or Internet Protocol (IP) address of the server computer.
     *
     * @return  the current server host/ip address
     */
    public String getServer() {
        return server;
    }
    
    /**
     * <p>Changing the name of the server while the RpcBroker is connected disconnects the RpcBroker from the previous server.</p>
     * <p>If the name is used instead of the IP address, it should be resolved by the socket through
     * the Domain Name Service (DNS).</p>
     *
     * @param server  the server host/IP address to set
     * @throws BrokerException
     * @see #getServer()
     */
    public void setServer(String server) throws BrokerException {
        // if changing the name of the server, must disconnect first
        if ((this.server != null) && !this.server.equals(server) && isConnected())
            setConnected(false);
        this.server = server;
    }
    
    /**
     * The login handle property holds the application handle for doing a silent login.
     * The application handle is obtained from the server by a currently running application
     * using the <code>getAppHandle</code> method.</p>
     *
     * <p>The login handle may be passed as a command line argument to an application that is being started.
     * The new application must know to look for the handle, and if present, set up the silent login.</p>
     *
     * <p>Note: The app handle that is obtained via the <code>getAppHandle</code> function is only valid
     * for approximately 20 seconds, after which the silent login would fail.</p>
     *
     * @return  the login handle that was obtained by a previous call to <code>getAppHandle</code>
     * @see #getAppHandle()
     */
    public String getLoginHandle() {
        return loginHandle;
    }
    
    /**
     * Returns the timeout duration (in msec) in which RpcBroker will log off
     * of VistA if a connection has been idle for a period of time.
     * @return inactiveTimeout duration of inactivity in msec.
     */
    public int getInactiveTimeout() {
        return inactiveTimeout;
    }

    /**
     * Sets the timeout duration (in msec) for inactivity, after which RpcBroker
     * will log off of VistA.
     * @param inactiveTimeout the duration of inactivity in msec.
     */
    public void setInactiveTimeout(int inactiveTimeout) {
        this.inactiveTimeout = inactiveTimeout;
    }
    
   /**
    * Returns the timestamp of the last (non-pulse) message sent to VistA.
    * @return timestamp of last RPC invocation.
    */
    public Date getLastMessageTime() {
        return lastMessageTime;
    }

    /**
     * The log channel is used to write messages to a logging facility.
     *
     * @return  the static log channel used by this RpcBroker
     */    
    public static LogChannel getLogChannel() {
      return logChannel;
    }
    
    /**
     * Sets the static log channel used for writing messages to the logging facility.
     *
     * @param logChannel  the log channel to use for logging
     * @see #getLogChannel()
     */      
    public static void setLogChannel(LogChannel logChannel) {
      RpcBroker.logChannel = logChannel;
    }
    
    /**
     * Writes the message to the logging facility. 
     *
     * @param level  the logging level (e.g. Logger.ERROR)
     * @param msg  the messge to log
     */       
    public void log(int level, String msg) {
      if (logChannel != null)
        logChannel.write(level, msg);
      else {
        StringBuffer output = new StringBuffer();
        output.append(DateUtils.getCurrentDateTime());
        output.append(": ");
        output.append(this.getClass().getName());
        output.append(", ");
        output.append(Logger.getLevelName(level));
        output.append(": ");
        output.append(msg);
        if ((level != Logger.EMERGENCY) && 
            (level != Logger.ALERT) && 
            (level != Logger.CRITICAL) &&
            (level != Logger.ERROR))
          System.out.println(output.toString());
        else
          System.err.println(output.toString());
      }
    }    
    
    /**
     * Writes the exception message to the logging facility. 
     *
     * @param level  the logging level (e.g. Logger.ERROR)
     * @param msg  the exception to log
     */      
    public void log(int level, Exception e) {
      String errMsg = ExceptionUtils.describeException(e);
      log(level, errMsg);
    }
    
    /**
     * Tests if a connection is established, and allows you to branch accordingly depending
     * on whether a connection is established or not.
     *
     * @return  <code>true</code> if connected to the broker server, <code>false</code> otherwise
     */
    public boolean isConnected() {
        return rpcProtocol.isConnected();
    }
    
    /**
     * If <code>value</code> is <code>true</code>, connects to the broker server using the
     * previously set server host/ip and port.  Otherwise, if <code>value</code> is <code>false</code>
     * disconnects from the broker server.
     *
     * @return  <code>true</code> if connected to the broker server, <code>false</code> otherwise
     * @throws BrokerException
     */
    public synchronized boolean setConnected(boolean value) throws BrokerException {
        if (value && isConnected())
            return true;
        if (clearResults)
            doClearResults();
        if (value) {
            if (!isConnecting()) {
                connecting = true;
                boolean connected = rpcProtocol.connect(server, listenerPort); 
                connecting = false;
                if (connected) {
                    setContext(null);
                    createContext("");
                    // Create and start timer for 'IM HERE' pulse
                    pulseTimer = new PulseTimer(this, DEFAULT_PULSE);    
                    pulseTimer.start();
                    // Create and start timer for connection inactivity timeout
                    timeoutTimer = new Timer(this, TIMEOUT_TIMER);
                    timeoutTimer.start();
                } else {
                    pulseTimer.shutdown();
                    timeoutTimer.shutdown();
                }
                return connected;
            } else 
                return false;
        } else {
            // Stop the pulse and timeout timers.
            // This action will cause them to exit their run
            // loops so that they can be GC'd.
            pulseTimer.shutdown();
            timeoutTimer.shutdown();
            return rpcProtocol.disconnect();
        }
    }
    
    /**
     * Connect to the broker server using the previously set server host/ip and port.
     *
     * @return  <code>true</code> if connected to the broker server, <code>false</code> otherwise
     * @throws BrokerException
     */
    public boolean connect() throws BrokerException {
        if (!isConnected())
            return setConnected(true);
        else
            return true;
    }
    
    /**
     * Connect to the broker server.
     *
     * @param server  the host name or IP address of the broker server
     * @param port  the listener port on the broker server
     * @return  <code>true</code> if connected to the broker server, <code>false</code> otherwise
     * @throws BrokerException
     */
    public boolean connect(String server, int port) throws BrokerException {
        setServer(server);
        setListenerPort(port);
        return connect();
    }
    
    /**
     * Disconnects from the broker server.
     *
     * @return <code>true</code> if successfully disconnected, <code>false</code> otherwise
     * @throws BrokerException
     */
    public boolean disconnect() throws BrokerException {
        return setConnected(false);
    }
    
    /**
     * <p>Creates and sets the context on the broker server.</p>
     * <p>You can use the <code>createContext</code> method to create a context for your application.
     * To create context, pass an option name as a parameter.  If the function returns <code>true</code>,
     * a context was created, and your application can use all RPCs entered in the option's RPC multiple.
     * If the RpcBroker is not connected at the time context is created, a connection will be established.
     * If for some reason a context could not be created, the <code>createContext</code> method will return False.</p>
     * <p>Since context is nothing more than a client/server 'B'-type option in the OPTION file (#19), standard MenuMan
     * security is applied in establishing a context. Therefore, a context option can be granted to user(s) exactly
     * the same way as regular options are done using MenuMan. Before any RPC can run, it must have a context
     * established for it to on the server. Thus, all RPCs must be registered to one or more 'B'-type option(s).
     * This plays a major role in Broker security.</p>
     * A context cannot be established for the following reasons:
     * <ul>
     * <li>The user has no access to that option.</li>
     * <li>The option is temporarily out of order.</li>
     * </ul>
     * An application can switch from one context to another as often as it needs to. Each time a context is created the previous context is overwritten.
     *
     * @param context  the context to create
     * @return  <code>true</code> if the context was created, <code>false</code> otherwise
     * @throws BrokerException
     */
    public synchronized boolean createContext(String aContext) throws BrokerException {
      return doCreateContext(aContext);
    }
    
    /**
     * Performs a signon setup and signon using the supplied
     * semicolon delimited access and verify codes.  Note that
     * this method returns 0 if the signon was not successful
     * for any reason, including invalid access/verify codes,
     * expired verify code, or inability to connect to the broker
     * server or create the required context.
     *
     * @param avCode  semicolon-delimited access and verify codes
     * @return  the user's duz if signon was successful, 0 otherwise
     * @throws BrokerException
     */
    public String doSignon(String avCode) throws BrokerException {
        this.avCodes = Hash.encrypt(avCode);
        return signon(avCodes);
    }
    
    /**
     * Performs a signon setup and signon using the supplied
     * access and verify codes.
     *
     * @param accessCode  the user's access code
     * @param verifyCode  the user's verify code
     * @return  the user's duz if signon was successful, 0 otherwise
     * @throws BrokerException
     * @see #doSignon(String)
     */
    public String doSignon(String accessCode, String verifyCode) throws BrokerException {
        return doSignon(accessCode + ";" + verifyCode);
    }
    
    /**
     * Performs a signon setup and signon using the cached access and verify codes.
     *
     * @return  the user's duz if signon was successful, 0 otherwise
     * @throws BrokerException
     * @see #doSignon(String)
     */
    public String doSignon() throws BrokerException {
        if (avCodes != null)
          return signon(avCodes);
        else
          throw createBrokerException(null, "signon", 0);
    }
    
    // Do the actual sign-on with hashed av codes.
    private synchronized String signon(String hashedAVCodes) throws BrokerException {
        // connect to VistA
        if (connect()) {
            // do signon setup
            if (createContext("XUS SIGNON")) {
                String result = call("XUS SIGNON SETUP");
                ArrayList list = StringUtils.getArrayList(result);
                // if setup was successful then do signon
                if (list.size() >= 6) {
                    String[] params = {hashedAVCodes};
                    result = call("XUS AV CODE", params);
                    list = StringUtils.getArrayList(result);
                    if (list.size() >= 6) {
                        if (((String)list.get(0)).equals("0")) {
                            this.duz = null;
                        } else {
                            this.duz = (String)list.get(0);
                            result = call("XUS GET USER INFO");
                            list = StringUtils.getArrayList(result);
                            if (list.size() >= 8) {
                              setUserName((String)list.get(1));
                              String division = (String)list.get(3);
                              setStationIen(StringUtils.piece(division, 1));
                              setStation(StringUtils.piece(division, 2));
                              setStationNo(StringUtils.piece(division, 3));
                              setTitle((String)list.get(4));
                              setServiceSection((String)list.get(5));
                            }
                            if (createContext("OR CPRS GUI CHART")) {
                              result = call("ORWU USERINFO");
                              setUserClass(StringUtils.piece(result, 3));
                              setDomain(StringUtils.piece(result, 13));
                              setService(StringUtils.piece(result, 14));
                            }
                            if (getEventHandler() != null)
                              getEventHandler().onRpcBrokerSignon(this.duz);
                        }
                    }
                }
            } else
                return null; // couldn't create the signon context
        }
        return this.duz;
    }
    
    /**
     * Gets info from the server regarding setup and parameters of the broker.
     *
     * @return  timeout period for handler READs or 0 if the operation was not successful
     * @throws BrokerException
     */
    public synchronized int getBrokerInfo() throws BrokerException {
        if (createContext("XUS SIGNON")) {
            String s = call("XWB GET BROKER INFO");
            if (s.length() > 0)
                return Integer.valueOf(s).intValue();
        }
        return 0;
    }
    
    /**
     * Gets a token that can be used to sign-on a new process.
     * @return  a token that can be used to sign-on a new process or a
     * blank string ("") if the operation was not successful
     * @throws BrokerException
     */
    public synchronized String getAppHandle() throws BrokerException {
        if (createContext("XUS SIGNON")) {
            loginHandle = call("XUS GET TOKEN");
            return loginHandle;
        } else
            return "";
    }
    
  /*
   * Called from the timer event of the <code>PulseTimer</code>.
   * Sends a 'heartbeat' pulse to the broker server.
   * (non-Javadoc)
   * @see gov.va.med.lom.javaBroker.rpc.IPulseHandler#doPulse()
   */
    public synchronized void doPulse() {
        lockCallMonitor();
        // Save current settings so that environment
        // is the same after the procedure as before.
        String savedRpcVersion = rpcProtocol.getRpcVersion();
        try {
            rpcProtocol.setRpcVersion("1.106");
            rpcProtocol.call(XWB_IM_HERE, new Params());
        } catch(BrokerException be) {
            log(Logger.ERROR, "VistA Broker error when attempting 'XWB IM HERE' pulse.  Disconnecting now!");
            //log(Logger.ERROR, be.getMessage());
            try { 
              disconnect();
            } catch(BrokerException be2) {}
        } finally {
            // Restore pre-existing properties
            rpcProtocol.setRpcVersion(savedRpcVersion);
            unlockCallMonitor();
        }
        
    }
    
    /*
     * Called from the timer vent of the <code>Timer</code>.
     * Checks whether the broker connection has been inactive for longer
     * than the max inactivity duration, and if so then disconnects. 
     *  (non-Javadoc)
     * @see gov.va.med.lom.javaBroker.util.ITimerHandler#onTimerEvent()
     */
    public synchronized void onTimerEvent() {
      long now = new Date().getTime();
      if ((now - lastMessageTime.getTime()) > inactiveTimeout) {
        try {
          disconnect();
        } catch(BrokerException be) {}
      }
    }
    
   // The following overloaded methods make a call to the broker server.
    
    /**
     * Makes the call on the broker server using the current context and current RPC API.
     * @return the result as an array of strings
     * @throws BrokerException
     */
    public synchronized String call() throws BrokerException {
        lockCallMonitor();   
        if (clearResults)
          doClearResults();
        try {
            // Get timestamp for this call if not pulse
            if (!remoteProcedure.equals(XWB_IM_HERE))
              lastMessageTime = new Date();
            String result = rpcProtocol.call(remoteProcedure, params);
            results.append(result);
        } catch(BrokerException be) {
            // Check if broker server disconnected with #BYE#
            if ((be.getCode() == IRpcProtocol.XWB_DISCONNECTED_WITH_BYE) || 
                (be.getCode() == IRpcProtocol.XWB_NO_CONNECTION_TO_SERVER)) {
              if (be.getCode() == IRpcProtocol.XWB_DISCONNECTED_WITH_BYE)
                log(Logger.WARNING, "VistA Broker disconnected with unexpected #BYE#.");
              else if (be.getCode() == IRpcProtocol.XWB_NO_CONNECTION_TO_SERVER) 
                log(Logger.WARNING, "Client disconnected from VistA Broker.");
              Exception brEx = null;
              try {
                results.append(doReconnect(true, this.currentContext));
                log(Logger.WARNING, "Reconnected successfully.");
              } catch(Exception e) {
                  log(Logger.ERROR, e);
                  brEx = e;
              }
              if (brEx != null) {
                  BrokerException brokerException = createBrokerException(brEx, "send", 0);
                  if (rpcProtocol.getEventHandler() != null)
                      rpcProtocol.getEventHandler().onRpcBrokerError(brokerException);
                  throw brokerException;
              }
            } else
                throw be;
        } finally {
            unlockCallMonitor();
        }
        return results.toString();
    }
    
    /**
     * Makes the call on the broker server.
     *
     * @param api  the RPC API to call
     * @param paramArray  the array of parameters to pass to the call
     * @return the result as an array of strings
     * @throws BrokerException
     */
    public synchronized String call(String api, Object[] paramArray) throws BrokerException {
        if (api != null)
            setRemoteProcedure(api);
        if (clearParams) {
            doClearParams();
            if ((paramArray != null) && (paramArray.length > 0)) {
                for (int i=0; i < paramArray.length; i++) {
                    if (paramArray[i].getClass() == Mult.class) {
                        params.addMult((Mult)paramArray[i]);
                    } else if (paramArray[i] instanceof List) {
                        params.addList((List)paramArray[i]);
                    } else {
                        params.addLiteral((String)paramArray[i]);
                    }
                }
                
            }
        }
        return call();
    }
    
    /**
     * Makes the call on the broker server using the current context.
     *
     * @param api  the RPC API to call
     * @param ien  the internal entry number of the record to retrieve
     * @return  the RPC result 
     * @throws BrokerException
     */
    public String call(String api, String ien) throws BrokerException {
        String[] params = {ien};
        return call(api, params);
    }
    
    /**
     * Makes the call on the broker server using the current context.
     *
     * @param api  the RPC API to call
     * @param params  the Params object containing the list of params
     * @return  the RPC resulty
     * @throws BrokerException
     */
    public String call(String api, Params params) throws BrokerException {
      setParams(params);
      setRemoteProcedure(api);
      return call();
    }
    
    /**
     * Makes the call on the broker server using the current context.
     *
     * @param api  the RPC API to call
     * @return  the result as an array of strings
     * @throws BrokerException
     */
    public String call(String api) throws BrokerException {
      doClearParams();
      setRemoteProcedure(api);
      return call();
    }
    
    /**
     * Calls the onRpcBrokerSignon event handler.
     *
     */    
    public void callOnRpcBrokerSignon() {
        if (rpcProtocol.getEventHandler() != null)
            rpcProtocol.getEventHandler().onRpcBrokerSignon(this.duz);
  }
    
    /*
     * PRIVATE METHODS
     */
    protected synchronized void lockCallMonitor() {
        try {
            // Obtain a lock on the call semaphore
            callMonitor.acquire();
        } catch(InterruptedException ie) {}   
    }
  
    protected synchronized void unlockCallMonitor() {
        // Release the lock on the call semaphore
        callMonitor.release(); 
    } 
    
    // Performs the actual work of creating the context.
    private synchronized boolean doCreateContext(String aContext) throws BrokerException {
        boolean retry = false;
        boolean created = true;
        // Get the current context
        String theContext = getCurrentContext();
        // Create the context only if it's different from the current context
        if ((theContext == null) || (!theContext.equals(aContext))) {
            lockCallMonitor();
            // save the current RPC and params
            Params contextParams = new Params();
            String s = Hash.encrypt(aContext);
            contextParams.addParameter(s, IParamType.LITERAL);
            String x = null;
            try {
                x = rpcProtocol.call("XWB CREATE CONTEXT", contextParams);
            } catch(Exception ex) {
                log(Logger.ERROR, ex);
                if (ex instanceof BrokerException) {
                    // Set current context to null since context creation failed
                    setContext(null);
                    // Check if broker server disconnected with #BYE#
                    if (((BrokerException)ex).getCode() == IRpcProtocol.XWB_DISCONNECTED_WITH_BYE) {
                        retry = true;
                        log(Logger.WARNING, "VistA Broker disconnected with unexpected #BYE#.");
                    }
                }
                created = false;
            } finally {
                if (retry) {
                    // VistA broker terminated connection, try to reconnect and resend
                    String results = null;
                    try {
                        results = doReconnect();
                        log(Logger.WARNING, "Reconnected successfully.");
                    } catch(Exception e) {
                        log(Logger.ERROR, e);
                    }
                    if (results != null) {
                        try {
                            created = false;
                            ArrayList list = StringUtils.getArrayList(results);
                            if ((list != null) && (list.size() >= 1)) {
                                x  = (String)list.get(0);
                                if ((x != null) && (x.length() > 0) && (x.charAt(0) == '1')) {
                                    created = true;
                                    setContext(aContext);
                                    if (rpcProtocol.getEventHandler() != null)
                                        rpcProtocol.getEventHandler().onRpcBrokerCreateContext(aContext);
                                }
                            }
                        } catch(Exception e) {
                            log(Logger.ERROR, e);
                            created = false;
                        }
                    }
                }
                if (x != null) {
                    // If the context was created, then set the current context
                    if ((x.length() > 0) && (x.charAt(0) == '1')) {
                        setContext(aContext);
                        if (rpcProtocol.getEventHandler() != null)
                            rpcProtocol.getEventHandler().onRpcBrokerCreateContext(aContext);
                        created = true;
                    } else
                      created = false;
                } else
                  created = false;
                if (!created) {
                    created = false;
                }
                unlockCallMonitor();
            }
        }
        return created;    
    }
    
    private String doReconnect() throws Exception {
      return doReconnect(false, null);
    }
    
    private String doReconnect(boolean recreateContext, String aContext) throws Exception {
        String results = null;
        // Construct a new RpcBroker object to handle reconnection and signon
        RpcBroker rpcBroker = new RpcBroker(this.rpcProtocolVersion);
        // Reconnect and signon
        try {
            if (rpcBroker.connect(this.server, this.listenerPort)) {
                if (rpcBroker.signon(this.avCodes) != null) {
                    try {
                        // Set this object's params, remote procedure, and context to surrogate rpc broker
                        rpcBroker.setParams(this.params);
                        rpcBroker.setRemoteProcedure(this.remoteProcedure);
                        if (recreateContext) {
                            rpcBroker.setCurrentContext(aContext);
                        }
                        // Try sending message again
                        results = rpcBroker.call();
                        // Assign surrogate rpc broker's socket to this broker
                        this.rpcProtocol = rpcBroker.getRpcProtocol();
                    } catch(BrokerException be) {
                        log(Logger.ERROR, "Re-send failed.");
                        log(Logger.ERROR, be);
                        throw be;
                    }
                } else
                  log(Logger.ERROR, "Re-signon failed");
            } else
                log(Logger.ERROR, "Re-connect failed");
        } catch(BrokerException be) {
            log(Logger.ERROR, be);
            results = null;
        }        
        return results;
    }    
    
    // MAIN METHOD
    public static void main(String[] args) {
        // If user didn't pass the required command-line params, then print usage and exit
        String host = null;
        int port = 0;
        String access = null;
        String verify = null;
        if (args.length == 0) {
            System.out.println("Signs the user on to the VistA Broker server and prints the user's DUZ.");
            System.out.println("Usage: java RpcBroker SERVER PORT ACCESS VERIFY");
            System.out.println("where SERVER AND PORT are the host name and port of the broker server and.");
            System.out.println("      ACCESS and VERIFY are the users signon codes.");
            System.exit(1);
        }  else {
            host = args[0];
            port = Integer.valueOf(args[1]).intValue();
            access = args[2];
            verify = args[3];
        }
        RpcBroker rpcBroker = null;
        try {
            rpcBroker = new RpcBroker(host, port);
            String duz = rpcBroker.doSignon(access, verify);
            System.out.println("DUZ = " + duz);
            System.out.println("Name = " + rpcBroker.getUserName());
            System.out.println("Station = " + rpcBroker.getStation());
            System.out.println("Station IEN = " + rpcBroker.getStationIen());
            System.out.println("Station No = " + rpcBroker.getStationNo());
            System.out.println("User Domain = " + rpcBroker.getDomain());
            System.out.println("User Class = " + rpcBroker.getUserClass());
            System.out.println("Title = " + rpcBroker.getTitle());
            System.out.println("Service ID = " + rpcBroker.getService());
            System.out.println("Service/Section = " + rpcBroker.getServiceSection());
        } catch(Exception e) {
            System.err.println(e.getMessage());
        } finally {
            try {
                if (rpcBroker != null)
                    rpcBroker.disconnect();
            } catch(Exception e) {
                System.err.println("Error disconnecting: " + e.getMessage());
            }
        }
    }
}
