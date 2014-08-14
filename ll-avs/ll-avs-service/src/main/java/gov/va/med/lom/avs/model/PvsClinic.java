package gov.va.med.lom.avs.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="ckoPvsClinic")
public class PvsClinic extends BaseModel implements Serializable {

  private String stationNo;
  private String clinicIen;
  private String clinicName;
  private String printerIen;
  private String printerName;
  private String printerIp;
  private boolean inpatient;
  
  public String getStationNo() {
    return stationNo;
  }
  public void setStationNo(String stationNo) {
    this.stationNo = stationNo;
  }
  public String getClinicIen() {
    return clinicIen;
  }
  public void setClinicIen(String clinicIen) {
    this.clinicIen = clinicIen;
  }
  public String getClinicName() {
    return clinicName;
  }
  public void setClinicName(String clinicName) {
    this.clinicName = clinicName;
  }
  public String getPrinterName() {
    return printerName;
  }
  public void setPrinterName(String printerName) {
    this.printerName = printerName;
  }
  public String getPrinterIp() {
    return printerIp;
  }
  public void setPrinterIp(String printerIp) {
    this.printerIp = printerIp;
  }
  public boolean isInpatient() {
    return inpatient;
  }
  public void setInpatient(boolean inpatient) {
    this.inpatient = inpatient;
  }
  public String getPrinterIen() {
    return printerIen;
  }
  public void setPrinterIen(String printerIen) {
    this.printerIen = printerIen;
  }
  
}
