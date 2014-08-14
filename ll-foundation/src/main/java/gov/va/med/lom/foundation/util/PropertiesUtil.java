/**
 * 
 */
package gov.va.med.lom.foundation.util;

import java.util.Iterator;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Null-safe utility for extracting property values from 
 * {@link java.util.Properties} instances. 
 * @author Rob Proper
 */
public final class PropertiesUtil {
	
	private static final Log LOG = LogFactory.getLog(PropertiesUtil.class);

	public static final String RELOAD_SYSTEM_PROPERTY_NAME = 
		"properties.reload";
	
	public static boolean getReloadProperties() {
	    String property = System.getProperty(RELOAD_SYSTEM_PROPERTY_NAME);
	    boolean value = Boolean.parseBoolean(property);
	    if (LOG.isInfoEnabled()) {
	        LOG.info(((value) ? "Reload properties" : "Load properties once")
	            + ". Property '" + RELOAD_SYSTEM_PROPERTY_NAME + "'=["
	            + property + "].");
	    }
	    return value;
	}
	
	private PropertiesUtil() {
		// Hide constructor because utility class
		super();
	}

	/**
	 * Extract a named int property from the given properties instance.
	 * @param resourceName The name of the resource the properties where loaded
	 * from.
	 * @param properties The properties instance to inspect.
	 * @param name The property name
	 * @param defaultValue The default value, to use if no value found.
	 * @return The value if found; If not found, the properties instance is 
	 * null or the value was not a valid int value, the given default value.
	 */
	public static int getIntValue(String resourceName, Properties properties, 
		String name, int defaultValue)
	{
		int value = defaultValue;
		if (properties != null) {
			String propertyValue = properties.getProperty(name);
			if (!StringUtils.isBlank(propertyValue)) {
				try {
					value = Integer.parseInt(propertyValue);
				} catch (NumberFormatException e) {
                    logFormatWarning(name, propertyValue, resourceName, 
                        Integer.toString(defaultValue), e);
				}
			} else {
                logNoValueInfo(name, resourceName, Integer.toString(
                    defaultValue));
            }
		} else {
			logMissingProperties(resourceName, name, Integer.toString(
				defaultValue));
		}
		return value;
	}

    
	/**
	 * Extract a named boolean property from the given properties instance.
	 * @param resourceName The name of the resource the properties where loaded
	 * from.
	 * @param properties The properties instance to inspect.
	 * @param name The property name
	 * @param defaultValue The default value, to use if no value found.
	 * @return The value if found; If not found, the properties instance is 
	 * null or the value was not a valid boolean value, the given default value.
	 */
	public static boolean getBoolValue(String resourceName, 
		Properties properties, String name, boolean defaultValue)
	{
		boolean value = defaultValue;
		if (properties != null) {
			String propertyValue = properties.getProperty(name);
			if (!StringUtils.isBlank(propertyValue)) {
				try {
					value = Boolean.parseBoolean(propertyValue);
				} catch (NumberFormatException e) {
					LOG.warn("Format error in property '" + name + "' value '" 
						+ propertyValue + "' from '" + resourceName 
						+ "'. Using default value '" + defaultValue + "'", e);
				}
			}
		} else {
			logMissingProperties(resourceName, name, Boolean.toString(
				defaultValue));
		}
		return value;
	}    
    
	/**
	 * Extract a named String property from the given properties instance.
	 * @param resourceName The name of the resource the properties where loaded
	 * from.
	 * @param properties The properties instance to inspect.
	 * @param name The property name
	 * @param defaultValue The default value, to use if no value found.
	 * @return The value if found; If not found or the properties instance is 
	 * null, the given default value.
	 */
	public static String getValue(String resourceName, Properties properties, 
		String name, String defaultValue)
	{
		String value = defaultValue;
		if (properties != null) {
			value = properties.getProperty(name, defaultValue);
            if (StringUtils.isBlank(value)) {
                logNoValueInfo(name, resourceName, defaultValue);
            }
		} else {
			logMissingProperties(resourceName, name, defaultValue);
		}
		return value;
	}

	/**
	 * Extract a named String property from the given properties instance.
	 * @param resourceName The name of the resource the properties where loaded
	 * from.
	 * @param properties The properties instance to inspect.
	 * @param name The property name
	 * @param defaultValue The default value, to use if no value found.
	 * @return The value if found; If not found or the properties instance is 
	 * null, the given default value.
	 */
	public static String getValueIgnoreCase(String resourceName, 
		Properties properties, String name, String defaultValue)
	{
		Precondition.assertNotBlank("name", name);
		String value = defaultValue;
		if ((properties != null)){
			boolean found = false;
			for (Iterator i = properties.keySet().iterator(); 
			     (!found) && i.hasNext();) 
			{
				String key = (String) i.next();
				found = (name.equalsIgnoreCase(key));
				if (found) {
					value = getValue(resourceName, properties, name, 
						defaultValue);
				}
			}
		} else {
			logMissingProperties(resourceName, name, defaultValue);
		}
		return value;
	}

    private static void logFormatWarning(String name, String propertyValue, 
        String resourceName, String defaultValue, Exception e) 
    {
        LOG.warn("Format error in property '" + name + "' value '" 
            + propertyValue + "' from '" + resourceName 
            + "'. Using default value '" + defaultValue + "'", e);
    }

    private static void logNoValueInfo(String name, String resourceName, 
        String defaultValue) 
    {
        LOG.info("No value found for property '" + name + "' from '" 
            + resourceName + "'. Using default value '" + defaultValue 
            + "'");
    }

	private static void logMissingProperties(String resourceName, String name, 
		String defaultValue) 
	{
		LOG.info("Properties '" + resourceName + "' not found ."
			+ " Using default value '" + defaultValue + "' for property '" 
			+ name + "'");
	}

	
}
