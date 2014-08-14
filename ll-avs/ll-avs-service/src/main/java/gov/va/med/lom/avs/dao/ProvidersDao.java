package gov.va.med.lom.avs.dao;

import javax.ejb.Local;
import org.bson.types.ObjectId;

import gov.va.med.lom.avs.model.Provider;

@Local
public interface ProvidersDao extends BaseMongoDao<Provider, ObjectId> {

  public abstract Provider find(String stationNo, String providerDuz);
	
}
