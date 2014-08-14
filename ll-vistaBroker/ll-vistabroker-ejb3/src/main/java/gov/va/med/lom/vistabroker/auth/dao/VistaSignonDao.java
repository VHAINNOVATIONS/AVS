package gov.va.med.lom.vistabroker.auth.dao;

import gov.va.med.exception.FoundationsException;
import gov.va.med.lom.vistabroker.auth.data.ChangeVerifyCodeResult;
import gov.va.med.lom.vistabroker.auth.data.LoginUserInfo;
import gov.va.med.lom.vistabroker.auth.data.VistaSignonResult;
import gov.va.med.lom.vistabroker.dao.BaseDao;
import gov.va.med.lom.vistabroker.exception.VistaBrokerConnectionException;
import gov.va.med.lom.vistabroker.exception.VistaBrokerSecurityException;
import gov.va.med.lom.vistabroker.util.Hash;
import gov.va.med.vistalink.adapter.cci.VistaLinkConnection;
import gov.va.med.vistalink.adapter.cci.VistaLinkConnectionFactory;
import gov.va.med.vistalink.adapter.cci.VistaLinkConnectionSpec;
import gov.va.med.vistalink.institution.InstitutionMappingDelegate;
import gov.va.med.vistalink.rpc.RpcRequest;
import gov.va.med.vistalink.rpc.RpcRequestFactory;
import gov.va.med.vistalink.rpc.RpcResponse;
import gov.va.med.vistalink.security.m.SecurityAccessVerifyCodePairInvalidException;
import gov.va.med.vistalink.security.m.SecurityDivisionDeterminationFaultException;
import gov.va.med.vistalink.security.m.SecurityIdentityDeterminationFaultException;
import gov.va.med.vistalink.security.m.SecurityTooManyInvalidLoginAttemptsFaultException;
import gov.va.med.vistalink.security.m.SecurityUserAuthorizationException;
import gov.va.med.vistalink.security.m.SecurityUserVerifyCodeException;

import java.util.List;
import java.util.StringTokenizer;

import javax.naming.InitialContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;



public class VistaSignonDao extends BaseDao {
  
  private static final Log log = LogFactory.getLog(VistaSignonDao.class);
			
  // CONSTRUCTORS
  public VistaSignonDao() {
    super();
  }
  
  public VistaSignonDao(BaseDao baseDao) {
    super(baseDao);
  }
  
