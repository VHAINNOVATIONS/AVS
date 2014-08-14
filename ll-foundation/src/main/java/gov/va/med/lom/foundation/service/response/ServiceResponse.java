package gov.va.med.lom.foundation.service.response;


/**
 * Response object used to marshall a single domain object and messages from the service
 * tier to the presentation tier.
 * 
 * @author Chris Morrisette - Sep 11, 2007
 *
 */
public class ServiceResponse<T> extends BaseServiceResponse {
	/**
	 * 
	 */
	private static final long serialVersionUID = -340745061237488069L;
	private T payload;

	/**
	 * Get the payload marshalled within this response
	 * @return instance of class of type T
	 */
	public T getPayload() {
		return payload;
	}

	/**
	 * Set the payload marshalled within this response
	 * @param payload instance of type T set onto this response
	 */
	public void setPayload(T payload) {
		this.payload = payload;
	}

}
