package gov.va.med.lom.avs.dao.morphia;

import javax.ejb.Local;
import javax.ejb.Stateless;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.query.Query;
import org.bson.types.ObjectId;

import gov.va.med.lom.avs.dao.ClinicsDao;
import gov.va.med.lom.avs.model.Clinic;

@Stateless(name="gov.va.med.lom.avs.dao.ClinicsDao")
@Local(ClinicsDao.class)
public class ClinicsDaoImpl extends BaseMongoDaoImpl<Clinic, ObjectId> implements ClinicsDao {

  public Clinic find(String stationNo, String clinicIen) {
      
    Datastore ds = super.getDatastore();
    Query<Clinic> query = 
        ds.createQuery(Clinic.class)
        .disableValidation()
        .filter("stationNo =", stationNo)
        .filter("ien =", clinicIen);    
    
    return this.find(query).get();
  }
  
}

