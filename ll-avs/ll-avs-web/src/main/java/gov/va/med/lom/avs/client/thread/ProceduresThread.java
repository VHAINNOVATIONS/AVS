package gov.va.med.lom.avs.client.thread;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import gov.va.med.lom.foundation.service.response.CollectionServiceResponse;

import gov.va.med.lom.javaUtils.misc.StringUtils;

import gov.va.med.lom.avs.client.model.ProcedureJson;
import gov.va.med.lom.avs.web.util.AvsWebUtils;
import gov.va.med.lom.avs.model.Procedure;

public class ProceduresThread extends SheetDataThread {

  public void run() {
    
    StringBuffer body = new StringBuffer();
    List<ProcedureJson> proceduresList = new ArrayList<ProcedureJson>();
    List<String> items = new ArrayList<String>();
    List<ProcedureJson> procedures = new ArrayList<ProcedureJson>();
    try {
      CollectionServiceResponse<Procedure> response = 
          super.getSheetService().getProcedures(super.securityContext, super.getEncounterInfo());
      Collection<Procedure> list = response.getCollection();      
      for (Procedure procedure : list) {
        ProcedureJson procedureJson = null;
        procedureJson = new ProcedureJson();
        proceduresList.add(procedureJson);
        procedureJson.setSite(procedure.getStationName());
        procedureJson.setName(StringUtils.mixedCase(procedure.getName()));
        procedureJson.setDate(procedure.getDate());
        procedureJson.setCode(procedure.getCode());
        procedures.add(procedureJson);
        items.add(StringUtils.mixedCase(procedure.getName()));
      }
      
      switch (items.size()) {
      case 0:
        body.append("None");
        break;
      case 1:
        body.append(items.get(0));
        break;
      default:
        body.append(AvsWebUtils.renderUnorderedList(items));
    }
      
    } finally {
      String content = TPL_SECTION
        .replace("__SECTION_CLASS__", "section")
        .replace("__SECTION_ID_SUFFIX__", "procedures")
        .replace("__SECTION_TITLE__", super.getStringResource("procedures"))
        .replace("__CONTENTS__", body.toString());
      setContentData("procedures", content, procedures);
    }  
  }
  
}
