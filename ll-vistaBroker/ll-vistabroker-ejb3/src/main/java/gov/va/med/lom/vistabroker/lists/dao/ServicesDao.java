package gov.va.med.lom.vistabroker.lists.dao;

import gov.va.med.lom.javaUtils.misc.StringUtils;
import gov.va.med.lom.vistabroker.dao.BaseDao;
import gov.va.med.lom.vistabroker.lists.data.ListItem;

import java.util.ArrayList;
import java.util.List;

public class ServicesDao extends BaseDao {
	
  // CONSTRUCTORS
  public ServicesDao() {
    super();
  }
  
  public ServicesDao(BaseDao baseDao) {
    super(baseDao);
  }
  
  // RPC API
  /**
   * purpose: 0=display all services, 1=forward or order from possible services
   */
  public synchronized List<ListItem> getServiceList(int purpose) throws Exception {
    setDefaultContext("OR CPRS GUI CHART");
    setDefaultRpcName("ORQQCN SVCTREE");
    List<String> list = lCall(purpose);
    return getItems(list);
  }
  
  public synchronized List<ListItem> getServiceListWithSynonyms(int purpose) throws Exception {
    setDefaultContext("OR CPRS GUI CHART");   
    setDefaultRpcName("ORQQCN SVC W/SYNONYMS");
    Object[] params = {"1", String.valueOf(purpose), "true"};
    List<String> list = lCall(params);
    return getItems(list);
  }  
  
  public synchronized List<ListItem> getServiceListWithSynonymsForConsult(int purpose, String consultIen) throws Exception {
    setDefaultContext("OR CPRS GUI CHART");
    setDefaultRpcName("ORQQCN SVC W/SYNONYMS");
    Object[] params = {"1", purpose, "true", consultIen};
    List<String> list = lCall(params);
    return getItems(list);
  }    
  
  //lists all hospital locations
  public synchronized List<ListItem> getSubSetOfServices(String startFrom, int direction) throws Exception {
    setDefaultContext("OR CPRS GUI CHART");
    setDefaultRpcName("ORQQCN SVCLIST");
    Object[] params = {startFrom, direction};
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
