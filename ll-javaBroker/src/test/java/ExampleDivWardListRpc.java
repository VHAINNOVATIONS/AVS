import gov.va.med.lom.javaBroker.rpc.BrokerException;
import gov.va.med.lom.javaBroker.rpc.RpcBroker;
import gov.va.med.lom.javaBroker.rpc.user.*;
import gov.va.med.lom.javaBroker.rpc.user.models.*;
import gov.va.med.lom.javaBroker.rpc.lists.*;
import gov.va.med.lom.javaBroker.rpc.lists.models.*;
import gov.va.med.lom.javaBroker.util.DateUtils;
import gov.va.med.lom.javaBroker.util.StringUtils;

import java.text.ParseException;
import java.util.ResourceBundle;
import java.util.List;


public class ExampleDivWardListRpc {

	static void printUsage() {
	    System.out.println("Usage: java ExampleDemographicsRpc AUTH_PROPS");
	    System.out.println("where AUTH_PROPS is the name of a properties file containing VistA sign-on info.");
	  }
	  
	  /*
	   * Prints demographic and inpatient info for the first patient matching the specified ssn. 
	   */
	  public static void main(String[] args) {
	    
	    String server = null;
	    int port = 0;
	    String access = null;
	    String verify = null;
        RpcBroker rpcBroker = null;

	    if (args.length != 1) {
	      printUsage();
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
	    	  rpcBroker = vistaSignonRpc.getRpcBroker();
          }

          listWards(rpcBroker, "636");
          listWards(rpcBroker, "636A6");
          listWards(rpcBroker, "636A7");
          listWards(rpcBroker, "636A8");
          listWards(rpcBroker, "636A4");
          listWards(rpcBroker, "636A5");
        }catch(Exception e){
            e.printStackTrace();
        }
        
      }
 
      private static void listWards(RpcBroker rpcBroker, String wardName){
          try{ 
	          LocationsRpc locationsRpc = new LocationsRpc(rpcBroker);

              //List<DivWardLocation> wards = locationsRpc.listAllByDiv();
              List<DivWardLocation> wards = locationsRpc.listAllForDiv(wardName);
	          
	          for(DivWardLocation ward : wards){
	        	  System.out.println("ien: " + ward.getIen() + "  div: " + 
	        			  ward.getDivision() + "  name: " + ward.getName());
	          }
	          
	      locationsRpc.disconnect();
	    } catch(Exception e) {
            e.printStackTrace();
	    }      
	  }
}
