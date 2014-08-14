package gov.va.med.lom.kaajee.jboss.security.auth;

import gov.va.med.authentication.kernel.KaajeeInstitutionResourceException;
import gov.va.med.authentication.kernel.VistaDivisionVO;
import gov.va.med.term.access.Institution;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

/**
 * For internal KAAJEE use only. Value object for otbaining / manipulating configuration values. 
 * <p>
 * This class is public and implemented as a javabean so that the KAAJEE login JSP pages can access these 
 * configuration values. 
 * @author VHIT Security and Other Common Services (S&OCS)
 * @version 1.1.0.007
 */
public class ConfigurationVO {

	private static ConfigurationVO me = null;

	private String hostApplicationName;
	private String introductoryText;
	private String institutionLogonDropDownList;
	//AC/OIFO - Next line added to support sorted institution list by name on the JSP Institution Drop-Down:
	private String institutionLogonDropDownListByName;
	private TreeMap<String, VistaDivisionVO> institutionMap;
	//AC/OIFO - Next line added to support sorted institution list by name on the JSP Institution Drop-Down:
	private TreeMap<String, VistaDivisionVO> institutionMapByName;
	private String oracleDAOFactoryJndiDataSourceName;
	private String daoFactoryDatabaseChoice;
	private boolean retrieveUserNewPersonDivisions;
	private boolean retrieveComputingFacilityDivisions;
	private boolean cactusModeEnabled;
	private String contextName;
	
		
	

	private static Log logger = LogFactory.getLog(ConfigurationVO.class);

	private static Object syncObj = new Object();

	/**
	 * private constructor. This class does not need to be instantiated.
	 */
	private ConfigurationVO(Document configDoc) {
		try {
			init(configDoc);
		} catch (KaajeeInstitutionResourceException e) {
			logger.error("Could not initialize institutions.", e);
		}
	}

	/**
	 * retrieves the instance of this singleton object. Returns null if singleton has somehow not been 
	 * initialized yet.
	 * @return the singleton instance of ConfigurationVO
	 * @throws IllegalStateException thrown if singleton not yet initialized
	 */
	public static ConfigurationVO getInstance() throws IllegalStateException {
		if (logger.isDebugEnabled()) {
			logger.debug("In getInstance().");
		}
		synchronized (syncObj) {
			if (me == null) {
				String exceptionString = "ConfigurationVO singleton not yet initialized.";
				if (logger.isDebugEnabled()) {
					logger.error(exceptionString);
				}
				throw new IllegalStateException(exceptionString);
			}
			return me;
		}
	}

	/**
	 * Initializes the singleton.
	 * @param configDoc Document containing KAAJEE config settings
	 * @throws IllegalStateException if singleton already initialized
	 */
	static void createInstance(Document configDoc) throws IllegalStateException {
		if (logger.isDebugEnabled()) {
			logger.debug("In createInstance().");
		}
		synchronized (syncObj) {
			if (me == null) {
				if (logger.isDebugEnabled()) {
					logger.debug("creating a new ConfigurationVO.");
				}
				me = new ConfigurationVO(configDoc);
			} else {
				String exceptionString = "ConfigurationVO instance already exists.";
				if (logger.isDebugEnabled()) {
					logger.error(exceptionString);
				}
				throw new IllegalStateException(exceptionString);
			}
		}
	}

