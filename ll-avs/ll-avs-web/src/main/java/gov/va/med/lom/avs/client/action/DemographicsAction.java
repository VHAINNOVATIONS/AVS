package gov.va.med.lom.avs.client.action;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import gov.va.med.lom.foundation.util.Precondition;
import gov.va.med.lom.foundation.service.response.ServiceResponse;

import gov.va.med.lom.avs.web.util.AvsWebUtils;
import gov.va.med.lom.avs.model.BasicDemographics;
import gov.va.med.lom.avs.model.EncounterInfo;
import gov.va.med.lom.avs.service.ServiceFactory;

public class DemographicsAction extends BaseCardAction {

	private static final long serialVersionUID = 0;

	protected static final Log log = LogFactory.getLog(DemographicsAction.class);

	public String run() throws Exception {

		Precondition.assertNotBlank("patientDfn", super.patientDfn);
		EncounterInfo encounterInfo = new EncounterInfo();
		encounterInfo.setFacilityNo(super.facilityNo);
		encounterInfo.setPatientDfn(super.patientDfn);
		ServiceResponse<BasicDemographics> response = ServiceFactory.getSheetService()
				.getBasicDemographics(super.securityContext, encounterInfo);
		AvsWebUtils.handleServiceErrors(response, log);
		BasicDemographics demographics = response.getPayload();
		
		if (SheetAction.IS_DEMO) {
		  demographics.setName(SheetAction.DEMO_PT_NAME);
		  demographics.setSsn(SheetAction.DEMO_PT_SSN);
		  demographics.setDob(SheetAction.DEMO_PT_DOB_DM);
		}

		return super.setJson(demographics);
	}

}
