package gov.va.med.lom.vistabroker.service.impl;

import gov.va.med.lom.foundation.service.response.ServiceResponse;

import gov.va.med.lom.vistabroker.auth.dao.VistaSignonDao;
import gov.va.med.lom.vistabroker.auth.dao.VistaSignonSetupDao;
import gov.va.med.lom.vistabroker.auth.data.ChangeVerifyCodeResult;
import gov.va.med.lom.vistabroker.auth.data.VistaServerInfo;
import gov.va.med.lom.vistabroker.auth.data.VistaSignonResult;
import gov.va.med.lom.vistabroker.security.ISecurityContext;
import gov.va.med.lom.vistabroker.security.SecurityContextFactory;
import gov.va.med.lom.vistabroker.service.AuthVBService;

import java.io.Serializable;

import javax.ejb.Remote;
import javax.ejb.Stateless;

@Stateless(name = "gov.va.med.lom.vistabroker.AuthVBService")
@Remote(AuthVBService.class)
public class AuthVBServiceImpl extends BaseService implements AuthVBService, Serializable {

	private static final String AUTH_APP_PROXY = "ALSIAPPPROXY,VISTA BROKER";

	/*
	 * CONSTRUCTORS
	 */
	public AuthVBServiceImpl() {
		super();
		setLogEvent(false);
		try {
			setSecurityContext(SecurityContextFactory
					.createAppProxySecurityContext(null, AUTH_APP_PROXY));
		} catch (Exception e) {
			log.warn(
					"Exception creating app proxy security context for AuthRpcs.",
					e);
		}
	}

	/*
	 * BUSINESS METHODS
	 */

	// Signon Setup RPCs
	public ServiceResponse<String> getAVHelp(String division,
			boolean mustEnterOldVC) {
		securityContext.setDivision(division);
		return singleResult(String.class, VistaSignonSetupDao.class,
				"getAVHelp", mustEnterOldVC);
	}

	public ServiceResponse<VistaServerInfo> getVistaServerInfo(String division) {
		securityContext.setDivision(division);
		return singleResult(VistaServerInfo.class, VistaSignonSetupDao.class,
				"getVistaServerInfo");
	}

	public ServiceResponse<String> getIntroMessage(String division) {
		securityContext.setDivision(division);
		return singleResult(String.class, VistaSignonSetupDao.class,
				"getIntroMessage");
	}

	// Signon RPCs
	public ServiceResponse<VistaSignonResult> doAVCodeAuth1(String division,
			String applicationName, String accessCode, String verifyCode,
			String clientIp) {
		verifyParam("division", division);
		verifyParam("applicationName", applicationName);
		verifyParam("accessCode", accessCode);
		verifyParam("verifyCode", verifyCode);
		verifyParam("clientIp", clientIp);
		return doAVCodeAuth2(division, applicationName, accessCode + ";"
				+ verifyCode, clientIp);
	}

	public ServiceResponse<VistaSignonResult> doAVCodeAuth2(String division,
			String applicationName, String avCode, String clientIp) {
		verifyParam("division", division);
		verifyParam("applicationName", applicationName);
		verifyParam("avCode", avCode);
		verifyParam("clientIp", clientIp);

		securityContext.setDivision(division);
		String[] params = { division, applicationName, avCode, clientIp };
		ServiceResponse<VistaSignonResult> sr = singleResult(
				VistaSignonResult.class, VistaSignonDao.class, "doVistaSignon",
				params);
		logCall("doVistaSignon3");

		/**
		 * create a new security context that can be retrieved by the client for
		 * future use
		 */

		VistaSignonResult result = sr.getPayload();
		ISecurityContext sc = SecurityContextFactory.createDuzSecurityContext(
				division, result.getDuz());
		this.securityContext = sc;

		return sr;

	}

	private void verifyParam(String name, String value) {
		if (value == null)
			throw new NullPointerException(name + " cannot be null.");
	}

	public ServiceResponse<ChangeVerifyCodeResult> changeVerifyCode(
			String division, String oldVerifyCode, String newVerifyCode,
			String confirmVerifyCode) {
		securityContext.setDivision(division);
		String[] params = { oldVerifyCode, newVerifyCode, confirmVerifyCode };
		ServiceResponse<ChangeVerifyCodeResult> sr = singleResult(
				ChangeVerifyCodeResult.class, VistaSignonDao.class,
				"changeVerifyCode", params);
		logCall("changeVerifyCode");
		return sr;
	}

}
