package gov.va.med.lom.avs.dao;

import gov.va.med.lom.avs.model.EncounterCacheMongo;

import javax.ejb.Local;
import java.util.List;
import java.util.Date;

import org.bson.types.ObjectId;

@Local
public interface EncounterCacheMongoDao extends BaseMongoDao<EncounterCacheMongo, ObjectId> {

	public abstract EncounterCacheMongo find(String facilityNo, String patientDfn, List<String> locationIens, 
	    List<Double> datetimes, String docType);
	public abstract List<EncounterCacheMongo> findByDates(String facilityNo, Date beginDate, Date endDate);
	public abstract void updateEncounterNoteIen(ObjectId objectId, String visitString, String encounterNoteIen);
}
