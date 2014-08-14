package gov.va.med.lom.vistabroker.service;

import gov.va.med.lom.foundation.service.response.ServiceResponse;

import gov.va.med.lom.vistabroker.misc.data.VistaStation;
import gov.va.med.lom.vistabroker.security.ISecurityContext;

import javax.ejb.Remote;

@Remote
public interface MiscVBService extends Service {
  
  public abstract ServiceResponse<Double> strToFMDateTime(ISecurityContext securityContext, String str);
  public abstract ServiceResponse<Double> validDateTimeStr(ISecurityContext securityContext, String str, String flags);
  public abstract ServiceResponse<Double> fmNow(ISecurityContext securityContext);
  public abstract ServiceResponse<Integer> fmToday(ISecurityContext securityContext);
  public abstract ServiceResponse<VistaStation> getStation(ISecurityContext securityContext, String stationNo); 
  public abstract ServiceResponse<String> externalName(ISecurityContext securityContext, String ien, double fileNumber);
  public abstract ServiceResponse<String> getVariableValue(ISecurityContext securityContext, String arg);
  
}
