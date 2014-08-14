package gov.va.med.lom.avs.dao;

import gov.va.med.lom.avs.model.ProviderPrefs;

import gov.va.med.lom.jpa.foundation.dao.BaseEntityDao;

import javax.ejb.Local;

/**
 * Provider Preferences DAO object
 */
@Local
public interface ProviderPrefsDao extends BaseEntityDao<ProviderPrefs, Long> {

	public static final String QUERY_FIND_PREFS = "providerPrefs.find";

	/**
	 * Looks up Provider preferences by their Facility Number and DUZ
	 * 
	 * @param providerDuz The Provider's unique identifier
	 * @param facilityNo The Facility's unique identifier
	 * @return Provider preferences object
	 */
	public abstract ProviderPrefs find(String providerDuz, String facilityNo);
	
}
