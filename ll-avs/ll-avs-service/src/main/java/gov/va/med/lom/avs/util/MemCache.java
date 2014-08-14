package gov.va.med.lom.avs.util;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import gov.va.med.lom.avs.model.Encounter;
import gov.va.med.lom.avs.model.EncounterInfo;

class CacheCleaner extends Thread {

  private long delay;
  private MemCache memCache;
  private boolean stopped = false;

  CacheCleaner(MemCache memCache, long delay) {
    if (delay > 0)
      this.delay = delay;
    this.memCache = memCache;
  }

  public void run() {
    while(true) {
      try {
        if (!stopped) {
          sleep(delay);
          memCache.cleanCache();
        } else
          break;
      } catch(Exception e) {
        e.printStackTrace();
      }
    }
  }
  
  public void terminate() {
    stopped = true;
  }
}

class CacheData {

  private java.util.Date lastAccessed;
  private Object data;
  
  public CacheData(Object data) {
    lastAccessed = new java.util.Date();
    this.data = data;
  }
  
  public Object get() {
    this.lastAccessed = new java.util.Date();
    return this.data;
  }
  
  public long getLastAccessed() {
    return lastAccessed.getTime();
  }
  
  public String toString() {
    return this.data.toString();
  }
  
}

public class MemCache {

  private static MemCache memCache;
  private static final long CLEAN_INTERVAL = 1000 * 60 * 10;    // 10 minutes
  private static long INACTIVE_TIMEOUT = 1000 * 60 * 60 * 1;    // 1 hour
  private static ConcurrentHashMap<String, CacheData> cacheMap;
  private static CacheCleaner cacheCleaner = null;
  private static final Log log = LogFactory.getLog(MemCache.class);

  private MemCache() {
    cacheMap = new ConcurrentHashMap<String, CacheData>();
    cacheCleaner = new CacheCleaner(this, CLEAN_INTERVAL);
    cacheCleaner.start();
  }
  
  public static MemCache createMemCache() {
    if (memCache == null) {
      memCache = new MemCache();
    }
    return memCache;
  }   

  public void cleanCache() throws Exception {
    log.info(String.format("Cleaning MemCache for stale objects: total # objects=%d, heap=%d, free heap=%d", 
        cacheMap.size(), Runtime.getRuntime().totalMemory(), Runtime.getRuntime().freeMemory()));
    List<String> keys = new ArrayList<String>();
    Iterator<String> it = cacheMap.keySet().iterator();
    while (it.hasNext()) {
      String key = it.next();
      CacheData cacheData = cacheMap.get(key);
      if ((new java.util.Date().getTime() - cacheData.getLastAccessed()) >  INACTIVE_TIMEOUT) {
        keys.add(key);
      }
    }
    int numRemoved = 0;    
    for (String key : keys) {
      cacheMap.remove(key);
      numRemoved++;
    }
    System.gc();
    log.info(String.format("Done cleaning MemCache: # objects removed=%d, total # objects=%d, heap=%d, free heap=%d", 
        numRemoved, cacheMap.size(), Runtime.getRuntime().totalMemory(), Runtime.getRuntime().freeMemory()));
  }
  
  public synchronized Object put(String id, String name, Object data) {
    CacheData cacheData = new CacheData(data);
    cacheMap.put(createKey(id, name), cacheData);
    return cacheData.get();
  }  
  
  public synchronized Object put(String stationNo, String patientDfn, String name, Object data) {
    CacheData cacheData = new CacheData(data);
    cacheMap.put(createKey(stationNo, patientDfn, name), cacheData);
    return cacheData.get();
  }   
  
  public synchronized Object put(EncounterInfo encounterInfo, String name, Object data) {
    CacheData cacheData = new CacheData(data);
    cacheMap.put(createKey(encounterInfo, name), cacheData);
    return cacheData.get();
  }   
  
  public synchronized Object get(String id, String name) {
    CacheData cacheData = cacheMap.get(createKey(id, name));
    if (cacheData != null) {
      return cacheData.get();
    }
    return null;
  } 
  
  public synchronized Object get(String stationNo, String patientDfn, String name) {
    CacheData cacheData = cacheMap.get(createKey(stationNo, patientDfn, name));
    if (cacheData != null) {
      return cacheData.get();
    }
    return null;
  }   
  
  public synchronized Object get(EncounterInfo encounterInfo, String name) {
    CacheData cacheData = cacheMap.get(createKey(encounterInfo, name));
    if (cacheData != null) {
      return cacheData.get();
    }
    return null;
  } 
  
  public synchronized List<CacheData> getCachedObjects() {
    List<CacheData> list = new ArrayList<CacheData>();
    Iterator<String> it = cacheMap.keySet().iterator();
    while (it.hasNext()) {
      String key = it.next();
      list.add(cacheMap.get(key));
    }
    return list;
  }  

  public synchronized Object contains(String id, String name) {
    return cacheMap.containsKey(createKey(id, name));
  }
  
  public synchronized Object contains(String stationNo, String patientDfn, String name) {
    return cacheMap.containsKey(createKey(stationNo, patientDfn, name));
  }  
  
  public synchronized Object contains(EncounterInfo encounterInfo, String name) {
    return cacheMap.containsKey(createKey(encounterInfo, name));
  }   
  
  public int getCacheSize() {
    return cacheMap.size();
  }
   
  private String createKey(String id, String name) {
    StringBuffer sb = new StringBuffer(id);
    sb.append("^");
    sb.append(name);
    return sb.toString();
  }
  
  private String createKey(String stationNo, String patientDfn, String name) {
    StringBuffer sb = new StringBuffer(stationNo);
    sb.append("^");
    sb.append(patientDfn);
    sb.append("^");
    sb.append(name);
    return sb.toString();
  }  
  
  private String createKey(EncounterInfo encounterInfo, String name) {
    
    StringBuffer loc = new StringBuffer();
    StringBuffer dt = new StringBuffer();
    if (encounterInfo.getEncounterCache() != null) {
      for (Encounter encounter : encounterInfo.getEncounterCache().getEncounters()) {
        loc.append(encounter.getLocation().getLocationIen());
        loc.append(",");
        dt.append(encounter.getEncounterDatetime());
        dt.append(",");
      }
    }
    StringBuffer sb = new StringBuffer();
    sb.append(encounterInfo.getFacilityNo());
    sb.append("^");
    sb.append(encounterInfo.getUserDuz());
    sb.append("^");
    sb.append(encounterInfo.getPatientDfn());
    sb.append("^");
    sb.append(loc.toString());
    sb.append("^");
    sb.append(dt.toString());
    sb.append("^");
    sb.append(name);
    return sb.toString();
  }
  
}
