package gov.va.med.lom.avs.dao.morphia;

import javax.ejb.Local;
import javax.ejb.Stateless;

import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.query.Query;

import gov.va.med.lom.avs.dao.RemoteAllergiesDao;
import gov.va.med.lom.avs.model.RemoteAllergies;

@Stateless(name="gov.va.med.lom.avs.dao.RemoteAllergiesDao")
@Local(RemoteAllergiesDao.class)
public class RemoteAllergiesDaoImpl extends BaseMongoDaoImpl<RemoteAllergies, ObjectId> implements RemoteAllergiesDao {

  public RemoteAllergies findByPatient(String stationNo, String patientDfn) {
      
    Datastore ds = super.getDatastore();
    Query<RemoteAllergies> query = 
        ds.createQuery(RemoteAllergies.class)
        .disableValidation()
        .filter("stationNo =", stationNo)
        .filter("patientDfn =", patientDfn);
    
   return this.find(query).get();
  }
}

