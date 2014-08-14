package gov.va.med.lom.vistabroker.service.impl;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import javax.naming.InitialContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;

import gov.va.med.lom.foundation.service.response.CollectionServiceResponse;
import gov.va.med.lom.foundation.service.response.ServiceResponse;

import gov.va.med.lom.javaUtils.misc.ReflectUtil;

import gov.va.med.lom.vistabroker.exception.*;
import gov.va.med.lom.vistabroker.service.Service;
import gov.va.med.lom.vistabroker.dao.BaseDao;
import gov.va.med.lom.vistabroker.security.ISecurityContext;

public class BaseService implements Service {

	/*
	 * FIELDS
	 */
	protected String defaultContext;
	protected String rpcName;
	protected ISecurityContext securityContext;
	protected boolean logEvent;
	protected BaseDao baseDao;
	protected InitialContext ctx;

	protected static final Log log = LogFactory.getLog(BaseService.class);

	/*
	 * CONSTRUCTORS
	 */
	public BaseService() {
		super();
		this.defaultContext = null;
		this.rpcName = null;
		this.securityContext = null;
		this.baseDao = null;
		this.logEvent = true;

	}

	/*
	 * SECURITY METHODS
	 */
	public void setSecurityContext(ISecurityContext securityContext) {
		if ((securityContext != null)
				&& ((securityContext.getType() >= ISecurityContext.APP_PROXY_CONNECTION_SPEC) && (securityContext
						.getType() <= ISecurityContext.VPID_CONNECTION_SPEC))) {
			// Check whether the security ID is valid
			this.securityContext = securityContext;
		}
	}

	/*
	 * PROPERTY ACCESSORS
	 */
	public ISecurityContext getSecurityContext() {
		return securityContext;
	}

	public String getRpcName() {
		return rpcName;
	}

	public void setRpcName(String rpcName) {
		this.rpcName = rpcName;
	}

	public String getDefaultContext() {
		return defaultContext;
	}

	public void setDefaultContext(String defaultContext) {
		this.defaultContext = defaultContext;
	}

	public String getResults() {
		if (baseDao != null)
			return baseDao.getResults();
		else
			return null;
	}

	public String getResultType() {
		if (baseDao != null)
			return baseDao.getResultType();
		else
			return null;
	}

	public Document getDocument() throws Exception {
		if (baseDao != null)
			return baseDao.getDocument();
		else
			return null;
	}
	
	// Implements Service
	public boolean isAlive() {
	  return true;
	}

	// Flag set by sub-classes to turn on/off event logging
	protected void setLogEvent(boolean logEvent) {
		this.logEvent = logEvent;
	}

	@SuppressWarnings("rawtypes")
	protected Object invokeCall(Class daoClass, String methodName)
			throws VistaBrokerException, IllegalArgumentException,
			IllegalAccessException, InvocationTargetException,
			NoSuchMethodException {
		Object[] args = {};
		return invokeCall(daoClass, methodName, args);
	}

	@SuppressWarnings("rawtypes")
	protected Object invokeCall(Class daoClass, String methodName, Object arg)
			throws VistaBrokerException, IllegalArgumentException,
			IllegalAccessException, InvocationTargetException,
			NoSuchMethodException {
		Object[] args = { arg };
		return invokeCall(daoClass, methodName, args);
	}

	@SuppressWarnings("rawtypes")
	protected Object invokeCall(Class daoClass, String methodName, Object[] args)
			throws VistaBrokerException, IllegalArgumentException,
			IllegalAccessException, InvocationTargetException,
			NoSuchMethodException {

		Class[] argCls = new Class[args.length];
		for (int i = 0; i < argCls.length; i++) {
			if (args[i] == null)
				argCls[i] = null;
			else
				argCls[i] = args[i].getClass();
		}
		Object result = null;

		try {

			Method method = ReflectUtil.getMethod(daoClass, methodName, argCls);
			BaseDao baseDao = getDao(daoClass);
			// Set the default context
			baseDao.setDefaultContext(defaultContext);

			// if there is an rpc name in the security context it trumps the
			// the default rpc name so set it in the DAO
			if (securityContext.getRpcName() != null
					&& securityContext.getRpcName().trim().length() > 0) {
				baseDao.setRpcName(securityContext.getRpcName());
			}

			// if however, the user actually sets the rpc name directly in the
			// bus
			// then it trumps all
			if ((rpcName != null) && (rpcName.trim().length() > 0))
				baseDao.setRpcName(rpcName);

			// Log the call
			if (logEvent) {
				try {
					logCall(methodName);
				} catch (Exception e) {
					log.warn("Could not log the call: " + methodName, e);
				}
			}

			// Invoke the method
			result = method.invoke(baseDao, args);

		} finally {
			setRpcName(null);
		}

		return result;
	}

