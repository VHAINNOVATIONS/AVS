package gov.va.med.lom.avs.service.impl;

import gov.va.med.lom.avs.dao.ClinicPrefsDao;
import gov.va.med.lom.avs.dao.FacilityPrefsDao;
import gov.va.med.lom.avs.dao.LanguageDao;
import gov.va.med.lom.avs.dao.ProviderPrefsDao;
import gov.va.med.lom.avs.dao.PvsClinicalRemindersDao;
import gov.va.med.lom.avs.dao.ServiceDao;
import gov.va.med.lom.avs.dao.StringResourcesDao;
import gov.va.med.lom.avs.dao.TranslationDao;
import gov.va.med.lom.avs.dao.UsageLogMongoDao;
import gov.va.med.lom.avs.dao.UserSettingsDao;
import gov.va.med.lom.avs.dao.ProvidersDao;
import gov.va.med.lom.avs.dao.ClinicsDao;
import gov.va.med.lom.avs.dao.PatientsDao;
import gov.va.med.lom.avs.dao.FacilityHealthFactorsDao;
import gov.va.med.lom.avs.dao.morphia.ProvidersDaoImpl;
import gov.va.med.lom.avs.dao.morphia.ClinicsDaoImpl;
import gov.va.med.lom.avs.dao.morphia.PatientsDaoImpl;
import gov.va.med.lom.avs.dao.morphia.UsageLogMongoDaoImpl;
import gov.va.med.lom.avs.enumeration.DisclaimerTypeEnum;
import gov.va.med.lom.avs.enumeration.SortDirectionEnum;
import gov.va.med.lom.avs.enumeration.SortEnum;
import gov.va.med.lom.avs.enumeration.TranslationTypeEnum;
import gov.va.med.lom.avs.model.ClinicPrefs;
import gov.va.med.lom.avs.model.Division;
import gov.va.med.lom.avs.model.FacilityPrefs;
import gov.va.med.lom.avs.model.Language;
import gov.va.med.lom.avs.model.Clinic;
import gov.va.med.lom.avs.model.Provider;
import gov.va.med.lom.avs.model.ProviderPrefs;
import gov.va.med.lom.avs.model.PvsClinicalReminder;
import gov.va.med.lom.avs.model.Service;
import gov.va.med.lom.avs.model.StringResource;
import gov.va.med.lom.avs.model.Translation;
import gov.va.med.lom.avs.model.UsageLogMongo;
import gov.va.med.lom.avs.model.UserSettings;
import gov.va.med.lom.avs.model.VistaPrinter;
import gov.va.med.lom.avs.model.FacilityHealthFactor;
import gov.va.med.lom.avs.service.BaseService;
import gov.va.med.lom.avs.service.SettingsService;
import gov.va.med.lom.avs.util.FilterProperty;
import gov.va.med.lom.avs.util.SheetConfig;

import gov.va.med.lom.foundation.service.response.CollectionServiceResponse;
import gov.va.med.lom.foundation.service.response.ServiceResponse;
import gov.va.med.lom.foundation.util.Precondition;
import gov.va.med.lom.javaUtils.misc.StringUtils;

import gov.va.med.lom.vistabroker.lists.data.ListItem;
import gov.va.med.lom.vistabroker.patient.data.Patient;
import gov.va.med.lom.vistabroker.security.ISecurityContext;
import gov.va.med.lom.vistabroker.security.SecurityContextFactory;
import gov.va.med.lom.vistabroker.service.ListsVBService;
import gov.va.med.lom.vistabroker.service.DdrVBService;
import gov.va.med.lom.vistabroker.service.MiscVBService;
import gov.va.med.lom.vistabroker.service.PatientVBService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Iterator;

import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

@Stateless(name="gov.va.med.lom.avs.SettingsService")
@Local(SettingsService.class)
@TransactionManagement(TransactionManagementType.BEAN)
public class SettingsServiceImpl extends BaseServiceImpl implements BaseService, SettingsService {

  protected static final Log log = LogFactory.getLog(SettingsServiceImpl.class);
  
  private static Hashtable<String, ISecurityContext> DDR_SEC_CTX_MAP 
      = new Hashtable<String, ISecurityContext>();
  
  private static Hashtable<String, Hashtable<String, Division>> FACILITY_DIVISIONS_MAP 
  = new Hashtable<String, Hashtable<String, Division>>();  
  
  private static Hashtable<String, String> DIVISIONS_FACILITY_MAP 
  = new Hashtable<String, String>();    
  
  private static final int DEF_UPCOMING_APPOINTMENTS_RANGE = 3;
  private static final String DEF_LANGUAGE = "en";
  
  private char[] capitalizingDelimiters = {' ', '/', ',', '&', '(', ')', '-'};

  @EJB
  protected ListsVBService listRpcs;
  
  @EJB
  protected PatientVBService patientRpcs;
  
  @EJB
  protected DdrVBService ddrRPCs;
  
  @EJB
  private MiscVBService miscRpcs;
  
  @EJB
  private ProviderPrefsDao providerPrefsDao;

  @EJB
  private ClinicPrefsDao clinicPrefsDao;

  @EJB
  private FacilityPrefsDao facilityPrefsDao;
  
  @EJB
  private TranslationDao translationDao;
  
  @EJB
  private ServiceDao serviceDao;

