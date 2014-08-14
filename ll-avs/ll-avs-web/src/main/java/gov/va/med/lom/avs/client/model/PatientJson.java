package gov.va.med.lom.avs.client.model;

import java.io.Serializable;

public class PatientJson implements Serializable {

  private String dfn;
  private String name;
  private String ssn;
  
  public String getDfn() {
    return dfn;
  }

  public void setDfn(String dfn) {
    this.dfn = dfn;
  }

  public String getName() {
    return name;
  }
  
  public void setName(String name) {
    this.name = name;
  }

  public String getSsn() {
    return ssn;
  }
  
  public void setSsn(String ssn) {
    this.ssn = ssn;
  }

}
