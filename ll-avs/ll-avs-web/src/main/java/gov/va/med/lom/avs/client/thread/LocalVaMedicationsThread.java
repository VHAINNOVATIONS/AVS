package gov.va.med.lom.avs.client.thread;

import java.util.Collection;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.regex.Pattern;

import gov.va.med.lom.foundation.service.response.CollectionServiceResponse;

import gov.va.med.lom.javaUtils.misc.DateUtils;
import gov.va.med.lom.javaUtils.misc.StringUtils;

import gov.va.med.lom.vistabroker.patient.data.Medication;

import gov.va.med.lom.avs.model.MedDescription;
import gov.va.med.lom.avs.model.EncounterInfo;
import gov.va.med.lom.avs.client.model.MedicationJson;

public class LocalVaMedicationsThread extends SheetDataThread {

  static final Pattern SIG_PATTERN = Pattern.compile("^ *Sig: *");
  static final int CLINIC_MEDS = 0;
  static final int OUTPATIENT_MEDS = 1;
  
  public void run() {
    
    List<Object> results = new ArrayList<Object>();
    try {
      CollectionServiceResponse<Medication> response = super.getSheetService()
          .getMedications(super.securityContext, super.getEncounterInfo());
      Collection<Medication> localVaMedications = response.getCollection();
      
      List<String> medNdcs = new ArrayList<String>();
      for (Medication med : localVaMedications) {
        if (med.getPharmId() != null) {
          String ndc = StringUtils.piece(med.getPharmId(), 2);
          if (!ndc.isEmpty()) {
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
          ndc = StringUtils.deleteChar(ndc, '-');
          medDescriptionsHT.put(ndc, item);
        }
      }
  
      List<Medication> clinicMeds = new ArrayList<Medication>();
      List<Medication> outpatientMeds = new ArrayList<Medication>();
      for (Medication med : localVaMedications) {
        if ((med.getType() != null) && (med.getType().equals("C"))) {
          clinicMeds.add(med);
        } else {
          outpatientMeds.add(med);
        }
      }
      
      List<Medication> medications = null;
      for (int i = 0; i <= 1; i++) {
        
        List<MedicationJson> medsJson = new ArrayList<MedicationJson>();
        String type = null;
        if (i == CLINIC_MEDS) {
          medications = clinicMeds;
          type = "Clinic";
        } else if (i == OUTPATIENT_MEDS) {
          medications = outpatientMeds;
          type = "Outpatient";
        }
        
        StringBuffer body = new StringBuffer();
        
        if (medications.size() == 0) {
          body.append(super.getStringResource("none"));
          
        } else {
          
          if (i == OUTPATIENT_MEDS) {
            body.append("<div class=\"med-instructions\">");
            if (super.docType.equals(EncounterInfo.AVS)) {
              body.append(super.getStringResource("avsMedsInstructions"));
            } else {
              body.append(super.getStringResource("pvsMedsInstructions"));
            }
            body.append("</div>");
          }
          
          for (Medication med : medications) {
            MedicationJson medJson = new MedicationJson();
            medsJson.add(medJson);
            medJson.setSite(super.getEncounterInfo().getFacilityName());
            medJson.setStationNo(super.getEncounterInfo().getFacilityNo());
            medJson.setType(type);
            
            String dateExpires;
            try {
              dateExpires = DateUtils.formatDate(med.getDateExpires(), "MMMM dd, yyyy");
              medJson.setDateExpires(DateUtils.formatDate(med.getDateExpires(), "MM/dd/yyyy"));
            } catch(Exception e) {
              dateExpires = super.getStringResource("na");
              medJson.setDateExpires(dateExpires);
            }
            
            String dateLastFilled;
            try {
              dateLastFilled = DateUtils.formatDate(med.getDateLastFilled(), "MMMM dd, yyyy");
              medJson.setDateLastFilled(DateUtils.formatDate(med.getDateLastFilled(), "MM/dd/yyyy"));
            } catch(Exception e) {
              dateLastFilled = super.getStringResource("na");
              medJson.setDateLastFilled(dateLastFilled);
            }    
            
            body.append("<div class=\"med-name\">");
            if (super.docType.equals(EncounterInfo.PVS)) {
              body.append("<input type=\"checkbox\">&nbsp;&nbsp;");
            }
            body.append(StringUtils.mixedCase(med.getName()))
              .append("</div>\n");
            medJson.setName(med.getName());
    
            String sig = SIG_PATTERN.matcher(med.getSig()).replaceAll("");
            body.append("<div class=\"med-detail\">")
              .append(sig)
              .append("</div>\n");    
            medJson.setSig(sig);
            
            String ndc = null;
            String lastReleaseDate = null;
            String totalNumRefills = null;
            if (med.getPharmId() != null) {
              ndc = StringUtils.piece(med.getPharmId(), 2);
              ndc = StringUtils.deleteChar(ndc, '-').trim();
              lastReleaseDate = StringUtils.piece(med.getPharmId(), 3);
              totalNumRefills = StringUtils.piece(med.getPharmId(), 4);
              medJson.setNdc(ndc);
            }
           
            if (i == OUTPATIENT_MEDS) {
              body.append("<div class=\"med-detail\">");
              
              if ((totalNumRefills != null) && !totalNumRefills.isEmpty()) {
                //body.append(super.getStringResource("totalRefills") + ": ")
                //.append(totalNumRefills);
                medJson.setTotalNumRefills(StringUtils.toInt(totalNumRefills, 0));                
              }
              
              body.append(super.getStringResource("refills") + ": ")
              .append(med.getRefills());
              medJson.setRefillsRemaining(med.getRefills());
              
              if ((lastReleaseDate != null) &&!lastReleaseDate.isEmpty()) {
                body.append("&nbsp;&nbsp;&nbsp;")
                .append(super.getStringResource("lastReleased") + ": ")
                .append(lastReleaseDate);
                try {
                  medJson.setDateLastReleased(DateUtils.formatDate(DateUtils.toDate(lastReleaseDate, "MMMM dd, yyyy"), "MM/dd/yyyy"));
                } catch(Exception e) {}
              } else {
                if (!dateLastFilled.isEmpty()) {
                  String s = super.getStringResource("requestedNotReleased");
                  s = s.replaceAll("%LAST_FILL_DATE%", dateLastFilled);
                  body.append("&nbsp;&nbsp;&nbsp;")
                  .append(super.getStringResource("lastReleased") + ": ")
                  .append(s);
                }
              }
              if (!dateExpires.isEmpty()) {
                body.append("&nbsp;&nbsp;&nbsp;")
                .append(super.getStringResource("expires") + ": ")
                .append(dateExpires);
              }
              if (super.getEncounterInfo().getDocType().equals(EncounterInfo.PVS)) {
                body.append("&nbsp;&nbsp;&nbsp;")
                .append(super.getStringResource("status") + ": ")
                .append(med.getStatus());
              }
              medJson.setStatus(med.getStatus());
              body.append("</div>\n");
            }
            
            // Med description
            if (!ndc.isEmpty()) {
              MedDescription medDescription = medDescriptionsHT.get(ndc);
              if (medDescription == null) {
                continue;
              }
              if (!medDescription.getShape().isEmpty() && !medDescription.getShape().equals("null")) {
                StringBuffer sb = new StringBuffer();
                body.append("<div class=\"med-detail\">");
                body.append(super.getStringResource("description") + ": ")
                .append(medDescription.getShape().toLowerCase());
                sb.append(medDescription.getShape().toLowerCase());
                if (!medDescription.getColor().isEmpty() && !medDescription.getColor().equals("null")) {
                    body.append(", ")
                    .append(medDescription.getColor().toLowerCase());
                    sb.append(", ");
                    sb.append(medDescription.getColor().toLowerCase());
                }
                if (!medDescription.getFrontImprint().isEmpty() || !medDescription.getFrontImprint().isEmpty()) {
                  body.append(", ")
                  .append(super.getStringResource("imprintedWith"))
                  .append(" ");
                  sb.append(", imprinted with ");
                  if (!medDescription.getFrontImprint().isEmpty()) {
                    body.append(medDescription.getFrontImprint());
                    sb.append(medDescription.getFrontImprint());
                  }
                  if (!medDescription.getBackImprint().isEmpty()) {
                     body.append(", ")
                    .append(medDescription.getBackImprint());
                     sb.append(", ");
                     sb.append(medDescription.getBackImprint());
                  }
                }
                body.append("</div>\n");
                medJson.setDescription(sb.toString());
              }
            }
          }
        }
        
        String content = null;
        if (i == CLINIC_MEDS) {
          String sectionClass = null;
          if (medications.size() == 0) {
            sectionClass = "section-hidden";
          } else {
            sectionClass = "section";
          }
          content = TPL_SECTION
              .replace("__SECTION_CLASS__", sectionClass)
              .replace("__SECTION_ID_SUFFIX__", "clinic_medications")
              .replace("__SECTION_TITLE__", super.getStringResource("clinicMeds")) 
              .replace("__CONTENTS__", body.toString());
        } else if (i == OUTPATIENT_MEDS) {
          content = TPL_SECTION
              .replace("__SECTION_CLASS__", "section")
              .replace("__SECTION_ID_SUFFIX__", "va_medications")
              .replace("__SECTION_TITLE__", super.getStringResource("vaMeds"))
              .replace("__CONTENTS__", body.toString());          
        }
        results.add(content);
        results.add(medsJson);
      }
    } finally {
      setData("va_medications", results); 
    }    
    
  }
  
}
