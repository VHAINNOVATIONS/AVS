package gov.va.med.lom.avs.client.action;

import java.util.List;
import java.util.Collection;
import java.util.HashMap;
import java.util.ArrayList;

import gov.va.med.lom.javaUtils.misc.StringUtils;
import gov.va.med.lom.json.util.JsonResponse;

import gov.va.med.lom.login.struts.session.SessionUtil;

import gov.va.med.lom.avs.client.model.FacilitySettingsJson;
import gov.va.med.lom.avs.client.model.LanguageJson;
import gov.va.med.lom.avs.client.model.SettingJson;
import gov.va.med.lom.avs.client.model.StringResourceJson;
import gov.va.med.lom.avs.model.FacilityHealthFactor;
import gov.va.med.lom.avs.model.FacilityPrefs;
import gov.va.med.lom.avs.model.StringResource;
import gov.va.med.lom.avs.model.Language;
import gov.va.med.lom.avs.service.ServiceFactory;
import gov.va.med.lom.avs.service.SettingsService;
import gov.va.med.lom.foundation.service.response.ServiceResponse;
import gov.va.med.lom.foundation.service.response.CollectionServiceResponse;

public class SettingsAction extends BaseCardAction {

  private String setting;
  private String value;
  private String tiuNoteText;
  private String name;
  private String divisionNo;
  private String language;
  private SettingsService settingsService;
  
  /*
   * Action Methods
   */  
  public void prepare() throws Exception {
    
    super.prepare();    
    if (!SessionUtil.isActiveSession(super.request))
      return;
    try {
      settingsService = ServiceFactory.getSettingsService();
    } catch(Exception e) {
      log.error("error creating service", e);
    }
    
  }  
  
	public String fetchSettings() {
	  ServiceResponse<FacilityPrefs> response = this.settingsService
        .getFacilityPrefs(this.getDivisionNo());
	  FacilityPrefs facilityPrefs = response.getPayload();
	  FacilitySettingsJson fsj = new FacilitySettingsJson();
	  fsj.setFacilityNo(this.getDivisionNo());
	  fsj.setTimeZone(facilityPrefs.getTimeZone());
	  fsj.setTiuNoteEnabled((facilityPrefs.getTiuTitleIen() != null) && !facilityPrefs.getTiuTitleIen().isEmpty());
	  fsj.setKramesEnabled(facilityPrefs.getKramesEnabled());
	  fsj.setServicesEnabled(facilityPrefs.getServicesEnabled());
	  fsj.setRefreshFrequency(facilityPrefs.getRefreshFrequency());
	  
    return setJson(fsj);
	}
	
	public String fetchSettingsForGrid() {
	   ServiceResponse<FacilityPrefs> response = this.settingsService
	        .getFacilityPrefs(this.getDivisionNo());
	   FacilityPrefs facilityPrefs = response.getPayload();
	   
	   List<SettingJson> list = new ArrayList<SettingJson>();
	   SettingJson setting = new SettingJson();
	   setting.setSetting("timeZone");
	   setting.setName("Time Zone");
	   setting.setValue(facilityPrefs.getTimeZone());
	   list.add(setting);
	   setting = new SettingJson();
     setting.setSetting("tiuTitleIen");
     setting.setName("TIU Title IEN");
     setting.setValue(facilityPrefs.getTiuTitleIen());
     list.add(setting);
     setting = new SettingJson();
     setting.setSetting("kramesEnabled");
     setting.setName("Krames Enabled (requires Krames On Demand license)");
     setting.setValue(StringUtils.boolToStr(facilityPrefs.getKramesEnabled(), "yes", "no"));
     list.add(setting);     
     setting = new SettingJson();
     setting.setSetting("servicesEnabled");
     setting.setName("Clinical Services Enabled");
     setting.setValue(StringUtils.boolToStr(facilityPrefs.getServicesEnabled(), "yes", "no"));
     list.add(setting);        
     setting = new SettingJson();
     setting.setSetting("refreshFrequency");
     setting.setName("Auto-Refresh Frequency (msec)");
     setting.setValue(String.valueOf(facilityPrefs.getRefreshFrequency()));
     list.add(setting);     
     setting = new SettingJson();
     setting.setSetting("upcomingAppointmentRange");
     setting.setName(" Upcoming Appointments Range (months)");
     setting.setValue(String.valueOf(facilityPrefs.getUpcomingAppointmentRange()));
     list.add(setting);    
     setting = new SettingJson();
     setting.setSetting("orderTimeFrom");
     setting.setName(" Order Retrieval Time From (minutes)");
     setting.setValue(String.valueOf(facilityPrefs.getOrderTimeFrom()));
     list.add(setting);      
     setting = new SettingJson();
     setting.setSetting("orderTimeThru");
     setting.setName(" Order Retrieval Time Thru (minutes)");
     setting.setValue(String.valueOf(facilityPrefs.getOrderTimeThru()));
     list.add(setting);    
     
     String hfStr = null;
     CollectionServiceResponse<FacilityHealthFactor> csr = 
       this.settingsService.getHealthFactorsByType(this.getDivisionNo(), "avs_printed");
     List<FacilityHealthFactor> hfs = (List<FacilityHealthFactor>)csr.getCollection();
     if (!list.isEmpty()) {
       FacilityHealthFactor hf = hfs.get(0);
       hfStr = hf.getIen() + ";" + hf.getValue();
     } else {
       hfStr = "";
     }
     setting = new SettingJson();
     setting.setSetting("avsPrintedHF");
     setting.setName(" AVS Printed Health Factor (ien;name)");
     setting.setValue(hfStr);
     list.add(setting);         
     
	   return setJson(list);
	}
	
