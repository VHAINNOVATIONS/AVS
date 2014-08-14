package gov.va.med.lom.avs.service;

import gov.va.med.lom.foundation.service.response.CollectionServiceResponse;
import gov.va.med.lom.foundation.service.response.ServiceResponse;

import gov.va.med.lom.avs.enumeration.DisclaimerTypeEnum;
import gov.va.med.lom.avs.enumeration.SortDirectionEnum;
import gov.va.med.lom.avs.enumeration.SortEnum;
import gov.va.med.lom.avs.enumeration.TranslationTypeEnum;
import gov.va.med.lom.avs.model.Division;
import gov.va.med.lom.avs.model.FacilityHealthFactor;
import gov.va.med.lom.avs.model.FacilityPrefs;
import gov.va.med.lom.avs.model.Language;
import gov.va.med.lom.avs.model.PvsClinicalReminder;
import gov.va.med.lom.avs.model.Service;
import gov.va.med.lom.avs.model.StringResource;
import gov.va.med.lom.avs.model.Translation;
import gov.va.med.lom.avs.model.Provider;
import gov.va.med.lom.avs.model.Clinic;
import gov.va.med.lom.avs.model.UsageLogMongo;
import gov.va.med.lom.avs.model.UserSettings;
import gov.va.med.lom.avs.model.VistaPrinter;
import gov.va.med.lom.avs.service.BaseService;
import gov.va.med.lom.avs.util.FilterProperty;
import gov.va.med.lom.avs.util.SheetConfig;

import gov.va.med.lom.vistabroker.lists.data.ListItem;
import gov.va.med.lom.vistabroker.patient.data.Patient;
import gov.va.med.lom.vistabroker.security.ISecurityContext;

import java.util.Date;
import java.util.List;

import javax.ejb.Local;

@Local
public interface SettingsService extends BaseService {

	// AGGREGATE SHEET CONFIG
	public abstract ServiceResponse<SheetConfig> getSheetConfig(
			String facilityNo, String locationIen, String providerDuz);

	// HEADER/FOOTER
	public abstract CollectionServiceResponse<String> getHeaderAndFooter(String facilityNo);
	public abstract ServiceResponse<Boolean> saveHeaderAndFooter(
			String facilityNo, String language, String header, String footer);
	
	// FACILITY PREFS 
	public abstract ServiceResponse<FacilityPrefs> getFacilityPrefs(String facilityNo);
	public abstract ServiceResponse<String> getTiuTitleIenForFacility(String facilityNo);
	public abstract ServiceResponse<String> getTiuNoteTextForFacilty(String facilityNo);
	public abstract ServiceResponse<Boolean> saveTiuNoteTextForFacility(String facilityNo, String text);
	public abstract ServiceResponse<String> getServiceDuz(String facilityNo);
	public abstract ServiceResponse<Integer> getUpcomingAppointmentsRange(String facilityNo);
	public abstract ServiceResponse<Boolean> saveFacilitySetting(String facilityNo, String setting, String value);
	
	// TRANSLATIONS
	public abstract CollectionServiceResponse<Translation> fetchTranslations(
			String facilityNo, String language, Integer start, Integer limit, SortEnum sort, 
			SortDirectionEnum dir, List<FilterProperty> filters);
	public abstract ServiceResponse<Long> fetchTranslationsCount(
			String facilityNo, String language, List<FilterProperty> filters);
	public abstract ServiceResponse<Translation> updateTranslation(
			String facilityNo, Long id, String translation);
	public abstract CollectionServiceResponse<String> translateStrings(
			String facilityNo, String language, TranslationTypeEnum type, List<String> stringsToTranslate);

  // SERVICES
  public abstract CollectionServiceResponse<Service> fetchServices(String facilityNo);
  public abstract ServiceResponse<Long> fetchServicesCount(String facilityNo);
  public abstract ServiceResponse<Service> addService(
      String facilityNo, String name, String location, String hours, String phone, String comment);
  public abstract ServiceResponse<Service> updateService(
      String facilityNo, Long id, String name, String location, String hours, String phone, String comment);
  public abstract void deleteService(Long id);

	// DISCLAIMERS
	public abstract ServiceResponse<String> fetchDisclaimers(
			String facilityNo, DisclaimerTypeEnum type, String ien);
	public abstract ServiceResponse<Boolean> saveDisclaimers(
			String facilityNo, DisclaimerTypeEnum type, String ien, String text);
	public abstract CollectionServiceResponse<ListItem> searchForClinics(
			ISecurityContext securityContext, String startFrom);
	
  // PRINTERS
	public abstract CollectionServiceResponse<VistaPrinter> getVistaPrinters(
	    ISecurityContext securityContext, String facilityNo);
	
	// PATIENTS
  public CollectionServiceResponse<Patient> listPatientsByWard(ISecurityContext securityContext, String wardIen);
  public CollectionServiceResponse<Patient> listPatientsByClinic(ISecurityContext securityContext, String clinicIen, 
                                                                 Date startDate, Date endDate);
	
	// DIVISIONS
	public abstract CollectionServiceResponse<Division> getDivisions(String facilityNo);
	public abstract ServiceResponse<String> getFacilityNoForDivision(String divisionNo);
	public abstract ServiceResponse<Division> getDefaultDivisionForUser(String facilityNo, String userDuz);
	
	// USER CLASSES
	 public abstract CollectionServiceResponse<String> getUserClasses(String facilityNo, String userDuz);
	
  // PROVIDERS
	public abstract ServiceResponse<Provider> getProvider(String facilityNo, String userDuz);	 
	
  // CLINICS
  public abstract ServiceResponse<Clinic> getClinic(String facilityNo, String userDuz, String clinicIen); 
  
  // PATIENTS
  public abstract ServiceResponse<gov.va.med.lom.avs.model.Patient> getPatient(String facilityNo, String userDuz, String patientDfn);
	 
	// USER SETTINGS
	public abstract ServiceResponse<UserSettings> getUserSettings(
	    String facilityNo, String userDuz);
	public abstract ServiceResponse<UserSettings> saveUserSettings(UserSettings userSettings);
	public abstract ServiceResponse<UserSettings> updateUserSettings(UserSettings userSettings);
	
  // STRING RESOURCES
	public abstract CollectionServiceResponse<StringResource> getAllStringResources();
  public abstract CollectionServiceResponse<StringResource> getStringResources(String facilityNo, String language);
  public abstract ServiceResponse<StringResource> getStringResource(String facilityNo, String name, String language);
  public abstract ServiceResponse<StringResource> saveStringResource(StringResource stringResource);  
  public abstract ServiceResponse<StringResource> updateStringResource(StringResource stringResource);	
	
  // LANGUAGE
  public abstract CollectionServiceResponse<Language> getLanguages();
  public abstract ServiceResponse<Language> getLanguageByAbbreviation(String abbreviation);  
  
  // HEALTH FACTORS
  public abstract CollectionServiceResponse<FacilityHealthFactor> getHealthFactorsForFacility(String facilityNo);
  public abstract CollectionServiceResponse<FacilityHealthFactor> getHealthFactorsByType(String facilityNo, String type);
  
	// USAGE LOG
	public abstract void saveUsageLog(UsageLogMongo usageLog);
	
  /* Pre-Visit Summary */
  public abstract CollectionServiceResponse<PvsClinicalReminder> findClinicalRemindersByStation(String stationNo);

}
