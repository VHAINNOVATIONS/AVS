package gov.va.med.lom.avs.dao;

import gov.va.med.lom.avs.model.ClinicPrefs;

import gov.va.med.lom.jpa.foundation.dao.BaseEntityDao;

import javax.ejb.Local;

/**
 * Clinic Preferences DAO object
 */
@Local
public interface ClinicPrefsDao extends BaseEntityDao<ClinicPrefs, Long> {

    public static final String QUERY_FIND_PREFS = "clinicPrefs.find";

	/**
	 * Looks up Clinic preferences by their Facility Number and IEN
	 * 
	 * @param clinicIen The Clinic's unique identifier
	 * @param facilityNo The Facility's unique identifier
	 * @return Clinic preferences object
	 */
	public abstract ClinicPrefs find(String clinicIen, String facilityNo);
	
}
