package gov.va.med.lom.avs.web.util;

import gov.va.med.lom.vistabroker.patient.data.DiscreteItemData;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

public class LabCache {

  private String stationNo;
  private String userDuz;
  private String patientDfn;
  LinkedHashMap<String, List<DiscreteItemData>> data;
  private Date lastAccessed;
  
  public LabCache() {}
  
  public LabCache(LinkedHashMap<String, List<DiscreteItemData>> data) {
    this.data = data;
  }
  
  public String getUserDuz() {
    return userDuz;
  }
  public void setUserDuz(String userDuz) {
    this.userDuz = userDuz;
  }
  public Date getLastAccessed() {
    return lastAccessed;
  }
  public void setLastAccessed(Date lastAccessed) {
    this.lastAccessed = lastAccessed;
  }
  public String getStationNo() {
    return stationNo;
  }
  public void setStationNo(String stationNo) {
    this.stationNo = stationNo;
  }

  public LinkedHashMap<String, List<DiscreteItemData>> getData() {
    return data;
  }

  public void setData(LinkedHashMap<String, List<DiscreteItemData>> data) {
    this.data = data;
  }

  public String getPatientDfn() {
    return patientDfn;
  }

  public void setPatientDfn(String patientDfn) {
    this.patientDfn = patientDfn;
  }
  
  public boolean keyExists(String key) {
    return data.containsKey(key);
  }
  
  public List<DiscreteItemData> getCachedData(String key) {
    return data.get(key);
  }
  
  public List<DiscreteItemData> removeCachedData(String key) {
    return data.remove(key);
  }
  
}
