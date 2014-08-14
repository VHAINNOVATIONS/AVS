 package gov.va.med.lom.foundation.service.response;


import gov.va.med.lom.foundation.service.response.messages.DefaultMessages;
import gov.va.med.lom.foundation.service.response.messages.Message;
import gov.va.med.lom.foundation.service.response.messages.MessageFactory;
import gov.va.med.lom.foundation.service.response.messages.Messages;
import gov.va.med.lom.foundation.service.response.messages.MessagesUtils;
import gov.va.med.lom.foundation.service.response.messages.Severity;
import gov.va.med.lom.foundation.util.MessagesStringBuilder;
import gov.va.med.lom.foundation.util.Precondition;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * Base service response used to marshall messages, this includes
 * capabilities to handle error and informational messages
 * 
 * @author Chris Morrisette - Sep 11, 2007
 *
 */
public class BaseServiceResponse implements Serializable{

    private static final Log LOG = LogFactory.getLog(BaseServiceResponse.class);
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -186563496582930635L;
	
	/**
	 * Copies all message from one service response to another 
	 * @param copyTo The service response to copy the messages to
	 * @param copyFrom The service response to copy the messages from
	 */
	public static void copyMessages(BaseServiceResponse copyTo, 
	    BaseServiceResponse copyFrom) 
	{
	    if ((copyTo != null) && (copyFrom != null)) {
	        copyTo.getMessages().addMessages(copyFrom.getMessages());
	    }
	}

	/**
	 * Determines (null safe) if the response has any error messages.
	 * @param response The response to check
	 * @return True if the response has any error messages; false otherwise.
	 */
	public static boolean hasErrorMessage(BaseServiceResponse response) {
	    return (response != null) && MessagesUtils.hasErrorMessage(
	        response.getMessages());
	}

	/**
	 * Determines (null safe) if the response has a specific error message.
	 * @param response The response to check
	 * @parame key The error message to check for
	 * @return True if the response has the given error message; 
	 * false otherwise.
	 */
	public static boolean hasErrorMessage(BaseServiceResponse response, 
	    String key) 
	{
	    return (response != null) && MessagesUtils.hasErrorMessage(response.
	        getMessages(), key);
	}

    /**
     * Determines (null safe) if the response has any error messages.
     * @param response The response to check
     * @return True if the response has any error messages; false otherwise.
     */
    public static boolean hasWarningMessage(BaseServiceResponse response) {
        return (response != null) && MessagesUtils.hasWarningMessage(
            response.getMessages());
    }

    /**
     * Determines (null safe) if the response has a specific error message.
     * @param response The response to check
     * @parame key The error message to check for
     * @return True if the response has the given error message; 
     * false otherwise.
     */
    public static boolean hasWarningMessage(BaseServiceResponse response, 
        String key) 
    {
        return (response != null) && MessagesUtils.hasWarningMessage(response.
            getMessages(), key);
    }

    /**
     * Determines (null safe) if the response has any error messages.
     * @param response The response to check
     * @return True if the response has any error messages; false otherwise.
     */
    public static boolean hasInformationalMessage(BaseServiceResponse response) {
        return (response != null) && MessagesUtils.hasInformationalMessage(
            response.getMessages());
    }

    /**
     * Determines (null safe) if the response has a specific error message.
     * @param response The response to check
     * @parame key The error message to check for
     * @return True if the response has the given error message; 
     * false otherwise.
     */
    public static boolean hasInformationalMessage(BaseServiceResponse response, 
        String key) 
    {
        return (response != null) && MessagesUtils.hasInformationalMessage(
            response.getMessages(), key);
    }

	/**
	 * Logs the messages in a given response.
	 * @param response The response to check.
	 * @param clazz The class used to obtain the resource bundle.
	 * @return True if the response has any error messages; false otherwise.
	 */
	public static void logMessages(BaseServiceResponse response, Class clazz, 
	    Log log) 
	{
	    logMessages(null, response, clazz, log);
    }
    public static void logMessages(String message, BaseServiceResponse response, 
        Class clazz, Log log) 
    {
	    if (response == null) {
	        return;
	    }
	    Precondition.assertNotNull("clazz", clazz);
	    MessagesStringBuilder builder = new MessagesStringBuilder();
	    builder.append(response.getMessages(), clazz);
	    logMessages(message, builder, log);
	}

	private static void logMessages(String message, 
        MessagesStringBuilder builder, Log log) 
    {
	    if (log == null) {
	        log = LOG;
	    }
        message = (StringUtils.isBlank(message)) ? "" : message + ". ";
        String errors = builder.getErrorString();
        if (!StringUtils.isBlank(errors)) {
            log.error(message + errors);
        }
        if (log.isWarnEnabled()) {
            String warn = builder.getWarningString();
            if (!StringUtils.isBlank(warn)) {
                log.warn(message + warn);
            }
        }
	    if (log.isInfoEnabled()) {
            String info = builder.getInfoString();
            if (!StringUtils.isBlank(info)) {
                log.info(message + info);
            }
	    }
	}

