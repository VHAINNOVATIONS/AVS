package gov.va.med.lom.javaBroker.rpc.user;

import java.util.ArrayList;

import gov.va.med.lom.javaBroker.rpc.*;
import gov.va.med.lom.javaBroker.rpc.user.models.*;
import gov.va.med.lom.javaBroker.util.Hash;
import gov.va.med.lom.javaBroker.util.StringUtils;

public class VistaSignonRpc extends AbstractRpc {
  
  // SIGNON RESULTS
  public static final int DEVICE_LOCKED = 1;
  public static final int CHANGE_VERIFY_CODE = 2;
  public static final int SIGNON_FAILED = 3;
  public static final int SIGNON_SUCCEEDED = 4; 
  
  // FIELDS
  private VistaSignonResult vistaSignonResult;
  
  // CONSTRUCTORS
  public VistaSignonRpc() throws BrokerException {
    super();
  }
  
  public VistaSignonRpc(RpcBroker rpcBroker) throws BrokerException {
    super(rpcBroker);
  }
  
  // PROPERTY ACCESSOR
  public VistaSignonResult getVistaSignonResult() {
    return vistaSignonResult;
  }  
  
  // RPC API  
  
  public synchronized VistaSignonResult doVistaSignon(String avCode)throws BrokerException {
	  
	RpcBroker rpcBroker = getRpcBroker();
	rpcBroker.setHashedAVCodes(Hash.encrypt(avCode));
	return doVistaSignon();
  }
  
  public synchronized VistaSignonResult doVistaSignon(String accessCode, String verifyCode) throws BrokerException {
	RpcBroker rpcBroker = getRpcBroker();
	rpcBroker.setHashedAVCodes(Hash.encrypt(accessCode + ";" + verifyCode));
	return doVistaSignon();
  }
  
  public synchronized VistaSignonResult doVistaSignon() throws BrokerException {
	  
	if(!setContext("XUS SIGNON")){
	  throw getCreateContextException("XUS SIGNON");
	}
    String[] params = {rpcBroker.getHashedAVCodes()};
    ArrayList list = lCall("XUS AV CODE", params);
    VistaSignonResult result = getVistaSignonResult(list);
    if (result.getSignonSucceeded()) {
      rpcBroker.setUserDuz(result.getDuz());
      rpcBroker.callOnRpcBrokerSignon();
      setInternalVars();
    }  
    return result;
      
  }

  private synchronized void setInternalVars(){
	  
    try{
      ArrayList list = lCall("XUS GET USER INFO");
      if (list.size() >= 8) {
        rpcBroker.setUserName((String)list.get(1));
        String division = (String)list.get(3);
        rpcBroker.setStationIen(StringUtils.piece(division, 1));
        rpcBroker.setStation(StringUtils.piece(division, 2));
        rpcBroker.setStationNo(StringUtils.piece(division, 3));
        rpcBroker.setTitle((String)list.get(4));
        rpcBroker.setServiceSection((String)list.get(5));
      }
  	}catch(Exception e){
  		e.printStackTrace();
  		/* no op */
  	}
  }
  
  public synchronized VistaSignonResult doVistaSignonViaToken(String token) throws BrokerException {
    if (!setContext("XUS SIGNON")) {
    	throw getCreateContextException("XUS SIGNON");
    }
    String[] params = new String[1];
    params[0] = token;
    ArrayList list = lCall("XUS AV CODE", params);
    RpcBroker rpcBroker = getRpcBroker();
    VistaSignonResult result = getVistaSignonResult(list);
    if (result.getSignonSucceeded()) {
      rpcBroker.setUserDuz(result.getDuz());
      rpcBroker.callOnRpcBrokerSignon();
      setInternalVars();
    }    
    return result;
      
  }  
  
  public synchronized String getSignonToken() throws BrokerException {
    if (!setContext("XUS SIGNON")){
    	throw getCreateContextException("OR CPRS GUI CHART");
    }
    return sCall("XUS GET TOKEN");
      
  }  

  private synchronized VistaSignonResult getVistaSignonResult(ArrayList list) {
    vistaSignonResult = new VistaSignonResult(); 
    if (returnRpcResult) {
      StringBuffer sb = new StringBuffer();
      for(int i = 0; i < list.size(); i++)
        sb.append((String)list.get(i) + "\n");
      vistaSignonResult.setRpcResult(sb.toString().trim());
    }    
    if (list.size() >= 6) {
      if (((String)list.get(0)).equals("0")) {
        duz = null;
        vistaSignonResult.setSignonSucceeded(false);
      } else {
        duz = (String)list.get(0);
        vistaSignonResult.setSignonSucceeded(true);
      }  
      vistaSignonResult.setDuz(duz);
      vistaSignonResult.setDeviceLocked(((String)list.get(1)).equals("1"));
      vistaSignonResult.setChangeVerifyCode(((String)list.get(2)).equals("1"));
      vistaSignonResult.setMessage((String)list.get(3));
      StringBuffer greeting = new StringBuffer();  
      for (int i = 6; i < list.size(); i++)
        greeting.append((String)list.get(i) + '\n');
      vistaSignonResult.setGreeting(greeting.toString());
      vistaSignonResult.setHost(rpcBroker.getServer());
      vistaSignonResult.setPort(rpcBroker.getListenerPort());
    }
    return vistaSignonResult;    
  }
  
}
