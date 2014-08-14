package gov.va.med.lom.avs.report.dashboard;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.opensymphony.xwork2.Preparable;

import gov.va.med.authentication.kernel.LoginUserInfoVO;

import gov.va.med.lom.reports.model.GridColumn;
import gov.va.med.lom.reports.model.SortInfo;

import gov.va.med.lom.avs.model.EncounterCacheMongo;
import gov.va.med.lom.avs.model.Provider;
import gov.va.med.lom.avs.model.Clinic;
import gov.va.med.lom.avs.model.Patient;
import gov.va.med.lom.javaUtils.misc.DateUtils;
import gov.va.med.lom.javaUtils.misc.StringUtils;
import gov.va.med.lom.login.struts.session.SessionUtil;
import gov.va.med.lom.avs.report.model.AvsEncounterJson;
import gov.va.med.lom.avs.service.ServiceFactory;
import gov.va.med.lom.avs.service.SheetService;
import gov.va.med.lom.avs.service.SettingsService;

import gov.va.med.lom.reports.struts.action.BaseDashboardReportsAction;
import gov.va.med.lom.vistabroker.util.FMDateUtils;


public class EncounterReportAction extends BaseDashboardReportsAction implements ServletRequestAware, Preparable {

private static final Log log = LogFactory.getLog(EncounterReportAction.class);
  
  // client request params
  private String stationNo;
  private String startDate;
  private String endDate;
  
  // services
  private SheetService sheetService;
  private SettingsService settingsService;
  
  // data fields
  private LoginUserInfoVO loginUserInfo;
  
  public void prepare() throws Exception {    
    
    try {
      super.prepare();
      this.sheetService = ServiceFactory.getSheetService();
      this.settingsService = ServiceFactory.getSettingsService();
      loginUserInfo = SessionUtil.getLoginUserInfo(request);
      if (stationNo == null) {
        stationNo = loginUserInfo.getLoginStationNumber();
      }
    } catch(Exception e){
      log.error("error creating services", e);
    }    
    
  }
  