  @EJB
  private UserSettingsDao userSettingsDao;
  
  @EJB
  private StringResourcesDao stringResourcesDao;  

  @EJB
  private LanguageDao languageDao;
  
  @EJB
  private PvsClinicalRemindersDao pvsClinicalRemindersDao;
  
  @EJB
  private FacilityHealthFactorsDao facilityHealthFactorsDao;
  
  public SettingsServiceImpl() {}

  public ServiceResponse<SheetConfig> getSheetConfig(String facilityNo, String locationIen, String providerDuz) {

    Precondition.assertNotEmpty("facilityNo", facilityNo);

    SheetConfig sheetConfig = new SheetConfig();

    // retrieve and add Facility prefs
    FacilityPrefs facilityPrefs = getFacilityPrefs(facilityNo).getPayload();
    if (facilityPrefs != null) {
      facilityPrefs.assignSheetConfigValues(sheetConfig);
    }
    
    // retrieve and add Clinic prefs
    if (locationIen != null && !locationIen.isEmpty()) {
      ClinicPrefs clinicPrefs = this.clinicPrefsDao.find(locationIen, facilityNo);
      if (clinicPrefs != null) {
        clinicPrefs.assignSheetConfigValues(sheetConfig);
      }
    }
  
    // retrieve and add Provider prefs
    if (providerDuz != null && !providerDuz.isEmpty()) {
      ProviderPrefs providerPrefs = this.providerPrefsDao.find(providerDuz, facilityNo);
      if (providerPrefs != null) {
        providerPrefs.assignSheetConfigValues(sheetConfig);
      }
    }

    ServiceResponse<SheetConfig> response = new ServiceResponse<SheetConfig>();
    response.setPayload(sheetConfig);

    return response;  
  }

  public ServiceResponse<String> getFacilityNoForDivision(String divisionNo) {
    
    if (!DIVISIONS_FACILITY_MAP.containsKey(divisionNo)) {
      this.getDivisions(divisionNo);
    }
    ServiceResponse<String> response = new ServiceResponse<String>();
    response.setPayload(DIVISIONS_FACILITY_MAP.get(divisionNo));
    return response;
  }
  
  public ServiceResponse<FacilityPrefs> getFacilityPrefs(String facilityNo) {

    Precondition.assertNotEmpty("facilityNo", facilityNo);
    
    ServiceResponse<FacilityPrefs> response = new ServiceResponse<FacilityPrefs>();
    FacilityPrefs facilityPrefs = this.facilityPrefsDao.find(facilityNo);
    if (facilityPrefs == null) {
      facilityNo = getFacilityNoForDivision(facilityNo).getPayload();
      facilityPrefs = this.facilityPrefsDao.find(facilityNo);
    }
    
    response.setPayload(facilityPrefs);
    
    return response;
  }
  
  public CollectionServiceResponse<String> getHeaderAndFooter(String facilityNo) {

    FacilityPrefs facilityPrefs = getFacilityPrefs(facilityNo).getPayload();

    Collection<String> headerAndFooter = new ArrayList<String>();
    if (facilityPrefs == null) {
      headerAndFooter.add("After Visit Summary<br />%PATIENT_NAME%<br />%ENCOUNTER_DATETIME%");
      headerAndFooter.add("Visit www.myhealth.va.gov!");
    } else {
      headerAndFooter.add(facilityPrefs.getHeader());
      headerAndFooter.add(facilityPrefs.getFooter());
    }
    
    CollectionServiceResponse<String> response = new CollectionServiceResponse<String>();
    response.setCollection(headerAndFooter);

    return response;  
  }
  
  public ServiceResponse<Boolean> saveHeaderAndFooter(String facilityNo, String language, String header, String footer) {
    
    FacilityPrefs facilityPrefs = getFacilityPrefs(facilityNo).getPayload();

    boolean existingPrefs = true;
    if (facilityPrefs == null) {
      facilityPrefs = new FacilityPrefs();
      facilityPrefs.setFacilityNo(facilityNo);
      existingPrefs = false;
    }
    
    facilityPrefs.setHeader(header);
    facilityPrefs.setFooter(footer);

    if (existingPrefs) {
      this.facilityPrefsDao.update(facilityPrefs);
    } else {
      this.facilityPrefsDao.save(facilityPrefs);
    }
    
    ServiceResponse<Boolean> response = new ServiceResponse<Boolean>();
    response.setPayload(true);

    return response;  
  }

