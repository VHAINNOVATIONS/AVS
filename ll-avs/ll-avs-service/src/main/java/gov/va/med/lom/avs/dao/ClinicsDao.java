package gov.va.med.lom.avs.dao;

import javax.ejb.Local;
import org.bson.types.ObjectId;

import gov.va.med.lom.avs.model.Clinic;

@Local
public interface ClinicsDao extends BaseMongoDao<Clinic, ObjectId> {

  public abstract Clinic find(String stationNo, String clinicIen);
	
}
