
import gov.va.med.lom.javaBroker.rpc.Params;
import gov.va.med.lom.javaBroker.rpc.RpcBroker;
import gov.va.med.lom.javaBroker.rpc.BrokerException;
import gov.va.med.lom.javaBroker.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ExampleDiscreteResultsRpcs {
  
    /*
     * Prints the results of an RPC call (OR CPRS GUI CHART context only) 
     * Hint: use the "testRpc" method to test an RPC.  See commented out examples fore specifics.
     */
	public static void main(String[] args) throws BrokerException {

	    if (args.length != 1) {
	      System.out.println("Usage: java ExampleRpcBrowser AUTH_PROPS");
	      System.out.println("      AUTH_PROPS is the name of a properties file containing VistA sign-on info.");
	      System.exit(1);
	    }
	
	    // initialize an RPC Broker instance
	    initRpcBroker(args[0]);

		String dfn = "10263160"; // ZZTEST,BEA"
		
		/*
		 * ORWGRPC TYPES - Summary of major data types available.  Example output (without subtypes):
		 * 
		 * 63^LAB TESTS^1^60^LAB(60,^B^^CH^~  ~units~flag~|
		 * 120.5^VITALS^1^120.51^GMRD(120.51,^B^^VSD^~  ~
		 * 9000011^PROBLEMS^2^80^ICD9(^B^prob^PLL^~  ~
		 * 63AP^ANATOMIC PATHOLOGY^2^*^^^ap^SP^~  ~
		 * 
		 * Major Types result format:
		 *    1          2          3           4           5           6           7           8           9
		 * typeIen ^ typeName ^ (unknown) ^ (unknown) ^ (unknown) ^ (unknown) ^ (unknown) ^ (unknown) ^ (unknown)
		 * 
		 * With includeSubtypes="1", subtype results are returned at the end of the major types list, but have different column
		 * structure:
		 * 
		 * 63AP;M^AP: Morphology
		 * 63AP;S^AP: Specimen
		 * 63MI;A^Microbiology: Antibiotic
		 * 63MI;T^Microbiology: Test
		 * 63MI;S^Microbiology: Specimen
		 * 63MI;O^Microbiology: Organism
		 * 811.2^Reminder Taxonomy
		 * 50.605^Drug Class
		 * 
		 * Subtypes result format:
		 *     1           2
		 * (unknown) ^ (unknown)
		 * 
		 * @param dfn - if "0", returns all types system-wide
		 * @param includeSubtypes - "0" for false, "1" for true
		 */
		//testRpc("ORWGRPC TYPES", dfn, "0");
		
		/*
		 * ORWGRPC ITEMS - Summary of items available for the given patient.  Item numbers do appear to be 
		 * consistent system-wide.  Example output:
		 * 
		 * 63^55^^BICARBONATE (HCO3)^^3090624.1115^9^lab - PULMONARY^BG
		 * 63^58^^PH (BLD GAS)^^3090624.1115^9^lab - PULMONARY^BG
		 * 63^97^^HEMOGLOBIN A1C (LAB)^^3100528.063634^28^lab - MANUAL CHEM^MAN
		 * 63^104^^IRON^^3090626.124^65^lab - LX20^LX
		 * 
		 * Result format:
		 *    1         2         3            4          5             6               7          8
		 * typeIen ^ itemNum ^ (unknown) ^ itemName ^ (unknown) ^ latestTimestamp ^ (unknown) ^ (unknown) 
		 * 
		 * @param dfn
		 * @param typeIen
		 */
		//testRpc("ORWGRPC ITEMS", dfn, "120.5");
		
		/*
		 * ORWGRPC ALLITEMS - All items on record for the patient.  Example output:
		 * 
		 * 63^58^PH (BLD GAS)^9^lab - PULMONARY^BG
		 * 63^97^HEMOGLOBIN A1C (LAB)^28^lab - MANUAL CHEM^MAN
		 * 63^104^IRON^65^lab - LX20^LX
		 * 120.5^1^BLOOD PRESSURE
		 * 120.5^2^TEMPERATURE
		 * 120.5^3^RESPIRATION
		 * 63AP^A;E;1216^ETIOLOGY: ASPERGILLUS SP
		 * 63AP^A;E;1231^ETIOLOGY: CANDIDA
		 * 63MI^M;A;1^ANTIBIOTIC: AMIKACN
		 * 63MI^M;A;2^ANTIBIOTIC: AMPICLN
		 * 63BB^260^ISBT AS1 RBC LR
		 * 690^22^PULMONARY FUNCTION TEST
		 * 
		 * Result format:
		 *    1         2        3            4           5           6
		 * typeIen ^ itemNum ^ itemName ^ (unknown) ^ (unknown) ^ (unknown)
		 * 
		 * @param dfn
		 */
		//testRpc("ORWGRPC ALLITEMS", dfn);		
		
		/*
		 * ORWGRPC ITEMDATA - Item data.  Returns all data from earliest one on record, through the date given.  Example output:
		 * 
		 * 63^97^3081103.075249^^5.5^^70^BLOOD^^4.2!5.9^%
		 * 63^97^3100528.063634^^7.1^H^70^BLOOD^^4.2!5.9^%
		 * 
		 * Result format:
		 *    1          2         3           4         5        6          7            8              9                      10
		 * typeIen ^ itemNum ^ timestamp ^ (unknown) ^ value ^ High/Low ^ (unknown) ^ sampleType ^ referenceRange ^ (unknown - possibly the units?)
		 *
		 * @param typeitem - hat-delimited pair of typeIen and itemNum, e.g. "63^97"
		 * @param endDate - last timestamp for which to return results
		 * @param dfn
		 */
		//testRpc("ORWGRPC ITEMDATA", "120.5^1", "3140219.13", dfn);
		testRpc("ORWGRPC ITEMDATA", "63^1348", "3140317.135253", "25443"); 
		
		/*
		 * ORWGRPC DETAILS -- Formatted text blob report for a given patient, item, and range of dates
		 * 
		 * @param dfn
		 * @param date1 - start date
		 * @param date2 - end date
		 * @param typeitem - hat-delimited "typeIen ^ itemNum" value
		 * @param includeComplete
		 */		
		//testRpc("ORWGRPC DETAILS", dfn, "3090601", "3100528.063634", "63^49", "0");
		
		/*
		 * ORWGRPC DATEITEM -- List of items available for a given patient within a given date range.  Example result:
		 * 
		 * 63^47^9^lab - PULMONARY^BG
		 * 63^48^9^lab - PULMONARY^BG
		 * 63^49^9^lab - PULMONARY^BG
		 * 
		 * Result format:
		 *    1         2          3           4
		 * typeIen ^ itemNum ^ (unknown) ^ (unknown)
		 * 
		 * NOTE: Columns 3 and above not always present, depending on the typeIen given!!!
		 * 
		 * @param startDate
		 * @param endDate
		 * @param typeIen, a.k.a. fileNum
		 * @param dfn
		 */
		//testRpc("ORWGRPC DATEITEM", "3090601", "3100528.063634", "63", dfn);

		/* 
		 * ORWLRR ALLTESTS & company -- Lists of labs.... a variety of RPCs filtered by lab type.  
		 * Seems incomplete though (where's Hemoglobin A1c?  Blood pressure?  LDL?)  Partial example of output:
		 * 
		 * 2^ 
		 * 267^ 
		 * 1015^  
		 * 5625^  LUPUS CUSTOM ANTICOAGULANT PANEL
		 * 5775^ CD5
		 * 346^ DIHYDROTESTOSTERONE (DC'D 7/03)
		 * 1106^ LEGIONELLA CULTURE
		 * 1427^ SALICYLATE (NOT IN USE AFTER 5/20/99)
		 * 
		 * @param date - "Start From"
		 * @param direction - only "1" and "-1" seem to work, although only "1" seems to return data.  "0" and "2" throw exceptions.
		 */
		//testRpc("ORWLRR ALLTESTS", "3010101", "1"); // all
		//testRpc("ORWLRR SPEC", "3020101", "1"); // specimens
		//testRpc("ORWLRR ATOMICS", "3020101", "1"); // atomic tests
		//testRpc("ORWLRR CHEMTEST", "3020101", "1"); // chem tests
		//testRpc("ORWLRR USERS", "3020101", "1"); // users ???
		
		/*
		 * ORWLRR PARAM -- "Specimen Defaults" ???  Complete output this gave:
		 * 
		 * 70^71^72^73^999^^0^1^180
		 * 
		 * Result format:
		 *   1       2       3       4          5           6           7           8           9
		 * blood ^ serum ^ urine ^ plasma ^ (unknown) ^ (unknown) ^ (unknown) ^ (unknown) ^ (unknown)
		 */
		//testRpc("ORWLRR PARAM");

		/*
		 * ORWRP3 EXPAND COLUMNS -- Data used to populate the left column menu tree of CPRS's "Labs" tab
		 * @param tab, e.g. "LABS"
		 */
		//testRpc("ORWRP3 EXPAND COLUMNS", "LABS");
		
		/*
		 * ORWRP LAB REPORT LISTS -- Subset of data from ORWRP3 EXPAND COLUMNS -- list of left column menu tree options
		 */
		//testRpc("ORWRP LAB REPORT LISTS");

		/*
		 * ORWLR REPORT LISTS -- List of "lab cumulative" sections (not sure what that means) with possible date ranges from which to select.  Partial example of results:
		 * 
		 * [REPORT LIST]
		 * ARTERIAL BLOOD GAS^Arterial Blood Gas ^Y^N^80
		 * AUTOPSY^Autopsy ^Y^N^80
		 * BLOOD BANK^Blood Bank ^Y^N^80
		 * CARDIAC PANEL^Cardiac Panel ^Y^N^80
		 * CBC PROFILE^Cbc Profile ^Y^N^80
		 * CBC PROFILE (ABS)^Cbc Profile (abs) ^Y^N^80
		 * ...
		 * $$END
		 * [DATE RANGES]
		 * dS^Date Range...
		 * d0^Today
		 * d7^One Week
		 * 
		 * Result format for Report List section:
		 *       1               2              3                 4                   5
		 * sectionNameId ^ mixedCaseName ^ askDateRange ^ askHealthSummaryType ^ rightMargin
		 */
		//testRpc("ORWLR REPORT LISTS");
		
		/*
		 * ORWMC PATIENT PROCEDURES -- Procedures this patient has had done.
		 * 
		 * @param dfn
		 */
		//testRpc("ORWMC PATIENT PROCEDURES", dfn);
				
		/*
		 * ORWLRR NEWOLD -- Newest and oldest dates, is that the oldest and newest data points across all items???
		 * 
		 * Result format:
		 *     1            2
		 * newestDate ^ oldestDate
		 * 
		 * @param dfn
		 */
		//testRpc("ORWLRR NEWOLD", dfn);
		
		/*
		 * ORWLRR INFO -- Text blob describing an item (patient-independent, generic info.)
		 * 
		 * @param itemNum
		 */
		//testRpc("ORWLRR INFO", "97");

		/*
		 * ORWLRR ATESTS -- Name of an item
		 * 
		 * @param itemNum
		 */
		//testRpc("ORWLRR ATESTS", "97");
		
		/*
		 * ORWCV LAB -- recent lab activity; used on cover page of CPRS.  Example output:
		 * 
		 * 26271962;1^POTASSIUM BLOOD SERUM WC LB #675479^3101209.072649^COMPLETE
		 * 26271961;1^SODIUM BLOOD SERUM STAT WC LB #674702^3101209.0725^COMPLETE
		 * 
		 * @param dfn
		 */
		//testRpc("ORWCV LAB", dfn);
		
		
		/*
		 * ORQQLR DETAIL -- get lab result in text blob format
		 * 
		 * @param dfn
		 * @param labIen
		 */
		//testRpc("ORQQLR DETAIL", dfn, "26271961");

		
		//testRpc("ORWDLR32 LOAD", "26271961"); // dunno, no results yet
		
		
		/*
		 * ORWGRPC FASTITEM -- UNKNOWN -- haven't gotten this RPC to output anything yet
		 *
		 * @param dfn
		 */
		//testRpc("ORWGRPC FASTITEM", dfn); // returns nothing

		/*
		 * ORWLR CUMULATIVE REPORT -- UNKNOWN -- haven't gotten this RPC to output anything yet
		 *
		 * @param dfn
		 */
		//testRpc("ORWLR CUMULATIVE REPORT", dfn);
		
		/*
		 * ORWLRR INTERIM -- UNKNOWN -- haven't gotten this RPC to output anything yet
		 *
		 * @param dfn
		 * @param date1 - start date???
		 * @param date2 - end date???
		 */
		//testRpc("ORWLRR INTERIM", dfn, "3020101", "3101129");
	
		/*
		 * ORWLRR MICRO -- UNKNOWN -- haven't gotten this RPC to output anything yet
		 * 
		 * @param dfn
		 * @param date1
		 * @param date2
		 */
		//testRpc("ORWLRR MICRO", dfn, "3070101", "3100101");

		/*
		 * ORWLRR INTERIMS -- UNKNOWN -- haven't gotten this RPC to output anything yet
		 * 
		 * @param dfn
		 * @param date1 - start date???
		 * @param date2 - end date???
		 * @param tests - array of tests, but format is unclear
		 */
		//List<String> interimsTests = new ArrayList<String>();
		//interimsTests.add("47"); // interesting... changing this to "67" or "69" makes the RPC choke, but "471" does not.  Must be on to something....
		//testRpc("ORWLRR INTERIMS", dfn, "3020101", "3101129", interimsTests);

		/*
		 * ORWGRPC DETAIL -- UNKNOWN -- Supposedly returns formatted text blob reports for a given patient, items (PLURAL!), and range of dates
		 * 
		 * @param dfn
		 * @param date1 - start date
		 * @param date2 - end date
		 * @param typeitems - list of hat-delimited "typeIen ^ itemNum" values
		 * @param includeComplete
		 */
		//List<String> detailTypeitems = new ArrayList<String>();
		//detailTypeitems.add("49");
		//testRpc("ORWGRPC DETAILS", dfn, "3020101", "3100528.063634", detailTypeitems, "0");

  	}
  
    /**
     * Call an RPC with no arguments
     */
    private static String testRpc(String call) {
    	return executeRpc(call, null);
    }

    /**
     * Call an RPC with 1 argument
     */
    private static String testRpc(String call, Object arg1) {
    	Params params = new Params();
    	addParam(params, arg1);
    	return executeRpc(call, params);
    }

    /**
     * Call an RPC with 2 arguments
     */
    private static String testRpc(String call, Object arg1, Object arg2) {
    	Params params = new Params();
    	addParam(params, arg1);
    	addParam(params, arg2);
    	return executeRpc(call, params);
    }

    /**
     * Call an RPC with 3 arguments
     */
    private static String testRpc(String call, Object arg1, Object arg2, Object arg3) {
    	Params params = new Params();
    	addParam(params, arg1);
    	addParam(params, arg2);
    	addParam(params, arg3);
    	return executeRpc(call, params);
    }

    /**
     * Call an RPC with 4 arguments
     */
    private static String testRpc(String call, Object arg1, Object arg2, Object arg3, Object arg4) {
    	Params params = new Params();
    	params.getClass().getName();
    	addParam(params, arg1);
    	addParam(params, arg2);
    	addParam(params, arg3);
    	addParam(params, arg4);
    	return executeRpc(call, params);
    }

    /**
     * Call an RPC with 5 arguments
     */
    private static String testRpc(String call, Object arg1, Object arg2, Object arg3, Object arg4, Object arg5) {
    	Params params = new Params();
    	params.getClass().getName();
    	addParam(params, arg1);
    	addParam(params, arg2);
    	addParam(params, arg3);
    	addParam(params, arg4);
    	addParam(params, arg5);
    	return executeRpc(call, params);
    }

    /**
     * Add a parameter to a Param list, in preparation for calling an RPC.  
     * Param can be a String or an ArrayList.
     */
  	private static void addParam(Params params, Object arg) {
  		String className = arg.getClass().getName();
  		
  		if (arg instanceof String) {
  			params.addLiteral((String)arg);
  		
  		} else if (arg instanceof List<?>) {
  			params.addList((List<?>)arg);

  		} else {
  			System.out.println("I do not know what to do with " + className);
  		}
  	}

    /**
     * Execute an RPC using pre-prepared Params list.  Returns the results, and optionally
     * pretty-prints them.  RPC must belong to the "OR CPRS GUI CHART" context.
     */
    private static String executeRpc(String call, Params params) {

    	rpcBroker.setContext("OR CPRS GUI CHART");

	    String result;
	    try {
		    if (params == null) {
			    result = rpcBroker.call(call);
		    } else {
		    	rpcBroker.setParams(params);
			    result = rpcBroker.call(call, params);
	    	}

	    } catch(BrokerException be) {
	        System.err.println("*ACTION: " + be.getAction());
	        System.err.println("*CODE: " + be.getCode());
	        System.err.println("*HOST: " + be.getHost());
	        System.err.println("*LOCALIZEDMESSAGE: " + be.getLocalizedMessage());
	        System.err.println("*MESSAGE: " + be.getMessage());
	        System.err.println("*MNEMONIC: " + be.getMnemonic());
	        System.err.println("*PORT: " + be.getPort());
	        System.err.println("*RECEIVED: " + be.getReceived());
	        System.err.println("*RPC: " + be.getRpc());
	        System.err.println("*SENT: " + be.getSent());
	        System.err.println("*CAUSE: " + be.getCause());
	
	        result = "";
    	}
    
	    if (printResult != null && printResult == true) {

	    	String args = "";
	    	if (params != null) {
	    		String join = "";
	    		for (int i = 0; i < params.getCount(); i++) {
	    			Object value = params.getParameter(i).getValue();
	    			if (value == null) {
	    				value = params.getParameter(i).getList();
	    			}
	    			args += join + value;
	    			join = ", ";
	    		}
		    	System.out.println("CallV('" + call + "', [" + args + "]);");
	    	} else {
		    	System.out.println("CallV('" + call + "');");
	    	}
	    	System.out.println("Result size: " + result.length() + " bytes");
	    	System.out.println();
	    	System.out.println(result);
	    }
	    
	    return result;
    }

    /**
     * Initialize an RPC Broker instance
     */
    private static void initRpcBroker(String authBundleName) throws BrokerException {
        ResourceBundle res = ResourceBundle.getBundle(authBundleName);
        
        String server = res.getString("server");
        int port = Integer.valueOf(res.getString("port")).intValue();
        String access = res.getString("accessCode");
        String verify = res.getString("verifyCode");

    	rpcBroker = new RpcBroker();
   	    rpcBroker.connect(server, port);
   	   String duz = rpcBroker.doSignon(access, verify);
   	    if (duz == null) {
   	    	System.err.println("Failed RPC Broker signon");
   	    }
    }
    
    private static void setPrintResult(Boolean print) {
    	printResult = print;
    }

    private static RpcBroker rpcBroker;
    private static Boolean printResult = true;
}