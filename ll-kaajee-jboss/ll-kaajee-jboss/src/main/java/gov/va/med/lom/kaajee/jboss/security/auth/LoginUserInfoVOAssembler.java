package gov.va.med.lom.kaajee.jboss.security.auth;

import gov.va.med.authentication.kernel.KaajeeException;
import gov.va.med.authentication.kernel.KaajeeInstitutionResourceException;
import gov.va.med.authentication.kernel.LoginUserInfoVO;
import gov.va.med.authentication.kernel.VistaDivisionVO;
import gov.va.med.crypto.VistaKernelHash;
import gov.va.med.exception.FoundationsException;
import gov.va.med.term.access.Institution;
import gov.va.med.vistalink.adapter.cci.VistaLinkConnection;
import gov.va.med.vistalink.adapter.cci.VistaLinkDuzConnectionSpec;
import gov.va.med.vistalink.adapter.record.LoginsDisabledFaultException;
import gov.va.med.vistalink.rpc.RpcRequest;
import gov.va.med.vistalink.rpc.RpcRequestFactory;
import gov.va.med.vistalink.rpc.RpcResponse;
import gov.va.med.vistalink.security.m.SecurityAccessVerifyCodePairInvalidException;
import gov.va.med.vistalink.security.m.SecurityDivisionDeterminationFaultException;
import gov.va.med.vistalink.security.m.SecurityIdentityDeterminationFaultException;
import gov.va.med.vistalink.security.m.SecurityTooManyInvalidLoginAttemptsFaultException;
import gov.va.med.vistalink.security.m.SecurityUserAuthorizationException;
import gov.va.med.vistalink.security.m.SecurityUserVerifyCodeException;

import java.util.StringTokenizer;
import java.util.TreeMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Assembles LoginUserInfoVO objects.
 * @author VHIT Security and Other Common Services (S&OCS)
 * @version 1.1.0.007
 */
class LoginUserInfoVOAssembler {

	private static final Log log = LogFactory.getLog(LoginUserInfoVOAssembler.class);

	private static String USER_INFO_RPC = "XUS KAAJEE GET USER INFO";
	private static String USER_INFO_PROXY_RPC = "XUS KAAJEE GET USER VIA PROXY";
	private static final String KERNEL_USER_INFO_RPC = "XUS GET USER INFO";

