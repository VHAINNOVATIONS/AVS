package gov.va.med.lom.foundation.service.response.messages;

/**
 * Core messsages
 * 
 * @author Chris Morrisette - Sep 27, 2007
 *
 */
public interface CoreMessages {

	static final String NULL_NOT_ALLOWED = "null.not.allowed";
	static final String MAX_VALUE_EXCEEDED = "value.must.be.lt.max";
	static final String MIN_VALUE_NOT_MET = "value.must.be.gt.min";
	static final String MAX_LENGTH_EXCEEDED = "value.must.be.shorter.than";
	static final String MIN_LENGTH_NOT_MET = "value.must.be.longer.than";
	static final String INVALID_SCALE = "invalid.scale";
	static final String INVALID_ENUM_INSTANCE = "invalid.enum.instance";
	static final String INVALID_NUMBER = "form.entry.invalid.number";
	static final String INVALID_DATE   = "form.entry.invalid.date";
	static final String INVALID_EMAIL = "invalid.email.address";

	static final String ENTITY_MAINTENANCE_SAVE_SUCCEEDED = 
        "entity.maintenance.save.succeeded";

    static final String UNKNOWN_ERROR = "unknown.exception.occurred";

}
