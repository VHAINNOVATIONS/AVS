package gov.va.med.lom.vistabroker.user.dao;

import gov.va.med.lom.javaUtils.misc.DateUtils;
import gov.va.med.lom.javaUtils.misc.StringUtils;
import gov.va.med.lom.vistabroker.dao.BaseDao;
import gov.va.med.lom.vistabroker.lists.data.ListItem;
import gov.va.med.lom.vistabroker.user.data.Employee;
import gov.va.med.lom.vistabroker.user.data.VistaUser;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class VistaUserDao extends BaseDao {

  @SuppressWarnings("unused")
  private static final Log log = LogFactory.getLog(VistaUserDao.class);
	
  // CONSTRUCTORS
  public VistaUserDao() {
    super();
  }
  
  public VistaUserDao(BaseDao baseDao) {
    super(baseDao);
  }
  
  // RPC API  
  public VistaUser getVistaUser() throws Exception {
    setDefaultContext("OR CPRS GUI CHART");
    setDefaultRpcName("ORWU USERINFO");
    VistaUser vistaUser = new VistaUser();       
    String x = sCall();
    vistaUser.setDuz(StringUtils.piece(x, 1));
    vistaUser.setUserClass(StringUtils.toInt(StringUtils.piece(x, 3), 0));
    vistaUser.setCanSignOrders(StringUtils.piece(x, 4).equals("1"));
    vistaUser.setProvider(StringUtils.piece(x, 5).equals("1"));
    vistaUser.setOrderRole(StringUtils.toInt(StringUtils.piece(x, 6), 0));
    vistaUser.setNoOrdering(StringUtils.piece(x, 7).equals("1"));
    vistaUser.setDTime(StringUtils.toInt(StringUtils.piece(x, 8), 300));
    vistaUser.setCountDown(StringUtils.toInt(StringUtils.piece(x, 9), 10));
    vistaUser.setEnableVerify(StringUtils.piece(x, 10).equals("1"));
    vistaUser.setNotifyAppsWM(StringUtils.piece(x, 11).equals("1"));
    vistaUser.setPtMsgHang(StringUtils.toInt(StringUtils.piece(x, 12), 5));
    vistaUser.setDomain(StringUtils.piece(x, 13));
    vistaUser.setService(StringUtils.toInt(StringUtils.piece(x, 14), 0));
    vistaUser.setAutoSave(StringUtils.toInt(StringUtils.piece(x, 15), 180));
    vistaUser.setInitialTab(StringUtils.toInt(StringUtils.piece(x, 16), 1));
    vistaUser.setUseLastTab(StringUtils.piece(x, 17).equals("1"));
    vistaUser.setWebAccess(!StringUtils.piece(x, 18).equals("1"));
    vistaUser.setDisableHold(StringUtils.piece(x, 19).equals("1"));
    vistaUser.setIsRPL(StringUtils.piece(x, 20));
    vistaUser.setRplList(StringUtils.piece(x, 21));
    vistaUser.setHasCorTabs(StringUtils.piece(x, 22).equals("1"));
    vistaUser.setHasRptTab(StringUtils.piece(x, 23).equals("1"));
    vistaUser.setReportsOnly(false);
    setDefaultRpcName("XUS GET USER INFO");
    List<String> list = lCall();
    if (list.size() >= 8) {
      vistaUser.setName((String)list.get(1));
      vistaUser.setStandardName((String)list.get(2));
      String division = (String)list.get(3);
      vistaUser.setStationIen(StringUtils.piece(division, 1));
      vistaUser.setStation(StringUtils.piece(division, 2));
      vistaUser.setStationNumber(StringUtils.piece(division, 3));
      vistaUser.setTitle((String)list.get(4));
      vistaUser.setServiceSection((String)list.get(5));
      int language = 0;
      try {
        language = Integer.valueOf((String)list.get(6)).intValue();
      } catch(NumberFormatException nfe) {
        // ignore
      }
      vistaUser.setLanguage(language);
    }
    return vistaUser;
  }   

  // Simple call to return a parameter value.  The call assumes the current
  // user, 'defaultable' entities, and one instance.
  public String getUserParam(String paramName) throws Exception {
    setDefaultContext("OR CPRS GUI CHART");
    setDefaultRpcName("ORWU PARAM");
    Object[] params = {paramName};  
    String x = sCall(params);
    return x;
  }
  
  // returns true if the currently logged in user has a given security key 
  public boolean hasSecurityKey(String keyName) throws Exception {
    setDefaultContext("OR CPRS GUI CHART");
    setDefaultRpcName("ORWU HASKEY");
    Object[] params = {keyName};  
    String x = sCall(params);
    return x.equals("1");
  }  
  
  // returns true if the currently logged in user has a given  key 
  public boolean hasKey(String keyName) throws Exception {
    setDefaultContext("OR CPRS GUI CHART");
    setDefaultRpcName("ORWU NPHASKEY");
    Object[] params = {keyName};  
    String x = sCall(params);
    return x.equals("1");
  }   
  
  public boolean hasMenuOptionAccess(String optionName) throws Exception {
    setDefaultContext("OR CPRS GUI CHART");
    setDefaultRpcName("ORWU HAS OPTION ACCESS");
    Object[] params = {optionName};  
    String x = sCall(params);
    return x.equals("1");
  }    
  
  public boolean userInactive(String duz) throws Exception {
    setDefaultContext("OR CPRS GUI CHART");
    setDefaultRpcName("TIU USER INACTIVE?");
    String x = sCall(duz);
    return !x.equals("0");
  }     
  
  public Employee getEmployeeByDuz(String duz) throws Exception {
    setDefaultContext("ALT INTRANET RPCS");
    setDefaultRpcName("ALSI EMI EMPLOYEE GET");
    String x = sCall(duz);
    return stringToEmployee(x);
  }
  
  public List<ListItem> getEmployeesForTL(String tlCode) throws Exception{
	  
	  setDefaultContext("ALT INTRANET RPCS");
	  setDefaultRpcName("ALSI EMI EMPLOYEES BY TL");
	  List<String> x = lCall(tlCode);
	  List<ListItem> l = new ArrayList<ListItem>();
	  if(x==null) return l;
	  for(String y : x){
		  ListItem i = new ListItem();
		  i.setIen(StringUtils.piece(y, 2));
		  i.setName(StringUtils.piece(y,3));
		  l.add(i);
	  }
	  return l;
  }
  
  public List<ListItem> getTLForSupervisor(String duz) throws Exception{
	  
	  setDefaultContext("ALT INTRANET RPCS");
	  setDefaultRpcName("ALSI EMI SUPERVISOR TL");
	  List<String> x = lCall(duz);
	  List<ListItem> l = new ArrayList<ListItem>();
	  if(x==null) return l;
	  for(String y : x){
		  ListItem i = new ListItem();
		  i.setIen(StringUtils.piece(y, 2));
		  i.setName(StringUtils.piece(y,3));
		  l.add(i);
	  }
	  return l;
  }

  public List<Employee> searchEmployeesByName(String name, 
          Boolean activeOnly, Boolean paidOnly, Integer maxResults) throws Exception{
      
      List<Employee> l = new ArrayList<Employee>();
      
      setDefaultContext("ALT INTRANET RPCS");
      setDefaultRpcName("ALSI EMI EMPLOYEE SEARCH");
      

      Object[] params = { name.toUpperCase(), activeOnly?"1":"0", paidOnly?"1":"0", maxResults.toString() };
      List<String> s = this.lCall(params);
      
      for(String x : s){
  	    Employee employee = new Employee();
	    employee.setStationNo(StringUtils.piece(x, 1));
	    employee.setDuz(StringUtils.piece(x, 2));
	    employee.setSsn(StringUtils.piece(x, 3));
	    employee.setName(StringUtils.piece(x, 4));
	    employee.setTitle(StringUtils.piece(x, 5));
	    String termDate = StringUtils.piece(x, 6);
	    if ((termDate != null) && (termDate.length() > 0)) {
	      try {
	        employee.setTerminationDate(DateUtils.convertEnglishDateStr(termDate));
	      } catch(Exception e) {}
	    }
	    employee.setExtension(StringUtils.piece(x, 7));
	    employee.setRoom(StringUtils.piece(x, 8));
	    String lastSign = StringUtils.piece(x, 13);
	    if ((lastSign != null) && (lastSign.length() > 0)) {
	      try {
	        employee.setLastSignon(DateUtils.convertEnglishDateStr(lastSign));
	      } catch(Exception e) {}
	    }
        l.add(employee);
      }
      
      return l;
  }
  
  private Employee stringToEmployee(String x){
    Employee employee = new Employee();
    employee.setStationNo(StringUtils.piece(x, 1));
    employee.setDuz(StringUtils.piece(x, 2));
    employee.setName(StringUtils.piece(x, 3));
    employee.setTitle(StringUtils.piece(x, 4));
    employee.setExtension(StringUtils.piece(x, 5));
    employee.setPager(StringUtils.piece(x, 6));
    employee.setRoom(StringUtils.piece(x, 7));
    String s = StringUtils.piece(x, 8);
    if ((s != null) && (s.length() > 0)) {
      try {
        employee.setTerminationDate(DateUtils.convertEnglishDateStr(s));
      } catch(Exception e) {}
    }
    employee.setSsn(StringUtils.piece(x, 9));
    employee.setMailCode(StringUtils.piece(x, 11));
    s = StringUtils.piece(x, 12);
    if ((s != null) && (s.length() > 0))
      employee.setPaidId(Long.parseLong(s));
    s = StringUtils.piece(x, 13);
    if ((s != null) && (s.length() > 0)) {
      try {
        employee.setLastSignon(DateUtils.convertEnglishDateStr(s));
      } catch(Exception e) {}
    }
    employee.setServiceIen(StringUtils.piece(x, 14));
    employee.setDegree(StringUtils.piece(x, 15));
    employee.setProgram(StringUtils.piece(x, 16));    
    return employee;
  }

}
