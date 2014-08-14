package gov.va.med.lom.foundation.validate;

import gov.va.med.lom.foundation.service.response.messages.Messages;

/**
 * Generic validator interface to be implemented by any class providing
 * validation functionality.  This interface defines the validate method,
 * which accepts a value to validate along with information to be used
 * in creating validation error(s).
 */
public interface Validator {
	
	/**
	 * Validate a value, producing the appropriate error(s) if the
	 * value is invalid.  It is up to subclasses to specify the
	 * content and ordering of the property and insert arrays.
	 *
	 * @param value The value to be validated
	 * @param properties List of properties to be used in creating errors
	 * @param inserts List of inserts to be used in creating errors
	 * @return A (non-null) messages object
	 */
	Messages validate(Object value, String [] properties, Object [] inserts);
	
}
