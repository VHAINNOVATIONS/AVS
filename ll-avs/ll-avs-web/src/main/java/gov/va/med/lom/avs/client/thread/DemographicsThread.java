package gov.va.med.lom.avs.client.thread;

import gov.va.med.lom.foundation.service.response.ServiceResponse;

import gov.va.med.lom.avs.model.BasicDemographics;

public class DemographicsThread extends SheetDataThread {

  public void run() {
    
    ServiceResponse<BasicDemographics> response = this.getSheetService().getBasicDemographics(
        this.securityContext, super.getEncounterInfo());
    BasicDemographics demographics = response.getPayload();
    
    setData("demographics", demographics);
  }
  
}
