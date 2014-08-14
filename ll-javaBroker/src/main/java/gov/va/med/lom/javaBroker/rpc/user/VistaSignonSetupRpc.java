package gov.va.med.lom.javaBroker.rpc.user;

import java.util.ArrayList;

import gov.va.med.lom.javaBroker.rpc.*;
import gov.va.med.lom.javaBroker.rpc.user.models.*;
import gov.va.med.lom.javaBroker.util.StringUtils;

public class VistaSignonSetupRpc extends AbstractRpc {
  
  // FIELDS
  private VistaSignonSetupResult vistaSignonSetupResult;
  
  // CONSTRUCTORS
  public VistaSignonSetupRpc() throws BrokerException {
    super();
  }
  
  public VistaSignonSetupRpc(RpcBroker rpcBroker) throws BrokerException {
    super(rpcBroker);
  }
  
  // PROPERTY ACCESSOR
  public VistaSignonSetupResult getVistaSignonSetupResult() {
    return vistaSignonSetupResult;
  }    
  
  // RPC API  
  public synchronized VistaSignonSetupResult doVistaSignonSetup() throws BrokerException {
    if (setContext("XUS SIGNON")) {
      vistaSignonSetupResult = new VistaSignonSetupResult(); 
      ArrayList list = lCall("XUS SIGNON SETUP");
      if (returnRpcResult) {
        StringBuffer sb = new StringBuffer();
        for(int i = 0; i < list.size(); i++)
          sb.append((String)list.get(i) + "\n");
        vistaSignonSetupResult.setRpcResult(sb.toString().trim());
      }    
      if (list.size() >= 6) {
        vistaSignonSetupResult.setServerAvailable(true);
        vistaSignonSetupResult.setServer((String)list.get(0));
        vistaSignonSetupResult.setVolume((String)list.get(1));
        vistaSignonSetupResult.setUci((String)list.get(2));
        vistaSignonSetupResult.setPort((String)list.get(3));
        vistaSignonSetupResult.setDeviceLocked(StringUtils.strToBool((String)list.get(4), "1"));
        vistaSignonSetupResult.setSignonRequired(StringUtils.strToBool((String)list.get(5), "0"));
        vistaSignonSetupResult.setIntroMessage(getIntroMessage());
      } else
        vistaSignonSetupResult.setServerAvailable(false);
      return vistaSignonSetupResult;
    } else
      throw getCreateContextException("XUS SIGNON");
    
  }
  
  public synchronized String getIntroMessage() throws BrokerException {
    if (setContext("XUS SIGNON"))
      return sCall("XUS INTRO MSG");
    else
      throw getCreateContextException("XUS SIGNON");
  }
  
}