	private void init(Document configDoc) throws KaajeeInstitutionResourceException {

		if (logger.isDebugEnabled()) {
			logger.debug("initializing.");
		}
		// initialize defaults 
		this.institutionMap = new TreeMap<String, VistaDivisionVO>();
		//AC/OIFO - Next line added to support sorted institution list by name on the JSP Institution Drop-Down:
		this.institutionMapByName = new TreeMap<String, VistaDivisionVO>();
		this.introductoryText =
			"[Introductory Text for the system not found. Adminstrators should fill in the KAAJEE settings file to furnish the required introductory text.]";
		this.hostApplicationName = "[unidentified application]";
		this.oracleDAOFactoryJndiDataSourceName = "";
		this.daoFactoryDatabaseChoice = "";
		this.institutionLogonDropDownList = "";
		this.retrieveComputingFacilityDivisions = false;
		this.retrieveUserNewPersonDivisions = false;
		this.cactusModeEnabled = false;
		this.contextName = "";
		

		// process document
		if (configDoc == null) {
			logger.error("Configuration file document object was null. Can't retrieve KAAJEE settings.");

		} else {

			// retrieve application name
			{
				NodeList appNameNodeList = configDoc.getElementsByTagName("host-application-name");
				if (appNameNodeList.getLength() > 0) {
					hostApplicationName = ((Text) appNameNodeList.item(0).getFirstChild()).getData();
				}
				if (logger.isDebugEnabled()) {
					logger.debug("set host application name: " + hostApplicationName);
				}
			}
			
//			retrieve context-name
			{
				NodeList ContextNode = configDoc.getElementsByTagName("context-root-name");
				
				if (ContextNode.getLength() > 0) {
							contextName = ((Text)ContextNode.item(0).getFirstChild()).getData();
				}
				
				if (logger.isDebugEnabled()) {
					 logger.debug("set ContextName  : " + contextName);
				}
			}
			
			// retrieve introductory text
			{
				String rawText = "";
				StringBuffer sb = new StringBuffer();
				NodeList introTextNodeList = configDoc.getElementsByTagName("system-announcement");
				if (introTextNodeList.getLength() > 0) {
					rawText = ((Text) introTextNodeList.item(0).getFirstChild()).getData();
				}
				StringTokenizer st = new StringTokenizer(rawText, "~");
				while (st.hasMoreTokens()) {
					sb.append(st.nextToken());
					sb.append("&nbsp;<br>");
				}
				introductoryText = sb.toString();
				if (logger.isDebugEnabled()) {
					logger.debug("retrieved introductory text: " + introductoryText);
				}
			}

			// retrieve database choice for DAO database choice
			{
				NodeList databaseChoiceNodeList = configDoc.getElementsByTagName("database-choice");
				if (databaseChoiceNodeList.getLength() > 0) {
					daoFactoryDatabaseChoice = ((Text) databaseChoiceNodeList.item(0).getFirstChild()).getData();
				}
				if (logger.isDebugEnabled()) {
					logger.debug("retrieved DAO database choice: " + daoFactoryDatabaseChoice);
				}
			}

			// retrieve Oracle DAO JNDI Datasource Name
			{
				NodeList jndiDataSourceNodeList = configDoc.getElementsByTagName("database-jndi-data-source-name");
				if (jndiDataSourceNodeList.getLength() > 0) {
					oracleDAOFactoryJndiDataSourceName =
						((Text) jndiDataSourceNodeList.item(0).getFirstChild()).getData();
				}
				if (logger.isDebugEnabled()) {
					logger.debug(
						"retrieved Oracle DAO Factory JNDI DataSource Name: " + oracleDAOFactoryJndiDataSourceName);
				}
			}

			// retrieve divisions from user's new Person file entries?
			//  <user-new-person-divisions retrieve="true" />
			{
				NodeList npDivisionNodeList = configDoc.getElementsByTagName("user-new-person-divisions");
				if (npDivisionNodeList.getLength() > 0) {
					Node npDivisionNode = npDivisionNodeList.item(0);
					NamedNodeMap attributes = npDivisionNode.getAttributes();
					Attr retrieveAttr = (Attr) attributes.item(0);
					if ("retrieve".equals(retrieveAttr.getName()) && ("true".equals(retrieveAttr.getValue()))) {
						retrieveUserNewPersonDivisions = true;
						if (logger.isDebugEnabled()) {
							logger.debug("Setting 'retrieve new person divisions' to true.");
						}
					}
				}
			}

			// retrieve all divisions related to the login computing facility?
			// <computing-facility-divisions retrieve="true" />
			{
				NodeList cfDivisionNodeList = configDoc.getElementsByTagName("computing-facility-divisions");
				if (cfDivisionNodeList.getLength() > 0) {
					Node cfDivisionNode = cfDivisionNodeList.item(0);
					NamedNodeMap attributes = cfDivisionNode.getAttributes();
					Attr retrieveAttr = (Attr) attributes.item(0);
					if ("retrieve".equals(retrieveAttr.getName()) && ("true".equals(retrieveAttr.getValue()))) {
						retrieveComputingFacilityDivisions = true;
						if (logger.isDebugEnabled()) {
							logger.debug("Setting 'retrieve computing facility divisions' to true.");
						}
					}
				}
			}

			// Cactus Mode
			// <cactus-insecure-mode enabled="true" />
			{
				NodeList cfDivisionNodeList = configDoc.getElementsByTagName("cactus-insecure-mode");
				if (cfDivisionNodeList.getLength() > 0) {
					Node cfDivisionNode = cfDivisionNodeList.item(0);
					NamedNodeMap attributes = cfDivisionNode.getAttributes();
					Attr retrieveAttr = (Attr) attributes.item(0);
					if ("enabled".equals(retrieveAttr.getName()) && ("true".equals(retrieveAttr.getValue()))) {
						cactusModeEnabled = true;
						//TODO add a Production check if there's a production system marker to check against
						if (logger.isDebugEnabled()) {
							logger.debug(
								"Setting Cactus mode to True. This mode should NEVER be enabled for a production application.");
						}
					}
				}
			}

			//Stations
			{
				NodeList institutionMappingNodeList = configDoc.getElementsByTagName("station-number");
				for (int i = 0; i < institutionMappingNodeList.getLength(); i++) {
					
					String stationNumber = ((Text) institutionMappingNodeList.item(i).getFirstChild()).getData();
					String logonDisplayName = null;
					
					if (stationNumber == null) {
						if (logger.isDebugEnabled()) {
							logger.debug(
								"Attempt to retrieve station number from KAAJEE configuration resulted in NULL station number.");
						}

					} else {

						if (logger.isDebugEnabled()) {
							logger.debug("retrieved station number from KAAJEE configuration: " + stationNumber);
						}

						// getting real name from Institution table/SDS
						Institution inst = null;
						try {
							inst = Institution.factory.obtainByStationNumber(stationNumber);
						} catch (Throwable t) {
							StringBuffer sb = new StringBuffer("Error retrieving institution. ");
							sb.append(t.getMessage());
							// following can't be done for JRE 1.3.x
							// sb.append(t.getStackTrace());
							throw new KaajeeInstitutionResourceException(sb.toString());
						}

						if (inst == null) {
							logonDisplayName = stationNumber + " [WARNING: NOT FOUND IN INSTITUTION TABLE]";
							if (logger.isDebugEnabled()) {
								logger.debug("SDS returned null institution for station#: " + stationNumber);
							}
						} else {
							logonDisplayName = inst.getName();
							if (logger.isDebugEnabled()) {
								logger.debug("Used SDS for login display name: " + logonDisplayName);
							}
							VistaDivisionVO icdVO = new VistaDivisionVO();
							icdVO.setName(logonDisplayName);
							icdVO.setNumber(stationNumber);
							//TODO check against double-entering (e.g. overwriting) first?
							institutionMap.put(stationNumber, icdVO);
							//AC/OIFO - Next line added to support sorted institution list by name on the JSP Institution Drop-Down:
							institutionMapByName.put(logonDisplayName, icdVO);
							if (logger.isDebugEnabled()) {
								logger.debug("added new TreeMap entry: " + icdVO.toString());
							}
						}
					}
				}
			}

			// generate the logon DropDownList string		
			institutionLogonDropDownList = createJspDropDownListOptions();
			institutionLogonDropDownListByName = createJspDropDownListOptionsByName();
		}
	}

