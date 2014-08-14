package gov.va.med.lom.vistabroker.service.impl;

import gov.va.med.lom.foundation.service.response.CollectionServiceResponse;
import gov.va.med.lom.foundation.service.response.ServiceResponse;

import gov.va.med.lom.vistabroker.ddr.*;
import gov.va.med.lom.vistabroker.security.ISecurityContext;
import gov.va.med.lom.vistabroker.service.DdrVBService;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Remote;
import javax.ejb.Stateless;

@Stateless(name = "gov.va.med.lom.vistabroker.DdrVBService")
@Remote(DdrVBService.class)
public class DdrVBServiceImpl extends BaseService implements DdrVBService, Serializable {

	/*
	 * CONSTRUCTORS
	 */
	public DdrVBServiceImpl() {
		super();
	}

	/*
	 * BUSINESS METHODS
	 */

  public ServiceResponse<String> execDdrFiler(ISecurityContext securityContext, String operation, String[] args) {
  
    ServiceResponse<String> response = new ServiceResponse<String>();
    
    try {
      
      DdrFiler ddrFiler = new DdrFiler(securityContext);
      ddrFiler.setOperation(operation);
      ddrFiler.setArgs(args);
    
      String result = ddrFiler.execute();
      response.setPayload(result);
      
    } catch (Exception ex) {
      response.addError(ex.getMessage());
    }
    
    return response;
    
  }
  
  public CollectionServiceResponse<String> execDdrGetsEntry(ISecurityContext securityContext, String file, String iens, String fields, String flags) {
  
    CollectionServiceResponse<String> response = new CollectionServiceResponse<String>();
    
    try {
      
      DdrGetsEntry dDdrGetsEntry = new DdrGetsEntry(securityContext);
      dDdrGetsEntry.setFile(file);
      dDdrGetsEntry.setIens(iens);
      dDdrGetsEntry.setFields(fields);
      dDdrGetsEntry.setFlags(flags);
    
      List<String> result = dDdrGetsEntry.execute();
      response.setCollection(result);
      
    } catch (Exception ex) {
      response.addError(ex.getMessage());
    }
    
    return response;
    
  }
  
  public CollectionServiceResponse<String> execDdrLister(ISecurityContext securityContext, String file, String iens, String fields, 
                                                         String flags, Integer max, String from, String part, String xref,
                                                         String screen, String id, String options, String moreFrom, String moreIens) {
                                        
    CollectionServiceResponse<String> response = new CollectionServiceResponse<String>();
    
    try {
      
      DdrLister ddrLister = new DdrLister(securityContext);
      ddrLister.setFile(file);
      ddrLister.setIens(iens);
      ddrLister.setFields(fields);
      ddrLister.setFlags(flags);
      ddrLister.setMax(max);
      ddrLister.setFrom(from);
      ddrLister.setPart(part);
      ddrLister.setXref(xref);
      ddrLister.setScreen(screen);
      ddrLister.setId(id);
      ddrLister.setOptions(options);
      ddrLister.setMoreFrom(moreFrom);
      ddrLister.setMoreIens(moreIens);
      
      List<String> result = ddrLister.execute();
      response.setCollection(result);
      
    } catch (Exception ex) {
      response.addError(ex.getMessage());
    }
    
    return response;
    
  }
  
  public ServiceResponse<String> execDdrValidator(ISecurityContext securityContext, String file, String iens, String field, String value) {
  
    ServiceResponse<String> response = new ServiceResponse<String>();
    
    try {
      
      DdrValidator ddrValidator = new DdrValidator(securityContext);
      ddrValidator.setFile(file);
      ddrValidator.setIens(iens);
      ddrValidator.setField(field);
      ddrValidator.setValue(value);
      
      String result = ddrValidator.execute();
      response.setPayload(result);
      
    } catch (Exception ex) {
      response.addError(ex.getMessage());
    }
    
    return response;
    
  }
	
}