	@SuppressWarnings("rawtypes")
	protected <T> ServiceResponse<T> singleResult(Class T, Class daoClass,
			String methodName) {
		Object[] args = {};
		return singleResult(T, daoClass, methodName, args);
	}

	@SuppressWarnings("rawtypes")
	protected <T> ServiceResponse<T> singleResult(Class T, Class daoClass,
			String methodName, Object arg) {
		Object[] args = { arg };
		return singleResult(T, daoClass, methodName, args);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected <T> ServiceResponse<T> singleResult(Class T, Class daoClass,
			String methodName, Object[] args) {

		ServiceResponse<T> response = new ServiceResponse<T>();

		try {

			T result = (T) invokeCall(daoClass, methodName, args);
			response.setPayload(result);

		} catch (Exception ex) {
		  
		  Throwable cause;
		  if (ex instanceof InvocationTargetException) {
		    cause = ex.getCause();
		  } else {
		    cause = ex;
		  }
		  
			if (cause instanceof VistaBrokerException) {
				if (cause instanceof VistaBrokerSocketTimeoutException) {
					// add the timeout as a property
					String[] properties = { String
							.valueOf(((VistaBrokerSocketTimeoutException) cause)
									.getTimeout()) };
					response.addError(((VistaBrokerException) cause).getKey(),
							properties, null);
				} else {
					response.addError(((VistaBrokerException) cause).getKey());
				}
			} else {
				response.addError(cause.getMessage());
			}
			log.error("VistaBrokerException", cause);
		}

		return response;
	}

	@SuppressWarnings("rawtypes")
	protected <T> CollectionServiceResponse<T> collectionResult(Class T,
			Class daoClass, String methodName) {
		Object[] args = {};
		return collectionResult(T, daoClass, methodName, args);
	}

	@SuppressWarnings("rawtypes")
	protected <T> CollectionServiceResponse<T> collectionResult(Class T,
			Class daoClass, String methodName, Object arg) {
		Object[] args = { arg };
		return collectionResult(T, daoClass, methodName, args);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected <T> CollectionServiceResponse<T> collectionResult(Class T,
			Class daoClass, String methodName, Object[] args) {

		CollectionServiceResponse<T> response = new CollectionServiceResponse<T>();

		try {

			Collection<T> result = (Collection<T>) invokeCall(daoClass,
					methodName, args);
			response.setCollection(result);

		} catch (Exception ex) {
		  
      Throwable cause;
      if (ex instanceof InvocationTargetException) {
        cause = ex.getCause();
      } else {
        cause = ex;
      }
		  
			if (cause instanceof VistaBrokerException) {
				if (cause instanceof VistaBrokerSocketTimeoutException) {
					// add the timeout as a property
					String[] properties = { String
							.valueOf(((VistaBrokerSocketTimeoutException) cause)
									.getTimeout()) };
					response.addError(((VistaBrokerException) cause).getKey(),
							properties, null);
				} else {
					response.addError(((VistaBrokerException) cause).getKey());
				}
			} else {
				response.addError(cause.getMessage());
			}
			log.error("VistaBrokerException", cause);
		}

		return response;
	}

	// Instantiates a DAO of the given class
	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected BaseDao getDao(Class daoClass) throws VistaBrokerException {
		if (securityContext == null)
			throw new VistaBrokerSecurityException("client not authenticated");
		try {
			Class[] argCls = {};
			Object[] argObjs = {};
			Constructor ct = daoClass.getConstructor(argCls);
			baseDao = (BaseDao) ct.newInstance(argObjs);
			baseDao.setSecurityContext(securityContext);
		} catch (Exception e) {
			log.error("Error instantiating DAO: ", e);
		}
		return baseDao;
	}
	
	// Log the call
	protected void logCall(String call) {
		/* ******** Comment Out DB Logging For Performance Gains ************* */
		/*
		 * switch(securityContext.getType()) { case
		 * ISecurityContext.APP_PROXY_CONNECTION_SPEC :
		 * vistaBrokerSecurityLocal.logAppProxyCall(appGuid.getId(),
		 * securityContext.getDivision(), securityContext.getUserId(), call);
		 * break; case ISecurityContext.CCOW_DIVISION_CONNECTION_SPEC : case
		 * ISecurityContext.CCOW_HOST_CONNECTION_SPEC :
		 * vistaBrokerSecurityLocal.logCcowCall(appGuid.getId(),
		 * securityContext.getDivision(), securityContext.getUserId(), call);
		 * break; case ISecurityContext.DUZ_CONNECTION_SPEC :
		 * vistaBrokerSecurityLocal.logDuzCall(appGuid.getId(),
		 * securityContext.getDivision(), securityContext.getUserId(), call);
		 * break; case ISecurityContext.VPID_CONNECTION_SPEC :
		 * vistaBrokerSecurityLocal.logAppProxyCall(appGuid.getId(),
		 * securityContext.getDivision(), securityContext.getUserId(), call);
		 * break; }
		 */
	}

}
