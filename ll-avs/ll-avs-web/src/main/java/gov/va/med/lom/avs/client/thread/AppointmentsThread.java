package gov.va.med.lom.avs.client.thread;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.LinkedHashMap;

import gov.va.med.lom.avs.client.model.AppointmentJson;
import gov.va.med.lom.avs.enumeration.TranslationTypeEnum;
import gov.va.med.lom.foundation.service.response.CollectionServiceResponse;
import gov.va.med.lom.javaUtils.misc.DateUtils;
import gov.va.med.lom.javaUtils.misc.StringUtils;

import gov.va.med.lom.vistabroker.patient.data.Appointment;
import gov.va.med.lom.vistabroker.util.FMDateUtils;


public class AppointmentsThread extends SheetDataThread {

  public void run() {
    
    List<AppointmentJson> apptsList = new ArrayList<AppointmentJson>();
    StringBuffer body = new StringBuffer();
    try {
      
      int upcomingAppointmentRange = this.getSettingsService().getUpcomingAppointmentsRange(this.facilityNo).getPayload();
      
      CollectionServiceResponse<LinkedHashMap<Double, LinkedHashMap<Double, Appointment>>> response = super.getSheetService()
          .getAppointments(super.securityContext, super.getEncounterInfo(), upcomingAppointmentRange);
      List<LinkedHashMap<Double, LinkedHashMap<Double, Appointment>>> allAppts = 
          (List<LinkedHashMap<Double, LinkedHashMap<Double, Appointment>>>)response.getCollection();
      
      LinkedHashMap<Double, LinkedHashMap<Double, Appointment>> scheduledAppts = allAppts.get(0);
      LinkedHashMap<Double, LinkedHashMap<Double, Appointment>> recallAppts = allAppts.get(1);
      
      if ((scheduledAppts.size() == 0) && (recallAppts.size() == 0)) {
        body.append(super.getStringResource("noAppointments").replace("__MONTHS__", "" + upcomingAppointmentRange));
      } else {
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
        // get clinic name translations from scheduled and recall appointment lists
        HashMap<String, String> clinicNameTranslations = new HashMap<String, String>();
        for (int i = 0; i < 2; i++) {
          LinkedHashMap<Double, LinkedHashMap<Double, Appointment>> appointments;
          if (i == 0) {
            appointments = scheduledAppts;
          } else {
            appointments = recallAppts;
          }
          ArrayList<String> clinicNames = new ArrayList<String>();
          for (Double fmDate : appointments.keySet()) {
            for (Double fmDateTime : appointments.get(fmDate).keySet()) {      
              String clinicName = appointments.get(fmDate).get(fmDateTime).getLocation();
              clinicNames.add(clinicName);
            }
          }
          CollectionServiceResponse<String> csr = super.getSettingsService()
            .translateStrings(super.facilityNo, super.getLanguage(), TranslationTypeEnum.LOCATION_NAME, clinicNames);
          List<String> translations = (List<String>)csr.getCollection();
          int j = 0;
          for (String clinic : clinicNames) {
            clinicNameTranslations.put(clinic.toUpperCase(), translations.get(j));
            j++;
          }
          
          if (appointments.size() > 0) {
            // scheduled appointments            
            if (i == 0) {
              body.append("<div class=\"appointment-category\">")
                .append(super.getStringResource("scheduledAppointments"))
                .append("</div>")
                .append("<div class=\"appointment-instructions\">")
                .append(super.getStringResource("appointments").replace("__MONTHS__", "" + upcomingAppointmentRange))
                .append(":</div>");        
            // recall appointments
            } else if (i == 1) {
              body.append("<div class=\"appointment-category\">")
                .append(super.getStringResource("recallAppointments"))
                .append("</div>")              
                .append("<div class=\"appointments-instructions\">")
                .append(super.getStringResource("recallAppointmentsInstructions"))
                .append("</div>");     
            }
            Integer index = 0;
            for (Double fmDate : appointments.keySet()) {
              String formattedDate = null;
              try {
                formattedDate = DateUtils.toDateTimeStr(FMDateUtils.fmDateTimeToDate(fmDate), "EEEE, MMMM dd, yyyy");
              } catch (ParseException e) {
                formattedDate = FMDateUtils.fmDateTimeToDate(fmDate).toString();
              }
              body.append("<div class=\"appointment-date\">")
                .append(formattedDate)
                .append("</div>\n");
              
              for (Double fmDateTime : appointments.get(fmDate).keySet()) {
                Appointment appointment = appointments.get(fmDate).get(fmDateTime);
                body.append("<div class=\"appointment-detail\">");
                AppointmentJson apptJson = new AppointmentJson();
                String clinicName = clinicNameTranslations.get(appointment.getLocation().toUpperCase());
                clinicName = (clinicName != null) ? clinicName : appointment.getLocation();
                apptJson.setLocation(clinicName);
                if (i == 0) {
                  body.append(formatter.format(FMDateUtils.fmDateTimeToDate(fmDateTime)))
                    .append(" - ");
                  apptJson.setType("Scheduled");
                } else {
                  apptJson.setType("Recall");
                }
                body.append(clinicName);
                
                try {
                  apptJson.setDatetime(DateUtils.toDateTimeStr(DateUtils.fmDateTimeToDateTime(appointment.getFmDatetime()).getTime(), "MM/dd/yyyy@HH:mm"));
                } catch (ParseException e) {
                  apptJson.setDatetime(FMDateUtils.fmDateTimeToDate(fmDate).toString());
                }
                apptJson.setFmDatetime(appointment.getFmDatetime());
                String institution = (this.pceDataList != null) && (this.pceDataList.size() > 0) && 
                    (this.pceDataList.get(0).getInstitutionName() != null) ?
                    this.pceDataList.get(0).getInstitutionName() : this.getVistaUser().getStation();
                String facility = StringUtils.piece(appointment.getHeader(), 1).replaceAll("%FACILITY_NAME%", institution);
                apptJson.setSite(facility);
                String stationNo = StringUtils.piece(appointment.getHeader(), 2);
                apptJson.setStationNo(stationNo);
                apptsList.add(apptJson);
                // display facility if remote appointment
                if (!facility.isEmpty() && (!stationNo.equals(super.stationNo))) {
                  body.append(" (")
                    .append(facility)
                    .append(")");
                }
                body.append("</div>\n");
                index++;
              }
            }
            body.append("<div>&nbsp;</div>");
          }
        }
      }
    } finally {
      String content = TPL_SECTION
        .replace("__SECTION_CLASS__", "section")
        .replace("__SECTION_ID_SUFFIX__", "appointments")
        .replace("__SECTION_TITLE__", super.getStringResource("upcomingAppointments"))
        .replace("__CONTENTS__", body.toString());
      
      setContentData("appointments", content, apptsList);
    }    
    
  }
  
}
