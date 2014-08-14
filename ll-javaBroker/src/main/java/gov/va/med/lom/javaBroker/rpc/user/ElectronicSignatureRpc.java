package gov.va.med.lom.javaBroker.rpc.user;

import java.util.ArrayList;

import gov.va.med.lom.javaBroker.rpc.*;
import gov.va.med.lom.javaBroker.rpc.user.models.*;
import gov.va.med.lom.javaBroker.util.Hash;
import gov.va.med.lom.javaBroker.util.StringUtils;

public class ElectronicSignatureRpc extends AbstractRpc {
	
  // CONSTRUCTORS
  public ElectronicSignatureRpc() throws BrokerException {
    super();
  }
  
  public ElectronicSignatureRpc(RpcBroker rpcBroker) throws BrokerException {
    super(rpcBroker);
  }
  
  // RPC API  
  public synchronized ActionResult actOnDocument(String ien, String actionName) throws BrokerException {
    if (setContext("OR CPRS GUI CHART")) {
      Object[] params = {ien, actionName};
      String x = sCall("TIU AUTHORIZATION", params);
      ActionResult actionResult = new ActionResult();
      actionResult.setSuccess(StringUtils.piece(x, 1).equals("1"));
      actionResult.setMessage(StringUtils.piece(x, 2));
      if (returnRpcResult)
        actionResult.setRpcResult(x);
      return actionResult;
    } else
      throw getCreateContextException("OR CPRS GUI CHART");
  }
  
  public synchronized boolean canChangeCosigner(String ien) throws BrokerException {
    if (setContext("OR CPRS GUI CHART")) {
      String x = sCall("TIU CAN CHANGE COSIGNER?", ien);
      return StringUtils.piece(x, 1).equals("1");
    } else
      throw getCreateContextException("OR CPRS GUI CHART");
  }
  
  public synchronized boolean authorSignedDocument(String ien, String duz) throws BrokerException {
    if (setContext("OR CPRS GUI CHART")) {
      Object[] params = {ien, duz};
      String x = sCall("TIU HAS AUTHOR SIGNED?", params);
      return StringUtils.piece(x, 1).equals("1");
    } else
      throw getCreateContextException("OR CPRS GUI CHART");
  }  
  
  public synchronized boolean canCosign(String titleIen, int docType, String duz) throws BrokerException {
    if (setContext("OR CPRS GUI CHART")) {
      if ((titleIen != null) && !titleIen.isEmpty())
        docType = 0;
      Object[] params = {titleIen, String.valueOf(docType), duz};
      String x = sCall("TIU REQUIRES COSIGNATURE", params);
      return !StringUtils.piece(x, 1).equals("1");
    } else
      throw getCreateContextException("OR CPRS GUI CHART");
  }    
  
  public synchronized boolean mustCosignDocument(String ien) throws BrokerException {
    if (setContext("OR CPRS GUI CHART")) {
      String x = sCall("TIU WHICH SIGNATURE ACTION", ien);
      return x.equalsIgnoreCase("COSIGNATURE");
    } else
      throw getCreateContextException("OR CPRS GUI CHART");
  }    
  
  public synchronized boolean isValidElectronicSignatureCode(String code) throws BrokerException {
    if (setContext("OR CPRS GUI CHART")) {
      Object[] params = {Hash.encrypt(code)};
      String x = sCall("ORWU VALIDSIG", params);
      return x.equals("1");
    } else
      throw getCreateContextException("OR CPRS GUI CHART");
  }  
  
  public synchronized SignersList getCurrentSigners(String ien) throws BrokerException {
    if (setContext("OR CPRS GUI CHART")) {
      ArrayList list = lCall("TIU GET ADDITIONAL SIGNERS", ien);
      Signer[] signers = new Signer[list.size()]; 
      for(int i=0; i < list.size(); i++) {
        Signer signer = new Signer();
        String x = (String)list.get(i);
        if (returnRpcResult)
          signer.setRpcResult(x);
        String duz = StringUtils.piece(x, 1);
        String name = StringUtils.mixedCase(StringUtils.piece(x, 2));
        String comment =StringUtils.piece(x, 3);
        signer.setDuz(duz);
        signer.setName(name);
        signer.setComment(comment);
        signers[i] = signer;
      }    
      SignersList signersList = new SignersList();
      signersList.setSigners(signers);
      return signersList;
    } else
      throw getCreateContextException("OR CPRS GUI CHART");
  }  

  public synchronized SignersList updateAdditionalSigners(String ien, SignersList signersList) throws BrokerException {
    if (setContext("OR CPRS GUI CHART")) {
      Signer[] signersArray = signersList.getSigners();
      String[] results = new String[signersArray.length];
      for (int i = 0; i < results.length; i++)
        results[i] = signersArray[i].getDuz();
      Object[] params = {ien, results};
      ArrayList list = lCall("TIU UPDATE ADDITIONAL SIGNERS", params);
      Signer[] signers = new Signer[list.size()]; 
      for(int i=0; i < list.size(); i++) {
        Signer signer = new Signer();
        String x = (String)list.get(i);
        if (returnRpcResult)
          signer.setRpcResult(x);
        String duz = StringUtils.piece(x, 1);
        String name= StringUtils.mixedCase(StringUtils.piece(x, 2));
        signer.setDuz(duz);
        signer.setName(name);      
        signers[i] = signer;
      }    
      SignersList resultsList = new SignersList();
      resultsList.setSigners(signers);
      return resultsList;
    } else
      throw getCreateContextException("OR CPRS GUI CHART");
  }  
  
  public synchronized String changeCosigner(String ien, String cosignerDuz) throws BrokerException {
    if (setContext("OR CPRS GUI CHART")) {
      Mult mult = new Mult();
      if ((cosignerDuz != null) && !cosignerDuz.isEmpty()) 
        mult.setMultiple("1208", cosignerDuz);
      else
        mult.setMultiple("1208", "@");  
      Object[] params = {ien, mult};
      String x = sCall("ORWU VALIDSIG", params);
      return StringUtils.piece(x, 2);
      } else
        throw getCreateContextException("OR CPRS GUI CHART");
  }
  
  public synchronized ActionResult signDocument(String ien, String esCode) throws BrokerException {
    if (setContext("OR CPRS GUI CHART")) {
      Object[] params = {ien, Hash.encrypt(esCode)};
      String x = sCall("TIU SIGN RECORD", params);
      ActionResult actionResult = new ActionResult();
      actionResult.setSuccess(StringUtils.piece(x, 1).equals("0"));
      actionResult.setMessage(StringUtils.piece(x, 2));
      if (returnRpcResult)
        actionResult.setRpcResult(x);
      return actionResult;
      } else
        throw getCreateContextException("OR CPRS GUI CHART");
  }
  
}