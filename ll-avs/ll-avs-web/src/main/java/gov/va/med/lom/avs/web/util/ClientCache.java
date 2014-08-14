package gov.va.med.lom.avs.web.util;

import java.util.Date;

public class ClientCache {

  private String stationNo;
  private String userDuz;
  private String printerIen;
  private String printerIp;
  private String printerName;
  private boolean printServiceDescriptions;
  private boolean isDefaultPrinter;
  private Date lastAccessed;
  
  public ClientCache() {}
  
  public ClientCache(String printerIen, String printerIp, String printerName, 
      boolean printServiceDescriptions, boolean isDefaultPrinter) {
    this.printerIen = printerIen;
    this.printerIp = printerIp;
    this.printerName = printerName;
    this.printServiceDescriptions = printServiceDescriptions;
    this.isDefaultPrinter = isDefaultPrinter;
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
  public boolean isPrintServiceDescriptions() {
    return printServiceDescriptions;
  }
  public void setPrintServiceDescriptions(boolean printServiceDescriptions) {
    this.printServiceDescriptions = printServiceDescriptions;
  }
  public String getUserDuz() {
    return userDuz;
  }
  public void setUserDuz(String userDuz) {
    this.userDuz = userDuz;
  }
  public Date getLastAccessed() {
    return lastAccessed;
  }
  public void setLastAccessed(Date lastAccessed) {
    this.lastAccessed = lastAccessed;
  }
  public String getStationNo() {
    return stationNo;
  }
  public void setStationNo(String stationNo) {
    this.stationNo = stationNo;
  }
  public boolean getIsDefaultPrinter() {
    return isDefaultPrinter;
  }
  public void setIsDefaultPrinter(boolean isDefaultPrinter) {
    this.isDefaultPrinter = isDefaultPrinter;
  }
  
}
