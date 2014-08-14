package gov.va.med.lom.avs.client.thread;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;

import gov.va.med.lom.foundation.service.response.CollectionServiceResponse;

import gov.va.med.lom.javaUtils.misc.QuickSort;
import gov.va.med.lom.javaUtils.misc.QSCallBack;
import gov.va.med.lom.vistabroker.util.FMDateUtils;

import gov.va.med.lom.avs.client.model.ClinicVisitedJson;
import gov.va.med.lom.avs.web.util.AvsWebUtils;
import gov.va.med.lom.avs.model.Encounter;
import gov.va.med.lom.avs.model.EncounterProvider;
import gov.va.med.lom.avs.enumeration.TranslationTypeEnum;

public class ClinicsVisitedThread extends SheetDataThread implements QSCallBack {

  public void run() {
    
    StringBuffer body = new StringBuffer();
    List<ClinicVisitedJson> clinicsVisited = new ArrayList<ClinicVisitedJson>();
    try {
      // sort encounters by date/time
      List<Encounter> sortedEncounters = new ArrayList<Encounter>();
      for (Encounter encounter : super.encounterCache.getEncounters()) {
        sortedEncounters.add(encounter);
      }
      QuickSort quickSort = new QuickSort();
      quickSort.setQSCallBack(this);
      quickSort.quickSort(sortedEncounters);
      
      ArrayList<String> clinicNames = new ArrayList<String>();
      for (Encounter encounter : sortedEncounters) {
        clinicNames.add(encounter.getLocation().getLocationName());
      }      
      List<String> clinicNameTranslations = null;
      try {
        CollectionServiceResponse<String> translationsResponse = super.getSettingsService()
          .translateStrings(super.facilityNo, super.getLanguage(), TranslationTypeEnum.LOCATION_NAME, clinicNames);
        AvsWebUtils.handleServiceErrors(translationsResponse, log);
        clinicNameTranslations = new ArrayList<String>(translationsResponse.getCollection());
      } catch(Exception e) {
        e.printStackTrace();
        clinicNameTranslations = new ArrayList<String>();
      }
  
      List<String> items = new ArrayList<String>();
      SimpleDateFormat dateFormatter = new SimpleDateFormat("MM/dd/yyyy");
      SimpleDateFormat timeFormatter = new SimpleDateFormat("HH:mm");
      Integer index = 0;
      for (Encounter encounter : sortedEncounters) {
        ClinicVisitedJson clinicVisited = new ClinicVisitedJson();
        String clinic = clinicNameTranslations.get(index);
        index++;
  
        if ((clinic == null) || clinic.isEmpty()) {
          clinic = super.getStringResource("unknownClinic");
        }

        StringBuffer description = new StringBuffer(clinic);
        StringBuffer sb = new StringBuffer();
        int i = 0;
        for (EncounterProvider provider : encounter.getProviders()) {
          if (provider.getProviderName() != null && !provider.getProviderName().isEmpty()) {
            if (i > 0) {
              sb.append("; ");
              description.append("<br/>");
            }
            sb.append(provider.getProviderName());
            description.append(" / ");
            description.append(provider.getProviderName());
            i++;
          }
        }
        clinicVisited.setProvider(sb.toString());
  
        StringBuffer fullDescription = new StringBuffer();
        if (encounter.getEncounterDatetime() > 0) {
          Date date = FMDateUtils.fmDateTimeToDate(encounter.getEncounterDatetime());
          clinicVisited.setDate(dateFormatter.format(date));
          String time = timeFormatter.format(date);
          clinicVisited.setTime(time);
          fullDescription.append(time).append(" - ");
        }
        fullDescription.append(description.toString().trim());
        clinicVisited.setSite(super.getEncounterInfo().getFacilityName());
        clinicVisited.setClinic(clinic);
        clinicsVisited.add(clinicVisited);
        items.add(fullDescription.toString().trim());
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
      
      setContentData("clinicsVisited", content, clinicsVisited);
    }    
    
  }
  
  // QuickSort callback
  public int compare(Object obj1, Object obj2) {
    return (int)(((Encounter)obj1).getEncounterDatetime() - ((Encounter)obj2).getEncounterDatetime());
  }  
  
}
