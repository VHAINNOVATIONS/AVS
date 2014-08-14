package gov.va.med.lom.javaBroker.rpc.lists;

import java.util.ArrayList;

import gov.va.med.lom.javaBroker.rpc.*;
import gov.va.med.lom.javaBroker.rpc.lists.models.*;
import gov.va.med.lom.javaBroker.util.StringUtils;

public class SpecialtiesTeamsRpc extends AbstractRpc {
	
  // FIELDS
  private SpecialtiesTeamsList specialtiesTeamsList;

  // CONSTRUCTORS
  public SpecialtiesTeamsRpc() throws BrokerException {
    super();
  }
  
  public SpecialtiesTeamsRpc(RpcBroker rpcBroker) throws BrokerException {
    super(rpcBroker);
  }
  
  // RPC API
  // Lists all treating specialties
  public synchronized SpecialtiesTeamsList listAllSpecialties() throws BrokerException {
    if (setContext("OR CPRS GUI CHART")) {
      ArrayList list = lCall("ORQPT SPECIALTIES");
      SpecialtyTeam[] specialtiesTeams = getItems(list, true);
      specialtiesTeamsList = new SpecialtiesTeamsList();
      specialtiesTeamsList.setSpecialtiesTeams(specialtiesTeams);
      return specialtiesTeamsList;
    } else
      throw getCreateContextException("OR CPRS GUI CHART");
  }
  
  // Lists all patient care teams
  public synchronized SpecialtiesTeamsList listAllTeams() throws BrokerException {
    if (setContext("OR CPRS GUI CHART")) {
      ArrayList list = lCall("ORQPT TEAMS");
      SpecialtyTeam[] specialtiesTeams = getItems(list, true);
      specialtiesTeamsList = new SpecialtiesTeamsList();
      specialtiesTeamsList.setSpecialtiesTeams(specialtiesTeams);
      return specialtiesTeamsList;
    } else
      throw getCreateContextException("OR CPRS GUI CHART");
  }
  
  // Parses the result list from VistA and returns array of patient list item objects
  private SpecialtyTeam[] getItems(ArrayList list, boolean mixedCase) {
    SpecialtyTeam[] specialtiesTeams = new SpecialtyTeam[list.size()]; 
    for(int i=0; i < list.size(); i++) {
      SpecialtyTeam specialtyTeam = new SpecialtyTeam();
      String x = (String)list.get(i);
      String ien = StringUtils.piece(x, 1);
      String name;
      if (mixedCase)
        name = StringUtils.mixedCase(StringUtils.piece(x, 2));
      else
        name = StringUtils.piece(x, 2);
      specialtyTeam.setIen(ien);
      specialtyTeam.setName(name);      
      specialtiesTeams[i] = specialtyTeam;
    }
    return specialtiesTeams;
  }  
}
