package gov.va.med.lom.avs.dao.morphia;

import javax.ejb.Local;
import javax.ejb.Stateless;
import org.bson.types.ObjectId;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.query.Query;

import gov.va.med.lom.avs.dao.ProvidersDao;
import gov.va.med.lom.avs.model.Provider;

@Stateless(name="gov.va.med.lom.avs.dao.ProvidersDao")
@Local(ProvidersDao.class)
public class ProvidersDaoImpl extends BaseMongoDaoImpl<Provider, ObjectId> implements ProvidersDao {

  public Provider find(String stationNo, String providerDuz) {
      
    Datastore ds = super.getDatastore();
    Query<Provider> query = 
        ds.createQuery(Provider.class)
        .disableValidation()
        .filter("stationNo =", stationNo)
        .filter("patientDfn =", providerDuz);    
    
    return this.find(query).get();
  }
  
}

