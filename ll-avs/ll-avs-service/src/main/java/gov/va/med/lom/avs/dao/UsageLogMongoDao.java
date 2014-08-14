package gov.va.med.lom.avs.dao;

import gov.va.med.lom.avs.model.UsageLogMongo;

import javax.ejb.Local;

import org.bson.types.ObjectId;

@Local
public interface UsageLogMongoDao extends BaseMongoDao<UsageLogMongo, ObjectId> {

}
