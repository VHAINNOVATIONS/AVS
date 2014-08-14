package gov.va.med.lom.avs.util;

import gov.va.med.lom.foundation.service.response.CollectionServiceResponse;

import gov.va.med.lom.avs.model.StringResource;
import gov.va.med.lom.avs.service.ServiceFactory;
import gov.va.med.lom.avs.service.SettingsService;

import java.util.Date;
import java.util.List;
import java.util.HashMap;

public class StringResources {

  static Date lastStringResourcesRefresh = new Date();
  static final long REFRESH_INTERVAL = 60 * 1000 * 10;
  static final HashMap<String, StringResource> STRING_RESOURCES_MAP = new HashMap<String, StringResource>();
  private static StringResources STRING_RESOURCES;
  
  private SettingsService settingsService;
  
  private StringResources() {
    initStringResources();
  }
  
  public static StringResources getStringResources() {
    if (STRING_RESOURCES == null) {
      STRING_RESOURCES = new StringResources();
      
    }
    return STRING_RESOURCES;
  }
  
  private void initStringResources() {
    
    if ((STRING_RESOURCES_MAP.size() == 0) || (new Date().getTime() - lastStringResourcesRefresh.getTime() > REFRESH_INTERVAL)) {
      CollectionServiceResponse<StringResource> response = this.getSettingsService().getAllStringResources();
      List<StringResource> stringResources = (List<StringResource>)response.getCollection();
      
      STRING_RESOURCES_MAP.clear();
      for (StringResource stringResource : stringResources) {
        StringBuffer key = new StringBuffer(stringResource.getName());
        key.append("_");
        key.append(stringResource.getLanguage().getAbbreviation());
        if (stringResource.getStationNo() != null) {
          key.append("_");
          key.append(stringResource.getStationNo());
        }
        STRING_RESOURCES_MAP.put(key.toString(), stringResource);
      }
    }
  }
  
  private SettingsService getSettingsService() {
    if (this.settingsService == null) {
      this.settingsService = ServiceFactory.getSettingsService();
    }
    return this.settingsService;
  }
  
  public String getStringResource(String stationNo, String name, String language) {
    
    StringBuffer key = new StringBuffer(name);
    key.append("_");
    key.append(language);
    StringResource sr1 = STRING_RESOURCES_MAP.get(key.toString());
    key.append("_");
    key.append(stationNo);
    StringResource sr2 = STRING_RESOURCES_MAP.get(key.toString());
    if (sr2 != null) {
      return sr2.getValue();
    } else if (sr1 != null) {
      return sr1.getValue();
    } else {
      return "";
    }
  }
  
}
