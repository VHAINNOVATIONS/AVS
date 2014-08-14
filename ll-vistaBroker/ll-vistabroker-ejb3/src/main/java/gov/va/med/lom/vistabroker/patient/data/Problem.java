package gov.va.med.lom.vistabroker.patient.data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class Problem implements Serializable {

  private String dfn;
  private String ien;
  private String status;
  private String description;
  private String code;
  private String onsetDateStr;
  private Date onsetDate;
  private String lastUpdatedStr;
  private Date lastUpdated;
  private String scStatus;
  private String scConditions;
  private String detailRpc;
  private boolean transcribed;
  private long locationIen;
  private String location;
  private String locationType;
  private String providerIen;
  private String provider;
  private String serviceIen;
  private String service;
  private String version;
  private List<String> comments;
  
  public Problem() {
    this.dfn = null;
    this.ien = null;
    this.status = null;
    this.description = null;
    this.code = null;
    this.onsetDate = null;
    this.onsetDateStr = null;
    this.lastUpdatedStr = null;
    this.lastUpdated = null;
    this.scStatus = null;
    this.scConditions = null;
    this.detailRpc = null;
    this.transcribed = false;
    this.locationIen = 0;
    this.location = null;
    this.locationType = null;
    this.providerIen = null;
    this.provider = null;
    this.serviceIen = null;
    this.service = null;
    this.version = null;
    this.comments = null;   
  }
  
  public String getDfn() {
    return dfn;
  }

  public void setDfn(String dfn) {
    this.dfn = dfn;
  }

  public String getIen() {
    return ien;
  }

  public void setIen(String ien) {
    this.ien = ien;
  }

  public Date getLastUpdated() {
    return lastUpdated;
  }

  public void setLastUpdated(Date lastUpdated) {
    this.lastUpdated = lastUpdated;
  }
  
  public Date getOnsetDate() {
    return onsetDate;
  }
  
  public void setOnsetDate(Date onsetDate) {
    this.onsetDate = onsetDate;
  }
  
  public String getCode() {
    return code;
  }
  
  public void setCode(String code) {
    this.code = code;
  }
  
  public List<String> getComments() {
    return comments;
  }
  
  public void setComments(List<String> comments) {
    this.comments = comments;
  }
  
  public String getDescription() {
    return description;
  }
  
  public void setDescription(String description) {
    this.description = description;
  }
  
  public String getDetailRpc() {
    return detailRpc;
  }
  
  public void setDetailRpc(String detailRPC) {
    this.detailRpc = detailRPC;
  }
  
  public String getLastUpdatedStr() {
    return lastUpdatedStr;
  }
  
  public void setLastUpdatedStr(String lastUpdated) {
    this.lastUpdatedStr = lastUpdated;
  }
  
  public String getLocation() {
    return location;
  }
  
  public void setLocation(String location) {
    this.location = location;
  }
  
  public long getLocationIen() {
    return locationIen;
  }
  
  public void setLocationIen(long locationIen) {
    this.locationIen = locationIen;
  }
  
  public String getLocationType() {
    return locationType;
  }
  
  public void setLocationType(String locationType) {
    this.locationType = locationType;
  }
  
  public String getOnsetDateStr() {
    return onsetDateStr;
  }
  
  public void setOnsetDateStr(String onsetDate) {
    this.onsetDateStr = onsetDate;
  }
  
  public String getProvider() {
    return provider;
  }
  
  public void setProvider(String provider) {
    this.provider = provider;
  }
  
  public String getProviderIen() {
    return providerIen;
  }
  
  public void setProviderIen(String providerIen) {
    this.providerIen = providerIen;
  }
  
  public String getScConditions() {
    return scConditions;
  }
  
  public void setScConditions(String scConditions) {
    this.scConditions = scConditions;
  }
  
  public String getScStatus() {
    return scStatus;
  }
  
  public void setScStatus(String scStatus) {
    this.scStatus = scStatus;
  }
  
  public String getService() {
    return service;
  }
  
  public void setService(String service) {
    this.service = service;
  }
  
  public String getServiceIen() {
    return serviceIen;
  }
  
  public void setServiceIen(String serviceIen) {
    this.serviceIen = serviceIen;
  }
  
  public String getStatus() {
    return status;
  }
  
  public void setStatus(String status) {
    this.status = status;
  }
  
  public boolean getTranscribed() {
    return transcribed;
  }
  
  public void setTranscribed(boolean transcribed) {
    this.transcribed = transcribed;
  }
  
  public String getVersion() {
    return version;
  }
  
  public void setVersion(String version) {
    this.version = version;
  }
  
}
