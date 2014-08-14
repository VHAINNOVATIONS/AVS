package gov.va.med.lom.foundation.service;

import gov.va.med.lom.foundation.util.AssertionUtils;
import gov.va.med.lom.foundation.util.Describeable;
import gov.va.med.lom.foundation.util.DescriptionBuilder;
import gov.va.med.lom.foundation.util.Precondition;

import java.io.Serializable;



/**
 * Contains the information needed to invoke a service asynchronously.
 */
public class ServiceOperationDescriptor implements Serializable, Describeable {

    /**
     * 
     */
    private static final long serialVersionUID = -2270771484344095457L;

    public static void assertValidity(ServiceOperationDescriptor descriptor) {
        Precondition.assertNotNull("serviceDescriptor", descriptor);
        Precondition.assertNotBlank("serviceDescriptor.serviceName", 
            descriptor.getServiceName());
        Precondition.assertNotBlank("serviceDescriptor.operationName", 
            descriptor.getOperationName());
        Object[] parameters = descriptor.getParameters(); 
        Precondition.assertNotNull("serviceDescriptor.parameter", 
            (Object) parameters);
        Class[] parameterTypes = descriptor.getParameterTypes(); 
        Precondition.assertNotNull("serviceDescriptor.parameterTypes", 
            parameterTypes);
        if (parameters.length != parameterTypes.length) {
            Precondition.fail("The number of serviceDescriptor.parameters " + 
               "does not equal the number of serviceDescriptor.parameterTypes");
        }
        for (int i = 0; i < descriptor.getParameters().length; i++) {
            if ((parameters[i] != null) && !AssertionUtils.isAssignableFrom(
                    parameters[i], parameterTypes[i]))
            {
                Precondition.fail("Expected type + " + parameterTypes[i] + 
                    " for parameter[" + i + "] but got " + parameters[i].
                    getClass());
            }
        }
    }
    
    private String serviceName = null;
	private String operationName = null;
	private Object[] parameters = null;
	private Class[] parameterTypes = null;

    /**
     * @return Returns the serviceName.
     */
    public String getServiceName() {
        return serviceName;
    }
    
    /**
     * @param serviceName The serviceName to set.
     */
    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

	/**
	 * @return Returns the operationName.
	 */
	public String getOperationName() {
		return operationName;
	}
	
	/**
	 * @param operationName The operationName to set.
	 */
	public void setOperationName(String operationName) {
		this.operationName = operationName;
	}
	
	/**
	 * @return Returns the paramList.
	 */
	public Object[] getParameters() {
		return parameters;
	}

	/**
	 * @param parameters The paramList to set.
	 */
	public void setParameters(Object[] parameters) {
		this.parameters = parameters;
	}

	/**
	 * @return Returns the classList.
	 */
	public Class[] getParameterTypes() {
		return parameterTypes;
	}

	/**
	 * @param parameterTypes The classList to set.
	 */
	public void setParameterTypes(Class[] parameterTypes) {
		this.parameterTypes = parameterTypes;
	}

    public void describe(DescriptionBuilder builder) {
        Precondition.assertNotNull("builder", builder);
        builder.header(this);
        builder.openPropertyList();
        builder.appendProperty("serviceName", serviceName);
        builder.appendProperty("operationName", operationName);
        builder.appendProperty("parameters", parameters);
        builder.appendProperty("parameterTypes", parameterTypes);
        builder.closePropertyList();
    }	
    
}
