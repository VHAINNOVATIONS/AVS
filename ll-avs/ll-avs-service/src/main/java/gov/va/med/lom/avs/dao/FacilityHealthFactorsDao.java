package gov.va.med.lom.avs.dao;

import gov.va.med.lom.avs.model.FacilityHealthFactor;

import gov.va.med.lom.jpa.foundation.dao.BaseEntityDao;

import java.util.List;
import javax.ejb.Local;

/**
 * Health Factors DAO object
 */
@Local
public interface FacilityHealthFactorsDao extends BaseEntityDao<FacilityHealthFactor, Long> {

    public static final String QUERY_FIND_HF = "healthfactors.find";
    public static final String QUERY_FIND_HF_BY_TYPE = "healthfactors.findByType";

	/**
	 * Looks up health factors by station number
	 * 
	 * @param stationNo The station's unique identifier
	 * @return Health Factors
	 */
	public abstract List<FacilityHealthFactor> find(String stationNo);
	
  /**
   * Looks up health factors by station number and type
   * 
   * @param stationNo The station's unique identifier
   * @param type The health factor type
   * @return Health Factors
   */	
	public abstract List<FacilityHealthFactor> findByType(String stationNo, String type);
	
}
