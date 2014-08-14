package gov.va.med.lom.vistabroker.patient.dao;

import gov.va.med.lom.javaUtils.misc.DateUtils;
import gov.va.med.lom.javaUtils.misc.StringUtils;
import gov.va.med.lom.vistabroker.dao.BaseDao;
import gov.va.med.lom.vistabroker.patient.data.Admission;
import gov.va.med.lom.vistabroker.util.FMDateUtils;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class AdmissionsDao extends BaseDao {
  
  // CONSTRUCTORS
  public AdmissionsDao() {
    super();
  }
  
  public AdmissionsDao(BaseDao baseDao) {
    super(baseDao);
  }
  
  // RPC API
  public List<Admission> getAdmissions(String dfn) throws Exception {
    setDefaultContext("OR CPRS GUI CHART");
    setDefaultRpcName("ORWPT ADMITLST");
    List<String> list = lCall(dfn);
    List<Admission> admissions = new ArrayList<Admission>(); 
    for(int i = 0; i < list.size(); i++) {
      String x = (String)list.get(i);
      if ((x.trim().length() > 0)  && (StringUtils.piece(x, 1).length() > 0)) {
        double admit = StringUtils.toDouble(StringUtils.piece(x, 1), 0);
        String[] params = {String.valueOf(dfn), String.valueOf(admit)};
        setDefaultRpcName("ORWPT DISCHARGE");
        String y = sCall(params);
        Admission admission = new Admission();
        admission.setRpcResult(y);
        admission.setDfn(dfn);
        admission.setAdmitDate(FMDateUtils.fmDateTimeToDate(StringUtils.piece(x, 1)));
        try {
          admission.setAdmitDateStr(DateUtils.toEnglishDate(admission.getAdmitDate()));
        } catch(ParseException pe) {}
        admission.setDischargeDate(FMDateUtils.fmDateTimeToDate(StringUtils.piece(y, 1)));
        try {
          admission.setDischargeDateStr(DateUtils.toEnglishDate(admission.getDischargeDate()));
        } catch(ParseException pe) {}        
        admission.setLocationIen(StringUtils.piece(x, 2));
        admission.setLocation(StringUtils.piece(x, 3));
        admission.setType(StringUtils.piece(x, 4));
        admissions.add(admission);
      }
    }
    return admissions;
  } 
  
}