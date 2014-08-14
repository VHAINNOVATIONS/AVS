package gov.va.med.lom.foundation.service.response;


import java.util.ArrayList;
import java.util.Collection;

/**
 * CollectionServiceResponse meant to marshall collections of information
 * in addition to messages from the service tier to the presentation tier 
 * 
 * @author Chris Morrisette - Sep 11, 2007
 *
 */
public class CollectionServiceResponse<T> extends BaseServiceResponse {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6841216984798286405L;
	Collection<T> collection = new ArrayList<T>();
	
	/**
	 * Add a instance of <T> to the collection being marshalled
	 * @param payload instance to add to the collection
	 */
	public void addPayload(T payload) {
		getCollection().add(payload);
	}
	
	/**
	 * Add a collection of type <T> to the collection being marshalled
	 * @param payloads collection to add to the marshalled data
	 */
	public void addPayloads(Collection<T> payloads) {
		getCollection().addAll(payloads);
	}

	/**
	 * Get the collection being marshalled
	 * @return
	 */
	public Collection<T> getCollection() {
		return collection;
	}

	/**
	 * Set the collection being marshalled
	 * @param collection collection to set onto the response
	 */
	public void setCollection(Collection<T> collection) {
		this.collection = collection;
	}

}
