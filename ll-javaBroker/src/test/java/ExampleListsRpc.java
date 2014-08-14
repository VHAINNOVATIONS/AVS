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

public class ExampleListsRpc {
  
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
        /*
        // Create a locations rpc object
        LocationsRpc locationsRpc = new LocationsRpc(rpcBroker);
        LocationsList locationsList;
        Location[] locations;
        //locationsList = locationsRpc.listAllClinics();
        // get and print the list of wards
        //locationsList = locationsRpc.getSubSetOfClinics("NEPHRO", 1);
        locationsList = locationsRpc.listAllWards();
        locations = locationsList.getLocations();
        for(int i = 0; i < locations.length; i++) {
          System.out.println(locations[i].getName() + " (IEN: " + locations[i].getIen() + ")");
        }
        System.out.println("RPC Result: " + locationsList.getRpcResult());
        System.out.println("XML: " + locationsList.getXml());
        */
        
        PersonsRpc personsRpc = new PersonsRpc(rpcBroker);
        PersonsList personsList = personsRpc.listProvidersAll();
        Person[] persons = personsList.getPersons();
        for (int i = 0; i < persons.length; i++) {
          System.out.println(persons[i].getName() + " (IEN: " + persons[i].getDuz() + ")");
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