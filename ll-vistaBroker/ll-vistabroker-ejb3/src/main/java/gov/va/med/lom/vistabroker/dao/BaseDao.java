package gov.va.med.lom.vistabroker.dao;

import gov.va.med.exception.FoundationsException;
import gov.va.med.lom.javaUtils.misc.StringUtils;
import gov.va.med.lom.vistabroker.exception.VistaBrokerException;
import gov.va.med.lom.vistabroker.exception.VistaBrokerIllegalArgumentException;
import gov.va.med.lom.vistabroker.exception.VistaBrokerInvalidResultException;
import gov.va.med.lom.vistabroker.exception.VistaBrokerSecurityException;
import gov.va.med.lom.vistabroker.exception.VistaBrokerSocketTimeoutException;
import gov.va.med.lom.vistabroker.exception.VistaBrokerConnectionException;
import gov.va.med.lom.vistabroker.security.ISecurityContext;
import gov.va.med.vistalink.adapter.cci.VistaLinkAppProxyConnectionSpec;
import gov.va.med.vistalink.adapter.cci.VistaLinkCcowConnectionSpec;
import gov.va.med.vistalink.adapter.cci.VistaLinkConnection;
import gov.va.med.vistalink.adapter.cci.VistaLinkConnectionFactory;
import gov.va.med.vistalink.adapter.cci.VistaLinkConnectionSpec;
import gov.va.med.vistalink.adapter.cci.VistaLinkDuzConnectionSpec;
import gov.va.med.vistalink.adapter.cci.VistaLinkVpidConnectionSpec;
import gov.va.med.vistalink.institution.InstitutionMappingDelegate;
import gov.va.med.vistalink.rpc.RpcRequest;
import gov.va.med.vistalink.rpc.RpcRequestFactory;
import gov.va.med.vistalink.rpc.RpcResponse;

import java.util.List;
import java.util.ResourceBundle;

import javax.naming.InitialContext;
import javax.resource.ResourceException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;

public class BaseDao {

	/*
	 * FIELDS
	 */
	private String defaultContext;            // the default rpc context
	private String defaultRpcName;            // the default rpc name
	private String rpcName;                   // the rpc name
	protected ISecurityContext securityContext; // the security context
	private RpcResponse rpcResponse;          // rpc response object 
	private VistaLinkConnection conn;         // the vistalink connection object
	private int timeoutForCall;               // the request timeout for a single call

	private static final Log log = LogFactory.getLog(BaseDao.class);
	private static final int DEF_REQ_TIMEOUT = 15000;  // 15 seconds

	private static String jndiPrefix;          // prefix used with vistalink jndi name (may vary with JEE server)
	
	/*
	 * Exception Codes
	 */
	public static final int INVALID_SECURITY_PARAMS   = 1;
	public static final int VISTALINK_FAULT_EXCEPTION = 2;
	public static final int SOCKET_TIMEOUT            = 3;
	public static final int NO_MANAGED_CONNECTIONS    = 4;
	public static final int VISTA_CONNECTION_ERROR    = 5;
	public static final int UNDETERMINED_EXCEPTION    = 6;

	/*
	 * Static Initializer
	 */
  static {
    
    ResourceBundle res = ResourceBundle.getBundle("vistabroker");
    
    jndiPrefix =  res.getString("jndi.prefix");
    if (jndiPrefix == null)
      jndiPrefix = "";
    
  }
	
	/*
	 * CONSTRUCTORS
	 */
	public BaseDao() {
		super();
		this.defaultContext = null;
		this.defaultRpcName = null;
		this.rpcName = null;
		this.securityContext = null;
		this.conn = null;
		this.timeoutForCall = 0;
	}

	public BaseDao(BaseDao baseDao) {
		this.defaultContext = baseDao.getDefaultContext();
		this.defaultRpcName = baseDao.getDefaultRpcName();
		this.rpcName = baseDao.getRpcName();
		this.securityContext = baseDao.getSecurityContext();
		this.conn = baseDao.conn;
	}

