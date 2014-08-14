package gov.va.med.lom.javaBroker.rpc.patient;

import gov.va.med.lom.javaBroker.rpc.*;
import gov.va.med.lom.javaBroker.rpc.patient.models.*;
import gov.va.med.lom.javaBroker.util.StringUtils;

public class AddressRpc extends AbstractRpc {
  
  // FIELDS
  private Address address;

  // CONSTRUCTORS
  public AddressRpc() throws BrokerException {
    super();
  }
  
  public AddressRpc(RpcBroker rpcBroker) throws BrokerException {
    super(rpcBroker);
  }
  
  // RPC API
  public synchronized Address getAddress(String dfn) throws BrokerException {
    setDfn(dfn);
    address = new Address();    
    if (setContext("ALS CLINICAL RPC")) {
      String x = sCall("ALSI ADDRESS LOOKUP", dfn);
      if (returnRpcResult)
        address.setRpcResult(x);
      if (x.length() > 0) {
        address.setDfn(dfn);
        address.setStreet1(StringUtils.piece(x, 3));
        address.setStreet2(StringUtils.piece(x, 4));
        address.setStreet3(StringUtils.piece(x, 5));
        address.setCity(StringUtils.piece(x, 6));
        address.setStateNumber(StringUtils.toInt(StringUtils.piece(x, 7), 0));
        address.setState(StringUtils.piece(x, 8));
        address.setZipCode(StringUtils.piece(x, 9));
        address.setCountyNumber(StringUtils.toInt(StringUtils.piece(x, 10), 0));
        address.setCounty(StringUtils.piece(x, 11));
        address.setPhoneNumber(StringUtils.piece(x, 12));
        address.setFlagNumber(StringUtils.toInt(StringUtils.piece(x, 13), 0));
        address.setFlag(StringUtils.piece(x, 14));
        address.setEmail(StringUtils.piece(x, 15));
      }
    }
    return address;
  }
  
}