package gov.va.med.lom.vistabroker.service;

import gov.va.med.lom.foundation.service.response.ServiceResponse;

import gov.va.med.lom.vistabroker.auth.data.*;

import javax.ejb.Remote;

@Remote
public interface AuthVBService extends Service {
  
  public abstract ServiceResponse<String> getAVHelp(String division, boolean mustEnterOldVC);
  public abstract ServiceResponse<VistaServerInfo> getVistaServerInfo(String division);
  public abstract ServiceResponse<String> getIntroMessage(String division);
  public abstract ServiceResponse<VistaSignonResult> doAVCodeAuth1(String division, 
                                                  String applicationName, String accessCode, 
                                                  String verifyCode, String clientIp);
  public abstract ServiceResponse<VistaSignonResult> doAVCodeAuth2(String division, String applicationName, 
                                                  String avCode, String clientIp);
  public abstract ServiceResponse<ChangeVerifyCodeResult> changeVerifyCode(String division, 
                                                          String oldVerifyCode, String newVerifyCode,
                                                          String confirmVerifyCode);
}
