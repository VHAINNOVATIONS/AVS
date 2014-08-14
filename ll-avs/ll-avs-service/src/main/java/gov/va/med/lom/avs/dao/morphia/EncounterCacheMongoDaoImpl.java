package gov.va.med.lom.avs.dao.morphia;

import gov.va.med.lom.avs.dao.EncounterCacheMongoDao;
import gov.va.med.lom.avs.model.EncounterCacheMongo;
import gov.va.med.lom.avs.model.Encounter;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;

import javax.ejb.Local;
import javax.ejb.Stateless;

import org.bson.types.ObjectId;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;
import org.mongodb.morphia.Datastore;

@Stateless(name="gov.va.med.lom.avs.dao.EncounterCacheMongoDao")
@Local(EncounterCacheMongoDao.class)
public class EncounterCacheMongoDaoImpl extends BaseMongoDaoImpl<EncounterCacheMongo, ObjectId> implements EncounterCacheMongoDao {

  public EncounterCacheMongo find(String facilityNo, String patientDfn, List<String> locationIens, List<Double> datetimes, String docType){
    	
    Datastore ds = super.getDatastore();
    List<EncounterCacheMongo> list = ds.createQuery(EncounterCacheMongo.class)
      .field("facilityNo").equal(facilityNo)
      .field("patientDfn").equal(patientDfn)
      .field("docType").equal(docType)
      .asList();
    if (list.size() > 0) {
      HashMap<String, String> locationsMap = new HashMap<String, String>();
      for (String ien : locationIens) {
        locationsMap.put(ien, ien);
      }
      HashMap<Double, Double> datetimesMap = new HashMap<Double, Double>();
      for (Double dt : datetimes) {
        datetimesMap.put(dt, dt);
      } 
      for (EncounterCacheMongo cache : list) {
        boolean found = true;
        if (cache.getEncounters().size() == datetimesMap.size()) {
          for (Encounter encounter : cache.getEncounters()) {
            if (encounter.getLocation() !=  null) {
              found = found && locationsMap.containsKey(encounter.getLocation().getLocationIen()) 
                  && datetimesMap.containsKey(encounter.getEncounterDatetime());
              if (!found) {
                continue;
              }
            }
          }
          if (found) {
            return cache;
          }          
        }
      }
    }
    return null;
	}
  
  @SuppressWarnings("unchecked")  
  public List<EncounterCacheMongo> findByDates(String facilityNo, Date startDate, Date endDate) {
    
    Datastore ds = super.getDatastore();
    Query<EncounterCacheMongo> query = 
        ds.createQuery(EncounterCacheMongo.class)
        .disableValidation()
        .filter("facilityNo=", facilityNo)
        .filter("startDate=", startDate)
        .filter("endDate=", endDate);
    
    return super.find(query).asList();    
  }
  
  @SuppressWarnings("unchecked")  
  public void updateEncounterNoteIen(ObjectId objectId, String visitString, String encounterNoteIen) {
    
    Datastore ds = super.getDatastore();
    Query<EncounterCacheMongo> query = 
        ds.createQuery(EncounterCacheMongo.class)
        .disableValidation()
        .field("_id").equal(objectId);
    
    EncounterCacheMongo entity = null;
    List<Encounter> encounters = new ArrayList<Encounter>();
    List<EncounterCacheMongo> list = super.find(query).asList();
    if (list.size() == 1) { 
      entity = list.get(0);
      for (Encounter e : entity.getEncounters()) {
        if ((e.getVisitString() != null) && e.getVisitString().equals(visitString)) {
          e.setEncounterNoteIen(encounterNoteIen);
        }
        encounters.add(e);
      }
    }
    if (entity != null) {
      UpdateOperations<EncounterCacheMongo> ops = 
          ds.createUpdateOperations(EncounterCacheMongo.class).unset("encounters");
      ds.update(query, ops);
      
      ops = ds.createUpdateOperations(EncounterCacheMongo.class).addAll("encounters", encounters, true);
      ds.update(query, ops);
    }
  }
}

