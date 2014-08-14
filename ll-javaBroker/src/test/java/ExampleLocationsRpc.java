/*
 * ExampleLocationsRpc.java
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
import java.util.*;

import gov.va.med.lom.javaBroker.rpc.user.*;
import gov.va.med.lom.javaBroker.rpc.user.models.*;
import gov.va.med.lom.javaBroker.rpc.lists.*;
import gov.va.med.lom.javaBroker.rpc.lists.models.*;
import gov.va.med.lom.javaBroker.rpc.BrokerException;
import gov.va.med.lom.javaBroker.rpc.RpcBroker;
import gov.va.med.lom.javaBroker.util.StringUtils;

public class ExampleLocationsRpc {
  
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
      System.out.println("Usage: java ExampleLocationsRpc AUTH_PROPS");
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
        // Create a locations rpc object
        LocationsRpc locationsRpc = new LocationsRpc(rpcBroker);
        locationsRpc.setReturnRpcResult(false);
        LocationsList locationsList;
        Location[] locations;
        
        /*
        //Hashtable<Long, Location> ht = new Hashtable<Long, Location>();
        String str = "";
        int startIndex = 0;
        int num = 0;
        while (true) {
          locationsList = locationsRpc.getSubSetOfLocations(str, 1);
          locations = locationsList.getLocations();
          for (int j = startIndex; j < locations.length; j++) {
            //ht.put(locations[j].getIen(), locations[j]);
            System.out.println(locations[j].getName());
            num++;
          }
          if (num > 0) {
            str = locations[locations.length-1].getName();
            startIndex = 1;
            num = 0;
          } else
            break;
        }
        */
        
        // get and print the list of wards
        locationsList = locationsRpc.listAllWards();
        locations = locationsList.getLocations();
        System.out.println("---------- ACTIVE INPATIENT WARDS ------------");
        for(int i = 0; i < locations.length; i++) {
          System.out.println(locations[i].getName() + " (IEN: " + locations[i].getIen() + ")");
        }
        //System.out.println("RPC Result: " + locationsList.getRpcResult());
        //System.out.println("XML: " + locationsList.getXml());
        // get and print the list of hospital locations (for use in a list box)
        locationsList = locationsRpc.getSubSetOfLocations("",1);
        locations = locationsList.getLocations();
        System.out.println("\n\n---------- HOSPITAL LOCATIONS ----------");
        for(int i = 0; i < locations.length; i++) {
          System.out.println(locations[i].getName() + " (IEN: " + locations[i].getIen() + ")");
        }      
        //System.out.println("RPC Result: " + locationsList.getRpcResult());
        //System.out.println("XML: " + locationsList.getXml());
        /*
        // get and print the list of clinics (for use in a list box)
        //LLRED/MRC/BAKHSH/MOD 5
        locationsList = locationsRpc.getSubSetOfClinics("LLRED/MRC/BAKHSH/L~",1);
        locations = locationsList.getLocations();
        System.out.println("\n\n---------- CLINICS ----------");
        for(int i = 0; i < locations.length; i++) {
          System.out.println(locations[i].getName() + " (IEN: " + locations[i].getIen() + ")");
        } 
        */
        
        /*
        String name = "LLRED/MRC/*";
        StringBuffer nameSB = new StringBuffer(name);
        Hashtable<Long, String> locList = new Hashtable<Long, String>();
        // prepare string
        if (nameSB.charAt(nameSB.length()-1) == '*') {
          nameSB.delete(nameSB.length()-1, nameSB.length());
        } else if (nameSB.charAt(nameSB.length()-1) != '/') {
          nameSB.append("/");
        }
        String prepStr = nameSB.toString();
        char endChar = 'A';
        boolean done = false;
        while (!done) {
          String searchStr = nameSB.toString() + endChar;
          StringBuffer sb = new StringBuffer();
          String[] p = StringUtils.pieceList(searchStr, '/');
          for (int i = 0; i < p.length; i++) {
            sb.append(p[i]);
            if (i < p.length-1)
              sb.append("/");
          }
          char c = p[p.length-1].charAt(p[p.length-1].length()-1);
          if (((c >= '0') && (c <= '9')) || ((c >= 'A') && (c < 'Z'))) {
            c--;
            sb.replace(sb.length()-1, sb.length(), String.valueOf(c));
            sb.append("~");
          } 
          
          locationsList = locationsRpc.getSubSetOfClinics(sb.toString().toUpperCase(), 1);
          locations = locationsList.getLocations();
          
          for(int i = 0; i < locations.length; i++) {
            System.out.print(locations[i].getName() + " (IEN: " + locations[i].getIen() + ")");
            int index = locations[i].getName().indexOf(prepStr, 0);
            if (index == -1) {
              System.out.println(" - NOT INCLUDED");
              done = true;
              break;
            }
            if (locList.containsKey(locations[i].getIen()))
              continue;
            System.out.println(" - INCLUDED");
            locList.put(locations[i].getIen(), locations[i].getName());
            if (i == locations.length-1) {
              index = nameSB.toString().length() + 1;
              nameSB.delete(0, nameSB.length());
              nameSB.append(locations[i].getName().substring(0, index));
              endChar = locations[i].getName().charAt(index);
            }
          } 

        }
        */
        
        /*
        System.out.println("RPC Result: " + locationsList.getRpcResult());
        System.out.println("XML: " + locationsList.getXml());
        */
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