package gov.va.med.lom.javaBroker.rpc.user;

import java.util.ArrayList;
import java.util.List;

import gov.va.med.lom.javaBroker.util.PassphraseCrypto;
import gov.va.med.lom.javaBroker.rpc.*;
import gov.va.med.lom.javaBroker.rpc.ddr.DdrFiler;
import gov.va.med.lom.javaBroker.rpc.user.models.*;
import gov.va.med.lom.javaBroker.util.Hash;
import gov.va.med.lom.javaBroker.util.StringUtils;

public class VistaRemoteSignonRpc extends AbstractRpc {
  
  // STATIC FIELDS
  static final String NON_BSE_SECURITY_PHRASE = "NON-BSE";
  static final String HASHED_SECURITY_PHRASE = "WHgafhAkItJqu&]Cbp<H";
  static final String MY_SECURITY_PHRASE = "Good players are always lucky";
  static final String ENCRYPTION_KEY = "Another day in paradise";
  static final String MENU_OPTIONS_FILE = "200.03";
  static final String OR_CPRS_GUI_CHART = "OR CPRS GUI CHART";
  
  // INSTANCE FIELDS
  private RpcBroker localBroker;
  
  // CONSTRUCTORS
  public VistaRemoteSignonRpc(String server, int port, String accessCode, 
                              String verifyCode) throws BrokerException {
    localBroker = new RpcBroker();
    localBroker.connect(server, port);
    // Do the signon setup 
    VistaSignonSetupRpc vistaSignonSetupRpc = new VistaSignonSetupRpc(localBroker);
    vistaSignonSetupRpc.doVistaSignonSetup();
    // Do signon 
    VistaSignonRpc vistaSignonRpc = new VistaSignonRpc(localBroker);
    vistaSignonRpc.setReturnRpcResult(true);
    vistaSignonRpc.doVistaSignon(accessCode, verifyCode);
    setRpcBroker(localBroker);
  }
  
  public VistaRemoteSignonRpc(RpcBroker rpcBroker) throws Exception {
    super(rpcBroker);
    this.localBroker = rpcBroker;
  }
  
