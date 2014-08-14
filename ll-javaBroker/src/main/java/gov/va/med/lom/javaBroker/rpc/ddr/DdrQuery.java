package gov.va.med.lom.javaBroker.rpc.ddr;

import gov.va.med.lom.javaBroker.rpc.RpcBroker;
import gov.va.med.lom.javaBroker.rpc.Params;

public class DdrQuery {

  public static final String CAPRI_CONTEXT = "DVBA CAPRI GUI";
  public static final String CPRS_CONTEXT = "OR CPRS GUI CHART";
  
  protected RpcBroker rpcBroker;
  
  public DdrQuery(RpcBroker rpcBroker) {
    this.rpcBroker = rpcBroker;
  }
  
  public String execute(String api, Params params) throws Exception {
    String currentContext = rpcBroker.getCurrentContext();
    if (!currentContext.equals(CAPRI_CONTEXT)) {
      try {
        if (!rpcBroker.createContext(CAPRI_CONTEXT)) {
          throw new Exception("Unable to set CAPRI context.");
        }
      } catch(Exception e) {
        // conferContext
      }
    }
    String response = rpcBroker.call(api, params);
    if (!currentContext.equals(CAPRI_CONTEXT)) {
      if (!rpcBroker.createContext(currentContext)) {
        throw new Exception("Unable to set " + currentContext + " context.");
      }
    }
    return response;
  }
  
  // TO-DO: conferContext method
  
}
