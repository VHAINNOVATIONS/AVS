package gov.va.med.lom.kaajee.jboss.security.auth;

import gov.va.med.authentication.kernel.KaajeeVistaLinkConnectionSpec;
import gov.va.med.authentication.kernel.LoginUserInfoVO;
import gov.va.med.lom.kaajee.jboss.model.KaajeeGroup;
import gov.va.med.lom.kaajee.jboss.model.KaajeePrincipal;
import gov.va.med.lom.kaajee.jboss.model.json.LoginUserInfoVOJson;
import gov.va.med.term.access.Institution;
import gov.va.med.vistalink.adapter.cci.VistaLinkConnection;
import gov.va.med.vistalink.adapter.cci.VistaLinkConnectionSpec;
import gov.va.med.vistalink.adapter.cci.VistaLinkDuzConnectionSpec;
import gov.va.med.vistalink.adapter.cci.VistaLinkCcowConnectionSpec;

import java.io.IOException;
import java.security.Principal;
import java.security.acl.Group;
import java.util.List;
import java.util.Map;

import javax.resource.ResourceException;
import javax.security.auth.Subject;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.login.LoginException;
import javax.security.jacc.PolicyContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.security.auth.spi.AbstractServerLoginModule;

public class KaajeeLoginModule extends AbstractServerLoginModule {

	 
	public static final String LOGIN_RPC_CONTEXT = "XUS KAAJEE WEB LOGON"; // Reset to original value: "XUS KAAJEE WEB LOGON";
	public static final String DEFAULT_ROLE = "AUTHENTICATED_KAAJEE_USER";
	private final static Log log = LogFactory.getLog(KaajeeLoginModule.class);	
	
	private KaajeePrincipal user;
	private List<KaajeePrincipal> roles;
	
	public void initialize(Subject subject, CallbackHandler callbackHandler,
			Map<String, ?> sharedState, Map<String, ?> options) {
		
		super.initialize(subject, callbackHandler, sharedState, options);

	}

	
	/**
	 * username callback should return site
	 * password callback should return avcode separated with semi-colon	
	 *  
	 * 
	 * (non-Javadoc)
	 * @see javax.security.auth.spi.LoginModule#login()
	 */
	 @Override
    public boolean login() throws LoginException {
	     
	     String access = null;
	     String verify = null;
	     String vistaToken = null;
	     String userDuz = null;
	     
	      loginOk = false;

        try {
            NameCallback nameCallback = new NameCallback("Username");
            PasswordCallback passwordCallback = new PasswordCallback(
                    "Password", false);

            Callback[] callbacks = new Callback[] { nameCallback,
                    passwordCallback };

            callbackHandler.handle(callbacks);

            String username = nameCallback.getName();
            String password = null;
            String institution = null;
            
            
            char[] passwordChar = passwordCallback.getPassword();
            passwordCallback.clearPassword();
            
            if(passwordChar != null){
                password = new String(passwordChar);
            }
            if(username != null){
                String split[] = username.split(";");
                institution = split[0];
            }
            
            String av = password.toString();
            
            String avSplit[] = av.split(";");
            if(avSplit.length == 2){
                access = avSplit[0];
                verify = avSplit[1];
            } else {
              if (av.startsWith("~")) {
                vistaToken = av; 
              } else {
                userDuz = av;
              }
            }

            HttpServletRequest request = (HttpServletRequest) PolicyContext
                    .getContext("javax.servlet.http.HttpServletRequest");

            LoginUserInfoVOJson userJson = 
            		processCredentialsAndLogIn(request, institution, access, verify, vistaToken, userDuz);
            
            if (userJson != null) {
	            if (userJson.isSuccess()) {
	                loginOk = true;
	                return true;
	            } else {
	                loginOk = false;
	                return false;  
	            }
            } else {
                return false;
            }
            
        } catch (Exception e) {
            log.error("Authentication Failed", e);
            return false;
        }

    }

	
    @Override
    protected Principal getIdentity() {
        log.debug("getIdentity() returning: " + user.getName());
        return user;
    }