  public ServiceResponse<Boolean> saveFacilitySetting(String facilityNo, String setting, String value) {
   
     if (setting.equals("avsPrintedHF")) {
       boolean existingHF = true;
       List<FacilityHealthFactor> list = this.facilityHealthFactorsDao.findByType(facilityNo, "avs_printed");
       FacilityHealthFactor hf = null;
       if (!list.isEmpty()) {
         hf = list.get(0);
       } else {
         existingHF = false;
         hf = new FacilityHealthFactor();
         hf.setStationNo(facilityNo);
         hf.setType("avs_printed");
       }
       hf.setIen(StringUtils.piece(value, ';', 1));
       hf.setValue(StringUtils.piece(value, ';', 2));
       if (existingHF) {
         this.facilityHealthFactorsDao.update(hf);
       } else {
         this.facilityHealthFactorsDao.save(hf);
       }
     } else {
     
      FacilityPrefs facilityPrefs = getFacilityPrefs(facilityNo).getPayload();
  
      boolean existingPrefs = true;
      if (facilityPrefs == null) {
        facilityPrefs = new FacilityPrefs();
        facilityPrefs.setFacilityNo(facilityNo);
        existingPrefs = false;
      }
      
      if (setting.equals("timeZone")) {
        facilityPrefs.setTimeZone(value);
      } else if (setting.equals("tiuTitleIen")) {
        facilityPrefs.setTiuTitleIen(value);
      } else if (setting.equals("kramesEnabled")) {
        facilityPrefs.setKramesEnabled(StringUtils.strToBool(value.toLowerCase(), "yes"));
      } else if (setting.equals("servicesEnabled")) {
        facilityPrefs.setServicesEnabled(StringUtils.strToBool(value.toLowerCase(), "yes"));
      } else if (setting.equals("refreshFrequency")) {
        facilityPrefs.setRefreshFrequency(StringUtils.toInt(value, 180000));
      } else if (setting.equals("upcomingAppointmentRange")) {
        facilityPrefs.setUpcomingAppointmentRange(StringUtils.toInt(value, 3));
      } else if (setting.equals("orderTimeFrom")) {
        facilityPrefs.setOrderTimeFrom(StringUtils.toInt(value, 30));
      } else if (setting.equals("orderTimeThru")) {
        facilityPrefs.setOrderTimeThru(StringUtils.toInt(value, 180));
      }
      
      if (existingPrefs) {
        this.facilityPrefsDao.update(facilityPrefs);
      } else {
        this.facilityPrefsDao.save(facilityPrefs);
      }    
     }
     
    ServiceResponse<Boolean> response = new ServiceResponse<Boolean>();
    response.setPayload(true);

    return response;  
  }
  
  public ServiceResponse<String> getTiuTitleIenForFacility(String facilityNo) {
    
    ServiceResponse<String> sr = new ServiceResponse<String>();
    
    FacilityPrefs facilityPrefs = getFacilityPrefs(facilityNo).getPayload();

    if (facilityPrefs != null) {
      sr.setPayload(facilityPrefs.getTiuTitleIen());
    }
    
    return sr;
  }
  
  public ServiceResponse<String> getTiuNoteTextForFacilty(String facilityNo) {
    
    ServiceResponse<String> sr = new ServiceResponse<String>();
    
    FacilityPrefs facilityPrefs = getFacilityPrefs(facilityNo).getPayload();

    if (facilityPrefs != null) {
      sr.setPayload(facilityPrefs.getTiuNoteText());
    }
    
    return sr;
  }
  
  public ServiceResponse<Boolean> saveTiuNoteTextForFacility(String facilityNo, String text) {
    
    FacilityPrefs facilityPrefs = getFacilityPrefs(facilityNo).getPayload();

    boolean existingPrefs = true;
    if (facilityPrefs == null) {
      facilityPrefs = new FacilityPrefs();
      facilityPrefs.setFacilityNo(facilityNo);
      existingPrefs = false;
    }
    
    facilityPrefs.setTiuNoteText(text);

    if (existingPrefs) {
      this.facilityPrefsDao.update(facilityPrefs);
    } else {
      this.facilityPrefsDao.save(facilityPrefs);
    }  
    
    ServiceResponse<Boolean> response = new ServiceResponse<Boolean>();
    response.setPayload(true);

    return response;  
  }  
  
  public ServiceResponse<String> getServiceDuz(String facilityNo) {
    
    ServiceResponse<String> sr = new ServiceResponse<String>();
      
    FacilityPrefs facilityPrefs = getFacilityPrefs(facilityNo).getPayload();

    if (facilityPrefs != null) {
      sr.setPayload(facilityPrefs.getServiceDuz());
    }
      
    return sr;
  }  
  
  public ServiceResponse<Integer> getUpcomingAppointmentsRange(String facilityNo) {
    
    ServiceResponse<Integer> sr = new ServiceResponse<Integer>();
    
    FacilityPrefs facilityPrefs = getFacilityPrefs(facilityNo).getPayload();

    if ((facilityPrefs != null) && (facilityPrefs.getUpcomingAppointmentRange() > 0)) {
      sr.setPayload(facilityPrefs.getUpcomingAppointmentRange());
    } else {
      sr.setPayload(DEF_UPCOMING_APPOINTMENTS_RANGE);
    }
    
    return sr;
  }
  
  public CollectionServiceResponse<Translation> fetchTranslations(String facilityNo, String language, Integer start, Integer limit, 
      SortEnum sort, SortDirectionEnum dir, List<FilterProperty> filters) {
    
    facilityNo = this.getFacilityNoForDivision(facilityNo).getPayload();
    Language lang = languageDao.findByAbbreviation(language != null ? language : DEF_LANGUAGE);
    
    List<Translation> translations = this.translationDao.fetchListForEditor(facilityNo, lang.getId(), start, limit, sort, dir, filters);
    
    CollectionServiceResponse<Translation> response = new CollectionServiceResponse<Translation>();
    response.setCollection(translations);
    
    return response;
  }
  
