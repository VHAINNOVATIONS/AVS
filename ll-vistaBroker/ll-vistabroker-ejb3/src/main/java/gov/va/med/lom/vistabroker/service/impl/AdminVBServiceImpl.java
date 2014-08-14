package gov.va.med.lom.vistabroker.service.impl;

import gov.va.med.lom.foundation.service.response.CollectionServiceResponse;
import gov.va.med.lom.foundation.service.response.ServiceResponse;

import gov.va.med.lom.vistabroker.admin.dao.*;
import gov.va.med.lom.vistabroker.admin.data.*;
import gov.va.med.lom.vistabroker.security.ISecurityContext;
import gov.va.med.lom.vistabroker.service.AdminVBService;

import java.io.Serializable;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.SortedMap;

import javax.ejb.Remote;
import javax.ejb.Stateless;

@Stateless(name = "gov.va.med.lom.vistabroker.AdminVBService")
@Remote(AdminVBService.class)
public class AdminVBServiceImpl extends BaseService implements AdminVBService, Serializable {

	/*
	 * CONSTRUCTORS
	 */
	public AdminVBServiceImpl() {
		super();
	}

	/*
	 * BUSINESS METHODS
	 */

	// GenForm RPCs
	public CollectionServiceResponse<GenFormData> putGenFormData(
			ISecurityContext securityContext, List<GenFormData> genFormDataList) {
		setSecurityContext(securityContext);
		return collectionResult(GenFormData.class, FormGeneratorDao.class,
				"putData", genFormDataList);
	}

	public ServiceResponse<Boolean> putGenFormWpData(
			ISecurityContext securityContext, List<GenFormData> genFormDataList) {
		setSecurityContext(securityContext);
		return singleResult(Boolean.class, FormGeneratorDao.class, "putWpData",
				genFormDataList);
	}

	public CollectionServiceResponse<GenFormData> getGenFormData(
			ISecurityContext securityContext, List<GenFormData> genFormDataList) {
		setSecurityContext(securityContext);
		return collectionResult(GenFormData.class, FormGeneratorDao.class,
				"getData", genFormDataList);
	}

	public CollectionServiceResponse<GenFormListItem> getFormGenListValues(
			ISecurityContext securityContext, GenFormListParams listParams) {
		setSecurityContext(securityContext);
		return collectionResult(GenFormListItem.class, FormGeneratorDao.class,
				"getListValues", listParams);
	}

	// Software Package RPC
	public CollectionServiceResponse<SoftwarePackage> getSoftwarePackages(
			ISecurityContext securityContext, int sort) {
		setSecurityContext(securityContext);
		return collectionResult(SoftwarePackage.class, PackageSupportDao.class,
				"fetch", sort);
	}

	// Patient Movement and Scheduled Admit RPCs
	public CollectionServiceResponse<PatientMovement> fetchPatientMovements(
			ISecurityContext securityContext, GregorianCalendar startDate,
			GregorianCalendar stopDate) {
		setSecurityContext(securityContext);
		Object[] params = { startDate, stopDate };
		return collectionResult(PatientMovement.class,
				PatientMovementsDao.class, "fetch", params);
	}

	public CollectionServiceResponse<ScheduledAdmission> fetchScheduledAdmits(
			ISecurityContext securityContext, GregorianCalendar startDate,
			GregorianCalendar stopDate) {
		setSecurityContext(securityContext);
		Object[] params = { startDate, stopDate };
		return collectionResult(ScheduledAdmission.class,
				ScheduledAdmitsDao.class, "fetch", params);
	}

	// Spooler Text RPCs
	public CollectionServiceResponse<String> getSpoolerTextList(
			ISecurityContext securityContext, String reportName) {
		setSecurityContext(securityContext);
		return collectionResult(String.class, SpoolerTextDao.class,
				"getSpoolerTextList", reportName);
	}

	public ServiceResponse<String> getSpoolerText(
			ISecurityContext securityContext, String reportName) {
		setSecurityContext(securityContext);
		return singleResult(String.class, SpoolerTextDao.class,
				"getSpoolerText", reportName);
	}

	// Tides RPCs
	@SuppressWarnings("unchecked")
	public ServiceResponse<SortedMap<String, TidesItem>> getTidesPromptOrData(
			ISecurityContext securityContext, String dfn, String contactDT,
			String field, String flags) {
		setSecurityContext(securityContext);
		Object[] params = { dfn, contactDT, field, flags };
		SortedMap<String, TidesItem> result;
		ServiceResponse<SortedMap<String, TidesItem>> response = new ServiceResponse<SortedMap<String, TidesItem>>();

		try {
			result = (SortedMap<String, TidesItem>) invokeCall(TidesDao.class,
					"getPromptOrData", params);
			response.setPayload(result);
		} catch (Exception e) {
			log.error("getTidesPropmtOrData", e);
		}

		return response;
	}

	public ServiceResponse<String> setTidesFileData(
			ISecurityContext securityContext, List<String> data, String flags) {
		setSecurityContext(securityContext);
		Object[] params = { data, flags };
		return singleResult(String.class, TidesDao.class, "setFileData", params);
	}

	public ServiceResponse<String> setTidesWordProcessingData(
			ISecurityContext securityContext, String ien, String fieldNumber,
			List<String> data) {
		setSecurityContext(securityContext);
		Object[] params = { ien, fieldNumber, data };
		return singleResult(String.class, TidesDao.class,
				"setWordProcessingData", params);
	}

	public CollectionServiceResponse<String> getTidesTargetValues(
			ISecurityContext securityContext, String shortForm, String file,
			String iens, String fields, String flags, String number,
			String from, String part, String index, String screen,
			String indextifier) {
		setSecurityContext(securityContext);
		Object[] params = { shortForm, file, iens, fields, flags, number, from,
				part, index, screen, indextifier };
		return collectionResult(String.class, TidesDao.class,
				"getTargetValues", params);
	}

	public CollectionServiceResponse<SurgeryScheduleItem> getSurgerySchedule(
			ISecurityContext securityContext, GregorianCalendar date) {
		setSecurityContext(securityContext);
		Object[] params = { date };
		return collectionResult(SurgeryScheduleItem.class,
				SurgeryScheduleDao.class, "fetch", params);

	}

}
