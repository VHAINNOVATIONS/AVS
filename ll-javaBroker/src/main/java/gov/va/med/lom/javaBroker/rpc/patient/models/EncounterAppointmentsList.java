package gov.va.med.lom.javaBroker.rpc.patient.models;

import java.io.Serializable;

import gov.va.med.lom.javaBroker.rpc.BaseBean;

public class EncounterAppointmentsList extends BaseBean implements Serializable {

  private EncounterAppointment[] encounterAppointments;
  
  public EncounterAppointmentsList() {
    this.encounterAppointments = null;
  }
  
  public EncounterAppointment[] getEncounterAppointments() {
    return encounterAppointments;
  }
  
  public void setEncounterAppointments(EncounterAppointment[] encounterAppointments) {
    this.encounterAppointments = encounterAppointments;
  }
}
