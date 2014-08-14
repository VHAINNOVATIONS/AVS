package gov.va.med.lom.foundation.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ConfigurationManager {
	
	private static Log logger = LogFactory.getLog( ConfigurationManager.class );

	private static final String CONFIG_LOCATION_PROPERTY = "config.location";
	
	public static Properties getConfiguration(String propertyFileName) {
		String location = System.getProperty( CONFIG_LOCATION_PROPERTY );
		
		if( StringUtils.isBlank( location ) ) {
			IllegalStateException ex =  new IllegalStateException( "Invalid configuration.  Missing system property: " + CONFIG_LOCATION_PROPERTY );
			logger.fatal( "gov.va.med.mhv.util.ConfigurationManager", ex );
			logger.fatal("CJM - 02062008 - PUT BACK IN");
			//TODO PUT THIS BACK IN ONCE UNIT TESTS ARE REFACTORED TO HANDLE THE FAIL FAST
			//throw ex;
		}
		
		InputStream is = null;
		try {
			is = new FileInputStream( location + propertyFileName );
			Properties properties = new Properties();
			properties.load( is );
			return properties;
		}
		catch(FileNotFoundException fnfe) {
			logger.fatal( "gov.va.med.mhv.util.ConfigurationManager", fnfe );
			logger.fatal("CJM - 02062008 - PUT BACK IN");
			//TODO PUT THIS BACK IN ONCE UNIT TESTS ARE REFACTORED TO HANDLE THE FAIL FAST
			//throw new IllegalStateException( "Unable to locate properties file: " + location + propertyFileName );
		}
		catch(IOException ioe) { 
			logger.fatal( "gov.va.med.mhv.util.ConfigurationManager", ioe );
			logger.fatal("CJM - 02062008 - PUT BACK IN");
			//TODO PUT THIS BACK IN ONCE UNIT TESTS ARE REFACTORED TO HANDLE THE FAIL FAST
			//throw new IllegalStateException( "Unable to open properties file: " + location + propertyFileName ); 
		}
		finally {
			try {
				if( is != null)
					is.close();
			}
			catch(IOException ignore) {
			}
		}
		return null;
	}	
}
