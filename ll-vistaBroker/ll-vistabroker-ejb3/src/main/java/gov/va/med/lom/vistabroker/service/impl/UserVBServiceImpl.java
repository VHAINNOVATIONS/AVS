package gov.va.med.lom.vistabroker.service.impl;

import gov.va.med.lom.foundation.service.response.CollectionServiceResponse;
import gov.va.med.lom.foundation.service.response.ServiceResponse;
import gov.va.med.lom.vistabroker.lists.data.ListItem;
import gov.va.med.lom.vistabroker.security.ISecurityContext;
import gov.va.med.lom.vistabroker.service.UserVBService;
import gov.va.med.lom.vistabroker.user.dao.ElectronicSignatureDao;
import gov.va.med.lom.vistabroker.user.dao.NotificationsDao;
import gov.va.med.lom.vistabroker.user.dao.VistaUserDao;
import gov.va.med.lom.vistabroker.user.data.ActionResult;
import gov.va.med.lom.vistabroker.user.data.Employee;
import gov.va.med.lom.vistabroker.user.data.Notification;
import gov.va.med.lom.vistabroker.user.data.Signer;
import gov.va.med.lom.vistabroker.user.data.TiuAlertInfo;
import gov.va.med.lom.vistabroker.user.data.VistaUser;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Remote;
import javax.ejb.Stateless;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

@Stateless(name = "gov.va.med.lom.vistabroker.UserVBService")
@Remote(UserVBService.class)
public class UserVBServiceImpl extends BaseService implements UserVBService, Serializable {

	@SuppressWarnings("unused")
	private static final Log log = LogFactory.getLog(UserVBServiceImpl.class);

	/*
	 * CONSTRUCTORS
	 */
	public UserVBServiceImpl() {
		super();
	}

	/*
	 * BUSINESS METHODS
	 */

	// Electronic Signature RPCs
	public ServiceResponse<ActionResult> actOnDocument(
			ISecurityContext securityContext, String ien, String actionName) {
		setSecurityContext(securityContext);
		String[] params = { ien, actionName };
		return singleResult(ActionResult.class, ElectronicSignatureDao.class,
				"actOnDocument", params);
	}

	public ServiceResponse<Boolean> canChangeCosigner(
			ISecurityContext securityContext, String ien) {
		setSecurityContext(securityContext);
		return singleResult(Boolean.class, ElectronicSignatureDao.class,
				"canChangeCosigner", ien);
	}

	public ServiceResponse<Boolean> authorSignedDocument(
			ISecurityContext securityContext, String ien, String duz) {
		setSecurityContext(securityContext);
		String[] params = { ien, duz };
		return singleResult(Boolean.class, ElectronicSignatureDao.class,
				"authorSignedDocument", params);
	}

	public ServiceResponse<Boolean> canCosign(ISecurityContext securityContext,
			String titleIen, int docType, String duz) {
		setSecurityContext(securityContext);
		Object[] params = { titleIen, docType, duz };
		return singleResult(Boolean.class, ElectronicSignatureDao.class,
				"canCosign", params);
	}

	public ServiceResponse<Boolean> tiuRequiresCosignature(
			ISecurityContext securityContext, int docType, String duz) {
		setSecurityContext(securityContext);
		Object[] params = { docType, duz };
		return singleResult(Boolean.class, ElectronicSignatureDao.class,
				"tiuRequiresCosignature", params);
	}

	public ServiceResponse<Boolean> mustCosignDocument(
			ISecurityContext securityContext, String ien) {
		setSecurityContext(securityContext);
		return singleResult(Boolean.class, ElectronicSignatureDao.class,
				"mustCosignDocument", ien);
	}

	public ServiceResponse<Boolean> isValidElectronicSignatureCode(
			ISecurityContext securityContext, String code) {
		setSecurityContext(securityContext);
		return singleResult(Boolean.class, ElectronicSignatureDao.class,
				"isValidElectronicSignatureCode", code);
	}

	public CollectionServiceResponse<Signer> getCurrentSigners(
			ISecurityContext securityContext, String ien) {
		setSecurityContext(securityContext);
		return collectionResult(Signer.class, ElectronicSignatureDao.class,
				"getCurrentSigners", ien);
	}

	public CollectionServiceResponse<Signer> updateAdditionalSigners(
			ISecurityContext securityContext, String ien,
			List<Signer> signersList) {
		setSecurityContext(securityContext);
		Object[] params = { ien, signersList };
		return collectionResult(Signer.class, ElectronicSignatureDao.class,
				"updateAdditionalSigners", params);
	}

	public ServiceResponse<String> changeCosigner(
			ISecurityContext securityContext, String ien, String cosignerDuz) {
		setSecurityContext(securityContext);
		String[] params = { ien, cosignerDuz };
		return singleResult(String.class, ElectronicSignatureDao.class,
				"changeCosigner", params);
	}

