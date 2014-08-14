package gov.va.med.lom.avs.client.action;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.gson.reflect.TypeToken;

import gov.va.med.lom.foundation.service.response.CollectionServiceResponse;
import gov.va.med.lom.foundation.service.response.ServiceResponse;
import gov.va.med.lom.json.util.JsonResponse;

import gov.va.med.lom.avs.client.model.TranslationJson;
import gov.va.med.lom.avs.enumeration.SortDirectionEnum;
import gov.va.med.lom.avs.enumeration.SortEnum;
import gov.va.med.lom.avs.model.Translation;
import gov.va.med.lom.avs.service.ServiceFactory;
import gov.va.med.lom.avs.util.FilterProperty;
import gov.va.med.lom.avs.web.util.AvsWebUtils;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class TranslationsAction extends BaseCardAction {

	protected static final Log log = LogFactory.getLog(TranslationsAction.class);

	private static final long serialVersionUID = 0;

	private Long id;
	private String translation;
  private String divisionNo;
  private String language;
	private Integer start;
	private Integer limit;

	/**
	 * JSON filter string sent by ExtJS, e.g. ?filter=[{"property":"source","value":"gim"}] 
	 */
	private String filter;

	private SortEnum sort;
	private SortDirectionEnum dir;

	@SuppressWarnings("unchecked")
	public String fetch() throws Exception {

		List<FilterProperty> filters = null;
		if (filter != null && !filter.isEmpty()) {
			Type collectionType = new TypeToken<Collection<FilterProperty>>(){}.getType();
			filters = (List<FilterProperty>)JsonResponse.deserializeJson(this.filter, collectionType);
		}
		CollectionServiceResponse<Translation> serviceResponse = ServiceFactory.getSettingsService()
			.fetchTranslations(super.facilityNo, this.getLanguage(), this.start, this.limit, this.sort, this.dir, filters);
		AvsWebUtils.handleServiceErrors(serviceResponse, log);
		Collection<Translation> translations = serviceResponse.getCollection();

		ServiceResponse<Long> serviceResponse2 = ServiceFactory.getSettingsService()
			.fetchTranslationsCount(super.facilityNo, this.getLanguage(), filters);
		AvsWebUtils.handleServiceErrors(serviceResponse2, log);
		Integer totalCount = safelyConvertLongToInteger(serviceResponse2.getPayload());

		List<TranslationJson> items = new ArrayList<TranslationJson>();
		for (Translation translation : translations) {
			items.add(new TranslationJson(translation));
		}

    JsonResponse.embedRoot(response, true, totalCount, "root", false, (Object)items);

    return SUCCESS;
	}

	public String update() throws Exception {

	  usageLog("Update Translation", "ID=" + this.id + ", Translation=" + this.translation);
	  
		ServiceResponse<Translation> response = ServiceFactory.getSettingsService()
			.updateTranslation(this.getDivisionNo(), this.id, this.translation);

		AvsWebUtils.handleServiceErrors(response, log);
		
		Translation translation = response.getPayload();

		return super.setJson(new TranslationJson(translation));
	}

	public Integer getStart() {
		return this.start;
	}
	
	public Integer getLimit() {
		return this.limit;
	}

	public SortEnum getSort() {
		return this.sort;
	}
	
	public SortDirectionEnum getDir() {
		return this.dir;
	}

	public void setStart(Integer start) {
		this.start = start;
	}

	public void setLimit(Integer limit) {
		this.limit = limit;
	}

	public void setSort(SortEnum sort) {
		this.sort = sort;
	}

	public void setDir(SortDirectionEnum dir) {
		this.dir = dir;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setTranslation(String translation) {
		this.translation = translation;
	}
	
	public String getFilter() {
		return this.filter;
	}

	public void setFilter(String filter) {
		this.filter = filter;
	}

  public String getDivisionNo() {
    return this.divisionNo != null && !this.divisionNo.isEmpty() ? this.divisionNo : super.facilityNo;
  }

  public void setDivisionNo(String divisionNo) {
    this.divisionNo = divisionNo;
  }

  public String getLanguage() {
    return this.language != null && !this.language.isEmpty() ? this.language : "en";
  }

  public void setLanguage(String language) {
    this.language = language;
  }
	
}
