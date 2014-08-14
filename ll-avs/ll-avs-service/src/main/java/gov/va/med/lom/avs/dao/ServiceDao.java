package gov.va.med.lom.avs.dao;

import gov.va.med.lom.avs.model.Service;

import gov.va.med.lom.jpa.foundation.dao.BaseEntityDao;

import java.util.List;

import javax.ejb.Local;

@Local
public interface ServiceDao extends BaseEntityDao<Service, Long> {

	public static final String QUERY_FIND_BY_ID = "services.find.byId";
	public static final String QUERY_FIND_BY_NAME = "services.find.byName";

	public abstract List<Service> fetchListForEditor(String facilityNo);
	public abstract Long fetchTotalCount(String facilityNo);
	public abstract Service find(String facilityNo, Long id);
	public abstract Service findByName(String facilityNo, String name);

}
