package gov.va.med.lom.vistabroker.security;


public class CcowDivisionSecurityContext extends SecurityContext implements java.io.Serializable {
  
  // Constructors
  public CcowDivisionSecurityContext() {
    super(ISecurityContext.CCOW_DIVISION_CONNECTION_SPEC);
  }
  
  public CcowDivisionSecurityContext(String division, String ccowHandle) {
    this();
    setDivision(division);
    setUserId(ccowHandle);
    setHost(null);
  }

}
