package gov.va.med.lom.vistabroker.test;

import gov.va.med.lom.javaUtils.misc.StringUtils;
import gov.va.med.lom.vistabroker.patient.dao.DiscreteResultsDao;
import gov.va.med.lom.vistabroker.patient.dao.ProblemsDao;
import gov.va.med.lom.vistabroker.patient.data.DiscreteItemData;
import gov.va.med.lom.vistabroker.patient.data.PatientInfo;
import gov.va.med.lom.vistabroker.patient.data.Problem;
import gov.va.med.lom.vistabroker.security.ISecurityContext;
import gov.va.med.lom.vistabroker.security.SecurityContextFactory;
import gov.va.med.lom.vistabroker.service.PatientVBService;
import gov.va.med.lom.vistabroker.util.FMDateUtils;
import gov.va.med.lom.vistabroker.util.VistaBrokerServiceFactory;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class TestDiscreteResultsRpc {

	  private static final Log log = LogFactory.getLog(TestDiscreteResultsRpc.class);
	  
	  static void printUsage() {
	    System.out.println("Usage: java TestLabsRpc AUTH_PROPS");
	    System.out.println("      where AUTH_PROPS is the name of a properties file containing VistA connection info.");
	  }
	  
	  /*
	   * Prints lab results for the first patient matching the specified ssn. 
	   */
	  public static void main(String[] args) {
	    PatientVBService patientRpcs = VistaBrokerServiceFactory.getPatientVBService();
	    
	    String division = null;
	    String duz = null;
	    if (args.length != 1) {
	      printUsage();
	      System.exit(1);
	    } else {
	      ResourceBundle res = ResourceBundle.getBundle(args[0]);
	      division = res.getString("division");
	      duz = res.getString("duz");
	    }
	    try {
	        // Set security context
	        ISecurityContext securityContext = SecurityContextFactory.createDuzSecurityContext(division, duz);

	        String dfn = "10190057";
	        	        
	        // set up start date ("minus X days ago")
	        GregorianCalendar startDate = new GregorianCalendar();
	        startDate.add(Calendar.DATE, -365*10);
	        double fmStartDate = FMDateUtils.dateTimeToFMDateTime(startDate);

	        // set up end date ("today")
	        GregorianCalendar endDate = new GregorianCalendar();
	        double fmEndDate = FMDateUtils.dateTimeToFMDateTime(endDate);

	        //String itemNum;

			// display the results
			//NumberFormat formatter = new DecimalFormat("0000000.000000");
						
			PatientInfo patientInfo = patientRpcs.getPatientInfo(securityContext, dfn).getPayload();
			List<Problem> problems = (List<Problem>)patientRpcs.getProblems(securityContext, dfn, ProblemsDao.ACTIVE).getCollection();

			int cardiacRiskLevel = 0;
			List<String> cardiacRiskReasons = new ArrayList<String>();
			
			// Retrieve any ICD-9 codes indicating circulatory diseases
			Boolean foundHtnAlready = false;
			for (Problem prob : problems) {
				Double code = 0.0;
				try {
					code = Double.valueOf(prob.getCode());
					if (code > 390 && code < 459.99) {
						String description = prob.getDescription().replaceAll(" +\\(ICD.*", "");
						cardiacRiskLevel = 1;
						cardiacRiskReasons.add(code + " " + description);
						if (code >= 401.0 && code <= 405.99) {
							foundHtnAlready = true;
						}
					}
				} catch (NumberFormatException e) {} // probably a "V" or "E" code and thus not applicable
			}
			
			// HDL Cholesterol
			DiscreteItemData hdl = getLatestValue(securityContext, patientRpcs, fmStartDate, fmEndDate, DiscreteResultsDao.LAB_TESTS_FILENUM, "HDL CHOLESTEROL", dfn);
			if (hdl != null) {
				try {
					Double hdlValue = Double.valueOf(hdl.getValue());
					if (patientInfo.getSex().equals("M") && hdlValue < 40) {
						cardiacRiskLevel = 1;
						cardiacRiskReasons.add("HDL<40");
					} else if (patientInfo.getSex().equals("F") && hdlValue < 50) {
						cardiacRiskLevel = 1;
						cardiacRiskReasons.add("HDL<50");
					}
				} catch (NumberFormatException e) {} // oopse, I guess it's not numeric...
			}

			// Body Mass Index
			DiscreteItemData bmi = getLatestValue(securityContext, patientRpcs, fmStartDate, fmEndDate, DiscreteResultsDao.VITALS_FILENUM, "BODY MASS INDEX", dfn);
			if (bmi != null && Double.valueOf(bmi.getValue()) >= 30) {
				cardiacRiskLevel = 1;
				cardiacRiskReasons.add("BMI>30");
			}
			
			// LDL Cholesterol
			DiscreteItemData ldl = getLatestValue(securityContext, patientRpcs, fmStartDate, fmEndDate, DiscreteResultsDao.LAB_TESTS_FILENUM, "CALC LDL CHOL", dfn);
			if (ldl != null && Double.valueOf(ldl.getValue()) > 130.0) {
				cardiacRiskLevel = 1;
				cardiacRiskReasons.add("LDL>130");
			}

			// Hypertension (method: BP analysis, if we haven't already found it in the Problems List)
			if (foundHtnAlready.equals(false)) {
				DiscreteItemData bp =  getLatestValue(securityContext, patientRpcs, fmStartDate, fmEndDate, DiscreteResultsDao.VITALS_FILENUM, "BLOOD PRESSURE", dfn);
				if (bp != null) {
					int systolicBp = Integer.valueOf(StringUtils.piece(bp.getValue(), '/', 1));
					int diastolicBp = Integer.valueOf(StringUtils.piece(bp.getValue(), '/', 2));
			        if ((systolicBp > 0 && systolicBp >= 140) && (diastolicBp > 0 && diastolicBp >= 90)) {
						cardiacRiskLevel = 1;
						cardiacRiskReasons.add("HTN");
			        } else if (systolicBp > 0 && systolicBp >= 140) {
						cardiacRiskLevel = 1;
						cardiacRiskReasons.add("Systolic HTN");
			        } else if (diastolicBp > 0 && diastolicBp >= 90) {
						cardiacRiskLevel = 1;
						cardiacRiskReasons.add("Diastolic HTN");
			        }
				}
			}

			// DISPLAY RESULTS
			System.out.println("Patient DFN: " + dfn + " (" + patientInfo.getName() + ")");
			System.out.println("\n");
			System.out.println("Overall cardiac risk level: " + (cardiacRiskLevel == 1 ? "HIGH" : "USUAL"));
			if (cardiacRiskLevel == 1) {
				System.out.println("Reasons:");
				for (String reason : cardiacRiskReasons) {
					System.out.println("    " + reason);
				}
			}
			
			/*
	        // A1C
			itemNum = patientRpcs.lookupDiscreteItemNum(securityContext, dfn, DiscreteResultsDao.LAB_TESTS_FILENUM, "HEMOGLOBIN A1C (LAB)");
	        List<DiscreteItemData> a1cData = patientRpcs.getDiscreteItemData(securityContext, dfn, DiscreteResultsDao.LAB_TESTS_FILENUM, itemNum, fmStartDate, fmEndDate);
			System.out.println("\nHemoglobin A1c: " + a1cData.size() + " data points");
			for (DiscreteItemData data : a1cData) {
				System.out.println("    " + formatter.format(data.getFmDate()) + ": " + data.getValue());
			}

			// BP
			itemNum = patientRpcs.lookupDiscreteItemNum(securityContext, dfn, DiscreteResultsDao.VITALS_FILENUM, "BLOOD PRESSURE");
			List<DiscreteItemData> bpData = patientRpcs.getDiscreteItemData(securityContext, dfn, DiscreteResultsDao.VITALS_FILENUM, itemNum, fmStartDate, fmEndDate);
			System.out.println("\nBlood Pressure: " + bpData.size() + " data points");
			for (DiscreteItemData data : bpData) {
				System.out.println("    " + formatter.format(data.getFmDate()) + ": " + data.getValue());
			}
			*/


	    
	    } catch (Exception e) {
	        System.err.println(e.getMessage());
	        log.error("Error occurred while calling RPC: ", e);
	    }
	}
	  
	private static DiscreteItemData getLatestValue(ISecurityContext securityContext, PatientVBService patientRpcs, double fmStartDate, double fmEndDate, String fileNum, String itemName, String dfn) {

		String itemNum = patientRpcs.lookupDiscreteItemNum(securityContext, dfn, fileNum, itemName).getPayload();
		List<DiscreteItemData> data = (List<DiscreteItemData>)patientRpcs.getDiscreteItemDataSet(securityContext, dfn, fileNum, itemNum, fmStartDate).getCollection();

		/* We might be able to speed this up if we could verify that the RPC returns its results in
		 * chronological order.  Then we wouldn't have to loop through each item.
		 * 
		 * For performance reasons, maybe we should add a method to VistaBroker.DiscreteResultsDao for retrieving the last value
		 * without having to parse everything up so nicely and put it into DiscreteItemData beans first.
		 */
		DiscreteItemData latestItem = null;
		double date = 0;
		for (DiscreteItemData item : data) {
			if (item.getFmDate() > date) {
				date = item.getFmDate();
				latestItem = item;
			}
		}
		return latestItem;
	}

}



