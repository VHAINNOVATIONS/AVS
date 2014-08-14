package gov.va.med.lom.foundation.service.response.messages;

import java.text.MessageFormat;
import java.util.Collection;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Utility class containing static methods which make it easier to deal with
 * messages.
 */
public final class MessageUtils {

	private static final String BUNDLE_NAME = "messages";
	private static final String SUMMARY_SUFFIX = ".summary";
	private static final String DETAIL_SUFFIX = ".detail";
	private static Log LOG = LogFactory.getLog(MessageUtils.class);

	private static final ResourceBundleResolver RESOLVER;

	static {
		RESOLVER = new ResourceBundleResolver(BUNDLE_NAME);
	}

	/** Utility class - does not expose a constructor. */
	private MessageUtils() {
	}

	/**
	 * Create a message with a specified key, severity 'Error', and the
	 * specified properties and inserts.
	 * 
	 * @param key
	 *            The key for the message - must be non-null/non-empty
	 * @param properties
	 *            Properties with which the message is associated - may be
	 *            null/empty
	 * @param inserts
	 *            Inserts for the message - may be null/empty
	 * @return The messages created from the parameters given, with a severity
	 *         of 'Error'
	 * @throws IllegalArgumentException
	 *             if the key is null or blank
	 */
	public static Message createErrorMessage(String key, String[] properties, 
        Object[] inserts) 
    {

		return createMessage(key, Severity.ERROR,  properties, inserts);
	}

    /**
     * Create a message with a specified key, severity 'Warning', 
     * and the specified properties and inserts.
     * 
     * @param key The key for the message - must be non-null/non-empty
     * @param properties Properties with which the message is associated - 
     *     may be null/empty
     * @param inserts Inserts for the message - may be null/empty
     * @return The messages created from the parameters given, with a severity
     *     of 'Warning'
     * @throws IllegalArgumentException if the key is null or blank
     */
    public static Message createWarningMessage(String key, String[] properties, 
        Object[] inserts) 
    {
        return createMessage(key, Severity.WARNING, properties, 
            inserts);
    }

	/**
	 * Create a message with a specified key, severity 'Informational', and the
	 * specified properties and inserts.
	 * 
	 * @param key
	 *            The key for the message - must be non-null/non-empty
	 * @param properties
	 *            Properties with which the message is associated - may be
	 *            null/empty
	 * @param inserts
	 *            Inserts for the message - may be null/empty
	 * @return The messages created from the parameters given, with a severity
	 *         of 'Informational'
	 * @throws IllegalArgumentException
	 *             if the key is null or blank
	 */
	public static Message createInformationalMessage(String key, 
        String[] properties, Object[] inserts) 
    {

		return createMessage(key, Severity.INFORMATIONAL, properties, inserts);
	}

	/**
	 * Generically create any type of message.
	 * 
	 * @param key
	 *            The key for the message - must be non-null/non-empty
	 * @param severity
	 *            The severity for the new message - must be non-null/non-empty
	 * @param properties
	 *            Properties with which the message is associated - may be
	 *            null/empty
	 * @param inserts
	 *            Inserts for the message - may be null/empty
	 * @return The messages created from the parameters given, with a severity
	 *         of 'Informational'
	 * @throws IllegalArgumentException
	 *             if the key is null or blank
	 */
	public static Message createMessage(String key, Severity severity, 
        String[] properties, Object[] inserts) 
    {
		if (StringUtils.isBlank(key) || severity == null) {
			throw new IllegalArgumentException(
                "Key and severity must not be null");
		}

		Message message = MessageFactory.createMessage();
		message.setKey(key);
		message.setSeverity(severity);

		if (inserts == null) {
			inserts = new Object[] {};
		}
		for (int i = 0; i < inserts.length; i++) {
			message.addInsert(inserts[i]);
		}

		if (properties == null) {
			properties = new String[] {};
		}
		for (int i = 0; i < properties.length; i++) {
			message.addProperty(properties[i]);
		}

		return message;
	}

	/**
	 * Get the summary message for the default locale
	 * 
	 * @param key
	 * @param inserts
	 * @param clazz
	 * @return
	 */
	public static String getSummaryMessage(String key, Collection inserts, 
        Class clazz) 
    {
		return getSummaryMessage(key, inserts, clazz, Locale.getDefault());
	}

	/**
	 * Get the summary message for the specified key, class, inserts and locale
	 * 
	 * @param key
	 * @param inserts
	 * @param clazz
	 * @param locale
	 * @return
	 */
	public static String getSummaryMessage(String key, Collection inserts, 
        Class clazz, Locale locale) 
    {
		return formatMessage(key + SUMMARY_SUFFIX, inserts, clazz, locale);
	}

	public static String getDetailMessage(String key, Collection inserts, 
        Class clazz) 
    {
		return getDetailMessage(key, inserts, clazz, Locale.getDefault());
	}

	public static String getDetailMessage(String key, Collection inserts, 
        Class clazz, Locale locale) 
    {
		return formatMessage(key + DETAIL_SUFFIX, inserts, clazz, locale);
	}

    /**
     * Determines if a given message is an error message
     * @param message The message to check
     * @return True if the message is not null and has error severity
     * @see Severity
     */
    public static boolean isError(Message message) {
        return (message != null) && (Severity.ERROR == message.getSeverity());
    }
    
    /**
     * Determines if a given message is a warning message
     * @param message The message to check
     * @return True if the message is not null and has warning severity
     * @see Severity
     */
    public static boolean isWarning(Message message) {
        return (message != null) && (Severity.WARNING == message.getSeverity());
    }
    
    /**
     * Determines if a given message is an information message
     * @param message The message to check
     * @return True if the message is not null and has informational severity
     * @see Severity
     */
    public static boolean isInformational(Message message) {
        return (message != null) && (Severity.INFORMATIONAL == message.getSeverity());
    }

    private static String formatMessage(String key, Collection inserts, 
        Class clazz, Locale locale) 
    {
		ResourceBundle bundle = null;
		String message = null;
		try {
			bundle = RESOLVER.getResourceBundle(clazz, locale, key);
			message = bundle.getString(key);
		} catch (MissingResourceException e) {
			LOG.error("Error looking up key: " + key + " for class: " + 
                clazz.getName() + "\n\tthis.classloader: " + 
                MessageUtils.class.getClassLoader() + "; class.classloader " + 
                clazz.getClassLoader(), e);
			throw e;
		}
		Object[] values = (inserts == null) ? new Object[] {} : 
            inserts.toArray();
		message = escape(message);
		return MessageFormat.format(message, values);
	}

	private static String escape(String string) {

		if ((string == null) || (string.indexOf('\'') < 0))
			return (string);

		int n = string.length();
		StringBuffer sb = new StringBuffer(n);

		for (int i = 0; i < n; i++) {
			char ch = string.charAt(i);
			if (ch == '\'')
				sb.append('\'');
			sb.append(ch);
		}
		return (sb.toString());
	}

}
