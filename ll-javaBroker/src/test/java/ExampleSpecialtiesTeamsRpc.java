/*
 * ExampleSpecialtiesTeamsRpc.java
 * 
 * Author: Rob Durkin (rob.durkin@med.va.gov)
 * Version 1.2 (06/22/2005)
 *  
 * Prints a list of specialties and teams.
 *
 * Usage: java ExampleSpecialtiesTeamsRpc AUTH_PROPS
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

public class ExampleSpecialtiesTeamsRpc {
  
  /*
   * Prints a list of specialties/teams. 
   */
  public static void main(String[] args) {
    // If user didn't pass the name of the auth properties file, then print usage and exit
    String server = null;
    int port = 0;
    String access = null;
    String verify = null;
    if (args.length != 1) {
      System.out.println("Usage: java ExampleSpecialtiesTeamsRpc AUTH_PROPS");
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
        // Create a specialties/teams rpc object
        SpecialtiesTeamsRpc specialtiesTeamsRpc = new SpecialtiesTeamsRpc(rpcBroker);
        specialtiesTeamsRpc.setReturnRpcResult(true);
        SpecialtyTeam[] specialtiesTeams;
        SpecialtiesTeamsList specialtiesTeamsList;
        // get and print the list of specialties
        specialtiesTeamsList = specialtiesTeamsRpc.listAllSpecialties();
        specialtiesTeams = specialtiesTeamsList.getSpecialtiesTeams();
        System.out.println("---------- SPECIALTIES ----------");
        for(int i = 0; i < specialtiesTeams.length; i++) {
          System.out.println(specialtiesTeams[i].getName() + " (IEN: " + specialtiesTeams[i].getIen() + ")");
        }
        System.out.println("RPC Result: " + specialtiesTeamsList.getRpcResult());
        // get and print the list of teams
        specialtiesTeamsList = specialtiesTeamsRpc.listAllTeams();
        specialtiesTeams = specialtiesTeamsList.getSpecialtiesTeams(); 
        System.out.println("\n---------- TEAMS ----------");
        for(int i = 0; i < specialtiesTeams.length; i++) {
          System.out.println(specialtiesTeams[i].getName() + " (IEN: " + specialtiesTeams[i].getIen() + ")");
        }
        //System.out.println("RPC Result: " + specialtiesTeamsList.getRpcResult());
        //System.out.println("XML: " + specialtiesTeamsList.getXml());
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