	/**
	 * Returns a list of &lt;OPTION&gt;s for inclusion in a JSP page's SELECT drop down list, created from
	 * the set of division mappings maintained by this object. The value of 
	 * each option is the station number from the division mapping; the text
	 * of each option is the value of the logonDisplayName element of the
	 * mapping, concatenated with the station number in parentheses. The list
	 * is sorted in station number order, sorted as strings. 
	 * @return Concatenated string of &lt;OPTION&gt;s for use in a JSP login page's SELECT drop down list.
	 * been created, this exception is thrown.
	 */
	public String getJspDropDownListLoginOptions() {
		return institutionLogonDropDownList;
	}
	
	/**
	 * Returns a list of &lt;OPTION&gt;s for inclusion in a JSP page's SELECT drop down list, created from
	 * the set of division mappings maintained by this object. The value of 
	 * each option is the station number from the division mapping; the text
	 * of each option is the value of the logonDisplayName element of the
	 * mapping, concatenated with the station number in parentheses. The list
	 * is sorted in SDS logon display name order, sorted as strings. 
	 * @return Concatenated string of &lt;OPTION&gt;s for use in a JSP login page's SELECT drop down list.
	 * been created, this exception is thrown.
	 */
	public String getJspDropDownListLoginOptionsByName() {
		return institutionLogonDropDownListByName;
	}

	/**
	 * internal method to create the list of &lt;OPTION&gt;s for inclusion in a JSP page's SELECT drop down list.
	 * @return Concatenated string of &lt;OPTION&gt;s for use in a JSP login page's SELECT drop down list.
	 */
	private String createJspDropDownListOptions() {
		//TODO need a way to select one option...? Or can it be done through javascript.

		Set<String> instSet = this.institutionMap.keySet();
		Iterator<String> instSetIterator = instSet.iterator();
		StringBuffer sb = new StringBuffer();
		while (true) {
			try {
				String stationNumber = (String) instSetIterator.next();
				VistaDivisionVO im = (VistaDivisionVO) institutionMap.get(stationNumber);
				
				sb.append("<OPTION value=");
				sb.append(im.getNumber());
				sb.append(">");
				sb.append(im.getName());
				sb.append(" (");
				sb.append(im.getNumber());
				sb.append(")");
				sb.append("</OPTION>\n");

			} catch (NoSuchElementException e) {
				break;
			}
		}
		if (logger.isDebugEnabled()) {
			logger.debug(sb.toString());
		}
		return sb.toString();
	}
	