  public ServiceResponse<Long> fetchTranslationsCount(String facilityNo, String language, List<FilterProperty> filters) {
    
    facilityNo = this.getFacilityNoForDivision(facilityNo).getPayload();
    Language lang = languageDao.findByAbbreviation(language != null ? language : DEF_LANGUAGE);
    
    Long totalCount = this.translationDao.fetchTotalCount(facilityNo, lang.getId(), filters);

    ServiceResponse<Long> response = new ServiceResponse<Long>();
    response.setPayload(totalCount);
    
    return response;
  }
  

  public ServiceResponse<Translation> updateTranslation(String facilityNo, Long id, String translationText) {

    Precondition.assertNotEmpty("facilityNo", facilityNo);
    Precondition.assertPositive("id", id);
    
    facilityNo = this.getFacilityNoForDivision(facilityNo).getPayload();

    Translation translation = this.translationDao.find(facilityNo, id);
    if (translation == null) {
      throw new RuntimeException("Translation with ID '" + id + "' does not exist");
    }

    translation.setTranslation(translationText);
    this.translationDao.update(translation);

    ServiceResponse<Translation> response = new ServiceResponse<Translation>();
    response.setPayload(translation);

    return response;  
  }

  public CollectionServiceResponse<String> translateStrings(String facilityNo, String language, 
      TranslationTypeEnum type, List<String> stringsToTranslate) {
    
    if (stringsToTranslate == null || stringsToTranslate.isEmpty()) {
      return new CollectionServiceResponse<String>();
    }
    
    facilityNo = this.getFacilityNoForDivision(facilityNo).getPayload();
    Language lang = languageDao.findByAbbreviation(language != null ? language : DEF_LANGUAGE);
    
    List<Translation> translations = this.translationDao
        .fetchListByTypeAndSource(facilityNo, lang.getId(), type, stringsToTranslate);

    List<String> translatedStrings = new ArrayList<String>();
    for (int i = 0; i < stringsToTranslate.size(); i++) {
      String source = stringsToTranslate.get(i);

      Boolean foundTranslation = false;
      for (Translation translation : translations) {

        if (translation.getSource().equals(source)) {
          String translationText = translation.getTranslation();

          if (translationText == null || translationText.isEmpty()) {
            translationText = source;
          }

          translatedStrings.add(translationText);

          foundTranslation = true;
          break;
        }
      }

      if (!foundTranslation) {
        this.createTranslation(facilityNo, lang.getId(), type, source, null);
        translatedStrings.add(source);
      }
    }

    CollectionServiceResponse<String> response = new CollectionServiceResponse<String>();
    response.setCollection(translatedStrings);
    
    return response;
  }

  private Translation createTranslation(
      String facilityNo, long languageId, TranslationTypeEnum type, String sourceText, String translationText) {

    Precondition.assertNotEmpty("facilityNo", facilityNo);
    Precondition.assertNotEmpty("sourceText", sourceText);
    Precondition.assertNotNull("type", type);

    facilityNo = this.getFacilityNoForDivision(facilityNo).getPayload();
    
    Translation translation = this.translationDao.findBySource(facilityNo, languageId, sourceText);
    if (translation == null) {
      Language language = this.languageDao.findById(languageId);
      translation = new Translation(facilityNo, language, type, sourceText);
    }

    if (translationText != null && translationText.isEmpty()) {
      translationText = null;
    }

    translation.setTranslation(translationText);
    if (translation.getId() == null) {
      this.translationDao.save(translation);
    } else {
      this.translationDao.update(translation);
    }

    return translation;
  }
  
  public CollectionServiceResponse<Service> fetchServices(String facilityNo) {
    
    List<Service> services = this.serviceDao.fetchListForEditor(facilityNo);
        if (services.isEmpty()) {
      facilityNo = this.getFacilityNoForDivision(facilityNo).getPayload();
      services = this.serviceDao.fetchListForEditor(facilityNo);
    }
        
    CollectionServiceResponse<Service> response = new CollectionServiceResponse<Service>();
    response.setCollection(services);
    
    return response;
  }

  public ServiceResponse<Long> fetchServicesCount(String facilityNo) {
    
    Long totalCount = this.serviceDao.fetchTotalCount(facilityNo);
    if (totalCount == 0) {
      facilityNo = this.getFacilityNoForDivision(facilityNo).getPayload();
      totalCount = this.serviceDao.fetchTotalCount(facilityNo);
    }
    
    ServiceResponse<Long> response = new ServiceResponse<Long>();
    response.setPayload(totalCount);
    
    return response;    
  }
  
  public ServiceResponse<Service> addService(
      String facilityNo, String name, String location, String hours, String phone, String comment) {

    Precondition.assertNotEmpty("facilityNo", facilityNo);
    Precondition.assertNotEmpty("name", name);
    Precondition.assertNotNull("location", location);
    
    Service service = this.serviceDao.findByName(facilityNo, name);
    if (service == null) {
      facilityNo = this.getFacilityNoForDivision(facilityNo).getPayload();
      service = this.serviceDao.findByName(facilityNo, name);
      if (service == null) {
        service = new Service(facilityNo, name, location, hours, phone, comment);
      }
    }
    
    service.setName(name);
    service.setLocation(location);
    service.setHours(hours);
    service.setPhone(phone);
    service.setComment(comment);
    
    this.serviceDao.save(service);

    ServiceResponse<Service> response = new ServiceResponse<Service>();
    response.setPayload(service);

    return response;   
  }

