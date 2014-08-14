package gov.va.med.lom.avs.service;

import gov.va.med.lom.foundation.service.response.ServiceResponse;

import gov.va.med.lom.avs.enumeration.MedClass;
import gov.va.med.lom.avs.enumeration.MedName;

import gov.va.med.lom.vistabroker.patient.data.DiscreteItemData;
import gov.va.med.lom.vistabroker.patient.data.Medication;
import gov.va.med.lom.vistabroker.security.ISecurityContext;

import java.util.LinkedHashMap;
import java.util.List;

import javax.ejb.Local;

@Local
public interface LabValuesService extends BaseService {

	public static final Integer ONE_YEAR_AGO = -12;
	public static final Integer TWO_YEARS_AGO = -24;
	public static final Integer FIVE_YEARS_AGO = -60;
	public static final Integer ALL_DATA = -1200;

	/* Identifiers for specific Discrete Item Data sets */
	public static final String FILE_LABS = "63";
	public static final String ITEM_LABS_HBA1C = "97";
	public static final String ITEM_LABS_CHOL  = "";
	public static final String ITEM_LABS_HDL   = "244";
	public static final String ITEM_LABS_LDL   = "901";
	public static final String ITEM_LABS_HGB	= "1462";
	public static final String ITEM_LABS_TRG  = "205";
  public static final String ITEM_LABS_CRT  = "5663";
	public static final String ITEM_LABS_PLT	= "1472";
	public static final String ITEM_LABS_EGFR  = "5662";
	public static final String ITEM_LABS_PSA  = "1348";

	public static final String FILE_VITALS = "120.5";
	public static final String ITEM_VITALS_BMI = "99999";
	public static final String ITEM_VITALS_BP  = "1";
	public static final String ITEM_VITALS_PULSE  = "5";
	public static final String ITEM_VITALS_WEIGHT  = "9";

	public abstract ServiceResponse<LinkedHashMap<Double, String>> getHba1cHistory(ISecurityContext securityContext, String patientDfn, double fmEndDate);
	public abstract ServiceResponse<LinkedHashMap<Double, String>> getLdlHistory(ISecurityContext securityContext, String patientDfn, double fmEndDate);
	public abstract ServiceResponse<LinkedHashMap<Double, String>> getBpHistory(ISecurityContext securityContext, String patientDfn, double fmEndDate);
	public abstract ServiceResponse<LinkedHashMap<Double, String>> getPulseHistory(ISecurityContext securityContext, String patientDfn, double fmEndDate);
	public abstract ServiceResponse<LinkedHashMap<Double, String>> getWeightHistory(ISecurityContext securityContext, String patientDfn, double fmEndDate);
	public abstract ServiceResponse<LinkedHashMap<Double, String>> getBmiHistory(ISecurityContext securityContext, String patientDfn, double fmEndDate);
	
	public abstract ServiceResponse<LinkedHashMap<MedClass, List<Medication>>> getMedsByClass(ISecurityContext securityContext, String patientDfn);
	public abstract LinkedHashMap<MedName, List<Medication>> getSortedMeds(ISecurityContext securityContext, String patientDfn);
	public abstract ServiceResponse<LinkedHashMap<String, List<DiscreteItemData>>>
	    getRecentLabValues(ISecurityContext securityContext, String patientDfn, double endDate);

	public abstract Double getCurrentBpSystolicValue(ISecurityContext securityContext, String patientDfn, double fmEndDate);
	public abstract Double getCurrentBpDiastolicValue(ISecurityContext securityContext, String patientDfn, double fmEndDate);
	public abstract DiscreteItemData getCurrentHdlValue(ISecurityContext securityContext, String patientDfn, double fmEndDate);
	public abstract DiscreteItemData getCurrentLdlValue(ISecurityContext securityContext, String patientDfn, double fmEndDate);
	public abstract DiscreteItemData getCurrentBmiValue(ISecurityContext securityContext, String patientDfn, double fmEndDate);
	public abstract DiscreteItemData getCurrentEgfrValue(ISecurityContext securityContext, String patientDfn, double fmEndDate);
	public abstract DiscreteItemData getCurrentHba1cValue(ISecurityContext securityContext, String patientDfn, double fmEndDate);
	
	public abstract List<DiscreteItemData> getRecentHba1cValues(ISecurityContext securityContext, String patientDfn, double fmEndDate);
	public abstract List<DiscreteItemData> getRecentLdlValues(ISecurityContext securityContext, String patientDfn, double fmEndDate);
	public abstract List<DiscreteItemData> getRecentHgbValues(ISecurityContext securityContext, String patientDfn, double fmEndDate);
	public abstract List<DiscreteItemData> getRecentHdlValues(ISecurityContext securityContext, String patientDfn, double fmEndDate);
	public abstract List<DiscreteItemData> getRecentTriglycerideValues(ISecurityContext securityContext, String patientDfn, double fmEndDate);
	public abstract List<DiscreteItemData> getRecentCreatinineValues(ISecurityContext securityContext, String patientDfn, double fmEndDate);
	public abstract List<DiscreteItemData> getRecentPlateletValues(ISecurityContext securityContext, String patientDfn, double fmEndDate);
	public abstract List<DiscreteItemData> getRecentEgfrValues(ISecurityContext securityContext, String patientDfn, double fmEndDate);
	public abstract List<DiscreteItemData> getRecentTotalCholesterolValues(ISecurityContext securityContext, String patientDfn, double fmEndDate);
	public abstract List<DiscreteItemData> getRecentPsaValues(ISecurityContext securityContext, String patientDfn, double fmEndDate);

	public abstract Integer getRecentMonths();
	
}
