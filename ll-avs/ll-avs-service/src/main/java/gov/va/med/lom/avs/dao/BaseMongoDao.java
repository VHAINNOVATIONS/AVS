package gov.va.med.lom.avs.dao;

import java.io.Serializable;

import org.bson.types.ObjectId;
import org.mongodb.morphia.Key;
import org.mongodb.morphia.Morphia;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;
import org.mongodb.morphia.query.UpdateResults;

import com.mongodb.MongoClient;
import com.mongodb.WriteResult;

public interface BaseMongoDao<T, K> extends Serializable {

  
  public T get(final K id);
  public Key<T> save(final T entity);
  public Query<T> getQueryForId(Class cls, ObjectId id);
  public UpdateOperations<T> getUpdateOperations(Class cls);
  public UpdateResults<T> update(Class cls, ObjectId id, UpdateOperations<T> ops);
  public WriteResult delete(final T entity);
  public MongoClient getMongoClient();
  public Morphia getMorphia();
  public void closeConnections();  
  
}