	/*
	 * PROPERTY ACCESSORS
	 */
	public String getDefaultContext() {
		return defaultContext;
	}

	public void setDefaultContext(String defaultContext) {
		if (this.defaultContext == null)
			this.defaultContext = defaultContext;
	}

	public String getDefaultRpcName() {
		return defaultRpcName;
	}

	public void setDefaultRpcName(String defaultRpcName) {
		this.defaultRpcName = defaultRpcName;
	}

	public String getRpcName() {
		return rpcName;
	}

	public void setRpcName(String rpcName) {
		this.rpcName = rpcName;
	}

	public String getDivision() {
		return securityContext.getDivision();
	}

	public String getUserId() {
		return securityContext.getUserId();
	}

	public ISecurityContext getSecurityContext() {
		return this.securityContext;
	}

	public void setSecurityContext(ISecurityContext securityContext) {
		this.securityContext = securityContext;
	}

	public static Log getLog() {
		return log;
	}

	public RpcResponse getRpcResponse() {
		return rpcResponse;
	}

	public String getResults() {
		return rpcResponse.getResults();
	}

	public String getResultType() {
		return rpcResponse.getResultsType();
	}

	public int getTimeoutForCall() {
		return timeoutForCall;
	}

	public void setTimeoutForCall(int timeoutForCall) {
		this.timeoutForCall = timeoutForCall;
	}

	public Document getDocument() throws Exception {
		try {
			return rpcResponse.getResultsDocument();
		} catch(Exception e) {
			log.error("Exception occurred getting XML document.", e);
			throw e;
		}
	}

	public void close() {
		if (conn != null) {
			try {
				conn.close();
			} catch (ResourceException e) {
				log.error("Error closing connection:", e);
			} finally {
				conn = null;
			}
		}
	}

	/*
	 * RPC CALL METHODS
	 */
	protected String sCall() throws VistaBrokerException {
		String[] params = {};
		return sCall(params);
	}  

	protected String sCall(Object param) throws VistaBrokerException {
		Object[] params = {param};
		return sCall(params);
	}

	protected String sCall(Object[] params) throws VistaBrokerException {
		try {
			RpcRequest req = null;
			req = RpcRequestFactory.getRpcRequest();
			// Set the rpc name
			if (rpcName != null && rpcName.trim().length() > 0){
				req.setRpcName(rpcName);
			} else {
				req.setRpcName(defaultRpcName);
			}
			
			if(req.getRpcName() == null){
				throw new VistaBrokerIllegalArgumentException("rpc name not specified.");
			}
						
			req.clearParams();
			if (params != null) {
				for (int i = 0; i < params.length; i++) {
					String type = null;
					if (params[i] == null)
						params[i] = "";
					if ((params[i].getClass() == java.util.HashMap.class) ||
					    (params[i].getClass() == java.util.Hashtable.class) ||
					    (params[i].getClass() == java.util.TreeMap.class) || 
							(params[i].getClass() == java.util.ArrayList.class) ||
							(params[i].getClass() == java.util.HashSet.class) || 
							(params[i].getClass() == java.util.TreeSet.class))
						type = "array";
					else
						type = "string";
					req.getParams().setParam(i+1, type, params[i]);
				}
			}
			return sCall(req);
		} catch (FoundationsException e1) {
			log.error("Foundations Exception",e1);
			return null;
		}     
	}

