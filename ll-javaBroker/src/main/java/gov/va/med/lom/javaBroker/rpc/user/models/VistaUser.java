package gov.va.med.lom.javaBroker.rpc.user.models;

import gov.va.med.lom.javaBroker.rpc.BaseBean;

import java.io.Serializable;

public class VistaUser extends BaseBean implements Serializable {
  
  private String name;
  private String standardName;
  private String stationIen;
  private String station;
  private String stationNumber;
  private String userClass;
  private boolean canSignOrders;
  private int orderRole;
  private boolean isProvider;
  private boolean noOrdering;
  private int dTime;
  private int countDown;
  private boolean enableVerify;
  private boolean notifyAppsWM;
  private int ptMsgHang;
  private String domain;
  private String service;
  private int autoSave;
  private int initialTab;
  private boolean useLastTab;
  private boolean webAccess;
  private boolean disableHold;
  private String isRPL;
  private String rplList;
  private boolean hasCorTabs;
  private boolean hasRptTab;
  private boolean isReportsOnly;
  private String title;
  private String serviceSection;
  private int language;
  
  public VistaUser() {
    this.name = null;
    this.standardName = null;
    this.stationIen = null;
    this.station = null;
    this.stationNumber = null;
    this.userClass = null;
    this.canSignOrders = false;
    this.orderRole = 0;
    this.isProvider = false;
    this.noOrdering = false;
    this.dTime = 0;
    this.countDown = 0;
    this.enableVerify = false;
    this.notifyAppsWM = false;
    this.ptMsgHang = 0;
    this.domain = null;
    this.service = null;
    this.autoSave = 0;
    this.initialTab = 0;
    this.useLastTab = false;
    this.webAccess = false;
    this.disableHold = false;
    this.isRPL = null;
    this.rplList = null;
    this.hasCorTabs = false;
    this.hasRptTab = false;
    this.isReportsOnly = false;
    this.title = null;
    this.serviceSection = null;
    this.language = 0;    
  }
  
  public int getAutoSave() {
    return autoSave;
  }
  
  public void setAutoSave(int autoSave) {
    this.autoSave = autoSave;
  }
  
  public boolean getCanSignOrders() {
    return canSignOrders;
  }
  
  public void setCanSignOrders(boolean canSignOrders) {
    this.canSignOrders = canSignOrders;
  }
  
  public int getCountDown() {
    return countDown;
  }
  
  public void setCountDown(int countDown) {
    this.countDown = countDown;
  }
  
  public boolean getDisableHold() {
    return disableHold;
  }
  
  public void setDisableHold(boolean disableHold) {
    this.disableHold = disableHold;
  }
  
  public String getStation() {
    return station;
  }
  
  public void setStation(String station) {
    this.station = station;
  }
  
  public String getStationIen() {
    return stationIen;
  }
  
  public void setStationIen(String stationIen) {
    this.stationIen = stationIen;
  }   
  
  public String getStationNumber() {
    return stationNumber;
  }
  
  public void setStationNumber(String stationNumber) {
    this.stationNumber = stationNumber;
  }  
  
  public String getDomain() {
    return domain;
  }
  
  public void setDomain(String domain) {
    this.domain = domain;
  }
  
  public int getDTime() {
    return dTime;
  }
  
  public void setDTime(int time) {
    dTime = time;
  }
  
  public boolean getEnableVerify() {
    return enableVerify;
  }
  
  public void setEnableVerify(boolean enableVerify) {
    this.enableVerify = enableVerify;
  }
  
  public boolean getHasCorTabs() {
    return hasCorTabs;
  }
  
  public void setHasCorTabs(boolean hasCorTabs) {
    this.hasCorTabs = hasCorTabs;
  }
  
  public boolean getHasRptTab() {
    return hasRptTab;
  }
  
  public void setHasRptTab(boolean hasRptTab) {
    this.hasRptTab = hasRptTab;
  }
  
  public int getInitialTab() {
    return initialTab;
  }
  
  public void setInitialTab(int initialTab) {
    this.initialTab = initialTab;
  }
  
  public boolean getIsProvider() {
    return isProvider;
  }
  
  public void setProvider(boolean isProvider) {
    this.isProvider = isProvider;
  }
  
  public boolean getIsReportsOnly() {
    return isReportsOnly;
  }
  
  public void setReportsOnly(boolean isReportsOnly) {
    this.isReportsOnly = isReportsOnly;
  }
  
  public String getIsRPL() {
    return isRPL;
  }

  public void setIsRPL(String isRPL) {
    this.isRPL = isRPL;
  }
  
  public int getLanguage() {
    return language;
  }
  
  public void setLanguage(int language) {
    this.language = language;
  }
  
  public String getName() {
    return name;
  }
  
  public void setName(String name) {
    this.name = name;
  }
  
  public boolean getNoOrdering() {
    return noOrdering;
  }
  
  public void setNoOrdering(boolean noOrdering) {
    this.noOrdering = noOrdering;
  }
  
  public boolean getNotifyAppsWM() {
    return notifyAppsWM;
  }
  
  public void setNotifyAppsWM(boolean notifyAppsWM) {
    this.notifyAppsWM = notifyAppsWM;
  }
  
  public int getOrderRole() {
    return orderRole;
  }
  
  public void setOrderRole(int orderRole) {
    this.orderRole = orderRole;
  }
  
  public int getPtMsgHang() {
    return ptMsgHang;
  }
  
  public void setPtMsgHang(int ptMsgHang) {
    this.ptMsgHang = ptMsgHang;
  }
  
  public String getRplList() {
    return rplList;
  }
  
  public void setRplList(String rplList) {
    this.rplList = rplList;
  }
  
  public String getService() {
    return service;
  }
  
  public void setService(String service) {
    this.service = service;
  }
  
  public String getServiceSection() {
    return serviceSection;
  }
  
  public void setServiceSection(String serviceSection) {
    this.serviceSection = serviceSection;
  }
  
  public String getStandardName() {
    return standardName;
  }
  
  public void setStandardName(String standardName) {
    this.standardName = standardName;
  }
  
  public String getTitle() {
    return title;
  }
  
  public void setTitle(String title) {
    this.title = title;
  }
  
  public boolean getUseLastTab() {
    return useLastTab;
  }
  
  public void setUseLastTab(boolean useLastTab) {
    this.useLastTab = useLastTab;
  }
  
  public String getUserClass() {
    return userClass;
  }
  
  public void setUserClass(String userClass) {
    this.userClass = userClass;
  }
  
  public boolean getWebAccess() {
    return webAccess;
  }
  
  public void setWebAccess(boolean webAccess) {
    this.webAccess = webAccess;
  }
  
}
