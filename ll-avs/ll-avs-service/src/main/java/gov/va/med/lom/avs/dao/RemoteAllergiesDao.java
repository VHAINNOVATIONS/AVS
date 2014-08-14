package gov.va.med.lom.avs.dao;

import javax.ejb.Local;
import org.bson.types.ObjectId;

import gov.va.med.lom.avs.model.RemoteAllergies;

@Local
public interface RemoteAllergiesDao extends BaseMongoDao<RemoteAllergies, ObjectId> {

  public abstract RemoteAllergies findByPatient(String stationNo, String patientDfn);
  
}
