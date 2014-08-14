package gov.va.med.lom.avs.dao.hibernate;

import java.util.List;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.Query;

import gov.va.med.lom.jpa.foundation.dao.impl.BaseEntityDaoJpa;

import gov.va.med.lom.avs.dao.PvsClinicalRemindersDao;
import gov.va.med.lom.avs.model.PvsClinicalReminder;

@Stateless(name="gov.va.med.lom.avs.dao.PvsClinicalRemindersDao")
@Local(PvsClinicalRemindersDao.class)
@SuppressWarnings("unchecked")
public class PvsClinicalReminderDaoImpl extends BaseEntityDaoJpa<PvsClinicalReminder, Long> implements PvsClinicalRemindersDao {
  
  public List<PvsClinicalReminder> findByStationNo(String stationNo) {
    
    Query query = entityManager.createNamedQuery(QRY_FIND_BY_STATION);
    query.setParameter("stationNo",stationNo);
    
    return query.getResultList();  
    
  }  
    
}

