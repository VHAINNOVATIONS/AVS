package gov.va.med.lom.javaBroker.rpc.user;

import java.util.ArrayList;

import gov.va.med.lom.javaBroker.rpc.*;
import gov.va.med.lom.javaBroker.rpc.user.models.*;

import gov.va.med.lom.javaBroker.util.DateUtils;
import gov.va.med.lom.javaBroker.util.Hash;
import gov.va.med.lom.javaBroker.util.StringUtils;

public class VistaUserRpc extends AbstractRpc {

  // FIELDS
  private VistaUser vistaUser;
  
  // CONSTRUCTORS
  public VistaUserRpc() throws BrokerException {
    super();
  }
  
  public VistaUserRpc(RpcBroker rpcBroker) throws BrokerException {
    super(rpcBroker);
  }
  
  // EXECUTE RPC API  
  public synchronized VistaUser getVistaUser() throws BrokerException {
    if (setContext("OR CPRS GUI CHART")) {
      vistaUser = new VistaUser();       
      String x = sCall("ORWU USERINFO");
      StringBuffer sb = null;
      if (returnRpcResult) {
        sb = new StringBuffer();
        sb.append(x + "\n");
      }      
      setDuz(StringUtils.piece(x, 1));
      vistaUser.setDuz(getDuz());
      vistaUser.setUserClass(StringUtils.piece(x, 3));
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
      vistaUser.setService(StringUtils.piece(x, 14));
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
      
      rpcBroker.setUserClass(vistaUser.getUserClass());
      rpcBroker.setDomain(vistaUser.getDomain());
      rpcBroker.setService(vistaUser.getService());
      
      ArrayList list = lCall("XUS GET USER INFO");
      if (returnRpcResult) {
        for(int i = 0; i < list.size(); i++)
          sb.append((String)list.get(i) + "\n");
        vistaUser.setRpcResult(sb.toString().trim());
      }      
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
        
        rpcBroker.setUserName(vistaUser.getName());
        rpcBroker.setStationIen(vistaUser.getStationIen());
        rpcBroker.setStation(vistaUser.getStation());
        rpcBroker.setStationNo(vistaUser.getStationNumber());
        rpcBroker.setServiceSection(vistaUser.getServiceSection());
        rpcBroker.setTitle(vistaUser.getTitle());
      }
      return vistaUser;
    } else
      throw getCreateContextException("OR CPRS GUI CHART");
  }   
  
  public synchronized ChangeVerifyCodeResult changeVerifyCode(String oldVerifyCode, String newVerifyCode,
                                                              String confirmVerifyCode) throws BrokerException {
    if (setContext("XUS SIGNON")) {
      Object[] params = {Hash.encrypt(oldVerifyCode.toUpperCase()) + "^" +
                         Hash.encrypt(newVerifyCode.toUpperCase()) + "^" +
                         Hash.encrypt(confirmVerifyCode.toUpperCase())}; 
      ArrayList list = lCall("XUS CVC", params);
      ChangeVerifyCodeResult changeVerifyCodeResult = new ChangeVerifyCodeResult();
      if (returnRpcResult) {
        StringBuffer sb = new StringBuffer();
        for(int i = 0; i < list.size(); i++)
          sb.append((String)list.get(i) + "\n");
        changeVerifyCodeResult.setRpcResult(sb.toString().trim());
      }    
      if (list.size() > 0) {
        if (((String)list.get(0)).equals("0")) 
          changeVerifyCodeResult.setSuccess(true);
        else
          changeVerifyCodeResult.setSuccess(false);
        changeVerifyCodeResult.setMessage((String)list.get(1));
      } else {
        changeVerifyCodeResult.setSuccess(false);
        changeVerifyCodeResult.setMessage("Your VERIFY code was not changed.");
      }
      return changeVerifyCodeResult;
    } else
      throw getCreateContextException("XUS SIGNON");
  }
  
  public synchronized String getAVHelp(boolean mustEnterOldVC) throws BrokerException {
    if (setContext("XUS SIGNON")) {
      String x = sCall("XUS AV HELP");
      StringBuffer sb = new StringBuffer(x);
      sb.insert(0, "Enter a new verify code and then confirm it." + '\n');
      if (mustEnterOldVC)
        sb.insert(0, "Enter your current verify code first." + '\n');
      return sb.toString();
    } else
      throw getCreateContextException("XUS SIGNON");
  }
  
  // Simple call to return a parameter value.  The call assumes the current
  // user, 'defaultable' entities, and one instance.
  public synchronized String getUserParam(String paramName) throws BrokerException {
    if (setContext("OR CPRS GUI CHART")) {
      Object[] params = {paramName};  
      String x = sCall("ORWU PARAM", params);
      return x;
    } else
      throw getCreateContextException("OR CPRS GUI CHART");
  }
  
  // returns true if the currently logged in user has a given security key 
  public synchronized boolean hasSecurityKey(String keyName) throws BrokerException {
    if (setContext("OR CPRS GUI CHART")) {
      Object[] params = {keyName};  
      String x = sCall("ORWU HASKEY", params);
      return x.equals("1");
    } else
      throw getCreateContextException("OR CPRS GUI CHART");
  }  
  
  public synchronized boolean hasMenuOptionAccess(String optionName) throws BrokerException {
    if (setContext("OR CPRS GUI CHART")) {
      Object[] params = {optionName};  
      String x = sCall("ORWU HAS OPTION ACCESS", params);
      return x.equals("1");
    } else
      throw getCreateContextException("OR CPRS GUI CHART");
  }    
  
  public synchronized boolean userInactive(String duz) throws BrokerException {
    if (setContext("OR CPRS GUI CHART")) {
      String x = sCall("TIU USER INACTIVE?", duz);
      return !x.equals("0");
    } else
      throw getCreateContextException("OR CPRS GUI CHART");
  }     
  
  public synchronized Employee getEmployeeByDuz(String duz) throws BrokerException {
    // StationNo^DUZ^Name^Title^Ext^Pager^Room^Termination Date^SSN^^
    // Mail Code^Paid ID^Last Sign-On^Service IEN^Degree^Program^
    if (setContext("ALT INTRANET RPCS")) {
      String x = sCall("ALSI EMI EMPLOYEE GET", duz);
      Employee employee = new Employee();
      String s = null;
      s = StringUtils.piece(x, 1);
      if ((s != null) && (s.length() > 0))
        employee.setStationNo(Integer.parseInt(s));
      s = StringUtils.piece(x, 2);
      if ((s != null) && (s.length() > 0))
        employee.setDuz(s);
      employee.setName(StringUtils.piece(x, 3));
      employee.setTitle(StringUtils.piece(x, 4));
      employee.setExtension(StringUtils.piece(x, 5));
      employee.setPager(StringUtils.piece(x, 6));
      employee.setRoom(StringUtils.piece(x, 7));
      s = StringUtils.piece(x, 8);
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
      s = StringUtils.piece(x, 14);
      if ((s != null) && (s.length() > 0))
        employee.setServiceIen(Integer.parseInt(s));
      employee.setDegree(StringUtils.piece(x, 15));
      employee.setProgram(StringUtils.piece(x, 16));
      return employee;
    } else
      throw getCreateContextException("ALT INTRANET RPCS");
  }
  
}
