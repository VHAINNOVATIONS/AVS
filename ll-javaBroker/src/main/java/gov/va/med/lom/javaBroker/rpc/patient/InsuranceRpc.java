package gov.va.med.lom.javaBroker.rpc.patient;

import gov.va.med.lom.javaBroker.rpc.*;

public class InsuranceRpc extends AbstractRpc {
  
  // CONSTRUCTORS
  public InsuranceRpc() throws BrokerException {
    super();
  }
  
  public InsuranceRpc(RpcBroker rpcBroker) throws BrokerException {
    super(rpcBroker);
  }
  
  // RPC API
  public synchronized String getInsuranceInfo(String dfn) throws BrokerException {
    setDfn(dfn);
    String x = null;
    if (setContext("ALS CPRS COM OBJECT RPCS"))
      x = sCall("ALSI INS COM", dfn);
    return x;
  }
  
}