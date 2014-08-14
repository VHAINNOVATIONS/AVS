package gov.va.med.lom.vistabroker.service.impl;

import gov.va.med.lom.foundation.service.response.CollectionServiceResponse;
import gov.va.med.lom.foundation.service.response.ServiceResponse;

import gov.va.med.lom.vistabroker.lists.dao.*;
import gov.va.med.lom.vistabroker.lists.data.*;
import gov.va.med.lom.vistabroker.security.ISecurityContext;
import gov.va.med.lom.vistabroker.service.ListsVBService;

import java.io.Serializable;
import java.util.Date;

import javax.ejb.Remote;
import javax.ejb.Stateless;

@Stateless(name = "gov.va.med.lom.vistabroker.ListsVBService")
@Remote(ListsVBService.class)
public class ListsVBServiceImpl extends BaseService implements ListsVBService, Serializable {

	/*
	 * CONSTRUCTORS
	 */
	public ListsVBServiceImpl() {
		super();
	}

	/*
	 * BUSINESS METHODS
	 */

	// ICD-9/CPT Codes RPCs
	public CollectionServiceResponse<Icd9Code> listICD9Codes(
			ISecurityContext securityContext, String dsix, String scr) {
		setSecurityContext(securityContext);
		Object[] params = { dsix, scr };
		return collectionResult(Icd9Code.class, CodesDao.class,
				"listICD9Codes", params);
	}

	public CollectionServiceResponse<Icd9Code> listIcd9Lexicon(
			ISecurityContext securityContext, String lex, double dt) {
		setSecurityContext(securityContext);
		Object[] params = { lex, dt };
		return collectionResult(Icd9Code.class, CodesDao.class,
				"listIcd9Lexicon", params);
	}

	public CollectionServiceResponse<CptCode> listCPTCodes(
			ISecurityContext securityContext, String val) {
		setSecurityContext(securityContext);
		return collectionResult(CptCode.class, CodesDao.class, "listCPTCodes",
				val);
	}

	public CollectionServiceResponse<CptCode> listCptLexicon(
			ISecurityContext securityContext, String lex, double dt) {
		setSecurityContext(securityContext);
		Object[] params = { lex, dt };
		return collectionResult(CptCode.class, CodesDao.class,
				"listCptLexicon", params);
	}

	public ServiceResponse<String> lexiconToIcd9Code(
			ISecurityContext securityContext, String ien, double dt) {
		setSecurityContext(securityContext);
		Object[] params = { ien, dt };
		return singleResult(String.class, CodesDao.class, "lexiconToIcd9Code",
				params);
	}

	public ServiceResponse<String> lexiconToCptCode(
			ISecurityContext securityContext, String ien, double dt) {
		setSecurityContext(securityContext);
		Object[] params = { ien, dt };
		return singleResult(String.class, CodesDao.class, "lexiconToCptCode",
				params);
	}

	// Specialties/Teams RPCs
	public CollectionServiceResponse<ListItem> listAllSpecialties(
			ISecurityContext securityContext) {
		setSecurityContext(securityContext);
		return collectionResult(ListItem.class, SpecialtiesTeamsDao.class,
				null, "listAllSpecialties");
	}

	public CollectionServiceResponse<ListItem> listAllTeams(
			ISecurityContext securityContext) {
		setSecurityContext(securityContext);
		return collectionResult(ListItem.class, SpecialtiesTeamsDao.class,
				"listAllTeams");
	}

	// Cover Sheet List RPC
	public CollectionServiceResponse<CoverSheetRpc> getCoverSheetList(
			ISecurityContext securityContext) {
		setSecurityContext(securityContext);
		return collectionResult(CoverSheetRpc.class, CoverSheetListDao.class,
				"getCoverSheetList");
	}

	// Lab Test Type RPCs
	public CollectionServiceResponse<ListItem> getAtomicTests(
			ISecurityContext securityContext, String startFrom, int direction) {
		setSecurityContext(securityContext);
		Object[] params = { startFrom, direction };
		return collectionResult(ListItem.class, LabTestTypesDao.class,
				"getAtomicTests", params);
	}