	/**
	 * Create/populate a LoginUserInfoVO object with the user demographics information for this user.
	 * Calls XUS KAAJEE GET USER INFO rpc to get most of this information.
	 * @param myConnection VistaLink connection
	 * @param kajLoginDivStNum Station number chosen by the user at KAAJEE login
	 * @param clientIp client IP address, used to mark the sign-on log
	 * @return a LoginUserInfoVO object containing user demographics information for this user.
	 * @throws FoundationsException thrown if any errors encountered retrieving the user demographics info.
	 */
	static LoginUserInfoVO getLoginUserInfo(VistaLinkConnection myConnection, String kajLoginDivStNum, String clientIp)
		throws KaajeeInstitutionResourceException, KaajeeException {
		LoginUserInfoVO returnVal = null;

		ConfigurationVO configVO = ConfigurationVO.getInstance();
		boolean retrieveNewPersonDivisions = configVO.getRetrieveNewPersonDivisions();
		boolean retrieveComputingFacilityDivisions = configVO.getRetrieveComputingFacilityDivisions();

		// get div info for kaajee login division and Vista Provider institution
		Institution kajLoginDivInst = null;
		try {
			kajLoginDivInst = Institution.factory.obtainByStationNumber(kajLoginDivStNum);
		} catch (Throwable t) {
			StringBuffer sb = new StringBuffer("Error retrieving institution. ");
			sb.append(t.getMessage());
			// sb.append(t.getStackTrace());
			throw new KaajeeInstitutionResourceException(sb.toString());
		}
		Institution kajLoginDivVistaProviderInst =
			getLoginDivisionVistaProviderInstitution(kajLoginDivStNum, kajLoginDivInst);
		String kajLoginDivVistaProviderStNum =
			getLoginDivisionVistaProviderStationNumber(kajLoginDivStNum, kajLoginDivVistaProviderInst);

		String rpcResults = null;

		try {

			RpcRequest vReq = RpcRequestFactory.getRpcRequest();
			vReq.setRpcContext(KaajeeLoginModule.LOGIN_RPC_CONTEXT);
			vReq.setRpcClientTimeOut(600);
			vReq.setUseProprietaryMessageFormat(true);
			vReq.setRpcName(USER_INFO_RPC);
			vReq.getParams().setParam(1, "string", clientIp);
			vReq.getParams().setParam(2, "string,", ConfigurationVO.getInstance().getHostApplicationName());
			RpcResponse vResp = myConnection.executeRPC(vReq);
			rpcResults = vResp.getResults();

		} catch (SecurityUserAuthorizationException e) {

			String exceptionString =
				"Authorization failed for your user account on the M system; could not log you on.<br>&nbsp;<br>Please contact your site manager for assistance.<br>&nbsp;<br>More details below:<br>&nbsp;<br>";
			throw new KaajeeException(exceptionString, e);

		} catch (SecurityTooManyInvalidLoginAttemptsFaultException e) {

			StringBuffer sb =
				new StringBuffer("Login failed due to too many invalid login attempts.<br>&nbsp;<br>Please contact your site manager for assistance.<br>&nbsp;<br>More details below:<br>&nbsp;<br>");
			sb.append(e.getMessage());
			if (log.isErrorEnabled()) {
				log.error(sb.toString(), e);
			}
			throw new KaajeeException(sb.toString(), e);

		} catch (SecurityUserVerifyCodeException e) {

			String exceptionString =
				"Your verify code has expired or needs changing; could not log you on.<br>&nbsp;<br>Please use another application to change your verify code and then try to log on again here. Or, contact your site manager for assistance.";
			throw new KaajeeException(exceptionString);

		} catch (SecurityAccessVerifyCodePairInvalidException e) {

			String exceptionString = "Not a valid ACCESS CODE/VERIFY CODE pair.";
			throw new KaajeeException(exceptionString);

		} catch (LoginsDisabledFaultException e) {

			String exceptionString = "Logins are disabled on the M system.";
			throw new KaajeeException(exceptionString);

		} catch (SecurityIdentityDeterminationFaultException e) {

			StringBuffer sb =
				new StringBuffer("Could not match you with your M account; could not log you on.<br>&nbsp;<br>Please contact your site manager for assistance.<br>&nbsp;<br>More details below:<br>&nbsp;<br>");
			sb.append(e.getMessage());
			if (log.isErrorEnabled()) {
				log.error(sb.toString(), e);
			}
			throw new KaajeeException(sb.toString(), e);

		} catch (SecurityDivisionDeterminationFaultException e) {

			StringBuffer sb =
				new StringBuffer("The institution/division you selected for login is not valid for your M user account; could not log you on.<br>&nbsp;<br>Please contact your site manager for assistance.<br>&nbsp;<br>More details below:<br>&nbsp;<br>");
			sb.append(e.getMessage());
			if (log.isErrorEnabled()) {
				log.error(sb.toString(), e);
			}
			throw new KaajeeException(sb.toString(), e);

		} catch (FoundationsException e) {

			StringBuffer sb =
				new StringBuffer("Error logging on or retrieving user information; could not log you on.<br>&nbsp;<br>Please contact your site manager for assistance.<br>&nbsp;<br>More details below:<br>&nbsp;<br>");
			sb.append(e.getMessage());
			if (log.isErrorEnabled()) {
				log.error(sb.toString(), e);
			}
			throw new KaajeeException(sb.toString(), e);

		}

		if (log.isDebugEnabled()) {
			log.debug("results of XUS KAAJEE GET USER INFO: " + rpcResults);
		}
		/*
		 * ; Result(0) is the users DUZ.
		 * ; Result(1) is the user name from the .01 field.
		 * ; Result(2) is the users full name from the name standard file.
		 * ; Result(3) is the FAMILY (LAST) NAME (or ^ if null)
		 * ; Result(4) is the GIVEN (FIRST) NAME (or ^ if null)
		 * ; Result(5) is the MIDDLE NAME (or ^ if null)
		 * ; Result(6) is the PREFIX (or ^ if null)
		 * ; Result(7) is the SUFFIX (or ^ if null)
		 * ; Result(8) is the DEGREE (or ^ if null)
		 * ; Result(9) is station # of the division that the user is working in.
		 * ; Result(10) is the station # of the parent facility for the login division
		 * ; Result(11) is the station # of the computer system "parent" from the KSP file.
		 * ; Result(12) is the IEN of the signon log entry
		 * ; Result(13) = # of permissible divisions
		 * ; Result(14-n) are the permissible divisions for user login, in the format:
		 * ;           IEN of file 4^Station Name^Station Number^default? (1 or 0)
		 */

		returnVal = new LoginUserInfoVO();
		StringTokenizer st = new StringTokenizer(rpcResults, "\n");
		returnVal.setUserDuz(st.nextToken()); //0
		returnVal.setUserName01(st.nextToken()); //1
		returnVal.setUserNameDisplay(st.nextToken()); //2
		returnVal.setUserLastName(st.nextToken()); //3
		returnVal.setUserFirstName(st.nextToken()); //4
		returnVal.setUserMiddleName(st.nextToken()); //5
		returnVal.setUserPrefix(st.nextToken()); //6
		returnVal.setUserSuffix(st.nextToken()); //7
		returnVal.setUserDegree(st.nextToken()); //8

		//Login Division #
		//TODO take a stronger action if M-side and SDS-side disagree? ABORT THE LOGIN?
		returnVal.setLoginStationNumber(kajLoginDivStNum);
		String mLoginInstitutionStationNumber = st.nextToken(); //9
		if (!kajLoginDivStNum.equals(mLoginInstitutionStationNumber)) {
			if (log.isErrorEnabled()) {
				StringBuffer sb = new StringBuffer("Warning: For User DUZ: '");
				sb.append(returnVal.getUserDuz());
				sb.append("KAAJEE login division '");
				sb.append(kajLoginDivStNum);
				sb.append("' is different than M login division DUZ(2) value '");
				sb.append(mLoginInstitutionStationNumber);
				sb.append("'.");
				log.error(sb.toString());
			}
		}

		//Parent Station #
		{
			String mLoginDivParentStNum = st.nextToken(); // 10
			String kajLoginDivParentStNum = getParentStationNumber(kajLoginDivInst, mLoginDivParentStNum);
			returnVal.setUserParentAdministrativeFacilityStationNumber(kajLoginDivParentStNum);
			//TODO take a stronger action if M-side and SDS-side disagree?
			if (!kajLoginDivParentStNum.equals(mLoginDivParentStNum)) {
				if (log.isErrorEnabled()) {
					StringBuffer sb = new StringBuffer("Warning: For Login Station# '");
					sb.append(kajLoginDivStNum);
					sb.append("' the SDS Administrative Parent Station# '");
					sb.append(kajLoginDivParentStNum);
					sb.append("' does not match the value returned from M: '");
					sb.append(mLoginDivParentStNum);
					sb.append("'.");
					log.error(sb.toString());
				}
			}
		}

		//Vista Provider Station #
		//TODO take a stronger action if M-side and SDS-side disagree?
		{
			String mLoginDivVistaProviderStNum = st.nextToken(); //11
			returnVal.setUserParentComputerSystemStationNumber(kajLoginDivVistaProviderStNum);
			if (!kajLoginDivVistaProviderStNum.equals(mLoginDivVistaProviderStNum)) {
				if (log.isErrorEnabled()) {
					StringBuffer sb = new StringBuffer("Warning: For Login Station# '");
					sb.append(kajLoginDivStNum);
					sb.append("' the SDS VistaProvider Station# '");
					sb.append(kajLoginDivVistaProviderStNum);
					sb.append("' does not match the value returned from M: '");
					sb.append(mLoginDivVistaProviderStNum);
					sb.append("'.");
					log.error(sb.toString());
				}
			}
		}

		// store signon log IEN
		returnVal.setSignonLogIen(st.nextToken());

		// Process permitted divisions from New Person file			
		TreeMap<String, VistaDivisionVO> newPersonDivisions = new TreeMap<String, VistaDivisionVO>();
		int divCount = Integer.parseInt(st.nextToken()); //13
		for (int i = 0; i < divCount; i++) { //14-n
			VistaDivisionVO foundDivision = getDivisionInfo(st.nextToken());
			if (retrieveNewPersonDivisions) {
				if (log.isDebugEnabled()) {
					log.debug("Found division: " + foundDivision.toString());
				}
				newPersonDivisions.put(foundDivision.getNumber(), foundDivision);
			}
		}

		// put New Persion division list in LoginUserInfoVO object depending on KAAJEE site setting
		if (retrieveNewPersonDivisions) {

		/*	 filter out divisions not in KAAJEE division list
		 //comparing  sub divisions with configuration station number, if it won't match take out subdivision
			LoginUserInfoDivisionFilters.isDivisionConfiguredInKaajeeFilter(
				kajLoginDivStNum,
				newPersonDivisions,
				returnVal.getUserDuz(),
				configVO);
            */
			// filter out divisions not sharing same computing facility as the KAAJEE login division
			LoginUserInfoDivisionFilters.sameVistaProviderFilter(
				kajLoginDivStNum,
				newPersonDivisions,
				returnVal.getUserDuz());

			// add to the return value object
			returnVal.setPermittedDivisons(newPersonDivisions);
		}

		// if KAAJEE site settings says to populate the "computing facility divisions' portion of the
		//   LoginUserInfoVO object, then do so.
		if (retrieveComputingFacilityDivisions) {

			TreeMap<String, VistaDivisionVO> cfDivisionsMap = getComputingFacilityDivisions(kajLoginDivVistaProviderInst, kajLoginDivInst);
			returnVal.setLoginDivisionVistaProviderDivisions(cfDivisionsMap);
		}
		
        // OIFO/AC -- Add code to extract VPID using Kernel User Demographics RPC:
        String rpcResults2 = null;

        try
        {
            RpcRequest vReq2 = RpcRequestFactory.getRpcRequest();

            vReq2.setRpcContext(KaajeeLoginModule.LOGIN_RPC_CONTEXT);
            vReq2.setRpcClientTimeOut(600);
            // setTimeOut (in milliseconds) is passed to M and enforced on the M side
            vReq2.setTimeOut(5000);
            vReq2.setUseProprietaryMessageFormat(true);
            vReq2.setRpcName(KERNEL_USER_INFO_RPC);

            // vReq2.getParams().setParam(1, "string", clientIp);
            // vReq2.getParams().setParam(2, "string,", ConfigurationVO.getInstance().getHostApplicationName());
            RpcResponse vResp2 = myConnection.executeRPC(vReq2);

            rpcResults2 = vResp2.getResults();
            
        } catch (SecurityUserAuthorizationException e) {

			String exceptionString =
				"Authorization failed for your user account on the M system; could not log you on.<br>&nbsp;<br>Please contact your site manager for assistance.<br>&nbsp;<br>More details below:<br>&nbsp;<br>";
			throw new KaajeeException(exceptionString, e);

		} catch (SecurityTooManyInvalidLoginAttemptsFaultException e) {

			StringBuffer sb =
				new StringBuffer("Login failed due to too many invalid login attempts.<br>&nbsp;<br>Please contact your site manager for assistance.<br>&nbsp;<br>More details below:<br>&nbsp;<br>");
			sb.append(e.getMessage());
			if (log.isErrorEnabled()) {
				log.error(sb.toString(), e);
			}
			throw new KaajeeException(sb.toString(), e);

		} catch (SecurityUserVerifyCodeException e) {

			String exceptionString =
				"Your verify code has expired or needs changing; could not log you on.<br>&nbsp;<br>Please use another application to change your verify code and then try to log on again here. Or, contact your site manager for assistance.";
			throw new KaajeeException(exceptionString);

		} catch (SecurityAccessVerifyCodePairInvalidException e) {

			String exceptionString = "Not a valid ACCESS CODE/VERIFY CODE pair.";
			throw new KaajeeException(exceptionString);

		} catch (LoginsDisabledFaultException e) {

			String exceptionString = "Logins are disabled on the M system.";
			throw new KaajeeException(exceptionString);

		} catch (SecurityIdentityDeterminationFaultException e) {

			StringBuffer sb =
				new StringBuffer("Could not match you with your M account; could not log you on.<br>&nbsp;<br>Please contact your site manager for assistance.<br>&nbsp;<br>More details below:<br>&nbsp;<br>");
			sb.append(e.getMessage());
			if (log.isErrorEnabled()) {
				log.error(sb.toString(), e);
			}
			throw new KaajeeException(sb.toString(), e);

		} catch (SecurityDivisionDeterminationFaultException e) {

			StringBuffer sb =
				new StringBuffer("The institution/division you selected for login is not valid for your M user account; could not log you on.<br>&nbsp;<br>Please contact your site manager for assistance.<br>&nbsp;<br>More details below:<br>&nbsp;<br>");
			sb.append(e.getMessage());
			if (log.isErrorEnabled()) {
				log.error(sb.toString(), e);
			}
			throw new KaajeeException(sb.toString(), e);

		} catch (FoundationsException e) {

			StringBuffer sb =
				new StringBuffer("Error logging on or retrieving user information; could not log you on.<br>&nbsp;<br>Please contact your site manager for assistance.<br>&nbsp;<br>More details below:<br>&nbsp;<br>");
			sb.append(e.getMessage());
			if (log.isErrorEnabled()) {
				log.error(sb.toString(), e);
			}
			throw new KaajeeException(sb.toString(), e);

		}

        log.debug("XUS GET USER INFO results:\n" + rpcResults2); //$NON-NLS-1$

        String[] sToks = rpcResults2.split("\\n");        //$NON-NLS-1$
        for (int i=0; i < sToks.length; i++)
        {
            String value = sToks[i];
            
            switch (i)
            {
            	case 0:
            	    // Result(0) is the users DUZ.
            	    break;
            	case 1:
            	    // Result(1) is the user name from the .01 field.
            	    break;            	    
            	case 2:
            	    // Result(2) is the users full name from the name standard file.
            	    break;
            	case 3:
            	    // Result(3) is data about the division that the user is working in.
                    // IEN of file 4^Station Name^Station Number
            	    String val[] = value.split("\\^"); //$NON-NLS-1$
            	    returnVal.setDivisionName(val[1]);
            	    returnVal.setDivisionIEN(value.replace('^', '|'));
            	    break;
            	case 4:
            	    // Result(4) is the users Title.
            	    returnVal.setUserTitle(value);
            	    break;
            	case 5:
            	    // Result(5) is the Service/Section.
            	    returnVal.setUserServiceSection(value);
            	    break;
            	case 6:
            	    // Result(6) is the users langage choice.
            	    returnVal.setLanguage(value);
            	    break;
            	case 7:
            	    // Result(7) is the users DTIME value.
            	    returnVal.setDateTime(value);
            	    break;     
            	case 8:
            	    // Result(8) is the users VPID value.
            	    returnVal.setUserVpid(value);
            	    break;
            }                                    
        }

		tidyUpNullNameValues(returnVal);
		return returnVal;
	}

