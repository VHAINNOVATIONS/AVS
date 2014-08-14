package gov.va.med.lom.avs.dao;

import java.util.List;

import gov.va.med.lom.avs.model.VhaSite;
import gov.va.med.lom.jpa.foundation.dao.BaseEntityDao;

public interface VhaSitesDao extends BaseEntityDao<VhaSite, Long> {
 
  public static final String QRY_FIND_BY_STATION = "sites.findByStation";
  public static final String QRY_FIND_BY_STATION_PROTOCOL = "sites.findByStationAndProtocol";
  
  public abstract List<VhaSite> findByStation(String stationNo);
  public abstract VhaSite findByStationAndProtocol(String stationNo, String protocol);
  
}

