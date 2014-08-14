package gov.va.med.lom.jpa.foundation.dao;

import java.io.Serializable;
import java.util.List;

public interface BaseEntityDao<T, ID extends Serializable> {

	
	public T save(T entity);
	
	public T update(T entity);

	public T refresh(T entity);
	
	public void remove(T entity);

	public void remove(ID id);
	
	public void removeAll();
	
	public T findById(ID id);

	public List<T> findAll();

	public List<T> findActive();
	
}

