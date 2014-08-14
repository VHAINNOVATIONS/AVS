package gov.va.med.lom.avs.client.thread;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.TreeSet;

import gov.va.med.lom.foundation.service.response.ServiceResponse;
import gov.va.med.lom.javaUtils.misc.DateUtils;
import gov.va.med.lom.javaUtils.misc.StringUtils;

import gov.va.med.lom.vistabroker.patient.data.AllergyReactant;
import gov.va.med.lom.vistabroker.patient.data.AllergiesReactants;

import gov.va.med.lom.avs.client.model.AllergiesReactionsJson;
import gov.va.med.lom.avs.client.model.AllergyJson;
import gov.va.med.lom.avs.web.util.AvsWebUtils;


public class AllergiesThread extends SheetDataThread {

  public void run() {
    
    StringBuffer body = new StringBuffer();
    AllergiesReactionsJson allergiesReactionsJson = new AllergiesReactionsJson();
    
    try {
      ServiceResponse<AllergiesReactants> response = super.getSheetService()
          .getAllergies(super.securityContext, super.getEncounterInfo());
      AllergiesReactants localAllergiesReactions = response.getPayload();
      
      response = super.getSheetService().getRemoteAllergies(super.securityContext, super.getEncounterInfo());
      AllergiesReactants remoteAllergiesReactions = response.getPayload();
      
      List<AllergyReactant> localAllergies = localAllergiesReactions.getAllergiesReactants();
      List<AllergyReactant> remoteAllergies = remoteAllergiesReactions.getAllergiesReactants();
      
      if ((localAllergiesReactions.getNoAllergyAssessment() == true) &&
          ((remoteAllergies == null) || (remoteAllergies.size() == 0) || 
              (remoteAllergiesReactions.getNoAllergyAssessment() == true))) {
        body.append(super.getStringResource("noAllergyAssessment"));
        allergiesReactionsJson.setNoAllergyAssessment(true);
        
      } else if ((localAllergiesReactions.getNoKnownAllergies() == true) &&
                 ((remoteAllergies == null) ||  (remoteAllergies.size() == 0) ||
                     (remoteAllergiesReactions.getNoKnownAllergies() == true))) {
        body.append(super.getStringResource("noKnownAllergies"));
        allergiesReactionsJson.setNoKnownAllergies(true);
        
      } else {
        
        Hashtable<String, AllergyReactant> allHT = new Hashtable<String, AllergyReactant>();
        for (AllergyReactant allergyReactant : localAllergies) {
          allHT.put(allergyReactant.getAllergenReactant(), allergyReactant);
        }
        if (remoteAllergies != null) {
          for (AllergyReactant allergyReactant : remoteAllergies) {
            allHT.put(allergyReactant.getAllergenReactant(), allergyReactant);
          }
        }
        
        Enumeration<AllergyReactant> en = allHT.elements();
        
        TreeSet<String> items = new TreeSet<String>();
        while (en.hasMoreElements()) {
          AllergyReactant allergy = en.nextElement();
          String allergen = StringUtils.piece(allergy.getAllergenReactant().replace("/", " / "), 1);
          String site = StringUtils.piece(allergy.getAllergenReactant(), 2);
          String stationNo = StringUtils.piece(allergy.getAllergenReactant(), 3);
          String type = StringUtils.piece(allergy.getAllergenReactant(), 4);
          if (type.equals("D")) {
            type = "Drug";
          } else if (type.equals("F")) {
            type = "Food";
          } else if (type.equals("O")) {
            type = "Other";
          } else {
            type = "";
          }
          String s = StringUtils.piece(allergy.getAllergenReactant(), 5);
          String verifiedDate = null;
          if (!s.isEmpty()) {
            try {
              verifiedDate = DateUtils.toDateTimeStr(DateUtils.fmDateTimeToDateTime(StringUtils.toDouble(s, 0)).getTime(), "MM/dd/yyyy");
            } catch(Exception e) {
              verifiedDate = "";
            }
          } else {
            verifiedDate = "";
          }
          if (site.isEmpty() && (remoteAllergies != null) && (remoteAllergies.size() > 0)) {
            site = super.getEncounterInfo().getFacilityName();
            stationNo = super.getEncounterInfo().getFacilityNo();
          }
          StringBuffer description = new StringBuffer("<b>");
          description.append(AvsWebUtils.adjustCapitalization(allergen));
          description.append("</b>");
          String[] reactions = allergy.getReactionsSymptoms();
          
          AllergyJson allergyJson = new AllergyJson();
          allergyJson.setAllergen(allergen);
          allergyJson.setSite(site);
          allergyJson.setStationNo(stationNo);
          allergyJson.setSeverity(allergy.getSeverity());
          allergyJson.setReactions(Arrays.asList(reactions));
          allergyJson.setType(type);
          allergyJson.setVerifiedDate(verifiedDate);
          allergiesReactionsJson.getAllergies().add(allergyJson);
          
          if (reactions.length > 0) {
            if (!reactions[0].trim().isEmpty()) {
              description.append(" (");
              description.append(AvsWebUtils.adjustCapitalization(AvsWebUtils.delimitString(reactions, true)));
              description.append(")");
            }
          }
          /*
          if (!type.isEmpty()) {
            description.append("<br/>" + super.getStringResource("allergyType") + ": ");
            description.append(type);
          }       
          if (!verifiedDate.isEmpty()) {
            description.append("<br/>" + super.getStringResource("allergyVerified") + ": ");
            description.append(verifiedDate);
          }
          */            
          if (!site.isEmpty()) {
            description.append("<br/>" + super.getStringResource("documentingFacility") + ": ");
            description.append(site);
          }
          items.add(description.toString());
        }
        
        body.append(AvsWebUtils.renderTwoColumnList(new ArrayList<String>(items), super.media == MEDIA_PDF, super.fontClass));
      }
    } finally {
      String content = TPL_SECTION
        .replace("__SECTION_CLASS__", "section")
        .replace("__SECTION_ID_SUFFIX__", "allergies")
        .replace("__SECTION_TITLE__", super.getStringResource("allergiesReactions"))
        .replace("__CONTENTS__", body.toString());
      
      setContentData("allergies", content, allergiesReactionsJson);
    }    
    
  }
  
}
