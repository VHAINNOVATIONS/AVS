package gov.va.med.lom.avs.model;

import java.util.List;
import java.util.ArrayList;

public class PceData {

  private double datetime;
  private String visitString;
  private List<String> providers;
  private List<String> providerTitles;
  private List<String> providerDuzs;
  private List<String> diagnoses;
  private List<String> immunizations;
  private List<String> codes;
  private String clinicName;
  private String clinicIen;
  private String institutionName;
  private String institutionIen;
  private String reasonForVisit;
  private String reasonForVisitCode;
  
  public PceData() {
    providers = new ArrayList<String>();
    providerTitles = new ArrayList<String>();
    providerDuzs = new ArrayList<String>();
    diagnoses = new ArrayList<String>();
    immunizations = new ArrayList<String>();
    codes = new ArrayList<String>();
  }
  
  public List<String> getProviders() {
    return providers;
  }
  public void setProviders(List<String> providers) {
    this.providers = providers;
  }
  public void addProvider(String provider) {
    this.providers.add(provider);
  }
  public List<String> getDiagnoses() {
    return diagnoses;
  }
  public void setDiagnoses(List<String> diagnoses) {
    this.diagnoses = diagnoses;
  }
  public void addDiagnosis(String diagnosis) {
    this.diagnoses.add(diagnosis);
  }
  public List<String> getImmunizations() {
    return immunizations;
  }
  public void setImmunizations(List<String> immunizations) {
    this.immunizations = immunizations;
  }
  public void addImmunization(String immunization) {
    this.immunizations.add(immunization);
  }
  public List<String> getCodes() {
    return codes;
  }
  public void addCode(String code) {
    codes.add(code);
  }
  public void setCodes(List<String> codes) {
    this.codes = codes;
  }
  public String getInstitutionIen() {
    return institutionIen;
  }
  public void setInstitutionIen(String institutionIen) {
    this.institutionIen = institutionIen;
  }
  public String getClinicIen() {
    return clinicIen;
  }
  public void setClinicIen(String clinicIen) {
    this.clinicIen = clinicIen;
  }
  public String getClinicName() {
    return clinicName;
  }
  public void setClinicName(String clinicName) {
    this.clinicName = clinicName;
  }
  public String getInstitutionName() {
    return institutionName;
  }
  public void setInstitutionName(String institutionName) {
    this.institutionName = institutionName;
  }
  public List<String> getProviderDuzs() {
    return providerDuzs;
  }
  public void setProviderDuzs(List<String> providerDuzs) {
    this.providerDuzs = providerDuzs;
  }
  public void addProviderDuz(String providerDuz) {
    this.providerDuzs.add(providerDuz);
  }
  public String getReasonForVisit() {
    return reasonForVisit;
  }
  public void setReasonForVisit(String reasonForVisit) {
    this.reasonForVisit = reasonForVisit;
  }

  public String getReasonForVisitCode() {
    return reasonForVisitCode;
  }

  public void setReasonForVisitCode(String reasonForVisitCode) {
    this.reasonForVisitCode = reasonForVisitCode;
  }

  public List<String> getProviderTitles() {
    return providerTitles;
  }

  public void setProviderTitles(List<String> providerTitles) {
    this.providerTitles = providerTitles;
  }
  public void addProviderTitle(String providerTitle) {
    this.providerTitles.add(providerTitle);
  }

  public double getDatetime() {
    return datetime;
  }

  public void setDatetime(double datetime) {
    this.datetime = datetime;
  }

  public String getVisitString() {
    return visitString;
  }

  public void setVisitString(String visitString) {
    this.visitString = visitString;
  }
  
}
