package gov.va.med.authentication.kernel;

import java.io.Serializable;
import java.util.TreeMap;

/**
 * KAAJEE returns this JavaBean to the enclosing application after login. It is returned to the enclosing 
 * application as an object in HttpSession. It contains user demographics information about the logged-in
 * user. A public static field provides the key for the application to find the object in HttpSession.
 * 
 * @author VHIT Security and Other Common Services (S&OCS)
 * @version 1.1.0.007
 */
public class LoginUserInfoVO implements Serializable {

	private String userDuz;
	private String userName01;
	private String userNameDisplay;
	private String userLoginStationNumber;
	private String userParentAdministrativeFacilityStationNumber;
	private String userParentComputerSystemStationNumber;
	private TreeMap<?, ?> userPermittedDivisions;
	private TreeMap<?, ?> loginDivisionVistaProviderDivisions;
	private String userLastName;
	private String userFirstName;
	private String userMiddleName;
	private String userPrefix;
	private String userSuffix;
	private String userDegree;
	private String signonLogIen;
	private String divisionName;
	private String divisionIEN;
	private String userTitle;
	private String userServiceSection;
	private String language;
	private String dateTime;
	private String userVpid;

	/**
	 * The key under which this value is placed in the session object during login, and from which this object
	 * can be retrieved by the enclosing web application post-login.
	 */
	public static final String SESSION_KEY = "gov.va.med.authentication.kernel.LoginUserInfo";

	/**
	 * generic constructor.
	 */
	public LoginUserInfoVO() {
		init();
	}

	/**
	 * initialize the object.
	 */
	void init() {
		this.userDuz = "";
		this.userName01 = "";
		this.userNameDisplay = "";
		this.userLoginStationNumber = "";
		this.userParentAdministrativeFacilityStationNumber = "";
		this.userParentComputerSystemStationNumber = "";
		this.userPermittedDivisions = null;
		this.userFirstName = "";
		this.userLastName = "";
		this.userMiddleName = "";
		this.userPrefix = "";
		this.userSuffix = "";
		this.userDegree = "";
		this.loginDivisionVistaProviderDivisions = null;
		this.signonLogIen = "";
		this.divisionName = "";
		this.divisionIEN = "";
		this.userTitle = "";
		this.userServiceSection = "";
		this.language = "";
		this.dateTime = "";
		this.userVpid = "";
	}

	/**
	 * return the user's DUZ.
	 * @return user's DUZ.
	 */
	public String getUserDuz() {
		return this.userDuz;
	}

	/**
	 * set the user's DUZ.
	 * @param userDuz DUZ to set.
	 */
	public void setUserDuz(String userDuz) {
		this.userDuz = userDuz;
	}

	/**
	 * gets the user name as retrieved from the .01 field of the New Person file, e.g., "SMITH,JANE E".
	 * @return user name as retrieved from the .01 field of the New Person file.
	 */
	public String getUserName01() {
		return this.userName01;
	}

	/**
	 * Sets the user name as retrieved from the .01 field of the New Person file.
	 * @param userName01 user name matching the value of the user name in the .01 field of the New Person file.
	 */
	public void setUserName01(String userName01) {
		this.userName01 = userName01;
	}

	/**
	 * Gets the display name of the user, as put together by the Name Standardization APIs on M,
	 * e.g., "Jane E. Smith".
	 * @return display name of the user
	 */
	public String getUserNameDisplay() {
		return this.userNameDisplay;
	}

	/** 
	 * Sets the display name of the user, as put together by the Name Standardization APIs on M.
	 * @param userNameDisplay display name of the user
	 */
	public void setUserNameDisplay(String userNameDisplay) {
		this.userNameDisplay = userNameDisplay;
	}

	/**
	 * returns the station number of the division the user selected at login. This can be used as a
	 * key to retrieve additional information (e.g.) name about the login division from the TreeMap of
	 * permitted divisions returned by the getPermittedDivisions method.
	 * @return station number
	 * @see #getPermittedDivisions
	 */
	public String getLoginStationNumber() {
		return this.userLoginStationNumber;
	}

	/**
	 * sets the station number of the user's division.
	 * @param userLoginStationNumber sets the station number
	 */
	public void setLoginStationNumber(String userLoginStationNumber) {
		this.userLoginStationNumber = userLoginStationNumber;
	}

	/**
	 * A list of the user's permitted divisions returned as a TreeMap. The key value in the TreeMap is
	 * the station number, which is a String. The object value stored under each key is a VistaDivisionVO object.
	 * <p>
	 * This list represents all of the divisions on the M/Kernel system that the user could have logged into. 
	 * Applications can display a list of other divisions that the user could switch to within the application,
	 * allowing the user to select a different division. It is then the application's responsibility to use
	 * the proper division for its own internal business rules, and also to pass
	 * the proper division station number with each VistALink RPC call it makes to M.
	 * @return a TreeMap list of permitted divisions (sorted by station number.)
	 * @see VistaDivisionVO
	 */
	public TreeMap<?, ?> getPermittedNewPersonFileDivisions() {
		return this.userPermittedDivisions;
	}

	/**
	 * sets the list of the user's permitted divisions
	 * @param userPermittedDivisions the list of permitted divisions
	 */
	public void setPermittedDivisons(TreeMap<?, ?> userPermittedDivisions) {
		this.userPermittedDivisions = userPermittedDivisions;
	}

	/**
	 * Name Components File value.
	 * @return last name.
	 */
	public String getUserLastName() {
		return this.userLastName;
	}

