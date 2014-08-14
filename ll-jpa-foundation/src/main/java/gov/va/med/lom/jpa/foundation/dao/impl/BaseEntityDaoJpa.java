package gov.va.med.lom.jpa.foundation.dao.impl;

import gov.va.med.lom.jpa.foundation.dao.BaseEntityDao;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

public abstract class BaseEntityDaoJpa<T, ID extends Serializable> implements
		BaseEntityDao<T, ID> {

	protected Class<T> entityClass;

	@PersistenceContext
	protected EntityManager entityManager;

	@SuppressWarnings("unchecked")
	public BaseEntityDaoJpa() {
		ParameterizedType genericSuperclass = (ParameterizedType) getClass()
				.getGenericSuperclass();
		this.entityClass = (Class<T>) genericSuperclass
				.getActualTypeArguments()[0];
	}
	
	public T save(T entity) {
	  entityManager.persist(entity);
    return entity;
	}
	
  public T update(T entity) {
    entityManager.merge(entity);
    return entity;
  }	

	public T refresh(T entity){
		entityManager.refresh(entity);
		return entity;
	}
	
	public void remove(T entity) {
		entityManager.remove(entity);
	}
	
	public void remove(ID id) {
		T obj = findById(id);
		entityManager.remove(obj);
	}

	public void removeAll(){
		Query q = entityManager.createQuery("delete from " + entityClass.getName());
		q.executeUpdate();
	}
	
	public T findById(ID id) {
		return entityManager.find(entityClass, id);
	}

	@SuppressWarnings("unchecked")
	public List<T> findAll() {
		Query q = entityManager.createQuery("select e from "
				+ entityClass.getName() + " as e");
		return (List<T>) q.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<T> findActive(){
		Query q = entityManager.createQuery("select e from "
				+ entityClass.getName() + " as e where active = 1");
		return (List<T>) q.getResultList();
	}
	
	public Query findByNamedQueryAndNamedParam(String named, String[] params, Object[] paramValues){
	    
	    Query q = entityManager.createNamedQuery(named);
	    
	    for(int i = 0; i < params.length; i++){
	        q.setParameter(params[i], paramValues[i]);
	    }
	    
	    return q;
	    
	}
	
	public Query findByNamedQuery(String named){
        return entityManager.createNamedQuery(named);
        
    }
	
}

