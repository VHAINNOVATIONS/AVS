package gov.va.med.lom.avs.dao.hibernate;

import java.util.List;

import gov.va.med.lom.avs.dao.ProviderPrefsDao;
import gov.va.med.lom.avs.model.ProviderPrefs;

import gov.va.med.lom.jpa.foundation.dao.impl.BaseEntityDaoJpa;

import javax.ejb.Local;
import javax.ejb.Stateless;

import javax.persistence.Query;

/**
 * Provider Preferences DAO Implementation
 */
@Stateless(name="gov.va.med.lom.avs.dao.ProviderPrefsDao")
@Local(ProviderPrefsDao.class)
public class ProviderPrefsDaoImpl extends BaseEntityDaoJpa<ProviderPrefs, Long> implements ProviderPrefsDao {

	/**
	 * Looks up Provider Prefs based on a Facility Number and Provider DUZ.
	 * 
	 * @param providerDuz The Provider DUZ to filter by.
	 * @param facilityNo The Facility Number to filter by.
	 * @return Provider preferences
	 * @throws DataIntegrityViolationException
	 */
	public ProviderPrefs find(String providerDuz, String facilityNo) {
    	
    	Query query = super.entityManager.createNamedQuery(QUERY_FIND_PREFS);
	    query.setParameter("providerDuz", providerDuz);
	    query.setParameter("facilityNo", facilityNo);
		
	    query.setMaxResults(1);
	    
		@SuppressWarnings("unchecked")
	    List<ProviderPrefs> list = query.getResultList();

		if (list == null || list.size() == 0) {
			return null;
		}

		return list.get(0);

	}
}

