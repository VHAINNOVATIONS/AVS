package gov.va.med.lom.vistabroker.patient.dao;

import gov.va.med.lom.javaUtils.misc.DateUtils;
import gov.va.med.lom.javaUtils.misc.StringUtils;
import gov.va.med.lom.vistabroker.dao.BaseDao;
import gov.va.med.lom.vistabroker.misc.dao.MiscRpcsDao;
import gov.va.med.lom.vistabroker.patient.data.CumulativeLabResults;
import gov.va.med.lom.vistabroker.patient.data.LabResultTestType;
import gov.va.med.lom.vistabroker.patient.data.LabTestResult;
import gov.va.med.lom.vistabroker.util.FMDateUtils;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class LabsDao extends BaseDao {
  
  // CONSTRUCTORS
  public LabsDao() {
    super();
  }
  
  public LabsDao(BaseDao baseDao) {
    super(baseDao);
  }
  
  // RPC API  
  public List<LabTestResult> getRecentLabs(String dfn) throws Exception {
    setDefaultContext("OR CPRS GUI CHART");
    setDefaultRpcName("ORWCV LAB");
    List<String> list = lCall(dfn);
    List<LabTestResult> labTestResults = new ArrayList<LabTestResult>();
    for(String s : list) {
      if ((s.trim().length() > 0)  && (StringUtils.piece(s, 1).length() > 0)) {
        String labIen = StringUtils.piece(StringUtils.piece(s, 1), ';', 1);
        Object[] params = {String.valueOf(dfn), String.valueOf(labIen)};
        setDefaultRpcName("ORQQLR DETAIL");
        String y = sCall(params);
        LabTestResult recentLab = new LabTestResult();
        recentLab.setDfn(dfn);
        recentLab.setIen(labIen);
        recentLab.setName(StringUtils.mixedCase(StringUtils.piece(s, 2)));
        recentLab.setDate(FMDateUtils.fmDateTimeToDate(StringUtils.piece(s, 3)));
        try {
          recentLab.setDateStr(DateUtils.toEnglishDate(recentLab.getDate()));
        } catch(ParseException pe) {}
        recentLab.setStatus(StringUtils.mixedCase(StringUtils.piece(s, 4)));
        recentLab.setResult(StringUtils.mixedCase(y));
        labTestResults.add(recentLab);
      }
    }
    return labTestResults;
  } 
  
  public List<String> getLabTestData(String labTestIen) throws Exception {
    setDefaultContext("OR CPRS GUI CHART");
    setDefaultRpcName("ORWDLR32 LOAD");
    return lCall(labTestIen);
  }
  
  public CumulativeLabResults getCumulativeLabResults(String patientDfn, int daysBack, Date date1, Date date2) throws Exception {
    setDefaultContext("OR CPRS GUI CHART");
    setDefaultRpcName("ORWLR CUMULATIVE REPORT");
    double fmDate1 = 0;
    double fmDate2 = 0;
    if (date1 == null) {
      MiscRpcsDao miscRpcsDao = new MiscRpcsDao(this);
      fmDate1 = miscRpcsDao.fmNow();
    } else
      fmDate1 = FMDateUtils.dateToFMDate(date1);
    fmDate1 +=  0.2359;
    if (date2 == null)
      fmDate2 = FMDateUtils.dateToFMDate(DateUtils.subtractDaysFromDate(new Date(), daysBack));   
    else
      fmDate2 = FMDateUtils.dateToFMDate(date2);
    Object[] params = {patientDfn, daysBack, fmDate1, fmDate2};
    List<String> list = lCall(params);
    CumulativeLabResults cumulativeLabResults = new CumulativeLabResults();
    List<String> repList = new ArrayList<String>();
    for(String s : list) {
      if (!s.startsWith("[HIDDEN TEXT]") && !s.startsWith("[REPORT TEXT]"))
        repList.add(s);
      else if (s.startsWith("[REPORT TEXT]"))
        break;
    }
    List<LabResultTestType> labResultTestTypes = new ArrayList<LabResultTestType>();
    for (String s : repList) {
      LabResultTestType labResultTestType = new LabResultTestType();
      labResultTestType.setStart(StringUtils.toInt(StringUtils.piece(s, 1), 0));
      labResultTestType.setName(StringUtils.piece(s, 2));
      labResultTestTypes.add(labResultTestType);
    }
    cumulativeLabResults.setLabResultTestTypes(labResultTestTypes);
    StringBuffer results = new StringBuffer();
    for(String s : list) {
      results.append(s + "\n");
    }
    cumulativeLabResults.setText(results.toString().trim());
    return cumulativeLabResults;
  }
  
  public String getTestsByDate(String patientDfn, int daysBack, Date date1, Date date2) throws Exception {
    setDefaultContext("OR CPRS GUI CHART");
    setDefaultRpcName("ORWLRR INTERIM");
    double fmDate1 = 0;
    double fmDate2 = 0;
    if (date1 == null) {
      MiscRpcsDao miscRpcsDao = new MiscRpcsDao(this);
      fmDate1 = miscRpcsDao.fmNow();
    } else
      fmDate1 = FMDateUtils.dateToFMDate(date1);
    fmDate1 +=  0.2359;
    if (date2 == null)
      fmDate2 = FMDateUtils.dateToFMDate(DateUtils.subtractDaysFromDate(new Date(), daysBack));   
    else
      fmDate2 = FMDateUtils.dateToFMDate(date2);
    Object[] params = {patientDfn, fmDate1, fmDate2};
    StringBuffer sb = new StringBuffer();
    List<String> list = lCall(params);
    for(Object s : list) {
      sb.append(s);
      sb.append("\n");
    }
    return sb.toString();
  }
  
}