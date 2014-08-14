package gov.va.med.lom.avs.client.thread;

import java.util.List;

import gov.va.med.lom.foundation.service.response.CollectionServiceResponse;

import gov.va.med.lom.avs.model.PceData;

public class PceDataThread extends SheetDataThread {

  public void run() {
    CollectionServiceResponse<PceData> response = this.getSheetService().getPceData(securityContext, super.getEncounterInfo());
    List<PceData> result = (List<PceData>)response.getCollection();
    setData("pceData", result);
  }
  
}
