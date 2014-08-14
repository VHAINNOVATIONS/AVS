package gov.va.med.lom.javaBroker.rpc.lists;

import java.util.ArrayList;

import gov.va.med.lom.javaBroker.rpc.*;
import gov.va.med.lom.javaBroker.rpc.lists.models.*;
import gov.va.med.lom.javaBroker.util.StringUtils;

public class StatusUrgenciesRpc extends AbstractRpc {
  
  // FIELDS
  private StatusUrgencyList statusUrgencyList;  
	
  // CONSTRUCTORS
  public StatusUrgenciesRpc() throws BrokerException {
    super();
  }
  
  public StatusUrgenciesRpc(RpcBroker rpcBroker) throws BrokerException {
    super(rpcBroker);
  }
  
  // RPC API
  public synchronized StatusUrgencyList subSetOfStatus() throws BrokerException {
    if (setContext("OR CPRS GUI CHART")) {
      ArrayList list = lCall("ORQQCN STATUS");
      statusUrgencyList = new StatusUrgencyList();
      StatusUrgency[] statusUrgencies = new StatusUrgency[list.size()];
      for (int i = 0; i < list.size(); i++) {
        String x = (String)list.get(i);
        statusUrgencies[i].setIen(StringUtils.piece(x, 1));
        statusUrgencies[i].setText(StringUtils.piece(x, 2));
        if (returnRpcResult)
          statusUrgencies[i].setRpcResult(x);
      }
      statusUrgencyList.setStatusUrgencies(statusUrgencies);
      return statusUrgencyList;
    } else
      throw getCreateContextException("OR CPRS GUI CHART");
  }

  public synchronized StatusUrgencyList subSetOfUrgencies(String consultIen) throws BrokerException {
    if (setContext("OR CPRS GUI CHART")) {
      ArrayList list = lCall( "ORQQCN URGENCIES", consultIen);
      statusUrgencyList = new StatusUrgencyList();
      StatusUrgency[] statusUrgencies = new StatusUrgency[list.size()];
      for (int i = 0; i < list.size(); i++) {
        String x = (String)list.get(i);
        statusUrgencies[i].setIen(StringUtils.piece(x, 1));
        statusUrgencies[i].setText(StringUtils.piece(x, 2));
        if (returnRpcResult)
          statusUrgencies[i].setRpcResult(x);
      }
      statusUrgencyList.setStatusUrgencies(statusUrgencies);
      return statusUrgencyList;
    } else
      throw getCreateContextException("OR CPRS GUI CHART");
  }  
  
}
