package gov.va.med.lom.javaBroker.rpc.lists;

import java.util.ArrayList;

import gov.va.med.lom.javaBroker.rpc.*;
import gov.va.med.lom.javaBroker.rpc.lists.models.*;
import gov.va.med.lom.javaBroker.util.StringUtils;

public class ProceduresRpc extends AbstractRpc {
	
  // FIELDS
  private ProceduresList proceduresList;

  // CONSTRUCTORS
  public ProceduresRpc() throws BrokerException {
    super();
  }
  
  public ProceduresRpc(RpcBroker rpcBroker) throws BrokerException {
    super(rpcBroker);
  }
  
  // RPC API
  /**
   * Returns init values for procedures dialog.
   */
  public synchronized String[] odForProcedures() throws BrokerException {
    if (setContext("OR CPRS GUI CHART")) {
      Object[] params = {"P"};
      ArrayList list = lCall("ORWDCN32 DEF", params);
      String[] results = new String[list.size()];
      for (int i = 0; i < results.length; i++)
        results[i] = (String)list.get(i);
      return results;
    } else 
      throw getCreateContextException("OR CPRS GUI CHART");
  }
  
  public synchronized ProceduresList getSubSetOfProcedures(String startFrom, int direction) throws BrokerException {
    if (setContext("OR CPRS GUI CHART")) {
      String[] params = {startFrom, String.valueOf(direction)};
      ArrayList list = lCall("ORWDCN32 PROCEDURES", params);
      proceduresList = getItems(list, false);
      return proceduresList;
    } else 
      throw getCreateContextException("OR CPRS GUI CHART");
  }
  
  // Parses the result list from VistA and returns array of patient list item objects
  private ProceduresList getItems(ArrayList list, boolean mixedCase) {
    proceduresList = new ProceduresList();
    if (returnRpcResult) {
      StringBuffer sb = new StringBuffer();
      for(int i = 0; i < list.size(); i++)
        sb.append((String)list.get(i) + "\n");
      proceduresList.setRpcResult(sb.toString().trim());
    }     
    Procedure[] procedures = new Procedure[list.size()]; 
    for(int i=0; i < list.size(); i++) {
      Procedure procedure = new Procedure();
      String x = (String)list.get(i);
      if (returnRpcResult)
        procedure.setRpcResult(x);
      String ien = StringUtils.piece(x, 1);
      String name;
      if (mixedCase)
        name = StringUtils.mixedCase(StringUtils.piece(x, 2));
      else
        name = StringUtils.piece(x, 2);
      procedure.setIen(ien);
      procedure.setName(name);  
      procedures[i] = procedure;
    }
    proceduresList.setProcedures(procedures);
    return proceduresList;    
  }  
}
