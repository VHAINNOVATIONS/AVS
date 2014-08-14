package gov.va.med.lom.avs.dao.hibernate;

import gov.va.med.lom.avs.dao.FacilityHealthFactorsDao;
import gov.va.med.lom.avs.model.FacilityHealthFactor;

import gov.va.med.lom.jpa.foundation.dao.impl.BaseEntityDaoJpa;

import java.util.List;

import javax.ejb.Local;
import javax.ejb.Stateless;

import javax.persistence.Query;


/**
 * Facility Health Factors DAO Implementation
 */
@Stateless(name="gov.va.med.lom.avs.dao.FacilityHealthFactorsDao")
@Local(FacilityHealthFactorsDao.class)
public class FacilityHealthFactorsDaoImpl extends BaseEntityDaoJpa<FacilityHealthFactor, Long> implements FacilityHealthFactorsDao {

	/**
	 * Looks up health factors by station number.
	 * 
	 * @param stationNo The station number to filter by.
	 * @return Health Factors
	 */
  @SuppressWarnings("unchecked")
	public List<FacilityHealthFactor> find(String stationNo) {
    	
    	Query query = super.entityManager.createNamedQuery(QUERY_FIND_HF);
	    query.setParameter("stationNo", stationNo);

	    return query.getResultList();
	}
  
  /**
   * Looks up health factors by station number and type.
   * 
   * @param stationNo The station number to filter by.
   * @param type The health factor type to filter by.
   * @return Health Factors
   */
  @SuppressWarnings("unchecked")  
  public List<FacilityHealthFactor> findByType(String stationNo, String type) {
   
    Query query = super.entityManager.createNamedQuery(QUERY_FIND_HF_BY_TYPE);
    query.setParameter("stationNo", stationNo);
    query.setParameter("type", type);

    return query.getResultList();    
  }
}

