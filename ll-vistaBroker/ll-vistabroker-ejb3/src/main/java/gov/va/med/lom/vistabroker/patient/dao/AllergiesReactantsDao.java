package gov.va.med.lom.vistabroker.patient.dao;

import gov.va.med.lom.javaUtils.misc.StringUtils;
import gov.va.med.lom.vistabroker.dao.BaseDao;
import gov.va.med.lom.vistabroker.patient.data.AllergiesReactants;
import gov.va.med.lom.vistabroker.patient.data.AllergyReactant;

import java.util.ArrayList;
import java.util.List;

public class AllergiesReactantsDao extends BaseDao {
  
  // CONSTRUCTORS
  public AllergiesReactantsDao() {
    super();
  }
  
  public AllergiesReactantsDao(BaseDao baseDao) {
    super(baseDao);
  }
  
  // RPC API
  public AllergiesReactants getAllergiesReactants(String dfn) throws Exception {
    setDefaultContext("OR CPRS GUI CHART");
    setDefaultRpcName("ORQQAL LIST");
    List<String> list = lCall(dfn);
    /*
     Returns array of patient allergies.  Returned data is delimited by "^" and
     includes: allergen/reactant, reactions/symptoms (multiple symptoms/
     reactions are possible - delimited by ";"), severity, allergy id (record
     number from the Patient Allergies file (#120.8).
    */
    AllergiesReactants allergiesReactants = new AllergiesReactants();
    if (list.size() == 1) {
      String a = StringUtils.piece((String)list.get(0), 2); 
      if (a.equalsIgnoreCase("no known allergies")) {
        allergiesReactants.setNoKnownAllergies(true);
        list.clear();
      } else if (a.equalsIgnoreCase("no allergy assessment")) { 
        allergiesReactants.setNoAllergyAssessment(true);
        list.clear();
      }
    }
    List<AllergyReactant> allergiesReactantsList = new ArrayList<AllergyReactant>();
    for(String s : list) {
      AllergyReactant allergyReactant = new AllergyReactant();
      allergyReactant.setDfn(dfn);
      allergyReactant.setIen(StringUtils.piece(s, 1));
      allergyReactant.setAllergenReactant(StringUtils.piece(s, 2));
      allergyReactant.setSeverity(StringUtils.piece(s, 3));
      String[] x = StringUtils.pieceList(StringUtils.piece(s, 4), ';');
      allergyReactant.setReactionsSymptoms(x);
      allergiesReactantsList.add(allergyReactant);
    }
    allergiesReactants.setAllergiesReactants(allergiesReactantsList);
    return allergiesReactants;
  } 
  
  public String getAllergyDetail(String dfn, String ien) throws Exception {
    setDefaultContext("OR CPRS GUI CHART");
    setDefaultRpcName("ORQQAL DETAIL");
    String[] params = {dfn, ien, ""};
    List<String> list = lCall(params);
    StringBuffer sb = new StringBuffer();
    for(String s : list)
      sb.append(s + "\n");
    return sb.toString();
  }  
  
}