	/**
	 * Set Name Components File value.
	 * @param lastName last name.
	 */
	public void setUserLastName(String lastName) {
		this.userLastName = lastName;
	}

	/**
	 * Name Components File value.
	 * @return first name
	 */
	public String getUserFirstName() {
		return this.userFirstName;
	}

	/**
	 * Name Components File value. 
	 * @param firstName first name
	 */
	public void setUserFirstName(String firstName) {
		this.userFirstName = firstName;
	}

	/**
	 *  Name Components File value.
	 * @return middle name
	 */	
	public String getUserMiddleName() {
		return this.userMiddleName;
	}

	/**
	 * Name Components File value. 
	 * @param middleName middle name
	 */	
	public void setUserMiddleName(String middleName) {
		this.userMiddleName = middleName;
	}

	/**
	 * Name Components File value. 
	 * @return prefix
	 */	
	public String getUserPrefix() {
		return this.userPrefix;
	}

	/**
	 * Name Components File value. 
	 * @param prefix prefix
	 */	
	public void setUserPrefix(String prefix) {
		this.userPrefix = prefix;
	}

	/**
	 * Name Components File value. 
	 * @return suffix
	 */	
	public String getUserSuffix() {
		return this.userSuffix;
	}

	/**
	 * Name Components File value. 
	 * @param suffix suffix
	 */	
	public void setUserSuffix(String suffix) {
		this.userSuffix = suffix;
	}

	/**
	 * Name Components File value. 
	 * @return degree
	 */
	public String getUserDegree() {
		return this.userDegree;	
	}

	/**
	 * Name Components File value. 
	 * @param degree degree
	 */	
	public void setUserDegree(String degree) {
		this.userDegree = degree;
	}

	/**
	 * The parent facility of the division used for login, as resolved on the login computer system based 
	 * on that system's Institution file.
	 * @return station number of parent facility
	 */
	public String getUserParentAdministrativeFacilityStationNumber() {
		return this.userParentAdministrativeFacilityStationNumber;	
	}
	
	/**
	 * Sets the parent facility of the division used for login, as resolved on the login computer system based 
	 * on that system's Institution file.
	 * @param parentFacilityStationNumber station number
	 */
	public void setUserParentAdministrativeFacilityStationNumber(String parentFacilityStationNumber) {
		this.userParentAdministrativeFacilityStationNumber = parentFacilityStationNumber;
	}
	
	/**
	 * The computer system's default institution/computer system institution, as identified in the
	 * system's Kernel Site Parameters file.
	 * @return station number
	 */
	public String getUserParentComputerSystemStationNumber() {
		return this.userParentComputerSystemStationNumber;
	}
	
	/**
	 * sets the computer system's default institution/computer system institution, as identified in the
	 * system's Kernel Site Parameters file.
	 * @param computerSystemParentStationNumber station number to set
	 */
	public void setUserParentComputerSystemStationNumber(String computerSystemParentStationNumber) {
		this.userParentComputerSystemStationNumber = computerSystemParentStationNumber;
	}

	/**
	 * returns the list of divisions (based on information in the SDS Institution table) whose Vista Provider
	 * is the same as the Vista Provider computer system of the login division.
	 * @return the list of divisions stored in this object matching the criteria above.
	 */
	public TreeMap<?, ?> getLoginDivisionVistaProviderDivisions() {
		return this.loginDivisionVistaProviderDivisions;
	}
	
	/**
	 * set the list of divisions (based on information in the SDS Institution table) whose Vista Provider
	 * is the same as the Vista Provider computer system of the login division.
	 * @param loginDivisionVistaProviderDivisions TreeMap of divisions matching the criteria above.
	 */
	public void setLoginDivisionVistaProviderDivisions(TreeMap<?, ?> loginDivisionVistaProviderDivisions) {
		this.loginDivisionVistaProviderDivisions = loginDivisionVistaProviderDivisions;
	}

	/**
	 * place to hold signon log IEN
	 * @param signonLogIen signon log IEN
	 */
	public void setSignonLogIen(String signonLogIen) {
		this.signonLogIen = signonLogIen;
	}

	/**
	 * get the signon log IEN
	 * @return signon log IEN
	 */	
	public String getSignonLogIen() {
		return this.signonLogIen;
	}

	public String getDivisionName() {
		return divisionName;
	}

	public void setDivisionName(String divisionName) {
		this.divisionName = divisionName;
	}

	public String getDateTime() {
		return dateTime;
	}

	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}

	public String getDivisionIEN() {
		return divisionIEN;
	}

	public void setDivisionIEN(String divisionIEN) {
		this.divisionIEN = divisionIEN;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getUserServiceSection() {
		return userServiceSection;
	}

	public void setUserServiceSection(String userServiceSection) {
		this.userServiceSection = userServiceSection;
	}

	public String getUserTitle() {
		return userTitle;
	}

	public void setUserTitle(String userTitle) {
		this.userTitle = userTitle;
	}

	public String getUserVpid() {
		return userVpid;
	}

	public void setUserVpid(String userVpid) {
		this.userVpid = userVpid;
	}

	/**
	 * Returns a string representation of the values in the object.
	 * @return string representation of the values in the object.
	 */
	public String toString() {
		StringBuffer sb = new StringBuffer(".01 Name: ");
		sb.append(this.userName01);
		sb.append("; Display Name: ");
		sb.append(this.userNameDisplay);
		sb.append("; DUZ: ");
		sb.append(this.userDuz);
		sb.append("; Login Station Number: ");
		sb.append(this.userLoginStationNumber);
		return sb.toString();
		//TODO add list of permissible divisions here, too? and various names?
	}

}
