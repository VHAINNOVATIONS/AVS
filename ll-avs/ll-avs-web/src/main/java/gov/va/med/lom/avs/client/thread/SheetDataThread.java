package gov.va.med.lom.avs.client.thread;

import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import gov.va.med.lom.foundation.service.response.ServiceResponse;

import gov.va.med.lom.vistabroker.security.ISecurityContext;
import gov.va.med.lom.vistabroker.service.PatientVBService;
import gov.va.med.lom.vistabroker.service.UserVBService;
import gov.va.med.lom.vistabroker.service.MiscVBService;
import gov.va.med.lom.vistabroker.service.DdrVBService;
import gov.va.med.lom.vistabroker.user.data.VistaUser;
import gov.va.med.lom.vistabroker.util.FMDateUtils;
import gov.va.med.lom.vistabroker.util.VistaBrokerServiceFactory;

import gov.va.med.lom.avs.model.BasicDemographics;
import gov.va.med.lom.avs.model.Encounter;
import gov.va.med.lom.avs.model.EncounterInfo;
import gov.va.med.lom.avs.model.FacilityPrefs;
import gov.va.med.lom.avs.model.EncounterCacheMongo;
import gov.va.med.lom.avs.model.PceData;
import gov.va.med.lom.avs.service.ServiceFactory;
import gov.va.med.lom.avs.service.SettingsService;
import gov.va.med.lom.avs.service.SheetService;
import gov.va.med.lom.avs.service.LabValuesService;
import gov.va.med.lom.avs.util.StringResources;
import gov.va.med.lom.avs.web.util.AvsWebUtils;

public abstract class SheetDataThread extends Thread {

  static final Log log = LogFactory.getLog(SheetDataThread.class);
  
  static final int MEDIA_HTML = 1;
  static final int MEDIA_PDF = 2;
  public static final int CONTENT_HTML = 1;
  public static final int CONTENT_JSON = 1;
  static final int DEF_EXPIRED_MED_DAYS = 30; // days
  
  static final Pattern SIG_PATTERN = Pattern.compile("^ *Sig: *");
  
  static final String TPL_SECTION = "" +
      "<div id=\"section-__SECTION_ID_SUFFIX__\" class=\"__SECTION_CLASS__\">" +
        "<h3 class=\"section-title\">__SECTION_TITLE__</h3>" +
        "<div class=\"section-body\">__CONTENTS__</div>" + 
      "</div>";
  
  protected ISecurityContext securityContext;
  protected String facilityNo;
  protected String stationNo;
  protected String facilityName;
  protected String userDuz;
  protected String patientDfn;
  protected String patientSsn;
  protected String fontClass;
  protected BasicDemographics demographics;
  protected EncounterCacheMongo encounterCache;
  protected List<Encounter> encounters;
  protected List<PceData> pceDataList;
  protected List<String> providerDuzs;
  protected int media;
  protected String docType;
  protected int contentType;
  protected boolean initialRequest;
  protected String language;
  protected FacilityPrefs facilityPrefs;
  protected EncounterInfo encounterInfo;
  
  // data & utils
  private VistaUser vistaUser;
  private ICallbackObject callback;
  private StringResources stringResources;
  
  // services
  private SheetService sheetService;
  private SettingsService settingsService;
  private LabValuesService labValuesService;
  private PatientVBService patientVBService;
  private UserVBService userVBService;
  private MiscVBService miscVBService;
  private DdrVBService ddrVBService;
  
  /*
   * Constructor
   */
  public SheetDataThread() {
    stringResources = StringResources.getStringResources();
  }
  
  /*
   * Run
   */
  public abstract void run();
  
  /*
   * String Resources
   */
  
  public String getStringResource(String stationNo, String name, String language) {
    return stringResources.getStringResource(stationNo, name, language);
  }
  
  public String getStringResource(String name) {
    return getStringResource(this.facilityNo, name, this.language);
  }
  
  /*
   * Services
   */
  public SheetService getSheetService() {
    if (this.sheetService == null) {
      this.sheetService = ServiceFactory.getSheetService();
    }
    return this.sheetService;
  }
  
  public SettingsService getSettingsService() {
    if (this.settingsService == null) {
      this.settingsService = ServiceFactory.getSettingsService();
    }
    return this.settingsService;
  }
  
  public LabValuesService getLabValuesService() {
    if (this.labValuesService == null) {
      this.labValuesService = ServiceFactory.getLabValuesService();
    }
    return this.labValuesService;
  }  
  
  public PatientVBService getPatientVBService() {
    if (this.patientVBService == null) {
      this.patientVBService = VistaBrokerServiceFactory.getPatientVBService();
    }
    return this.patientVBService;
  }  
  
  public UserVBService getUserVBService() {
    if (this.userVBService == null) {
      this.userVBService = VistaBrokerServiceFactory.getUserVBService();
    }
    return this.userVBService;
  }  

  
  public MiscVBService getMiscVBService() {
    if (this.miscVBService == null) {
      this.miscVBService = VistaBrokerServiceFactory.getMiscVBService();
    }
    return this.miscVBService;
  }  
  
  public DdrVBService getDdrVBService() {
    if (this.ddrVBService == null) {
      this.ddrVBService = VistaBrokerServiceFactory.getDdrVBService();
    }
    return this.ddrVBService;
  }    
  /*
   * Data Methods
   */
 
  protected VistaUser getVistaUser() {
    if (this.vistaUser == null) {
      this.userVBService = VistaBrokerServiceFactory.getUserVBService();
      ServiceResponse<VistaUser> response = userVBService.getVistaUser(this.securityContext);
      AvsWebUtils.handleServiceErrors(response, log);
      this.vistaUser = response.getPayload();
    }
    
    return this.vistaUser;
  }
  
