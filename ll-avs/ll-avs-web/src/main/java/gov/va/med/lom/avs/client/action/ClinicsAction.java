package gov.va.med.lom.avs.client.action;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import gov.va.med.lom.foundation.service.response.CollectionServiceResponse;

import gov.va.med.lom.avs.web.util.AvsWebUtils;
import gov.va.med.lom.avs.service.ServiceFactory;

import gov.va.med.lom.vistabroker.lists.data.ListItem;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ClinicsAction extends BaseCardAction {

	protected static final Log log = LogFactory.getLog(ClinicsAction.class);

	private static final long serialVersionUID = 0;

	private String startFrom;

	public String search() throws Exception {

		List<List<String>> clinics = new ArrayList<List<String>>(); 

		if (this.startFrom != null && this.startFrom.length() > 0) {

			CollectionServiceResponse<ListItem> serviceResponse = ServiceFactory.getSettingsService()
				.searchForClinics(super.securityContext, this.startFrom);
			AvsWebUtils.handleServiceErrors(serviceResponse, log);
			Collection<ListItem> clinicItems = serviceResponse.getCollection();

			for (ListItem clinicItem : clinicItems) {
				List<String> clinic = new ArrayList<String>();
				clinic.add(clinicItem.getIen());
				clinic.add(clinicItem.getName());

				clinics.add(clinic);
			}

		}

		return super.setJson(clinics);
	}

	public String getStartFrom() {
		return this.startFrom;
	}

	public void setStartFrom(String startFrom) {
		this.startFrom = startFrom;
	}

}
