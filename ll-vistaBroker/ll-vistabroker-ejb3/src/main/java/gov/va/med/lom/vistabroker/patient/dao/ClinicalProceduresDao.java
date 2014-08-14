package gov.va.med.lom.vistabroker.patient.dao;

import gov.va.med.lom.javaUtils.misc.StringUtils;
import gov.va.med.lom.vistabroker.dao.BaseDao;
import gov.va.med.lom.vistabroker.patient.data.ClinicalProcedure;

public class ClinicalProceduresDao extends BaseDao {
	
  private ClinicalProcedure clinicalProcedure;
  
  // CONSTRUCTORS
  public ClinicalProceduresDao() {
    super();
  }
  
  public ClinicalProceduresDao(BaseDao baseDao) {
    super(baseDao);
  }
  
  // RPC API
  /**
   * Returns the TIU note IEN, consult IEN, and order ien for a given instrument order number.   
   *            
   * orderNumber - This is the instrument order entry number
   */
  public ClinicalProcedure getClinicalProcedure(String orderEntryNumber) throws Exception {
    setDefaultContext("R1MDAA RPC MAIN MENU");
    setDefaultRpcName("R1MDAA CP IENS");
    Object[] params = {orderEntryNumber};
    String x = sCall(params);
    clinicalProcedure = new ClinicalProcedure();
    clinicalProcedure.setRpcResult(x);
    if (x.length() > 2) {
      clinicalProcedure.setOrderNumber(orderEntryNumber);
      clinicalProcedure.setDfn(StringUtils.piece(x, 1));
      clinicalProcedure.setTiuNoteIen(StringUtils.piece(x, 2));
      clinicalProcedure.setConsultIen(StringUtils.piece(x, 3));
      clinicalProcedure.setOrderIen(StringUtils.piece(x, 4));
    }
    return clinicalProcedure;
  }
 
  /**
   * Closes the consult and TIU note associated with a procedure.   
   *            
   * tiuNoteIen - This is the TIU note internal entry number
   */
  public String closeCpNote(String tiuNoteIen) throws Exception {
    setDefaultContext("R1MDAA RPC MAIN MENU");
    setDefaultRpcName("R1MDAA MDCLOSE");
    Object[] params = {"CLOSE", tiuNoteIen};
    return sCall(params);
  }
  
}
