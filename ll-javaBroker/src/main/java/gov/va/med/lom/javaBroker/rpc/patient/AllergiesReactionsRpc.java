package gov.va.med.lom.javaBroker.rpc.patient;

import java.util.ArrayList;

import gov.va.med.lom.javaBroker.rpc.*;
import gov.va.med.lom.javaBroker.rpc.patient.models.*;
import gov.va.med.lom.javaBroker.util.StringUtils;

public class AllergiesReactionsRpc extends AbstractRpc {
  
  // FIELDS
  private AllergiesReactions allergiesReactions;
  
  // CONSTRUCTORS
  public AllergiesReactionsRpc() throws BrokerException {
    super();
  }
  
  public AllergiesReactionsRpc(RpcBroker rpcBroker) throws BrokerException {
    super(rpcBroker);
  }
  
  // RPC API
  public synchronized AllergiesReactions getAllergiesReactions(String dfn) throws BrokerException {
    if (setContext("OR CPRS GUI CHART")) {
      setDfn(dfn);
      allergiesReactions = new AllergiesReactions();
      ArrayList list = lCall("ORQQAL LIST", dfn);
      if (returnRpcResult) {
        StringBuffer sb = new StringBuffer();
        for(int i = 0; i < list.size(); i++)
          sb.append((String)list.get(i) + "\n");
        allergiesReactions.setRpcResult(sb.toString().trim());
      }    
      /*
       Returns array of patient allergies.  Returned data is delimited by "^" and
       includes: allergen/reaction, reactions/symptoms (multiple symptoms/
       reactions are possible - delimited by ";"), severity, allergy id (record
       number from the Patient Allergies file (#120.8).
      */
      if (list.size() == 1) {
        String a = StringUtils.piece((String)list.get(0), 2); 
        if (a.equalsIgnoreCase("no known allergies")) {
          allergiesReactions.setNoKnownAllergies(true);
          list.clear();
        } else if (a.equalsIgnoreCase("no allergy assessment")) { 
          allergiesReactions.setNoAllergyAssessment(true);
          list.clear();
        }
      }
      AllergyReaction[] allergiesReactionsArray = new AllergyReaction[list.size()];
      for(int i = 0; i < list.size(); i++) {
        allergiesReactionsArray[i] = new AllergyReaction();
        String x = (String)list.get(i);
        if (returnRpcResult)
          allergiesReactionsArray[i].setRpcResult(x);
        allergiesReactionsArray[i] = new AllergyReaction();
        allergiesReactionsArray[i].setDfn(dfn);
        allergiesReactionsArray[i].setIen(StringUtils.piece(x, 1));
        allergiesReactionsArray[i].setAllergy(StringUtils.piece(x, 2));
        allergiesReactionsArray[i].setSeverity(StringUtils.piece(x, 3));
        String[] s = StringUtils.pieceList(StringUtils.piece(x, 4), ';');
        allergiesReactionsArray[i].setReactionsSymptoms(s);
      }
      allergiesReactions.setAllergiesReactions(allergiesReactionsArray);
      return allergiesReactions;
    } else
      throw getCreateContextException("OR CPRS GUI CHART");
  } 
  
  public synchronized String getAllergyDetail(String dfn, String ien) throws BrokerException {
    if (setContext("OR CPRS GUI CHART")) {
      setDfn(dfn);
      Object[] params = {dfn, ien, ""};
      ArrayList list = lCall("ORQQAL DETAIL", params);
      StringBuffer sb = new StringBuffer();
      for(int i = 0; i < list.size(); i++)
        sb.append((String)list.get(i) + "\n");
      return sb.toString();
    } else
      throw getCreateContextException("OR CPRS GUI CHART");
  }  
  
}
