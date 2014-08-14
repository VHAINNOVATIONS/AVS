package gov.va.med.lom.vistabroker.service;

import gov.va.med.lom.foundation.service.response.CollectionServiceResponse;
import gov.va.med.lom.foundation.service.response.ServiceResponse;

import gov.va.med.lom.vistabroker.admin.data.*;
import gov.va.med.lom.vistabroker.security.ISecurityContext;

import java.util.GregorianCalendar;
import java.util.List;
import java.util.SortedMap;

import javax.ejb.Remote;

@Remote
public interface AdminVBService extends Service {
  
  public abstract CollectionServiceResponse<GenFormData> putGenFormData(ISecurityContext securityContext, List<GenFormData> genFormDataList);
  public abstract ServiceResponse<Boolean> putGenFormWpData(ISecurityContext securityContext, List<GenFormData> genFormDataList);
  public abstract CollectionServiceResponse<GenFormData> getGenFormData(ISecurityContext securityContext, List<GenFormData> genFormDataList);
  public abstract CollectionServiceResponse<GenFormListItem> getFormGenListValues(ISecurityContext securityContext, GenFormListParams listParams);
  
  public abstract CollectionServiceResponse<SoftwarePackage> getSoftwarePackages(ISecurityContext securityContext, int sort);
  public abstract CollectionServiceResponse<PatientMovement> fetchPatientMovements(ISecurityContext securityContext, GregorianCalendar startDate, 
                                                              GregorianCalendar stopDate);
  public abstract CollectionServiceResponse<ScheduledAdmission> fetchScheduledAdmits(ISecurityContext securityContext, GregorianCalendar startDate, 
                                                                GregorianCalendar stopDate);
  public abstract CollectionServiceResponse<String> getSpoolerTextList(ISecurityContext securityContext, String reportName);
  public abstract ServiceResponse<String> getSpoolerText(ISecurityContext securityContext, String reportName);
  public ServiceResponse<SortedMap<String,TidesItem>> getTidesPromptOrData(ISecurityContext securityContext, String dfn,String contactDT,String field,String flags);
  public ServiceResponse<String> setTidesFileData(ISecurityContext securityContext, List<String> data,String flags );
  public ServiceResponse<String> setTidesWordProcessingData(ISecurityContext securityContext, String ien,String fieldNumber,List<String> data );
  public CollectionServiceResponse<String> getTidesTargetValues(ISecurityContext securityContext, String shortForm,String file,String iens,String fields,String flags,String number,String from,String part,String index,String screen,String indextifier);    
  
  public CollectionServiceResponse<SurgeryScheduleItem> getSurgerySchedule(ISecurityContext securityContext, GregorianCalendar date);
}
