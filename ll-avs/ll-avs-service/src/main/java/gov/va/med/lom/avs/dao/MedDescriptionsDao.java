package gov.va.med.lom.avs.dao;

import javax.ejb.Local;

import java.util.List;
import org.bson.types.ObjectId;

import gov.va.med.lom.avs.model.MedDescription;

@Local
public interface MedDescriptionsDao extends BaseMongoDao<MedDescription, ObjectId> {

  public abstract List<MedDescription> findByPatient(String stationNo, String patientDfn);
  public abstract MedDescription findByNdc(String ndc);
	
}