	public CollectionServiceResponse<ListItem> getSpecimens(
			ISecurityContext securityContext, String startFrom, int direction) {
		setSecurityContext(securityContext);
		Object[] params = { startFrom, direction };
		return collectionResult(ListItem.class, LabTestTypesDao.class,
				"getSpecimens", params);
	}

	public CollectionServiceResponse<ListItem> getAllTests(
			ISecurityContext securityContext, String startFrom, int direction) {
		setSecurityContext(securityContext);
		Object[] params = { startFrom, direction };
		return collectionResult(ListItem.class, LabTestTypesDao.class,
				"getAllTests", params);
	}

	public CollectionServiceResponse<ListItem> getChemTests(
			ISecurityContext securityContext, String startFrom, int direction) {
		setSecurityContext(securityContext);
		Object[] params = { startFrom, direction };
		return collectionResult(ListItem.class, LabTestTypesDao.class,
				"getChemTests", params);
	}

	public CollectionServiceResponse<ListItem> getLabUsers(
			ISecurityContext securityContext, String startFrom, int direction) {
		setSecurityContext(securityContext);
		Object[] params = { startFrom, direction };
		return collectionResult(ListItem.class, LabTestTypesDao.class,
				"getLabUsers", params);
	}

	public CollectionServiceResponse<ListItem> getTestGroupsForLabUser(
			ISecurityContext securityContext, String userDuz) {
		setSecurityContext(securityContext);
		return collectionResult(ListItem.class, LabTestTypesDao.class,
				"getTestGroupsForLabUser", userDuz);
	}

	public CollectionServiceResponse<ListItem> getATest(
			ISecurityContext securityContext, String testIen) {
		setSecurityContext(securityContext);
		return collectionResult(ListItem.class, LabTestTypesDao.class,
				"getATest", testIen);
	}

	public CollectionServiceResponse<ListItem> getATestGroup(
			ISecurityContext securityContext, String testGroupIen,
			String userDuz) {
		setSecurityContext(securityContext);
		Object[] params = { testGroupIen, userDuz };
		return collectionResult(ListItem.class, LabTestTypesDao.class,
				"getATestGroup", params);
	}

	public ServiceResponse<String> getTestInfo(
			ISecurityContext securityContext, String testIen) {
		setSecurityContext(securityContext);
		return singleResult(String.class, LabTestTypesDao.class, "getTestInfo",
				testIen);
	}

	public CollectionServiceResponse<ListItem> getLabReportLists(
			ISecurityContext securityContext) {
		setSecurityContext(securityContext);
		return collectionResult(ListItem.class, LabTestTypesDao.class,
				"getLabReportLists");
	}

	// Locations RPCs
	public CollectionServiceResponse<ListItem> listAllWards(
			ISecurityContext securityContext) {
		setSecurityContext(securityContext);
		return collectionResult(ListItem.class, LocationsDao.class,
				"listAllWards");
	}

	public CollectionServiceResponse<ListItem> getSubSetOfLocations(
			ISecurityContext securityContext, String startFrom, int direction) {
		setSecurityContext(securityContext);
		Object[] params = { startFrom, direction };
		return collectionResult(ListItem.class, LocationsDao.class,
				"getSubSetOfLocations", params);
	}

	public CollectionServiceResponse<ListItem> getSubSetOfInpatientLocations(
			ISecurityContext securityContext, String startFrom, int direction) {
		setSecurityContext(securityContext);
		Object[] params = { startFrom, direction };
		return collectionResult(ListItem.class, LocationsDao.class,
				"getSubSetOfInpatientLocations", params);
	}

	public CollectionServiceResponse<ListItem> getSubSetOfNewLocations(
			ISecurityContext securityContext, String startFrom, int direction) {
		setSecurityContext(securityContext);
		Object[] params = { startFrom, direction };
		return collectionResult(ListItem.class, LocationsDao.class,
				"getSubSetOfNewLocations", params);
	}

