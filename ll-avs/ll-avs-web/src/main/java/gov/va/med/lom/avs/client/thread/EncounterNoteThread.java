package gov.va.med.lom.avs.client.thread;

import java.util.List;
import java.util.Calendar;
import java.util.Date;
import java.text.SimpleDateFormat;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import gov.va.med.lom.foundation.service.response.CollectionServiceResponse;

import gov.va.med.lom.javaUtils.misc.StringUtils;
import gov.va.med.lom.vistabroker.security.ISecurityContext;
import gov.va.med.lom.vistabroker.service.DdrVBService;
import gov.va.med.lom.vistabroker.service.PatientVBService;
import gov.va.med.lom.vistabroker.util.FMDateUtils;
import gov.va.med.lom.vistabroker.util.VistaBrokerServiceFactory;

import gov.va.med.lom.avs.model.EncounterCacheMongo;
import gov.va.med.lom.avs.model.Encounter;
import gov.va.med.lom.avs.model.EncounterInfo;
import gov.va.med.lom.avs.service.ServiceFactory;
import gov.va.med.lom.avs.service.SheetService;
import gov.va.med.lom.avs.util.AvsUtils;

public class EncounterNoteThread extends Thread {

  private SheetService sheetService;
  private ISecurityContext securityContext;
  private EncounterInfo encounterInfo;
  private DdrVBService ddrVBService;
  private PatientVBService patientVBService;
  
  private static final Log log = LogFactory.getLog(EncounterNoteThread.class);
  
  public EncounterNoteThread() {}
    
  public EncounterNoteThread(ISecurityContext securityContext, EncounterInfo encounterInfo) {
    this.securityContext = securityContext;
    this.encounterInfo = encounterInfo;
  }
  
  public void run() {
    try {
      this.sheetService = ServiceFactory.getSheetService();
      this.ddrVBService = VistaBrokerServiceFactory.getDdrVBService();
      this.patientVBService = VistaBrokerServiceFactory.getPatientVBService();
      
      if (encounterInfo.getEncounterCache() != null) {      
        boolean haveEncounterIen = false;
        List<Encounter> encounters = encounterInfo.getEncounterCache().getEncounters();
        if (encounters.size() > 0) {
          for (Encounter encounter : encounters) {
            haveEncounterIen = haveEncounterIen || (encounter.getEncounterNoteIen() != null);
          }
        }
        if (haveEncounterIen) { 
          return;
        }
        ISecurityContext ddrSecCtx = this.sheetService.getServiceSecurityContext(encounterInfo);
        for (Encounter encounter : encounters) {
          if (encounter.getEncounterNoteIen() == null) {
            CollectionServiceResponse<String> csr = ddrVBService.execDdrLister(ddrSecCtx, "8925", null, ".01;.05;.07;1202;1211;.02", 
                "IP", 10, AvsUtils.adjustForNumericSearch(encounterInfo.getPatientDfn()), encounterInfo.getPatientDfn(), 
                "C", "I $P($G(^(0)),U,7)=" + encounter.getEncounterDatetime(), null, null, null, null);
            List<String> list = (List<String>)csr.getCollection();
            for (String x : list) {
              String dfn = StringUtils.piece(x, 7);
              if (!dfn.equals(encounterInfo.getPatientDfn())) {
                continue;
              }
              String noteIen = StringUtils.piece(x, 1);
              String fmDtTm = StringUtils.piece(x, 4);
              String locIen = StringUtils.piece(x, 6);
              // Truncate time to four digits
              String[] dtList = StringUtils.pieceList(fmDtTm, '.');
              if (dtList.length == 2) {
                if (dtList[1].length() > 4) {
                  dtList[1] = dtList[1].substring(0, 4);
                }
                fmDtTm = dtList[0] + "." + dtList[1];
              }
              
              try {
                Double.parseDouble(fmDtTm);
              } catch(NumberFormatException nfe) {
                // I don't know why date/time values in some visit strings
                // are formatted like this:  Thu Oct 03 07:52:31 MDT 2013
                // But need to handle these cases by converting to fm date/time
                try {
                  String mm = StringUtils.piece(fmDtTm, ' ', 2);
                  String dd = StringUtils.piece(fmDtTm, ' ', 3);
                  String tt = StringUtils.piece(fmDtTm, ' ', 4);
                  String hh = StringUtils.piece(tt, ':', 1);
                  String min = StringUtils.piece(tt, ':', 2);
                  String yy = StringUtils.piece(fmDtTm, ' ', 6);
                  String ds = mm + " " + dd + " " + yy + " " + hh + ":" + min; 
                  SimpleDateFormat dateFormatter = new SimpleDateFormat("MMM dd yyyy hh:mm");
                  Date dt = dateFormatter.parse(ds);
                  Calendar calendar = Calendar.getInstance();
                  calendar.setTime(dt);
                  double d = FMDateUtils.dateTimeToFMDateTime(calendar);
                  fmDtTm = String.valueOf(d);
                } catch(Exception e) {
                }
              }
              String tempFmDtTm = String.valueOf(encounter.getEncounterDatetime());
              if (tempFmDtTm.length() > fmDtTm.length()) {
                tempFmDtTm = tempFmDtTm.substring(0, fmDtTm.length());
              }
              if (encounter.getLocation().getLocationIen().equals(locIen) && tempFmDtTm.equals(fmDtTm)) {
                encounter.setEncounterNoteIen(noteIen);
                // check if note has any pce data associated with it
                boolean hasPceData = false;
                CollectionServiceResponse<String> pceDataCSR = this.patientVBService.getPCEDataForNote(securityContext, encounter.getEncounterNoteIen());
                List<String> pceResults = (List<String>)pceDataCSR.getCollection();
                for (String s : pceResults) {
                  if ((s.substring(0, 3).equals("PRV") && s.charAt(3) != '-') ||
                      (s.substring(0, 3).equals("POV") && s.charAt(3) != '-') ||
                      (s.substring(0, 3).equals("IMM") && s.charAt(3) != '-')) {
                    hasPceData = true;
                    break;
                  }
                }
                if (hasPceData) {
                  break;
                } else {
                  encounter.setEncounterNoteIen(null);
                }
              }
            }
          }
        }
      }
      this.saveEncounterNoteIens(encounterInfo.getEncounterCache());
    } catch(Exception e) {
      log.error("Error getting encounter note iens", e);
    }
  }
  
  private void saveEncounterNoteIens(EncounterCacheMongo encounterCache) {
    List<Encounter> encounters = encounterCache.getEncounters();
    for (Encounter encounter : encounters) {
      String ien = (encounter.getEncounterNoteIen() != null) ? encounter.getEncounterNoteIen() : "";
      try {
        this.sheetService.updateEncounterCacheMongoEncounterNoteIen(encounterCache, encounter.getVisitString(), ien);
      } catch(Exception e) {
        log.error("Error saving encounter note iens in encounter cache", e);
      }
    }
  }

}
