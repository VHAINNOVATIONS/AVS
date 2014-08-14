package gov.va.med.lom.vistabroker.lists.dao;

import gov.va.med.lom.javaUtils.misc.StringUtils;
import gov.va.med.lom.vistabroker.dao.BaseDao;
import gov.va.med.lom.vistabroker.lists.data.ListItem;

import java.util.ArrayList;
import java.util.List;

public class StatusUrgenciesDao extends BaseDao {
  
  // CONSTRUCTORS
  public StatusUrgenciesDao() {
    super();
  }
  
  public StatusUrgenciesDao(BaseDao baseDao) {
    super(baseDao);
  }
  
  // RPC API
  public synchronized List<ListItem> subSetOfStatus() throws Exception {
    setDefaultContext("OR CPRS GUI CHART");
    setDefaultRpcName("ORQQCN STATUS");
    List<String> list = lCall();
    return getItems(list);
  }

  public synchronized List<ListItem> subSetOfUrgencies(String consultIen) throws Exception {
    setDefaultContext("OR CPRS GUI CHART");
    setDefaultRpcName("ORQQCN URGENCIES");
    List<String> list = lCall(consultIen);
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
