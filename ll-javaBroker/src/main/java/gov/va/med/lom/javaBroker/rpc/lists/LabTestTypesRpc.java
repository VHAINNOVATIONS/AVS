package gov.va.med.lom.javaBroker.rpc.lists;

import java.util.ArrayList;

import gov.va.med.lom.javaBroker.rpc.*;
import gov.va.med.lom.javaBroker.rpc.lists.models.*;
import gov.va.med.lom.javaBroker.util.StringUtils;

public class LabTestTypesRpc extends AbstractRpc {
	
  // CONSTRUCTORS
  public LabTestTypesRpc() throws BrokerException {
    super();
  }
  
  public LabTestTypesRpc(RpcBroker rpcBroker) throws BrokerException {
    super(rpcBroker);
  }
  
  // RPC API
  public synchronized LabTestTypesList getAtomicTests(String startFrom, int direction) throws BrokerException {
    if (setContext("OR CPRS GUI CHART")) {
      Object[] params = {startFrom, String.valueOf(direction)};
      ArrayList list = lCall("ORWLRR ATOMICS", params);
      return getList(list);
    } else 
      throw getCreateContextException("OR CPRS GUI CHART");
  }
  
  public synchronized LabTestTypesList getSpecimens(String startFrom, int direction) throws BrokerException {
    if (setContext("OR CPRS GUI CHART")) {
      Object[] params = {startFrom, String.valueOf(direction)};
      ArrayList list = lCall("ORWLRR SPEC", params);
      return getList(list);
    } else 
      throw getCreateContextException("OR CPRS GUI CHART");
  }  
  
  public synchronized LabTestTypesList getAllTests(String startFrom, int direction) throws BrokerException {
    if (setContext("OR CPRS GUI CHART")) {
      Object[] params = {startFrom, String.valueOf(direction)};
      ArrayList list = lCall("ORWLRR ALLTESTS", params);
      return getList(list);
    } else 
      throw getCreateContextException("OR CPRS GUI CHART");
  }   
  
  public synchronized LabTestTypesList getChemTests(String startFrom, int direction) throws BrokerException {
    if (setContext("OR CPRS GUI CHART")) {
      Object[] params = {startFrom, String.valueOf(direction)};
      ArrayList list = lCall("ORWLRR CHEMTEST", params);
      return getList(list);
    } else 
      throw getCreateContextException("OR CPRS GUI CHART");
  }   
  
  public synchronized LabTestTypesList getUsers(String startFrom, int direction) throws BrokerException {
    if (setContext("OR CPRS GUI CHART")) {
      Object[] params = {startFrom, String.valueOf(direction)};
      ArrayList list = lCall("ORWLRR USERS", params);
      return getList(list);
    } else 
      throw getCreateContextException("OR CPRS GUI CHART");
  }    
  
  public synchronized LabTestTypesList getTestGroupsForUser(String userDuz) throws BrokerException {
    if (setContext("OR CPRS GUI CHART")) {
      ArrayList list = lCall("ORWLRR TG", userDuz);
      return getList(list);
    } else 
      throw getCreateContextException("OR CPRS GUI CHART");
  }     
  
  public synchronized LabTestTypesList getATest(String testIen) throws BrokerException {
    if (setContext("OR CPRS GUI CHART")) {
      ArrayList list = lCall("ORWLRR ATESTS", testIen);
      return getList(list);
    } else 
      throw getCreateContextException("OR CPRS GUI CHART");
  }      
  
  public synchronized LabTestTypesList getATestGroup(long testGroupIen, long userDuz) throws BrokerException {
    if (setContext("OR CPRS GUI CHART")) {
      Object[] params = {testGroupIen, userDuz};
      ArrayList list = lCall("ORWLRR ATG", params);
      return getList(list);
    } else 
      throw getCreateContextException("OR CPRS GUI CHART");
  }     
  
  public synchronized String getTestInfo(String testIen) throws BrokerException {
    if (setContext("OR CPRS GUI CHART"))
      return sCall("ORWLRR INFO", testIen);
    else 
      throw getCreateContextException("OR CPRS GUI CHART");
  }   

  public synchronized LabReportList getLabReportLists() throws BrokerException {
    if (setContext("OR CPRS GUI CHART")) {
      ArrayList list = lCall("ORWRP LAB REPORT LISTS");
      String[] results = StringUtils.extractSection(list, "[LAB REPORT LIST]", true);
      LabReport[] labReports = new LabReport[results.length];
      for(int i = 0; i < results.length; i++) {
        labReports[i] = new LabReport();
        labReports[i].setIen(StringUtils.piece(results[i], 1));
        labReports[i].setName(StringUtils.piece(results[i], 2));
        labReports[i].setRpc(StringUtils.piece(results[i], 6));
      }
      LabReportList labReportList = new LabReportList();
      labReportList.setLabReports(labReports);
      return labReportList;
    } else 
      throw getCreateContextException("OR CPRS GUI CHART");
  }
  
  private synchronized LabTestTypesList getList(ArrayList list) {
    LabTestType[] labTestTypes = new LabTestType[list.size()];
    for(int i = 0; i < list.size(); i++) {
      String x = (String)list.get(i);      
      labTestTypes[i].setIen(StringUtils.piece(x, 1));
      labTestTypes[i].setName(StringUtils.piece(x, 2));
      if (returnRpcResult)
        labTestTypes[i].setRpcResult(x);
    }
    LabTestTypesList labTestTypesList = new LabTestTypesList();
    labTestTypesList.setLabTestTypes(labTestTypes);
    return labTestTypesList;    
  }  
  
}
