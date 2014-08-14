package gov.va.med.lom.javaBroker.rpc.lists;

import java.util.ArrayList;

import gov.va.med.lom.javaBroker.rpc.*;
import gov.va.med.lom.javaBroker.rpc.lists.models.*;
import gov.va.med.lom.javaBroker.util.StringUtils;

public class DevicesRpc extends AbstractRpc {
	
  // CONSTRUCTORS
  public DevicesRpc() throws BrokerException {
    super();
  }
  
  public DevicesRpc(RpcBroker rpcBroker) throws BrokerException {
    super(rpcBroker);
  }
  
  // RPC API
  public synchronized Device[] subsetOfDevices(String start, int dir) throws BrokerException {
    if (setContext("OR CPRS GUI CHART")) {
      Object[] params = {start, String.valueOf(dir)};
      ArrayList list = lCall("ORWU DEVICE", params);
      Device[] devices = new Device[list.size()];
      for (int i = 0; i < devices.length; i++) {
        devices[i] = new Device();
        String x = (String)list.get(i);
        devices[i].setRpcResult(x);
        devices[i].setIen(StringUtils.piece(StringUtils.piece(x, 1), ';', 1));
        devices[i].setName(StringUtils.piece(StringUtils.piece(x, 1), ';', 2));
        devices[i].setDisplayName(StringUtils.piece(x, 2));
        devices[i].setLocation(StringUtils.piece(x, 3));
        devices[i].setRightMargin(StringUtils.toInt(StringUtils.piece(x, 4), 0));
        devices[i].setPageLength(StringUtils.toInt(StringUtils.piece(x, 5), 0));
      }
      return devices;      
    } else 
      throw getCreateContextException("OR CPRS GUI CHART");
  }
  
}
