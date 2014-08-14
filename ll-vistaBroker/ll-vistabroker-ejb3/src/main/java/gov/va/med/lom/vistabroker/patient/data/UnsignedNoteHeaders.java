package gov.va.med.lom.vistabroker.patient.data;

import java.io.Serializable;
import java.util.List;

public class UnsignedNoteHeaders implements Serializable {

  private String summary;
  private List<TiuNoteHeader> unsignedProgressNotes;
  private List<TiuNoteHeader> uncosignedProgressNotes;
  private List<TiuNoteHeader> unsignedDischargeSummaries;
  private List<TiuNoteHeader> uncosignedDischargeSummaries;
  private List<TiuNoteHeader> unsignedClinicalProcedures;
  private List<TiuNoteHeader> uncosignedClinicalProcedures;
  private List<TiuNoteHeader> unsignedSurgicalReports;
  private List<TiuNoteHeader> uncosignedSurgicalReports;
  private List<TiuNoteHeader> unsignedByExpectedAdditonalSignerNotes;
  
  public String getSummary() {
    return summary;
  }
  
  public void setSummary(String summary) {
    this.summary = summary;
  }
  
  public List<TiuNoteHeader> getUncosignedClinicalProcedures() {
    return uncosignedClinicalProcedures;
  }
  
  public void setUncosignedClinicalProcedures(
      List<TiuNoteHeader> uncosignedClinicalProcedures) {
    this.uncosignedClinicalProcedures = uncosignedClinicalProcedures;
  }
  
  public List<TiuNoteHeader> getUncosignedDischargeSummaries() {
    return uncosignedDischargeSummaries;
  }
  
  public void setUncosignedDischargeSummaries(
      List<TiuNoteHeader> uncosignedDischargeSummaries) {
    this.uncosignedDischargeSummaries = uncosignedDischargeSummaries;
  }
  
  public List<TiuNoteHeader> getUncosignedProgressNotes() {
    return uncosignedProgressNotes;
  }
  
  public void setUncosignedProgressNotes(List<TiuNoteHeader> uncosignedProgressNotes) {
    this.uncosignedProgressNotes = uncosignedProgressNotes;
  }
  
  public List<TiuNoteHeader> getUncosignedSurgicalReports() {
    return uncosignedSurgicalReports;
  }
  
  public void setUncosignedSurgicalReports(
      List<TiuNoteHeader> uncosignedSurgicalReports) {
    this.uncosignedSurgicalReports = uncosignedSurgicalReports;
  }
  
  public List<TiuNoteHeader> getUnsignedClinicalProcedures() {
    return unsignedClinicalProcedures;
  }
  
  public void setUnsignedClinicalProcedures(
      List<TiuNoteHeader> unsignedClinicalProcedures) {
    this.unsignedClinicalProcedures = unsignedClinicalProcedures;
  }
  
  public List<TiuNoteHeader> getUnsignedDischargeSummaries() {
    return unsignedDischargeSummaries;
  }
  
  public void setUnsignedDischargeSummaries(
      List<TiuNoteHeader> unsignedDischargeSummaries) {
    this.unsignedDischargeSummaries = unsignedDischargeSummaries;
  }
  
  public List<TiuNoteHeader> getUnsignedProgressNotes() {
    return unsignedProgressNotes;
  }
  
  public void setUnsignedProgressNotes(List<TiuNoteHeader> unsignedProgressNotes) {
    this.unsignedProgressNotes = unsignedProgressNotes;
  }
  
  public List<TiuNoteHeader> getUnsignedSurgicalReports() {
    return unsignedSurgicalReports;
  }
  
  public void setUnsignedSurgicalReports(List<TiuNoteHeader> unsignedSurgicalReports) {
    this.unsignedSurgicalReports = unsignedSurgicalReports;
  }

  public List<TiuNoteHeader> getUnsignedByExpectedAdditonalSignerNotes() {
    return unsignedByExpectedAdditonalSignerNotes;
  }

  public void setUnsignedByExpectedAdditonalSignerNotes(
      List<TiuNoteHeader> unsignedByExpectedAdditonalSignerNotes) {
    this.unsignedByExpectedAdditonalSignerNotes = unsignedByExpectedAdditonalSignerNotes;
  }
  
}


