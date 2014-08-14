package gov.va.med.lom.avs.dao;

import javax.ejb.Local;
import org.bson.types.ObjectId;

import gov.va.med.lom.avs.model.Patient;

@Local
public interface PatientsDao extends BaseMongoDao<Patient, ObjectId> {

  public abstract Patient find(String stationNo, String patientDfn);
	
}
