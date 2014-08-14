package gov.va.med.lom.avs.client.action;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import gov.va.med.lom.foundation.service.response.ServiceResponse;
import gov.va.med.lom.foundation.util.Precondition;

import gov.va.med.lom.avs.web.util.AvsWebUtils;
import gov.va.med.lom.avs.enumeration.DisclaimerTypeEnum;
import gov.va.med.lom.avs.service.ServiceFactory;

public class DisclaimersAction extends BaseCardAction {

	protected static final Log log = LogFactory.getLog(DisclaimersAction.class);

	private static final long serialVersionUID = 0;

	private DisclaimerTypeEnum type;
	private String ien;
	private String text;
	private String stationNo;

	public String fetch() throws Exception {

		Precondition.assertNotNull("type", this.type);
		Precondition.assertNotEmpty("ien", this.ien);
		Precondition.assertNotBlank("stationNo", stationNo);

		ServiceResponse<String> response = ServiceFactory.getSettingsService()
			.fetchDisclaimers(this.stationNo, this.type, this.ien);
		AvsWebUtils.handleServiceErrors(response, log);
		String disclaimerText = response.getPayload();

		if (disclaimerText == null) {
			disclaimerText = "";
		}

		return super.setJson(disclaimerText);
	}

	public String save() throws Exception {

		Precondition.assertNotNull("type", this.type);
		Precondition.assertNotEmpty("ien", this.ien);
		Precondition.assertNotBlank("stationNo", stationNo);

		usageLog("Update Disclaimers", "Type=" + this.type + ", IEN=" + this.ien);
		
		ServiceResponse<Boolean> response = ServiceFactory.getSettingsService()
			.saveDisclaimers(this.stationNo, this.type, this.ien, this.text);
		AvsWebUtils.handleServiceErrors(response, log);

		return super.setJson("");
	}
	
	public DisclaimerTypeEnum getType() {
		return this.type;
	}
	
	public String getIen() {
		return this.ien;
	}

	public String getText() {
		return this.text;
	}

	public void setType(DisclaimerTypeEnum type) {
		this.type = type;
	}

	public void setIen(String ien) {
		this.ien = ien;
	}
	
	public void setText(String text) {
		this.text = text;
	}
	
  public String getStationNo() {
    return stationNo;
  }

  public void setStationNo(String stationNo) {
    this.stationNo = stationNo;
  }	
}
