package gov.va.med.lom.vistabroker.service;

import gov.va.med.lom.foundation.service.response.ServiceResponse;
import gov.va.med.lom.foundation.service.response.CollectionServiceResponse;

import gov.va.med.lom.vistabroker.security.ISecurityContext;

import javax.ejb.Remote;

@Remote
public interface DdrVBService extends Service {
  
  public abstract ServiceResponse<String> execDdrFiler(ISecurityContext securityContext, String operation, String[] args);
  public abstract CollectionServiceResponse<String> execDdrGetsEntry(ISecurityContext securityContext, String file, String iens, String fields, String flags);
  public abstract CollectionServiceResponse<String> execDdrLister(ISecurityContext securityContext, String file, String iens, String fields, 
                                                                  String flags, Integer max, String from, String part, String xref,
                                                                  String screen, String id, String options, String moreFrom, String moreIens);
  public abstract ServiceResponse<String> execDdrValidator(ISecurityContext securityContext, String file, String iens, String field, String value);
  
}
