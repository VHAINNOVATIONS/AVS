package gov.va.med.lom.avs.client.thread;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import gov.va.med.lom.foundation.service.response.CollectionServiceResponse;
import gov.va.med.lom.javaUtils.misc.StringUtils;

import gov.va.med.lom.avs.web.util.AvsWebUtils;
import gov.va.med.lom.avs.client.model.MedicationJson;
import gov.va.med.lom.avs.model.MedicationRdv;
import gov.va.med.lom.avs.model.EncounterInfo;

public class LocalNonVaMedicationsThread extends SheetDataThread {

  private String remoteNonVaMedicationsHtml;
  
  public LocalNonVaMedicationsThread() {
    super();
  }
  
  public LocalNonVaMedicationsThread(String remoteNonVaMedicationsHtml) {
    super();
    this.remoteNonVaMedicationsHtml = remoteNonVaMedicationsHtml;
  }
  
  public void run() {
    
    StringBuffer body = new StringBuffer();
    List<MedicationJson> medsJson = new ArrayList<MedicationJson>();
    String sectionClass = "section-hidden";
    try {
      CollectionServiceResponse<MedicationRdv> response = 
          super.getSheetService().getNonVAMedicationsRdv(super.securityContext, super.getEncounterInfo(), 
              AvsWebUtils.getStartDate(DEF_EXPIRED_MED_DAYS));
      Collection<MedicationRdv> localNonVaMedications = response.getCollection();
        
      int numMeds = 0;
      body.append("<div id=\"non-va-meds-div\">");
      if ((!super.isInitialRequest() || (super.media == MEDIA_PDF)) && 
          (this.remoteNonVaMedicationsHtml != null) && !this.remoteNonVaMedicationsHtml.isEmpty() &&
          (this.remoteNonVaMedicationsHtml.indexOf(super.getStringResource("medsNotTaking")) > 0)) {
        body.append("<div class=\"med-instructions\">");
        body.append("<u>" + super.getStringResource("medsTaking") + "</u>");
        body.append("</div>");
      }   
      if (localNonVaMedications.size() > 0) {
        for (MedicationRdv med : localNonVaMedications) {
          if ((med.getName() == null) || !med.getStatus().equalsIgnoreCase("active")) {
            continue;
          }
          numMeds++;
          MedicationJson medJson = new MedicationJson();
          medsJson.add(medJson);
          medJson.setSite(super.getEncounterInfo().getFacilityName());
          medJson.setStationNo(super.getEncounterInfo().getFacilityNo());
          medJson.setType("Outpatient");
          
          body.append("<div class=\"med-name\">");
          if (super.docType.equals(EncounterInfo.PVS)) {
            body.append("<input type=\"checkbox\">&nbsp;&nbsp;");
          }
          body.append(StringUtils.mixedCase(med.getName()))
            .append("</div>\n");  
          medJson.setName(med.getName());
          
          StringBuffer sig = new StringBuffer(SIG_PATTERN.matcher(med.getSig()).replaceAll(""));
          if (!sig.toString().isEmpty()) {
            sig.insert(0, super.getStringResource("take") + " ");
            if (sig.toString().contains("MOUTH") && !sig.toString().contains(" BY ")) {
              sig.insert(sig.indexOf("MOUTH"), super.getStringResource("by") + " ");
            }
          }
          body.append("<div class=\"med-detail\">")
            .append(sig.toString())
            .append("</div>\n");    
          medJson.setSig(sig.toString());
          
          body.append("<div class=\"med-detail\">");
          if ((med.getStartDate() != null) && !med.getStartDate().isEmpty()) {
            body.append(super.getStringResource("startDate") + ": ")
            .append(med.getStartDate())
            .append("&nbsp;&nbsp;&nbsp;");
            medJson.setStartDate(med.getStartDate());
          }
          
          if ((med.getStopDate() != null) && !med.getStopDate().isEmpty()) {
            body.append(super.getStringResource("stopDate") + ": ")
            .append(med.getStopDate());
            medJson.setStopDate(med.getStopDate());
          }
          
          body.append("</div>\n");
        }
      }
      body.append("</div>");
      if (super.isInitialRequest() || ((this.remoteNonVaMedicationsHtml != null) && !this.remoteNonVaMedicationsHtml.isEmpty())) {
        body.append("<div id=\"remote-non-va-medications-div\">");
        body.append(this.remoteNonVaMedicationsHtml != null ? this.remoteNonVaMedicationsHtml : "");
        body.append("</div>");      
      }
      
      if ((numMeds > 0) || ((this.remoteNonVaMedicationsHtml != null) && !this.remoteNonVaMedicationsHtml.isEmpty())) {
        sectionClass = "section";
      }
    } finally {
      String content = TPL_SECTION
        .replace("__SECTION_CLASS__", sectionClass)
        .replace("__SECTION_ID_SUFFIX__", "non-va_medications")
        .replace("__SECTION_TITLE__", super.getStringResource("nonVaMeds"))
        .replace("__CONTENTS__", body.toString());
      
      setContentData("non-va_medications", content, medsJson);
    }    
  }
  
}
