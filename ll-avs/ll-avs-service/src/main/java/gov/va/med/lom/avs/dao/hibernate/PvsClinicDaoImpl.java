package gov.va.med.lom.avs.dao.hibernate;

import java.util.List;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.Query;

import gov.va.med.lom.jpa.foundation.dao.impl.BaseEntityDaoJpa;

import gov.va.med.lom.avs.dao.PvsClinicDao;
import gov.va.med.lom.avs.model.PvsClinic;

@Stateless(name="gov.va.med.lom.avs.dao.PvsClinicDao")
@Local(PvsClinicDao.class)
@SuppressWarnings("unchecked")
public class PvsClinicDaoImpl extends BaseEntityDaoJpa<PvsClinic, Long> implements PvsClinicDao {
  
  public List<PvsClinic> findByStationNo(String stationNo) {
    
    Query query = entityManager.createNamedQuery(QRY_FIND_BY_STATION);
    query.setParameter("stationNo",stationNo);
    
    return query.getResultList();  
    
  }  
    
}

