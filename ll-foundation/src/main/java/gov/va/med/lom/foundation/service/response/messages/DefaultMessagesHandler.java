package gov.va.med.lom.foundation.service.response.messages;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang.StringUtils;


/**
 * Default message handler implementation - handles messages 
 */
public class DefaultMessagesHandler implements MessagesHandler {

	/**
	 * Handle a messages object by making the messages available in 
     * the request.
	 * This will format any messages and make them available (as lists) 
     * as attributes keyed by 
     * <code>Messages.REQ_ATTR_ERROR_MSGS</code>,
     * <code>Messages.REQ_ATTR_WARN_MSGS</code> and 
     * <code>Messages.REQ_ATTR_INFO_MSGS</code>.
	 */
	public void handleMessages(Messages messages, Class clazz) 
    {
		if (messages.hasErrorMessages()) {
            handleMessages(messages.getErrorMessages(), clazz, 
                Messages.REQ_ATTR_ERROR_MSGS);
		}

        if (messages.hasWarningMessages()) {
            handleMessages(messages.getWarningMessages(), clazz, 
                Messages.REQ_ATTR_WARN_MSGS);
        }

		if (messages.hasInformationalMessages()) {
            handleMessages(messages.getInformationalMessages(), clazz, 
                Messages.REQ_ATTR_INFO_MSGS);
		}
	}

    private void handleMessages(Collection<Message> messages, Class clazz, 
        String name)
    {
        List<String> formattedMessages = new ArrayList<String>();
        if (messages != null) {
            for (Message message: messages) {
                String formattedMessage = MessageUtils.getSummaryMessage(
                    message.getKey(), message.getInserts(), clazz);
                if (!StringUtils.isBlank(formattedMessage)) {
                    formattedMessages.add(formattedMessage);
                }
            }
        }   
       
    }
}
