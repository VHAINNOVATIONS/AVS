package gov.va.med.lom.avs.dao.morphia;

import javax.ejb.Local;
import javax.ejb.Stateless;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.query.Query;
import org.bson.types.ObjectId;

import gov.va.med.lom.avs.dao.PatientsDao;
import gov.va.med.lom.avs.model.Patient;

@Stateless(name="gov.va.med.lom.avs.dao.Patient")
@Local(PatientsDao.class)
public class PatientsDaoImpl extends BaseMongoDaoImpl<Patient, ObjectId> implements PatientsDao {

  public Patient find(String stationNo, String patientDfn) {
      
    Datastore ds = super.getDatastore();
    Query<Patient> query = 
        ds.createQuery(Patient.class)
        .disableValidation()
        .filter("stationNo =", stationNo)
        .filter("dfn =", patientDfn);    
    
    return this.find(query).get();
  }
  
}

