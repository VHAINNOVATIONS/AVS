package gov.va.med.lom.foundation.service;

import gov.va.med.lom.foundation.service.response.ServiceResponse;

/**
 * Provides a generic service to invoke asynchronous services.  
 */
public interface AsynchronousServiceDelegate {
	
	/**
	 * Asynchronously invokes the service described in a given 
     * {@link ServiceOperationDescriptor}.
	 * @param serviceDescritor Describes the service method to execute
	 * @return A response containing any feedback related to making the 
     * call asynchronously. The response does not contain a response from 
     * the service. 
	 */
	ServiceResponse<Void> invoke(
        ServiceOperationDescriptor serviceOperationDescriptor);
    
    
}
