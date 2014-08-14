package gov.va.med.lom.avs.dao;

import gov.va.med.lom.avs.model.FacilityPrefs;

import gov.va.med.lom.jpa.foundation.dao.BaseEntityDao;

import javax.ejb.Local;

/**
 * Station Preferences DAO
 */
@Local
public interface FacilityPrefsDao extends BaseEntityDao<FacilityPrefs, Long> {

	public static final String QUERY_FIND_PREFS = "facilityPrefs.find";

	/**
	 * Looks up Facility preferences by its Facility Number
	 * 
	 * @param facilityNo The Facility's unique identifier
	 * @return Facility preferences object
	 */
	public abstract FacilityPrefs find(String facilityNo);

}
