package gov.va.med.lom.vistabroker.patient.dao;

import gov.va.med.lom.vistabroker.dao.BaseDao;

public class InsuranceDao extends BaseDao {
  
  // CONSTRUCTORS
  public InsuranceDao() {
    super();
  }
  
  public InsuranceDao(BaseDao baseDao) {
    super(baseDao);
  }
  
  // RPC API
  public String getInsuranceInfo(String dfn) throws Exception {
    setDefaultContext("ALS CPRS COM OBJECT RPCS");
    setDefaultRpcName("ALSI INS COM");
    return sCall(dfn);
  }
  
}