    @Override
    protected Group[] getRoleSets() throws LoginException {
        Group[] groups = {new KaajeeGroup("Roles")};
        
        for(KaajeePrincipal role : roles){
            groups[0].addMember(role);
        }
        
        return groups;
    }
	
	
	private LoginUserInfoVOJson processCredentialsAndLogIn(
			HttpServletRequest request,
			String institution,
			String access,
			String verify,
			String vistaToken,
			String userDuz)
	throws ServletException, IOException {

	    log.debug("processCredentialsAndLogIn");
	    
		VistaLinkConnection myConnection = null;
		LoginUserInfoVO userInfo = null;
		HttpSession session = request.getSession(true);
		String clientIp = request.getRemoteAddr();

		LoginUserInfoVOJson userJson = null;
		
		try {

			// get LoginUserInfoVO if ccow VistA token was successfully validated
			userInfo = (LoginUserInfoVO) session.getAttribute(LoginUserInfoVO.SESSION_KEY);
			if (userInfo != null){
				// userInfo came from validation of ccow VistA token
				// will need a VistaLinkConnection for calls downstream
				log.debug("ccow");
				String loginStationNumber = userInfo.getLoginStationNumber();
				String duz = userInfo.getUserDuz();
				VistaLinkConnectionSpec connSpec = new VistaLinkDuzConnectionSpec(loginStationNumber, duz);
				myConnection = LoginControllerUtils.getVistaLinkConnection(loginStationNumber,connSpec);
			} else {
				// userInfo is null, process the other credentials and login
	      // is this a/v code login?
        if ((access != null) && (verify != null)) {
  				// 1. check form submission values.
  				log.debug("1. institution=" + institution +", access=" + access + ", verify=" + verify);
  				LoginControllerUtils.checkFormInputValues(institution, access, verify);
  
  				// 2. Now check if we're OK as far as the login Institution choice is concerned.
  				log.debug("2");
  				LoginControllerUtils.validateKaajeeLoginDivision(institution);
  				// 3. get the connection
  				log.debug("3. clientIp=" + clientIp);
  				// VistaLinkAVConnectionSpec avConnSpec = new VistaLinkAVConnectionSpec(institution, access, verify);
  				KaajeeVistaLinkConnectionSpec avConnSpec = new KaajeeVistaLinkConnectionSpec(institution, access, verify, clientIp);
  				myConnection = LoginControllerUtils.getVistaLinkConnection(institution, avConnSpec);
        } else if (vistaToken != null) {
          log.debug("4. vistaToken=" + vistaToken);
          // ccow login via vista token
          if (vistaToken.indexOf('^') > 0) {
            userDuz = vistaToken.substring(vistaToken.indexOf('^')+1, vistaToken.length());
            vistaToken = vistaToken.substring(0, vistaToken.indexOf('^'));
          }
          VistaLinkConnectionSpec connSpec = new VistaLinkCcowConnectionSpec(vistaToken);
          myConnection = LoginControllerUtils.getVistaLinkConnection(institution,connSpec);
        } else {
          log.debug("4. login via user duz=" + userDuz);
          // try login via user duz
          VistaLinkConnectionSpec connSpec = new VistaLinkDuzConnectionSpec(institution, userDuz);
          myConnection = LoginControllerUtils.getVistaLinkConnection(institution, connSpec);          
        }
			}
			
			// 4. Create LoginUserInfoVO object, put in Session
			log.debug("5. VistaLinkConnection=" + myConnection);
			try {
			  userInfo = LoginUserInfoVOAssembler.getLoginUserInfo(myConnection, institution, clientIp);
			} catch(Exception e) {
			  e.printStackTrace();
        if ((e.getMessage() != null) && (e.getMessage().indexOf("Different IP") > 0) && (userDuz != null)) {
          // try login via user duz
          VistaLinkConnectionSpec connSpec = new VistaLinkDuzConnectionSpec(institution, userDuz);
          myConnection = LoginControllerUtils.getVistaLinkConnection(institution, connSpec);    
          userInfo = LoginUserInfoVOAssembler.getLoginUserInfo(myConnection, institution, clientIp);
        } else {
	        userJson = new LoginUserInfoVOJson();
	        userJson.setSuccess(false);
	        userJson.setErrors("Login Failed: " + e.getMessage());
			    return userJson;
        }
			}
			session.setAttribute(LoginUserInfoVO.SESSION_KEY, userInfo);
			Institution loginInst = Institution.factory.obtainByStationNumber(institution);
			userJson =  LoginControllerUtils.mapJson(userInfo, loginInst);
			
			// 6. Obtain user roles
			log.debug("6. userJson=" + userJson);
			roles = RoleUtilsAjax.cacheUserRoles(myConnection, userJson);
			user = new KaajeePrincipal(userInfo.getLoginStationNumber().toString() + userInfo.getUserDuz().toString());
			
			userJson.setSuccess(true);
			
			log.debug("7. principal user:" + user.getName());
			if(log.isDebugEnabled()){
			    for(KaajeePrincipal role : roles){
			        log.debug("   role: " + role.getName());
			    }
			}
			
		} catch (Exception e) {
			String exceptionString = "Error processing login credentials: ";
			log.debug(exceptionString, e);
			userJson = new LoginUserInfoVOJson();
			userJson.setSuccess(false);
			userJson.setErrors("Login Failed: " + e.getMessage());
			e.printStackTrace();
		} finally {
			// we're done with M connections now.
			if (myConnection != null) {
				try {
					myConnection.close();
				} catch (ResourceException e) {
					/* no op */
				}
			}
			session.setAttribute(LoginUserInfoVOJson.SESSION_KEY, userJson);
		}
		
		return userJson;
	}


    

	
	
	
}
