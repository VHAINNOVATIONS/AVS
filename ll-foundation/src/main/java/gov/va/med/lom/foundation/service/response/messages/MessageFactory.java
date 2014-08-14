package gov.va.med.lom.foundation.service.response.messages;


/**
 * Factory for creating messages.  Currently only the DefaultMessage and 
 * DefaultMessages class is being returned
 * 
 * @author Chris Morrisette - Sep 11, 2007
 *
 */
public class MessageFactory {
	public static Message createMessage() {
		return new DefaultMessage();
	}
	public static Messages createMessages() {
		return new DefaultMessages();
	}
	
	public static MessagesHandler createMessagesHandler() {
		return new DefaultMessagesHandler();
	}
}
