package gov.va.med.lom.vistabroker.security;


public class AppProxySecurityContext extends SecurityContext implements java.io.Serializable {
  
  // Constructors
  public AppProxySecurityContext() {
    super(ISecurityContext.APP_PROXY_CONNECTION_SPEC);
  }

  public AppProxySecurityContext(String division, String appProxyName) {
    this();
    setDivision(division);
    setUserId(appProxyName);
    setHost(null);
  }

}
