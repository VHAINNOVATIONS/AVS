package gov.va.med.lom.vistabroker.test;
/*
 * TestMiscRpcs.java
 * 
 * Author: Rob Durkin (rob.durkin@med.va.gov)
 * Version 1.0 (03/10/2007)
 *  
 */
import gov.va.med.lom.vistabroker.security.ISecurityContext;
import gov.va.med.lom.vistabroker.security.SecurityContextFactory;
import gov.va.med.lom.vistabroker.service.MiscVBService;
import gov.va.med.lom.vistabroker.util.VistaBrokerServiceFactory;

import java.util.ResourceBundle;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class TestMiscRpcs {
  
  private static final Log log = LogFactory.getLog(TestMiscRpcs.class);
  
  static void printUsage() {
    System.out.println("Usage: java TestMiscRpcs AUTH_PROPS");
    System.out.println("AUTH_PROPS is the name of a properties file containing VistA connection info.");
  }
  
  public static void main(String[] args) {
    MiscVBService miscRpcs = VistaBrokerServiceFactory.getMiscVBService();
    
    String division = null;
    String duz = null;
    @SuppressWarnings("unused")
    String securityId = null;
    if (args.length != 1) {
      printUsage();
      System.exit(1);
    } else {
      ResourceBundle res = ResourceBundle.getBundle(args[0]);
      division = res.getString("division");
      duz = res.getString("duz");
      securityId = res.getString("securityID");
    }
    try {
      // Set security context
      ISecurityContext securityContext = SecurityContextFactory.createDuzSecurityContext(division, duz);
  
      // Get and print date/time from VistA
      System.out.println("---------- DATE/TIME ---------");
      System.out.println("TODAY (FM date format): " + miscRpcs.fmToday(securityContext));
      System.out.println("NOW (FM date/time format): " + miscRpcs.fmNow(securityContext));
    } catch(Exception e) {
      System.err.println(e.getMessage());
      log.error("Error occurred while calling RPC: ", e);
    }      
  }

}