  public EncounterInfo getEncounterInfo() {
    if (this.encounterInfo == null) {
      this.encounterInfo = new EncounterInfo();
      this.encounterInfo.setFacilityNo(this.facilityNo);
      this.encounterInfo.setFacilityName(this.facilityName);
      this.encounterInfo.setUserDuz(this.userDuz);
      this.encounterInfo.setPatientDfn(this.patientDfn);
      this.encounterInfo.setPatientSsn(this.patientSsn);
      this.encounterInfo.setPatientLanguage(this.language);
      this.encounterInfo.setFacilityPrefs(this.facilityPrefs);
      this.encounterInfo.setDocType(this.docType);      
      this.encounterInfo.setEncounterCache(this.encounterCache);
    }
    return this.encounterInfo;
  }
  
  protected Encounter getEarliestEncounter() {
    Encounter enc = null;
    double fmdatetime =FMDateUtils.dateTimeToFMDateTime(DateUtils.addDays(new Date(), 365));
    List<Encounter> encs = this.getEncounterInfo().getEncounterCache().getEncounters();
    if ((encs != null) && (encs.size() > 0)) {
      for (Encounter encounter : encs) {
        if (encounter.getEncounterDatetime() < fmdatetime) {
          fmdatetime = encounter.getEncounterDatetime();
        }
      }
    }
    if (enc == null) {
      enc = new Encounter();
      enc.setEncounterDatetime(this.getMiscVBService().fmNow(securityContext).getPayload());
    }
    return enc;
  }
  
  protected Encounter getLatestEncounter() {
    Encounter enc = null;
    double fmdatetime =FMDateUtils.dateTimeToFMDateTime(DateUtils.addDays(new Date(), -365));
    List<Encounter> encs = this.getEncounterInfo().getEncounterCache().getEncounters();
    if ((encs != null) && (encs.size() > 0)) {
      for (Encounter encounter : encs) {
        if (encounter.getEncounterDatetime() > fmdatetime) {
          fmdatetime = encounter.getEncounterDatetime();
        }
      }
    }
    if (enc == null) {
      enc = new Encounter();
      enc.setEncounterDatetime(this.getMiscVBService().fmNow(securityContext).getPayload());
    }
    return enc;
  }  
    
  /*
   * Callback Methods
   */
  protected void setContent(String name, String content) {
    this.callback.contentCallback(name, content);
  }
  
  protected void setContentData(String name, String content, Object data) {
    this.callback.contentDataCallback(name, content, data);
  }  
  
  protected void setData(String name, Object data) {
    this.callback.dataCallback(name, data);    
  }
  
  /*
   * Property Accessors
   */
  public void setStationNo(String stationNo) {
    this.stationNo = stationNo;
  }

  public String getPatientDfn() {
    return patientDfn;
  }

  public void setPatientDfn(String patientDfn) {
    this.patientDfn = patientDfn;
  }

  public ISecurityContext getSecurityContext() {
    return securityContext;
  }

  public void setSecurityContext(ISecurityContext securityContext) {
    this.securityContext = securityContext;
  }

  public String getFontClass() {
    return fontClass;
  }

  public void setFontClass(String fontClass) {
    this.fontClass = fontClass;
  }

  public List<String> getProviderDuzs() {
    return this.providerDuzs;
  }
  
  public void setProviderDuzs(List<String> providerDuzs) {
    this.providerDuzs = providerDuzs;
  }

  public int getMedia() {
    return media;
  }

  public void setMedia(int media) {
    this.media = media;
  }

  public boolean isInitialRequest() {
    return initialRequest;
  }

  public void setInitialRequest(boolean initialRequest) {
    this.initialRequest = initialRequest;
  }

  public String getFacilityNo() {
    return facilityNo;
  }

  public void setFacilityNo(String facilityNo) {
    this.facilityNo = facilityNo;
  }

  public String getUserDuz() {
    return userDuz;
  }

  public void setUserDuz(String userDuz) {
    this.userDuz = userDuz;
  }

  public ICallbackObject getCallback() {
    return callback;
  }

  public void setCallback(ICallbackObject callback) {
    this.callback = callback;
  }

  public BasicDemographics getDemographics() {
    return demographics;
  }

  public void setDemographics(BasicDemographics demographics) {
    this.demographics = demographics;
  }

  public List<Encounter> getEncounters() {
    return encounters;
  }

  public void setEncounters(List<Encounter> encounters) {
    this.encounters = encounters;
  }

  public String getLanguage() {
    return language;
  }

  public void setLanguage(String language) {
    this.language = language;
  }

  public int getContentType() {
    return contentType;
  }

  public void setContentType(int contentType) {
    this.contentType = contentType;
  }

  public String getPatientSsn() {
    return patientSsn;
  }

  public void setPatientSsn(String patientSsn) {
    this.patientSsn = patientSsn;
  }

  public FacilityPrefs getFacilityPrefs() {
    return facilityPrefs;
  }

  public void setFacilityPrefs(FacilityPrefs facilityPrefs) {
    this.facilityPrefs = facilityPrefs;
  }

  public String getDocType() {
    return docType;
  }

  public void setDocType(String docType) {
    this.docType = docType;
  }

  public String getFacilityName() {
    return facilityName;
  }

  public void setFacilityName(String facilityName) {
    this.facilityName = facilityName;
  }

  public EncounterCacheMongo getEncounterCache() {
    return encounterCache;
  }

  public void setEncounterCache(EncounterCacheMongo encounterCache) {
    this.encounterCache = encounterCache;
  }

  public List<PceData> getPceDataList() {
    return pceDataList;
  }

  public void setPceDataList(List<PceData> pceDataList) {
    this.pceDataList = pceDataList;
  }

}