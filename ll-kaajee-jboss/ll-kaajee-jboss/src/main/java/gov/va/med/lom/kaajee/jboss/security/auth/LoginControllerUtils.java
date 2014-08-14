package gov.va.med.lom.kaajee.jboss.security.auth;

import gov.va.med.authentication.kernel.KaajeeException;
import gov.va.med.authentication.kernel.KaajeeInstitutionResourceException;
import gov.va.med.authentication.kernel.LoginUserInfoVO;
import gov.va.med.lom.kaajee.jboss.model.json.LoginUserInfoVOJson;
import gov.va.med.term.access.Institution;
import gov.va.med.vistalink.adapter.cci.VistaLinkConnection;
import gov.va.med.vistalink.adapter.cci.VistaLinkConnectionFactory;
import gov.va.med.vistalink.adapter.cci.VistaLinkConnectionSpec;
import gov.va.med.vistalink.institution.InstitutionMapNotInitializedException;
import gov.va.med.vistalink.institution.InstitutionMappingDelegate;
import gov.va.med.vistalink.institution.InstitutionMappingNotFoundException;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.resource.ResourceException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;



/**
 * Utility calls used by the LoginController.
 * @author VHIT Security and Other Common Services (S&OCS)
 * @version 1.1.0.007
 */
class LoginControllerUtils {

	private static Log log = LogFactory.getLog(LoginControllerUtils.class);
	static final String J_PASSWORD_PREFIX_WEB = "WEB";
	static final String J_PASSWORD_PREFIX_CACTUS = "TST";
	static final String J_USERNAME_DUZ = "DUZ_";
	static final String J_USERNAME_COMPSYS = "~CMPSYS_";

	/**
	 * close a VistaLink connection
	 * @param myConnection
	 */
	static void closeVistaLinkConnection(VistaLinkConnection myConnection) {
		if (myConnection != null) {
			try {
				myConnection.close();
			} catch (ResourceException e) {
				//TODO -- anything besides swallow?
			}
		}
	}

	/**
	 * use to do basic input value checking on form submission values
	 * @param institution login institution submitted
	 * @param access access code submitted
	 * @param verify verify code submitted
	 * @throws KaajeeException thrown if input found to be invalid.
	 */
	static void checkFormInputValues(String institution, String access, String verify) throws KaajeeException {
		{
			// TODO add some regexp checking here too.
			if (((institution == null) || (access == null) || (verify == null))
				|| ((institution.length() < 1) || (access.length() < 1) || (verify.length() < 1))) {

				StringBuffer sb = new StringBuffer("Problem with submitted login information: ");
				if ((access == null) || (access.length() < 1)) {
					sb.append("Access code not provided");
				}
				if ((verify == null) || (verify.length() < 1)) {
					if (sb.length() > 0) {
						sb.append("; ");
					}
					sb.append("Verify code not provided");
				}
				if ((institution == null) || (institution.length() < 1)) {
					if (sb.length() > 0) {
						sb.append("; ");
					}
					sb.append("Login facility not provided");
				}
				sb.append(".");
				throw new KaajeeException(sb.toString());
			}
		}
	}

	/**
	 * Validates a candidate "login institution" against several criteria.
	 * @param institutionStNum station number to validate
	 * @throws KaajeeException thrown if institution is invalid.
	 */
	static void validateKaajeeLoginDivision(String institutionStNum)
		throws KaajeeInstitutionResourceException, KaajeeException {

	    log.debug("validating stationNo:  " + institutionStNum);
	    
		// 1. verify that is in Institution table, and has a legitimate Vista Provider too. (An exception
		//    is thrown if not)
		getValidatedVistaProvider(institutionStNum);

		// 2. verify that it's in the KAAJEE login list		
		ConfigurationVO configVO = ConfigurationVO.getInstance();
		if (!configVO.isKaajeeLoginDivision(institutionStNum)) {
			// if not in the KAAJEE login file (which probably means a hacking attempt?) don't let in
			StringBuffer sb = new StringBuffer("Selected Login Division '");
			sb.append(institutionStNum);
			sb.append("' not configured in KAAJEE for login.");
			throw new KaajeeException(sb.toString());
		}
	}

