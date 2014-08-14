package gov.va.med.lom.vistabroker.lists.dao;

import gov.va.med.lom.javaUtils.misc.StringUtils;
import gov.va.med.lom.vistabroker.dao.BaseDao;
import gov.va.med.lom.vistabroker.lists.data.ListItem;

import java.util.ArrayList;
import java.util.List;

public class ProceduresDao extends BaseDao {
	
  // CONSTRUCTORS
  public ProceduresDao() {
    super();
  }
  
  public ProceduresDao(BaseDao baseDao) {
    super(baseDao);
  }
  
  // RPC API
  /**
   * Returns init values for procedures dialog.
   */
  public synchronized List<String> odForProcedures() throws Exception {
    setDefaultContext("OR CPRS GUI CHART");
    setDefaultRpcName("ORWDCN32 DEF");
    return lCall("P");
  }
  
  public synchronized List<ListItem> getSubSetOfProcedures(String startFrom, int direction) throws Exception {
    setDefaultContext("OR CPRS GUI CHART");
    setDefaultRpcName("ORWDCN32 PROCEDURES");
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
