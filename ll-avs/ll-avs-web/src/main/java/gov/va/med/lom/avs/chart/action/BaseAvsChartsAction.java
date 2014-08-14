package gov.va.med.lom.avs.chart.action;

import gov.va.med.authentication.kernel.LoginUserInfoVO;

import gov.va.med.lom.avs.model.UsageLogMongo;
import gov.va.med.lom.avs.service.ServiceFactory;
import gov.va.med.lom.avs.web.util.AvsWebUtils;
import gov.va.med.lom.foundation.service.response.BaseServiceResponse;
import gov.va.med.lom.foundation.service.response.messages.Message;
import gov.va.med.lom.foundation.service.response.messages.Messages;

import gov.va.med.lom.charts.struts.action.BaseChartsAction;

import gov.va.med.lom.javaBroker.util.StringUtils;
import gov.va.med.lom.login.struts.session.SessionUtil;

import gov.va.med.lom.vistabroker.security.ISecurityContext;
import gov.va.med.lom.vistabroker.security.SecurityContextFactory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

abstract public class BaseAvsChartsAction extends BaseChartsAction {

	protected ISecurityContext securityContext;
	protected String facilityNo;
	protected String userDuz;
	protected String patientDfn;
  protected String locationIens;
  protected String locationNames;
  protected String datetimes;	
  
  private List<Double> datetimeList;
  private List<String> locationIenList;
  private List<String> locationNameList;
  
	private static final long serialVersionUID = 0;

	protected static final Log log = LogFactory.getLog(BaseAvsChartsAction.class);

	public void prepare() throws Exception {

		super.prepare();

		if (!SessionUtil.isActiveSession(super.request)) {
			return;
		}

		LoginUserInfoVO loginUserInfo = SessionUtil.getLoginUserInfo(super.request);		

		this.facilityNo = loginUserInfo.getLoginStationNumber();
		this.userDuz = loginUserInfo.getUserDuz();

		this.securityContext = SecurityContextFactory.createDuzSecurityContext(this.facilityNo, this.userDuz);
	}

	protected static void handleServiceErrors(BaseServiceResponse response) throws RuntimeException {
		handleServiceErrors(response, null);
	}

	protected static void handleServiceErrors(BaseServiceResponse response, String prefix) throws RuntimeException {

		if (!BaseServiceResponse.hasErrorMessage(response)) {
			return;
		}

		StringBuffer sb = new StringBuffer();
		sb.append("ERROR: ");
		if (prefix != null) {
			sb.append(prefix);
		}
		sb.append(":\n");
		Messages messages = response.getMessages();
		Iterator<Message> it = messages.getErrorMessages().iterator();
		while (it.hasNext()) {
			Message msg = it.next();
			sb.append(msg.getKey()).append("\n");
		}
		String message = sb.toString();
		log.error(message);
		System.out.println(message);
		throw new RuntimeException(message);
	}
	
  protected UsageLogMongo usageLog(String action, String data) {
    UsageLogMongo ul = null;
    try {
      ul = new UsageLogMongo();
      ul.setAction(action);
      ul.setData(data);
      ul.setFacilityNo(this.facilityNo);
      ul.setUserDuz(this.userDuz);
      ul.setPatientDfn(this.patientDfn);
      ul.setLocationIens(this.getLocationIens());
      ul.setLocationNames(this.getLocationNames());
      ul.setDatetimes(this.getDatetimes());
      ServiceFactory.getSettingsService().saveUsageLog(ul);
    } catch(Exception e) {
      e.printStackTrace();
    }
    return ul;
  }
  
  /**
   * Output HTML to the browser.  This method is made to parallel BaseAction.setJson.
   */
  protected String setHtml(String html) {
    try {
      super.response.setHeader("Expires", "0");
      super.response.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
      super.response.setHeader("Pragma", "public");
      super.response.setContentType("text/html");
      super.response.setContentLength(html.length());
      super.response.getWriter().write(html);
    } catch (Exception e) {
      log.error("error creating HTML data", e);
    }

    return SUCCESS;
  }  
	
	public String getPatientDfn() {
		return this.patientDfn;
	}
	public void setPatientDfn(String patientDfn) {
		this.patientDfn = patientDfn;
	}
	
  public List<String> getLocationIens() {
    if (this.locationIenList == null) {
      this.locationIenList = AvsWebUtils.delimitedStringToList(this.locationIens, ',');
    }
    return this.locationIenList;    
  }
  
  public List<String> getLocationNames() {
    if (this.locationNameList == null) {
      this.locationNameList = new ArrayList<String>();
      String[] list = StringUtils.pieceList(this.locationNames, ',');
      for (String s : list) {
        this.locationNameList.add(s.trim());
      }
    }
    return this.locationNameList;  
  } 

  public List<Double> getDatetimes() {
    if (this.datetimeList == null) {
      this.datetimeList = new ArrayList<Double>();
      String[] list = StringUtils.pieceList(this.datetimes, ',');
      for (String s : list) {
        this.datetimeList.add(StringUtils.toDouble(s.trim(), 0.0));
      }
    }
    return this.datetimeList;
  }
}