  public ServiceResponse<Service> updateService(String facilityNo, Long id, String name, 
      String location, String hours, String phone, String comment) {
    
    Precondition.assertNotEmpty("facilityNo", facilityNo);
    Precondition.assertPositive("id", id);

    boolean existingService = true;
    Service service = this.serviceDao.find(facilityNo, id);
    if (service == null) {
      facilityNo = this.getFacilityNoForDivision(facilityNo).getPayload();
      service = this.serviceDao.find(facilityNo, id);
      if (service == null) {
        throw new RuntimeException("Service with ID '" + id + "' does not exist");
      }
      existingService = false;
    }
  
    service.setName(name);
    service.setLocation(location);
    service.setHours(hours);
    service.setPhone(phone);
    service.setComment(comment);
    
    if (existingService) {
      this.serviceDao.update(service);
    } else {
      this.serviceDao.save(service);
    }

    ServiceResponse<Service> response = new ServiceResponse<Service>();
    response.setPayload(service);

    return response;      
  }
  
  public void deleteService(Long id) {
    Precondition.assertPositive("id", id);
    
    Service service = this.serviceDao.findById(id);
    if (service == null) {
      throw new RuntimeException("Service with ID '" + id + "' does not exist");
    }
    
    this.serviceDao.remove(id);
  }
  
  public ServiceResponse<String> fetchDisclaimers(String facilityNo, DisclaimerTypeEnum type, String ien) {
    String disclaimers = null;

    switch (type) {
      case facility:
        FacilityPrefs facilityPrefs = getFacilityPrefs(facilityNo).getPayload();
        if (facilityPrefs != null) {
          disclaimers = facilityPrefs.getBoilerplate();
        }
        
        break;
      case clinic:
        ClinicPrefs clinicPrefs = this.clinicPrefsDao.find(ien, facilityNo);
        if (clinicPrefs != null) {
          disclaimers = clinicPrefs.getBoilerplate();
        }

        break;
      case provider:
        ProviderPrefs providerPrefs = this.providerPrefsDao.find(ien, facilityNo);
        if (providerPrefs != null) {
          disclaimers = providerPrefs.getBoilerplate();
        }
        
        break;
    }

    ServiceResponse<String> response = new ServiceResponse<String>();
    response.setPayload(disclaimers);

    return response;
  }

  public ServiceResponse<Boolean> saveDisclaimers(String facilityNo, DisclaimerTypeEnum type, String ien, String text) {
    boolean existingDisclaimer = true;
    switch (type) {
      case facility:
        FacilityPrefs facilityPrefs = getFacilityPrefs(facilityNo).getPayload();
        if (facilityPrefs == null) {
          facilityPrefs = new FacilityPrefs(facilityNo);
          existingDisclaimer = false;
        }
        facilityPrefs.setBoilerplate(text);
        if (existingDisclaimer) {
          this.facilityPrefsDao.update(facilityPrefs);
        } else {
          this.facilityPrefsDao.save(facilityPrefs);
        }
        
        break;
      case clinic:
        ClinicPrefs clinicPrefs = this.clinicPrefsDao.find(ien, facilityNo);
        if (clinicPrefs == null) {
          clinicPrefs = new ClinicPrefs(ien, facilityNo);
          existingDisclaimer = false;
        }
        clinicPrefs.setBoilerplate(text);
        if (existingDisclaimer) {
          this.clinicPrefsDao.update(clinicPrefs);
        } else {
          this.clinicPrefsDao.save(clinicPrefs);
        }        
  
        break;
      case provider:
        ProviderPrefs providerPrefs = this.providerPrefsDao.find(ien, facilityNo);
        if (providerPrefs == null) {
          providerPrefs = new ProviderPrefs(ien, facilityNo);
          existingDisclaimer = false;
        }
        providerPrefs.setBoilerplate(text);
        if (existingDisclaimer) {
          this.providerPrefsDao.update(providerPrefs);
        } else {
          this.providerPrefsDao.save(providerPrefs);
        }         
        
        break;
    }
    
    ServiceResponse<Boolean> response = new ServiceResponse<Boolean>();
    response.setPayload(true);
  
    return response;
  }
  
  public CollectionServiceResponse<ListItem> searchForClinics(ISecurityContext securityContext, String startFrom) {

    CollectionServiceResponse<ListItem> vistaResponse = this.listRpcs.getSubSetOfClinics(securityContext, startFrom.toUpperCase(), 1);
    super.checkVistaExceptions(vistaResponse);
    Collection<ListItem> clinics = vistaResponse.getCollection();

    CollectionServiceResponse<ListItem> response = new CollectionServiceResponse<ListItem>();
    response.setCollection(clinics);
    
    return response;
  }  
  
