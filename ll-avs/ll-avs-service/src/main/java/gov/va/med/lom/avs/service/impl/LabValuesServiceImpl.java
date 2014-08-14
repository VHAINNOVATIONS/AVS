package gov.va.med.lom.avs.service.impl;

import gov.va.med.lom.foundation.service.response.CollectionServiceResponse;
import gov.va.med.lom.foundation.service.response.ServiceResponse;

import gov.va.med.lom.avs.enumeration.MedClass;
import gov.va.med.lom.avs.enumeration.MedName;
import gov.va.med.lom.avs.service.BaseService;
import gov.va.med.lom.avs.service.LabValuesService;

import gov.va.med.lom.javaUtils.misc.StringUtils;

import gov.va.med.lom.vistabroker.patient.data.DiscreteItemData;
import gov.va.med.lom.vistabroker.patient.data.Medication;
import gov.va.med.lom.vistabroker.security.ISecurityContext;
import gov.va.med.lom.vistabroker.service.PatientVBService;
import gov.va.med.lom.vistabroker.util.FMDateUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Iterator;

import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

@Stateless(name="gov.va.med.lom.avs.LabValuesService")
@Local(LabValuesService.class)
@TransactionManagement(TransactionManagementType.BEAN)
public class LabValuesServiceImpl extends BaseServiceImpl implements BaseService, LabValuesService {	

	protected static final Log log = LogFactory.getLog(LabValuesServiceImpl.class);

	@EJB
	private PatientVBService patientRpcs;

	private HashMap<String, List<DiscreteItemData>> discreteItemDataCache = new HashMap<String, List<DiscreteItemData>>();	

	public ServiceResponse<LinkedHashMap<Double, String>> getHba1cHistory(ISecurityContext securityContext, String patientDfn, double fmEndDate) {
		
		List<DiscreteItemData> data = this.getLatestNumericValues(securityContext, patientDfn, FILE_LABS, ITEM_LABS_HBA1C, fmEndDate, ONE_YEAR_AGO);
		LinkedHashMap<Double, String> values = new LinkedHashMap<Double, String>();
		for (DiscreteItemData point : data) {
			values.put(point.getFmDate(), point.getValue());
		}
		
		ServiceResponse<LinkedHashMap<Double, String>> response = new ServiceResponse<LinkedHashMap<Double, String>>();
		response.setPayload(values);
		return response;
	}

	public ServiceResponse<LinkedHashMap<Double, String>> getLdlHistory(ISecurityContext securityContext, String patientDfn, double fmEndDate) {
		
		List<DiscreteItemData> data = this.getLatestNumericValues(securityContext, patientDfn, FILE_LABS, ITEM_LABS_LDL, fmEndDate, ONE_YEAR_AGO);
		LinkedHashMap<Double, String> values = new LinkedHashMap<Double, String>();
		for (DiscreteItemData point : data) {
			values.put(point.getFmDate(), point.getValue());
		}
		
		ServiceResponse<LinkedHashMap<Double, String>> response = new ServiceResponse<LinkedHashMap<Double, String>>();
		response.setPayload(values);
		return response;
	}

	public ServiceResponse<LinkedHashMap<Double, String>> getBpHistory(ISecurityContext securityContext, String patientDfn, double fmEndDate) {

	  LinkedHashMap<Double, String> results = new LinkedHashMap<Double, String>();
	  
		LinkedHashMap<Double, String> values = getVitalSignHistory(securityContext, patientDfn, fmEndDate, ITEM_VITALS_BP);
		String sysValue, diasValue;
    Iterator<Double> keys = values.keySet().iterator();
    while (keys.hasNext()) {
      Double dt = keys.next();
      String value = values.get(dt);
      
			// extract a piece of the value if requested, otherwise the whole value
			sysValue = StringUtils.piece(value, '/', 1);
			diasValue = StringUtils.piece(value, '/', 2);
			
			if (this.isNumeric(sysValue) && this.isNumeric(diasValue)) {
			  results.put(dt, value);
			} else {
				log.debug("Value in Blood Pressure file (" + FILE_VITALS + "^" + ITEM_VITALS_BP + ") is non-numeric and will be ignored: '" + value + "'");
			}
		}
		
		ServiceResponse<LinkedHashMap<Double, String>> response = new ServiceResponse<LinkedHashMap<Double, String>>();
		response.setPayload(results);
		return response;
	}

