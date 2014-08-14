package gov.va.med.lom.vistabroker.patient.dao;

import java.util.Date;

import gov.va.med.lom.vistabroker.dao.BaseDao;
import gov.va.med.lom.vistabroker.util.FMDateUtils;

public class ReportsDao extends BaseDao {
  
  // REPORT ARGS
  public static final String CHEM_HEM_REPORTS = "OR_CH:CHEM & HEMATOLOGY~CH;ORDV02;3;";
  public static final String ALLERGIES_REPORT = "OR_BADR:ALLERGIES~ADR;ORDV01;73;";
  public static final String CYTOLOGY_REPORT = "OR_CY:CYTOLOGY~CY;ORDV02A;0;";
  public static final String CYTOPATHOLOGY_REPORT = "26:CYTOPATHOLOGY~;;0";
  public static final String ELECTRON_MICROSCOPY_REPORT = "OR_EM:ELECTRON MICROSCOPY~EM;ORDV02A;0;";
  public static final String BLOOD_AVAILABILITY_REPORT = "OR_BA:BLOOD AVAILABILITY~;;45;";
  public static final String BLOOD_TRANSFUSION_REPORT = "OR_BT:BLOOD TRANSFUSION~;;36;";
  public static final String BLOOD_BANK_REPORT = "2:BLOOD BANK REPORT~;;0";
  public static final String AUTOPSY_REPORT = "OR_AU:AUTOPSY~;;0";
  public static final String OUTPATIENT_ENCOUNTERS_REPORT = "OR_OE:OUTPATIENT ENCOUNTER~;;205;";
  public static final String ADMISSIONS_REPORT = "OR_ADC:ADM./DISCHARGE~;;10;";
  public static final String EXPANDED_ADTS_REPORT = "OR_EADT:EXPANDED ADT~;;64;";
  public static final String DISCHARGES_REPORT = "OR_DC:DISCHARGES~;;8;";
  public static final String TRANSFERS_REPORT = "OR_TR:TRANSFERS~;;16;";
  public static final String FUTURE_CLINIC_VISITS_REPORT = "OR_CVF:FUTURE CLINIC VISITS~;;9;";
  public static final String PAST_CLINIC_VISITS_REPORT = "OR_CVP:PAST CLINIC VISITS~;;14;";
  public static final String TREATING_SPECIALTY_REPORT = "OR_TS:TREATING SPECIALTY~;;17;";
  public static final String DISCHARGE_DIAGNOSIS_REPORT = "OR_DD:DISCHARGE DIAGNOSIS~;;11;";
  public static final String ICD_PROCEDURES_REPORT = "OR_PRC:ICD PROCEDURES~PRC;ORDV07;50;";
  public static final String COMP_AND_PEN_REPORT = "OR_OPC:ICD SURGERIES~ICDSUR;ORDV07;12;";
  public static final String ANATOMIC_PATHOLOGY_REPORT = "OR_APR:ANATOMIC PATHOLOGY~SP;ORDV02A;0;";
  public static final String SURGICAL_PATHOLOGY_REPORT = "OR_SP:SURGICAL PATHOLOGY~SP;ORDV02A;0;";
  public static final String MICROBIOLOGY_REPORT = "OR_MIC:MICROBIOLOGY~MI;ORDV05;38;";
  public static final String PROGRESS_NOTES_REPORT = "OR_PN:PROGRESS NOTES~TIUPRG;ORDV04;15;";
  public static final String DISCHARGE_SUMMARIES_REPORT = "OR_DS:DISCHARGE SUMMARY~TIUDCS;ORDV04;57;";
  public static final String CRISIS_NOTES_REPORT = "OR_CN:CRISIS NOTES~;;5;";
  public static final String ADVANCE_DIRECTIVES_REPORT = "OR_CD:ADVANCE DIRECTIVE~;;25;";
  public static final String CLINICAL_WARNINGS_REPORT = "OR_CW:CLINICAL WARNINGS~;;4;";
  public static final String OUTPATIENT_MEDS_RDV_REPORT = "OR_RXOP:ALL OUTPATIENT~RXOP;ORDV06;28;";
  public static final String ACTIVE_IV_MEDS_RDV_REPORT = "OR_IVA:ACTIVE IV~RXAV;ORDV06;0;";
  public static final String ALL_IV_MEDS_RDV_REPORT = "OR_RXIV:ALL IV~RXIV;ORDV06;30;";
  public static final String UNIT_DOSE_MEDS_RDV_REPORT = "OR_RXUD:UNIT DOSE~RXUD;ORDV06;29;";
  public static final String OTHER_MEDS_REQUEST_REPORT = "OR_RXN:HERBAL/OTC/NON-VA MEDS~NVA;ORDV06A;0;";
  public static final String OUTPATIENT_RX_PROFILE_REPORT = "13:OUTPATIENT RX PROFILE~;;0";
  public static final String MEDS_ADMIN_HX_REPORT = "22:MED ADMIN HISTORY (BCMA)~;;0;"; 
  public static final String MEDS_ADMIN_LOG_REPORT = "23:MED ADMIN LOG (BCMA)~;;0;";
  public static final String IMMUNIZATIONS_REPORT = "OR_IM:IMMUNIZATIONS~;;207;";
  public static final String RADIOLOGY_REPORTS = "OR_R18:IMAGING~RIM;ORDV08;0;";
  public static final String VITAL_SIGNS_REPORT = "OR_VS:VITAL SIGNS~VS;ORDV04;47;";
  public static final String DEMOGRAPHICS_REPORT = "OR_DEMG:DEMOGRAPHICS~DEM;ORDV07;6;";
  
  // CONSTRUCTORS
  public ReportsDao() {
    super();
  }
  
  public ReportsDao(BaseDao baseDao) {
    super(baseDao);
  }
  
  // RPC API
  public String getReportText(String dfn, Date fromDate, Date toDate, int nrpts, String arg) throws Exception {
    setDefaultContext("OR CPRS GUI CHART");
    setDefaultRpcName("ORWRP REPORT TEXT");
    
    double fromDateFM = 0;
    double toDateFM = 0;
    if (fromDate != null) {
      fromDateFM = FMDateUtils.dateTimeToFMDateTime(fromDate);
    }
    if (toDate != null) {
      toDateFM = FMDateUtils.dateTimeToFMDateTime(toDate);
    }    
    if (nrpts != 0) {
      arg += nrpts;
    }
    String[] params = {dfn, arg, "", "", "", String.valueOf(fromDateFM), String.valueOf(toDateFM)};
    String x = sCall(params);
    return x;
  } 
  
}
