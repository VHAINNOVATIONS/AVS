package gov.va.med.lom.foundation.util;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * Class useful for loading resource bundles based on a Java class and
 * (optionally) a locale.  This class can be used when multiple locations on
 * the classpath may have a resource bundle with the same name, but namespaced
 * differently (e.g. com.foo.props.properties and com.bar.props.propeties).
 * This class will locate the appropriate resource bundle and cache the
 * locale-specific instance for future lookups.  
 */
public final class ResourceBundleResolver {

	private static final String PKG_DELIM = ".";
	
	private String bundleName;
	private Map<Locale, Map<String, ResourceBundle>> bundles;
	private Map<String, String> packagesToBundles;
	
	/**
	 * Create a new resolver for a given resource bundle name.
	 *
	 * @param bundleName The name of the resource bundle, without any namspace
	 *                   (e.g., foo).
	 */
	public ResourceBundleResolver(String bundleName) {
		super();
		
		this.bundleName = bundleName;
		bundles = new HashMap<Locale, Map<String, ResourceBundle>>();
		packagesToBundles = new HashMap<String, String>();
	}
	
	/**
	 * Locate a resource bundle for a given class and the default locale.
	 * Equivalent to calling <code>getResourceBundle(clazz, Locale.getDefault())
	 * </code>.
	 *
	 * @param clazz The class to be used in locating the resource bundle
	 * @return The resource bundle
	 * @see #getResourceBundle(Class, Locale)
	 */
	public ResourceBundle getResourceBundle(Class clazz) {
		return getResourceBundle(clazz, Locale.getDefault());
	}
	
	/**
	 * Locate a locale-specific resource bundle for a given class.  This will
	 * use the class's package to resolve the appropriate resource bundle.  So,
	 * for example, a class of com.foo.bar.FooBar would find a resource bundle
	 * named com.foo.bar.<<bundle name>>, com.foo.<<bundle name>>,
     *  com.<<bundle name>>,
	 * or simplyy <<bundle name>>.
	 *
	 * @param clazz The class to be used in locating the resource bundle
	 * @param locale The localte to be used in locating the resource bundle
	 * @return The resource bundle
	 */
	public ResourceBundle getResourceBundle(Class clazz, Locale locale) {
		return findBundle(clazz.getPackage().getName(), locale);
	}
	
	/**
	 * Locate a locale-specific resource bundle for a given name.
     * This assumes that the name represents a package-like string 
     * (e.g., com.example.Service).
	 * So, for example, a name of com.foo.bar.FooBar would find a resource 
     * bundle named com.example.Service.<<bundle name>>, 
     * com.example.<<bundle name>>, com.<<bundle name>>, or 
     * simply <<bundle name>>.
	 *
	 * @param name The name to be used in locating the resource bundle
	 * @param locale The localte to be used in locating the resource bundle
	 * @return The resource bundle
	 */
	public ResourceBundle getResourceBundle(String name, Locale locale) {
		return findBundle(name, locale);
	}
	
	/**
	 * Locate a resource bundle for a given name and the default locale.
	 * Equivalent to calling <code>getResourceBundle(name, Locale.getDefault())
	 * </code>.
	 *
	 * @param name The name to be used in locating the resource bundle
	 * @return The resource bundle
	 * @see #getResourceBundle(String, Locale)
	 */
	public ResourceBundle getResourceBundle(String name) {
		return getResourceBundle(name, Locale.getDefault());
	}
	
	private ResourceBundle findBundle(String namespace, Locale locale) {
		Map<String, ResourceBundle> localeSpecificMap = 
            (Map<String, ResourceBundle>) bundles.get(locale);
		if (localeSpecificMap == null) {
			localeSpecificMap = new HashMap<String, ResourceBundle>();
			bundles.put(locale, localeSpecificMap);
		}
		
		String name = namespace;
		int index = name.lastIndexOf(PKG_DELIM);
		while (index > 0) {
			if (packagesToBundles.containsKey(name)) {
				// We already have a bundle for this package (in some locale)
				String bundleKey = (String) packagesToBundles.get(name);
				// Speed up future lookups
				packagesToBundles.put(namespace, bundleKey);
				// Ensure we have a bundle for the current locale
				ResourceBundle bundle = (ResourceBundle) localeSpecificMap.get(
                    bundleKey);
				if (bundle == null) {
					String bundleToLoad = bundleKey + PKG_DELIM + bundleName;
					bundle = ResourceBundle.getBundle(bundleToLoad, locale);
					localeSpecificMap.put(bundleKey, bundle);
				}
				return bundle;
			} else {
				// Try to load the bundle for this package
				try {
					String bundleToLoad = name + PKG_DELIM + bundleName;
					ResourceBundle bundle = ResourceBundle.getBundle(
                        bundleToLoad, locale);
					localeSpecificMap.put(name, bundle);
					// Speed up future lookups
					packagesToBundles.put(namespace, name);
					return bundle;
				} catch (MissingResourceException ex) {
					name = name.substring(0, index);
					index = name.lastIndexOf(PKG_DELIM);
				}
			}
		}

		throw new MissingResourceException(
            "Unable to fund resource bundle for namespace " + namespace + 
            " and Locale " + locale, null, null);
	}
	
}
