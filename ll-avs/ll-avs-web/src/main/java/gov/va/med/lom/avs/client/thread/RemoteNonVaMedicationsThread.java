package gov.va.med.lom.avs.client.thread;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import gov.va.med.lom.foundation.service.response.CollectionServiceResponse;

import gov.va.med.lom.javaUtils.misc.DateUtils;
import gov.va.med.lom.javaUtils.misc.StringUtils;

import gov.va.med.lom.avs.client.model.MedicationJson;
import gov.va.med.lom.avs.web.util.AvsWebUtils;
import gov.va.med.lom.avs.model.MedicationRdv;
import gov.va.med.lom.avs.model.EncounterInfo;

public class RemoteNonVaMedicationsThread extends SheetDataThread {

  private boolean formatAsHtml = false;
  
  public RemoteNonVaMedicationsThread() {
    this(false);
  }
  
  public RemoteNonVaMedicationsThread(boolean formatAsHtml) {
    super();
    this.formatAsHtml = formatAsHtml;
  }
  
  public void run() {
    
    StringBuffer body = null;
    List<MedicationJson> remoteNonVaMedsList = null;
    
    if (this.formatAsHtml) {
      body = new StringBuffer();
    } else {
      remoteNonVaMedsList = new ArrayList<MedicationJson>();
    }    
    
    try {
      CollectionServiceResponse<MedicationRdv> response = 
          super.getSheetService().getRemoteNonVaMedicationsRdv(super.securityContext, super.getEncounterInfo(), 
              AvsWebUtils.getStartDate(DEF_EXPIRED_MED_DAYS));
      Collection<MedicationRdv> remoteNonVaMedications = response.getCollection();      
      int i = 1;
      for (MedicationRdv med : remoteNonVaMedications) {
        
        MedicationJson rmJson = null;
        if (!this.formatAsHtml) {        
          rmJson = new MedicationJson();
          remoteNonVaMedsList.add(rmJson);
          rmJson.setId(String.valueOf(i++));
          rmJson.setName(StringUtils.mixedCase(med.getName()));
          rmJson.setType("Non-VA");
          rmJson.setSig(SIG_PATTERN.matcher(med.getSig()).replaceAll(""));
          try {
            Date dt = DateUtils.convertDateStr(med.getStartDate(), "mm/dd/yyyy");
            rmJson.setStartDate(DateUtils.formatDate(dt, "MMMM dd, yyyy"));
          } catch(Exception e) {
            rmJson.setStartDate(super.getStringResource("na"));
          }
          try {
            Date dt = DateUtils.convertDateStr(med.getStopDate(), "mm/dd/yyyy");
            rmJson.setStopDate(DateUtils.formatDate(dt, "MMMM dd, yyyy"));
          } catch(Exception e) {
            rmJson.setStopDate(super.getStringResource("na"));
          }       
          rmJson.setSite(med.getStationName());
          rmJson.setStationNo(med.getStationNo());
          rmJson.setProvider(med.getDocumentor());
          
        } else {
          
          body.append("<div class=\"med-name\">");
          if (super.docType.equals(EncounterInfo.PVS)) {
            body.append("<input type=\"checkbox\">&nbsp;&nbsp;");
          }
          body.append(StringUtils.mixedCase(med.getName()))
            .append("</div>\n");          

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
          
          body.append("<div class=\"med-detail\">");
          if ((med.getStartDate() != null) && !med.getStartDate().isEmpty()) {
            try {
              Date dt = DateUtils.convertDateStr(med.getStartDate(), "mm/dd/yyyy");
              body.append(super.getStringResource("startDate") + ": ")
              .append(DateUtils.formatDate(dt, "MMMM dd, yyyy"))
              .append("&nbsp;&nbsp;&nbsp;");
            } catch(Exception e) {
            }            
          }
          if ((med.getStopDate() != null) && !med.getStopDate().isEmpty()) {
            try {
              Date dt = DateUtils.convertDateStr(med.getStopDate(), "mm/dd/yyyy");
              body.append(super.getStringResource("stopDate") + ": ")
              .append(DateUtils.formatDate(dt, "MMMM dd, yyyy"));
            } catch(Exception e) {
            }             
          }
          if ((med.getStartDate() != null) && !med.getStartDate().isEmpty() &&
              (med.getStopDate() != null) && !med.getStopDate().isEmpty()) {
            body.append("<br/>");
          }
          body.append(super.getStringResource("documentingFacility") + ": ")
            .append(med.getStationName())
            .append("</div>\n");
        }
      }
    } finally {
      if (this.formatAsHtml) {
        super.setContent("remote-non_va_medications", body.toString()); 
      } else {
        super.setData("remote-non_va_medications", remoteNonVaMedsList);
      }      
    }
  }
  
}