	 public ServiceResponse<LinkedHashMap<Double, String>> getPulseHistory(ISecurityContext securityContext, String patientDfn, double fmEndDate) {
	   
	   LinkedHashMap<Double, String> results = getVitalSignHistory(securityContext, patientDfn, fmEndDate, ITEM_VITALS_PULSE);
	   ServiceResponse<LinkedHashMap<Double, String>> response = new ServiceResponse<LinkedHashMap<Double, String>>();
	    response.setPayload(results);
	    return response; 
	 }
	 
	 public ServiceResponse<LinkedHashMap<Double, String>> getWeightHistory(ISecurityContext securityContext, String patientDfn, double fmEndDate) {
	   
     LinkedHashMap<Double, String> results = getVitalSignHistory(securityContext, patientDfn, fmEndDate, ITEM_VITALS_WEIGHT);
     ServiceResponse<LinkedHashMap<Double, String>> response = new ServiceResponse<LinkedHashMap<Double, String>>();
     response.setPayload(results);
     return response; 
	 }
	 
   public ServiceResponse<LinkedHashMap<Double, String>> getBmiHistory(ISecurityContext securityContext, String patientDfn, double fmEndDate) {
     
     LinkedHashMap<Double, String> results = getVitalSignHistory(securityContext, patientDfn, fmEndDate, ITEM_VITALS_BMI);
     ServiceResponse<LinkedHashMap<Double, String>> response = new ServiceResponse<LinkedHashMap<Double, String>>();
     response.setPayload(results);
     return response; 
   }
	 
	 private LinkedHashMap<Double, String> getVitalSignHistory(ISecurityContext securityContext, String patientDfn, double fmEndDate, String typeId) {
	    List<DiscreteItemData> dataSet = this.getDiscreteItemDataset(securityContext, patientDfn, FILE_VITALS, typeId, 0.0, fmEndDate);

	    // calculate start date (X months prior to end date)
	    Date startDate = FMDateUtils.fmDateTimeToDate(fmEndDate);
	    GregorianCalendar start = new GregorianCalendar();
	    start.setTime(startDate);
	    start.add(Calendar.MONTH, ONE_YEAR_AGO);
	    double fmStartDate = FMDateUtils.dateTimeToFMDateTime(start);

	    LinkedHashMap<Double, String> values = new LinkedHashMap<Double, String>();
	    for (DiscreteItemData point : dataSet) {
	      if (point.getFmDate() < fmStartDate || point.getFmDate() > fmEndDate) {
	        continue;
	      }
	      values.put(point.getFmDate(), point.getValue());
	    }
	    
	    return values;
	 }
	 
	// IIRC, this method is used in Goal-setting wizard pane
	public ServiceResponse<LinkedHashMap<MedClass, List<Medication>>> getMedsByClass(ISecurityContext securityContext, String patientDfn) {

		ServiceResponse<LinkedHashMap<MedClass, List<Medication>>> response = new ServiceResponse<LinkedHashMap<MedClass, List<Medication>>>();

		// 1. Get list of Diabetes-related Non-Inpatient meds

		LinkedHashMap<MedName, List<Medication>> medsByName = new LinkedHashMap<MedName, List<Medication>>();

		CollectionServiceResponse<Medication> medsResponse = this.patientRpcs.getMedications(securityContext, patientDfn);
		checkVistaExceptions(medsResponse);
		List<Medication> rawList = (List<Medication>)medsResponse.getCollection();

		for (Medication med : rawList) {
			if (med.getStatus().equals("Discontinued")) {
				continue;
			}

			for (MedName medName : MedName.values()) {
				if (this.findMed(med, medName, medsByName)) {
					break;
				}
			}
		}

		// 2. Sort this list according to Medication Class

		LinkedHashMap<MedClass, List<Medication>> meds = new LinkedHashMap<MedClass, List<Medication>>();

		for (MedName medName : medsByName.keySet()) {
			List<Medication> list = medsByName.get(medName);
			for (Medication med : list) {
				if (!meds.containsKey(medName.getMedClass())) {
					meds.put(medName.getMedClass(), new ArrayList<Medication>());
				}
				meds.get(medName.getMedClass()).add(med);
			}
		}

		response.setPayload(meds);
		
		return response;
	}

