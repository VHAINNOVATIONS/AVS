package gov.va.med.lom.avs.dao.hibernate;

import gov.va.med.lom.avs.dao.UsageLogDao;
import gov.va.med.lom.avs.model.UsageLog;

import gov.va.med.lom.jpa.foundation.dao.impl.BaseEntityDaoJpa;

import javax.ejb.Local;
import javax.ejb.Stateless;

/**
 * Usage Log DAO Implementation
 */
@Stateless(name="gov.va.med.lom.avs.dao.UsageLogDao")
@Local(UsageLogDao.class)
public class UsageLogDaoImpl extends BaseEntityDaoJpa<UsageLog, Long> implements UsageLogDao {

}

