/**
 * 
 */
package gov.va.med.lom.foundation.service;


import gov.va.med.lom.foundation.service.response.BaseServiceResponse;
import gov.va.med.lom.foundation.service.response.messages.CoreMessages;
import gov.va.med.lom.foundation.service.response.messages.Message;
import gov.va.med.lom.foundation.service.response.messages.Messages;
import gov.va.med.lom.foundation.service.response.messages.MessagesUtils;
import gov.va.med.lom.foundation.service.response.messages.Severity;
import gov.va.med.lom.foundation.util.Precondition;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * @author Rob Proper (Aquilent Inc.)
 *
 */
public abstract class AbstractBaseService  {

    private static final Log LOG = LogFactory.getLog(AbstractBaseService.class);

    /**
     * Returns the Log instance of this service.
     * Override this method to yield the implementation's static log instance.
     * @return
     */
    protected abstract Log getLocalLog();

    /**
     * Returns the Log instance of this service.
     * Override this method to yield the implementation's static log instance.
     * @return
     */
    protected final Log getLog() {
        Log log = getLocalLog();
        return (log != null) ? log : LOG;
    }

    public boolean isAlive() {
  	  return true;
  	}
    
    /**
     * Throws an IllegalStateException when an error is found in the 
     * response with the given message. 
     * @param message The message to show in the exception
     * @param response The response to validate
     */
    protected void yieldOnErrors(String message, BaseServiceResponse response) {
        if (hasErrorMessage(response)) {
            logMessages(response);
            throw new IllegalStateException(message);
        }
    }
    
    /**
     * Determines (null safe) if the response has any error messages.
     * @param response The response to check
     * @return True if the response has any error messages; false otherwise.
     */
    protected boolean hasErrorMessage(BaseServiceResponse response) {
        return BaseServiceResponse.hasErrorMessage(response);
    }

    /**
     * Determines (null safe) if the messages instance any error messages.
     * @param messages The messages to check
     * @return True if the messages instance has any error messages; 
     * false otherwise.
     */
    protected boolean hasErrorMessage(Messages messages) {
        return MessagesUtils.hasErrorMessage(messages);
    }

    /**
     * Determines (null safe) if the response has a specific error message.
     * @param response The response to check
     * @parame key The error message to check for
     * @return True if the response has the given error message; 
     * false otherwise.
     */
    protected boolean hasErrorMessage(BaseServiceResponse response, 
        String key) 
    {
        return BaseServiceResponse.hasErrorMessage(response, key);
    }

    /**
     * Copies all message from one service response to another. 
     * @param copyTo The service response to copy the messages to
     * @param copyFrom The service response to copy the messages from
     */
    protected void copyMessages(BaseServiceResponse copyTo, 
        BaseServiceResponse copyFrom) 
    {
        BaseServiceResponse.copyMessages(copyTo, copyFrom);
    }

    /**
     * Copies all messages when the response has an error message.
     * @param copyTo The response to copy the messages to.
     * @param copyFrom The response to check for errors.
     * @return Returns true if the copyFrom response has an error message; 
     * Otherwise it returns false.
     */
    protected boolean copyMessagesOnError(BaseServiceResponse copyTo, 
        BaseServiceResponse copyFrom) 
    {
        Precondition.assertNotNull("copyTo", copyFrom);
        if (!hasErrorMessage(copyFrom)) {
            return false;
        }
        copyMessages(copyTo, copyFrom);
        return true;
    }

    /**
     * Throws an runtime exception if the given response has error messages.
     * All messages are logged.
     * @param operation The operation that invokes this call
     * @param response The response to check for errors.
     * @throws RuntimeException Throws a runtime exception with the 
     * description set to {@link CoreMessages#UNKNOWN_ERROR}.
     */
    protected void handleMessages(String operation, 
        BaseServiceResponse response) 
    {
        if (response == null) {
            return;
        }
        logMessages(operation, response.getMessages());
        if (BaseServiceResponse.hasErrorMessage(response)) {
            throw new RuntimeException(CoreMessages.UNKNOWN_ERROR);
        }
    }

    /**
     * Logs the messages in a given response.
     * @param response The response to check.
     * @param clazz The class used to obtain the resource bundle.
     * @return True if the response has any error messages; false otherwise.
     */
    protected void logMessages(BaseServiceResponse response){
        Precondition.assertNotNull("response", response);
        BaseServiceResponse.logMessages(response, getClass(), getLog());
    }
    protected void logMessages(String operation, BaseServiceResponse response) {
        Precondition.assertNotNull("response", response);
        logMessages(operation, response.getMessages());
    }
    protected void logMessages(String operation, Messages messages) {
        Precondition.assertNotNull("messages", messages);
        if (isEmpty(messages)) {
            return;
        }
        if (operation == null) { 
            operation = "<unknown operation>";
        }
        Log log = getLog();
        if (messages.hasErrorMessages()) {
            log.error(operation + " failed, because " + messages.
                getErrorMessages());
        }
        if (messages.hasWarningMessages()) {
            if (log.isWarnEnabled()) {
                log.warn(operation + ": " + messages.
                    getWarningMessages());
            }
        }
        if (messages.hasInformationalMessages()) {
            if (log.isInfoEnabled()) {
                log.error(operation + ": " + messages.
                    getInformationalMessages());
            }
        }
    }
    