	public CollectionServiceResponse<ListItem> listAllClinics(
			ISecurityContext securityContext) {
		setSecurityContext(securityContext);
		return collectionResult(ListItem.class, LocationsDao.class,
				"listAllClinics");
	}

	public CollectionServiceResponse<ListItem> getSubSetOfClinics(
			ISecurityContext securityContext, String startFrom, int direction) {
		setSecurityContext(securityContext);
		Object[] params = { startFrom, direction };
		return collectionResult(ListItem.class, LocationsDao.class,
				"getSubSetOfClinics", params);
	}

	// Note Titles RPCs
	public CollectionServiceResponse<ListItem> getTitlesForClass(
			ISecurityContext securityContext, String classIen,
			String startFrom, int direction) {
		setSecurityContext(securityContext);
		Object[] params = { classIen, startFrom, direction };
		return collectionResult(ListItem.class, NoteTitlesDao.class,
				"getTitlesForClass", params);
	}

	public CollectionServiceResponse<ListItem> subsetOfProgressNoteTitles(
			ISecurityContext securityContext, String startFrom, int direction) {
		setSecurityContext(securityContext);
		Object[] params = { startFrom, direction };
		return collectionResult(ListItem.class, NoteTitlesDao.class,
				"subsetOfProgressNoteTitles", params);
	}

	public CollectionServiceResponse<ListItem> subSetOfDCSummaryTitles(
			ISecurityContext securityContext, String startFrom, int direction) {
		setSecurityContext(securityContext);
		Object[] params = { startFrom, direction };
		return collectionResult(ListItem.class, NoteTitlesDao.class,
				"subSetOfDCSummaryTitles", params);
	}

	public CollectionServiceResponse<ListItem> subSetOfClinProcTitles(
			ISecurityContext securityContext, String startFrom, int direction,
			boolean idNoteTitlesOnly) {
		setSecurityContext(securityContext);
		Object[] params = { startFrom, direction, idNoteTitlesOnly };
		return collectionResult(ListItem.class, NoteTitlesDao.class,
				"subSetOfClinProcTitles", params);
	}

	// Order Items RPCs
	public CollectionServiceResponse<ListItem> getSubSetOfOrderItems(
			ISecurityContext securityContext, String startFrom, int direction,
			String xRef) {
		setSecurityContext(securityContext);
		Object[] params = { startFrom, direction, xRef };
		return collectionResult(ListItem.class, OrderItemsDao.class,
				"getSubSetOfOrderItems", params);
	}

	// Persons List RPCs
	public CollectionServiceResponse<ListItem> getSubSetOfPersons(
			ISecurityContext securityContext, String startFrom, int direction) {
		setSecurityContext(securityContext);
		Object[] params = { startFrom, direction };
		return collectionResult(ListItem.class, PersonsDao.class,
				"getSubSetOfPersons", params);
	}

	public CollectionServiceResponse<ListItem> getSubSetOfProviders(
			ISecurityContext securityContext, String startFrom, int direction) {
		setSecurityContext(securityContext);
		Object[] params = { startFrom, direction };
		return collectionResult(ListItem.class, PersonsDao.class,
				"getSubSetOfProviders", params);
	}

	public CollectionServiceResponse<ListItem> getSubSetOfProvidersWithClass(
			ISecurityContext securityContext, String startFrom, int direction,
			Date dateTime) {
		setSecurityContext(securityContext);
		Object[] params = { startFrom, direction, dateTime };
		return collectionResult(ListItem.class, PersonsDao.class,
				"getSubSetOfProvidersWithClass", params);
	}

	public CollectionServiceResponse<ListItem> listProvidersAll(
			ISecurityContext securityContext) {
		setSecurityContext(securityContext);
		return collectionResult(ListItem.class, PersonsDao.class,
				"listProvidersAll");
	}

