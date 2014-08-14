package gov.va.med.lom.avs.model;

import gov.va.med.lom.avs.model.EncounterCacheMongo;

public class EncounterInfo {

  public static final String AVS = "avs";
  public static final String PVS = "pvs"; 
  
  private String facilityNo;
  private String facilityName;
  private String userDuz;
  private String patientDfn;
  private String patientSsn;
  private EncounterCacheMongo encounterCache;
  private String patientLanguage;
  private FacilityPrefs facilityPrefs;
  private String docType;
  
  public String getFacilityNo() {
    return facilityNo;
  }
  public void setFacilityNo(String facilityNo) {
    this.facilityNo = facilityNo;
  }
  public String getPatientDfn() {
    return patientDfn;
  }
  public void setPatientDfn(String patientDfn) {
    this.patientDfn = patientDfn;
  }
  public String getPatientSsn() {
    return patientSsn;
  }
  public void setPatientSsn(String patientSsn) {
    this.patientSsn = patientSsn;
  }
  public EncounterCacheMongo getEncounterCache() {
    return encounterCache;
  }
  public void setEncounterCache(EncounterCacheMongo encounterCache) {
    this.encounterCache = encounterCache;
  }
  public String getPatientLanguage() {
    return patientLanguage;
  }
  public void setPatientLanguage(String patientLanguage) {
    this.patientLanguage = patientLanguage;
  }
  public String getFacilityName() {
    return facilityName;
  }
  public void setFacilityName(String facilityName) {
    this.facilityName = facilityName;
  }
  public FacilityPrefs getFacilityPrefs() {
    return facilityPrefs;
  }
  public void setFacilityPrefs(FacilityPrefs facilityPrefs) {
    this.facilityPrefs = facilityPrefs;
  }
  public String getUserDuz() {
    return userDuz;
  }
  public void setUserDuz(String userDuz) {
    this.userDuz = userDuz;
  }
  public String getDocType() {
    return docType;
  }
  public void setDocType(String docType) {
    this.docType = docType;
  }
  
}
