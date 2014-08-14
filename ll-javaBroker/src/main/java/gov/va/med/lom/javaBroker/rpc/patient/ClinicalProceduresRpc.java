package gov.va.med.lom.javaBroker.rpc.patient;

import gov.va.med.lom.javaBroker.rpc.*;
import gov.va.med.lom.javaBroker.rpc.patient.models.ClinicalProcedure;

import gov.va.med.lom.javaBroker.util.StringUtils;

public class ClinicalProceduresRpc extends AbstractRpc {
	
  private ClinicalProcedure clinicalProcedure;
  
  // CONSTRUCTORS
  public ClinicalProceduresRpc() throws BrokerException {
    super();
  }
  
  public ClinicalProceduresRpc(RpcBroker rpcBroker) throws BrokerException {
    super(rpcBroker);
  }
  
  // RPC API
  /**
   * Returns the TIU note IEN, consult IEN, and order ien for a given instrument order number.   
   *            
   * orderNumber - This is the instrument order entry number
   */
  public synchronized ClinicalProcedure getClinicalProcedure(String orderEntryNumber) throws BrokerException {
    if (setContext("ALS CLINICAL RPC")) {
      Object[] params = {orderEntryNumber};
      String x = sCall("ALSI CLINICAL PROCEDURE IENS", params);
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
    } else 
      throw getCreateContextException("ALS CLINICAL RPC");
  }
  
  /**
   * The Imaging capture station can mark a transaction as complete by making      
   * this call after a successful capture.  This call puts the procedure in      
   * a status of 'pr' (ready for interpretation.
   */
  public synchronized boolean updateCpConsult(String flag, String tiuDA) throws BrokerException {
    if (setContext("ALS CLINICAL RPC")) {
      Object[] params = {flag, tiuDA, flag};
      String x = sCall("MAG4 CP UPDATE CONSULT", params);
      return StringUtils.strToBool(x, "1");
    } else 
      throw getCreateContextException("ALS CLINICAL RPC");      
  }
  
}
