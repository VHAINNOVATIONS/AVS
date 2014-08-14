package gov.va.med.lom.avs.dao.hibernate;

import java.util.*;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.Query;

import gov.va.med.lom.jpa.foundation.dao.impl.BaseEntityDaoJpa;

import gov.va.med.lom.avs.dao.VhaSitesDao;
import gov.va.med.lom.avs.model.VhaSite;

@Stateless(name="gov.va.med.lom.avs.dao.VhaSitesDao")
@Local(VhaSitesDao.class)
public class VhaSitesDaoImpl extends BaseEntityDaoJpa<VhaSite, Long> implements VhaSitesDao {
  
  public List<VhaSite> findByStation(String stationNo) {
    
    Query query = entityManager.createNamedQuery(QRY_FIND_BY_STATION);
    query.setParameter("stationNo", stationNo);
  
    return query.getResultList();
    
  }
 
  public VhaSite findByStationAndProtocol(String stationNo, String protocol) {
    
    Query query = entityManager.createNamedQuery(QRY_FIND_BY_STATION_PROTOCOL);
    query.setParameter("stationNo", stationNo);
    query.setParameter("protocol", protocol);
    
    List<VhaSite> resultList = query.getResultList();  
    if (resultList != null && !resultList.isEmpty()) { 
      return resultList.get(0);
    } else {
      return null;
    }           

  }
  
}