	/**
	 * Create/populate a LoginUserInfoVO object with the user demographics information for this user.
	 * Calls XUS KAAJEE GET USER VIA PROXY rpc to get most of this information.
	 * @param myConnection VistaLink connection
	 * @param kajLoginDivStNum Station number chosen by the user at KAAJEE login
	 * @param clientIp client IP address, used to mark the sign-on log
	 * @param vistaToken
	 * @return a LoginUserInfoVO object containing user demographics information for this user.
	 * @throws FoundationsException thrown if any errors encountered retrieving the user demographics info.
	 */
	static LoginUserInfoVO getLoginUserInfoCCOW(VistaLinkConnection myConnection, String kajLoginDivStNum, String clientIp, String vistaToken)
	throws KaajeeInstitutionResourceException, KaajeeException {
	LoginUserInfoVO returnVal = null;

	ConfigurationVO configVO = ConfigurationVO.getInstance();
	boolean retrieveNewPersonDivisions = configVO.getRetrieveNewPersonDivisions();
	boolean retrieveComputingFacilityDivisions = configVO.getRetrieveComputingFacilityDivisions();

	// get div info for kaajee login division and Vista Provider institution
	Institution kajLoginDivInst = null;
	try {
		kajLoginDivInst = Institution.factory.obtainByStationNumber(kajLoginDivStNum);
	} catch (Throwable t) {
		StringBuffer sb = new StringBuffer("Error retrieving institution. ");
		sb.append(t.getMessage());
		// sb.append(t.getStackTrace());
		throw new KaajeeInstitutionResourceException(sb.toString());
	}
	Institution kajLoginDivVistaProviderInst =
		getLoginDivisionVistaProviderInstitution(kajLoginDivStNum, kajLoginDivInst);
	String kajLoginDivVistaProviderStNum =
		getLoginDivisionVistaProviderStationNumber(kajLoginDivStNum, kajLoginDivVistaProviderInst);

	String rpcResults = null;

	try {

		RpcRequest vReq = RpcRequestFactory.getRpcRequest();
		vReq.setRpcContext("XUS KAAJEE PROXY LOGON");
		vReq.setRpcClientTimeOut(600);
		vReq.setUseProprietaryMessageFormat(true);
		vReq.setRpcName(USER_INFO_PROXY_RPC);
		vReq.getParams().setParam(1, "string", clientIp);
		vReq.getParams().setParam(2, "string,", ConfigurationVO.getInstance().getHostApplicationName());
		
        vistaToken = VistaKernelHash.encrypt("~~TOK~~" + vistaToken, true);        
        vReq.getParams().setParam(3, "string", vistaToken);
        
		RpcResponse vResp = myConnection.executeRPC(vReq);
		rpcResults = vResp.getResults();

	} catch (SecurityUserAuthorizationException e) {

		String exceptionString =
			"Authorization failed for your user account on the M system; could not log you on.<br>&nbsp;<br>Please contact your site manager for assistance.<br>&nbsp;<br>More details below:<br>&nbsp;<br>";
		throw new KaajeeException(exceptionString, e);

	} catch (SecurityTooManyInvalidLoginAttemptsFaultException e) {

		StringBuffer sb =
			new StringBuffer("Login failed due to too many invalid login attempts.<br>&nbsp;<br>Please contact your site manager for assistance.<br>&nbsp;<br>More details below:<br>&nbsp;<br>");
		sb.append(e.getMessage());
		if (log.isErrorEnabled()) {
			log.error(sb.toString(), e);
		}
		throw new KaajeeException(sb.toString(), e);

	} catch (SecurityUserVerifyCodeException e) {

		String exceptionString =
			"Your verify code has expired or needs changing; could not log you on.<br>&nbsp;<br>Please use another application to change your verify code and then try to log on again here. Or, contact your site manager for assistance.";
		throw new KaajeeException(exceptionString);

	} catch (SecurityAccessVerifyCodePairInvalidException e) {

		String exceptionString = "Not a valid ACCESS CODE/VERIFY CODE pair.";
		throw new KaajeeException(exceptionString);

	} catch (LoginsDisabledFaultException e) {

		String exceptionString = "Logins are disabled on the M system.";
		throw new KaajeeException(exceptionString);

	} catch (SecurityIdentityDeterminationFaultException e) {

		StringBuffer sb =
			new StringBuffer("Could not match you with your M account; could not log you on.<br>&nbsp;<br>Please contact your site manager for assistance.<br>&nbsp;<br>More details below:<br>&nbsp;<br>");
		sb.append(e.getMessage());
		if (log.isErrorEnabled()) {
			log.error(sb.toString(), e);
		}
		throw new KaajeeException(sb.toString(), e);

	} catch (SecurityDivisionDeterminationFaultException e) {

		StringBuffer sb =
			new StringBuffer("The institution/division you selected for login is not valid for your M user account; could not log you on.<br>&nbsp;<br>Please contact your site manager for assistance.<br>&nbsp;<br>More details below:<br>&nbsp;<br>");
		sb.append(e.getMessage());
		if (log.isErrorEnabled()) {
			log.error(sb.toString(), e);
		}
		throw new KaajeeException(sb.toString(), e);

	} catch (FoundationsException e) {

		StringBuffer sb =
			new StringBuffer("Error logging on or retrieving user information; could not log you on.<br>&nbsp;<br>Please contact your site manager for assistance.<br>&nbsp;<br>More details below:<br>&nbsp;<br>");
		sb.append(e.getMessage());
		if (log.isErrorEnabled()) {
			log.error(sb.toString(), e);
		}
		throw new KaajeeException(sb.toString(), e);

	}

	if (log.isDebugEnabled()) {
		log.debug("results of XUS KAAJEE GET USER INFO CCOW: " + rpcResults);
	}
	/*
	 * ; Result(0) is the users DUZ.
	 * ; Result(1) is the user name from the .01 field.
	 * ; Result(2) is the users full name from the name standard file.
	 * ; Result(3) is the FAMILY (LAST) NAME (or ^ if null)
	 * ; Result(4) is the GIVEN (FIRST) NAME (or ^ if null)
	 * ; Result(5) is the MIDDLE NAME (or ^ if null)
	 * ; Result(6) is the PREFIX (or ^ if null)
	 * ; Result(7) is the SUFFIX (or ^ if null)
	 * ; Result(8) is the DEGREE (or ^ if null)
	 * ; Result(9) is station # of the division that the user is working in.
	 * ; Result(10) is the station # of the parent facility for the login division
	 * ; Result(11) is the station # of the computer system "parent" from the KSP file.
	 * ; Result(12) is the IEN of the signon log entry
	 * ; Result(13) = # of permissible divisions
	 * ; Result(14-n) are the permissible divisions for user login, in the format:
	 * ;           IEN of file 4^Station Name^Station Number^default? (1 or 0)
	 */

	returnVal = new LoginUserInfoVO();
	StringTokenizer st = new StringTokenizer(rpcResults, "\n");
	returnVal.setUserDuz(st.nextToken()); //0
	returnVal.setUserName01(st.nextToken()); //1
	returnVal.setUserNameDisplay(st.nextToken()); //2
	returnVal.setUserLastName(st.nextToken()); //3
	returnVal.setUserFirstName(st.nextToken()); //4
	returnVal.setUserMiddleName(st.nextToken()); //5
	returnVal.setUserPrefix(st.nextToken()); //6
	returnVal.setUserSuffix(st.nextToken()); //7
	returnVal.setUserDegree(st.nextToken()); //8

	//Login Division #
	//TODO take a stronger action if M-side and SDS-side disagree? ABORT THE LOGIN?
	returnVal.setLoginStationNumber(kajLoginDivStNum);
	String mLoginInstitutionStationNumber = st.nextToken(); //9
	if (!kajLoginDivStNum.equals(mLoginInstitutionStationNumber)) {
		if (log.isErrorEnabled()) {
			StringBuffer sb = new StringBuffer("Warning: For User DUZ: '");
			sb.append(returnVal.getUserDuz());
			sb.append("KAAJEE login division '");
			sb.append(kajLoginDivStNum);
			sb.append("' is different than M login division DUZ(2) value '");
			sb.append(mLoginInstitutionStationNumber);
			sb.append("'.");
			log.error(sb.toString());
		}
	}

	//Parent Station #
	{
		String mLoginDivParentStNum = st.nextToken(); // 10
		String kajLoginDivParentStNum = getParentStationNumber(kajLoginDivInst, mLoginDivParentStNum);
		returnVal.setUserParentAdministrativeFacilityStationNumber(kajLoginDivParentStNum);
		//TODO take a stronger action if M-side and SDS-side disagree?
		if (!kajLoginDivParentStNum.equals(mLoginDivParentStNum)) {
			if (log.isErrorEnabled()) {
				StringBuffer sb = new StringBuffer("Warning: For Login Station# '");
				sb.append(kajLoginDivStNum);
				sb.append("' the SDS Administrative Parent Station# '");
				sb.append(kajLoginDivParentStNum);
				sb.append("' does not match the value returned from M: '");
				sb.append(mLoginDivParentStNum);
				sb.append("'.");
				log.error(sb.toString());
			}
		}
	}

	//Vista Provider Station #
	//TODO take a stronger action if M-side and SDS-side disagree?
	{
		String mLoginDivVistaProviderStNum = st.nextToken(); //11
		returnVal.setUserParentComputerSystemStationNumber(kajLoginDivVistaProviderStNum);
		if (!kajLoginDivVistaProviderStNum.equals(mLoginDivVistaProviderStNum)) {
			if (log.isErrorEnabled()) {
				StringBuffer sb = new StringBuffer("Warning: For Login Station# '");
				sb.append(kajLoginDivStNum);
				sb.append("' the SDS VistaProvider Station# '");
				sb.append(kajLoginDivVistaProviderStNum);
				sb.append("' does not match the value returned from M: '");
				sb.append(mLoginDivVistaProviderStNum);
				sb.append("'.");
				log.error(sb.toString());
			}
		}
	}

	// store signon log IEN
	returnVal.setSignonLogIen(st.nextToken());

	// Process permitted divisions from New Person file			
	TreeMap<String, VistaDivisionVO> newPersonDivisions = new TreeMap<String, VistaDivisionVO>();
	int divCount = Integer.parseInt(st.nextToken()); //13
	for (int i = 0; i < divCount; i++) { //14-n
		VistaDivisionVO foundDivision = getDivisionInfo(st.nextToken());
		if (retrieveNewPersonDivisions) {
			if (log.isDebugEnabled()) {
				log.debug("Found division: " + foundDivision.toString());
			}
			newPersonDivisions.put(foundDivision.getNumber(), foundDivision);
		}
	}

	// put New Persion division list in LoginUserInfoVO object depending on KAAJEE site setting
	if (retrieveNewPersonDivisions) {

	/*	 filter out divisions not in KAAJEE division list
	 //comparing  sub divisions with configuration station number, if it won't match take out subdivision
		LoginUserInfoDivisionFilters.isDivisionConfiguredInKaajeeFilter(
			kajLoginDivStNum,
			newPersonDivisions,
			returnVal.getUserDuz(),
			configVO);
        */
		// filter out divisions not sharing same computing facility as the KAAJEE login division
		LoginUserInfoDivisionFilters.sameVistaProviderFilter(
			kajLoginDivStNum,
			newPersonDivisions,
			returnVal.getUserDuz());

		// add to the return value object
		returnVal.setPermittedDivisons(newPersonDivisions);
	}

	// if KAAJEE site settings says to populate the "computing facility divisions' portion of the
	//   LoginUserInfoVO object, then do so.
	if (retrieveComputingFacilityDivisions) {

		TreeMap<String, VistaDivisionVO> cfDivisionsMap = getComputingFacilityDivisions(kajLoginDivVistaProviderInst, kajLoginDivInst);
		returnVal.setLoginDivisionVistaProviderDivisions(cfDivisionsMap);
	}
	
    // OIFO/AC -- Add code to extract VPID using Kernel User Demographics RPC:
    String rpcResults2 = null;
    VistaLinkConnection myConnection2 = null;

    try
    {
    	VistaLinkDuzConnectionSpec connSpec = new VistaLinkDuzConnectionSpec(kajLoginDivStNum, returnVal.getUserDuz());
    	myConnection2 = LoginControllerUtils.getVistaLinkConnection(kajLoginDivStNum,connSpec);
    	RpcRequest vReq2 = RpcRequestFactory.getRpcRequest();

        vReq2.setRpcContext(KaajeeLoginModule.LOGIN_RPC_CONTEXT);
        vReq2.setRpcClientTimeOut(600);
        // setTimeOut (in milliseconds) is passed to M and enforced on the M side
        vReq2.setTimeOut(5000);
        vReq2.setUseProprietaryMessageFormat(true);
        vReq2.setRpcName(KERNEL_USER_INFO_RPC);

        // vReq2.getParams().setParam(1, "string", clientIp);
        // vReq2.getParams().setParam(2, "string,", ConfigurationVO.getInstance().getHostApplicationName());
        RpcResponse vResp2 = myConnection2.executeRPC(vReq2);

        rpcResults2 = vResp2.getResults();
        
    } catch (SecurityUserAuthorizationException e) {

		String exceptionString =
			"Authorization failed for your user account on the M system; could not log you on.<br>&nbsp;<br>Please contact your site manager for assistance.<br>&nbsp;<br>More details below:<br>&nbsp;<br>";
		throw new KaajeeException(exceptionString, e);

	} catch (SecurityTooManyInvalidLoginAttemptsFaultException e) {

		StringBuffer sb =
			new StringBuffer("Login failed due to too many invalid login attempts.<br>&nbsp;<br>Please contact your site manager for assistance.<br>&nbsp;<br>More details below:<br>&nbsp;<br>");
		sb.append(e.getMessage());
		if (log.isErrorEnabled()) {
			log.error(sb.toString(), e);
		}
		throw new KaajeeException(sb.toString(), e);

	} catch (SecurityUserVerifyCodeException e) {

		String exceptionString =
			"Your verify code has expired or needs changing; could not log you on.<br>&nbsp;<br>Please use another application to change your verify code and then try to log on again here. Or, contact your site manager for assistance.";
		throw new KaajeeException(exceptionString);

	} catch (SecurityAccessVerifyCodePairInvalidException e) {

		String exceptionString = "Not a valid ACCESS CODE/VERIFY CODE pair.";
		throw new KaajeeException(exceptionString);

	} catch (LoginsDisabledFaultException e) {

		String exceptionString = "Logins are disabled on the M system.";
		throw new KaajeeException(exceptionString);

	} catch (SecurityIdentityDeterminationFaultException e) {

		StringBuffer sb =
			new StringBuffer("Could not match you with your M account; could not log you on.<br>&nbsp;<br>Please contact your site manager for assistance.<br>&nbsp;<br>More details below:<br>&nbsp;<br>");
		sb.append(e.getMessage());
		if (log.isErrorEnabled()) {
			log.error(sb.toString(), e);
		}
		throw new KaajeeException(sb.toString(), e);

	} catch (SecurityDivisionDeterminationFaultException e) {

		StringBuffer sb =
			new StringBuffer("The institution/division you selected for login is not valid for your M user account; could not log you on.<br>&nbsp;<br>Please contact your site manager for assistance.<br>&nbsp;<br>More details below:<br>&nbsp;<br>");
		sb.append(e.getMessage());
		if (log.isErrorEnabled()) {
			log.error(sb.toString(), e);
		}
		throw new KaajeeException(sb.toString(), e);

	} catch (FoundationsException e) {

		StringBuffer sb =
			new StringBuffer("Error logging on or retrieving user information; could not log you on.<br>&nbsp;<br>Please contact your site manager for assistance.<br>&nbsp;<br>More details below:<br>&nbsp;<br>");
		sb.append(e.getMessage());
		if (log.isErrorEnabled()) {
			log.error(sb.toString(), e);
		}
		throw new KaajeeException(sb.toString(), e);

	}
    finally
    { 
    	LoginControllerUtils.closeVistaLinkConnection(myConnection2);
    }


    log.debug("XUS GET USER INFO CCOW results:\n" + rpcResults2); //$NON-NLS-1$

    String[] sToks = rpcResults2.split("\\n");        //$NON-NLS-1$
    for (int i=0; i < sToks.length; i++)
    {
        String value = sToks[i];
        
        switch (i)
        {
        	case 0:
        	    // Result(0) is the users DUZ.
        	    break;
        	case 1:
        	    // Result(1) is the user name from the .01 field.
        	    break;            	    
        	case 2:
        	    // Result(2) is the users full name from the name standard file.
        	    break;
        	case 3:
        	    // Result(3) is data about the division that the user is working in.
                // IEN of file 4^Station Name^Station Number
        	    String val[] = value.split("\\^"); //$NON-NLS-1$
        	    returnVal.setDivisionName(val[1]);
        	    returnVal.setDivisionIEN(value.replace('^', '|'));
        	    break;
        	case 4:
        	    // Result(4) is the users Title.
        	    returnVal.setUserTitle(value);
        	    break;
        	case 5:
        	    // Result(5) is the Service/Section.
        	    returnVal.setUserServiceSection(value);
        	    break;
        	case 6:
        	    // Result(6) is the users langage choice.
        	    returnVal.setLanguage(value);
        	    break;
        	case 7:
        	    // Result(7) is the users DTIME value.
        	    returnVal.setDateTime(value);
        	    break;     
        	case 8:
        	    // Result(8) is the users VPID value.
        	    returnVal.setUserVpid(value);
        	    break;
        }                                    
    }

	tidyUpNullNameValues(returnVal);
	return returnVal;
}