	public String encounterReport() {

	  Date now = new Date();
	  Date start = null;
	  Date end = null;
	  try {
	    start = DateUtils.convertAnsiDateStr(startDate);
	  } catch(Exception e) {}
	  if (start == null) {
	    start = getDateFloor(now).getTime();
	  }
    try {
      end = DateUtils.convertAnsiDateStr(endDate);
    } catch(Exception e) {}
    if (end == null) {
      end = now;
    }
    end = getDateCeil(end).getTime();    
    
    try {
      String startStr = DateUtils.toEnglishDate(start);
      String endStr = DateUtils.toEnglishDate(end);
      setTitle(getTitle() + " - " + startStr + " to " + endStr);
    } catch(Exception e) {}
    
    List<EncounterCacheMongo> encList = (List<EncounterCacheMongo>)
        this.sheetService.getEncounterCacheMongoByDates(stationNo, start, end).getCollection();
    List<AvsEncounterJson> encjList = mapJsonObjects(encList);
    
    setTotalCount(encjList.size());    
    setRoot(encjList);
    
    // hidden columns
    GridColumn gridColumn = new GridColumn();
    gridColumn.setDataIndex("id");
    gridColumn.setHidden(true);
    addGridColumn(gridColumn);
    
    // grid columns
    addGridColumn("Provider(s)", "provider", true, false, 120, GridColumn.LEFT);
    addGridColumn("Title", "title", true, false, 120, GridColumn.LEFT);
    addGridColumn("Location", "location", true, false, 120, GridColumn.LEFT);
    addGridColumn("Visit Date/Time", "encounterDatetime", true, false, 110, GridColumn.LEFT);
    addGridColumn("Font Size", "fontSize", true, false, 60, GridColumn.LEFT);
    addGridColumn("Lab Results", "labDaysBack", true, false, 70, GridColumn.LEFT);
    addGridColumn("Instructions", "hasInstructions", true, false, 70, GridColumn.CENTER);
    addGridColumn("Custom Content", "hasCustomContent", true, false, 90, GridColumn.CENTER);
    addGridColumn("Remote VA Meds", "hasRemoteVaMeds", true, false, 95, GridColumn.CENTER);
    addGridColumn("Remote Non-VA Meds", "hasRemoteNonVaMeds", true, false, 115, GridColumn.CENTER);
    addGridColumn("Clinical Services", "hasClinicalServices", true, false, 85, GridColumn.CENTER);
    addGridColumn("Charts", "hasCharts", true, false, 50, GridColumn.CENTER);
    addGridColumn("Locked", "locked", true, false, 50, GridColumn.CENTER);
    addGridColumn("Printed", "printed", true, false, 50, GridColumn.CENTER);
    
    // aggregates
    HashMap<String, String> providers = new HashMap<String, String>();
    HashMap<String, String> locations = new HashMap<String, String>(); 
    for(AvsEncounterJson enc : encjList) {
      String[] providerDuzs = StringUtils.pieceList(enc.getProvider(), ',');
      for (int i = 0; i < providerDuzs.length; i++) {
        providers.put(providerDuzs[i], providerDuzs[i]);
      }
      locations.put(enc.getLocation(), enc.getLocation());
    }    
    addAggregate("totalEncounters", "# Encounters", String.valueOf(encjList.size()));
    addAggregate("totalProviders", "# Providers", String.valueOf(providers.size()));
    addAggregate("totalLocations", "# Locations", String.valueOf(locations.size()));
    
    // sort/metadata
    addSortInfo("encounterDatetime", SortInfo.DESC);
    setAutoExpandColumn(1);
    getGridMetaData().setIdProperty("id");
    getGridMetaData().setTitle(endDate);
    /*
    if (format != null) {
      if (format.equals("pdf")) {
        return sendPdf(new CpDevicePdfReport("", loginUserInfo.getUserNameDisplay(), gridData).report());
      } else if (format.equals("csv")) {
        return sendCsv(new CpDeviceCsvReport("", gridData).report(), "report.csv");
      }
    } 
    */   
    
    return sendJsonNoRoot();    
    
	}
	private List<AvsEncounterJson> mapJsonObjects(List<EncounterCacheMongo> encounterCacheList) {
	  List<AvsEncounterJson> encjList = new ArrayList<AvsEncounterJson>();
	  /*
	  for (EncounterCacheMongo encCache : encounterCacheList) {
	    AvsEncounterJson encJson = new AvsEncounterJson();
	    encJson.setId(encCache.getId());
	    String[] providerDuzs = StringUtils.pieceList(encCache.getProviderDuz(), ',');
	    StringBuffer namesSB = new StringBuffer();
	    StringBuffer titlesSB = new StringBuffer();
	    if (encCache.getProviderName() != null) {
	      String[] providerNames = StringUtils.pieceList(encCache.getProviderName(), '^');
	      for (int i = 0; i < providerNames.length; i++) {
	        if ((providerNames.length > 1) && (i == providerNames.length-1)) {
            namesSB.append("<br/>");
          }
	        namesSB.append(providerNames[i]);
	      }
	    }
      if (encCache.getProviderTitle() != null) {
        String[] providerTitles = StringUtils.pieceList(encCache.getProviderTitle(), '^');
        for (int i = 0; i < providerTitles.length; i++) {
          if ((providerTitles.length > 1) && (i == providerTitles.length-1)) {
            titlesSB.append("<br/>");
          }
          titlesSB.append(providerTitles[i]);
        }	      
      }
	    if (namesSB.toString().isEmpty() || titlesSB.toString().isEmpty()) {
	      namesSB.delete(0, namesSB.length());
	      titlesSB.delete(0, titlesSB.length());
  	    for (int i = 0; i < providerDuzs.length; i++) {
  	      Provider provider = this.settingsService.getProvider(stationNo, providerDuzs[i]).getPayload();
  	      if (provider != null) {
  	        if ((providerDuzs.length > 1) && (i == providerDuzs.length-1)) {
  	          namesSB.append("<br/>");
  	          titlesSB.append("<br/>");
  	        }
  	        namesSB.append(provider.getName());
  	        titlesSB.append(provider.getTitle());
  	      }
  	    }
	    }
	    encJson.setProvider(namesSB.toString());
	    encJson.setTitle(titlesSB.toString());
	    if ((encCache.getLocationName() != null) && !encCache.getLocationName().isEmpty()) {
	      encJson.setLocation(encCache.getLocationName());
	    } else {
  	    Clinic clinic = this.settingsService.getClinic(stationNo, loginUserInfo.getUserDuz(), 
  	        encCache.getLocationIen()).getPayload();
  	    if (clinic != null) {
  	      encJson.setLocation(clinic.getName());
  	    } else {
  	      encJson.setLocation("");
  	    }
	    }
	    encJson.setEncounterDatetime(DateUtils.formatDate(FMDateUtils.fmDateTimeToDate(encCache.getEncounterDatetime()), "MM/dd/yyyy@HH:mm"));
	    if ((encCache.getFontClass() == null) || encCache.getFontClass().equals("normalFont")) {
	      encJson.setFontSize("Normal");
	    } else if (encCache.getFontClass().equals("largeFont")) {
	      encJson.setFontSize("Large");
	    } else if (encCache.getFontClass().equals("veryLargeFont")) {
        encJson.setFontSize("Very Large");	      
	    }
	    encJson.setHasInstructions(StringUtils.boolToStr((encCache.getInstructions() != null) && 
	        !encCache.getInstructions().isEmpty() && !encCache.getInstructions().equals("None"), "Yes", "No"));
	    encJson.setHasCustomContent(StringUtils.boolToStr((encCache.getCustomContent() != null) && 
	        !encCache.getCustomContent().isEmpty(), "Yes", "No"));
	    if ((encCache.getLabDateRange() == null) || encCache.getLabDateRange().equals("-1")) {
	      encJson.setLabDaysBack("None");
	    } else {
	      encJson.setLabDaysBack(encCache.getLabDateRange() + " Days");
	    }
	    encJson.setHasRemoteVaMeds(StringUtils.boolToStr((encCache.getRemoteVaMedications() != null) && 
	        !encCache.getRemoteVaMedications().isEmpty(), "Yes", "No"));
	    encJson.setHasRemoteNonVaMeds(StringUtils.boolToStr((encCache.getRemoteNonVaMedications() != null) && 
	        !encCache.getRemoteNonVaMedications().isEmpty(), "Yes", "No"));
	    encJson.setHasClinicalServices(StringUtils.boolToStr((encCache.getSelectedServiceDescriptions() != null) && 
	        !encCache.getSelectedServiceDescriptions().isEmpty(), "Yes", "No"));
	    encJson.setHasCharts(StringUtils.boolToStr((encCache.getCharts() != null) && 
	        !encCache.getCharts().isEmpty(), "Yes", "No"));
	    encJson.setLocked(StringUtils.boolToStr(encCache.isLocked(), "Yes", "No"));
	    encJson.setPrinted(StringUtils.boolToStr(encCache.isPrinted(), "Yes", "No"));
	    encjList.add(encJson);
	  }
	  */
	  return encjList;
	}
	
  private GregorianCalendar getDateFloor(Date date) {

    GregorianCalendar floor = new GregorianCalendar();
    floor.setTime(date);
    floor.set(Calendar.HOUR_OF_DAY, 0);
    floor.set(Calendar.MINUTE, 0);
    floor.set(Calendar.SECOND, 0);
    
    return floor;
  }

  private GregorianCalendar getDateCeil(Date date) {

    GregorianCalendar ceil = new GregorianCalendar();
    ceil.setTime(date);
    ceil.set(Calendar.HOUR_OF_DAY, 23);
    ceil.set(Calendar.MINUTE, 59);
    ceil.set(Calendar.SECOND, 59);
    
    return ceil;
  }  	

  public String getStationNo() {
    return stationNo;
  }

  public void setStationNo(String stationNo) {
    this.stationNo = stationNo;
  }

  public String getStartDate() {
    return startDate;
  }

  public void setStartDate(String startDate) {
    this.startDate = startDate;
  }

  public String getEndDate() {
    return endDate;
  }

  public void setEndDate(String endDate) {
    this.endDate = endDate;
  }
	
}
