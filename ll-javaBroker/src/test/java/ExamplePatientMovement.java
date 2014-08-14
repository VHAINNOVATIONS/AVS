/*
 * ExampleAddressRpc.java
 * 
 * Author: Rob Durkin (rob.durkin@med.va.gov)
 * Version 1.3 (07/05/2005)
 *  
 * Prints the address of the patient.
 * Note: Requires the custom 'ALSI ADDRESS LOOKUP' rpc 
 *       and the 'ALS CLINICAL RPC' context.
 *
 * Usage: java ExampleAddressRpc SSN AUTH_PROPS
 * where SSN is a patient's Social Security Number.
 *       AUTH_PROPS is the name of a properties file containing VistA sign-on info.
 * 
 * Required Libraries:
 *   javaBroker.jar 
 *   
 */
import java.util.ResourceBundle;

import gov.va.med.lom.javaBroker.rpc.admin.*;
import gov.va.med.lom.javaBroker.rpc.admin.models.*;
import gov.va.med.lom.javaBroker.rpc.user.*;
import gov.va.med.lom.javaBroker.rpc.user.models.*;
import gov.va.med.lom.javaBroker.rpc.*;
import java.util.GregorianCalendar;
import java.util.Calendar;
import java.util.List;
import java.text.SimpleDateFormat;


public class ExamplePatientMovement {
  
  /*
   * Prints the address for the first patient matching the specified ssn. 
   */
  public static void main(String[] args) {
    // If user didn't pass the auth properties file, then print usage and exit
    String server = null;
    int port = 0;
    String access = null;
    String verify = null;
    if (args.length != 1) {
      System.out.println("Usage: java ExampleScheduledAdmits AUTH_PROPS");
      System.out.println("      AUTH_PROPS is the name of a properties file containing VistA sign-on info.");
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

        PatientMovements patientMovementsRpc = new PatientMovements(rpcBroker);
        GregorianCalendar startDate = new GregorianCalendar();
        GregorianCalendar stopDate = new GregorianCalendar();
        startDate.add(Calendar.DATE,-3);
        List<PatientMovement> mvs = patientMovementsRpc.fetch(startDate, stopDate);
        SimpleDateFormat f = new SimpleDateFormat("MM-dd-yyyy hh:mm");
        
        if(mvs == null){
        	System.out.println("An error occured.");
        	System.exit(1);
        }
        if(mvs.size() == 0){
        	System.out.println("#####  no movements found for " + 
        			f.format(startDate.getTime()) + "  -  " + 
        			f.format(stopDate.getTime()));
        }
        for(PatientMovement mv : mvs){
        	System.out.println("###################################################");
        	System.out.println("Date: " + f.format(mv.getDate().getTime()));
        	System.out.println("Name: " + mv.getName());
        	System.out.println("SSN: " + mv.getSsn());
        	System.out.println("IEN: " + mv.getId());
        	System.out.println("DFN: " + mv.getDfn());
        	System.out.println("###################################################");
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