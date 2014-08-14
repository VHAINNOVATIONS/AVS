package gov.va.med.lom.javaBroker.rpc;

import gov.va.med.lom.javaBroker.util.StringUtils;

public class MiscRPCs {

    public static final String CAPRI_CONTEXT = "DVBA CAPRI GUI";
  
    // use %DT to validate and convert a string to Fileman format (accepts T, T-1, NOW, etc.)
    public synchronized static double strToFMDateTime(RpcBroker rpcBroker, String str) throws BrokerException {
      String[] params = {str};
      rpcBroker.createContext("OR CPRS GUI CHART");
      return StringUtils.toDouble(rpcBroker.call("ORWU DT", params), 0.0);
    }
    
    // use %DT to validate & convert a string to Fileman format, accepts %DT flags
    public synchronized static double validDateTimeStr(RpcBroker rpcBroker, String str, String flags) throws BrokerException {
      rpcBroker.createContext("OR CPRS GUI CHART");
      String[] params = {str, flags};
      return StringUtils.toDouble(rpcBroker.call("ORWU VALDT", params), 0.0);
    }    
    
    public synchronized static double fmNow(RpcBroker rpcBroker) throws BrokerException {
      return strToFMDateTime(rpcBroker, "NOW");
    }
    
    public synchronized static int fmToday(RpcBroker rpcBroker) throws BrokerException {
      return (int)fmNow(rpcBroker);
    }
    
    // returns the external name of the IEN within a file
    public synchronized static String externalName(RpcBroker rpcBroker, String ien, double fileNumber) throws BrokerException {
      rpcBroker.createContext("OR CPRS GUI CHART");
      String[] params = {ien, String.valueOf(fileNumber)};
      return rpcBroker.call("ORWU EXTNAME", params);
    }
  
    public synchronized static String getVariableValue(RpcBroker rpcBroker, String arg) throws BrokerException {
      rpcBroker.createContext(CAPRI_CONTEXT);
      Params p = new Params();
      p.addParameter(arg, Params.REFERENCE);
      return rpcBroker.call("XWB GET VARIABLE VALUE", p);      
    }
    
}