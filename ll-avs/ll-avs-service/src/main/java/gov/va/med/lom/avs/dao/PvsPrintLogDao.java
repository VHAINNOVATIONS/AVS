package gov.va.med.lom.avs.dao;

import java.util.List;

import gov.va.med.lom.jpa.foundation.dao.BaseEntityDao;

import gov.va.med.lom.avs.model.PvsPrintLog;

public interface PvsPrintLogDao extends BaseEntityDao<PvsPrintLog, Long> {
 
  public static final String QRY_FIND_BY_DATE_CLINIC = "pvs.findByDateClinic";
  
  public abstract List<PvsPrintLog> findByDateAndClinic(String date, long clinicId);
  
}

