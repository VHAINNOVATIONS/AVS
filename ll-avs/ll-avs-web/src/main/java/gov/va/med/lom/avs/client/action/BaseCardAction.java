package gov.va.med.lom.avs.client.action;

import java.util.List;
import java.util.ArrayList;

import gov.va.med.authentication.kernel.LoginUserInfoVO;

import gov.va.med.lom.avs.web.util.AvsWebUtils;
import gov.va.med.lom.avs.model.UsageLogMongo;
import gov.va.med.lom.avs.service.ServiceFactory;

import gov.va.med.lom.javaBroker.util.StringUtils;
import gov.va.med.lom.json.struts.action.BaseAction;

import gov.va.med.lom.login.struts.session.SessionUtil;

import gov.va.med.lom.vistabroker.security.ISecurityContext;
import gov.va.med.lom.vistabroker.security.SecurityContextFactory;

import java.io.ByteArrayOutputStream;

import javax.servlet.ServletOutputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

abstract public class BaseCardAction extends BaseAction {

	protected ISecurityContext securityContext;
	protected String facilityNo;
	protected String userDuz;
	protected String userName;
	protected String facilityName;
	protected String patientDfn;
  // encounter params
	protected String locationIens;
	protected String locationNames;
	protected String datetimes;
	
	private List<Double> datetimeList;
	private List<String> locationIenList;
	private List<String> locationNameList;
	
	private static final long serialVersionUID = 0;

	protected static final Log log = LogFactory.getLog(BaseCardAction.class);

	public void prepare() throws Exception {

		super.prepare();

		if (!SessionUtil.isActiveSession(super.request)) {
			return;
		}

		LoginUserInfoVO loginUserInfo = SessionUtil.getLoginUserInfo(super.request);		

		this.facilityNo = loginUserInfo.getLoginStationNumber();
		this.userDuz = loginUserInfo.getUserDuz();
		this.userName = loginUserInfo.getUserName01();
		this.facilityName = loginUserInfo.getDivisionName();
		this.datetimeList = null;
		this.locationIenList = null;
		this.securityContext = SecurityContextFactory.createDuzSecurityContext(this.facilityNo, this.userDuz);
	}

  protected UsageLogMongo usageLog(String action, String data) {
    UsageLogMongo ul = null;
    try {
      StringBuffer sb = new StringBuffer(action);
      sb.append(" - ");
      sb.append(data);
      sb.append("(Facility=");
      sb.append(this.facilityNo);
      sb.append(", DUZ=");
      sb.append(this.userDuz);
      sb.append(", DFN=");
      sb.append(this.patientDfn);
      sb.append(", LOC=");
      sb.append(this.locationIens);
      sb.append(", DT=");
      sb.append(this.datetimes);
      sb.append(")");
      log.info(sb.toString());
    } catch(Exception e) {
      e.printStackTrace();
    }
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
	public String setHtml(String html) {
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
	
  /**
   * Output PDF to the browser.  This method is made to parallel BaseAction.flushPdf.
   */  
	public String flushPdf(ByteArrayOutputStream buffer) {
    try {
      super.response.setHeader("Expires", "0");
      super.response.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
      super.response.setHeader("Pragma", "public");
      super.response.setContentType("application/pdf");
      super.response.setContentLength(buffer.size());
      ServletOutputStream out = super.response.getOutputStream();
      buffer.writeTo(out);
      out.flush();
    } catch (Exception e) {
      log.error("error flushing pdf", e);
    }
    
    return SUCCESS;
  } 	
	
  public String downloadPdf(ByteArrayOutputStream buffer, String filename) {
    try {
      super.response.setHeader("Expires", "0");
      super.response.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
      super.response.setHeader("Pragma", "public");
      super.response.setHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"");
      super.response.setContentType("application/pdf");
      super.response.setContentLength(buffer.size());
      ServletOutputStream out = super.response.getOutputStream();
      buffer.writeTo(out);
      out.flush();
    } catch (Exception e) {
      log.error("error flushing pdf", e);
    }
    
    return SUCCESS;
  } 	

	
	protected static int safelyConvertLongToInteger(long number) {
	  if (number < Integer.MIN_VALUE || number > Integer.MAX_VALUE) {
	    throw new IllegalArgumentException("The number " + number + " can't be safely converted to an Integer.");
	  }
	  return (int)number;
	}
    
	/* Accessors */
	
	public String getPatientDfn() {
		return this.patientDfn;
	}

	public List<String> getLocationIens() {
    if ((this.locationIenList == null) && (this.locationIens != null)) {
      this.locationIenList = AvsWebUtils.delimitedStringToList(this.locationIens, ',');
    }
    return this.locationIenList;		
	}
	
  public List<String> getLocationNames() {
    if ((this.locationNameList == null) && (this.locationNames != null)) {
      this.locationNameList = new ArrayList<String>();
      String[] list = StringUtils.pieceList(this.locationNames, ',');
      for (String s : list) {
        this.locationNameList.add(s.trim());
      }
    // populate locations list with empty strings
    } else if ((this.locationNameList == null) && 
        (this.locationNames == null) && (this.locationIens != null))  {
      this.locationNameList = new ArrayList<String>();
      List<String> tempIens = this.getLocationIens();
      for (String ien : tempIens) {
        this.locationNameList.add("");
      }
    }
    return this.locationNameList;  
  }	

	public List<Double> getDatetimes() {
	  if ((this.datetimeList == null) && (this.datetimes != null)) {
	    this.datetimeList = new ArrayList<Double>();
	    String[] list = StringUtils.pieceList(this.datetimes, ',');
	    for (String s : list) {
 	      this.datetimeList.add(StringUtils.toDouble(s.trim(), 0.0));
	    }
	  }
	  return this.datetimeList;
	}

	/* Mutators */
	
	public void setPatientDfn(String patientDfn) {
		this.patientDfn = patientDfn;
	}

	public void setLocationIens(String locationIens) {
		this.locationIens = locationIens;
	}

	public void setDatetimes(String datetimes) {
		this.datetimes = datetimes;
	}

  public void setLocationNames(String locationNames) {
    this.locationNames = locationNames;
  }
	
}
