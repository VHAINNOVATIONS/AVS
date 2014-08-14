package gov.va.med.lom.avs.dao.hibernate;

import java.util.List;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.Query;

import gov.va.med.lom.jpa.foundation.dao.impl.BaseEntityDaoJpa;

import gov.va.med.lom.avs.dao.PvsPrintLogDao;
import gov.va.med.lom.avs.model.PvsPrintLog;

@Stateless(name="gov.va.med.lom.avs.dao.PvsPrintLogDao")
@Local(PvsPrintLogDao.class)
@SuppressWarnings("unchecked")
public class PvsPrintLogDaoImpl extends BaseEntityDaoJpa<PvsPrintLog, Long> implements PvsPrintLogDao {
  
  public List<PvsPrintLog> findByDateAndClinic(String date, long clinicId) {
    
    Query query = entityManager.createNamedQuery(QRY_FIND_BY_DATE_CLINIC);
    query.setParameter("date", date);
    query.setParameter("clinicId", clinicId);
    
    return query.getResultList();  
    
  } 
  
}

