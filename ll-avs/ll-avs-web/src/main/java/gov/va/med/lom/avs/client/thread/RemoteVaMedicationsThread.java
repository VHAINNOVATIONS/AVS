package gov.va.med.lom.avs.client.thread;

import java.util.Collection;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;

import gov.va.med.lom.foundation.service.response.CollectionServiceResponse;

import gov.va.med.lom.javaUtils.misc.DateUtils;
import gov.va.med.lom.javaUtils.misc.StringUtils;

import gov.va.med.lom.avs.client.model.MedicationJson;
import gov.va.med.lom.avs.web.util.AvsWebUtils;
import gov.va.med.lom.avs.model.MedicationRdv;
import gov.va.med.lom.avs.model.MedDescription;
import gov.va.med.lom.avs.model.EncounterInfo;

public class RemoteVaMedicationsThread extends SheetDataThread {

  private boolean formatAsHtml = false;
  
  public RemoteVaMedicationsThread() {
    this(false);
  }
  
  public RemoteVaMedicationsThread(boolean formatAsHtml) {
    super();
    this.formatAsHtml = formatAsHtml;
  }
  
  public void run() {
    
    StringBuffer body = null;
    List<MedicationJson> remoteVaMedsList = null;
    
    if (this.formatAsHtml) {
      body = new StringBuffer();
    } else {
      remoteVaMedsList = new ArrayList<MedicationJson>();
    }
    
    try {
      CollectionServiceResponse<MedicationRdv> response2 = this.getSheetService()
          .getRemoteOutpatientMedicationsRdv(this.securityContext, super.getEncounterInfo(), 
              AvsWebUtils.getStartDate(DEF_EXPIRED_MED_DAYS));
      Collection<MedicationRdv> remoteVaMedications = response2.getCollection();
      List<String> medNdcs = new ArrayList<String>();
      for (MedicationRdv med : remoteVaMedications) {
        if (med.getComment() != null) {
          if (!med.getComment().isEmpty()) {
            String ndc = StringUtils.piece(med.getComment(), '^', 1);
            ndc = StringUtils.deleteChar(ndc, '-');
            medNdcs.add(ndc);
          }
        }
      }
      
      Hashtable<String, MedDescription> medDescriptionsHT = new Hashtable<String, MedDescription>(); 
      if (super.facilityPrefs.getMedDescriptionsEnabled() && !medNdcs.isEmpty()) {
        List<MedDescription> list = (List<MedDescription>)super.getSheetService().getMedicationDescriptions(medNdcs).getCollection();
        for (MedDescription item : list) {
          String ndc = item.getNdc();
          ndc = StringUtils.deleteChar(ndc, '-').trim();
          medDescriptionsHT.put(ndc, item);
        }
      }
      
      for (MedicationRdv med : remoteVaMedications) {
        
        String dateLastFilled;
        try {
          Date dt = DateUtils.convertDateStr(med.getLastFillDate(), "mm/dd/yyyy");
          dateLastFilled = DateUtils.formatDate(dt, "MMMM dd, yyyy");
        } catch(Exception e) {
          dateLastFilled = super.getStringResource("na");
        }              
        
        String lastReleaseDate = null;
        String ndc = null;
        String totalNumRefills = null;
        if ((med.getComment() != null) && !med.getComment().isEmpty()) {
          ndc = StringUtils.piece(med.getComment(), 1);
          ndc = StringUtils.deleteChar(ndc, '-').trim();
          lastReleaseDate = StringUtils.piece(med.getComment(), 2);
          totalNumRefills = StringUtils.piece(med.getComment(), 3);
        }
        
        MedicationJson rmJson = null;
        if (!this.formatAsHtml) {
          rmJson = new MedicationJson();
          remoteVaMedsList.add(rmJson);
          rmJson.setId(med.getRxNumber());
          rmJson.setName(med.getName());
          rmJson.setType("VA");
          rmJson.setSite(super.getEncounterInfo().getFacilityName());
          rmJson.setStationNo(super.getEncounterInfo().getFacilityNo());
          rmJson.setSig(SIG_PATTERN.matcher(med.getSig()).replaceAll(""));
          
          try {
            Date dt = DateUtils.convertDateStr(med.getExpirationDate(), "mm/dd/yyyy");
            rmJson.setDateExpires(DateUtils.formatDate(dt, "MMMM dd, yyyy"));
          } catch(Exception e) {
            rmJson.setDateExpires(super.getStringResource("na"));
          }
          
          if ((lastReleaseDate != null) &&!lastReleaseDate.isEmpty()) {
            rmJson.setDateLastReleased(lastReleaseDate);
          } else {
            if (!dateLastFilled.isEmpty()) {
              String s = super.getStringResource("requestedNotReleased");
              s = s.replaceAll("%LAST_FILL_DATE%", med.getLastFillDate());
              rmJson.setDateLastReleased(s);
            }
          }
          
          rmJson.setDateLastFilled(dateLastFilled);
          rmJson.setSite(med.getStationName());
          rmJson.setProvider(med.getProvider());
          rmJson.setTotalNumRefills(StringUtils.toInt(totalNumRefills, 0));
          rmJson.setRefillsRemaining(StringUtils.toInt(med.getRefills(), 0));
          rmJson.setStatus(med.getStatus());
          
        } else {
          
          body.append("<div class=\"med-name\">");
          if (super.docType.equals(EncounterInfo.PVS)) {
            body.append("<input type=\"checkbox\">&nbsp;&nbsp;");
          }
          body.append(StringUtils.mixedCase(med.getName()))
            .append("</div>\n");          

          body.append("<div class=\"med-detail\">")
            .append(SIG_PATTERN.matcher(med.getSig()).replaceAll(""))
            .append("</div>\n");       

          body.append("<div class=\"med-detail\">");
          
          /*
          if ((totalNumRefills != null) &&!totalNumRefills.isEmpty()) {
            body.append(super.getStringResource("refillsRemaining") + ": ")
            .append(totalNumRefills);
          }          
          */          
          
          body.append(super.getStringResource("refills") + ": ")
          .append(med.getRefills());
          
          if ((lastReleaseDate != null) &&!lastReleaseDate.isEmpty()) {
            body.append("&nbsp;&nbsp;&nbsp;")
            .append(super.getStringResource("lastReleased") + ": ")
            .append(lastReleaseDate);
          } else {
            if (!dateLastFilled.isEmpty()) {
              String s = super.getStringResource("requestedNotReleased");
              s = s.replaceAll("%LAST_FILL_DATE%", med.getLastFillDate());
              body.append("&nbsp;&nbsp;&nbsp;")
              .append(super.getStringResource("lastReleased") + ": ")
              .append(s);
            }
          }
          
          if (!med.getExpirationDate().isEmpty()) {
            body.append("&nbsp;&nbsp;&nbsp;")
            .append(super.getStringResource("expires") + ": ")
            .append(med.getExpirationDate());
          }
          if (super.getEncounterInfo().getDocType().equals(EncounterInfo.PVS)) {
            body.append("&nbsp;&nbsp;&nbsp;")
            .append(super.getStringResource("status") + ": ")
            .append(med.getStatus());
          }
          body.append("</div>\n");
        }
        
        StringBuffer description = new StringBuffer();
        if (!ndc.isEmpty()) {
          if (!this.formatAsHtml) {
            rmJson.setNdc(ndc);
          }
          MedDescription medDescription = medDescriptionsHT.get(ndc);
          if (medDescription == null) {
            continue;
          }
          if (!medDescription.getShape().isEmpty() && !medDescription.getShape().equals("null")) {
            description.append(medDescription.getShape().toLowerCase());
            if (!medDescription.getColor().isEmpty() && !medDescription.getColor().equals("null")) {
                description.append(", ")
                .append(medDescription.getColor().toLowerCase());
            }
            if (!medDescription.getFrontImprint().isEmpty() || !medDescription.getFrontImprint().isEmpty()) {
              description.append(", ")
              .append(super.getStringResource("imprintedWith"))
              .append(" ");
              if (!medDescription.getFrontImprint().isEmpty()) {
                description.append(medDescription.getFrontImprint());
              }
              if (!medDescription.getBackImprint().isEmpty()) {
                 description.append(", ")
                .append(medDescription.getBackImprint());
              }
            }
          }
        }
          
        if (!description.toString().isEmpty()) {
          if (this.formatAsHtml) {
            body.append("<div class=\"med-detail\">")
           .append(super.getStringResource("description") + ": ")
           .append(description.toString())
           .append("</div>\n");
          } else {
            rmJson.setDescription(description.toString());
          }
        }
        
      }    
    } finally {
      if (this.formatAsHtml) {
        super.setContent("remote-va_medications", body.toString()); 
      } else {
        super.setData("remote-va_medications", remoteVaMedsList);
      }
    }
  }
  
}