	public String updateSettings() {
    usageLog("Update Setting", "Setting=" + this.setting + ", Value=" + this.value);
    
    ServiceFactory.getSettingsService().saveFacilitySetting(this.getDivisionNo(), this.setting, this.value);

	  return SUCCESS;
	}
	
  
  public String fetchTiuNoteText() {
    String text = this.settingsService.getTiuNoteTextForFacilty(this.getDivisionNo()).getPayload();
    JsonResponse.flushText(super.response, text);
    return SUCCESS;
  }	
	
  public String updateTiuNoteText() {
    usageLog("Update Tiu Note Text", "");
    
    ServiceFactory.getSettingsService().saveTiuNoteTextForFacility(this.getDivisionNo(), this.tiuNoteText);

    return SUCCESS;
  }	
  
  public String fetchLabelsForGrid() {
    
    CollectionServiceResponse<StringResource> response = this.settingsService
        .getStringResources(super.facilityNo, this.getLanguage());
    List<StringResource> stringResources = (List<StringResource>)response.getCollection();
    
    HashMap<String, StringResource> map = new HashMap<String, StringResource>();
    for (StringResource stringResource : stringResources) {
      if (map.containsKey(stringResource.getName())) {
        StringResource sr = map.get(stringResource.getName());
        if (sr.getStationNo() == null) {
          map.put(stringResource.getName(), stringResource);
        }
      } else {
        map.put(stringResource.getName(), stringResource);
      }
    }
    
    List<StringResourceJson> srjList = new ArrayList<StringResourceJson>();
    Collection<StringResource> srList = map.values();
    for (StringResource stringResource : srList) {
      StringResourceJson stringResourceJson = new StringResourceJson();
      stringResourceJson.setLanguageAbbr(stringResource.getLanguage().getAbbreviation());
      stringResourceJson.setLanguageName(stringResource.getLanguage().getName());
      stringResourceJson.setName(stringResource.getName());
      stringResourceJson.setValue(stringResource.getValue());
      stringResourceJson.setStationNo(stringResource.getStationNo());
      srjList.add(stringResourceJson);
    }
    
    return setJson(srjList);
  }
  
  public String updateLabel() {
    
    usageLog("Update Label", "Label=" + this.name + ", Value=" + this.value + ", Language=" + this.getLanguage());
    
    StringResource stringResource = 
        ServiceFactory.getSettingsService().getStringResource(super.facilityNo, this.name, this.getLanguage()).getPayload();
    
    Language lang = ServiceFactory.getSettingsService().getLanguageByAbbreviation(this.getLanguage()).getPayload();
    if ((stringResource == null) || (stringResource.getStationNo() == null)) {
      stringResource = new StringResource();
      stringResource.setStationNo(super.facilityNo);
      stringResource.setName(this.name);
      stringResource.setValue(this.value);
      stringResource.setLanguage(lang);
      ServiceFactory.getSettingsService().saveStringResource(stringResource);
    } else {
      stringResource.setValue(this.value);
      stringResource.setLanguage(lang);
      ServiceFactory.getSettingsService().updateStringResource(stringResource);
    }

    return SUCCESS;
  }  
  
  public String languages() {
    
    List<Language> languages = (List<Language>)
        ServiceFactory.getSettingsService().getLanguages().getCollection();
    List<LanguageJson> list = new ArrayList<LanguageJson>();
    for (Language language : languages) {
      LanguageJson lj = new LanguageJson();
      lj.setAbbreviation(language.getAbbreviation());
      lj.setName(language.getName());
      list.add(lj);
    }
    return setJson(list);
  }

  public String getSetting() {
    return setting;
  }

  public void setSetting(String setting) {
    this.setting = setting;
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  public String getTiuNoteText() {
    return tiuNoteText;
  }

  public void setTiuNoteText(String tiuNoteText) {
    this.tiuNoteText = tiuNoteText;
  }

  public String getDivisionNo() {
    return this.divisionNo != null && !this.divisionNo.isEmpty() ? this.divisionNo : super.facilityNo;
  }

  public void setDivisionNo(String divisionNo) {
    this.divisionNo = divisionNo;
  }

  public String getLanguage() {
    return this.language != null && !this.language.isEmpty() ? this.language : "en";
  }

  public void setLanguage(String language) {
    this.language = language;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
  
}
