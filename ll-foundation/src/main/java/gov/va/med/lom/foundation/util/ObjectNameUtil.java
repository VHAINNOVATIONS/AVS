/**
 * 
 */
package gov.va.med.lom.foundation.util;

import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Utility to manipulate {@link javax.management.ObjectName}s.
 * @author Rob Proper
 */
public final class ObjectNameUtil {
	
	private static final Log LOG = LogFactory.getLog(ObjectNameUtil.class);

	/**
	 * Creates an {@link ObjectName} instance for the given string.
	 * @param name The string to use for the ObjectName.
	 * @param defaultName The alternative string to use for the ObjectName,
	 * when problem occured with creation of the ObjectName instance with the 
	 * given name. 
	 * @return The ObjectName instance for the given name or defaultName (on 
	 * problems).
	 */
	public static final ObjectName createBeanName(String name, 
		String defaultName) 
	{
		Precondition.assertNotBlank("name", name);
		Precondition.assertNotBlank("defaultName", defaultName);
		ObjectName beanObjectName = null;
		try {
			beanObjectName = new ObjectName(name);
		} catch (MalformedObjectNameException e) {
			LOG.error("Invalid MBean name '" + name + "'", e);
		} catch (NullPointerException e) {
			LOG.fatal("Unable to create MBeanname", e);
		}
		return (beanObjectName != null) ? beanObjectName 
			: createBeanName(defaultName);
	}

	/**
	 * Creates an {@link ObjectName} instance for the given string.
	 * @param name The string to use for the ObjectName.
	 * @return The ObjectName instance for the given name or null (on 
	 * problems).
	 */
	public static final ObjectName createBeanName(String name) {
		Precondition.assertNotBlank("name", name);
		ObjectName beanObjectName = null;
		try {
			beanObjectName = new ObjectName(name);
		} catch (MalformedObjectNameException e) {
			LOG.error("Invalid ObjectName name '" + name + "'", e);
		} catch (NullPointerException e) {
			LOG.fatal("Unable to create ObjectName '" + name + "'", e);
		}
		return beanObjectName;
	}

}
