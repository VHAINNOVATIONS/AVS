package gov.va.med.lom.foundation.validate;

import gov.va.med.lom.foundation.service.response.messages.DefaultMessage;
import gov.va.med.lom.foundation.service.response.messages.DefaultMessages;
import gov.va.med.lom.foundation.service.response.messages.Message;
import gov.va.med.lom.foundation.service.response.messages.Messages;
import gov.va.med.lom.foundation.service.response.messages.Severity;

public abstract class SinglePropertyValidator implements Validator {

	public final Messages validate(Object value, String[] properties, Object[] inserts) {
		Messages messages = new DefaultMessages();
		if (!checkValidity(value)) {
			Message message = new DefaultMessage();
			message.setSeverity(Severity.ERROR);
			message.setKey(getMessageKey());
			message.addProperty(properties[0]);
			message.addInsert(inserts[0]);
			message.addInsert(value);
			messages.addMessage(message);
		}
		return messages;
	}
	
	private boolean checkValidity(Object value) {
		if (value == null) {
			return true;
		}
		
		try {
			return isValid(value);
		} catch (ClassCastException ex) {
			return false;
		}
	}

	protected abstract boolean isValid(Object value);

	protected abstract String getMessageKey();

}
