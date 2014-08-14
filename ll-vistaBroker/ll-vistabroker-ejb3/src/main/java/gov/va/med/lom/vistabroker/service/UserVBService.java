package gov.va.med.lom.vistabroker.service;

import gov.va.med.lom.foundation.service.response.CollectionServiceResponse;
import gov.va.med.lom.foundation.service.response.ServiceResponse;

import gov.va.med.lom.vistabroker.security.ISecurityContext;
import gov.va.med.lom.vistabroker.user.data.*;

import java.util.List;

import javax.ejb.Remote;

@Remote
public interface UserVBService extends Service {
  
  public abstract ServiceResponse<ActionResult> actOnDocument(ISecurityContext securityContext, String ien, String actionName);
  public abstract ServiceResponse<Boolean> canChangeCosigner(ISecurityContext securityContext, String ien);
  public abstract ServiceResponse<Boolean> authorSignedDocument(ISecurityContext securityContext, String ien, String duz);
  public abstract ServiceResponse<Boolean> canCosign(ISecurityContext securityContext, String titleIen, int docType, String duz);
  public abstract ServiceResponse<Boolean> tiuRequiresCosignature(ISecurityContext securityContext, int docType, String duz);
  public abstract ServiceResponse<Boolean> mustCosignDocument(ISecurityContext securityContext, String ien);
  public abstract ServiceResponse<Boolean> isValidElectronicSignatureCode(ISecurityContext securityContext, String code);
  public abstract CollectionServiceResponse<Signer> getCurrentSigners(ISecurityContext securityContext, String ien);
  public abstract CollectionServiceResponse<Signer> updateAdditionalSigners(ISecurityContext securityContext, String ien, List<Signer> signersList);
  public abstract ServiceResponse<String> changeCosigner(ISecurityContext securityContext, String ien, String cosignerDuz);
  public abstract ServiceResponse<ActionResult> signDocument(ISecurityContext securityContext, String ien, String esCode);
  public abstract ServiceResponse<ActionResult> tiuAdminClose(ISecurityContext securityContext, String noteIen, String mode, String userDuz);
  public abstract CollectionServiceResponse<Notification> getNotifications(ISecurityContext securityContext);
  public abstract ServiceResponse<String> getXQAData(ISecurityContext securityContext, String xqaid);
  public abstract ServiceResponse<String> getNotificationFollowUpText(ISecurityContext securityContext, String dfn, String ien, String xqaData);
  public abstract ServiceResponse<TiuAlertInfo> getTIUAlertInfo(ISecurityContext securityContext, String xqaid);
  public abstract ServiceResponse<Boolean> deleteAlert(ISecurityContext securityContext, String xqaid);
  public abstract ServiceResponse<String> forwardAlert(ISecurityContext securityContext, String xqaid, String duz, String fwdType, String comment);
  public abstract ServiceResponse<VistaUser> getVistaUser(ISecurityContext securityContext);
  public abstract ServiceResponse<String> getUserParam(ISecurityContext securityContext, String paramName);
  public abstract ServiceResponse<Boolean> hasSecurityKey(ISecurityContext securityContext, String keyName);
  public abstract ServiceResponse<Boolean> hasKey(ISecurityContext securityContext, String keyName);
  public abstract ServiceResponse<Boolean> hasMenuOptionAccess(ISecurityContext securityContext, String optionName);
  public abstract ServiceResponse<Boolean> userInactive(ISecurityContext securityContext, String duz);
  public abstract ServiceResponse<Employee> getEmployeeByDuz(ISecurityContext securityContext, String duz);
  public abstract CollectionServiceResponse<Employee> searchEmployeesByName(ISecurityContext securityContext, String name, 
          Boolean activeOnly, Boolean paidOnly, Integer maxResults);
}
