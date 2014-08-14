package gov.va.med.lom.vistabroker.security;

import java.io.Serializable;

public abstract class SecurityContext implements ISecurityContext, Serializable {

  // Instance Fields
  protected int type;
  protected String division;
  protected String userId;
  protected String host;
  protected String rpcContext;
  protected String rpcName;
  protected int socketTimeout;
  
  // Constructors
  public SecurityContext() {
    this.type = 0;  
    this.division = null;
    this.userId = null;
    this.host = null;
    this.rpcContext = null;
    this.rpcName = null;
    socketTimeout = 0;
  }
  
  protected SecurityContext(int type) {
    this.type = type;
  }
  
  // Accessor Methods
  public String getDivision() {
    return this.division;
  }
  
  public void setDivision(String division) {
    this.division = division;
  }
  
  public String getHost() {
    return this.host;
  }

  public void setHost(String host) {
    this.host = host;
  }

  public String getUserId() {
    return this.userId;
  }
  
  public void setUserId(String userId) {
    this.userId = userId;
  }
  
  public int getType() {
    return type;
  }

  public String getRpcContext() {
	return rpcContext;
  }

  public void setRpcContext(String rpcContext) {
	this.rpcContext = rpcContext;
  }

  public String getRpcName() {
	return rpcName;
  }

  public void setRpcName(String rpcName) {
	this.rpcName = rpcName;
  }

  public int getSocketTimeout() {
    return socketTimeout;
  }

  public void setSocketTimeout(int socketTimeout) {
    this.socketTimeout = socketTimeout;
  }
  
}
