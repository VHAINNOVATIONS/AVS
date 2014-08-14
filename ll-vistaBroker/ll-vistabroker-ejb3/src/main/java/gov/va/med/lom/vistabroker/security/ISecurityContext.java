package gov.va.med.lom.vistabroker.security;

public interface ISecurityContext {
  
  // Connection Context Types
  public static final int APP_PROXY_CONNECTION_SPEC = 1;
  public static final int CCOW_HOST_CONNECTION_SPEC = 2;
  public static final int CCOW_DIVISION_CONNECTION_SPEC = 3;
  public static final int DUZ_CONNECTION_SPEC = 4;
  public static final int VPID_CONNECTION_SPEC = 5;
  
  // Accessor Methods
  public abstract String getDivision();
  public abstract void setDivision(String division);
  public abstract String getHost();
  public abstract void setHost(String host);
  public abstract String getUserId();
  public abstract void setUserId(String userId);
  public abstract int getType();
  public abstract String getRpcContext();
  public abstract void setRpcContext(String rpcContext);
  public abstract String getRpcName();
  public abstract void setRpcName(String rpcName);
  public abstract int getSocketTimeout();
  public abstract void setSocketTimeout(int timeout);
  
}