	public static String messagesToString(BaseServiceResponse response, 
	    Class clazz) 
	{
	    if (response == null) {
	        return "";
	    }
	    Precondition.assertNotNull("clazz", clazz);
	    MessagesStringBuilder builder = new MessagesStringBuilder();
	    builder.append(response.getMessages(), clazz);
	    return builder.toString();
	}
	
	protected Messages messages;

	/**
	 * Get the messages contained within this response
	 * @return messages object containing any messages.  
	 * If no messages are contained, its empty
	 */
	public final Messages getMessages() {
		if (messages == null) {
    		messages = new DefaultMessages();
		}
		return messages;
	}
	
	/**
	 * Add a message to the message queue
	 * @param severity severity of the message
	 * @param msgKey key of the message to look up 
	 * @param inserts any inserts to be inserted into the message
	 */
	public void addMessage(Severity severity, String msgKey, Object[] inserts) {
		addMessage(msgKey, new String [] {}, inserts, severity);
	}

	/**
	 * Add a message to the message queue
	 * @param key key of the message to look up 
	 * @param properties property of the message
	 * @param inserts any inserts to be inserted into the message
	 * @param severity severity of the message
	 */
	protected void addMessage(String key, String [] properties, 
		Object [] inserts, Severity severity) 
	{
    	Message message = MessageFactory.createMessage();
        message.setSeverity( severity);
        message.setKey(key);
        
        int propertiesLength = (properties != null) ? properties.length : 0;
        for (int i=0; i<propertiesLength; i++) {
        	message.addProperty(properties[i]);
        }
        
        int insertsLength = (inserts != null) ? inserts.length : 0;
        for (int i=0; i<insertsLength; i++) {
        	message.addInsert(inserts[i]);
        }
        
        getMessages().addMessage(message);	    	
    }

	/**
	 * Add an error to the message queue
	 * @param key key of the message to look up 
     * @param properties The list of properties that the message applies to
	 * @param inserts any inserts to be inserted into the message
	 */
    public void addError(String key, String [] properties, Object [] inserts) {
    	addMessage( key, properties, inserts, Severity.ERROR);	
    }	
    
    /**
     * Add an error to the message queue
     * @param msgKey key of the message to look up 
     * @param inserts any inserts to be inserted into the message
     */
    public void addError(String msgKey, Object[] inserts) {
        addMessage(Severity.ERROR, msgKey, inserts);
    }
    /**
     * Add an error to the message queue
     * @param msgKey key of the message to look up 
     */
    public void addError(String msgKey) {
        addError(msgKey, null);
    }

    /**
     * Add an informational message to the message queue
     * @param key key of the message to look up 
     * @param properties The list of properties that the message applies to
     * @param inserts any inserts to be inserted into the message
     */
    public void addWarning(String key, String [] properties, Object [] inserts) {
        addMessage(key, properties, inserts, Severity.WARNING);    
    }   
    /**
     * Add info to the message queue
     * @param msgKey key of the message to look up 
     * @param inserts any inserts to be inserted into the message
     */
    public void addWarning(String msgKey, Object[] inserts) {
        addMessage(Severity.WARNING, msgKey, inserts);
    }
    /**
     * Add info to the message queue
     * @param msgKey key of the message to look up 
     */
    public void addWarning(String msgKey) {
        addWarning(msgKey, null);
    }
    
    
    /**
     * Add an informational message to the message queue
     * @param key key of the message to look up 
     * @param properties The list of properties that the message applies to
     * @param inserts any inserts to be inserted into the message
     */
    public void addInfo(String key, String [] properties, Object [] inserts) {
        addMessage(key, properties, inserts, Severity.INFORMATIONAL);    
    }   
	/**
	 * Add info to the message queue
     * @param msgKey key of the message to look up 
     * @param inserts any inserts to be inserted into the message
	 */
	public void addInfo(String msgKey, Object[] inserts) {
		addMessage(Severity.INFORMATIONAL, msgKey, inserts);
	}
	/**
	 * Add info to the message queue
     * @param msgKey key of the message to look up 
	 */
	public void addInfo(String msgKey) {
		addInfo(msgKey, null);
	}

    public static boolean convertMessage(BaseServiceResponse response, 
        String key, Severity severity, String newKey, Severity newSeverity)
    {
       Precondition.assertNotNull("response", response);
       return MessagesUtils.findAndConvertMessage(response.getMessages(), key, 
           severity, newKey, newSeverity);
    }    

}
