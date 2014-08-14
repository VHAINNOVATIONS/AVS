package gov.va.med.lom.vistabroker.security;


public class DuzSecurityContext extends SecurityContext implements java.io.Serializable {
  
  // Constructors
  public DuzSecurityContext() {
    super(ISecurityContext.DUZ_CONNECTION_SPEC);
  }
  
  public DuzSecurityContext(String division, String duz) {
    this();
    setDivision(division);
    setUserId(duz);
    setHost(null);
  }

}
