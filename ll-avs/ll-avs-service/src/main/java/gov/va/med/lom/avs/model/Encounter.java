package gov.va.med.lom.avs.model;

import java.util.List;
import java.util.ArrayList;


public class Encounter {

  private List<EncounterProvider> providers;
  private EncounterLocation location;
  private double encounterDatetime;
  private String encounterNoteIen;
  private String visitString;
  
  public Encounter() {
    this.providers = new ArrayList<EncounterProvider>();
  }
  
  public void addProvider(EncounterProvider provider) {
    this.providers.add(provider);
  }

  public List<EncounterProvider> getProviders() {
    return providers;
  }

  public void setProviders(List<EncounterProvider> providers) {
    this.providers = providers;
  }

  public EncounterLocation getLocation() {
    return location;
  }

  public void setLocation(EncounterLocation location) {
    this.location = location;
  }

  public double getEncounterDatetime() {
    return encounterDatetime;
  }

  public void setEncounterDatetime(double encounterDatetime) {
    this.encounterDatetime = encounterDatetime;
  }

  public String getVisitString() {
    return visitString;
  }

  public void setVisitString(String visitString) {
    this.visitString = visitString;
  }

  public String getEncounterNoteIen() {
    return encounterNoteIen;
  }

  public void setEncounterNoteIen(String encounterNoteIen) {
    this.encounterNoteIen = encounterNoteIen;
  }

}