	/**
	 * Create a TreeMap of VistaDivisionVO objects keyed on station #, corresponding to all divisions sharing
	 * the same VistA computing facility as the login division.
	 * @param kajLoginDivVistaProviderInst Institution object for the computing provider of the login division
	 * @param kajLoginDivStNum login division station #
	 * @return Treemap of divisions
	 * @throws KaajeeException
	 */
	private static TreeMap<String, VistaDivisionVO> getComputingFacilityDivisions(
		Institution kajLoginDivVistaProviderInst,
		Institution kajLoginDivInst)
		throws KaajeeException {

		TreeMap<String, VistaDivisionVO> cfDivisionsMap = new TreeMap<String, VistaDivisionVO>();
		Institution[] instList = kajLoginDivVistaProviderInst.getChildren();
		// getSiblings() ?
		// getParent()?
		// check if parents are all the same?

		if (log.isDebugEnabled()) {
			StringBuffer sb = new StringBuffer("Retrieved ");
			sb.append(instList.length);
			sb.append(" child divisions for Login Division ");
			sb.append(kajLoginDivInst.getStationNumber());
			sb.append("'s Computing Provider system ");
			sb.append(kajLoginDivVistaProviderInst.getStationNumber());
			sb.append(".");
			log.debug(sb.toString());
		}

		// process the retrieved child divisions
		for (int i = 0; i < instList.length; i++) {
			if (log.isDebugEnabled()) {
				StringBuffer sb = new StringBuffer("child division station #: ");
				sb.append(instList[i].getStationNumber());
				sb.append(" name: ");
				sb.append(instList[i].getName());
				log.debug(sb.toString());
			}
			String stNum = instList[i].getStationNumber();
			String stName = instList[i].getName();
			Institution instVistaProvider = instList[i].getVistaProvider();

			// we got legitimate values
			if ((instVistaProvider != null) && (stNum != null) && (stName != null)) {

				String stVistaProviderStNum = instVistaProvider.getStationNumber();
				if (stVistaProviderStNum == null) {
					stVistaProviderStNum = "";
				}
				// check if computing facility the same as that of the KAAJEE login division
				if (!(kajLoginDivVistaProviderInst.getStationNumber()).equals(stVistaProviderStNum)) {

					if (log.isDebugEnabled()) {
						StringBuffer sb =
							new StringBuffer("Filtered out child division because its computing facility '");
						sb.append(stVistaProviderStNum);
						sb.append("' does not match the computing facility '");
						sb.append(kajLoginDivVistaProviderInst.getStationNumber());
						sb.append("' of the login division ");
						sb.append(kajLoginDivInst.getStationNumber());
						log.debug(sb.toString());
					}

				} else {

					// passed all the tests
					VistaDivisionVO newDiv = new VistaDivisionVO();
					newDiv.setNumber(stNum);
					newDiv.setName(stName);
					cfDivisionsMap.put(stNum, newDiv);
				}
			}
		}

		// add in login division if not there
		if (!cfDivisionsMap.containsKey(kajLoginDivInst.getStationNumber())) {
			VistaDivisionVO loginDiv = new VistaDivisionVO();
			loginDiv.setNumber(kajLoginDivInst.getStationNumber());
			loginDiv.setName(kajLoginDivInst.getName());
			cfDivisionsMap.put(kajLoginDivInst.getStationNumber(), loginDiv);
		}
		return cfDivisionsMap;
	}