	// IIRC, this method is used in TreatmentRecService
	public LinkedHashMap<MedName, List<Medication>> getSortedMeds(ISecurityContext securityContext, String patientDfn) {
		
		List<Medication> list = (List<Medication>)this.patientRpcs.getMedications(securityContext, patientDfn).getCollection();
		LinkedHashMap<MedName, List<Medication>> meds = new LinkedHashMap<MedName, List<Medication>>();

		for (Medication med : list) {

			// only consider Active meds that are Outpatient ("OP") or Non-VA ("NV")
			if (med.getStatus().equals("Active") && (med.getType().equals("OP") || med.getType().equals("NV"))) {
				for (MedName medName : MedName.values()) {
					if (this.findMed(med, medName, meds)) {
						break;
					}
				}
			}
		}
		return meds;
	}
	
	
	public Double getCurrentBpSystolicValue(ISecurityContext securityContext, String patientDfn, double fmEndDate) {
		return this.getLatestNumericAverage(securityContext, patientDfn, FILE_VITALS, ITEM_VITALS_BP, fmEndDate, 0, 1, '/', ONE_YEAR_AGO);
	}

	public Double getCurrentBpDiastolicValue(ISecurityContext securityContext, String patientDfn, double fmEndDate) {
		return this.getLatestNumericAverage(securityContext, patientDfn, FILE_VITALS, ITEM_VITALS_BP, fmEndDate, 0, 2, '/', ONE_YEAR_AGO);
	}

	public DiscreteItemData getCurrentHdlValue(ISecurityContext securityContext, String patientDfn, double fmEndDate) {
		return this.getLatestNumericValue(securityContext, patientDfn, FILE_LABS, ITEM_LABS_HDL, fmEndDate, ONE_YEAR_AGO);
	}

	public DiscreteItemData getCurrentLdlValue(ISecurityContext securityContext, String patientDfn, double fmEndDate) {
		 return this.getLatestNumericValue(securityContext, patientDfn, FILE_LABS, ITEM_LABS_LDL, fmEndDate, ONE_YEAR_AGO);
	}

	public DiscreteItemData getCurrentBmiValue(ISecurityContext securityContext, String patientDfn, double fmEndDate) {
		return this.getLatestNumericValue(securityContext, patientDfn, FILE_VITALS, ITEM_VITALS_BMI, fmEndDate, ONE_YEAR_AGO);
	}
	
	public DiscreteItemData getCurrentEgfrValue(ISecurityContext securityContext, String patientDfn, double fmEndDate) {
		HashMap<String, String> eGFRTranslations = new HashMap<String, String>();
		eGFRTranslations.put(">60", "100");
		return this.getLatestNumericValue(securityContext, patientDfn, FILE_LABS, ITEM_LABS_EGFR, fmEndDate, eGFRTranslations, ONE_YEAR_AGO);
	}

	public DiscreteItemData getCurrentHba1cValue(ISecurityContext securityContext, String patientDfn, double fmEndDate) {
		return this.getLatestNumericValue(securityContext, patientDfn, FILE_LABS, ITEM_LABS_HBA1C, fmEndDate, ONE_YEAR_AGO);
	}
	
	public List<DiscreteItemData> getRecentHba1cValues(ISecurityContext securityContext, String patientDfn, double fmEndDate) {
		return this.getLatestNumericValues(securityContext, patientDfn, FILE_LABS, ITEM_LABS_HBA1C, fmEndDate, ONE_YEAR_AGO);
	}

	public List<DiscreteItemData> getRecentLdlValues(ISecurityContext securityContext, String patientDfn, double fmEndDate) {
		return this.getLatestNumericValues(securityContext, patientDfn, FILE_LABS, ITEM_LABS_LDL, fmEndDate, ONE_YEAR_AGO);
	}
	
  public List<DiscreteItemData> getRecentHdlValues(ISecurityContext securityContext, String patientDfn, double fmEndDate) {
    return this.getLatestNumericValues(securityContext, patientDfn, FILE_LABS, ITEM_LABS_HDL, fmEndDate, ONE_YEAR_AGO);
  }
  
  public List<DiscreteItemData> getRecentTriglycerideValues(ISecurityContext securityContext, String patientDfn, double fmEndDate) {
    return this.getLatestNumericValues(securityContext, patientDfn, FILE_LABS, ITEM_LABS_TRG, fmEndDate, ONE_YEAR_AGO);
  }
  
  public List<DiscreteItemData> getRecentCreatinineValues(ISecurityContext securityContext, String patientDfn, double fmEndDate) {
    return this.getLatestNumericValues(securityContext, patientDfn, FILE_LABS, ITEM_LABS_CRT, fmEndDate, ONE_YEAR_AGO);
  }
  