  // API  
  public synchronized ArrayList getRemoteSites(String dfn) throws BrokerException {
    if (setContext("OR CPRS GUI CHART")) {
      setDfn(dfn);
      ArrayList list = lCall("ORWCIRN FACLIST", dfn);
      StringBuffer sb = new StringBuffer();
      ArrayList remoteSites = new ArrayList();
      for(int i = 0; i < list.size(); i++) {
        RemoteStation remoteStation = new RemoteStation();
        String s = (String)list.get(i);
        String stationNo = StringUtils.piece(s, 1);
        if (!stationNo.equals(localBroker.getStationNo())) {
          remoteStation.setStationNo(stationNo);
          remoteStation.setStationName(StringUtils.piece(s, 2));
          String dt = StringUtils.piece(s, 3);
          if (!dt.equals("")) {
            remoteStation.setLastSeenDate(Double.valueOf(dt));
          }
          remoteStation.setLastEvent(StringUtils.piece(s, 4));
          remoteSites.add(remoteStation);
        }
      }
      return remoteSites;
    } else
      throw getCreateContextException("OR CPRS GUI CHART");
  }
    
  
  public synchronized RemoteSignon getRemoteConnection(String remoteServer, int remotePort) throws Exception {
    RemoteSignon remoteSignon = new RemoteSignon();
    // Get user info 
    VistaUserRpc vistaUserRpc = new VistaUserRpc(localBroker);
    VistaUser vistaUser = vistaUserRpc.getVistaUser();
    String arg = "@\"^VA(200," + localBroker.getUserDuz() + ",1)\"";
    String rtn = MiscRPCs.getVariableValue(localBroker, arg);
    String userSsn = StringUtils.piece(rtn, 9);
    
    // close the connection to the local broker
    localBroker.disconnect();
    
    // Construct another rpc broker object to connect to remote site
    RpcBroker remoteBroker = new RpcBroker();
    if (!remoteBroker.connect(remoteServer, remotePort))
      throw new Exception("Could not connect to host " + remoteServer + " on port " + remotePort);
    
    // first try non-bse visit        
    StringBuffer visitStr = new StringBuffer();
    visitStr.append("-31^DVBA_^");
    visitStr.append(userSsn + "^");
    visitStr.append(vistaUser.getName() + "^");
    visitStr.append(vistaUser.getStation() + "^");
    visitStr.append(vistaUser.getStationNumber() + "^");
    visitStr.append(vistaUser.getDuz() + "^");
    visitStr.append("5611");
    
    boolean success = false;
    boolean isBse = false;
    // Setup signon at remote site with visit string
    if (!remoteBroker.createContext("XUS SIGNON")) {
      throw new Exception("Option locked or user does not have access to option.");
    }
    
    String[] params = {visitStr.toString()};
    ArrayList<String> list = null;
    while (true) {
      String results = remoteBroker.call("XUS SIGNON SETUP", params);
      list = StringUtils.getArrayList(results);
      success = (list.size() > 6) && (list.get(5).equals("1"));
      if (!success && !isBse) {
        // non-bse visit didn't work, so try bse visit
        isBse = true;
        String authToken = vistaUser.getStationNumber() + "_" + vistaUser.getDuz();
        authToken = PassphraseCrypto.encrypt(authToken, ENCRYPTION_KEY);
        authToken = StringUtils.escapeSingleQuotes(authToken);
        arg = "-35^" + Hash.encrypt(MY_SECURITY_PHRASE + "^" + authToken);
        params[0] = arg;
      } else
        break;
    }
    if (!success) {
      throw new Exception("Signon setup for remote site not successful.");
    }
    
    // Authorize
    boolean authorized = remoteBroker.createContext(MiscRPCs.CAPRI_CONTEXT);
    if (!authorized) {
      throw new Exception("Option locked or user does not have access to option.");
    }
    
    // Check if remote VistA is a test system
    boolean isTestSource = false;
    if (list.size() >= 8)
      isTestSource = list.get(7).equals("0"); 
    remoteSignon.setTestSystem(isTestSource);
      
    // Get User ID for remote site
    arg = "$O(^VA(200,\"SSN\",\"" + userSsn + "\",0))";
    String userId = MiscRPCs.getVariableValue(remoteBroker, arg);
    remoteSignon.setRemoteUserDuz(userId);
    
    // try to authorize with cprs context
    // if unsuccessful, just means that user has never visited this site before
    try {
      if (!remoteBroker.createContext(OR_CPRS_GUI_CHART)) {
        remoteBroker.createContext( MiscRPCs.CAPRI_CONTEXT);
        arg = "$O(^DIC(19,\"B\",\"" + OR_CPRS_GUI_CHART + "\",0))";
        String optionIen = MiscRPCs.getVariableValue(remoteBroker, arg);
        if (optionIen.equals(""))
          throw new Exception("No such context");         
        
        // make sure remote site has the requested option
        addMenuOption(remoteBroker, optionIen, userId);
        
        // try again to set the requested option
        try {
          if (!remoteBroker.createContext(OR_CPRS_GUI_CHART)) {
            // Some kind of xref error happened in Vista the first time, do it again.
            remoteBroker.createContext( MiscRPCs.CAPRI_CONTEXT);
            addMenuOption(remoteBroker, optionIen, userId);
            boolean ctxCreated = false;
            try {
              ctxCreated = remoteBroker.createContext(OR_CPRS_GUI_CHART);
            } catch(Exception e) {}
            if (!ctxCreated) {
              throw new Exception("Unable to set context.");
            }
          }
        } catch(Exception e) {}
      }
    } catch(Exception e) {}
    
    // set station on remote broker
    remoteBroker.setStation(vistaUser.getStation());
    remoteBroker.setStationNo(vistaUser.getStationNumber());
    
    remoteSignon.setRemoteBroker(remoteBroker);
    return remoteSignon;
  }
  
  // PRIVATE METHODS
  private static void addMenuOption(RpcBroker rpcBroker, String optionIen, String duz) throws Exception {
    // Add option to menu options file
     DdrFiler query = new DdrFiler(rpcBroker); 
     query.setOperation("ADD");
     String[] queryArgs = {MENU_OPTIONS_FILE + "^.01^+1," + duz + ",^" + optionIen};
     query.setArgs(queryArgs);
     String response = query.execute();
     System.out.println(response);
     List<String> lines = StringUtils.getStringList(response);
     if (lines.get(0).trim().equals("[Data]") && lines.size() == 2) {
       int p = lines.get(1).indexOf(",^");
       String optionNum = lines.get(1).substring(p + 2);
       System.out.println("Option # = " + optionNum);
     } else {
       if (!lines.get(0).equals("[Data]")) {
         throw new Exception("Invalid return format (" + response + ")");
       }
       if (lines.get(1).startsWith("[BEGIN_diERRORS]")) {
         throw new Exception(response.substring(8));
       }
       if (lines.size() == 1) {
         throw new Exception("No option number data");
       }
     }
   }  
  
  public RpcBroker getLocalBroker() {
    return localBroker;
  }

  public void setLocalBroker(RpcBroker localBroker) {
    this.localBroker = localBroker;
  }
  
}