	public ServiceResponse<ActionResult> signDocument(
			ISecurityContext securityContext, String ien, String esCode) {
		setSecurityContext(securityContext);
		String[] params = { ien, esCode };
		return singleResult(ActionResult.class, ElectronicSignatureDao.class,
				"signDocument", params);
	}

	public ServiceResponse<ActionResult> tiuAdminClose(ISecurityContext securityContext, String noteIen, String mode, String userDuz) {
    setSecurityContext(securityContext);
    String[] params = { noteIen, mode, userDuz };
    return singleResult(ActionResult.class, ElectronicSignatureDao.class,
        "tiuAdminClose", params);
  }
  
	// Notifications RPCs
	public CollectionServiceResponse<Notification> getNotifications(
			ISecurityContext securityContext) {
		setSecurityContext(securityContext);
		return collectionResult(Notification.class, NotificationsDao.class,
				"getNotifications");
	}

	public ServiceResponse<String> getXQAData(ISecurityContext securityContext,
			String xqaid) {
		setSecurityContext(securityContext);
		return singleResult(String.class, NotificationsDao.class, "getXQAData",
				xqaid);
	}

	public ServiceResponse<String> getNotificationFollowUpText(
			ISecurityContext securityContext, String dfn, String ien,
			String xqaData) {
		setSecurityContext(securityContext);
		Object[] params = { dfn, ien, xqaData };
		return singleResult(String.class, NotificationsDao.class,
				"getNotificationFollowUpText", params);
	}

	public ServiceResponse<TiuAlertInfo> getTIUAlertInfo(
			ISecurityContext securityContext, String xqaid) {
		setSecurityContext(securityContext);
		return singleResult(TiuAlertInfo.class, NotificationsDao.class,
				"getTIUAlertInfo", xqaid);
	}

	public ServiceResponse<Boolean> deleteAlert(
			ISecurityContext securityContext, String xqaid) {
		setSecurityContext(securityContext);
		return singleResult(Boolean.class, NotificationsDao.class,
				"deleteAlert", xqaid);
	}

	public ServiceResponse<String> forwardAlert(
			ISecurityContext securityContext, String xqaid, String duz,
			String fwdType, String comment) {
		setSecurityContext(securityContext);
		Object[] params = { xqaid, duz, fwdType, comment };
		return singleResult(String.class, NotificationsDao.class,
				"forwardAlert", params);
	}

	// User RPCs
	public ServiceResponse<VistaUser> getVistaUser(
			ISecurityContext securityContext) {
		setSecurityContext(securityContext);
		return singleResult(VistaUser.class, VistaUserDao.class, "getVistaUser");
	}

	public ServiceResponse<String> getUserParam(
			ISecurityContext securityContext, String paramName) {
		setSecurityContext(securityContext);
		return singleResult(String.class, VistaUserDao.class, "getAVHelp",
				paramName);
	}

	public ServiceResponse<Boolean> hasSecurityKey(
			ISecurityContext securityContext, String keyName) {
		setSecurityContext(securityContext);
		return singleResult(Boolean.class, VistaUserDao.class,
				"hasSecurityKey", keyName);
	}

  public ServiceResponse<Boolean> hasKey(
      ISecurityContext securityContext, String keyName) {
    setSecurityContext(securityContext);
    return singleResult(Boolean.class, VistaUserDao.class,
        "hasKey", keyName);
  }	
	
	public ServiceResponse<Boolean> hasMenuOptionAccess(
			ISecurityContext securityContext, String optionName) {
		setSecurityContext(securityContext);
		return singleResult(Boolean.class, VistaUserDao.class,
				"hasMenuOptionAccess", optionName);
	}

	public ServiceResponse<Boolean> userInactive(
			ISecurityContext securityContext, String duz) {
		setSecurityContext(securityContext);
		return singleResult(Boolean.class, VistaUserDao.class, "userInactive",
				duz);
	}

	public ServiceResponse<Employee> getEmployeeByDuz(
			ISecurityContext securityContext, String duz) {
		setSecurityContext(securityContext);
		return singleResult(Employee.class, VistaUserDao.class,
				"getEmployeeByDuz", duz);
	}

	public CollectionServiceResponse<ListItem> getEmployeesForTL(
			ISecurityContext securityContext, String tlCode) {
		setSecurityContext(securityContext);
		return collectionResult(ListItem.class, VistaUserDao.class,
				"getEmployeesForTL", tlCode);
	}

	public CollectionServiceResponse<ListItem> getTLForSupervisor(
			ISecurityContext securityContext, String duz) {
		setSecurityContext(securityContext);
		return collectionResult(ListItem.class, VistaUserDao.class,
				"getTLForSupervisor", duz);
	}

	public CollectionServiceResponse<Employee> searchEmployeesByName(
			ISecurityContext securityContext, String name, Boolean activeOnly,
			Boolean paidOnly, Integer maxResults) {
		setSecurityContext(securityContext);
		Object[] params = { name, activeOnly, paidOnly, maxResults };
		return collectionResult(Employee.class, VistaUserDao.class,
				"searchEmployeesByName", params);

	}

}
