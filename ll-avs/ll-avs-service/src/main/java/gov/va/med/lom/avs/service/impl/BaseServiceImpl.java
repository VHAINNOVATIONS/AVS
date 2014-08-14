package gov.va.med.lom.avs.service.impl;

import gov.va.med.lom.foundation.service.response.BaseServiceResponse;
import gov.va.med.lom.foundation.service.response.messages.Message;
import gov.va.med.lom.foundation.service.response.messages.Messages;

import gov.va.med.lom.avs.service.BaseService;
import gov.va.med.lom.avs.service.impl.BaseServiceImpl;

import java.util.Iterator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * AVS Service implementation
 */
abstract public class BaseServiceImpl implements BaseService {	

	protected static final Log log = LogFactory.getLog(BaseServiceImpl.class);

	public boolean isAlive() {
		return true;
	}

	protected static void checkVistaExceptions(BaseServiceResponse response) throws RuntimeException {
		checkVistaExceptions(response, null);
	}

	protected static void checkVistaExceptions(BaseServiceResponse response, String prefix) throws RuntimeException {
		if (BaseServiceResponse.hasErrorMessage(response)) {
			StringBuffer sb = new StringBuffer();
			sb.append("ERROR: ");
			if (prefix != null) {
				sb.append(prefix);
			}
			sb.append(":\n");
			Messages messages = response.getMessages();
			Iterator<Message> it = messages.getErrorMessages().iterator();
			while (it.hasNext()) {
				Message msg = it.next();
				sb.append(msg.getKey()).append("\n");
			}
			log.error(sb.toString());
			throw new RuntimeException(sb.toString());
		}
	}

}
