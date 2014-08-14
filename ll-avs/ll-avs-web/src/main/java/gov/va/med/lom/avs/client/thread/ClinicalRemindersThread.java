package gov.va.med.lom.avs.client.thread;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;

import gov.va.med.lom.foundation.service.response.CollectionServiceResponse;

import gov.va.med.lom.javaUtils.misc.StringUtils;

import gov.va.med.lom.vistabroker.patient.data.ClinicalReminder;

import gov.va.med.lom.avs.model.EncounterInfo;
import gov.va.med.lom.avs.model.PvsClinicalReminder;

public class ClinicalRemindersThread extends SheetDataThread {

  public void run() {
    
    StringBuffer body = new StringBuffer();
    HashMap<String, String> facilityCRMap = new HashMap<String, String>();
    try {
      
      List<PvsClinicalReminder> facilityCRList = 
          (List<PvsClinicalReminder>)super.getSettingsService().findClinicalRemindersByStation(super.facilityNo).getCollection();
      for (PvsClinicalReminder cr : facilityCRList) {
        facilityCRMap.put(cr.getReminderIen(), cr.getReminderName());
      }
      
      CollectionServiceResponse<ClinicalReminder> response = 
          super.getSheetService().getClinicalReminders(super.securityContext, super.getEncounterInfo());
      List<ClinicalReminder> clinicalReminders = (List<ClinicalReminder>)response.getCollection();
      List<ClinicalReminder> patientCRList = new ArrayList<ClinicalReminder>();
      for (ClinicalReminder clinicalReminder : clinicalReminders) {
        if ((facilityCRMap.size() == 0) || facilityCRMap.containsKey(clinicalReminder.getIen())) {
          patientCRList.add(clinicalReminder);
        }
      }
      
      if (patientCRList.size() == 0) {
        body.append(super.getStringResource("noClinicalReminders"));
        
      } else {
        body.append("<div class=\"med-instructions\">");
        body.append(super.getStringResource("clinicalRemindersInstructions"));
        body.append("</div");
        for (ClinicalReminder clinicalReminder : patientCRList) {

          body.append("<div class=\"med-name\">")
          .append(clinicalReminder.getName())
          .append("</div>\n");
          
          body.append("<div class=\"med-detail\">")
          .append(super.getStringResource("whenDue") + ": ")
          .append(clinicalReminder.getWhenDue());

          if (!clinicalReminder.getLastOccurrence().isEmpty()) {
            body.append("&nbsp;&nbsp;&nbsp;&nbsp;")
            .append(super.getStringResource("lastOccurrence") + ": ")
            .append(clinicalReminder.getLastOccurrence());
          }
          body.append("</div>\n");
          
          String x = this.getSheetService().getClinicalReminderDetail(super.securityContext, super.getEncounterInfo(), 
              clinicalReminder.getIen()).getPayload();
          String[] s = StringUtils.pieceList(x, '\n');
          String freq = null;
          for (int i = 0; i < s.length; i++) {
            if (s[i].startsWith(super.getStringResource("frequency") + ":")) {
                freq = s[i];
                break;
            }
          }
          if (freq != null) {
            body.append("<div class=\"med-detail\">")
            .append(freq)
            .append("</div>\n");
          }
        }
        
      }
    
    } finally {
      String content = TPL_SECTION
        .replace("__SECTION_CLASS__", "section")
        .replace("__SECTION_ID_SUFFIX__", "clinicalReminders")
        .replace("__SECTION_TITLE__", super.getStringResource("clinicalReminders"))
        .replace("__CONTENTS__", body.toString());
      
      setContent("clinicalReminders", content);
    }     
    
  }
  
}
