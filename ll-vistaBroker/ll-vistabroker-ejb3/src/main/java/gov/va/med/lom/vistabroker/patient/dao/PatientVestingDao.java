package gov.va.med.lom.vistabroker.patient.dao;

import gov.va.med.lom.javaUtils.misc.StringUtils;
import gov.va.med.lom.vistabroker.dao.BaseDao;

public class PatientVestingDao extends BaseDao {
  
  // CONSTRUCTORS
  public PatientVestingDao() {
    super();
  }
  
  public PatientVestingDao(BaseDao baseDao) {
    super(baseDao);
  }
  
  // RPC API    
	public boolean isPatientVested(String dfn) throws Exception {
    setDefaultContext("ARMQ RPC BROKER");
    setDefaultRpcName("ARMQ NEEDS VESTING EXAM");
    return StringUtils.strToBool(sCall(dfn), "1");
	}
  
  public boolean patientNeedsVestingCanVest(String dfn) throws Exception {
    setDefaultContext("ARMQ RPC BROKER");
    setDefaultRpcName("ARMQ NEEDS VESTING/CAN VEST");
    return StringUtils.strToBool(sCall(dfn), "1");
  }
  
}
