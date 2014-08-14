package gov.va.med.lom.avs.dao.hibernate;

import gov.va.med.lom.avs.dao.StringResourcesDao;
import gov.va.med.lom.avs.model.StringResource;

import gov.va.med.lom.jpa.foundation.dao.impl.BaseEntityDaoJpa;

import java.util.List;

import javax.ejb.Local;
import javax.ejb.Stateless;

import javax.persistence.Query;


/**
 * Facility Health Factors DAO Implementation
 */
@Stateless(name="gov.va.med.lom.avs.dao.StringResourcesDao")
@Local(StringResourcesDao.class)
public class StringResourcesDaoImpl extends BaseEntityDaoJpa<StringResource, Long> implements StringResourcesDao {

	/**
	 * Looks up  string resources by station number.
	 * 
	 * @param stationNo The station number to filter by.
	 * @param language The language to filter by.
	 * @return String Resources
	 */
  @SuppressWarnings("unchecked")
	public List<StringResource> findForStation(String stationNo, String language) {
    	
    	Query query = super.entityManager.createNamedQuery(QUERY_FIND_FOR_STATION);
	    query.setParameter("stationNo", stationNo);
	    query.setParameter("language", language);

	    return query.getResultList();
	}
  
  /**
   * Looks up a string resource by station number and name.
   * 
   * @param stationNo The station number to filter by.
   * @param name The name to filter by.
   * @param language The language to filter by.
   * @return String Resource
   */
  @SuppressWarnings("unchecked")
  public StringResource findByName(String stationNo, String name, String language) {
      
      Query query = super.entityManager.createNamedQuery(QUERY_FIND_BY_NAME);
      query.setParameter("stationNo", stationNo);
      query.setParameter("name", name);
      query.setParameter("language", language);

      
      @SuppressWarnings("unchecked")
      List<StringResource> list = query.getResultList();
      
      if (list == null || list.size() == 0) {
        return null;
      }

      return list.get(0);
  }
  
}

