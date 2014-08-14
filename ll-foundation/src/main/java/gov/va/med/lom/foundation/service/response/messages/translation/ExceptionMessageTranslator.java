package gov.va.med.lom.foundation.service.response.messages.translation;

import gov.va.med.lom.foundation.service.response.BaseServiceResponse;

/**
 * Interface for translating exceptions and adding the translation to the response
 * so it can be marshalled to the end user
 * @author cmorrisette
 *
 */
public interface ExceptionMessageTranslator {
	/**
	 * Attempt to translate the specific exception and then queue it onto the response object
	 * @param response - the response object to queue messages onto
	 * @param exception - the exception to attempt to interperet into something more user friendly
	 */
	void translateException(BaseServiceResponse response, Exception exception);
}
