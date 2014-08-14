package gov.va.med.lom.avs.client.action;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import gov.va.med.lom.foundation.service.response.CollectionServiceResponse;
import gov.va.med.lom.foundation.service.response.ServiceResponse;
import gov.va.med.lom.foundation.util.Precondition;

import gov.va.med.lom.avs.web.util.AvsWebUtils;
import gov.va.med.lom.avs.service.ServiceFactory;

import java.util.Collection;

public class HeaderFooterAction extends BaseCardAction {

	protected static final Log log = LogFactory.getLog(HeaderFooterAction.class);

	private static final long serialVersionUID = 0;

	private String header;
	private String footer;
	private String divisionNo;
  private String language;

	public String get() throws Exception {

		CollectionServiceResponse<String> response = ServiceFactory.getSettingsService()
			.getHeaderAndFooter(this.getDivisionNo());
		AvsWebUtils.handleServiceErrors(response, log);
		Collection<String> headerAndFooter = response.getCollection();
		return super.setJson(headerAndFooter);
	}

	public String save() throws Exception {

		Precondition.assertNotBlank("header", this.header.trim());
		Precondition.assertNotBlank("footer", this.footer.trim());

		usageLog("Update Header/Footer", "");
		
		ServiceResponse<Boolean> response = ServiceFactory.getSettingsService()
			.saveHeaderAndFooter(this.getDivisionNo(), this.getLanguage(), this.header, this.footer);
		AvsWebUtils.handleServiceErrors(response, log);

		return super.setJson("");
	}
	
	public String getHeader() {
		return this.header;
	}
	
	public String getFooter() {
		return this.footer;
	}
	
	public void setHeader(String header) {
		this.header = header;
	}

	public void setFooter(String footer) {
		this.footer = footer;
	}
	
  public String getDivisionNo() {
    return this.divisionNo != null && !this.divisionNo.isEmpty() ? this.divisionNo : super.facilityNo;
  }

  public void setDivisionNo(String divisionNo) {
    this.divisionNo = divisionNo;
  }

  public String getLanguage() {
    return this.language != null && !this.language.isEmpty() ? this.language : "en";
  }

  public void setLanguage(String language) {
    this.language = language;
  }

}
