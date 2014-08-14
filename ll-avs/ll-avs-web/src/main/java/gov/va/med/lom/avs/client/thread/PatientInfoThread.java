package gov.va.med.lom.avs.client.thread;

import java.util.TreeMap;
import java.util.SortedMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Date;

import gov.va.med.lom.javaUtils.misc.StringUtils;
import gov.va.med.lom.javaUtils.misc.DateUtils;
import gov.va.med.lom.vistabroker.patient.data.Problem;
import gov.va.med.lom.vistabroker.util.FMDateUtils;

import gov.va.med.lom.avs.model.BasicDemographics;
import gov.va.med.lom.avs.model.PatientInformation;
import gov.va.med.lom.avs.model.HealthFactor;

public class PatientInfoThread extends SheetDataThread {

  private BasicDemographics demographics;
  private static Hashtable<String, String> SMOKING_SNOMED_HT;
  private Hashtable<String, String> smokingHealthFactorsHT;
  private Hashtable<String, String> languageHealthFactorsHT;
  
  static {
    SMOKING_SNOMED_HT = new Hashtable<String, String>();
    SMOKING_SNOMED_HT.put("89765005","Tobacco dependence syndrome");
    SMOKING_SNOMED_HT.put("110483000","Tobacco user");
    SMOKING_SNOMED_HT.put("77176002","Smoker");
    SMOKING_SNOMED_HT.put("56294008","Nicotine dependence");
    SMOKING_SNOMED_HT.put("191889006","Tobacco dependence in remission");
    SMOKING_SNOMED_HT.put("449868002","Smokes tobacco daily");
    SMOKING_SNOMED_HT.put("449869005","Uses moist tobacco daily");
    SMOKING_SNOMED_HT.put("134406006","Smoking reduced");
    SMOKING_SNOMED_HT.put("160606002","Very heavy cigarette smoker (40+ cigs/day)");
    SMOKING_SNOMED_HT.put("160614008","Tobacco consumption unknown");
    SMOKING_SNOMED_HT.put("169940006","Maternal tobacco abuse");
    SMOKING_SNOMED_HT.put("191887008","Tobacco dependence, continuous");
    SMOKING_SNOMED_HT.put("191888003","Tobacco dependence, episodic");
    SMOKING_SNOMED_HT.put("228494002","Snuff user");
    SMOKING_SNOMED_HT.put("228499007","Finding relating to moist tobacco use");
    SMOKING_SNOMED_HT.put("228504007","User of moist powdered tobacco");
    SMOKING_SNOMED_HT.put("228509002","Finding relating to tobacco chewing");
    SMOKING_SNOMED_HT.put("394873005","Not interested in stopping smoking");
    SMOKING_SNOMED_HT.put("308438006","Smoking restarted");
    SMOKING_SNOMED_HT.put("266929003","Smoking started");
    SMOKING_SNOMED_HT.put("230064005","Very heavy cigarette smoker");
    SMOKING_SNOMED_HT.put("266920004","Trivial cigarette smoker (less than one cigarette/day)");
    SMOKING_SNOMED_HT.put("266927001","Tobacco smoking consumption unknown");
  }
  
  public PatientInfoThread(BasicDemographics demographics, 
      Hashtable<String, String> smokingHealthFactorsHT, 
      Hashtable<String, String> languageHealthFactorsHT) {
    this.demographics = demographics;
    this.smokingHealthFactorsHT = smokingHealthFactorsHT;
    this.languageHealthFactorsHT = languageHealthFactorsHT;
  }
  
  public void run() {
    
    PatientInformation patientInfo = new PatientInformation();
    
    try {
      patientInfo.setName(demographics.getName());
      try {
        Date date = FMDateUtils.fmDateTimeToDate(demographics.getDob());
        int age = DateUtils.calcAge(date);
        patientInfo.setAge(age);
        String dob = DateUtils.formatDate(date, "MMM dd, yyyy");
        patientInfo.setDob(dob);
      } catch(Exception e) {}
      
      SortedMap<Double, HealthFactor> smokingHF = new TreeMap<Double, HealthFactor>();
      
      List<Problem> problems = 
          (List<Problem>)super.getSheetService().getPatientProblems(super.securityContext, super.getEncounterInfo()).getCollection();
      for (Problem problem : problems) {
        String type = null;
        String code = null;
        for (int i = 0; i < 2; i++) {
          switch(i) {
            case 0 : type = "(ICD"; break;
            case 1 : type = "(SCT";
          }
          int index = problem.getDescription().indexOf(type);
          if (index > 0) {
            int index1 = problem.getDescription().indexOf(" ", index) + 1;
            int index2 = problem.getDescription().indexOf(")", index1);
            if ((index1 > 0) && (index2 > 0)) {
              code = problem.getDescription().substring(index1, index2);
              if (SMOKING_SNOMED_HT.containsKey(code)) {
                HealthFactor hf = new HealthFactor();
                if (type.indexOf("ICD") >= 0) {
                  code = "77176002";                  
                }
                hf.setHealthFactorName(SMOKING_SNOMED_HT.get(code));
                hf.setCode(code);
                Double fmdate = FMDateUtils.dateTimeToFMDateTime(problem.getLastUpdated());
                Date date = FMDateUtils.fmDateTimeToDate(fmdate);
                String dateStr = DateUtils.formatDate(date, "MMM dd, yyyy");
                hf.setVisitDate(dateStr);
                smokingHF.put(fmdate, hf);
              }
            }
          }
        }
      }
      
      List<HealthFactor> healthFactors = (List<HealthFactor>)super.getSheetService()
          .getPatientHealthFactors(super.securityContext, super.getEncounterInfo()).getCollection();
        
      SortedMap<Double, String> languageHF = new TreeMap<Double, String>();
      for (HealthFactor hf : healthFactors) {
        if (smokingHealthFactorsHT.containsKey(hf.getHealthFactorIen())) {
          smokingHF.put(getFmDate(hf.getVisitDate()), hf);
        } else if (languageHealthFactorsHT.containsKey(hf.getHealthFactorIen())) {
          String language = languageHealthFactorsHT.get(hf.getHealthFactorIen());
          if (language.equalsIgnoreCase("other")) {
            language = hf.getComment();
          }
          languageHF.put(getFmDate(hf.getVisitDate()), language);
        }
      }
      
      if (smokingHF.size() > 0) {
        HealthFactor hf = smokingHF.get(smokingHF.lastKey());
        patientInfo.setSmokingStatus(StringUtils.mixedCase(hf.getHealthFactorName()));
        patientInfo.setSmokingStatusDate(hf.getVisitDate());
      }
      
      if (languageHF.size() > 0) {
        patientInfo.setPreferredLanguage(languageHF.get(languageHF.lastKey()));
      }      
      
    } finally {
      setData("patientInfo", patientInfo);
    }    
    
  }
  
  private static Double getFmDate(String dateStr) {
    Double fmDate = 0.0;
    try {
      String str = StringUtils.piece(dateStr, '@', 1);
      Date date = DateUtils.convertDateStr(str, "MMM dd, yyyy");
      fmDate = FMDateUtils.dateToFMDate(date);
    } catch(Exception e) {}
    return fmDate;
  }
  
}
