package gov.va.med.lom.javaBroker.rpc.lists;

import java.util.ArrayList;

import gov.va.med.lom.javaBroker.rpc.*;
import gov.va.med.lom.javaBroker.rpc.lists.models.*;
import gov.va.med.lom.javaBroker.util.StringUtils;

public class OrderItemsRpc extends AbstractRpc {
	
  // FIELDS
  private OrderItemsList orderItemsList;

  // CONSTRUCTORS
  public OrderItemsRpc() throws BrokerException {
    super();
  }
  
  public OrderItemsRpc(RpcBroker rpcBroker) throws BrokerException {
    super(rpcBroker);
  }
  
  // RPC API
  public synchronized OrderItemsList getSubSetOfOrderItems(String startFrom, int direction, String xRef) throws BrokerException {
    if (setContext("OR CPRS GUI CHART")) {
      String[] params = {startFrom, String.valueOf(direction), xRef};
      ArrayList list = lCall("ORWDX ORDITM", params);
      orderItemsList = getItems(list, false);
      return orderItemsList;
    } else 
      throw getCreateContextException("OR CPRS GUI CHART");
  }
  
  // Parses the result list from VistA and returns array of patient list item objects
  private OrderItemsList getItems(ArrayList list, boolean mixedCase) {
    orderItemsList = new OrderItemsList();
    if (returnRpcResult) {
      StringBuffer sb = new StringBuffer();
      for(int i = 0; i < list.size(); i++)
        sb.append((String)list.get(i) + "\n");
      orderItemsList.setRpcResult(sb.toString().trim());
    }     
    OrderItem[] orderItems = new OrderItem[list.size()]; 
    for(int i=0; i < list.size(); i++) {
      OrderItem orderItem = new OrderItem();
      String x = (String)list.get(i);
      if (returnRpcResult)
        orderItem.setRpcResult(x);
      String ien = StringUtils.piece(x, 1);
      String name;
      if (mixedCase)
        name = StringUtils.mixedCase(StringUtils.piece(x, 2));
      else
        name = StringUtils.piece(x, 2);
      orderItem.setIen(ien);
      orderItem.setName(name);  
      orderItems[i] = orderItem;
    }
    orderItemsList.setOrderItems(orderItems);
    return orderItemsList;    
  }  
}
