package gov.va.med.lom.avs.dao;

import gov.va.med.lom.avs.model.UsageLog;

import gov.va.med.lom.jpa.foundation.dao.BaseEntityDao;

import javax.ejb.Local;

/**
 * Usage Log DAO object
 */
@Local
public interface UsageLogDao extends BaseEntityDao<UsageLog, Long> {

}
