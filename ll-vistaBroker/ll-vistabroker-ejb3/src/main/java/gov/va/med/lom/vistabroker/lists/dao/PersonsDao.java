package gov.va.med.lom.vistabroker.lists.dao;

import gov.va.med.lom.javaUtils.misc.StringUtils;
import gov.va.med.lom.vistabroker.dao.BaseDao;
import gov.va.med.lom.vistabroker.lists.data.ListItem;
import gov.va.med.lom.vistabroker.util.FMDateUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PersonsDao extends BaseDao {
	
  // CONSTRUCTORS
  public PersonsDao() {
    super();
  }
  
  public PersonsDao(BaseDao baseDao) {
    super(baseDao);
  }
  
  // RPC API
  // Returns a list of persons 
  public synchronized List<ListItem> getSubSetOfPersons(String startFrom, int direction) throws Exception {
    setDefaultContext("OR CPRS GUI CHART");
    setDefaultRpcName("ORWU NEWPERS");
    Object[] params = {startFrom, direction};
    List<String> list = lCall(params);
    return getItems(list);
  }
   
  // Returns a list of providers (for use in a long list box)
  public synchronized List<ListItem> getSubSetOfProviders(String startFrom, int direction) throws Exception {
    setDefaultContext("OR CPRS GUI CHART");
    setDefaultRpcName("ORWU NEWPERS");
    Object[] params = {startFrom, direction, "PROVIDER"};
    List<String> list = lCall(params);
    return getItems(list);
  }
  
  // Returns a list of providers (for use in a long list box)
  public synchronized List<ListItem> getSubSetOfProvidersWithClass(String startFrom, int direction, Date dateTime) throws Exception {
    setDefaultContext("OR CPRS GUI CHART");
    setDefaultRpcName("ORWU NEWPERS");
    if (dateTime == null)
      dateTime = new Date();
    double fmDate = FMDateUtils.dateToFMDate(dateTime);
    String[] params = {startFrom, String.valueOf(direction), "PROVIDER", String.valueOf(fmDate)};
    List<String> list = lCall(params);
    return getItems(list);
  }
  
  // Returns a list of providers
  public synchronized List<ListItem> listProvidersAll() throws Exception {
    setDefaultContext("OR CPRS GUI CHART");
    setDefaultRpcName("ORQPT PROVIDERS");
    List<String> list = lCall();
    return getItems(list);
  }  
  
  // Returns a list of users (for use in a long list box)
  public synchronized List<ListItem> getSubSetOfUsers(String startFrom, int direction) throws Exception {
    setDefaultContext("OR CPRS GUI CHART");
    setDefaultRpcName("ORWU NEWPERS");
    Object[] params = {startFrom, direction, ""};
    List<String> list = lCall(params);
    return getItems(list);
  }
  
  // Returns a list of users (for use in a long list box)
  public synchronized List<ListItem> getSubSetOfUsersByType(String startFrom, int direction,
                                                            String type) throws Exception {
    setDefaultContext("OR CPRS GUI CHART");
    setDefaultRpcName("ORWU NEWPERS");
    Object[] params = {startFrom, direction, type};
    List<String> list = lCall(params);
    return getItems(list);
  }
  
  // Returns a list of users (for use in a long list box)
  public synchronized List<ListItem> getSubSetOfUsersWithClass(String startFrom, int direction, 
                                                               String dateTime) throws Exception {
    setDefaultContext("OR CPRS GUI CHART");
    setDefaultRpcName("ORWU NEWPERS");
    Object[] params = {startFrom, direction, dateTime};
    List<String> list = lCall(params);
    return getItems(list);
  }
  
  // Returns a list of users (for use in a long list box)
  public synchronized List<ListItem> getSubSetOfActiveAndInactivePersons(String startFrom, 
                                                                         int direction) throws Exception {
    setDefaultContext("OR CPRS GUI CHART");
    setDefaultRpcName("ORWU NEWPERS");
    Object[] params = {startFrom, direction, "", "", "true"};
    List<String> list = lCall(params);
    return getItems(list);
  }
  
  // Returns a list of attendings (for use in a long list box)
  public synchronized List<ListItem> getSubSetOfAttendings(String startFrom, int direction,
                                                           Date dateTime, String docTypeIen, String docIen) throws Exception {
    setDefaultContext("OR CPRS GUI CHART");
    setDefaultRpcName("ORWU2 COSIGNER");
    String fmDate = null;
    if (dateTime != null)
      fmDate = String.valueOf(FMDateUtils.dateToFMDate(dateTime));
    else
      fmDate = "";
    Object[] params = {startFrom, String.valueOf(direction), fmDate, docTypeIen, docIen};
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
