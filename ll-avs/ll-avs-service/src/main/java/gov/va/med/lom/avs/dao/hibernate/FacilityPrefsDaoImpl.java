package gov.va.med.lom.avs.dao.hibernate;

import java.util.List;

import gov.va.med.lom.avs.dao.FacilityPrefsDao;
import gov.va.med.lom.avs.model.FacilityPrefs;

import gov.va.med.lom.jpa.foundation.dao.impl.BaseEntityDaoJpa;

import javax.ejb.Local;
import javax.ejb.Stateless;

import javax.persistence.Query;

/**
 * Station Preferences DAO Implementation
 */
@Stateless(name="gov.va.med.lom.avs.dao.FacilityPrefsDao")
@Local(FacilityPrefsDao.class)
public class FacilityPrefsDaoImpl extends BaseEntityDaoJpa<FacilityPrefs, Long> implements FacilityPrefsDao {

	/**
	 * Looks up Facility Prefs based on a Facility Number.
	 * 
	 * @param facilityNo The Facility Number to search for.
	 * @return Facility preferences
	 */
	public FacilityPrefs find(String facilityNo) {
	    
    	Query query = super.entityManager.createNamedQuery(QUERY_FIND_PREFS);
	    query.setParameter("facilityNo", facilityNo);
		
	    query.setMaxResults(1);
	    
		@SuppressWarnings("unchecked")
	    List<FacilityPrefs> list = query.getResultList();

		if (list == null || list.size() == 0) {
			return null;
		}

		return list.get(0);

	}

}

