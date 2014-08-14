package gov.va.med.lom.foundation.service.response.messages;


/**
 * Interface definition for message handlers.  Message handlers can be used to
 * handle messages, that is, to take a set of messages and perform whatever
 * work is needed to display them to the user.  This might involve passing them
 * off to the Notifications framework or them into a Struts ActionErrors
 * instance.
 */
public interface MessagesHandler {
	
	/**
	 * Handle a <code>Messages</code>, performing whatever processing is needeed
	 * in order to handle/display messages to the user.
	 *
	 * @param messages The messages to handle
	 * @param request The current HTTP request
	 */
	void handleMessages(Messages messages, Class clazz);
	
}
