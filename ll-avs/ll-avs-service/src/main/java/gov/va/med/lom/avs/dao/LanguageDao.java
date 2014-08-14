package gov.va.med.lom.avs.dao;

import gov.va.med.lom.avs.model.Language;

import gov.va.med.lom.jpa.foundation.dao.BaseEntityDao;

import javax.ejb.Local;

/**
 * Language DAO object
 */
@Local
public interface LanguageDao extends BaseEntityDao<Language, Long> {

  public static final String QUERY_FIND_BY_ABBR = "language.findByAbbr";

	 /**
   * Looks up language by abbreviation
   * 
   * @param abbreviation The abbreviation
   * @return String Resources
   */
  public abstract Language findByAbbreviation(String abbreviation);
	
}