  public List<DiscreteItemData> getRecentPlateletValues(ISecurityContext securityContext, String patientDfn, double fmEndDate) {
    return this.getLatestNumericValues(securityContext, patientDfn, FILE_LABS, ITEM_LABS_PLT, fmEndDate, ONE_YEAR_AGO);
  }
  
  public List<DiscreteItemData> getRecentEgfrValues(ISecurityContext securityContext, String patientDfn, double fmEndDate) {
    return this.getLatestNumericValues(securityContext, patientDfn, FILE_LABS, ITEM_LABS_EGFR, fmEndDate, ONE_YEAR_AGO);
  }
  
  public List<DiscreteItemData> getRecentTotalCholesterolValues(ISecurityContext securityContext, String patientDfn, double fmEndDate) {
    return this.getLatestNumericValues(securityContext, patientDfn, FILE_LABS, ITEM_LABS_CHOL, fmEndDate, ONE_YEAR_AGO);
  }
	
	public List<DiscreteItemData> getRecentHgbValues(ISecurityContext securityContext, String patientDfn, double fmEndDate) {
	  return this.getLatestNumericValues(securityContext, patientDfn, FILE_LABS, ITEM_LABS_HGB, fmEndDate, ONE_YEAR_AGO);
	}
	
  public List<DiscreteItemData> getRecentPsaValues(ISecurityContext securityContext, String patientDfn, double fmEndDate) {
    return this.getLatestNumericValues(securityContext, patientDfn, FILE_LABS, ITEM_LABS_PSA, fmEndDate, ONE_YEAR_AGO);
  }	
	

	public ServiceResponse<LinkedHashMap<String, List<DiscreteItemData>>> getRecentLabValues(ISecurityContext securityContext, String patientDfn, double endDate) {
		
		ServiceResponse<LinkedHashMap<String, List<DiscreteItemData>>> response = new ServiceResponse<LinkedHashMap<String, List<DiscreteItemData>>>();

		LinkedHashMap<String, List<DiscreteItemData>> values = new LinkedHashMap<String, List<DiscreteItemData>>();

    // BMI
    List<DiscreteItemData> bmi = this.getLatestNumericValues(securityContext, patientDfn, FILE_VITALS, ITEM_VITALS_BMI, endDate, ONE_YEAR_AGO);
    if (bmi != null) {
      values.put("BMI", bmi);
    }      
    
    // Blood Pressure
    List<DiscreteItemData> bp = this.getLatestNumericValues(securityContext, patientDfn, FILE_VITALS, ITEM_VITALS_BP, endDate, ONE_YEAR_AGO);
    if (bp != null) {
      values.put("BP", bp);
    }    
    
    /*
    // Cholesterol
    List<DiscreteItemData> chol = this.getLatestNumericValues(securityContext, patientDfn, FILE_LABS, ITEM_LABS_CHOL, endDate, ONE_YEAR_AGO);
    if (chol != null) {
      values.put("CHOL", chol);
    }
    */    

    // Creatinine
    List<DiscreteItemData> crt = this.getLatestNumericValues(securityContext, patientDfn, FILE_LABS, ITEM_LABS_CRT, endDate, ONE_YEAR_AGO);
    if (crt != null) {
      values.put("CRT", crt);
    }    

    // EGFR (Estimated Glomerular Filtration Rate)
    HashMap<String, String> egfrTranslations = new HashMap<String, String>();
    egfrTranslations.put(">60", "100");
    List<DiscreteItemData> egfr = this.getLatestNumericValues(securityContext, patientDfn, FILE_LABS, ITEM_LABS_EGFR, endDate, egfrTranslations, 0, ' ', ONE_YEAR_AGO);
    if (egfr != null) {
      values.put("EGFR", egfr);
    }    
    
    // Hemoglobin A1c
    List<DiscreteItemData> hbA1c = this.getLatestNumericValues(securityContext, patientDfn, FILE_LABS, ITEM_LABS_HBA1C, endDate, ONE_YEAR_AGO);
    if (hbA1c != null) {
      values.put("HBA1C", hbA1c);
    }     
    
    // Hemoglobin
    List<DiscreteItemData> hgb = this.getLatestNumericValues(securityContext, patientDfn, FILE_LABS, ITEM_LABS_HGB, endDate, ONE_YEAR_AGO);
    if (hgb != null) {
      values.put("HGB", hgb);
    }    
    
    // HDL
    List<DiscreteItemData> hdl = this.getLatestNumericValues(securityContext, patientDfn, FILE_LABS, ITEM_LABS_HDL, endDate, ONE_YEAR_AGO);
    if (hdl != null) {
      values.put("HDL", hdl);
    }    
    
    // LDL
    List<DiscreteItemData> ldl = this.getLatestNumericValues(securityContext, patientDfn, FILE_LABS, ITEM_LABS_LDL, endDate, ONE_YEAR_AGO);
    if (ldl != null) {
      values.put("LDL", ldl);
    }    
    
    // Platelets
    List<DiscreteItemData> plt = this.getLatestNumericValues(securityContext, patientDfn, FILE_LABS, ITEM_LABS_PLT, endDate, ONE_YEAR_AGO);
    if (plt != null) {
      values.put("PLT",  plt);
    }    
    
    // Pulse
    List<DiscreteItemData> pl = this.getLatestNumericValues(securityContext, patientDfn, FILE_VITALS, ITEM_VITALS_PULSE, endDate, ONE_YEAR_AGO);
    if (pl != null) {
      values.put("PULSE", pl);
    }       
    
    // Triglycerides
    List<DiscreteItemData> trg = this.getLatestNumericValues(securityContext, patientDfn, FILE_LABS, ITEM_LABS_TRG, endDate, ONE_YEAR_AGO);
    if (trg != null) {
      values.put("TRG", trg);
    }    
    
    // PSA
    List<DiscreteItemData> psa = this.getLatestNumericValues(securityContext, patientDfn, FILE_LABS, ITEM_LABS_PSA, endDate, ALL_DATA);
    if (psa != null) {
      values.put("PSA", psa);
    }       
    
    // Weight
    List<DiscreteItemData> wt = this.getLatestNumericValues(securityContext, patientDfn, FILE_VITALS, ITEM_VITALS_WEIGHT, endDate, ONE_YEAR_AGO);
    if (wt != null) {
      values.put("WGT", wt);
    }      
    
		response.setPayload(values);
		
		return response;
	}

