package gov.va.med.lom.avs.dao;

import java.util.List;

import gov.va.med.lom.jpa.foundation.dao.BaseEntityDao;

import gov.va.med.lom.avs.model.PvsClinic;

public interface PvsClinicDao extends BaseEntityDao<PvsClinic, Long> {
 
  public static final String QRY_FIND_BY_STATION = "pvs.findClinicsByStationNo";
  
  public abstract List<PvsClinic> findByStationNo(String stationNo);
  
}