  public CollectionServiceResponse<Division> getDivisions(String facilityNo) {
    CollectionServiceResponse<Division> csr = new CollectionServiceResponse<Division>();
    
    if (FACILITY_DIVISIONS_MAP.containsKey(facilityNo)) {
      Hashtable<String, Division> divisionsHT = FACILITY_DIVISIONS_MAP.get(facilityNo);
      List<Division> divisions = new ArrayList<Division>();
      Iterator<Division> it = divisionsHT.values().iterator();
      while (it.hasNext()) {
        divisions.add(it.next());
      }
      csr.setCollection(divisions);
      return csr;
    }
    
    FacilityPrefs facilityPrefs = this.facilityPrefsDao.find(facilityNo);
    ISecurityContext ddrSecCtx = SecurityContextFactory.createDuzSecurityContext(facilityNo, facilityPrefs.getServiceDuz());
    
    List<String> results = (List<String>)ddrRPCs.execDdrLister(ddrSecCtx, "40.8", null, ".01;.07;1", "IP", null, 
                                                 null, null, "#", null, null, null, null, null).getCollection();
    List<Division> divisions = new ArrayList<Division>();
    for (String x : results) {
      Division division = new Division(StringUtils.piece(x, 3), StringUtils.piece(x, 4), StringUtils.piece(x, 2));
      divisions.add(division);
    }
    Hashtable<String, Division> divisonsHT = new Hashtable<String, Division>();
    for (Division division : divisions) {
      divisonsHT.put(division.getInsitutionId(), division);
      DIVISIONS_FACILITY_MAP.put(division.getFacilityNo(), facilityNo);
    }
    FACILITY_DIVISIONS_MAP.put(facilityNo, divisonsHT);
    csr.setCollection(divisions);
    return csr;
  }
  
  public ServiceResponse<Division> getDefaultDivisionForUser(String facilityNo, String userDuz) {
    this.getDivisions(facilityNo);
    Hashtable<String, Division> divisionsHT = FACILITY_DIVISIONS_MAP.get(facilityNo);
    
    ISecurityContext ddrSecCtx = getServiceSecurityContext(facilityNo, userDuz);
    
    Division division = null;
    CollectionServiceResponse<String> csr = ddrRPCs.execDdrLister(ddrSecCtx, "200.02", "," + userDuz + ",", ".01E;1", "IP", null, 
        null, null, "#", "I $D(^DIC(4,$P(^(0),U,1),0))'=0", null, null, null, null);
    List<String> list = (List<String>)csr.getCollection();
    for (String x : list) {
      String s = StringUtils.piece(x, 3);
      boolean isDef = s.equals("") || s.equals("1");
      if (isDef) {
        String instNo = StringUtils.piece(x, 1);
        division = divisionsHT.get(instNo);
      }
    }
    
    if (division == null) {
      division = new Division(facilityNo, facilityNo, ""); 
    }
    
    ServiceResponse<Division> sr = new ServiceResponse<Division>();
    sr.setPayload(division);
    return sr;
  }
  
  public CollectionServiceResponse<String> getUserClasses(String facilityNo, String userDuz) {
    
    CollectionServiceResponse<String> response = new CollectionServiceResponse<String>();
    List<String> userClasses = new ArrayList<String>();
    response.setCollection(userClasses);
    
    ISecurityContext ddrSecCtx = getServiceSecurityContext(facilityNo, userDuz);
    
    CollectionServiceResponse<String> csr = ddrRPCs.execDdrLister(ddrSecCtx, "8930.3P", null, ".02;.02E", "IP", null, 
        null, null, "#", "I $P(^(0),U,1)=\"" + userDuz + "\"", null, null, null, null);
    List<String> list = (List<String>)csr.getCollection();
    for (String x : list) {
      String s1 = StringUtils.piece(x, 2);
      String s2 = StringUtils.piece(x, 3);
      userClasses.add(s1 + "^" + s2);
    }
    return response;
  }  
  
  public ServiceResponse<Provider> getProvider(String facilityNo, String providerDuz) {
    
    Provider provider = null;
    ProvidersDao providersDao = null;
    ServiceResponse<Provider> sr = new ServiceResponse<Provider>();
    try {
      providersDao = new ProvidersDaoImpl();
      provider = providersDao.find(facilityNo, providerDuz);
      if (provider == null) {
        // get the user title
        try {
          ISecurityContext ddrSecCtx = getServiceSecurityContext(facilityNo, providerDuz);
          ddrSecCtx.setSocketTimeout(60000);
          String arg = "@\"^VA(200," + providerDuz + ",0)\"";
          String rtn = this.miscRpcs.getVariableValue(ddrSecCtx, arg).getPayload();
          String name = StringUtils.piece(rtn, 1);
          String titleIen = StringUtils.piece(rtn, 9);
          arg = "@\"^DIC(3.1," + titleIen + ",0)\"";
          String title = this.miscRpcs.getVariableValue(ddrSecCtx, arg).getPayload();
          provider = new Provider();
          provider.setDuz(providerDuz);
          provider.setStationNo(facilityNo);
          provider.setName(name);
          provider.setTitle(StringUtils.piece(title, 1));
          providersDao.save(provider);
        } catch(Exception e) {
        }          
      }
      sr.setPayload(provider);
    } finally {
      try {
        providersDao.closeConnections();
      } catch (Exception e) {}
    }      

    return sr;
  }
  
  // CLINICS
  public ServiceResponse<Clinic> getClinic(String facilityNo, String userDuz, String clinicIen) {
    
    Clinic clinic = null;
    ClinicsDao clinicsDao = null;
    ServiceResponse<Clinic> sr = new ServiceResponse<Clinic>();
    try {
      clinicsDao = new ClinicsDaoImpl();
      clinic = clinicsDao.find(facilityNo, clinicIen);
      if (clinic == null) {
        // get the clinic name
        try {
          ISecurityContext ddrSecCtx = getServiceSecurityContext(facilityNo, userDuz);
          ddrSecCtx.setSocketTimeout(60000);
          String arg = "@\"^SC(" + clinicIen + ",0)\"";
          String rtn = miscRpcs.getVariableValue(ddrSecCtx, arg).getPayload();
          String name = StringUtils.piece(rtn, 1);
          clinic = new Clinic();
          clinic.setIen(clinicIen);
          clinic.setStationNo(facilityNo);
          clinic.setName(name);
          clinicsDao.save(clinic);
        } catch(Exception e) {
        }          
      }
      sr.setPayload(clinic);
    } finally {
      try {
        clinicsDao.closeConnections();
      } catch (Exception e) {}
    }      

    return sr;    
  }
  
