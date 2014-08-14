package gov.va.med.lom.javaBroker.rpc.patient.models;

public class UnsignedNoteHeaders {

  private String summary;
  private TiuNoteHeader[] unsignedProgressNotes;
  private TiuNoteHeader[] uncosignedProgressNotes;
  private TiuNoteHeader[] unsignedDischargeSummaries;
  private TiuNoteHeader[] uncosignedDischargeSummaries;
  private TiuNoteHeader[] unsignedClinicalProcedures;
  private TiuNoteHeader[] uncosignedClinicalProcedures;
  private TiuNoteHeader[] unsignedSurgicalReports;
  private TiuNoteHeader[] uncosignedSurgicalReports;
  private TiuNoteHeader[] unsignedByExpectedAdditonalSignerNotes;
  
  public String getSummary() {
    return summary;
  }
  
  public void setSummary(String summary) {
    this.summary = summary;
  }
  
  public TiuNoteHeader[] getUncosignedClinicalProcedures() {
    return uncosignedClinicalProcedures;
  }
  
  public void setUncosignedClinicalProcedures(
      TiuNoteHeader[] uncosignedClinicalProcedures) {
    this.uncosignedClinicalProcedures = uncosignedClinicalProcedures;
  }
  
  public TiuNoteHeader[] getUncosignedDischargeSummaries() {
    return uncosignedDischargeSummaries;
  }
  
  public void setUncosignedDischargeSummaries(
      TiuNoteHeader[] uncosignedDischargeSummaries) {
    this.uncosignedDischargeSummaries = uncosignedDischargeSummaries;
  }
  
  public TiuNoteHeader[] getUncosignedProgressNotes() {
    return uncosignedProgressNotes;
  }
  
  public void setUncosignedProgressNotes(TiuNoteHeader[] uncosignedProgressNotes) {
    this.uncosignedProgressNotes = uncosignedProgressNotes;
  }
  
  public TiuNoteHeader[] getUncosignedSurgicalReports() {
    return uncosignedSurgicalReports;
  }
  
  public void setUncosignedSurgicalReports(
      TiuNoteHeader[] uncosignedSurgicalReports) {
    this.uncosignedSurgicalReports = uncosignedSurgicalReports;
  }
  
  public TiuNoteHeader[] getUnsignedClinicalProcedures() {
    return unsignedClinicalProcedures;
  }
  
  public void setUnsignedClinicalProcedures(
      TiuNoteHeader[] unsignedClinicalProcedures) {
    this.unsignedClinicalProcedures = unsignedClinicalProcedures;
  }
  
  public TiuNoteHeader[] getUnsignedDischargeSummaries() {
    return unsignedDischargeSummaries;
  }
  
  public void setUnsignedDischargeSummaries(
      TiuNoteHeader[] unsignedDischargeSummaries) {
    this.unsignedDischargeSummaries = unsignedDischargeSummaries;
  }
  
  public TiuNoteHeader[] getUnsignedProgressNotes() {
    return unsignedProgressNotes;
  }
  
  public void setUnsignedProgressNotes(TiuNoteHeader[] unsignedProgressNotes) {
    this.unsignedProgressNotes = unsignedProgressNotes;
  }
  
  public TiuNoteHeader[] getUnsignedSurgicalReports() {
    return unsignedSurgicalReports;
  }
  
  public void setUnsignedSurgicalReports(TiuNoteHeader[] unsignedSurgicalReports) {
    this.unsignedSurgicalReports = unsignedSurgicalReports;
  }

  public TiuNoteHeader[] getUnsignedByExpectedAdditonalSignerNotes() {
    return unsignedByExpectedAdditonalSignerNotes;
  }

  public void setUnsignedByExpectedAdditonalSignerNotes(
      TiuNoteHeader[] unsignedByExpectedAdditonalSignerNotes) {
    this.unsignedByExpectedAdditonalSignerNotes = unsignedByExpectedAdditonalSignerNotes;
  }
  
}


