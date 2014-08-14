package gov.va.med.lom.vistabroker.lists.dao;

import gov.va.med.lom.javaUtils.misc.StringUtils;
import gov.va.med.lom.vistabroker.dao.BaseDao;
import gov.va.med.lom.vistabroker.lists.data.ListItem;

import java.util.ArrayList;
import java.util.List;

public class LocationsDao extends BaseDao {
	
  // CONSTRUCTORS
  public LocationsDao() {
    super();
  }
  
  public LocationsDao(BaseDao baseDao) {
    super(baseDao);
  }
  
  // RPC API
  // Lists all active inpatient wards
  public synchronized List<ListItem> listAllWards() throws Exception {
    setDefaultContext("OR CPRS GUI CHART");
    setDefaultRpcName("ORQPT WARDS");
    List<String> list = lCall();
    return getItems(list);
  }
  
  // Lists all hospital locations
  public synchronized List<ListItem> getSubSetOfLocations(String startFrom, int direction) throws Exception {
    setDefaultContext("OR CPRS GUI CHART");
    setDefaultRpcName("ORWU HOSPLOC");
    String[] params = {startFrom, String.valueOf(direction)};
    List<String> list = lCall(params);
    return getItems(list);
  }
  
  // Returns a list of locations (for use in a long list box)
  public synchronized List<ListItem> getSubSetOfInpatientLocations(String startFrom, int direction) throws Exception {
    setDefaultContext("OR CPRS GUI CHART");
    setDefaultRpcName("ORWU INPLOC");
    String[] params = {startFrom, String.valueOf(direction)};
    List<String> list = lCall(params);
    return getItems(list);
  }
  
  // Returns a list of locations (for use in a long list box)
  // Filtered by C, W, and Z types - i.e., Clinics, Wards, and "Other" type locations
  public synchronized List<ListItem> getSubSetOfNewLocations(String startFrom, int direction) throws Exception {
    setDefaultContext("OR CPRS GUI CHART");
    setDefaultRpcName("ORWU1 NEWLOC");
    String[] params = {startFrom, String.valueOf(direction)};
    List<String> list = lCall(params);
    return getItems(list);
  }
  
  // Returns a list of clinics
  public synchronized List<ListItem> listAllClinics() throws Exception {
    setDefaultContext("OR CPRS GUI CHART");
    setDefaultRpcName("ORQPT CLINICS");
    List<String> list = lCall();
    return getItems(list);
  }
  
  // Returns a list of clinics (for use in a long list box)
  public synchronized List<ListItem> getSubSetOfClinics(String startFrom, int direction) throws Exception {
    setDefaultContext("OR CPRS GUI CHART");
    setDefaultRpcName("ORWU CLINLOC");
    String[] params = {startFrom, String.valueOf(direction)};
    List<String> list = lCall(params);
    return getItems(list);
  }  
  
  // Parses the result list from VistA and returns array of list item objects
  private List<ListItem> getItems(List<String> list) {
    List<ListItem> listItems = new ArrayList<ListItem>();
    for(String s : list) {
      ListItem listItem = new ListItem();
      listItem.setIen(StringUtils.piece(s, 1));
      listItem.setName(StringUtils.piece(s, 2));
      listItem.setValue(StringUtils.piece(s, 3));
      listItem.setRpcResult(s);
      listItems.add(listItem);
    }
    return listItems;    
  }  
}
