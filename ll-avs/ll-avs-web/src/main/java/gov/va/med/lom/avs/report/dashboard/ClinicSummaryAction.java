package gov.va.med.lom.avs.report.dashboard;

import java.util.List;
import java.util.ArrayList;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.opensymphony.xwork2.Preparable;

import gov.va.med.authentication.kernel.LoginUserInfoVO;

import gov.va.med.lom.reports.model.GridColumn;
import gov.va.med.lom.reports.model.SortInfo;

import gov.va.med.lom.login.struts.session.SessionUtil;

import gov.va.med.lom.reports.struts.action.BaseDashboardReportsAction;


public class ClinicSummaryAction extends BaseDashboardReportsAction implements ServletRequestAware, Preparable {

private static final Log log = LogFactory.getLog(ClinicSummaryAction.class);
  
  private String stationNo;
  private LoginUserInfoVO loginUserInfo;
  
  public void prepare() throws Exception {    
    
    try {
      super.prepare();
      loginUserInfo = SessionUtil.getLoginUserInfo(request);
      if (stationNo == null) {
        stationNo = loginUserInfo.getLoginStationNumber();
      }
    } catch(Exception e){
      log.error("error creating services", e);
    }    
    
  }
  
	public String cpDevicesReport() {

    //List<CpDevice> cpdList = (List<CpDevice>)clinicalProceduresService.findCpDevicesdByStation(stationNo).getCollection();
    //List<CpDeviceJson> cpdjList = mapJsonObjects(cpdList);
    
    //setTotalCount(cpdjList.size());    
    //setRoot(cpdjList);
    
    // hidden columns
    GridColumn gridColumn = new GridColumn();
    gridColumn.setDataIndex("id");
    gridColumn.setHidden(true);
    addGridColumn(gridColumn);
    
    // grid columns
    addGridColumn("Station", "stationNo", true, false, 50, GridColumn.LEFT);
    addGridColumn("Service", "category", true, false, 120, GridColumn.LEFT);
    addGridColumn("Modality", "modality", true, false, 200, GridColumn.LEFT);
    addGridColumn("Device", "device", true, false, 200, GridColumn.LEFT);
    addGridColumn("IP Address", "ipAddress", true, false, 100, GridColumn.LEFT);
    addGridColumn("Port", "port", true, false, 50, GridColumn.LEFT);
    addGridColumn("Close Note", "closeTiuNote", true, false, 70, GridColumn.LEFT);
    addGridColumn("Results Source", "resultsSource", true, false, 85, GridColumn.LEFT);
    
    // sort/metadata
    addSortInfo("category", SortInfo.ASC);
    setAutoExpandColumn(4);
    getGridMetaData().setIdProperty("id");
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
	/*
	private static List<CpDeviceJson> mapJsonObjects(List<CpDevice> cpdList) {
	  List<CpDeviceJson> cpdjList = new ArrayList<CpDeviceJson>();
	  for (CpDevice cpDevice : cpdList) {
	    CpDeviceJson cpDeviceJson = new CpDeviceJson();
	    cpDeviceJson.setId(cpDevice.getId());
	    cpDeviceJson.setCategory(cpDevice.getModality().getCategory().getCategory());
	    cpDeviceJson.setStationNo(cpDevice.getStationNo());
	    cpDeviceJson.setDevice(cpDevice.getComment());
	    cpDeviceJson.setModality(cpDevice.getModality().getModality() + " (" + cpDevice.getModality().getComment() + ")");
	    cpDeviceJson.setIpAddress(cpDevice.getIpAddress());
	    cpDeviceJson.setPort(cpDevice.getPort());
	    cpDeviceJson.setForwardHl7(cpDevice.isForwardHl7());
	    cpDeviceJson.setCloseTiuNote(cpDevice.isCloseTiuNote());
	    cpDeviceJson.setTextProcessorClass(cpDevice.getTextProcessorClass());
	    cpDeviceJson.setResultsSource(cpDevice.getResultsSource());
	    cpdjList.add(cpDeviceJson);
	  }
	  return cpdjList;
	}
	*/

  public String getStationNo() {
    return stationNo;
  }

  public void setStationNo(String stationNo) {
    this.stationNo = stationNo;
  }
	
}
