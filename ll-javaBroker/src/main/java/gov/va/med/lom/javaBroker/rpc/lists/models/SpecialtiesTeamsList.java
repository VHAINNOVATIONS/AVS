package gov.va.med.lom.javaBroker.rpc.lists.models;

import gov.va.med.lom.javaBroker.rpc.BaseBean;

import java.io.Serializable;

public class SpecialtiesTeamsList extends BaseBean implements Serializable {

  private SpecialtyTeam[] specialtiesTeams;
  
  public SpecialtiesTeamsList() {
    this.specialtiesTeams = null;
  }
  
  public SpecialtyTeam[] getSpecialtiesTeams() {
    return specialtiesTeams;
  }

  public void setSpecialtiesTeams(SpecialtyTeam[] specialtiesTeams) {
    this.specialtiesTeams = specialtiesTeams;
  }
}
