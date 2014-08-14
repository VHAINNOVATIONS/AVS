package gov.va.med.lom.javaBroker.rpc.lists;

import java.util.ArrayList;

import gov.va.med.lom.javaBroker.rpc.*;
import gov.va.med.lom.javaBroker.rpc.lists.models.*;
import gov.va.med.lom.javaBroker.util.StringUtils;

public class CodesRpc extends AbstractRpc {
	
  // CONSTRUCTORS
  public CodesRpc() throws BrokerException {
    super();
  }
  
  public CodesRpc(RpcBroker rpcBroker) throws BrokerException {
    super(rpcBroker);
  }
  
  // RPC API
  /**
   * This will return a list of active ICD9 codes for a lookup value.   
   *            
   * dsix - This is the lookup value for file 80
   * scr - If "P", then the matches to the lookup value will be screened to only     
   * return diagnoses which are acceptable as principal diagnoses.                 
   */
  public synchronized Icd9Code[] listICD9Codes(String dsix, String scr) throws BrokerException {
    if (setContext("ALS CLINICAL RPC")) {
      Object[] params = {dsix, scr};
      ArrayList list = lCall("DSIC ICD9 GET LIST", params);
      Icd9Code[] icd9Codes = new Icd9Code[list.size()];
      for (int i = 0; i < icd9Codes.length; i++) {
        icd9Codes[i] = new Icd9Code();
        String x = (String)list.get(i);
        icd9Codes[i].setRpcResult(x);
        icd9Codes[i].setIen(StringUtils.piece(x, 1));
        icd9Codes[i].setCode(StringUtils.piece(x, 2));
        icd9Codes[i].setDiagnosis(StringUtils.piece(x, 3));
      }
      return icd9Codes;
    } else 
      throw getCreateContextException("ALS CLINICAL RPC");
  }
  
  /**
   * Performs a search on a CPT string and returns an array list of matches from file #81.        
   * 
   * val - CPT search string. 
   */
  public synchronized CptCode[] listCPTCodes(String val) throws BrokerException {
    if (setContext("ALS CLINICAL RPC")) {
      Object[] params = {val}; 
      ArrayList list = lCall("EC GETCPTLST", params);
      CptCode[] cptCodes = new CptCode[list.size()];
      for (int i = 0; i < cptCodes.length; i++) {
        cptCodes[i] = new CptCode();
        String x = (String)list.get(i);
        cptCodes[i].setRpcResult(x);
        cptCodes[i].setIen(StringUtils.piece(x, 1));
        cptCodes[i].setCode(StringUtils.piece(x, 2));
        cptCodes[i].setDescription(StringUtils.piece(x, 3));
      }
      return cptCodes;
    } else 
      throw getCreateContextException("ALS CLINICAL RPC");
  }
  
  /**
   * Returns ICD9 diagnosis list based on lexicon look-up.   
   *            
   * lex - lexicon entry (diagnosis)
   * dt - date in FM format     
   * return results of the diagnosis lookup.                 
   */
  public synchronized Icd9Code[] listIcd9Lexicon(String lex, double dt) throws BrokerException {
    if (setContext("OR CPRS GUI CHART")) {
      Object[] params = {lex, "ICD", String.valueOf(dt)};
      ArrayList list = lCall("ORWPCE LEX", params);
      Icd9Code[] icd9Codes = new Icd9Code[list.size()];
      for (int i = 0; i < icd9Codes.length; i++) {
        icd9Codes[i] = new Icd9Code();
        String x = (String)list.get(i);
        icd9Codes[i].setRpcResult(x);
        icd9Codes[i].setIen(StringUtils.piece(x, 1));
        icd9Codes[i].setDiagnosis(StringUtils.piece(x, 2));
      }
      return icd9Codes;      
    } else 
      throw getCreateContextException("OR CPRS GUI CHART");
  }
  
  /**
   * Returns an ICD9 code associated with a lexicon entry.
   *            
   * ien - ien of diagnosis
   * dt - date in FM format     
   * return result of code lookup.                
   */
  public synchronized String lexiconToIcd9Code(long ien, double dt) throws BrokerException {
    if (setContext("OR CPRS GUI CHART")) {
      Object[] params = {ien, "ICD", String.valueOf(dt)};
      return sCall("ORWPCE LEXCODE", params);
    } else 
      throw getCreateContextException("OR CPRS GUI CHART");    
  }
  
  /**
   * Returns CPT procedures list based on lexicon look-up.   
   *            
   * lex - lexicon entry (procedure)
   * dt - date in FM format     
   * return results of the procedure lookup.                 
   */
  public synchronized CptCode[] listCptLexicon(String lex, double dt) throws BrokerException {
    if (setContext("OR CPRS GUI CHART")) {
      Object[] params = {lex, "CPT", String.valueOf(dt)};
      ArrayList list = lCall("ORWPCE LEX", params);
      CptCode[] cptCodes = new CptCode[list.size()];
      for (int i = 0; i < cptCodes.length; i++) {
        cptCodes[i] = new CptCode();
        String x = (String)list.get(i);
        cptCodes[i].setRpcResult(x);
        cptCodes[i].setIen(StringUtils.piece(x, 1));
        cptCodes[i].setDescription(StringUtils.piece(x, 2));
      }
      return cptCodes;      
    } else 
      throw getCreateContextException("OR CPRS GUI CHART");
  }
  
  /**
   * Returns a CPT code associated with a lexicon entry.
   *            
   * ien - ien of procedure
   * dt - date in FM format     
   * return result of code lookup.                
   */
  public synchronized String lexiconToCptCode(long ien, double dt) throws BrokerException {
    if (setContext("OR CPRS GUI CHART")) {
      Object[] params = {ien, "CPT", String.valueOf(dt)};
      return sCall("ORWPCE LEXCODE", params);
    } else 
      throw getCreateContextException("OR CPRS GUI CHART");    
  }
  
}
