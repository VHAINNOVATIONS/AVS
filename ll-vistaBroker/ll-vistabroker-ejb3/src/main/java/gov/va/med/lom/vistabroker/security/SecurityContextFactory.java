package gov.va.med.lom.vistabroker.security;

public class SecurityContextFactory {
  
  public static ISecurityContext createAppProxySecurityContext(String division, String appProxyName) {
    return new AppProxySecurityContext(division, appProxyName);
  }
  
  public static ISecurityContext createCcowDivisionSecurityContext(String division, String ccowHandle) {
    return new CcowDivisionSecurityContext(division, ccowHandle);
  }
  
  public static ISecurityContext createCcowHostSecurityContext(String host, String ccowHandle) {
    return new CcowHostSecurityContext(host, ccowHandle);
  }
  
  public static ISecurityContext createDuzSecurityContext(String division, String duz) {
    return new DuzSecurityContext(division, duz);
  }

  public static ISecurityContext createVpidSecurityContext(String division, String vpid) {
    return new VpidSecurityContext(division, vpid);
  }
  
}