  // CLINICS
  public ServiceResponse<gov.va.med.lom.avs.model.Patient> getPatient(String facilityNo, String userDuz, String patientDfn) {
    
    gov.va.med.lom.avs.model.Patient patient = null;
    PatientsDao patientsDao = null;
    ServiceResponse<gov.va.med.lom.avs.model.Patient> sr = 
        new ServiceResponse<gov.va.med.lom.avs.model.Patient>();
    try {
      patientsDao = new PatientsDaoImpl();
      patient = patientsDao.find(facilityNo, patientDfn);
      if (patient == null) {
        // get the patient name
        try {
          ISecurityContext ddrSecCtx = getServiceSecurityContext(facilityNo, userDuz);
          ddrSecCtx.setSocketTimeout(60000);
          String arg = "@\"^DPT(" + patientDfn + ",0)\"";
          String rtn = miscRpcs.getVariableValue(ddrSecCtx, arg).getPayload();
          String name = StringUtils.piece(rtn, 1);
          patient = new gov.va.med.lom.avs.model.Patient();
          patient.setDfn(patientDfn);
          patient.setStationNo(facilityNo);
          patient.setName(name);
          patientsDao.save(patient);
        } catch(Exception e) {
        }          
      }
      sr.setPayload(patient);
    } finally {
      try {
        patientsDao.closeConnections();
      } catch (Exception e) {}
    }      

    return sr;    
  }  
  
  public CollectionServiceResponse<VistaPrinter> getVistaPrinters(ISecurityContext securityContext, String facilityNo) {
    
    ISecurityContext ddrSecCtx = getServiceSecurityContext(securityContext.getDivision(), securityContext.getUserId());
    
    CollectionServiceResponse<VistaPrinter> csr = new CollectionServiceResponse<VistaPrinter>();
    List<String> results = (List<String>)ddrRPCs.execDdrLister(ddrSecCtx, "3.5", null, ".01;.02;9;11;65", "IP", null, 
                                                 null, null, "#", null, null, null, null, null).getCollection();
    List<VistaPrinter> vistaPrinters = new ArrayList<VistaPrinter>();
    for (String x : results) {
      String ipAddress = StringUtils.piece(x, 6);
      if (!ipAddress.isEmpty()) {
      VistaPrinter vp = new VistaPrinter();
        vp.setIen(StringUtils.piece(x, 1));
        vp.setName(StringUtils.piece(x, 2));
        vp.setLocation(StringUtils.piece(x, 3));
        vp.setDisplayName(vp.getLocation() + " <" + vp.getName() + ">");
        vp.setRightMargin(StringUtils.toInt(StringUtils.piece(x, 4), 0));
        vp.setPageLength(StringUtils.toInt(StringUtils.piece(x, 5), 0));
        vp.setIpAddress(ipAddress);
        vistaPrinters.add(vp);
      }
    }
    csr.setCollection(vistaPrinters);
    return csr;
  }  
  
  public CollectionServiceResponse<Patient> listPatientsByWard(ISecurityContext securityContext, String wardIen) {
    
    ISecurityContext ddrSecCtx = getServiceSecurityContext(securityContext.getDivision(), securityContext.getUserId());
    
    return patientRpcs.listPtByWard(ddrSecCtx, wardIen);
  }
  
  
  public CollectionServiceResponse<Patient> listPatientsByClinic(ISecurityContext securityContext, String clinicIen, 
                                                                 Date startDate, Date endDate) {
    
    ISecurityContext ddrSecCtx = getServiceSecurityContext(securityContext.getDivision(), securityContext.getUserId());
    
    return patientRpcs.listPtByClinic(ddrSecCtx, clinicIen, startDate, endDate);
  }
  
  public void saveUsageLog(UsageLogMongo usageLog) {

    UsageLogMongoDao usageLogMongoDao = new UsageLogMongoDaoImpl();
    usageLogMongoDao.save(usageLog);
  }
  
  public ServiceResponse<UserSettings> getUserSettings(String facilityNo, String userDuz) {
    
    Precondition.assertNotEmpty("facilityNo", facilityNo);
    Precondition.assertNotEmpty("userDuz", userDuz);
    
    facilityNo = this.getFacilityNoForDivision(facilityNo).getPayload();
    
    UserSettings userSettings = this.userSettingsDao.find(facilityNo, userDuz);
    ServiceResponse<UserSettings> sr = new ServiceResponse<UserSettings>();
    sr.setPayload(userSettings);
    
    return sr;
  }
  
  public ServiceResponse<UserSettings> saveUserSettings(UserSettings userSettings) {
    
    Precondition.assertNotNull("userSettings", userSettings);

    userSettings = this.userSettingsDao.save(userSettings);
    ServiceResponse<UserSettings> sr = new ServiceResponse<UserSettings>();
    sr.setPayload(userSettings);
    
    return sr;
    
  }
  
