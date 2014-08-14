package gov.va.med.lom.vistabroker.test;
/*
 * TestDdrRpcs.java
 * 
 * Author: Rob Durkin (rob.durkin@med.va.gov)
 * Version 1.0 (05/03/2012)
 *  
 */

import java.util.List;

import gov.va.med.lom.foundation.service.response.CollectionServiceResponse;

import gov.va.med.lom.vistabroker.security.ISecurityContext;
import gov.va.med.lom.vistabroker.security.SecurityContextFactory;
import gov.va.med.lom.vistabroker.service.DdrVBService;
import gov.va.med.lom.vistabroker.util.VistaBrokerServiceFactory;

import java.util.ResourceBundle;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class TestDdrRpcs {
  
  private static final Log log = LogFactory.getLog(TestDdrRpcs.class);
  
  static void printUsage() {
    System.out.println("Usage: java TestDdrRpcs AUTH_PROPS");
    System.out.println("AUTH_PROPS is the name of a properties file containing VistA connection info.");
  }
  
  public static void main(String[] args) {
    
    String division = "605";
    String duz = "9276";
    /*
    if (args.length != 1) {
      printUsage();
      System.exit(1);
    } else {
      ResourceBundle res = ResourceBundle.getBundle(args[0]);
      division = res.getString("division");
      duz = res.getString("duz");
    }
    */
    try {
      // Set security context
      ISecurityContext securityContext = SecurityContextFactory.createDuzSecurityContext(division, duz);
  
      DdrVBService ddrVbService = VistaBrokerServiceFactory.getDdrVBService();
  
      CollectionServiceResponse<String> csr = 
          
       // Clinic stop codes from file 40.7
          //ddrVbService.execDdrLister(securityContext, "40.7", null, ".01;1", "IP", 20, null, null, "#", null, null, null, null, null);
        
       // Clinic locations from file 44
          ddrVbService.execDdrLister(securityContext, "44", null, ".01;1;8", "IP", null, null, null, "#", 
              "I $P($G(^(0)),U,1)']\"ZZ\"", null, null, null, null);
      
      List<String> list = (List<String>)csr.getCollection();
      for (String s : list) {
        System.out.println(s);
      }
      
    } catch(Exception e) {
      System.err.println(e.getMessage());
      log.error("Error occurred while calling RPC: ", e);
    }      
  }

}