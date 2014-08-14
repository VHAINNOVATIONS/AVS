package gov.va.med.lom.vistabroker.patient.dao;

import gov.va.med.lom.vistabroker.dao.BaseDao;

import java.util.List;

public class CombatVetDao extends BaseDao {
  
  // CONSTRUCTORS
  public CombatVetDao() {
    super();
  }
  
  public CombatVetDao(BaseDao baseDao) {
    super(baseDao);
  }
  
  // RPC API
  public List<String> getCombatVetInfo(String dfn) throws Exception {
    setDefaultContext("ALS CPRS COM OBJECT RPCS");
    setDefaultRpcName("ALSI COMBAT VET");
    return lCall(dfn);
  }
}