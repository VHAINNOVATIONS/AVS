package gov.va.med.lom.foundation.service.response.messages;

import java.io.Serializable;
import java.util.Collection;

/**
 * Container for messages.  Any object needing to hold on to <code>Message
 * </code> instances can use a <code>Messages</code> object to manage messages.
 * A <code>Messages</code> instance can provide messages based on their 
 * severities, or by the properties with which they are associated.  In 
 * addition, convenience methods are provided to answer whether the <code>
 * Messages</code> contains certain messages, and/or how many of such messages
 * there are.
 */
public interface Messages extends Serializable {

	public static final String REQ_ATTR_ERROR_MSGS = "WarningMessages";
    public static final String REQ_ATTR_WARN_MSGS = "WarnMessages";
    public static final String REQ_ATTR_INFO_MSGS = "InfoMessages";

	/**
	 * Add a message to this collection of messages.
	 * 
	 * @param message The message to add
	 * @throw IllegalArgumentException If the messages does not contain 
     *      a key and a severity
	 */
	void addMessage(Message message);
	
	/**
	 * Answer how many messages with a severity of 'Error' are present.
	 *
	 * @return The number of 'Error' messages
	 */
	int getErrorMessageCount();
	
	/**
	 * Answer how many messages with a severity of 'Error' are present that
	 * are associated with a given property name.
	 *
	 * @param property The name of the property
	 * @return The number of 'Error' messages associated with the given 
     * property
	 */
	int getErrorMessageCount(String property);
	
	/**
	 * Get all messages with a severity of 'Error' that are present
	 *
	 * @return A non-modifiable, non-null collection of 'Error' messages
	 */
	Collection<Message> getErrorMessages();
	
	/**
	 * Get all messages with a severity of 'Error' that are associated with a
	 * given property
	 *
	 * @param property The name of the property
	 * @return A non-modifiable, non-null collection of all 'Error' messages
	 *         associated with the given property
	 */
	Collection<Message> getErrorMessages(String property);
	

    /**
     * Answer how many messages with a severity of 'Warning' are present.
     *
     * @return The number of 'Warning' messages
     */
    int getWarningMessageCount();
    
    /**
     * Answer how many messages with a severity of 'Warning' are present 
     * that are associated with a given property name.
     *
     * @param property The name of the property
     * @return The number of 'Warning' messages associated with the 
     *     given property
     */
    int getWarningMessageCount(String property);
    
    /**
     * Get all messages with a severity of 'Warning' that are present
     *
     * @return A non-modifiable, non-null collection of 'Warning' messages
     */
    Collection<Message> getWarningMessages();
    
    /**
     * Get all messages with a severity of 'Warning' that are 
     * associated with a given property
     *
     * @param property The name of the property
     * @return A non-modifiable, non-null collection of all 'Warning' 
     *     messages associated with the given property
     */
    Collection<Message> getWarningMessages(String property);
    
    /**
	 * Answer how many messages with a severity of 'Informational' are 
     * present.
	 *
	 * @return The number of 'Informational' messages
	 */
	int getInformationalMessageCount();
	
	/**
	 * Answer how many messages with a severity of 'Informational' are 
     * present that are associated with a given property name.
	 *
	 * @param property The name of the property
	 * @return The number of 'Informational' messages associated with 
     *      the given property
	 */
	int getInformationalMessageCount(String property);
	
	/**
	 * Get all messages with a severity of 'Informational' that are present
	 *
	 * @return A non-modifiable, non-null collection of 'Informational' 
     * messages
	 */
	Collection<Message> getInformationalMessages();
	
	/**
	 * Get all messages with a severity of 'Informational' that are 
     * associated with a given property
	 *
	 * @param property The name of the property
	 * @return A non-modifiable, non-null collection of all 'Informational'
	 *    messages associated with the given property
	 */
	Collection<Message> getInformationalMessages(String property);
	
    /**
     * Answer whether any messages with a severity of 'Error' are present
     * 
     * @return True if any 'Error' messages are present, false otherwise
     */
    boolean hasErrorMessages();
    
    /** 
     * Answer whether any messages with a severity of 'Error' are 
     * present for a given property.
     *
     * @param property The property name
     * @return True if any 'Error' messages are present for the 
     *     given property, false otherwise
     */
    boolean hasErrorMessages(String property);
    
    
	/**
	 * Answer whether any messages with a severity of 'Warning' are present
	 * 
	 * @return True if any 'Warning' messages are present, false otherwise
	 */
	boolean hasWarningMessages();
	
	/** 
	 * Answer whether any messages with a severity of 'Warning' are 
     * present for a given property.
	 *
	 * @param property The property name
	 * @return True if any 'Warning' messages are present for the 
     *     given property, false otherwise
	 */
	boolean hasWarningMessages(String property);
	
    
	/**
	 * Answer whether any messages with a severity of 'Warning' are present
	 * 
	 * @return True if any 'Informational' messages are present, 
     *     false otherwise
	 */
	boolean hasInformationalMessages();
	
	/** 
	 * Answer whether any messages with a severity of 'Warning' are 
     * present for a given property.
	 *
	 * @param property The property name
	 * @return True if any 'Informational' messages are present for 
     *     the given property, false otherwise
	 */
	boolean hasInformationalMessages(String property);
	
	/**
	 * Add an entire list of messages to this message list 
	 * @param messages
	 */
	void addMessages(Messages messages);
	
}
