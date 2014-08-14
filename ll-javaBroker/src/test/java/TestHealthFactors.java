import java.util.ResourceBundle;
import java.util.ArrayList;
import java.util.List;

import gov.va.med.lom.javaBroker.rpc.user.*;
import gov.va.med.lom.javaBroker.rpc.user.models.*;
import gov.va.med.lom.javaBroker.rpc.BrokerException;
import gov.va.med.lom.javaBroker.rpc.RpcBroker;
import gov.va.med.lom.javaBroker.rpc.patient.PatientEncounterRpc;
import gov.va.med.lom.javaBroker.util.StringUtils;

import gov.va.med.lom.javaBroker.util.TextFile;

public class TestHealthFactors {
  
  public static void main(String[] args) throws Exception {
    // If user didn't pass the auth properties file, then print usage and exit
    String server = null;
    int port = 0;
    String access = null;
    String verify = null;
    if (args.length != 1) {
      System.out.println("Usage: java TestHealthFactors AUTH_PROPS");
      System.out.println("where AUTH_PROPS is the name of a properties file containing VistA sign-on info.");
      System.exit(1);
    } else {
      ResourceBundle res = ResourceBundle.getBundle(args[0]);
      server = res.getString("server");
      port = Integer.valueOf(res.getString("port")).intValue();
      access = res.getString("accessCode");
      verify = res.getString("verifyCode");
    }  
    try {
      // Call the static signon method and get an instance of the vista signon rpc
      VistaSignonRpc vistaSignonRpc = ExampleVistaSignonRpc.doVistaSignon(server, port, access, verify);
      // Get the vista signon result and check if signon was successful
      VistaSignonResult vistaSignonResult = vistaSignonRpc.getVistaSignonResult(); 
      if (vistaSignonResult.getSignonSucceeded()) {
        RpcBroker rpcBroker = vistaSignonRpc.getRpcBroker();
        rpcBroker.setCurrentContext("OR CPRS GUI CHART");
        String results = rpcBroker.call("ORWPCE GET HEALTH FACTORS TY");
        ArrayList<String> list = StringUtils.getArrayList(results);
        StringBuffer sb = new StringBuffer();
        for (String x : list) {
          sb.append(x);
          sb.append("\n");
          System.out.println(x);
        }
        TextFile textfile = new TextFile();
        textfile.setText(sb.toString());
        textfile.saveFile("healthfactors.txt");
        
      } else {
        System.out.println(vistaSignonResult.getMessage());
      }    
      // Close the connection to the broker
      vistaSignonRpc.disconnect();
    } catch(BrokerException be) {
      System.err.println(be.getMessage());
      System.err.println("Action: " + be.getAction());
      System.err.println("Code: " + be.getCode());
      System.err.println("Mnemonic: " + be.getMnemonic());
    }      
  }

}