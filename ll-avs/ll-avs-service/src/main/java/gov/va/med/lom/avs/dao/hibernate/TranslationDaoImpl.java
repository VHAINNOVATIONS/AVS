package gov.va.med.lom.avs.dao.hibernate;

import java.util.HashSet;
import java.util.List;

import gov.va.med.lom.avs.dao.TranslationDao;
import gov.va.med.lom.avs.enumeration.SortDirectionEnum;
import gov.va.med.lom.avs.enumeration.SortEnum;
import gov.va.med.lom.avs.enumeration.TranslationTypeEnum;
import gov.va.med.lom.avs.model.Translation;
import gov.va.med.lom.avs.util.FilterProperty;
import gov.va.med.lom.foundation.util.Precondition;

import gov.va.med.lom.jpa.foundation.dao.impl.BaseEntityDaoJpa;

import java.util.ArrayList;

import javax.ejb.Local;
import javax.ejb.Stateless;

import javax.persistence.Query;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

@Stateless(name="gov.va.med.lom.avs.dao.TranslationDao")
@Local(TranslationDao.class)
public class TranslationDaoImpl extends BaseEntityDaoJpa<Translation, Long> implements TranslationDao {

	protected static final Log log = LogFactory.getLog(TranslationDaoImpl.class);

	public List<Translation> fetchListForEditor(String facilityNo, long languageId, Integer start, Integer limit, 
			SortEnum sort, SortDirectionEnum dir, List<FilterProperty> filters) {

		StringBuffer sql = new StringBuffer(""
			+ "SELECT translation"
			+ " FROM Translation translation"
			+ " WHERE translation.facilityNo = :facilityNo"
			+ " AND translation.active = 1"
			+ " AND (translation.language.id IS NULL "
			+ " OR translation.language.id = :languageId)"
		);
		
		if (filters != null && filters.size() > 0) {
			sql.append(" AND (");
			Integer index = 0;
			for (FilterProperty property : filters) {
				if (index > 0) {
					sql.append(" OR ");
				}
				sql.append("translation." + property.getProperty() + " LIKE :property").append(index);
				index++;
			}
			sql.append(")");
		}
		
		sql.append(" ORDER BY translation." + sort + " " + dir);

		Query query = super.entityManager.createQuery(sql.toString());
	  query.setParameter("facilityNo", facilityNo);
	  query.setParameter("languageId", languageId);

		if (filters != null && filters.size() > 0) {
			Integer index = 0;
			for (FilterProperty property : filters) {
				query.setParameter("property" + index, "%" + property.getValue() + "%");
				index++;
			}
		}
		
	    query.setFirstResult(start);
	    query.setMaxResults(limit);

		@SuppressWarnings("unchecked")
	    List<Translation> list = query.getResultList();

		if (list == null) {
			list = new ArrayList<Translation>();
		}

		return list;
	}

	public Long fetchTotalCount(String facilityNo, long languageId, List<FilterProperty> filters) {
		
		StringBuffer sql = new StringBuffer(""
			+ "SELECT COUNT(*)"
			+ " FROM Translation translation"
			+ " WHERE translation.facilityNo = :facilityNo"
			+ " AND translation.active = 1"
      + " AND (translation.language.id IS NULL "
      + " OR translation.language.id = :languageId)"
		);
		
		if (filters != null && filters.size() > 0) {
			sql.append(" AND (");
			Integer index = 0;
			for (FilterProperty property : filters) {
				if (index > 0) {
					sql.append(" OR ");
				}
				sql.append("translation." + property.getProperty() + " LIKE :property").append(index);
				index++;
			}
			sql.append(")");
		}

		Query query = super.entityManager.createQuery(sql.toString());
	  query.setParameter("facilityNo", facilityNo);
	  query.setParameter("languageId", languageId);
	    
		if (filters != null && filters.size() > 0) {
			Integer index = 0;
			for (FilterProperty property : filters) {
				query.setParameter("property" + index, "%" + property.getValue() + "%");
				index++;
			}
		}

		return (Long)query.getSingleResult();
	}

	public List<Translation> fetchListByTypeAndSource(String facilityNo, long languageId, 
	    TranslationTypeEnum type, List<String> unsortedSources) {
		
		HashSet<String> sources = new HashSet<String>(unsortedSources);
		
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT translation FROM Translation translation");
		sql.append(" WHERE translation.facilityNo = :facilityNo");
		sql.append(" AND translation.type = :type");
		sql.append(" AND translation.active = 1");
		sql.append(" AND (translation.language.id IS NULL");
    sql.append(" OR translation.language.id = :languageId)");

		sql.append(" AND (");
		Integer index = 0;
		for (String source : sources) {
			if (source == null || source.isEmpty()) {
				continue;
			}
			if (index > 0) {
				sql.append(" OR ");
			}
			sql.append("translation.source = :source").append(index);
			index++;
		}
		sql.append(")");
		
		Query query = super.entityManager.createQuery(sql.toString());
	  query.setParameter("facilityNo", facilityNo);
	  query.setParameter("type", type);
	  query.setParameter("languageId", languageId);
	  
		index = 0;
		for (String source : sources) {
			if (source == null || source.isEmpty()) {
				continue;
			}
			query.setParameter("source" + index, source);
			index++;
		}
		
		@SuppressWarnings("unchecked")
	    List<Translation> list = query.getResultList();
		if (list == null) {
			return new ArrayList<Translation>();
		}

		return list;
	}

	public Translation find(String facilityNo, Long id) {

		Query query = super.entityManager.createNamedQuery(QUERY_FIND_BY_ID);
	    query.setParameter("facilityNo", facilityNo);
	    query.setParameter("id", id);
		
	    query.setMaxResults(1);
	    
		@SuppressWarnings("unchecked")
	    List<Translation> list = query.getResultList();

		if (list == null || list.size() == 0) {
			return null;
		}

		return list.get(0);
		
	}

	public Translation findBySource(String facilityNo, long languageId, String source) {

		Precondition.assertNotBlank("source", source);

		Query query = super.entityManager.createNamedQuery(QUERY_FIND_BY_SOURCE);
	    query.setParameter("facilityNo", facilityNo);
	    query.setParameter("source", source);
	    query.setParameter("languageId", languageId);
		
	    query.setMaxResults(1);
	    
		@SuppressWarnings("unchecked")
	    List<Translation> list = query.getResultList();

		if (list == null || list.size() == 0) {
			return null;
		}

		return list.get(0);
		
	}

}

