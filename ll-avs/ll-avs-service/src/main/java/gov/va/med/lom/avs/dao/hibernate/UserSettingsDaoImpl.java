package gov.va.med.lom.avs.dao.hibernate;

import java.util.List;

import gov.va.med.lom.avs.dao.UserSettingsDao;
import gov.va.med.lom.avs.model.UserSettings;

import gov.va.med.lom.jpa.foundation.dao.impl.BaseEntityDaoJpa;

import javax.ejb.Local;
import javax.ejb.Stateless;

import javax.persistence.Query;

/**
 * User settings DAO Implementation
 */
@Stateless(name="gov.va.med.lom.avs.dao.UserSettingsDao")
@Local(UserSettingsDao.class)
public class UserSettingsDaoImpl extends BaseEntityDaoJpa<UserSettings, Long> implements UserSettingsDao {

  /**
   * Looks up user settings by the facility number and user DUZ
   * 
   * @param facilityNo The Facility's unique identifier
   * @param userDuz The user's unique identifier
   * @return User settings object
   */
  public UserSettings find(String facilityNo, String userDuz) {
	    
    	Query query = super.entityManager.createNamedQuery(QUERY_FIND_USER_SETTINGS);
	    query.setParameter("facilityNo", facilityNo);
	    query.setParameter("userDuz", userDuz);
		
	    query.setMaxResults(1);
	    
		@SuppressWarnings("unchecked")
	    List<UserSettings> list = query.getResultList();

		if (list == null || list.size() == 0) {
			return null;
		}

		return list.get(0);

	}

}