	/**
	 * internal method to create the list of &lt;OPTION&gt;s for inclusion in a JSP page's SELECT drop down list.
	 * @return Concatenated string of &lt;OPTION&gt;s for use in a JSP login page's SELECT drop down list.
	 */
	private String createJspDropDownListOptionsByName() {
		//TODO need a way to select one option...? Or can it be done through javascript.

		Set<String> instSet = this.institutionMapByName.keySet();
		Iterator<String> instSetIterator = instSet.iterator();
		StringBuffer sb = new StringBuffer();
		while (true) {
			try {
				String logonDisplayName = (String) instSetIterator.next();
				VistaDivisionVO im = (VistaDivisionVO) institutionMapByName.get(logonDisplayName);
				
				sb.append("<OPTION value=");
				sb.append(im.getNumber());
				sb.append(">");
				sb.append(im.getName());
				sb.append(" (");
				sb.append(im.getNumber());
				sb.append(")");
				sb.append("</OPTION>\n");

			} catch (NoSuchElementException e) {
				break;
			}
		}
		if (logger.isDebugEnabled()) {
			logger.debug(sb.toString());
		}
		return sb.toString();
	}

	/**
	 * Introductory Text: Returns the text stored in KAAJEE configuration file. Interprets/replaces the tilde 
	 * character &quot~&quot; as a
	 * paragraph break, replacing (in the returned output) with &lt;BR&gt; characters.</li>
	 * @return a string containing the system announcement/introductory text.
	 */
	public String getIntroductoryText() {
		return introductoryText;
	}

	/**
	 * Used internall by KAAJEE. Returns the database choice configured for KAAJEE.
	 * @return database choice
	 */
	public String getDaoFactoryDatabaseChoice() {
		return daoFactoryDatabaseChoice;
	}

	/**
	 * Used internally by KAAJEE. Returns the JNDI Data Source name configured for KAAJEE.
	 * @return JNDI Data Source name
	 */
	public String getOracleDAOFactoryJndiDataSourceName() {
		return oracleDAOFactoryJndiDataSourceName;
	}

	/**
	 * Controls whether or not KAAJEE should retrieve the New Person divisions for a user.
	 * @return true if NP divisions should be retrieved
	 */
	public boolean getRetrieveNewPersonDivisions() {
		return this.retrieveUserNewPersonDivisions;
	}

	/**
	 * Controls whether or not KAAJEE should retrieve the computing facility divisions for a user.
	 * @return true if computing facility divisions should be retrieved
	 */
	public boolean getRetrieveComputingFacilityDivisions() {
		return this.retrieveComputingFacilityDivisions;
	}

	/**
	 * Checks if a given division is in the KAAJEE login list
	 * @param stationNumber division to check against Kaajee login list
	 * @return true if in list, false if not
	 */
	boolean isKaajeeLoginDivision(String stationNumber) {
		boolean returnVal = false;
		if (institutionMap.containsKey(stationNumber)) {
			returnVal = true;
		}
		return returnVal;
	}

	/**
	 * 
	 * @return true if enabled, false if not.
	 */
	public boolean getIsCactusModeEnabled() {
		return this.cactusModeEnabled;
	}

	/**
	 * Return the hosting application's name
	 * @return host app name
	 */
	public String getHostApplicationName() {
		return this.hostApplicationName;
	}
	
	/**
	* Return the Context root Name
	* @return Context root Name
	*/
	public String getContextName() {
		return this.contextName;
	}	
	
	/**
	 * String representation of this object.
	 * @return String representation of the data values held by this object.
	 */
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("; Database Choice: ");
		sb.append(this.getDaoFactoryDatabaseChoice());
		sb.append("Oracle JNDI Source Name: ");
		sb.append(this.getOracleDAOFactoryJndiDataSourceName());
		sb.append("Cactus Mode: ");
		sb.append(this.getIsCactusModeEnabled());
		sb.append("Retrieve computing facility divisions: ");
		sb.append(this.getRetrieveComputingFacilityDivisions());
		sb.append("; retreive NP divisions: ");
		sb.append(this.getRetrieveNewPersonDivisions());
		sb.append("; JSP Drop Down List Options: ");
		sb.append(this.getJspDropDownListLoginOptions());
		sb.append("; Introductory Text: ");
		sb.append(this.getIntroductoryText());
		return sb.toString();
	}
	
	public TreeMap<String, VistaDivisionVO> getInstitutionMap() {
		return institutionMap;
	}
	
}
