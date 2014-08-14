package gov.va.med.lom.vistabroker.lists.dao;

import gov.va.med.lom.javaUtils.misc.StringUtils;
import gov.va.med.lom.vistabroker.dao.BaseDao;
import gov.va.med.lom.vistabroker.lists.data.CptCode;
import gov.va.med.lom.vistabroker.lists.data.Icd9Code;

import java.util.ArrayList;
import java.util.List;

public class CodesDao extends BaseDao {
	
  // CONSTRUCTORS
  public CodesDao() {
    super();
  }
  
  public CodesDao(BaseDao baseDao) {
    super(baseDao);
  }
  
  // RPC API
  /**
   * This will return a list of active ICD9 codes for a lookup value.   
   *            
   * dsix - This is the lookup value for file 80
   * scr - If "P", then the matches to the lookup value will be screened to only     
   * return diagnoses which are acceptable as principal diagnoses.                 
   */
  public synchronized List<Icd9Code> listICD9Codes(String dsix, String scr) throws Exception {
    setDefaultContext("ALS CLINICAL RPC");
    setDefaultRpcName("DSIC ICD9 GET LIST");
    Object[] params = {dsix, scr};
    List<String> list = lCall(params);
    List<Icd9Code> icd9Codes = new ArrayList<Icd9Code>();
    for (String s : list) {
      Icd9Code icd9Code = new Icd9Code();
      icd9Code.setRpcResult(s);
      icd9Code.setIen(StringUtils.piece(s, 1));
      icd9Code.setCode(StringUtils.piece(s, 2));
      icd9Code.setDiagnosis(StringUtils.piece(s, 3));
      icd9Codes.add(icd9Code);
    }
    return icd9Codes;
  }
  
  /**
   * Performs a search on a CPT string and returns an array list of matches from file #81.        
   * 
   * val - CPT search string. 
   */
  public synchronized List<CptCode> listCPTCodes(String val) throws Exception {
    setDefaultContext("ALS CLINICAL RPC");
    setDefaultRpcName("EC GETCPTLST");
    List<String> list = lCall(val);
    List<CptCode> cptCodes = new ArrayList<CptCode>();
    for (String s : list) {
      CptCode cptCode = new CptCode();
      cptCode.setRpcResult(s);
      cptCode.setIen(StringUtils.piece(s, 1));
      cptCode.setCode(StringUtils.piece(s, 2));
      cptCode.setDescription(StringUtils.piece(s, 3));
      cptCodes.add(cptCode);
    }
    return cptCodes;
  }
  
  /**
   * Returns ICD9 diagnosis list based on lexicon look-up.   
   *            
   * lex - lexicon entry (diagnosis)
   * dt - date in FM format     
   * return results of the diagnosis lookup.                 
   */
  public synchronized List<Icd9Code> listIcd9Lexicon(String lex, double dt) throws Exception {
    setDefaultContext("OR CPRS GUI CHART");
    setDefaultRpcName("ORWPCE LEX");
    Object[] params = {lex, "ICD", String.valueOf(dt)};
    List<String> list = lCall(params);
    List<Icd9Code> icd9CodesList = new ArrayList<Icd9Code>();
    for (String s : list) {
      Icd9Code icd9Code = new Icd9Code();
      icd9Code.setRpcResult(s);
      icd9Code.setIen(StringUtils.piece(s, 1));
      String x = StringUtils.piece(s, 2);
      icd9Code.setDiagnosis(StringUtils.piece(x, '(', 1).trim());
      icd9Code.setCode(parseCode(x));
      icd9CodesList.add(icd9Code);
    }
    return icd9CodesList;     
  }
  
  /**
   * Returns an ICD9 code associated with a lexicon entry.
   *            
   * ien - ien of diagnosis
   * dt - date in FM format     
   * return result of code lookup.                
   */
  public synchronized String lexiconToIcd9Code(String ien, double dt) throws Exception {
    setDefaultContext("OR CPRS GUI CHART");
    setDefaultRpcName("ORWPCE LEXCODE");
    Object[] params = {ien, "ICD", String.valueOf(dt)};
    return sCall(params);
  }
  
  /**
   * Returns CPT procedures list based on lexicon look-up.   
   *            
   * lex - lexicon entry (procedure)
   * dt - date in FM format     
   * return results of the procedure lookup.                 
   */
  public synchronized List<CptCode> listCptLexicon(String lex, double dt) throws Exception {
    setDefaultContext("OR CPRS GUI CHART");
    setDefaultRpcName("ORWPCE LEX");
    Object[] params = {lex, "CPT", String.valueOf(dt)};
    List<String> list = lCall(params);
    List<CptCode> cptCodes = new ArrayList<CptCode>();
    for (String s : list) {
      CptCode cptCode = new CptCode();
      cptCode.setRpcResult(s);
      cptCode.setIen(StringUtils.piece(s, 1));
      String x = StringUtils.piece(s, 2);
      cptCode.setDescription(StringUtils.piece(x, '(', 1).trim());
      cptCode.setCode(parseCode(x));
      //cptCode.setCode(StringUtils.piece(s, 2));
      //cptCode.setDescription(StringUtils.piece(s, 3));
      cptCodes.add(cptCode);
    }
    return cptCodes;
  }
  
  /**
   * Returns a CPT code associated with a lexicon entry.
   *            
   * ien - ien of procedure
   * dt - date in FM format     
   * return result of code lookup.                
   */
  public synchronized String lexiconToCptCode(String ien, double dt) throws Exception {
    setDefaultContext("OR CPRS GUI CHART");
    setDefaultRpcName("ORWPCE LEXCODE");
    Object[] params = {ien, "CPT", String.valueOf(dt)};
    return sCall(params);
  }
  
  private static String parseCode(String x) {
    StringBuffer code = new StringBuffer();
    for (int i = x.lastIndexOf(')')-1; x.charAt(i) != ' '; i--) {
      code.insert(0, x.charAt(i));
    }
    return code.toString();
  }
  
}
