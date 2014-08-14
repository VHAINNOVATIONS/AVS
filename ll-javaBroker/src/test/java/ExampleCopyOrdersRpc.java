/*
 * ExampleCopyOrdersRpc.java
 * 
 * Usage: java ExampleCopyOrdersRpc AUTH_PROPS
 *       AUTH_PROPS is the name of a properties file containing VistA sign-on info
 * 
 * Required Libraries:
 *   javaBroker.jar 
 *   
 */
import java.util.ArrayList;
import java.util.ResourceBundle;

import gov.va.med.lom.javaBroker.rpc.*;
import gov.va.med.lom.javaBroker.rpc.user.*;
import gov.va.med.lom.javaBroker.rpc.user.models.*;
import gov.va.med.lom.javaBroker.rpc.patient.*;
import gov.va.med.lom.javaBroker.rpc.patient.models.*;
import gov.va.med.lom.javaBroker.rpc.BrokerException;
import gov.va.med.lom.javaBroker.rpc.RpcBroker;

import gov.va.med.lom.javaBroker.util.FMDateUtils;
import gov.va.med.lom.javaBroker.util.StringUtils;

public class ExampleCopyOrdersRpc {
  
  public static void main(String[] args) throws Exception {
    // If user didn't pass the patient's ssn and name of the auth properties file, then print usage and exit
    String server = null;
    int port = 0;
    String access = null;
    String verify = null;
    if (args.length != 1) {
      System.out.println("Usage: java ExampleCopyOrdersRpc AUTH_PROPS");
      System.out.println("where AUTH_PROPS is the name of a properties file containing VistA sign-on info.");
      System.exit(1);
    } else {
      ResourceBundle res = ResourceBundle.getBundle(args[0]);
      server = res.getString("server");
      port = Integer.valueOf(res.getString("port")).intValue();
      access = res.getString("accessCode");
      verify = res.getString("verifyCode");
    }
    
    String userDuz = "13243";
    String patientDfn = "10108336";
    String orderNum = "29591530";
    boolean isNowDose = true;
    boolean isPrn = false;
    String schedule = "";
    
    try {
      // Call the static signon method and get an instance of the vista signon rpc
      VistaSignonRpc vistaSignonRpc = ExampleVistaSignonRpc.doVistaSignon(server, port, access, verify);
      // Get the vista signon result and check if signon was successful
      VistaSignonResult vistaSignonResult = vistaSignonRpc.getVistaSignonResult(); 
      if (vistaSignonResult.getSignonSucceeded()) {
        
        // check for permissions, throw exception if user doesn't have permission to order meds
        RpcBroker rpcBroker = vistaSignonRpc.getRpcBroker();
        rpcBroker.createContext("OR CPRS GUI CHART");
        
        String x;
        
        Object[] p1 = {String.valueOf(userDuz), "PROVIDER"};
        x = rpcBroker.call("ORWU NPHASKEY", p1);
        if (!x.equals("1")) {
          throw new Exception("You do not have permission to order medication.");
        }
        
        // try to lock user, throw exception if patient record is being modified by someone else
        rpcBroker.doClearParams();
        x = rpcBroker.call("ORWDX LOCK", patientDfn);
        if (!x.equals("1")) {
          throw new Exception("Patient record is being modified by another user.");
        }
        
        // check if user can renew order
        Object[] p2 = {orderNum, "RW",  String.valueOf(userDuz)};
        rpcBroker.doClearParams();
        x = rpcBroker.call("ORWDXA VALID", p2);
        if (!x.equals("")) {
          throw new Exception(x);
        }
        
        // check for possible errors
        Object[] p3 = {orderNum};
        rpcBroker.doClearParams();
        x = rpcBroker.call("OREVNTX1 ODPTEVID", p3);
        if (!x.equals("")) {
          throw new Exception("Not Valid: " + x);
        }
        Object[] p4 = {"T" + orderNum};
        rpcBroker.doClearParams();
        x = rpcBroker.call("ORWDPS2 QOGRP", p4);
        if (x.equals("1")) {
          throw new Exception("Not Valid: " + x);
        }        
        
        // get patient demographics info
        DemographicsRpc demographicsRpc = new DemographicsRpc(rpcBroker);
        Demographics demographics = demographicsRpc.getDemographics(patientDfn); 
        String patientStatus;
        if (demographics.getRoomBed().equals(""))
          patientStatus = "0";
        else
          patientStatus = "1";
        // get order info
        String s = patientDfn + "^" + demographics.getLocationIen() + "^" + userDuz + "^" + patientStatus  + "^" + 
                   demographics.getSex() + "^" + demographics.getAge() + "^0;C;0;0" + 
                   demographics.getServiceConnectedPercent() + "^^^";
        Object[] p5 = {"T" + orderNum, s, "0"};
        rpcBroker.doClearParams();
        String buildResults = rpcBroker.call("ORWDXM1 BLDQRSP", p5);
        if (buildResults.equals("")) {
          throw new Exception("Unable to retrieve Order Information.");
        }        
        
        // check for conflicts
        Object[] p7 = {String.valueOf(patientDfn), "PSI"};
        rpcBroker.doClearParams();
        x = rpcBroker.call("ORWDXC DISPLAY", p7);
        
        // get data for next call
        Object[] p8 = {StringUtils.piece(buildResults, '^', 2)};
        rpcBroker.doClearParams();
        x = rpcBroker.call("ORWDX LOADRSP", p8);
        String loadRspResultsStr = x;
        ArrayList loadRspResults = StringUtils.getArrayList(x);
        if (loadRspResults.size() == 0) {
          throw new Exception("Problem getting order data.");
        }          
        
        // DGNM
        Object[] p9 = {"UD RX"};
        rpcBroker.doClearParams();
        String dgnm = rpcBroker.call("ORWDX DGNM", p9);
        
        // check for more possible problems
        String rsp = null;
        if (loadRspResults.size() > 21)
          rsp = StringUtils.piece((String)loadRspResults.get(1), 'i', 2) + "^PSI^" + 
                StringUtils.piece((String)loadRspResults.get(1), '&', 6);
        else
          rsp = StringUtils.piece((String)loadRspResults.get(1), 'i', 2) + "^PSI"; 
        Object[] p10 = {String.valueOf(patientDfn), "PSI", String.valueOf(demographics.getLocationIen()), 
                       rsp, orderNum};
        rpcBroker.doClearParams();
        x = rpcBroker.call("ORWDXC ACCEPT", p10);
        if (!x.equals("")) {
          throw new Exception(StringUtils.piece(x, '^', 3));
        }
        
        // call IMOOD
        Object[] p11 = {orderNum};
        rpcBroker.doClearParams();
        x = rpcBroker.call("ORIMO IMOOD", p11);
        if (!x.equals("") &&  (x.charAt(0) == ('1'))) {
          throw new Exception("Order is IMO order.");
        }
        
        // call ISCPLX
        Object[] p12 = {orderNum};
        rpcBroker.doClearParams();
        String complex = rpcBroker.call("ORWDXR ISCPLX", p12);
        
        // load order sheets
        Object[] p13 = {String.valueOf(patientDfn)};
        rpcBroker.doClearParams();
        x = rpcBroker.call("ORWOR SHEETS", p13);
        ArrayList orderSheets = StringUtils.getArrayList(x);

        // copy med
        Mult mult = new Mult();
        mult.setMultiple("\"WP\",256,1,1,0", "");
        
        int i = 0;
        for (Object obj : loadRspResults) {
          // Get order info that does not vary based on complex order
          String rspResult = (String)obj;
          if (rspResult.indexOf("4^1^ORDERABLE") >= 0) {
            mult.setMultiple("4,1", StringUtils.piece(rspResult, 'i', 2));
          }
          if (rspResult.indexOf("256^1^SIG") >= 0) {
            int k = 1;
            while (((String)loadRspResults.get(i + k)).charAt(0) == 't') {
              String t = (String)loadRspResults.get(i + k);
              mult.setMultiple("\"WP\",256,1,1,0", mult.getValue("\"WP\",256,1,1,0") + StringUtils.piece(t, 't', 2));
              k++;
            }
          }  
          if (rspResult.indexOf("7^1^URGENCY") >= 0) {
            String t = (String)loadRspResults.get(i + 1);
            mult.setMultiple("7,1", StringUtils.piece(t, 'i', 2));
          }
          if (rspResult.indexOf("138^1^DRUG") >= 0) {
            String t = (String)loadRspResults.get(i + 1);
            mult.setMultiple("138,1", StringUtils.piece(t, 'i', 2));
          }          
          i++;
        }
        // get order info that varies with complex order
        mult.setMultiple("583,1", "");
        int j = 1;
        while (j != 0) {
          i = 0;          
          for (Object obj : loadRspResults) {
            String rspResult = (String)obj;
            if (rspResult.indexOf("136^" + j + "^INSTR") >= 0) {
              String t = (String)loadRspResults.get(i + 1);
              mult.setMultiple("136," + j, StringUtils.piece(t, 'i', 2));
              // label: StringUtils.piece(t, 'i', 2)
            }
            if (rspResult.indexOf("137^" + j + "^ROUTE") >= 0) {
              String t = (String)loadRspResults.get(i + 1);
              mult.setMultiple("137," + j, StringUtils.piece(t, 'i', 2));
            }    
            if (rspResult.indexOf("170^" + j + "^SCHEDULE") >= 0) {
              String t = (String)loadRspResults.get(i + 1);
              mult.setMultiple("170," + j, StringUtils.piece(t, 'i', 2));
              // combobox (index of): StringUtils.piece(t, 'i', 2)
            }           
            if (rspResult.indexOf("153^" + j + "^DAYS") >= 0) {
              String t = (String)loadRspResults.get(i + 1);
              mult.setMultiple("153," + j, StringUtils.piece(t, 'i', 2));
            }     
            if (rspResult.indexOf("580^" + j + "^CONJ") >= 0) {
              String t = (String)loadRspResults.get(i + 1);
              mult.setMultiple("580," + j, StringUtils.piece(t, 'i', 2));
            } 
          }
          if (isNowDose || (!mult.getValue("583,1").equals("1"))) {
            mult.setMultiple("583,1", "1"); 
          }
          if (!mult.getValue("170," + j).equals(schedule)) {
            mult.setMultiple("170," + j, schedule); 
          }
          if (loadRspResultsStr.indexOf("580^" + j + "^CONJ") >= 0) {
            j++;
          } else {
            j = 0;
          }
        }
        mult.setMultiple("15,1", "ORDIALOG(\"WP\",15,1)"); 
        if (isPrn) {
          mult.setMultiple("170,1", mult.getValue("170,1") + " PRN");
          mult.setMultiple("\"WP\",256,1,1,0", mult.getValue("\"WP\",256,1,1,0") + " PRN");
        }
        mult.setMultiple("\"ORCHECK\"", String.valueOf(orderSheets.size() - 1));
        for (Object obj : orderSheets) {
          String orderSheet = (String)obj;
          String t = "\"ORCHECK\",\"" + StringUtils.piece(orderSheet, '^', 1) + "\",\"" +
                                        StringUtils.piece(orderSheet, '^', 3) + "\",\"" + (i+1) + "\"";
          mult.setMultiple(t, StringUtils.piece(orderSheet, '^', 2) + "^" +
                              StringUtils.piece(orderSheet, '^', 3) + "^" +
                              StringUtils.piece(orderSheet, '^', 4));
        }
        
        Object[] p14 = {String.valueOf(patientDfn), String.valueOf(userDuz), 
                        String.valueOf(demographics.getLocationIen()), "PSJ OR PAT OE",
                        dgnm, "129", "", mult, complex, "0", complex, "0"};
        rpcBroker.doClearParams();
        x = rpcBroker.call("ORWDX SAVE", p14);
        if (x.equals("")) {
          throw new Exception("Problem creating order.");
        }
        ArrayList orderSaveResults = StringUtils.getArrayList(x);
        x = (String)orderSaveResults.get(0);
        orderSaveResults.remove(0);
        StringBuffer y = new StringBuffer();
        while ((orderSaveResults.size() > 0) &&  
               (((String)orderSaveResults.get(0)).charAt(1) != '~') &&
               (((String)orderSaveResults.get(0)).charAt(1) != '|')) {
          y.append(((String)orderSaveResults.get(0)).substring(2));
          y.append("\r\n");
          orderSaveResults.remove(0);
        }
        if (y.length() > 0) {
          y.delete(y.length()-2, y.length()); // take off last CRLF
        }
        StringBuffer z = new StringBuffer();
        if ((orderSaveResults.size() > 0) && (((String)orderSaveResults.get(0)).equals("|"))) {
          orderSaveResults.remove(0);
          while ((orderSaveResults.size() > 0) &&  
              (((String)orderSaveResults.get(0)).charAt(1) != '~') &&
              (((String)orderSaveResults.get(0)).charAt(1) != '|')) {
            y.append(((String)orderSaveResults.get(0)).substring(2));
            orderSaveResults.remove(0);
          }
        }
        
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setId(StringUtils.piece(x, '^', 1).substring(2, StringUtils.piece(x, '^', 1).length()));
        orderInfo.setDGroup(StringUtils.piece(x, '^', 2));
        orderInfo.setOrderDatetimeStr(FMDateUtils.fmDateTimeToEnglishDateTime(Double.valueOf(StringUtils.piece(x, '^', 3))));
        orderInfo.setStartTimeStr(StringUtils.piece(x, '^', 4));
        orderInfo.setStopTimeStr(StringUtils.piece(x, '^', 5));
        orderInfo.setStatus(StringUtils.piece(x, '^', 6));
        orderInfo.setSignature(Integer.valueOf(StringUtils.piece(x, '^', 7)));
        orderInfo.setVerNurse(StringUtils.piece(x, '^', 8));
        orderInfo.setVerClerk(StringUtils.piece(x, '^', 9));
        orderInfo.setChartRev(StringUtils.piece(x, '^', 15));
        orderInfo.setProviderDuz(StringUtils.piece(x, '^', 10));
        orderInfo.setProviderName(StringUtils.piece(x, '^', 11));
        orderInfo.setProviderDEA(StringUtils.piece(x, '^', 16));
        orderInfo.setProviderVA(StringUtils.piece(x, '^', 17));
        orderInfo.setDigSigReq(StringUtils.piece(x, '^', 18));
        orderInfo.setFlagged(StringUtils.piece(x, '^', 13).equals("1"));
        orderInfo.setText(y.toString());
        orderInfo.setXmlText(z.toString());
        
        // call returns boolean, orders is billable=1 or nonbillable=0 or discontinued = 0
        Object[] p15 = {orderNum};
        rpcBroker.doClearParams();
        x = rpcBroker.call("ORWDBA1 ORPKGTYP", p15);
        System.out.println(x);
        
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