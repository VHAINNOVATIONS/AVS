/*
 * ExampleListsRpc.java
 * 
 * Author: Rob Durkin (rob.durkin@med.va.gov)
 * Version 1.3 (07/05/2005)
 *  
 * Prints a list of wards and clinics
 *
 * Usage: java ExampleLocationsRpc AUTH_PROPS
 * where AUTH_PROPS is the name of a properties file containing VistA sign-on info.
 * 
 * Required Libraries:
 *   javaBroker.jar 
 *   
 */
import java.util.ResourceBundle;
 
import gov.va.med.lom.javaBroker.rpc.user.*;
import gov.va.med.lom.javaBroker.rpc.user.models.*;
import gov.va.med.lom.javaBroker.rpc.lists.*;
import gov.va.med.lom.javaBroker.rpc.lists.models.*;
import gov.va.med.lom.javaBroker.rpc.BrokerException;
import gov.va.med.lom.javaBroker.rpc.RpcBroker;
import gov.va.med.lom.javaBroker.util.Console;

public class ExampleCodesRpc {
  
  /*
   * Prints a list of wards and clinics. 
   */
  public static void main(String[] args) {
    // If user didn't pass the name of the auth properties file, then print usage and exit
    String server = null;
    int port = 0;
    String access = null;
    String verify = null;
    if (args.length != 1) {
      System.out.println("Usage: java ExampleListsRpc AUTH_PROPS");
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
        // Create a codes rpc object
        CodesRpc codesRpc = new CodesRpc(rpcBroker);
        while (true) {
          Console.println("(1) Lookup CPT code");
          Console.println("(2) Lookup ICD code");
          Console.println("(3) Exit");
          int option = Console.readInt("\nOption: ");
          if (option == 1) {
            String cpt = Console.readLine("Code: ");
            CptCode[] cptCodes = codesRpc.listCPTCodes(cpt);
            for(int i = 0; i < cptCodes.length; i++) 
              Console.println(cptCodes[i].getCode() + " - " + cptCodes[i].getDescription());
          } else if (option == 2) {
            String icd = Console.readLine("Code: ");
            Icd9Code[] icdCodes = codesRpc.listICD9Codes(icd, "P");
            for(int i = 0; i < icdCodes.length; i++) 
              Console.println(icdCodes[i].getCode() + " - " + icdCodes[i].getDiagnosis());            
          } else if (option == 3) 
            break;
        }
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