package gov.va.med.lom.avs.client.thread;

import java.util.List;
import java.util.Collection;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.TreeSet;

import gov.va.med.lom.foundation.service.response.CollectionServiceResponse;

import gov.va.med.lom.javaUtils.misc.DateUtils;

import gov.va.med.lom.vistabroker.patient.data.VitalSignMeasurement;
import gov.va.med.lom.vistabroker.util.FMDateUtils;

import gov.va.med.lom.avs.client.model.VitalSignJson;
import gov.va.med.lom.avs.web.util.AvsWebUtils;

public class VitalsThread extends SheetDataThread {

  private HashMap<String, String> vitalSignMap;
  
  public void run() {
    
    vitalSignMap = new HashMap<String, String>();
    vitalSignMap.put("BMI", super.getStringResource("bmi"));
    vitalSignMap.put("BP",  super.getStringResource("bp"));
    vitalSignMap.put("CG",  super.getStringResource("cg"));
    vitalSignMap.put("CVP", super.getStringResource("cvp"));
    vitalSignMap.put("HT",  super.getStringResource("ht"));
    vitalSignMap.put("P",   super.getStringResource("p"));
    vitalSignMap.put("PN",  super.getStringResource("pn"));    
    vitalSignMap.put("POX", super.getStringResource("pox"));
    vitalSignMap.put("R",   super.getStringResource("r"));
    vitalSignMap.put("T",   super.getStringResource("t"));
    vitalSignMap.put("WT",  super.getStringResource("wt"));
    
    StringBuffer body = new StringBuffer();
    List<VitalSignJson> vitalsList = new ArrayList<VitalSignJson>();
    try {
      
      CollectionServiceResponse<VitalSignMeasurement> response = super.getSheetService()
          .getVitals(super.securityContext, super.getEncounterInfo());
      Collection<VitalSignMeasurement> vitals = response.getCollection();  
      
      if (vitals.size() == 0) {
        body.append(super.getStringResource("noMeasurements"));
  
      } else {
  
        Calendar visitDateCal = Calendar.getInstance();
        visitDateCal.setTime(FMDateUtils.fmDateTimeToDate(super.getLatestEncounter().getEncounterDatetime()));
        
        TreeSet<String> list = new TreeSet<String>();
  
        for (VitalSignMeasurement sign : vitals) {
  
          Calendar signDateCal = Calendar.getInstance();
          signDateCal.setTime(sign.getDate());
          
          boolean sameDay = visitDateCal.get(Calendar.YEAR) == signDateCal.get(Calendar.YEAR) &&
              visitDateCal.get(Calendar.DAY_OF_YEAR) == signDateCal.get(Calendar.DAY_OF_YEAR);
          
          Date signDate = null;
          if (!sameDay) {
            signDate = sign.getDate();
          }
          
          String typeLabel = this.translateVitalSignCode(sign.getTypeAbbr());
  
          VitalSignJson vitalSign = new VitalSignJson();
          vitalSign.setType(typeLabel);
          vitalSign.setValue(sign.getEnglishValue());
          vitalSign.setQualifiers(AvsWebUtils.adjustCapitalization(sign.getQualifiers()));
          try {
            vitalSign.setDate(DateUtils.toDateTimeStr(sign.getDate(), "MMM dd, yyyy"));
          } catch(Exception e ) {}
          vitalsList.add(vitalSign);
          
          if (sign.getQualifiers().isEmpty()) {
            if (signDate == null) {
              list.add(String.format("%s: %s", typeLabel, sign.getEnglishValue()));
            } else {
              try {
                list.add(String.format("%s: %s (%s)", typeLabel, sign.getEnglishValue(),
                    vitalSign.getDate()));
              } catch(Exception e) {}
            }
          } else {
            if (signDate == null) {
              list.add(String.format("%s: %s (%s)",
                  typeLabel, sign.getEnglishValue(), vitalSign.getQualifiers()));
            } else {
              try {
                list.add(String.format("%s: %s (%s) (%s)",
                    typeLabel, sign.getEnglishValue(),
                    vitalSign.getQualifiers(),
                    vitalSign.getDate()));
              } catch(Exception e) {}
            }
          }
        }
  
        ArrayList<String> items = new ArrayList<String>(list);
        body.append(AvsWebUtils.renderTwoColumnList(items, super.media == MEDIA_PDF, this.fontClass));
      }
    } finally {
      String content = TPL_SECTION
        .replace("__SECTION_CLASS__", "section")
        .replace("__SECTION_ID_SUFFIX__", "vitals")
        .replace("__SECTION_TITLE__", super.getStringResource("vitals"))
        .replace("__CONTENTS__", body.toString());
      
      setContentData("vitals", content, vitalsList);
    }    
    
  }
  
  private String translateVitalSignCode(String code) {
    String output = this.vitalSignMap.get(code);
    if (output == null) {
      output = code;
    }
    
    return output;
  }
  
}