	protected String sCall(RpcRequest req) throws VistaBrokerException {
	    
	    // set socket timeout to defaults
        int timeout = DEF_REQ_TIMEOUT;
        
		try {
			
			// Set context
			if (securityContext.getRpcContext() != null) {
				req.setRpcContext(securityContext.getRpcContext());
			} else {
				req.setRpcContext(defaultContext);
			}
			
			if(req.getRpcContext() == null || req.getRpcContext().trim().length() == 0){
				throw new VistaBrokerIllegalArgumentException("no rpc context identified");
			}
			
			// Create a connection if necessary
			String jndiName = "";
			if (conn == null) {
				InitialContext ic = new InitialContext();
				VistaLinkConnectionSpec connSpec = null;
				// Determine how to connect
				switch(securityContext.getType()) {
				case ISecurityContext.VPID_CONNECTION_SPEC : 
					connSpec = new VistaLinkVpidConnectionSpec(securityContext.getDivision(), securityContext.getUserId());
					break;
				case ISecurityContext.APP_PROXY_CONNECTION_SPEC : 
					connSpec = new VistaLinkAppProxyConnectionSpec(securityContext.getDivision(), securityContext.getUserId());
					break;
				case ISecurityContext.CCOW_DIVISION_CONNECTION_SPEC : 
				case ISecurityContext.CCOW_HOST_CONNECTION_SPEC : 
					connSpec = new VistaLinkCcowConnectionSpec(securityContext.getUserId());
					break;
				case ISecurityContext.DUZ_CONNECTION_SPEC : 
					connSpec = new VistaLinkDuzConnectionSpec(securityContext.getDivision(), securityContext.getUserId()); 
					break;            
				}
				if (connSpec != null) {
				  try {
					  jndiName = jndiPrefix + InstitutionMappingDelegate.getJndiConnectorNameForInstitution(securityContext.getDivision());
					  VistaLinkConnectionFactory cf = (VistaLinkConnectionFactory) ic.lookup(jndiName);
					  conn = (VistaLinkConnection) cf.getConnection(connSpec);
				  } catch(Exception e) {
	          throw new VistaBrokerConnectionException(e, VISTA_CONNECTION_ERROR, 
	              "Exception occurred when creating managed connection.",
	              req.getRpcName(), req.getRpcContext(), 
	              securityContext.getDivision(), securityContext.getUserId());				    
				  }
				} else
					throw new VistaBrokerSecurityException(null, INVALID_SECURITY_PARAMS, 
							"Invalid Division, DUZ, VPID, App Proxy, or CCOW handle.",
							req.getRpcName(), req.getRpcContext(), 
							securityContext.getDivision(), securityContext.getUserId());
			}
					
	        // see if the calling RPC needs to override defaults
			if (timeoutForCall > DEF_REQ_TIMEOUT) {
			    timeout = 0;
            }
			// see if the calling app needs to override set below default values
			else if(securityContext.getSocketTimeout() > DEF_REQ_TIMEOUT){
                
			    timeout = securityContext.getSocketTimeout();
			}
			
			conn.setTimeOut(timeout);
			
			// Execute the RPC and return the results
			log.debug("connection: " + jndiName + " context: " +  req.getRpcContext() + " rpc: " + req.getRpcName());
			rpcResponse = conn.executeRPC(req);
			return rpcResponse.getResults();

		}catch(gov.va.med.net.VistaSocketTimeOutException e){ 
		    VistaBrokerSocketTimeoutException vbe = new VistaBrokerSocketTimeoutException(e);
		    vbe.setTimeout(timeout);
		    throw vbe;
	    }catch(Exception e) {
			log.error("Error", e);

		} finally {
			try{
				this.close();
			}catch(Exception e){
				/* no op */
			}
		}
		// if we are here then we need to throw an exception because a success 
		// process will never end in a null result
		throw new VistaBrokerInvalidResultException("rpc call error.  results null. please check logs for problems");
	}

	protected List<String> lCall() throws VistaBrokerException {
		return StringUtils.getStringList(sCall());
	}  

	protected List<String> lCall(Object[] params) throws VistaBrokerException {
		return StringUtils.getStringList(sCall(params));
	}

	protected List<String> lCall(Object param) throws VistaBrokerException {
		return StringUtils.getStringList(sCall(param));    
	}

	protected List<String> lCall(RpcRequest req) throws VistaBrokerException {
		return StringUtils.getStringList(sCall(req));
	}

}
