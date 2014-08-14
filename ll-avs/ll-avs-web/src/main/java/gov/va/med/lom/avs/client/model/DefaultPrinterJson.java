package gov.va.med.lom.avs.client.model;

import gov.va.med.lom.avs.web.util.ClientCache;

public class DefaultPrinterJson {

  private String printerIen;
  private String printerIp;
  private String printerName;
  private boolean printServiceDescriptions;
  private boolean isDefault;
  
  public DefaultPrinterJson() {
    this.printerIen = "";
    this.printerIp = "";
    this.printerName = "";
    this.printServiceDescriptions = false;
    this.isDefault = false;
  }
  
  public DefaultPrinterJson(ClientCache clientParamsCacheData) {
    this.printerIen = clientParamsCacheData.getPrinterIen();
    this.printerIp = clientParamsCacheData.getPrinterIp();
    this.printerName = clientParamsCacheData.getPrinterName();
    this.printServiceDescriptions = clientParamsCacheData.isPrintServiceDescriptions();
    this.isDefault = clientParamsCacheData.getIsDefaultPrinter();
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
  public String getPrinterIen() {
    return printerIen;
  }
  public void setPrinterIen(String printerIen) {
    this.printerIen = printerIen;
  }

  public boolean getIsDefault() {
    return isDefault;
  }

  public void setIsDefault(boolean isDefault) {
    this.isDefault = isDefault;
  }
  
}