	/**
	 * Creates a VistaLinkConnection to the specified institution using the supplied Connection Specification.
	 * @param institution institution to get connection for
	 * @param connectionSpec A VistALink connection specification providing reauthentication credentials
	 * @return a VistaLinkConnection to the specified institution using the specified connection spec.
	 * @throws KaajeeException
	 */
	static VistaLinkConnection getVistaLinkConnection(String institution, VistaLinkConnectionSpec connectionSpec)
		throws KaajeeException {
		VistaLinkConnection myConnection = null;
		try {
			String jndiConnectionName = "java:/" + InstitutionMappingDelegate.getJndiConnectorNameForInstitution(institution);
			log.debug("jndiConnectionName: " + jndiConnectionName);
			InitialContext context = new InitialContext();
			VistaLinkConnectionFactory cf = (VistaLinkConnectionFactory) context.lookup(jndiConnectionName);
			myConnection = (VistaLinkConnection) cf.getConnection(connectionSpec);
			
		} catch (NamingException e) {

			LoginControllerUtils.closeVistaLinkConnection(myConnection);
			StringBuffer sb =
				new StringBuffer("Naming exception; Could not get a connection from connector pool for institution '");
			sb.append(institution);
			sb.append("'.");
			throw new KaajeeException(sb.toString(), e);

		} catch (ResourceException e) {
			LoginControllerUtils.closeVistaLinkConnection(myConnection);
			StringBuffer sb = new StringBuffer("Could not get a connection from connector pool for institution '");
			sb.append(institution);
			sb.append("'.");
			throw new KaajeeException(sb.toString(), e);

		} catch (InstitutionMapNotInitializedException e) {

			StringBuffer sb =
				new StringBuffer("Institution mapping not initialized. Could not get a connection from connector pool for institution '");
			sb.append(institution);
			sb.append("'.");
			throw new KaajeeException(sb.toString(), e);

		} catch (InstitutionMappingNotFoundException e) {

			LoginControllerUtils.closeVistaLinkConnection(myConnection);
			StringBuffer sb = new StringBuffer("Institution Mapping not found for institution '");
			sb.append(institution);
			sb.append("'. Could not get a connection from connector pool for institution.");
			throw new KaajeeException(sb.toString(), e);
			
		}

		if (log.isDebugEnabled()) {
			log.debug("got connection.");
		}
		return myConnection;
	}


	
	
	/**
	 * Validates an an institution and its computing provider, returning the combined information.
	 * @param divisionStationNumber station number to validate
	 * @return DivisionWithVistaProviderVO object populated with original station# plus station# of facility's
	 * Vista Provider, as returned by the SDS institution utilities.
	 * @throws KaajeeException thrown if 1) the passed division is not in the Institution file, 2) the
	 * passed division doesn't have a Vista Provider identified, 3) the Vista Provider does not have a station
	 * number.
	 * @throws KaajeeInstitutionResourceException if can't get institution information, throw this
	 */
	static DivisionWithVistaProviderVO getValidatedVistaProvider(String divisionStationNumber)
		throws KaajeeInstitutionResourceException, KaajeeException {
		// retrieve Institution for station #
		Institution loginInst = null;
		try {
			loginInst = Institution.factory.obtainByStationNumber(divisionStationNumber);
		} catch (Throwable t) {
		    log.error("some error occurred", t);
			StringBuffer sb =
				new StringBuffer("Institution validation error: Could not retrieve institution from Institution table. ");
			sb.append(t.getMessage());
			// sb.append(t.getStackTrace());
			throw new KaajeeInstitutionResourceException(sb.toString());
		}
		if (loginInst == null) {

			StringBuffer sb =
				new StringBuffer("Institution validation error: Could not find specified division station number in Institution table: '");
			sb.append(divisionStationNumber);
			sb.append("'.");
			throw new KaajeeException(sb.toString());

		} // get Vista Provider for station #
		Institution vistaProviderInst = loginInst.getVistaProvider();
		if (vistaProviderInst == null) {

			StringBuffer sb =
				new StringBuffer("Institution validation error: Could not retrieve Vista Provider institution for specified division station number in Institution table: '");
			sb.append(divisionStationNumber);
			sb.append("'.");
			throw new KaajeeException(sb.toString());

		}
		String vistaProviderInstStNum = vistaProviderInst.getStationNumber();
		// validate Vista Provider for station #
		if ((vistaProviderInstStNum == null) || (vistaProviderInstStNum.length() == 0)) {

			StringBuffer sb =
				new StringBuffer("Institution validation error: Vista Provider institution station number is null in Institution table, for specified division station number: '");
			sb.append(divisionStationNumber);
			sb.append("'.");
			throw new KaajeeException(sb.toString());
		}

		DivisionWithVistaProviderVO returnVal = new DivisionWithVistaProviderVO();
		returnVal.setDivisionStationNumber(divisionStationNumber);
		returnVal.setVistaProviderStationNumber(vistaProviderInstStNum);
		return returnVal;
	}

	
	public static LoginUserInfoVOJson mapJson(LoginUserInfoVO userInfo, Institution inst){
	    
	    LoginUserInfoVOJson json = new LoginUserInfoVOJson();
	    
	    json.setUserDuz(userInfo.getUserDuz());
	    json.setUserName01(userInfo.getUserName01());
	    json.setUserNameDisplay(userInfo.getUserNameDisplay());
	    json.setUserLoginStationNumber(inst.getStationNumber());
	    json.setUserParentAdministrativeFacilityStationNumber(userInfo.getUserParentAdministrativeFacilityStationNumber());
	    json.setUserParentComputerSystemStationNumber(userInfo.getUserParentComputerSystemStationNumber());
	    json.setUserLastName(userInfo.getUserLastName());
	    json.setUserFirstName(userInfo.getUserFirstName());
	    json.setUserMiddleName(userInfo.getUserMiddleName());
	    json.setUserPrefix(userInfo.getUserPrefix());
	    json.setUserSuffix(userInfo.getUserSuffix());
	    json.setUserDegree(userInfo.getUserDegree());
	    json.setSignonLogIen(userInfo.getSignonLogIen());
	    json.setUserLoginStationName(inst.getVistaName());
	    
	    return json;
	    
	}
	
	
	
}
	