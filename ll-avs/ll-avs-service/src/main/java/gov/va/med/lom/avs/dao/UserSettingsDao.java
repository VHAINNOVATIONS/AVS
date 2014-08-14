package gov.va.med.lom.avs.dao;

import gov.va.med.lom.avs.model.UserSettings;

import gov.va.med.lom.jpa.foundation.dao.BaseEntityDao;

import javax.ejb.Local;

/**
 * User Settings DAO
 */
@Local
public interface UserSettingsDao extends BaseEntityDao<UserSettings, Long> {

	public static final String QUERY_FIND_USER_SETTINGS = "userSettings.find";

	/**
	 * Looks up user settings by the facility number and user DUZ
	 * 
	 * @param facilityNo The Facility's unique identifier
	 * @param userDuz The user's unique identifier
	 * @return User settings object
	 */
	public abstract UserSettings find(String facilityNo, String userDuz);

}
