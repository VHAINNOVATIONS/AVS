package gov.va.med.lom.kaajee.jboss.security.auth;

import java.util.ArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * This object maintains the list of security roles expected by the application, as a singleton. The roles
 * are parsed during initialization from an InputStream whose format should correspond to the contents of the
 * web.xml file, containing &lt;security-role&gt; elements with &lt;role-name&gt; sub-elements.
 * @author VHIT Security and Other Common Services (S&OCS)
 * @version 1.1.0.007
 */
class ApplicationRoleListVOSingleton {

	private static ApplicationRoleListVOSingleton me;
	private static Log log = LogFactory.getLog(ApplicationRoleListVOSingleton.class);
	private ArrayList<String> roleList;

	private static Object syncObj = new Object();
	
	/**
	 * internal constructor
	 * @param inputWebXmlStream
	 */
	private ApplicationRoleListVOSingleton(Document configDoc) {
		roleList = getRoleList(configDoc);
	}

	/**
	 * Efficient getInstance method -- doesn't require passing an Document object, only returns instance
	 * if was already previously initialized.
	 * @return the ApplicationRoleListVOSingleton object, or null if it hasn't been created yet. If null
	 * is returned, call the other version of getInstance that requires an InputStream to be created/passed
	 * as a parameter.
	 * @throws IllegalStateException if singleton has not been initialized.
	 */
	static ApplicationRoleListVOSingleton getInstance() throws IllegalStateException {
		if (log.isDebugEnabled()) {
			log.debug("In getInstance().");
		}
		synchronized (syncObj) {
			if (me == null) {
				String exceptionString = "ApplicationRoleListVOSingleton not initialized.";
				if (log.isErrorEnabled()) {
					log.error(exceptionString);
				}
				throw new IllegalStateException(exceptionString);
			}
			return me;
		}
	}

	/**
	 * This method creates the singleton if it doesn't already exist. 
	 * @param inputWebXmlStream an InputStream containing the security-role XML elements that define the
	 * J2EE security roles for this application.
	 * @throws IllegalStateException if singleton has already been initialized
	 */
	static void createInstance(Document configDoc) throws IllegalStateException {

		if (log.isDebugEnabled()) {
			log.debug("In createInstance().");
		}
		synchronized (syncObj) {
			if (me == null) {
				if (log.isDebugEnabled()) {
					log.debug("Creating new singleton instance.");
				}
				me = new ApplicationRoleListVOSingleton(configDoc);
			} else {
			    /*
				String exceptionString = "ApplicationRoleListVOSingleton already initialized.";
				if (log.isErrorEnabled()) {
					log.error(exceptionString);
				}
				throw new IllegalStateException(exceptionString);
				*/
			    
			    /* no op */
			}
		}

	}

	/**
	 * Parse security-role and role-name elements from the input stream XML document.
	 * @param inputWebXmlStream
	 * @return an array of the role-names found
	 */
	private ArrayList<String> getRoleList(Document configDoc) {

		ArrayList<String> roleList = new ArrayList<String>();
		NodeList groupsNodeList = configDoc.getElementsByTagName("groups");
		for (int i = 0; i < groupsNodeList.getLength(); i++) {
			NodeList groupNodeList = groupsNodeList.item(i).getChildNodes();
			for (int j = 0; j < groupNodeList.getLength(); j++) {
				if ("group".equals(groupNodeList.item(j).getNodeName())) {
					Node groupNode = groupNodeList.item(j);
					NamedNodeMap attributes = groupNode.getAttributes();
					for (int k = 0; k < attributes.getLength(); k++) {
						Attr attr = (Attr) attributes.item(k);
						if ("name".equals(attr.getName())) {
							if (log.isDebugEnabled()) {
								log.debug("retrieved role from kaajee configuration file: " + attr.getValue());
							}
							roleList.add(attr.getValue());
							break;
						}
					}
				}
			}
		}
		return roleList;
	}

	/**
	 * returns the list of roles maintained by this object
	 * @return the list of roles maintained by this object
	 */
	ArrayList<String> getRoleList() {

		//Approach #1 should be safe -- returns a "deep", not shallow, copy of this object.
		/* ArrayList returnVal = new ArrayList(roleList); 
		   return returnVal; */

		//Approach #2 for efficiency we *prefer* a shallow, not deep, copy of this object? Since this is
		// a package-private object anyway.
		return roleList;

	}
}
