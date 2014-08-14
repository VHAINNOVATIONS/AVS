/*
 * ExampleVisitVistaRpc.java
 * 
 * Author: Rob Durkin (rob.durkin@med.va.gov)
 * Version 1.0 (05/19/2010)
 *  
 * Demonstrates visit functionality in which a local VistA user may sign on to a remote site.
 *
 * Usage: java ExampleVisitVistaRpc AUTH_PROPS REMOTE_HOST REMOTE_PORT
 * where AUTH_PROPS is the name of a properties file containing VistA sign-on info. 
 *       REMOTE_HOST is the host name of the remote VistA
 *       REMOTE_PORT is the port number of the remote VistA
 *       
 * 
 * Required Libraries:
 *   javaBroker.jar 
 *   
 */
import java.util.ResourceBundle;
import java.util.List;

import gov.va.med.lom.javaBroker.rpc.user.*;
import gov.va.med.lom.javaBroker.rpc.user.models.*;
import gov.va.med.lom.javaBroker.rpc.ddr.DdrLister;
import gov.va.med.lom.javaBroker.rpc.patient.*;
import gov.va.med.lom.javaBroker.rpc.patient.models.*;

public class ExampleVisitVistaRpc {
  
  static void printUsage() {
    System.out.println("Usage: java ExampleVisitVista AUTH_PROPS REMOTE_HOST REMOTE_PORT ");
    System.out.println("where AUTH_PROPS is the name of a properties file containing VistA sign-on info.");
    System.out.println("      REMOTE_HOST is the host name of the remote VistA");
    System.out.println("      REMOTE_PORT is the port number of the remote VistA");
  }
  
  /*
   * Prints demographic and inpatient info for the first patient matching the specified ssn. 
   */
  public static void main(String[] args) throws Exception {
    // If user didn't pass the patient's ssn and name of the auth properties file, then print usage and exit
    String server = null;
    int port = 0;
    String access = null;
    String verify = null;
    if (args.length != 3) {
      printUsage();
      System.exit(1);
    }  
    ResourceBundle res = ResourceBundle.getBundle(args[0]); 
    server = res.getString("server");
    port = Integer.valueOf(res.getString("port")).intValue();
    access = res.getString("accessCode");
    verify = res.getString("verifyCode");
    String remoteHost = args[1];
    int remotePort = Integer.valueOf(args[2]);

    VistaRemoteSignonRpc vistaRemoteSignonRpc = 
      new VistaRemoteSignonRpc(server, port, access, verify);
    
    RemoteSignon remoteSignon = vistaRemoteSignonRpc.getRemoteConnection(remoteHost, remotePort);
    
    // Print remote signon info
    System.out.println("Remote User DUZ = " + remoteSignon.getRemoteUserDuz());
    System.out.println("Test System = " + remoteSignon.isTestSystem());
    
    System.out.println("Medical Center Divisions  (File #40.8)");
    DdrLister query = new DdrLister(remoteSignon.getRemoteBroker());
    query.setFile("40.8");
    query.setFields(".01;.07;1");
    query.setFlags("IP");
    query.setXref("#");
    List<String> response = query.execute();
    for (String line : response) {
      System.out.println(line);
    }
    
    /*
    // Call patient selection RPC at remote site to retrieve patient list
    PatientSelectionRpc patientSelectionRpc = new PatientSelectionRpc(remoteSignon.getRemoteBroker()); 
    PatientList patientList = patientSelectionRpc.listAllPatients();
    PatientListItem[] patientListItems = patientList.getPatientListItems();
    for (int i = 0; i < patientListItems.length; i++) {
      System.out.println(patientListItems[i].getName());
    }
    */
  }
}