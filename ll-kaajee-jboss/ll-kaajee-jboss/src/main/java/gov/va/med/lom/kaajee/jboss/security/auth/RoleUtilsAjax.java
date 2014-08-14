package gov.va.med.lom.kaajee.jboss.security.auth;

import gov.va.med.authentication.kernel.KaajeeException;
import gov.va.med.exception.FoundationsException;
import gov.va.med.lom.kaajee.jboss.model.KaajeePrincipal;
import gov.va.med.lom.kaajee.jboss.model.json.LoginUserInfoVOJson;
import gov.va.med.vistalink.adapter.cci.VistaLinkConnection;
import gov.va.med.vistalink.rpc.RpcRequest;
import gov.va.med.vistalink.rpc.RpcRequestFactory;
import gov.va.med.vistalink.rpc.RpcResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Business Delegate that handles the details of caching a user's roles.
 * 
 * @author VHIT Security and Other Common Services (S&OCS)
 * @version 1.1.0.007
 */
class RoleUtilsAjax {

    private static final Log log = LogFactory.getLog(RoleUtilsAjax.class);

    static List<KaajeePrincipal> cacheUserRoles(
            VistaLinkConnection myConnection, LoginUserInfoVOJson userInfo)
            throws KaajeeException {
        String exceptionMessage = "";
        // TODO have the M role retrieval in a separate method, returning an array list
        // TODO do we still need RoleUtils class if we make this separation?
        try {
            // get role list
            ApplicationRoleListVOSingleton roleListVO = ApplicationRoleListVOSingleton
                    .getInstance();
            // but if not...
            if (roleListVO == null) {
                throw new KaajeeException(
                        "Could not retrieve application role list, because ApplicationRoleListVOSingleton has not been initialized.");
            } // cache the roles
            return RoleUtilsAjax.cacheRoles(myConnection, roleListVO, userInfo);
        } catch (KaajeeException e) {

            LoginControllerUtils.closeVistaLinkConnection(myConnection);
            exceptionMessage = "Could not cache user roles.";
            if (log.isErrorEnabled()) {
                log.error(exceptionMessage, e);
            }
            throw new KaajeeException(exceptionMessage, e);
        }
    }

    /**
     * Cache a user's roles. Called during login to a) retrieve a user's roles
     * from VistA, matching a the roles found in the RoleSingleton object, and
     * b) cache those roles in the role cache table for the current DAO
     * implementation in use.
     * 
     * @param username
     * @param myConnection
     * @param ApplicationRoleListVOSingleton
     * @return
     * @throws FoundationsException
     */
    private static List<KaajeePrincipal> cacheRoles(
            VistaLinkConnection myConnection,
            ApplicationRoleListVOSingleton roleListVO,
            LoginUserInfoVOJson userInfo) throws KaajeeException {

        List<String> userRoleListFromM = new ArrayList<String>();
        List<KaajeePrincipal> roles = new ArrayList<KaajeePrincipal>();

        try {

            
            // we are here so we have a valid vista user 
            // every vista user gets a AUTHENTICATED_KAAJEE_USER role
            
            roles.add(new KaajeePrincipal(KaajeeLoginModule.DEFAULT_ROLE));
            
            /*
             * NAME: XUS KEY CHECK TAG: OWNSKEY 
             * ROUTINE: XUSRB RETURN VALUE
             * TYPE: ARRAY 
             * AVAILABILITY: PUBLIC 
             * DESCRIPTION: This API will check
             * if the user (DUZ) holds a security key or an array of keys. If a
             * single security KEY is sent the result is returned in R(0). If an
             * array is sent down then the return array has the same order as
             * the calling array. 
             * INPUT PARAMETER: KEY 
             * PARAMETER TYPE: LIST
             * MAXIMUM DATA LENGTH: 30 
             * REQUIRED: YES 
             * DESCRIPTION: If key is a
             * single value it holds the one key to check. If key is an array
             * then the result is an array that matches the key list with values
             * that match the status of the key check for each key. The return
             * is a 1 if the user has the key and 0 if not.
             */

            // String rpcName = "XUS KEY CHECK";
            // XUS ALLKEYS this rpc call uses to get all security keys from M
            // system
            String rpcName = "XUS ALLKEYS";
            RpcRequest vReq = RpcRequestFactory.getRpcRequest();
            vReq.setUseProprietaryMessageFormat(true);
            vReq.setRpcContext(KaajeeLoginModule.LOGIN_RPC_CONTEXT);
            vReq.setRpcClientTimeOut(600);
            vReq.setRpcName(rpcName);

            RpcResponse vResp = myConnection.executeRPC(vReq);
            String results = vResp.getResults();
            if (log.isDebugEnabled()) {
                log.debug("results of XUS KEY CHECK: " + results);
            }

            StringTokenizer st = new StringTokenizer(results, "\n");
            String keyname = "";

            while (st.hasMoreTokens()) {
                keyname = st.nextToken();
                userRoleListFromM.add(keyname);
                if (log.isDebugEnabled()) {
                    StringBuffer sb = new StringBuffer("Adding role '");
                    sb.append(keyname);
                    sb.append("' to role cache for username '");
                    sb.append(userInfo.getUserLoginStationNumber() + "^"
                            + userInfo.getUserDuz());
                    sb.append("'.");
                    log.debug(sb.toString());
                }
                userInfo.addRole(keyname);
                roles.add(new KaajeePrincipal(keyname));

            }
        } catch (FoundationsException e) {

            if (log.isErrorEnabled()) {
                log.error("Could not retrieve roles.");
            }
            throw new KaajeeException(e);
        }

        return roles;

    }
}
