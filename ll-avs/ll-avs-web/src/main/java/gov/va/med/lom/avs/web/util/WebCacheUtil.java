package gov.va.med.lom.avs.web.util;

import gov.va.med.lom.javaUtils.misc.StringUtils;

import java.util.Date;
import java.util.HashMap;

public class WebCacheUtil {

  private static WebCacheUtil webCacheUtil;
  private static HashMap<String, LabCache> labCacheMap;
  private static HashMap<String, String> commentsCacheMap;
  
  private WebCacheUtil() {
    labCacheMap = new HashMap<String, LabCache>();
    commentsCacheMap = new HashMap<String, String>();
  }
  
  public static WebCacheUtil getWebCacheUtil() {
    if (webCacheUtil == null) {
      webCacheUtil = new WebCacheUtil();
    }
    return webCacheUtil;
  }

  public void cacheLabData(String stationNo, String userDuz, String patientDfn, LabCache labCacheData) {
    if ((stationNo != null) && (userDuz !=  null)) {
      labCacheData.setStationNo(stationNo);
      labCacheData.setUserDuz(userDuz);
      labCacheData.setLastAccessed(new Date());
      labCacheMap.put(stationNo + "_" + userDuz + "_" + patientDfn, labCacheData);
    }
  }
  
  public LabCache getLabCache(String stationNo, String userDuz, String patientDfn) {
    if ((stationNo != null) && (userDuz !=  null)) {
      LabCache labCacheData = labCacheMap.get(stationNo + "_" + userDuz + "_" + patientDfn);
      if (labCacheData != null) {
        labCacheData.setLastAccessed(new Date());
      }
      return labCacheData;
    } else {
      return null;
    }
  }
  
  public void removeLabCache(String stationNo, String userDuz, String patientDfn) {
    labCacheMap.remove(stationNo + "_" + userDuz + "_" + patientDfn);
  }
  
  public boolean labCacheContainsValues(String stationNo, String userDuz, String patientDfn) {
    LabCache cache = getLabCache(stationNo, userDuz, patientDfn);
    return cache.getData().size() > 0;
  }
  
  public String cacheComments(String comments) {
    String key = StringUtils.getRandomString(10, 10);
    commentsCacheMap.put(key, comments);
    return key;
  }
  
  public String getCommentsCache(String key) {
    return commentsCacheMap.get(key);
  }
  
}
