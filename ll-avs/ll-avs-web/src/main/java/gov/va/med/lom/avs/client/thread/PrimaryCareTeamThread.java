package gov.va.med.lom.avs.client.thread;

import java.util.List;
import java.util.ArrayList;

import gov.va.med.lom.avs.web.util.AvsWebUtils;
import gov.va.med.lom.avs.client.model.PrimaryCareTeamMemberJson;
import gov.va.med.lom.foundation.service.response.CollectionServiceResponse;
import gov.va.med.lom.javaUtils.misc.StringUtils;

public class PrimaryCareTeamThread extends SheetDataThread {

  public void run() {
    
    StringBuffer body = new StringBuffer();
    CollectionServiceResponse<String> response = 
        super.getSheetService().getPatientTeamMembers(securityContext, super.getEncounterInfo(), super.demographics.getPrimaryTeam());
    List<String> teamMembers = (List<String>)response.getCollection();
    if (teamMembers.isEmpty()) {
      super.setContent("primaryCareTeam", "");
    } else {
      List<PrimaryCareTeamMemberJson> pctMembers = new ArrayList<PrimaryCareTeamMemberJson>();
      for (String s : teamMembers) {
        pctMembers.add(new PrimaryCareTeamMemberJson(StringUtils.piece(s, '-', 1).trim(), StringUtils.piece(s, '-', 2).trim()));
      }
      body.append(AvsWebUtils.renderTwoColumnList(teamMembers, super.media == MEDIA_PDF, this.fontClass));
      String content = TPL_SECTION
        .replace("__SECTION_CLASS__", "section")
        .replace("__SECTION_ID_SUFFIX__", "primaryCareTeam")
        .replace("__SECTION_TITLE__", super.getStringResource("primaryCareTeam"))
        .replace("__CONTENTS__", body.toString());
      
      super.setContentData("primaryCareTeam", content, pctMembers);
    }
  }
}
