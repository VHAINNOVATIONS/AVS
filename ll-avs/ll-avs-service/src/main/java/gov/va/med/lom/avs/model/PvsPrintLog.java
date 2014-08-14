package gov.va.med.lom.avs.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="ckoPvsPrintLog")
public class PvsPrintLog extends BaseModel implements Serializable {

  private PvsClinic clinic;
  private String patientDfn;
  private boolean printed;
  
  @ManyToOne
  @JoinColumn(name="clinicId") 
  public PvsClinic getClinic() {
    return clinic;
  }
  public void setClinic(PvsClinic clinic) {
    this.clinic = clinic;
  }
  public String getPatientDfn() {
    return patientDfn;
  }
  public void setPatientDfn(String patientDfn) {
    this.patientDfn = patientDfn;
  }
  public boolean isPrinted() {
    return printed;
  }
  public void setPrinted(boolean printed) {
    this.printed = printed;
  }
  
}