  public ServiceResponse<UserSettings> updateUserSettings(UserSettings userSettings) {
    
    Precondition.assertNotNull("userSettings", userSettings);
    
    userSettings = this.userSettingsDao.update(userSettings);
    ServiceResponse<UserSettings> sr = new ServiceResponse<UserSettings>();
    sr.setPayload(userSettings);
    
    return sr;    
  }
  
  public CollectionServiceResponse<StringResource> getAllStringResources() {
    
    List<StringResource> stringResources = this.stringResourcesDao.findAll();
    CollectionServiceResponse<StringResource> sr = new CollectionServiceResponse<StringResource>();
    sr.setCollection(stringResources);
    
    return sr;    
  }
  
  public CollectionServiceResponse<StringResource> getStringResources(String facilityNo, String language) {
    Precondition.assertNotEmpty("facilityNo", facilityNo);
    Precondition.assertNotEmpty("language", language);
    
    facilityNo = this.getFacilityNoForDivision(facilityNo).getPayload();
    
    List<StringResource> stringResources = this.stringResourcesDao.findForStation(facilityNo, language);
    CollectionServiceResponse<StringResource> sr = new CollectionServiceResponse<StringResource>();
    sr.setCollection(stringResources);
    
    return sr;    
  }
  
  public ServiceResponse<StringResource> getStringResource(String facilityNo, String name, String language) {
    Precondition.assertNotEmpty("facilityNo", facilityNo);
    Precondition.assertNotEmpty("name", name);
    Precondition.assertNotEmpty("language", language);
    
    facilityNo = this.getFacilityNoForDivision(facilityNo).getPayload();
    
    StringResource stringResource = this.stringResourcesDao.findByName(facilityNo, name, language);
    ServiceResponse<StringResource> sr = new ServiceResponse<StringResource>();
    sr.setPayload(stringResource);
    
    return sr;    
  }  
  
  public ServiceResponse<StringResource> saveStringResource(StringResource stringResource) {
    
    Precondition.assertNotNull("stringResource", stringResource);

    stringResource = this.stringResourcesDao.save(stringResource);
    ServiceResponse<StringResource> sr = new ServiceResponse<StringResource>();
    sr.setPayload(stringResource);
    
    return sr;    
    
  }
  
  public ServiceResponse<StringResource> updateStringResource(StringResource stringResource) {
    
    Precondition.assertNotNull("stringResource", stringResource);

    stringResource = this.stringResourcesDao.update(stringResource);
    ServiceResponse<StringResource> sr = new ServiceResponse<StringResource>();
    sr.setPayload(stringResource);
    
    return sr;      
  }
  
  public CollectionServiceResponse<Language> getLanguages() {
    
    CollectionServiceResponse<Language> csr = new CollectionServiceResponse<Language>();
    List<Language> languages = languageDao.findAll();
    csr.setCollection(languages);
    
    return csr;
    
  }
  
  public ServiceResponse<Language> getLanguageByAbbreviation(String abbreviation) {
    
    Precondition.assertNotEmpty("abbreviation", abbreviation);
    
    ServiceResponse<Language> sr = new ServiceResponse<Language>();
    Language language = languageDao.findByAbbreviation(abbreviation);
    sr.setPayload(language);
    
    return sr;
  }
  
  public CollectionServiceResponse<FacilityHealthFactor> getHealthFactorsForFacility(String facilityNo) {
    List<FacilityHealthFactor> list = this.facilityHealthFactorsDao.find(facilityNo);
    CollectionServiceResponse<FacilityHealthFactor> csr = new CollectionServiceResponse<FacilityHealthFactor>();
    csr.setCollection(list);
     
    return csr;
  }  
  
  public CollectionServiceResponse<FacilityHealthFactor> getHealthFactorsByType(String facilityNo, String type) {
    List<FacilityHealthFactor> list = this.facilityHealthFactorsDao.findByType(facilityNo, type);
    CollectionServiceResponse<FacilityHealthFactor> csr = new CollectionServiceResponse<FacilityHealthFactor>();
    csr.setCollection(list);
     
    return csr;
  }
  
  private ISecurityContext getServiceSecurityContext(String facilityNo, String userDuz) {
    
    if (!DDR_SEC_CTX_MAP.containsKey(facilityNo)) {
      ISecurityContext secCtx = null;
      String serviceDuz = this.getServiceDuz(facilityNo).getPayload();
      if (serviceDuz != null) {
        secCtx = SecurityContextFactory.createDuzSecurityContext(facilityNo, serviceDuz);
      } else {
        secCtx = SecurityContextFactory.createDuzSecurityContext(facilityNo, userDuz);
      }
      DDR_SEC_CTX_MAP.put(facilityNo, secCtx);
    }
    return DDR_SEC_CTX_MAP.get(facilityNo);
    
  }
  
  /* Pre-Visit Summary */
  public CollectionServiceResponse<PvsClinicalReminder> findClinicalRemindersByStation(String stationNo) {
    CollectionServiceResponse<PvsClinicalReminder> response = new CollectionServiceResponse<PvsClinicalReminder>();
    response.setCollection(pvsClinicalRemindersDao.findByStationNo(stationNo));
    return response;
  }
}

