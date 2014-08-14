package gov.va.med.lom.kaajee.jboss.model.json;


public class LoginUserInfoVOJson {
	
	public static final String SESSION_KEY = "gov.va.med.lom.authentication.kernel.LoginUserInfoJson";
	
	private boolean success;
	private String errors;
	
	private String userDuz;
	private String userName01;
	private String userNameDisplay;
	private String userLoginStationNumber;
	private String userParentAdministrativeFacilityStationNumber;
	private String userParentComputerSystemStationNumber;
	private String userLastName;
	private String userFirstName;
	private String userMiddleName;
	private String userPrefix;
	private String userSuffix;
	private String userDegree;
	private String signonLogIen;
	
	private String userLoginStationName;
	
	/**
	 * ^ delimited list of user roles for
	 * client side presentation; server side validation
	 * still required
	 */
	private String userRoles;

	
	public LoginUserInfoVOJson(){
	    userRoles = null;
	}
	
	
	public String getUserDuz() {
		return userDuz;
	}
	public void setUserDuz(String userDuz) {
		this.userDuz = userDuz;
	}
	public String getUserName01() {
		return userName01;
	}
	public void setUserName01(String userName01) {
		this.userName01 = userName01;
	}
	public String getUserNameDisplay() {
		return userNameDisplay;
	}
	public void setUserNameDisplay(String userNameDisplay) {
		this.userNameDisplay = userNameDisplay;
	}
	public String getUserLoginStationNumber() {
		return userLoginStationNumber;
	}
	public void setUserLoginStationNumber(String userLoginStationNumber) {
		this.userLoginStationNumber = userLoginStationNumber;
	}
	public String getUserParentAdministrativeFacilityStationNumber() {
		return userParentAdministrativeFacilityStationNumber;
	}
	public void setUserParentAdministrativeFacilityStationNumber(
			String userParentAdministrativeFacilityStationNumber) {
		this.userParentAdministrativeFacilityStationNumber = userParentAdministrativeFacilityStationNumber;
	}
	public String getUserParentComputerSystemStationNumber() {
		return userParentComputerSystemStationNumber;
	}
	public void setUserParentComputerSystemStationNumber(
			String userParentComputerSystemStationNumber) {
		this.userParentComputerSystemStationNumber = userParentComputerSystemStationNumber;
	}
	public String getUserLastName() {
		return userLastName;
	}
	public void setUserLastName(String userLastName) {
		this.userLastName = userLastName;
	}
	public String getUserFirstName() {
		return userFirstName;
	}
	public void setUserFirstName(String userFirstName) {
		this.userFirstName = userFirstName;
	}
	public String getUserMiddleName() {
		return userMiddleName;
	}
	public void setUserMiddleName(String userMiddleName) {
		this.userMiddleName = userMiddleName;
	}
	public String getUserPrefix() {
		return userPrefix;
	}
	public void setUserPrefix(String userPrefix) {
		this.userPrefix = userPrefix;
	}
	public String getUserSuffix() {
		return userSuffix;
	}
	public void setUserSuffix(String userSuffix) {
		this.userSuffix = userSuffix;
	}
	public String getUserDegree() {
		return userDegree;
	}
	public void setUserDegree(String userDegree) {
		this.userDegree = userDegree;
	}
	public String getSignonLogIen() {
		return signonLogIen;
	}
	public void setSignonLogIen(String signonLogIen) {
		this.signonLogIen = signonLogIen;
	}
	public String getUserLoginStationName() {
		return userLoginStationName;
	}
	public void setUserLoginStationName(String userLoginStationName) {
		this.userLoginStationName = userLoginStationName;
	}
	public String getUserRoles() {
		return userRoles;
	}
	public void setUserRoles(String userRoles) {
		this.userRoles = userRoles;
	}
	public boolean isSuccess() {
		return success;
	}
	public void setSuccess(boolean success) {
		this.success = success;
	}
	public String getErrors() {
		return errors;
	}
	public void setErrors(String errors) {
		this.errors = errors;
	}
	
	public void addRole(String role){
		if(userRoles == null || userRoles.length() == 0){
			userRoles = role.trim();
		}else{
			userRoles += "^" + role.trim();
		}
		
	}
}
