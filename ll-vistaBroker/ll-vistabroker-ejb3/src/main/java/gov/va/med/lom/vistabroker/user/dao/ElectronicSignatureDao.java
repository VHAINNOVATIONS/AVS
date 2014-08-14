package gov.va.med.lom.vistabroker.user.dao;

import gov.va.med.lom.javaUtils.misc.StringUtils;
import gov.va.med.lom.vistabroker.dao.BaseDao;
import gov.va.med.lom.vistabroker.user.data.ActionResult;
import gov.va.med.lom.vistabroker.user.data.Signer;
import gov.va.med.lom.vistabroker.util.Hash;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ElectronicSignatureDao extends BaseDao {
	
  // CONSTRUCTORS
  public ElectronicSignatureDao() {
    super();
  }
  
  public ElectronicSignatureDao(BaseDao baseDao) {
    super(baseDao);
  }
  
  // RPC API  
  public ActionResult actOnDocument(String ien, String actionName) throws Exception {
    setDefaultContext("OR CPRS GUI CHART");
    setDefaultRpcName("TIU AUTHORIZATION");
    Object[] params = {ien, actionName};
    String x = sCall(params);
    ActionResult actionResult = new ActionResult();
    actionResult.setSuccess(StringUtils.piece(x, 1).equals("1"));
    actionResult.setMessage(StringUtils.piece(x, 2));
    return actionResult;
  }
  
  public boolean canChangeCosigner(String ien) throws Exception {
    setDefaultContext("OR CPRS GUI CHART");
    setDefaultRpcName("TIU CAN CHANGE COSIGNER?");
    String x = sCall(ien);
    return StringUtils.piece(x, 1).equals("1");
  }
  
  public boolean authorSignedDocument(String ien, String duz) throws Exception {
    setDefaultContext("OR CPRS GUI CHART");
    setDefaultRpcName("TIU HAS AUTHOR SIGNED?");
    Object[] params = {ien, duz};
    String x = sCall(params);
    return StringUtils.piece(x, 1).equals("1");
  }  
  
  public boolean canCosign(String titleIen, int docType, String duz) throws Exception {
    setDefaultContext("OR CPRS GUI CHART");
    setDefaultRpcName("TIU REQUIRES COSIGNATURE");
    if (!titleIen.equals("0"))
      docType = 0;
    Object[] params = {titleIen, docType, duz};
    String x = sCall(params);
    return !StringUtils.piece(x, 1).equals("1");
  }    
  
  public boolean tiuRequiresCosignature(int docType, String duz) throws Exception {
    setDefaultContext("OR CPRS GUI CHART");
    setDefaultRpcName("TIU REQUIRES COSIGNATURE");
    Object[] params = {docType, 0, duz};
    String x = sCall(params);
    return StringUtils.piece(x, 1).equals("1");
  }     
  
  public boolean mustCosignDocument(String ien) throws Exception {
    setDefaultContext("OR CPRS GUI CHART");
    setDefaultRpcName("TIU WHICH SIGNATURE ACTION");
    String x = sCall(ien);
    return x.equalsIgnoreCase("COSIGNATURE");
  }    
  
  public boolean isValidElectronicSignatureCode(String code) throws Exception {
    setDefaultContext("OR CPRS GUI CHART");
    setDefaultRpcName("ORWU VALIDSIG");
    Object[] params = {Hash.encrypt(code)};
    String x = sCall(params);
    return x.equals("1");
  }  
  
  public List<Signer> getCurrentSigners(String ien) throws Exception {
    setDefaultContext("OR CPRS GUI CHART");
    setDefaultRpcName("TIU GET ADDITIONAL SIGNERS");
    List<String> list = lCall(ien);
    List<Signer> signers = new ArrayList<Signer>(); 
    for(String s : list) {
      Signer signer = new Signer();
      signer.setDuz(StringUtils.piece(s, 1));
      signer.setName(StringUtils.mixedCase(StringUtils.piece(s, 2)));
      signer.setComment(StringUtils.piece(s, 3));
      signer.setRpcResult(s);
      signers.add(signer);
    }    
    return signers;
  }  

  public List<Signer> updateAdditionalSigners(String ien, List<Signer> signersList) throws Exception {
    setDefaultContext("OR CPRS GUI CHART");
    setDefaultRpcName("TIU UPDATE ADDITIONAL SIGNERS");
    
    List<String> duzList = new ArrayList<String>();
    for (Signer signer : signersList) {
      duzList.add(signer.getDuz());
    }
    Object[] params = {ien, duzList};
    List<String> list = lCall(params);
    List<Signer> signers = new ArrayList<Signer>(); 
    for(String s : list) {
      Signer signer = new Signer();
      signer.setDuz(StringUtils.piece(s, 1));
      signer.setName(StringUtils.mixedCase(StringUtils.piece(s, 2)));
      signer.setComment(StringUtils.piece(s, 3));
      signer.setRpcResult(s);
      signers.add(signer);
    }    
    return signers;
  }  
  
  public String changeCosigner(String ien, String cosignerDuz) throws Exception {
    setDefaultContext("OR CPRS GUI CHART");
    setDefaultRpcName("TIU UPDATE RECORD");
    HashMap<String, String> map = new HashMap<String, String>();
    if (!cosignerDuz.equals("0"))
      map.put("1208", String.valueOf(cosignerDuz));
    else
      map.put("1208", "@");  
    Object[] params = {ien, map};
    String x = sCall(params);
    return StringUtils.piece(x, 2);
  }
  
  public ActionResult signDocument(String ien, String esCode) throws Exception {
    setDefaultContext("OR CPRS GUI CHART");
    setDefaultRpcName("TIU SIGN RECORD");
    Object[] params = {ien, Hash.encrypt(esCode)};
    String x = sCall(params);
    ActionResult actionResult = new ActionResult();
    actionResult.setSuccess(StringUtils.piece(x, 1).equals("1"));
    actionResult.setMessage(StringUtils.piece(x, 2));
    return actionResult;
  }
  
  /*
   * This RPC sets the file attributes necessary to close a document by administrative action.
   * The "mode" parameter is th alphabetic code for the manner in which the document was closed
   * (i.e. "S" for scanned document, or "M" for manual closure).
   * If successful, this RPC returns the IEN in the TIU document file of the document that was closed.
   * Otherwise, it will return: 0^<Explanatory Message>
   */
  public ActionResult tiuAdminClose(String noteIen, String mode, String userDuz) throws Exception {
    setDefaultContext("OR CPRS GUI CHART");
    setDefaultRpcName("TIU SET ADMINISTRATIVE CLOSURE");
    Object[] params = {noteIen, mode, userDuz};
    String x = sCall(params);
    ActionResult actionResult = new ActionResult();
    actionResult.setSuccess(!StringUtils.piece(x, 1).equals("0"));
    actionResult.setMessage(StringUtils.piece(x, 2));
    return actionResult;
  }
  
}