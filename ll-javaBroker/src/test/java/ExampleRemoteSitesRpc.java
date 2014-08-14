import java.util.ResourceBundle;
import java.util.ArrayList;

import gov.va.med.lom.javaBroker.rpc.user.*;
import gov.va.med.lom.javaBroker.rpc.user.models.*;

public class ExampleRemoteSitesRpc {
  
  static void printUsage() {
    System.out.println("Usage: java ExampleRemoteSitesRpc AUTH_PROPS ");
    System.out.println("where AUTH_PROPS is the name of a properties file containing VistA sign-on info.");
  }
  
  public static void main(String[] args) throws Exception {
    // If user didn't pass the patient's ssn and name of the auth properties file, then print usage and exit
    String server = null;
    int port = 0;
    String access = null;
    String verify = null;
    if (args.length != 1) {
      printUsage();
      System.exit(1);
    }  
    ResourceBundle res = ResourceBundle.getBundle(args[0]); 
    server = res.getString("server");
    port = Integer.valueOf(res.getString("port")).intValue();
    access = res.getString("accessCode");
    verify = res.getString("verifyCode");

    VistaRemoteSignonRpc vistaRemoteSignonRpc = 
      new VistaRemoteSignonRpc(server, port, access, verify);
    
    String patientDfn = "236254";
    
    ArrayList remoteSites = vistaRemoteSignonRpc.getRemoteSites(patientDfn);
    for (int i=0; i < remoteSites.size(); i++) {
      RemoteStation remoteStation = (RemoteStation)remoteSites.get(i);
      System.out.println(remoteStation.getStationNo());
      System.out.println(remoteStation.getStationName());
      System.out.println(remoteStation.getLastSeenDate());
      System.out.println(remoteStation.getLastEvent());
    }
    
  }
}