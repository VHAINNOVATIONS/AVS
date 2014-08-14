package gov.va.med.lom.javaBroker.rpc.patient;

import java.util.ArrayList;

import gov.va.med.lom.javaBroker.rpc.*;

public class CombatVetRpc extends AbstractRpc {
  
  // CONSTRUCTORS
  public CombatVetRpc() throws BrokerException {
    super();
  }
  
  public CombatVetRpc(RpcBroker rpcBroker) throws BrokerException {
    super(rpcBroker);
  }
  
  // RPC API
  public synchronized ArrayList getCombatVetInfo(String dfn) throws BrokerException {
    setDfn(dfn);
    ArrayList list = null;
    if (setContext("ALS CPRS COM OBJECT RPCS"))
      list = lCall("ALSI COMBAT VET", dfn);
    return list;
  }
  
}