	/**
	 * Creates a VistaDivisionVO object, given an up-arrow-delimited string.
	 * @param rawDivInfo name^station number^default (1 if default, anything else if not)
	 * @return a VistaDivisionVO object
	 */
	private static VistaDivisionVO getDivisionInfo(String rawDivInfo) {
		// TODO throw KaajeeException if doesn't parse right? RegExp check?
		VistaDivisionVO returnVal = new VistaDivisionVO();
		int pos1 = rawDivInfo.indexOf('^');
		int pos2 = rawDivInfo.indexOf('^', pos1 + 1);
		int pos3 = rawDivInfo.indexOf('^', pos2 + 1);
		returnVal.setName(rawDivInfo.substring(pos1 + 1, pos2));
		returnVal.setNumber(rawDivInfo.substring(pos2 + 1, pos3));
		if ("1".equals(rawDivInfo.substring(pos3 + 1, rawDivInfo.length()))) {
			returnVal.setIsDefault(true);
		}

		return returnVal;
	}

	/**
	 * Determine the parent station #.
	 * If found in SDS lookup of login institution, use that.
	 * If SDS returns null, the login division may be the parent institution.
	 * If M's view of the parent institution and the login division agree, and the SDS says the login division
	 * has child divisions, then return the login division as the parent institution, too.
	 * @param kajLoginDivInst Institution for the login division
	 * @param loginStationNumber KAAJEE login station number
	 * @param mParentStationNumber
	 * @return
	 */
	private static String getParentStationNumber(Institution kajLoginDivInst, String mParentStationNumber) {
		String returnVal = "";
		String kajLoginDivParentStNum = "";

		//TODO take stronger actions if M-side and SDS-side disagree?

		// get the SDS view of the parent institution #
		Institution kajLoginDivParentInst = kajLoginDivInst.getParent();
		if (kajLoginDivParentInst != null) {

			kajLoginDivParentStNum = kajLoginDivParentInst.getStationNumber();
			if ((kajLoginDivParentStNum != null) && (kajLoginDivParentStNum.length() > 0)) {

				// we got an explicit return value from SDS.
				returnVal = kajLoginDivParentStNum;
				if (!returnVal.equals(mParentStationNumber)) {
					if (log.isErrorEnabled()) {
						StringBuffer sb = new StringBuffer("Warning: for the login division '");
						sb.append(kajLoginDivInst.getStationNumber());
						sb.append("' the SDS Parent Division# '");
						sb.append(kajLoginDivParentStNum);
						sb.append("' is different than the parent division returned from M, which is: '");
						sb.append(mParentStationNumber);
						sb.append("'. Using the SDS Parent Division.");
						log.error(sb.toString());
					}
				}

			} else {

				// if SDS parent not found, it may be that the login division is itself the parent
				// if SDS says the login division has childrent, and if M says the parent is the login division
				// then use this value.
				if (((kajLoginDivInst.getStationNumber()).equals(mParentStationNumber))
					&& ((kajLoginDivInst.getChildren()).length > 0)) {

					returnVal = kajLoginDivInst.getStationNumber();

				} else {

					if (true) {
						if (log.isErrorEnabled()) {
							StringBuffer sb =
								new StringBuffer("Could not determine parent station number for login station# '");
							sb.append(kajLoginDivInst.getStationNumber());
							sb.append("' the SDS Parent Division# '");
							sb.append(kajLoginDivParentStNum);
							sb.append(
								"' has no children, or the login division is different than the parent division returned from M, which is: '");
							sb.append(mParentStationNumber);
							sb.append("'.");
							log.error(sb.toString());
						}
					}
				}
			}
		}

		return returnVal;
	}

