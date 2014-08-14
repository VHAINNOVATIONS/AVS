package gov.va.med.lom.avs.dao.hibernate;

import gov.va.med.lom.avs.dao.EncounterCacheDao;
import gov.va.med.lom.avs.model.EncounterCache;

import gov.va.med.lom.reports.dao.hibernate.BaseQueryDaoJpa;

import java.util.Date;
import java.util.List;

import javax.ejb.Local;
import javax.ejb.Stateless;

import javax.persistence.Query;

/**
 * Encounter Cache DAO Implementation
 */
@Stateless(name="gov.va.med.lom.avs.dao.EncounterCacheDao")
@Local(EncounterCacheDao.class)
public class EncounterCacheDaoImpl extends BaseQueryDaoJpa<EncounterCache, Long> implements EncounterCacheDao {

  static {
    addQueryFilter(EncounterCacheDaoImpl.class, "avsDateRanges", "sra.termCalendar.termCalendarId", "or");
    addQueryFilter(EncounterCacheDaoImpl.class, "department", "sra.department", "or");
    addQueryFilter(EncounterCacheDaoImpl.class, "courseId", "sra.courseId", "or");
  }
  
  /**
   * Looks up encounter cache by facility, patient, location, and encounter date/time
   * 
   * @param facilityNo The facility's unique identifier
   * @param patientDfn The patient's unique identifier
   * @param locationIen The location's unique identifier
   * @param datetime The encounter date/time in FM format
   * @return Clinic preferences object
   */
  public EncounterCache find(String facilityNo, String patientDfn, String locationIen, double datetime){
    	
  	Query query = super.entityManager.createNamedQuery(QUERY_FIND_CACHE);
    query.setParameter("facilityNo", facilityNo);
    query.setParameter("patientDfn", patientDfn);
    query.setParameter("locationIen", locationIen);
    query.setParameter("datetime", datetime);

    query.setMaxResults(1);
	    
		@SuppressWarnings("unchecked")
    List<EncounterCache> list = query.getResultList();

		if (list == null || list.size() == 0) {
			return null;
		}

		return list.get(0);
	}
  
  @SuppressWarnings("unchecked")  
  public List<EncounterCache> findByDates(String facilityNo, Date startDate, Date endDate) {
    
    Query query = super.entityManager.createNamedQuery(QUERY_FIND_CACHE_BY_DATES);
    query.setParameter("facilityNo", facilityNo);
    query.setParameter("startDate", startDate);
    query.setParameter("endDate", endDate);
    
    return query.getResultList();
    
  }
}

