package gov.va.med.lom.avs.dao;

import java.util.List;

import gov.va.med.lom.jpa.foundation.dao.BaseEntityDao;

import gov.va.med.lom.avs.model.PvsClinicalReminder;

public interface PvsClinicalRemindersDao extends BaseEntityDao<PvsClinicalReminder, Long> {
 
  public static final String QRY_FIND_BY_STATION = "pvs.findClinicalRemindersByStationNo";
  
  public abstract List<PvsClinicalReminder> findByStationNo(String stationNo);
  
}