	public Integer getRecentMonths() {
		return ONE_YEAR_AGO;
	}

	private Double getLatestNumericAverage(ISecurityContext securityContext, String patientDfn, String fileNum, String itemNum, double fmEndDate, int numberOfPointsToInclude, int index, 
			char delimiter, int monthsAgo
	) {
		return this.getLatestNumericAverage(securityContext, patientDfn, fileNum, itemNum, fmEndDate, numberOfPointsToInclude, null, index, delimiter, monthsAgo);
	}

	private Double getLatestNumericAverage(ISecurityContext securityContext, String patientDfn, String fileNum, String itemNum, double fmEndDate, int numberOfPointsToInclude, 
			HashMap<String, String> translations, int index, char delimiter, int monthsAgo) {

		List<DiscreteItemData> dataSet = this.getLatestNumericValues(
				securityContext, patientDfn, fileNum, itemNum, fmEndDate, translations, index, delimiter, monthsAgo);

		if (dataSet.size() == 0) {
			return null;
		}
		
		if (numberOfPointsToInclude == 0 || dataSet.size() < numberOfPointsToInclude) {
			numberOfPointsToInclude = dataSet.size();
		}

		Double average = 0.0;
		String value;

		for (DiscreteItemData point : dataSet.subList(dataSet.size() - numberOfPointsToInclude, dataSet.size())) {
			if (index > 0) {
				value = StringUtils.piece(point.getValue(), delimiter, index);
			} else {
				value = point.getValue();
			}
			average += Double.parseDouble(value);
		}
		average /= Double.valueOf(numberOfPointsToInclude);

		return average;
	}
	
	private DiscreteItemData getLatestNumericValue(ISecurityContext securityContext, String patientDfn, String fileNum, String itemNum, double endDate, int monthsAgo) {
		return this.getLatestNumericValue(securityContext, patientDfn, fileNum, itemNum, endDate, 1, null, 0, ' ', monthsAgo);
	}

	private DiscreteItemData getLatestNumericValue(ISecurityContext securityContext, String patientDfn, String fileNum, String itemNum, double fmEndDate, HashMap<String, String> translations, int monthsAgo) {
		return this.getLatestNumericValue(securityContext, patientDfn, fileNum, itemNum, fmEndDate, 1, translations, 0, ' ', monthsAgo);
	}

