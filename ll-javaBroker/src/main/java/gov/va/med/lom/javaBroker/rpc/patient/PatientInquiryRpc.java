package gov.va.med.lom.javaBroker.rpc.patient;

import gov.va.med.lom.javaBroker.rpc.*;

public class PatientInquiryRpc extends AbstractRpc {
  
  private String patientInquiry;
    
  // CONSTRUCTORS
  public PatientInquiryRpc() throws BrokerException {
    super();
  }
  
  public PatientInquiryRpc(RpcBroker rpcBroker) throws BrokerException {
    super(rpcBroker);
  }
  
  // RPC API    
	public synchronized String getPatientInquiry(String dfn) throws BrokerException {
    if (setContext("OR CPRS GUI CHART")) {
      setDfn(dfn);
      patientInquiry = sCall("ORWPT PTINQ", dfn);
      return patientInquiry;
    } else
      throw getCreateContextException("OR CPRS GUI CHART");
	}
  
}
