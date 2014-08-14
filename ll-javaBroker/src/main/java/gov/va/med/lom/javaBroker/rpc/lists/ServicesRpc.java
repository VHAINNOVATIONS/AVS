package gov.va.med.lom.javaBroker.rpc.lists;

import java.util.ArrayList;

import gov.va.med.lom.javaBroker.rpc.*;
import gov.va.med.lom.javaBroker.rpc.lists.models.*;
import gov.va.med.lom.javaBroker.util.StringUtils;

public class ServicesRpc extends AbstractRpc {
	
  // FIELDS
  private ServicesList servicesList;

  // CONSTRUCTORS
  public ServicesRpc() throws BrokerException {
    super();
  }
  
  public ServicesRpc(RpcBroker rpcBroker) throws BrokerException {
    super(rpcBroker);
  }
  
  // RPC API
  
  /**
   * Purpose:  0=display all services, 1=forward or order from possible services
   */
  public synchronized ServicesList getServiceList(int purpose) throws BrokerException {
    if (setContext("OR CPRS GUI CHART")) {
      ArrayList list = lCall("ORQQCN SVCTREE", String.valueOf(purpose));
      servicesList = getItems(list, true);
      return servicesList;
    } else 
      throw getCreateContextException("OR CPRS GUI CHART");
  }
  
  public synchronized ServicesList getServiceListWithSynonyms(int purpose) throws BrokerException {
    if (setContext("OR CPRS GUI CHART")) {    
      Object[] params = {"1", String.valueOf(purpose), "true"};
      ArrayList list = lCall("ORQQCN SVC W/SYNONYMS", params);
      servicesList = getItems(list, true);
      return servicesList;
    } else 
      throw getCreateContextException("OR CPRS GUI CHART");
  }  
  
  public synchronized ServicesList getServiceListWithSynonymsForConsult(int purpose, long consultIen) throws BrokerException {
    if (setContext("OR CPRS GUI CHART")) {
      Object[] params = {"1", String.valueOf(purpose), "true", consultIen};
      ArrayList list = lCall("ORQQCN SVC W/SYNONYMS", params);
      servicesList = getItems(list, true);
      return servicesList;
    } else 
      throw getCreateContextException("OR CPRS GUI CHART");
  }    
  
  //lists all hospital locations
  public synchronized ServicesList getSubSetOfServices(String startFrom, int direction) throws BrokerException {
    if (setContext("OR CPRS GUI CHART")) {
      String[] params = {startFrom, String.valueOf(direction)};
      ArrayList list = lCall("ORQQCN SVCLIST", params);
      servicesList = getItems(list, false);
      return servicesList;
    } else 
      throw getCreateContextException("OR CPRS GUI CHART");
  }
  
  // Parses the result list from VistA and returns array of patient list item objects
  private ServicesList getItems(ArrayList list, boolean mixedCase) {
    servicesList = new ServicesList();
    if (returnRpcResult) {
      StringBuffer sb = new StringBuffer();
      for(int i = 0; i < list.size(); i++)
        sb.append((String)list.get(i) + "\n");
      servicesList.setRpcResult(sb.toString().trim());
    }     
    Service[] services = new Service[list.size()]; 
    for(int i=0; i < list.size(); i++) {
      Service service = new Service();
      String x = (String)list.get(i);
      if (returnRpcResult)
        service.setRpcResult(x);
      String ien = StringUtils.piece(x, 1);
      String name;
      if (mixedCase)
        name = StringUtils.mixedCase(StringUtils.piece(x, 2));
      else
        name = StringUtils.piece(x, 2);
      String synonym;
      if (mixedCase)
        synonym = StringUtils.mixedCase(StringUtils.piece(x, 3));
      else
        synonym = StringUtils.piece(x, 3);      
      service.setIen(ien);
      service.setName(name);  
      service.setSynonym(synonym);
      services[i] = service;
    }
    servicesList.setServices(services);
    return servicesList;    
  }  
}
