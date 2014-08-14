package gov.va.med.lom.vistabroker.patient.dao;

import gov.va.med.lom.vistabroker.dao.BaseDao;

public class PatientInquiryDao extends BaseDao {
  
  // CONSTRUCTORS
  public PatientInquiryDao() {
    super();
  }
  
  public PatientInquiryDao(BaseDao baseDao) {
    super(baseDao);
  }
  
  // RPC API    
	public String getPatientInquiry(String dfn) throws Exception {
    setDefaultContext("OR CPRS GUI CHART");
    setDefaultRpcName("ORWPT PTINQ");
    return sCall(dfn);
	}
  
}
