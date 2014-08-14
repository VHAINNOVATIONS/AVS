package gov.va.med.lom.foundation.service.response.messages;

import java.io.Serializable;
import java.util.Collection;

/**
 * Message interface definition.  A message is an object that consists of
 * a key (used to somehow identify the message's text) and a severity 
 * (e.g. 'Error', 'Informational').  Optionally, messages be associated with
 * one or more properties, and may contain insert values used to dynamically
 * populate the message text identified by the message's key.
 */
public interface Message extends Serializable {

	/** Unique string used to create <code>Message</code> instances. */
	public static final String BEAN_NAME = "Message";

	/**
	 * Get the key for this message.
	 *
	 * @return The message's key - the value used to identify the text
	 *         for this message
	 */
	String getKey();

	/**
	 * Get the severity for this message.
	 *
	 * @return The <code>Severity</code> enumeration value for this message
	 * @see Severity
	 */
	Severity getSeverity();

	/**
	 * Get the collection of properties for this message.
	 *
	 * @return A non-modifiable, non-null collection of strings containing the
	 *         properties associated with this messages.  Might contain 0..n
	 *         properties
	 */
	Collection getProperties();

	/**
	 * Get the inserts for this message.
	 *
	 * @return A non-modifiable, non-null collection of objects representing
	 *         the inserts to be used in association with the message key when
	 *         constructing the full message text
	 */
	Collection getInserts();

	/**
	 * Update the value of the message's key.
	 *
	 * @param key The key value to be set
	 */
	void setKey(String key);

	/**
	 * Update the value of the message's severity.
	 *
	 * @param severity The severity (enumerated type) value
	 * @see Severity
	 */
	void setSeverity(Severity severity);

	/**
	 * Add a property name to the message.  This method can be used by objects
	 * that create messages to associate messages with one or properties of
	 * the raising object.  A message can only be associated with an single
	 * property once; multiple calls to <code>addProperty</code> with the same
	 * value should only add the property once.
	 *
	 * @param property The property name to be added to the message
	 */
	void addProperty(String property);

	/**
	 * Add an insert value to the messages.  Inserts will be used along with the
	 * message key to formulate the full message text.
	 *
	 * @param insert The insert value to be added
	 */
	void addInsert(Object insert);

}
