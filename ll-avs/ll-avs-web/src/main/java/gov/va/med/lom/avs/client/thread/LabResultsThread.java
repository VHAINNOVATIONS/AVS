package gov.va.med.lom.avs.client.thread;

import gov.va.med.lom.foundation.service.response.ServiceResponse;

public class LabResultsThread extends SheetDataThread {

  private int labResultsDaysBack;
  
  public LabResultsThread(int labResultsDaysBack) {
    super();
    this.labResultsDaysBack = labResultsDaysBack;
  }
  
  public void run() {
    
    StringBuffer body = new StringBuffer();
    String data = null;
    try {
      ServiceResponse<String> response = 
          super.getSheetService().getLabResults(securityContext, super.getEncounterInfo(), this.labResultsDaysBack);
      String labResults = response.getPayload();
      if (labResults.isEmpty() || labResults.trim().equalsIgnoreCase("No Lab Results Found") ||
          labResults.trim().equalsIgnoreCase("No Data Found")) {
        data = super.getStringResource("none");
        body.append(data);
      } else {
        data = labResults;
        body.append("<pre class=\"fixed-width\">")
            .append(labResults)
            .append("</pre>\n");        
      }
    } finally {
      String content = TPL_SECTION
        .replace("__SECTION_CLASS__", "section")
        .replace("__SECTION_ID_SUFFIX__", "labResults")
        .replace("__SECTION_TITLE__", super.getStringResource("labResults"))
        .replace("__CONTENTS__", body.toString());
      
      super.setContentData("labResults", content, data);
    }
    
  }
  
}