  @SuppressWarnings("unused")
  public VistaSignonResult doVistaSignon(String division, String applicationName, 
		  String av, String clientIp) throws Exception {
	  
	  VistaLinkConnection conn = null;
	  
	  try {
		VistaSignonResult result = new VistaSignonResult();
		String LOGIN_RPC_CONTEXT = "XUS KAAJEE WEB LOGON";
		String getUserInfoRpcName = "XUS KAAJEE GET USER INFO";
		LoginUserInfo loginUserInfo;
		String rpcResults = null;
		
		VistaLinkConnectionSpec connSpec = new VistaBrokerVistaLinkConnectionSpec(
				division, av, "", clientIp);
		String jndiName = "";
		if (connSpec != null) {

			try {
				InitialContext ic = new InitialContext();
				jndiName = "java:/"
						+ InstitutionMappingDelegate
								.getJndiConnectorNameForInstitution(division);
				VistaLinkConnectionFactory cf = (VistaLinkConnectionFactory) ic
						.lookup(jndiName);
				conn = (VistaLinkConnection) cf.getConnection(connSpec);
			} catch (Exception e) {
				log.error(e);
				throw new VistaBrokerConnectionException(e);
			}
		} else {
			throw new VistaBrokerSecurityException(null, INVALID_SECURITY_PARAMS,
					"Invalid Division, DUZ, VPID, App Proxy, or CCOW handle.",
					"", "", division, "");
		}
		try {

			RpcRequest vReq = RpcRequestFactory.getRpcRequest();
			vReq.setRpcContext(LOGIN_RPC_CONTEXT);
			vReq.setRpcClientTimeOut(600);
			vReq.setRpcName(getUserInfoRpcName);
			vReq.getParams().setParam(1, "string", clientIp);
			vReq.getParams().setParam(2, "string,", applicationName);
			RpcResponse vResp = conn.executeRPC(vReq);
			rpcResults = vResp.getResults();

		} catch (SecurityUserAuthorizationException e) {

			String exceptionString = "Authorization failed for your user account on the M system; could not log you on.  Please contact your site manager for assistance.";
			result.setMessage(exceptionString);
			return result;

		} catch (SecurityTooManyInvalidLoginAttemptsFaultException e) {

			StringBuffer sb = new StringBuffer(
					"Login failed due to too many invalid login attempts.<br>&nbsp;<br>Please contact your site manager for assistance.<br>&nbsp;<br>More details below:<br>&nbsp;<br>");
			sb.append(e.getMessage());
			log.error(sb.toString(), e);

			result.setMessage(sb.toString());
			return result;

		} catch (SecurityUserVerifyCodeException e) {

			String exceptionString = "Your verify code has expired or needs changing; could not log you on.<br>&nbsp;<br>Please use another application to change your verify code and then try to log on again here. Or, contact your site manager for assistance.";
			result.setMessage(exceptionString);
			return result;

		} catch (SecurityAccessVerifyCodePairInvalidException e) {

			String exceptionString = "Not a valid ACCESS CODE/VERIFY CODE pair.";
			result.setMessage(exceptionString);
			return result;

		} catch (SecurityIdentityDeterminationFaultException e) {

			StringBuffer sb = new StringBuffer(
					"Could not match you with your M account; could not log you on.<br>&nbsp;<br>Please contact your site manager for assistance.<br>&nbsp;<br>More details below:<br>&nbsp;<br>");
			sb.append(e.getMessage());
			log.error(sb.toString(), e);

			result.setMessage(sb.toString());
			return result;

		} catch (SecurityDivisionDeterminationFaultException e) {

			StringBuffer sb = new StringBuffer(
					"The institution/division you selected for login is not valid for your M user account; could not log you on.<br>&nbsp;<br>Please contact your site manager for assistance.<br>&nbsp;<br>More details below:<br>&nbsp;<br>");
			sb.append(e.getMessage());
			log.error(sb.toString(), e);

			result.setMessage(sb.toString());
			return result;

		} catch (FoundationsException e) {

			StringBuffer sb = new StringBuffer(
					"Error logging on or retrieving user information; could not log you on.<br>&nbsp;<br>Please contact your site manager for assistance.<br>&nbsp;<br>More details below:<br>&nbsp;<br>");
			sb.append(e.getMessage());
			log.error(sb.toString(), e);

			result.setMessage(sb.toString());
			return result;

		}
		loginUserInfo = new LoginUserInfo();
		StringTokenizer st = new StringTokenizer(rpcResults, "\n");
		loginUserInfo.setUserDuz(st.nextToken()); //0
		loginUserInfo.setUserName01(st.nextToken()); //1
		loginUserInfo.setUserNameDisplay(st.nextToken()); //2
		loginUserInfo.setUserLastName(st.nextToken()); //3
		loginUserInfo.setUserFirstName(st.nextToken()); //4
		loginUserInfo.setUserMiddleName(st.nextToken()); //5
		loginUserInfo.setUserPrefix(st.nextToken()); //6
		loginUserInfo.setUserSuffix(st.nextToken()); //7
		loginUserInfo.setUserDegree(st.nextToken()); //8
		//Login Division #
		//TODO take a stronger action if M-side and SDS-side disagree? ABORT THE LOGIN?
		loginUserInfo.setLoginStationNumber(division);
		String mLoginInstitutionStationNumber = st.nextToken(); //9
		String mLoginDivParentStNum = st.nextToken(); // 10
		String mLoginDivVistaProviderStNum = st.nextToken(); //11
		loginUserInfo.setSignonLogIen(st.nextToken());
		result.setSignonSucceeded(true);
		result.setDuz(loginUserInfo.getUserDuz());
		result.setGreeting("");
		result.setMessage("");
		return result;
	} catch (Exception e) {
		log.error("Error authenticating user." , e);
		return new VistaSignonResult();
	}finally{
		try {
			if(conn != null) conn.close();
		} catch (Exception e) {
			/* no op */
		}
	}
	
	

  }


  public ChangeVerifyCodeResult changeVerifyCode(String oldVerifyCode, String newVerifyCode,
                                                              String confirmVerifyCode) throws Exception {
    setDefaultContext("ALSI VISTA BROKER");
    setDefaultRpcName("XUS CVC");
    Object[] params = {Hash.encrypt(oldVerifyCode.toUpperCase()) + "^" +
                       Hash.encrypt(newVerifyCode.toUpperCase()) + "^" +
                       Hash.encrypt(confirmVerifyCode.toUpperCase())}; 
    List<String> list = lCall(params);
    ChangeVerifyCodeResult changeVerifyCodeResult = new ChangeVerifyCodeResult();
    if (list.size() > 0) {
      if (((String)list.get(0)).equals("0")) 
        changeVerifyCodeResult.setSuccess(true);
      else
        changeVerifyCodeResult.setSuccess(false);
      changeVerifyCodeResult.setMessage((String)list.get(1));
    } else {
      changeVerifyCodeResult.setSuccess(false);
      changeVerifyCodeResult.setMessage("Your VERIFY code was not changed.");
    }
    return changeVerifyCodeResult;
  }
  
  @SuppressWarnings("unused")
  private VistaSignonResult getVistaSignonResult(List<String> list) {
    VistaSignonResult vistaSignonResult = new VistaSignonResult();
    String duz = null;
    if (list.size() >= 6) {
      if (((String)list.get(0)).equals("0")) {
        duz = "0";
        vistaSignonResult.setSignonSucceeded(false);
      } else {
        duz = (String)list.get(0);
        vistaSignonResult.setSignonSucceeded(true);
      }  
      vistaSignonResult.setDuz(duz);
      vistaSignonResult.setDeviceLocked(((String)list.get(1)).equals("1"));
      vistaSignonResult.setChangeVerifyCode(((String)list.get(2)).equals("1"));
      vistaSignonResult.setMessage((String)list.get(3));
      StringBuffer greeting = new StringBuffer();  
      for (int i = 6; i < list.size(); i++)
        greeting.append((String)list.get(i) + '\n');
      vistaSignonResult.setGreeting(greeting.toString());
    }
    return vistaSignonResult;    
  }
  
}
