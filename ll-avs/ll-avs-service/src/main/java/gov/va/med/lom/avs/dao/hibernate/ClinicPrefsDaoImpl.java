package gov.va.med.lom.avs.dao.hibernate;

import gov.va.med.lom.avs.dao.ClinicPrefsDao;
import gov.va.med.lom.avs.model.ClinicPrefs;

import gov.va.med.lom.jpa.foundation.dao.impl.BaseEntityDaoJpa;

import java.util.List;

import javax.ejb.Local;
import javax.ejb.Stateless;

import javax.persistence.Query;


/**
 * Provider Preferences DAO Implementation
 */
@Stateless(name="gov.va.med.lom.avs.dao.ClinicPrefsDao")
@Local(ClinicPrefsDao.class)
public class ClinicPrefsDaoImpl extends BaseEntityDaoJpa<ClinicPrefs, Long> implements ClinicPrefsDao {

	/**
	 * Looks up Clinic Prefs based on a Facility Number and IEN.
	 * 
	 * @param clinicIen The Clinic IEN to filter by.
	 * @param facilityNo The Facility Number to filter by.
	 * @return Clinic preferences 
	 * @throws DataIntegrityViolationException
	 */
	public ClinicPrefs find(String clinicIen, String facilityNo) {
    	
    	Query query = super.entityManager.createNamedQuery(QUERY_FIND_PREFS);
	    query.setParameter("clinicIen", clinicIen);
	    query.setParameter("facilityNo", facilityNo);

	    query.setMaxResults(1);
	    
		@SuppressWarnings("unchecked")
	    List<ClinicPrefs> list = query.getResultList();

		if (list == null || list.size() == 0) {
			return null;
		}

		return list.get(0);
	}
}

