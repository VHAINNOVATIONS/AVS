package gov.va.med.lom.avs.dao.morphia;

import gov.va.med.lom.avs.dao.UsageLogMongoDao;
import gov.va.med.lom.avs.model.UsageLogMongo;

import javax.ejb.Local;
import javax.ejb.Stateless;

import org.bson.types.ObjectId;

@Stateless(name="gov.va.med.lom.avs.dao.UsageLogMongoDao")
@Local(UsageLogMongoDao.class)
public class UsageLogMongoDaoImpl extends BaseMongoDaoImpl<UsageLogMongo, ObjectId> implements UsageLogMongoDao {

}

