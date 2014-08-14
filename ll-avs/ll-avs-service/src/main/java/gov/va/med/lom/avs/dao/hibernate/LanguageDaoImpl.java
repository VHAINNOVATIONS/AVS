package gov.va.med.lom.avs.dao.hibernate;

import gov.va.med.lom.avs.dao.LanguageDao;
import gov.va.med.lom.avs.model.Language;

import gov.va.med.lom.jpa.foundation.dao.impl.BaseEntityDaoJpa;

import java.util.List;

import javax.ejb.Local;
import javax.ejb.Stateless;

import javax.persistence.Query;


/**
 * Language DAO Implementation
 */
@Stateless(name="gov.va.med.lom.avs.dao.LanguageDao")
@Local(LanguageDao.class)
public class LanguageDaoImpl extends BaseEntityDaoJpa<Language, Long> implements LanguageDao {

  /**
   * Looks up a language by abbreviation.
   * 
   * @param abbreviation The abbreviation to filter by.
   * @return Language
   */
  @SuppressWarnings("unchecked")
  public Language findByAbbreviation(String abbreviation) {
      
      Query query = super.entityManager.createNamedQuery(QUERY_FIND_BY_ABBR);
      query.setParameter("abbreviation", abbreviation);

      
      @SuppressWarnings("unchecked")
      List<Language> list = query.getResultList();
      
      if (list == null || list.size() == 0) {
        return null;
      }

      return list.get(0);
  }
  
}

