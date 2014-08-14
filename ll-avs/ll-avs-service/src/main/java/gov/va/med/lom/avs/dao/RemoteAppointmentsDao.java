package gov.va.med.lom.avs.dao;

import javax.ejb.Local;
import org.bson.types.ObjectId;

import gov.va.med.lom.avs.model.RemoteAppointments;

@Local
public interface RemoteAppointmentsDao extends BaseMongoDao<RemoteAppointments, ObjectId> {

  public abstract RemoteAppointments findByPatient(String stationNo, String patientDfn);
  
}
