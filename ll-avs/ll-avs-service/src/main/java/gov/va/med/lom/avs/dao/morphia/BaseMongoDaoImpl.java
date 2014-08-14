package gov.va.med.lom.avs.dao.morphia;

import java.lang.reflect.ParameterizedType;
import java.net.UnknownHostException;
import java.util.ResourceBundle;


import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import org.mongodb.morphia.dao.BasicDAO;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;
import org.mongodb.morphia.query.UpdateResults;
import org.bson.types.ObjectId;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;

import gov.va.med.lom.avs.dao.BaseMongoDao;
import gov.va.med.lom.avs.model.BaseEntity;

public class BaseMongoDaoImpl<T, K> extends BasicDAO<T, K> implements BaseMongoDao<T, K> {

  private static MongoClient mongoClient;
  private static Morphia morphia;
  private static Datastore datastore;
  
  protected static String DB_SERVER;
  protected static String DB_NAME;
  
  static {
    try {
      ResourceBundle res = ResourceBundle.getBundle("gov.va.med.lom.avs.mongodb");
      DB_SERVER = res.getString("mongodb.server");
      DB_NAME = res.getString("mongodb.db");
      initMongo();
    } catch(Exception e) {
      DB_SERVER = "localhost";
      DB_NAME = "test";
    }
  }
  
  public static void initMongo() throws UnknownHostException {
    mongoClient = new MongoClient(DB_SERVER, MongoClientOptions.builder()
        .alwaysUseMBeans(true).build());
    morphia = new Morphia();
    datastore = morphia.mapPackage(BaseEntity.class.getPackage().getName())
        .createDatastore(mongoClient, DB_NAME);    
  }
  
  public BaseMongoDaoImpl() {
    super(datastore);
    super.initType(((Class<T>) ((ParameterizedType)getClass()
        .getGenericSuperclass())
        .getActualTypeArguments()[0]));
  }
  
  public MongoClient getMongoClient() {
    return mongoClient;
  }

  public Morphia getMorphia() {
    return morphia;
  }
  
  public Datastore getDatastore() {
    if (!mongoClient.getConnector().isOpen()) {
      try {
        initMongo();
      } catch(Exception e) {}
    }
    return datastore;
  }

  public void closeConnections() {
    mongoClient.close();
  }
  
  public Query<T> getQueryForId(Class cls, ObjectId id) {
    return getDatastore().find(cls, "_id", id);
  }
  
  public UpdateOperations<T> getUpdateOperations(Class cls) {
    return getDatastore().createUpdateOperations(cls);
  }
  
  public UpdateResults<T> update(Class cls, ObjectId id, UpdateOperations<T> ops) {
    Query<T> qry = getDatastore().find(cls, "_id", id);
    return getDatastore().update(qry, ops);
  }  
  
}

