package gov.va.med.lom.avs.dao.hibernate;

import java.util.List;

import gov.va.med.lom.avs.dao.ServiceDao;
import gov.va.med.lom.avs.model.Service;
import gov.va.med.lom.foundation.util.Precondition;

import gov.va.med.lom.jpa.foundation.dao.impl.BaseEntityDaoJpa;

import java.util.ArrayList;

import javax.ejb.Local;
import javax.ejb.Stateless;

import javax.persistence.Query;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

@Stateless(name="gov.va.med.lom.avs.dao.ServiceDao")
@Local(ServiceDao.class)
public class ServiceDaoImpl extends BaseEntityDaoJpa<Service, Long> implements ServiceDao {

	protected static final Log log = LogFactory.getLog(ServiceDaoImpl.class);

	public List<Service> fetchListForEditor(String facilityNo) {

		StringBuffer sql = new StringBuffer(""
			+ "SELECT service"
			+ " FROM Service service"
			+ " WHERE service.facilityNo = :facilityNo"
			+ " ORDER BY service.name ASC"
			+ " AND service.active = 1"
		);
		
		Query query = super.entityManager.createQuery(sql.toString());
	    query.setParameter("facilityNo", facilityNo);

		@SuppressWarnings("unchecked")
	    List<Service> list = query.getResultList();

		if (list == null) {
			list = new ArrayList<Service>();
		}

		return list;
	}

	public Long fetchTotalCount(String facilityNo) {
		
		StringBuffer sql = new StringBuffer(""
			+ "SELECT COUNT(*)"
			+ " FROM Service service"
			+ " WHERE service.facilityNo = :facilityNo"
			+ " AND service.active = 1"
		);
		
		Query query = super.entityManager.createQuery(sql.toString());
	  query.setParameter("facilityNo", facilityNo);
	    
		return (Long)query.getSingleResult();
	}

	public Service find(String facilityNo, Long id) {

		Query query = super.entityManager.createNamedQuery(QUERY_FIND_BY_ID);
	    query.setParameter("facilityNo", facilityNo);
	    query.setParameter("id", id);
		
	    query.setMaxResults(1);
	    
		@SuppressWarnings("unchecked")
	    List<Service> list = query.getResultList();

		if (list == null || list.size() == 0) {
			return null;
		}

		return list.get(0);
		
	}

	 public Service findByName(String facilityNo, String name) {

	    Precondition.assertNotBlank("name", name);

	    Query query = super.entityManager.createNamedQuery(QUERY_FIND_BY_NAME);
	      query.setParameter("facilityNo", facilityNo);
	      query.setParameter("name", name);
	    
	      query.setMaxResults(1);
	      
	    @SuppressWarnings("unchecked")
	      List<Service> list = query.getResultList();

	    if (list == null || list.size() == 0) {
	      return null;
	    }

	    return list.get(0);
	    
	  }
	
}

