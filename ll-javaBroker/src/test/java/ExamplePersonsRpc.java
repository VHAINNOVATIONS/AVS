/*
 * ExamplePersonsRpc.java
 * 
 * Author: Rob Durkin (rob.durkin@med.va.gov)
 * Version 1.3 (07/05/2005)
 *  
 * Prints a list of providers.
 *
 * Usage: java ExamplePersonsRpc AUTH_PROPS
 * where AUTH_PROPS is the name of a properties file containing VistA sign-on info.
 * 
 * Required Libraries:
 *   javaBroker.jar 
 *   
 */
import java.util.List;
import java.util.ResourceBundle;
 
import gov.va.med.lom.javaBroker.rpc.user.*;
import gov.va.med.lom.javaBroker.rpc.user.models.*;
import gov.va.med.lom.javaBroker.rpc.lists.*;
import gov.va.med.lom.javaBroker.rpc.lists.models.*;
import gov.va.med.lom.javaBroker.rpc.BrokerException;
import gov.va.med.lom.javaBroker.rpc.RpcBroker;
import gov.va.med.lom.javaBroker.util.StringUtils;

public class ExamplePersonsRpc {
  
  /*
   * Prints a list of providers. 
   */
  public static void main(String[] args) {
    // If user didn't pass the name of the auth properties file, then print usage and exit
    String server = null;
    int port = 0;
    String access = null;
    String verify = null;
    if (args.length != 1) {
      System.out.println("Usage: java ExamplePersonsRpc AUTH_PROPS");
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
        // Create a persons rpc object
        PersonsRpc personsRpc = new PersonsRpc(rpcBroker);
        personsRpc.setReturnRpcResult(false);
       
        /*
        String name = "Cprs,Physician";
        if ((name.length() > 0) && (StringUtils.getCharCount(name.toString(), ',') > 0)) {
          String vistaName = StringUtils.piece(name, ',', 1).trim() + "," + 
                             StringUtils.piece(name, ',', 2).trim();
          String s = StringUtils.piece(vistaName, ',', 1).trim() + "," + 
                     StringUtils.piece(vistaName, ',', 2).trim().substring(0, 1);
          PersonsList personsList = personsRpc.getSubSetOfUsers(s.toUpperCase(), 1);
          Person[] persons = personsList.getPersons();
          for (int i = 0; i < persons.length; i++) {
            if (persons[i].getName().toUpperCase().startsWith(vistaName.toUpperCase()) || 
                vistaName.toUpperCase().startsWith(persons[i].getName().toUpperCase())) {
              System.out.println(String.valueOf(persons[i].getDuz()));
            }
          }
        }    
        */
        
        /*
        // get and print the list of providers
        PersonsList personsList = personsRpc.getSubSetOfAttendings("S~", 1, null, "1", "0");
        //personsRpc.getSubSetOfUsersByType("B~", 1, "PROVIDER");
        Person[] persons = personsList.getPersons(); 
        for(int i = 0; i < persons.length; i++) {
          System.out.println(persons[i].getName() + " (DUZ: " + persons[i].getDuz() + ")");
        }
        //System.out.println("RPC Result: " + personsList.getRpcResult());
        //System.out.println("XML: " + personsList.getXml());
        */

        String name = "Cprs,Physician";
        if ((name.length() > 0) && (StringUtils.getCharCount(name, ',') > 0)) {
          name = StringUtils.replaceString(name, ",,", ",");
          String vistaName = StringUtils.piece(name, ',', 1).trim() + "," + 
          StringUtils.piece(name, ',', 2).trim();
          String s = StringUtils.piece(vistaName, ',', 1).trim() + "," + 
          StringUtils.piece(vistaName, ',', 2).trim().substring(0, 1);
          PersonsList personsList = personsRpc.getSubSetOfUsers(s.toUpperCase(), 1);
          Person[] persons = personsList.getPersons();
          for (int i = 0; i < persons.length; i++) {
            if (persons[i].getName().toUpperCase().startsWith(vistaName.toUpperCase()) || 
                vistaName.toUpperCase().startsWith(persons[i].getName().toUpperCase())) {
              System.out.println(persons[i].getIen());
            }
          }
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