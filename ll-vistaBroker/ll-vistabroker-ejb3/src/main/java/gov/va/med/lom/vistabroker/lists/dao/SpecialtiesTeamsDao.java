package gov.va.med.lom.vistabroker.lists.dao;

import gov.va.med.lom.javaUtils.misc.StringUtils;
import gov.va.med.lom.vistabroker.dao.BaseDao;
import gov.va.med.lom.vistabroker.lists.data.ListItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SpecialtiesTeamsDao extends BaseDao {
	
  /*
   * CONSTRUCTORS
   */
  public SpecialtiesTeamsDao()  {
    super();
  }
  
  public SpecialtiesTeamsDao(BaseDao baseDao) {
    super(baseDao);
  }
  
  /*
   * RPC API
   */ 
  // Lists all treating specialties
  public synchronized List<ListItem> listAllSpecialties() throws Exception {
	setDefaultContext("OR CPRS GUI CHART");
  setDefaultRpcName("ORQPT SPECIALTIES");
    List<String> list = lCall();
    return getItems(list, false);
  }
  
  // Lists all patient care teams
  public synchronized List<ListItem> listAllTeams() throws Exception {
    setDefaultContext("OR CPRS GUI CHART");
    setDefaultRpcName("ORQPT TEAMS");
    List<String> list = lCall();
    return getItems(list, false);
  }
  
  public Map<String,String> listAllTeamsMap() throws Exception {
	  List<ListItem> list = listAllTeams();
	  return getMap(list);
  }
  
  public Map<String,String> listAllSpecialtiesMap() throws Exception {
	  List<ListItem> list = listAllSpecialties();
	  return getMap(list);
  }
  
  private Map<String, String> getMap(List<ListItem> list){
	  Map<String, String> m = new HashMap<String, String>();
	  for(ListItem t : list){
		  m.put(t.getIen(), t.getName());
	  }
	  return m;
  }
  
  // Parses the result list from VistA and returns array of patient list item objects
  private List<ListItem> getItems(List<String> list, boolean mixedCase) {
    List<ListItem> specialtyTeamList = new ArrayList<ListItem>(); 
    for(String x : list) {
      ListItem listItem = new ListItem();
      String ien = StringUtils.piece(x, 1);
      String name;
      if (mixedCase)
        name = StringUtils.mixedCase(StringUtils.piece(x, 2));
      else
        name = StringUtils.piece(x, 2);
      listItem.setIen(ien);
      listItem.setName(name);      
      listItem.setValue(StringUtils.piece(x, 3));
      listItem.setRpcResult(x);
      specialtyTeamList.add(listItem);
    }
    return specialtyTeamList;
  }  
}
