package gov.va.med.lom.avs.dao.morphia;

import java.util.List;

import javax.ejb.Local;
import javax.ejb.Stateless;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.query.Query;
import org.bson.types.ObjectId;

import gov.va.med.lom.avs.dao.MedDescriptionsDao;
import gov.va.med.lom.avs.model.MedDescription;

@Stateless(name="gov.va.med.lom.avs.dao.MedDescriptionsDao")
@Local(MedDescriptionsDao.class)
public class MedDescriptionsDaoImpl extends BaseMongoDaoImpl<MedDescription, ObjectId> implements MedDescriptionsDao {

  public List<MedDescription> findByPatient(String stationNo, String patientDfn) {
      
    Datastore ds = super.getDatastore();
    Query<MedDescription> query = 
        ds.createQuery(MedDescription.class)
        .disableValidation()
        .filter("stationNo =", stationNo)
        .filter("patientDfn =", patientDfn);    
    
    return this.find(query).asList();
  }
  
  public MedDescription findByNdc(String ndc) {
   
    return super.findOne("ndc", ndc);
  }
  
}

