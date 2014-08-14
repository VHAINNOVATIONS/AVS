package gov.va.med.lom.vistabroker.security;


public class VpidSecurityContext extends SecurityContext implements java.io.Serializable {
  
  // Constructors
  public VpidSecurityContext() {
    super(ISecurityContext.VPID_CONNECTION_SPEC);
  }
  
  public VpidSecurityContext(String division, String vpid) {
    this();
    setDivision(division);
    setUserId(vpid);
    setHost(null);
  }

}