	public CollectionServiceResponse<ListItem> getSubSetOfUsers(
			ISecurityContext securityContext, String startFrom, int direction) {
		setSecurityContext(securityContext);
		Object[] params = { startFrom, direction };
		return collectionResult(ListItem.class, PersonsDao.class,
				"getSubSetOfUsers", params);
	}

	public CollectionServiceResponse<ListItem> getSubSetOfUsersByType(
			ISecurityContext securityContext, String startFrom, int direction,
			String type) {
		setSecurityContext(securityContext);
		Object[] params = { startFrom, direction, type };
		return collectionResult(ListItem.class, PersonsDao.class,
				"getSubSetOfUsersByType", params);
	}

	public CollectionServiceResponse<ListItem> getSubSetOfUsersWithClass(
			ISecurityContext securityContext, String startFrom, int direction,
			String dateTime) {
		setSecurityContext(securityContext);
		Object[] params = { startFrom, direction, dateTime };
		return collectionResult(ListItem.class, PersonsDao.class,
				"getSubSetOfUsersWithClass", params);
	}

	public CollectionServiceResponse<ListItem> getSubSetOfActiveAndInactivePersons(
			ISecurityContext securityContext, String startFrom, int direction) {
		setSecurityContext(securityContext);
		Object[] params = { startFrom, direction };
		return collectionResult(ListItem.class, PersonsDao.class,
				"getSubSetOfActiveAndInactivePersons", params);
	}

	public CollectionServiceResponse<ListItem> getSubSetOfAttendings(
			ISecurityContext securityContext, String startFrom, int direction,
			Date dateTime, String docTypeIen, String docIen) throws Exception {
		setSecurityContext(securityContext);
		Object[] params = { startFrom, direction, dateTime, docTypeIen, docIen };
		return collectionResult(ListItem.class, PersonsDao.class,
				"getSubSetOfAttendings", params);
	}

	// Procedure Types RPCs
	public CollectionServiceResponse<String> odForProcedures(
			ISecurityContext securityContext) {
		setSecurityContext(securityContext);
		return collectionResult(String.class, ProceduresDao.class,
				"odForProcedures");
	}

	public CollectionServiceResponse<ListItem> getSubSetOfProcedures(
			ISecurityContext securityContext, String startFrom, int direction) {
		setSecurityContext(securityContext);
		Object[] params = { startFrom, direction };
		return collectionResult(ListItem.class, ProceduresDao.class,
				"getSubSetOfProcedures", params);
	}

	// Services RPCs
	public CollectionServiceResponse<ListItem> getServiceList(
			ISecurityContext securityContext, int purpose) {
		setSecurityContext(securityContext);
		return collectionResult(ListItem.class, ServicesDao.class,
				"getServiceList", purpose);
	}

	public CollectionServiceResponse<ListItem> getServiceListWithSynonyms(
			ISecurityContext securityContext, int purpose, String consultIen) {
		setSecurityContext(securityContext);
		Object[] params = { purpose, consultIen };
		return collectionResult(ListItem.class, ServicesDao.class,
				"getServiceListWithSynonyms", params);
	}

	public CollectionServiceResponse<ListItem> getSubSetOfServices(
			ISecurityContext securityContext, String startFrom, int direction) {
		setSecurityContext(securityContext);
		Object[] params = { startFrom, direction };
		return collectionResult(ListItem.class, ServicesDao.class,
				"getSubSetOfServices", params);
	}

	// Status Urgencies List RPCs
	public CollectionServiceResponse<ListItem> subSetOfStatus(
			ISecurityContext securityContext) {
		setSecurityContext(securityContext);
		return collectionResult(ListItem.class, StatusUrgenciesDao.class,
				"subSetOfStatus");
	}

	public CollectionServiceResponse<ListItem> subSetOfUrgencies(
			ISecurityContext securityContext, String consultIen) {
		setSecurityContext(securityContext);
		return collectionResult(ListItem.class, StatusUrgenciesDao.class,
				"subSetOfUrgencies", consultIen);
	}

}
