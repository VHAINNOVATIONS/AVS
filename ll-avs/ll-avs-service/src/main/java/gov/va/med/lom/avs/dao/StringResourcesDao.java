package gov.va.med.lom.avs.dao;

import gov.va.med.lom.avs.model.StringResource;

import gov.va.med.lom.jpa.foundation.dao.BaseEntityDao;

import java.util.List;
import javax.ejb.Local;

/**
 * Health Factors DAO object
 */
@Local
public interface StringResourcesDao extends BaseEntityDao<StringResource, Long> {

  public static final String QUERY_FIND_FOR_STATION = "strings.findByStation";
  public static final String QUERY_FIND_BY_NAME = "strings.findByName";

	/**
	 * Looks up string resources by station number
	 * 
	 * @param stationNo The station's unique identifier
	 * @param language The language
	 * @return String Resources
	 */
	public abstract List<StringResource> findForStation(String stationNo, String language);
	
	 /**
   * Looks up string resources by name and station number
   * 
   * @param stationNo The station's unique identifier
   * @param name The name
   * @param language The language
   * @return String Resources
   */
  public abstract StringResource findByName(String stationNo, String name, String language);
	
}
