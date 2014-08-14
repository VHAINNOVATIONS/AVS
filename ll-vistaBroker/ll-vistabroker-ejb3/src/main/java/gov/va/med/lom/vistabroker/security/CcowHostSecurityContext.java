package gov.va.med.lom.vistabroker.security;


public class CcowHostSecurityContext extends SecurityContext implements java.io.Serializable {
  
  // Constructors
  public CcowHostSecurityContext() {
    super(ISecurityContext.CCOW_HOST_CONNECTION_SPEC);
  }
  
  public CcowHostSecurityContext(String host, String ccowHandle) {
    this();
    setDivision(null);
    setUserId(ccowHandle);
    setHost(host);
  }

}