    private boolean isEmpty(Messages messages) {
        return (messages == null) 
            || (messages.getErrorMessageCount() + 
                messages.getInformationalMessageCount() == 0);
    }

    /**
     * Logs messages when the response has an error message.
     * @param response The response to chek for errors.
     * @return Returns true if the response has an error message; Otherwise 
     * it returns false.
     */
    protected boolean logMessagesOnError(BaseServiceResponse response) {
        Precondition.assertNotNull("response", response);
        if (!hasErrorMessage(response)) {
            return false;
        }
        logMessages(response);
        return true;
    }

    protected void logException(String operation, Exception e) {
        if (operation == null) { 
            operation = "<unknown operation>";
        }
        Log log = getLog();
        log.error(operation + " failed", e);
    }

    /**
     * Adds a given message to a service response 
     * @param response The response to add the messages to
     * @param message The message to add
     */
    protected void addMessage(BaseServiceResponse response, Message message) {
        Precondition.assertNotNull("response", response);
        response.getMessages().addMessage(message);
    }

    /**
     * Adds an error message with a given key  to a given response
     * @param response The response to add the message to.
     * @param key The key of the message.
     */
    protected void addError(BaseServiceResponse response, String key) { 
        Precondition.assertNotNull("response", response);
        response.addError(key);
    }

    /**
     * Adds an error message with a given key  to a given response
     * @param response The response to add the message to.
     * @param key The key of the message.
     * @param inserts The inserts of the message.
     */
    protected void addError(BaseServiceResponse response, String key,
        Object[] inserts) 
    { 
        Precondition.assertNotNull("response", response);
        response.addError(key, inserts);
    }

    /**
     * Adds an error message with a given key  to a given response
     * @param response The response to add the message to.
     * @param key The key of the message.
     * @param properties The properties the message applies to
     * @param inserts The inserts of the message.
     */
    protected void addError(BaseServiceResponse response, String key, 
        String[] properties, Object[] inserts) 
    { 
        Precondition.assertNotNull("response", response);
        response.addError(key, properties, inserts);
    }
    

    
    /**
     * Adds an warning message with a given key  to a given response
     * @param response The response to add the message to.
     * @param key The key of the message.
     */
    protected void addWarning(BaseServiceResponse response, String key) { 
        Precondition.assertNotNull("response", response);
        response.addWarning(key);
    }

    /**
     * Adds an warning message with a given key  to a given response
     * @param response The response to add the message to.
     * @param key The key of the message.
     * @param inserts The inserts of the message.
     */
    protected void addWarning(BaseServiceResponse response, String key,
        Object[] inserts) 
    { 
        Precondition.assertNotNull("response", response);
        response.addWarning(key, inserts);
    }

    /**
     * Adds an warning message with a given key  to a given response
     * @param response The response to add the message to.
     * @param key The key of the message.
     * @param properties The properties the message applies to
     * @param inserts The inserts of the message.
     */
    protected void addWarning(BaseServiceResponse response, String key, 
        String[] properties, Object[] inserts) 
    { 
        Precondition.assertNotNull("response", response);
        response.addWarning(key, properties, inserts);
    }
    
    
    /**
     * Adds an informational message with a given key to a given response
     * @param response The response to add the message to.
     * @param key The key of the message.
     */
    protected void addInfo(BaseServiceResponse response, String key) { 
        Precondition.assertNotNull("response", response);
        response.addInfo(key, null);
    }
    
    /**
     * Adds an informational message with a given key  to a given response
     * @param response The response to add the message to.
     * @param key The key of the message.
     * @param inserts The inserts of the message.
     */
    protected void addInfo(BaseServiceResponse response, String key,
        Object[] inserts) 
    { 
        Precondition.assertNotNull("response", response);
        response.addInfo(key, inserts);
    }

    /**
     * Adds an informational message with a given key  to a given response
     * @param response The response to add the message to.
     * @param key The key of the message.
     * @param inserts The inserts of the message.
     */
    protected void addInfo(BaseServiceResponse response, String key,
        String[] properties, Object[] inserts) 
    { 
        Precondition.assertNotNull("response", response);
        response.addInfo(key, properties, inserts);
    }

    protected boolean convertMessages(BaseServiceResponse response, 
        String[] keys, Severity fromSeverity, Severity toSeverity) 
    { 
        Precondition.assertNotNull("response", response);
        boolean converted = false;
        if (keys != null) {
            for (String key: keys) {
                converted |= convertMessage(response, key, fromSeverity, 
                    toSeverity);
            }
        }
        return converted;
    }

    protected boolean convertMessage(BaseServiceResponse response, 
        String key, Severity fromSeverity, Severity toSeverity) 
    { 
        Precondition.assertNotNull("response", response);
        return BaseServiceResponse.convertMessage(response, key,
            fromSeverity, key, toSeverity);
    }

    protected boolean convertErrorsToWarnings(BaseServiceResponse response, 
        String[] key) 
    {
        return convertMessages(response, key, Severity.ERROR, 
            Severity.WARNING);
    }
    
    protected boolean convertErrorToWarning(BaseServiceResponse response, 
        String key) 
    {
        return convertMessage(response, key, Severity.ERROR, 
            Severity.WARNING);
    }
    

}
