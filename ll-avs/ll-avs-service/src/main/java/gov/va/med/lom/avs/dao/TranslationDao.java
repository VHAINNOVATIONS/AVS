package gov.va.med.lom.avs.dao;

import gov.va.med.lom.avs.enumeration.SortDirectionEnum;
import gov.va.med.lom.avs.enumeration.SortEnum;
import gov.va.med.lom.avs.enumeration.TranslationTypeEnum;
import gov.va.med.lom.avs.model.Translation;
import gov.va.med.lom.avs.util.FilterProperty;

import gov.va.med.lom.jpa.foundation.dao.BaseEntityDao;

import java.util.List;

import javax.ejb.Local;

@Local
public interface TranslationDao extends BaseEntityDao<Translation, Long> {

	public static final String QUERY_FIND_BY_ID = "translations.find.byId";
	public static final String QUERY_FIND_BY_SOURCE = "translations.find.bySource";

	//public static final String QUERY_FETCH_COUNT = "translations.fetchList.count";

	public abstract List<Translation> fetchListForEditor(String facilityNo, long languageId, 
	    Integer start, Integer limit, SortEnum sort, SortDirectionEnum dir, List<FilterProperty> filters);
	public abstract Long fetchTotalCount(String facilityNo, long languageId, List<FilterProperty> filters);
	public abstract Translation find(String facilityNo, Long id);
	public abstract Translation findBySource(String facilityNo, long languageId, String source);
	public abstract List<Translation> fetchListByTypeAndSource(String facilityNo, long languageId, TranslationTypeEnum type, List<String> sources);


}
