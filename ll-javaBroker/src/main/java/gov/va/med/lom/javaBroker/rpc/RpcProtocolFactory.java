package gov.va.med.lom.javaBroker.rpc;

import java.lang.reflect.Constructor;
import java.util.HashMap;

public class RpcProtocolFactory {

  // Broker protocol versions
  public static final int RPC_BROKER_PROTOCOL_V1    = 1;
  public static final int RPC_BROKER_PROTOCOL_V2    = 2;
  // Default protocol version
  public static final int DEFAULT_RPC_BROKER_VERSION = RPC_BROKER_PROTOCOL_V2;

  private static HashMap protocolMap = new HashMap();
  
  static {
    protocolMap.put(new Integer(RPC_BROKER_PROTOCOL_V1), RpcProtocolV1.class);
    protocolMap.put(new Integer(RPC_BROKER_PROTOCOL_V2), RpcProtocolV2.class);
  }

  public static IRpcProtocol getProtocol(int protocol) throws Exception {
    Class cls = (Class)protocolMap.get(new Integer(protocol));
    if (cls != null)
      return createRpcProtocol(cls, protocol);
    else
      return getDefaultProtocol();
  }
  
  public static IRpcProtocol getDefaultProtocol() throws Exception {
    Class cls = (Class)protocolMap.get(new Integer(DEFAULT_RPC_BROKER_VERSION));
    return createRpcProtocol(cls, DEFAULT_RPC_BROKER_VERSION);
  }
  
  private static IRpcProtocol createRpcProtocol(Class cls, int protocol) throws Exception {
    IRpcProtocol rpcProtocol = null;
    Constructor ct = cls.getConstructor(new Class[] {Integer.TYPE});
    rpcProtocol = (IRpcProtocol)ct.newInstance(new Object[] {new Integer(protocol)});
    return rpcProtocol;
  }
  
}