	private DiscreteItemData getLatestNumericValue(ISecurityContext securityContext, String patientDfn, String fileNum, String itemNum, double endDate, int numberOfPointsToInclude, HashMap<String, String> translations, int index, char delimiter, int monthsAgo) {
		List<DiscreteItemData> dataSet = this.getLatestNumericValues(securityContext, patientDfn, fileNum, itemNum, endDate, translations, index, delimiter, monthsAgo);

		return (dataSet.size() == 0) ? null : dataSet.get(dataSet.size() - 1);
	}	

	private List<DiscreteItemData> getLatestNumericValues(ISecurityContext securityContext, String patientDfn, String fileNum, String itemNum, double fmEndDate, int startMonthsAgo) {
		return this.getLatestNumericValues(securityContext, patientDfn, fileNum, itemNum, fmEndDate, null, 0, ' ', startMonthsAgo);
	}

	private List<DiscreteItemData> getLatestNumericValues(
			ISecurityContext securityContext, String patientDfn, String fileNum, String itemNum, double fmEndDate, HashMap<String, String> translations, 
			int index, char delimiter, int startMonthsAgo) {

		List<DiscreteItemData> dataSet = this.getDiscreteItemDataset(securityContext, patientDfn, fileNum, itemNum, 0.0, fmEndDate);

		// calculate start date (X months prior to end date)
		Date startDate = FMDateUtils.fmDateTimeToDate(fmEndDate);
		GregorianCalendar start = new GregorianCalendar();
		start.setTime(startDate);
		start.add(Calendar.MONTH, startMonthsAgo);
		double fmStartDate = FMDateUtils.dateTimeToFMDateTime(start);
		List<DiscreteItemData> newDataSet = new ArrayList<DiscreteItemData>();
		String value;
		for (DiscreteItemData point : dataSet) {
			if (point.getFmDate() < fmStartDate || point.getFmDate() > fmEndDate) {
				continue;
			}
			
			// extract a piece of the value if requested, otherwise the whole value
			value = (index > 0 ? StringUtils.piece(point.getValue(), delimiter, index) : point.getValue());

			// translate the value if requested
			if (translations != null && translations.containsKey(value)) {
				value = translations.get(value);
				point.setValue(value);
			}
			
			//if (this.isNumeric(value)) {
				newDataSet.add(point);
			//} else {
				//log.debug("Value in file " + fileNum + "^" + itemNum + " is non-numeric and will be ignored: '" + value + "'");
			//}
		}
		dataSet = newDataSet;

		return dataSet;
	}
	
	private List<DiscreteItemData> getDiscreteItemDataset(ISecurityContext securityContext, String patientDfn, String fileNum, String itemNum, Double startDate, double endDate) {
		// get results (locally cached)
		String cacheKey = endDate + '^' + fileNum + '^' + itemNum;
		if (!this.discreteItemDataCache.containsKey(cacheKey)) {

			// get results and add them to the cache
			List<DiscreteItemData> data = (List<DiscreteItemData>)this.patientRpcs.getDiscreteItemDataSet(securityContext, patientDfn, fileNum, itemNum, 0.0).getCollection();
			this.discreteItemDataCache.put(cacheKey, data);
		}
		return this.discreteItemDataCache.get(cacheKey);
	}

	private Boolean isNumeric(String string) {
		try {
			Double.parseDouble(string);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	private Boolean findMed(Medication vistaMed, MedName medName, LinkedHashMap<MedName, List<Medication>> meds) {

		if (vistaMed.getName().equals("No Reported Usage Miscellaneous")) {
			return false;
		}

		if (vistaMed.getName().matches("(.*)\\b" + medName.getName() + "\\b(.*)")) {
			// we have a match!
			this.addMedToList(vistaMed, medName, meds);
			return true;
		} else if (medName.getAliases() != null) {
			for (String alias : medName.getAliases()) {
				if (vistaMed.getName().matches("(.*)\\b" + alias + "\\b(.*)")) {
					this.addMedToList(vistaMed, medName, meds);
					return true;
				}
			}
		}
		
		return false;
	}
	
	private void addMedToList(Medication vistaMed, MedName medName, LinkedHashMap<MedName, List<Medication>> meds) {
		// create empty list for this med
		if (!meds.containsKey(medName)) {
			meds.put(medName, new ArrayList<Medication>());
		}

		// add this med to the list
		meds.get(medName).add(vistaMed);

	}

}
