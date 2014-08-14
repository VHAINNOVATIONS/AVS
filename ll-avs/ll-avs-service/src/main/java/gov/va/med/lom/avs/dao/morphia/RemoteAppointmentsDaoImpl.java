package gov.va.med.lom.avs.dao.morphia;

import javax.ejb.Local;
import javax.ejb.Stateless;

import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.query.Query;

import gov.va.med.lom.avs.dao.RemoteAppointmentsDao;
import gov.va.med.lom.avs.model.RemoteAppointments;

@Stateless(name="gov.va.med.lom.avs.dao.RemoteAppointmentsDao")
@Local(RemoteAppointmentsDao.class)
public class RemoteAppointmentsDaoImpl extends BaseMongoDaoImpl<RemoteAppointments, ObjectId> implements RemoteAppointmentsDao {

  public RemoteAppointments findByPatient(String stationNo, String patientDfn) {
      
    Datastore ds = super.getDatastore();
    Query<RemoteAppointments> query = 
        ds.createQuery(RemoteAppointments.class)
        .disableValidation()
        .filter("stationNo =", stationNo)
        .filter("patientDfn =", patientDfn);    
    
    return this.find(query).get();
    
  }
}

