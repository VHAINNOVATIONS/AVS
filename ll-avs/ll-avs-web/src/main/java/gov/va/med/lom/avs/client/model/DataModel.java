package gov.va.med.lom.avs.client.model;

import java.util.LinkedHashMap;
import java.util.List;

public class DataModel {

	private Header header;
	private PatientInfoJson patientInfo;
	private List<ClinicVisitedJson> clinicsVisited;
	private List<String> providers;
	private List<DiagnosisJson> reasonForVisit;
  private List<DiagnosisJson> diagnoses;
  private List<VitalSignJson> vitals;
  private List<OrderJson> orders;
  private List<ProcedureJson> procedures;
  private List<String> immunizations;
  private List<AppointmentJson> appointments;
  private String patientInstructions;
  private String primaryCareProvider;
  private String primaryCareTeam;
  private List<PrimaryCareTeamMemberJson> primaryCareTeamMembers;
	private AllergiesReactionsJson allergiesReactions;
  private List<MedicationJson> vaMedications;	
  private List<MedicationJson> nonvaMedications; 
	private String labResults;
	private LinkedHashMap<String, List<DiscreteItemJson>> discreteData;
	
  public Header getHeader() {
    return header;
  }
  public void setHeader(Header header) {
    this.header = header;
  }
  public AllergiesReactionsJson getAllergiesReactions() {
    return allergiesReactions;
  }
  public void setAllergiesReactions(AllergiesReactionsJson allergiesReactions) {
    this.allergiesReactions = allergiesReactions;
  }
  public List<AppointmentJson> getAppointments() {
    return appointments;
  }
  public void setAppointments(List<AppointmentJson> appointments) {
    this.appointments = appointments;
  }
  public List<ClinicVisitedJson> getClinicsVisited() {
    return clinicsVisited;
  }
  public void setClinicsVisited(List<ClinicVisitedJson> clinicsVisited) {
    this.clinicsVisited = clinicsVisited;
  }
  public String getPatientInstructions() {
    return patientInstructions;
  }
  public void setPatientInstructions(String patientInstructions) {
    this.patientInstructions = patientInstructions;
  }
  public String getLabResults() {
    return labResults;
  }
  public void setLabResults(String labResults) {
    this.labResults = labResults;
  }
  public List<OrderJson> getOrders() {
    return orders;
  }
  public void setOrders(List<OrderJson> orders) {
    this.orders = orders;
  }
  public List<VitalSignJson> getVitals() {
    return vitals;
  }
  public void setVitals(List<VitalSignJson> vitals) {
    this.vitals = vitals;
  }
  public List<DiagnosisJson> getReasonForVisit() {
    return reasonForVisit;
  }
  public void setReasonForVisit(List<DiagnosisJson> reasonForVisit) {
    this.reasonForVisit = reasonForVisit;
  }
  public List<String> getProviders() {
    return providers;
  }
  public void setProviders(List<String> providers) {
    this.providers = providers;
  }
  public List<DiagnosisJson> getDiagnoses() {
    return diagnoses;
  }
  public void setDiagnoses(List<DiagnosisJson> diagnoses) {
    this.diagnoses = diagnoses;
  }
  public List<String> getImmunizations() {
    return immunizations;
  }
  public void setImmunizations(List<String> immunizations) {
    this.immunizations = immunizations;
  }
  public String getPrimaryCareProvider() {
    return primaryCareProvider;
  }
  public void setPrimaryCareProvider(String primaryCareProvider) {
    this.primaryCareProvider = primaryCareProvider;
  }
  public String getPrimaryCareTeam() {
    return primaryCareTeam;
  }
  public void setPrimaryCareTeam(String primaryCareTeam) {
    this.primaryCareTeam = primaryCareTeam;
  }
  public List<PrimaryCareTeamMemberJson> getPrimaryCareTeamMembers() {
    return primaryCareTeamMembers;
  }
  public void setPrimaryCareTeamMembers(List<PrimaryCareTeamMemberJson> primaryCareTeamMembers) {
    this.primaryCareTeamMembers = primaryCareTeamMembers;
  }
  public PatientInfoJson getPatientInfo() {
    return patientInfo;
  }
  public void setPatientInfo(PatientInfoJson patientInfo) {
    this.patientInfo = patientInfo;
  }
  public LinkedHashMap<String, List<DiscreteItemJson>> getDiscreteData() {
    return discreteData;
  }
  public void setDiscreteData(
      LinkedHashMap<String, List<DiscreteItemJson>> discreteData) {
    this.discreteData = discreteData;
  }
  public List<MedicationJson> getVaMedications() {
    return vaMedications;
  }
  public void setVaMedications(List<MedicationJson> vaMedications) {
    this.vaMedications = vaMedications;
  }
  public List<MedicationJson> getNonvaMedications() {
    return nonvaMedications;
  }
  public void setNonvaMedications(List<MedicationJson> nonvaMedications) {
    this.nonvaMedications = nonvaMedications;
  }
  public List<ProcedureJson> getProcedures() {
    return procedures;
  }
  public void setProcedures(List<ProcedureJson> procedures) {
    this.procedures = procedures;
  }
  
}