	private static void tidyUpNullNameValues(LoginUserInfoVO userInfo) {
		// tidy up if null values are returned (returned as '^' characters so tokenizer doesn't skip)
		if (userInfo.getUserLastName().equals("^")) {
			userInfo.setUserLastName("");
		}
		if (userInfo.getUserFirstName().equals("^")) {
			userInfo.setUserFirstName("");
		}
		if (userInfo.getUserMiddleName().equals("^")) {
			userInfo.setUserMiddleName("");
		}
		if (userInfo.getUserPrefix().equals("^")) {
			userInfo.setUserPrefix("");
		}
		if (userInfo.getUserSuffix().equals("^")) {
			userInfo.setUserSuffix("");
		}
		if (userInfo.getUserDegree().equals("^")) {
			userInfo.setUserDegree("");
		}
	}

	@SuppressWarnings("unused")
	private static Institution getLoginDivisionVistaProviderInstitution(
		String loginStationNumber,
		Institution kajLoginDivInst)
		throws KaajeeException {
		Institution kajLoginDivVistaProviderInst = kajLoginDivInst.getVistaProvider();
		if (kajLoginDivInst == null) {
			StringBuffer sb =
				new StringBuffer("Vista Provider institution could not be retrieved for login division: '");
			sb.append(loginStationNumber);
			sb.append("'.");
			throw new KaajeeException(sb.toString());
		}
		return kajLoginDivVistaProviderInst;
	}

	private static String getLoginDivisionVistaProviderStationNumber(
		String loginStationNumber,
		Institution loginDivisionVistaProviderInst)
		throws KaajeeException {
		String kajLoginDivVistaProviderStNum = loginDivisionVistaProviderInst.getStationNumber();
		if ((kajLoginDivVistaProviderStNum == null) || (kajLoginDivVistaProviderStNum.length() < 1)) {
			StringBuffer sb =
				new StringBuffer("Vista Provider station # could not be identified for login division: '");
			sb.append(loginStationNumber);
			sb.append("'.");
			throw new KaajeeException(sb.toString());
		}
		return kajLoginDivVistaProviderStNum;
	}
}
