package gov.va.med.lom.avs.web.util;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.SortedMap;

import gov.va.med.lom.avs.enumeration.DGroupSeq;
import gov.va.med.lom.avs.model.MedicationRdv;
import gov.va.med.lom.avs.model.PceData;
import gov.va.med.lom.vistabroker.patient.data.AllergiesReactants;
import gov.va.med.lom.vistabroker.patient.data.Appointment;
import gov.va.med.lom.vistabroker.patient.data.DiscreteItemData;
import gov.va.med.lom.vistabroker.patient.data.Encounter;
import gov.va.med.lom.vistabroker.patient.data.Medication;
import gov.va.med.lom.vistabroker.patient.data.VitalSignMeasurement;

public interface ISheetCallback {

  public abstract void renderClinicsVisitedSection(Collection<Encounter> encounters);
  
  public abstract void renderProviders(PceData pceData);
  
  public abstract void renderDiagnoses(PceData pceData);
  
  public abstract void renderVitalsSection(Collection<VitalSignMeasurement> vitals);
  
  public abstract void renderImmunizations(PceData pceData);
  
  public abstract void renderOrdersSection(LinkedHashMap<DGroupSeq, SortedMap<String,List<String>>> orders);
  
  public abstract void renderAppointmentsSection
      (LinkedHashMap<Double, LinkedHashMap<Double, Appointment>> appointments);
  
  public abstract void renderProviderSection();
  
  public abstract void renderAllergiesSection(AllergiesReactants localAllergiesReactions,
      AllergiesReactants remoteAllergiesReactions);
  
  public abstract void renderLocalVAMedicationsSection(Collection<Medication> localVaMedications);
  
  public abstract void renderNonVAMedicationsSection(Collection<MedicationRdv> localNonVaMedications,
      Collection<MedicationRdv> remoteNonVaMedications);
  
  public abstract void renderRemoteVaMedicationsSection(Collection<MedicationRdv> remoteVaMedications);
  
  public abstract void renderChartsSection(LinkedHashMap<String, List<DiscreteItemData>> chartsData);
  
  public abstract void renderLabResultsSection(String labResults);
  
}

