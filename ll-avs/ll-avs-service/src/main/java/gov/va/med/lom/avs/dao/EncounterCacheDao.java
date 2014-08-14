package gov.va.med.lom.avs.dao;

import gov.va.med.lom.avs.model.EncounterCache;

import gov.va.med.lom.reports.dao.BaseQueryDao;

import javax.ejb.Local;
import java.util.List;
import java.util.Date;

/**
 * Clinic Preferences DAO object
 */
@Local
public interface EncounterCacheDao extends BaseQueryDao<EncounterCache, Long> {

    public static final String QUERY_FIND_CACHE = "encounterCache.find";
    public static final String QUERY_FIND_CACHE_BY_DATES = "encounterCache.findByDates";

	/**
	 * Looks up encounter cache by facility, patient, location, and encounter date/time
	 * 
	 * @param facilityNo The facility's unique identifier
	 * @param patientDfn The patient's unique identifier
	 * @param locationIen The location's unique identifier
	 * @param datetime The encounter date/time in FM format
	 * @return EncounterCache cached encounter info
	 */
	public abstract EncounterCache find(String facilityNo, String patientDfn, String locationIen, double datetime);
	public abstract List<EncounterCache> findByDates(String facilityNo, Date beginDate, Date endDate);
	
}
