package gov.va.med.lom.avs.client.action;

import java.util.List;
import java.util.ArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.interceptor.ServletRequestAware;

import com.opensymphony.xwork2.Preparable;

import gov.va.med.lom.javaUtils.misc.StringUtils;
import gov.va.med.lom.json.struts.action.BaseAction;

import gov.va.med.lom.avs.client.model.DivisionJson;
import gov.va.med.lom.avs.client.model.UserClassJson;
import gov.va.med.lom.avs.service.ServiceFactory;
import gov.va.med.lom.avs.service.SettingsService;
import gov.va.med.lom.avs.model.Division;

public class UserAction extends BaseAction implements ServletRequestAware, Preparable {

	protected static final Log log = LogFactory.getLog(UserAction.class);

	private static final long serialVersionUID = 0;

	private SettingsService settingsService;
	
	private String stationNo;
	private String userDuz;
	
  public void prepare() throws Exception {
    
    super.prepare();    
    try {
      this.settingsService = ServiceFactory.getSettingsService();
    } catch(Exception e) {
      log.error("error creating service", e);
    }
    request.getSession(true);
  } 	
	
  public String divisions() throws Exception {
    String facilityNo = this.settingsService.getFacilityNoForDivision(this.stationNo).getPayload();
    Division defDivision = this.settingsService.getDefaultDivisionForUser(facilityNo, this.userDuz).getPayload();
    List<Division> divisions = (List<Division>)this.settingsService.getDivisions(facilityNo).getCollection();
    List<DivisionJson> djList = new ArrayList<DivisionJson>();
    for (Division division : divisions) {
      boolean isDefault = defDivision.getFacilityNo().equals(division.getFacilityNo());
      DivisionJson divisionJson = 
          new DivisionJson(division.getInsitutionId(), division.getFacilityNo(), division.getName(), isDefault);
      djList.add(divisionJson);
    }
    return setJson(djList);
  }
  
	public String defaultDivision() throws Exception {
	  String facilityNo = this.settingsService.getFacilityNoForDivision(this.stationNo).getPayload();
    Division division = this.settingsService.getDefaultDivisionForUser(facilityNo, this.userDuz).getPayload();
    DivisionJson divisionJson = new DivisionJson(division.getInsitutionId(), division.getFacilityNo(), division.getName(), true);
    return writeJson(divisionJson);
	}
	
  public String userClasses() throws Exception {
    String facilityNo = this.settingsService.getFacilityNoForDivision(this.stationNo).getPayload();
    List<String> userClasses = 
        (List<String>)this.settingsService.getUserClasses(facilityNo, this.userDuz).getCollection();
    List<UserClassJson> ucjList = new ArrayList<UserClassJson>();
    for (String userClass : userClasses) {
      ucjList.add(new UserClassJson(StringUtils.piece(userClass, 1), StringUtils.piece(userClass, 2)));
    }
    return writeJson(ucjList);
  }	
  
  public String isUserAdmin() throws Exception {
    String facilityNo = this.settingsService.getFacilityNoForDivision(this.stationNo).getPayload();
    List<String> userClasses = 
        (List<String>)this.settingsService.getUserClasses(facilityNo, this.userDuz).getCollection();
    for (String userClass : userClasses) {
      String name = StringUtils.piece(userClass, 2);
      if (name.equals("AVS ADMINISTRATOR")) {
        return writeJson(true);
      }
    }
    return writeJson(false);
  }   

  public String getUserDuz() {
    return userDuz;
  }

  public void setUserDuz(String userDuz) {
    this.userDuz = userDuz;
  }

  public String getStationNo() {
    return stationNo;
  }

  public void setStationNo(String stationNo) {
    this.stationNo = stationNo;
  }

}
