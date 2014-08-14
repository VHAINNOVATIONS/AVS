package gov.va.med.lom.vistabroker.lists.dao;

import gov.va.med.lom.javaUtils.misc.StringUtils;
import gov.va.med.lom.vistabroker.dao.BaseDao;
import gov.va.med.lom.vistabroker.lists.data.ListItem;

import java.util.ArrayList;
import java.util.List;

public class LabTestTypesDao extends BaseDao {
	
  // CONSTRUCTORS
  public LabTestTypesDao() {
    super();
  }
  
  public LabTestTypesDao(BaseDao baseDao) {
    super(baseDao);
  }
  
  // RPC API
  
  public synchronized List<ListItem> getAtomicTests(String startFrom, int direction) throws Exception {
    setDefaultContext("OR CPRS GUI CHART");
    setDefaultRpcName("ORWLRR ATOMICS");
    Object[] params = {startFrom, String.valueOf(direction)};
    List<String> list = lCall(params);
    return getList(list);
  }
  
  public synchronized List<ListItem> getSpecimens(String startFrom, int direction) throws Exception {
    setDefaultContext("OR CPRS GUI CHART");
    setDefaultRpcName("ORWLRR SPEC");
    Object[] params = {startFrom, String.valueOf(direction)};
    List<String> list = lCall(params);
    return getList(list);
  }  
  
  public synchronized List<ListItem> getAllTests(String startFrom, int direction) throws Exception {
    setDefaultContext("OR CPRS GUI CHART");
    setDefaultRpcName("ORWLRR ALLTESTS");
    Object[] params = {startFrom, String.valueOf(direction)};
    List<String> list = lCall(params);
    return getList(list);
  }   
  
  public synchronized List<ListItem> getChemTests(String startFrom, int direction) throws Exception {
    setDefaultContext("OR CPRS GUI CHART");
    setDefaultRpcName("ORWLRR CHEMTEST");
    Object[] params = {startFrom, String.valueOf(direction)};
    List<String> list = lCall(params);
    return getList(list);
  }   
  
  public synchronized List<ListItem> getLabUsers(String startFrom, int direction) throws Exception {
    setDefaultContext("OR CPRS GUI CHART");
    setDefaultRpcName("ORWLRR USERS");
    Object[] params = {startFrom, String.valueOf(direction)};
    List<String> list = lCall(params);
    return getList(list);
  }    
  
  public synchronized List<ListItem> getTestGroupsForLabUser(String userDuz) throws Exception {
    setDefaultContext("OR CPRS GUI CHART");
    setDefaultRpcName("ORWLRR TG");
    List<String> list = lCall(userDuz);
    return getList(list);
  }     
  
  public synchronized List<ListItem> getATest(String testIen) throws Exception {
    setDefaultContext("OR CPRS GUI CHART");
    setDefaultRpcName("ORWLRR ATESTS");
    List<String> list = lCall(testIen);
    return getList(list);
  }      
  
  public synchronized List<ListItem> getATestGroup(String testGroupIen, String userDuz) throws Exception {
    setDefaultContext("OR CPRS GUI CHART");
    setDefaultRpcName("ORWLRR ATG");
    Object[] params = {testGroupIen, userDuz};
    List<String> list = lCall(params);
    return getList(list);
  }     
  
  public synchronized String getTestInfo(String testIen) throws Exception {
    setDefaultContext("OR CPRS GUI CHART");
    setDefaultRpcName("ORWLRR INFO");
    return sCall(testIen);
  }   

  public synchronized List<ListItem> getLabReportLists() throws Exception {
    setDefaultContext("OR CPRS GUI CHART");
    setDefaultRpcName("ORWRP LAB REPORT LISTS");
    List<String> list = lCall();
    String[] results = StringUtils.extractSection(list, "[LAB REPORT LIST]", true);
    List<ListItem> labReports = new ArrayList<ListItem>();
    for(int i = 0; i < results.length; i++) {
      ListItem listItem = new ListItem();
      listItem.setIen(StringUtils.piece(results[i], 1));
      listItem.setName(StringUtils.piece(results[i], 2));
      listItem.setValue(StringUtils.piece(results[i], 6));
      listItem.setRpcResult(results[i]);
      labReports.add(listItem);
    }
    return labReports;
  }
  
  private synchronized List<ListItem> getList(List<String> list) {
    List<ListItem> listItems = new ArrayList<ListItem>();
    for(int i = 0; i < list.size(); i++) {
      String x = (String)list.get(i);  
      ListItem listItem = new ListItem();
      listItem.setIen(StringUtils.piece(x, 1));
      listItem.setName(StringUtils.piece(x, 2));
      listItem.setValue(StringUtils.piece(x, 3));
      listItem.setRpcResult(x);
      listItems.add(listItem);
    }
    return listItems;    
  }  
  
}
