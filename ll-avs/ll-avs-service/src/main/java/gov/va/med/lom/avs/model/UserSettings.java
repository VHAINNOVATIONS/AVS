package gov.va.med.lom.avs.model;

import java.io.Serializable;
import javax.persistence.*;

/**
 * User Settings bean
 */
@Entity
@Table(name="ckoUserSettings")
public class UserSettings extends BaseModel implements Serializable {

  private static final long serialVersionUID = 0;

  private String facilityNo;
  private String userDuz;
  private String printerIen;
  private String printerIp;
  private String printerName;
  private boolean isDefaultPrinter;
  
  public String getFacilityNo() {
    return facilityNo;
  }
  public void setFacilityNo(String facilityNo) {
    this.facilityNo = facilityNo;
  }
  public String getUserDuz() {
    return userDuz;
  }
  public void setUserDuz(String userDuz) {
    this.userDuz = userDuz;
  }
  public String getPrinterIen() {
    return printerIen;
  }
  public void setPrinterIen(String printerIen) {
    this.printerIen = printerIen;
  }
  public String getPrinterIp() {
    return printerIp;
  }
  public void setPrinterIp(String printerIp) {
    this.printerIp = printerIp;
  }
  public String getPrinterName() {
    return printerName;
  }
  public void setPrinterName(String printerName) {
    this.printerName = printerName;
  }
  public boolean getIsDefaultPrinter() {
    return isDefaultPrinter;
  }
  public void setIsDefaultPrinter(boolean isDefaultPrinter) {
    this.isDefaultPrinter = isDefaultPrinter;
  }
  public static long getSerialversionuid() {
    return serialVersionUID;
  }
  
}