package gov.va.med.lom.vistabroker.patient.dao;

import gov.va.med.lom.javaUtils.misc.DateUtils;
import gov.va.med.lom.javaUtils.misc.StringUtils;
import gov.va.med.lom.vistabroker.dao.BaseDao;
import gov.va.med.lom.vistabroker.patient.data.Medication;
import gov.va.med.lom.vistabroker.util.FMDateUtils;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MedicationsDao extends BaseDao {
  
  // CONSTRUCTORS
  public MedicationsDao() {
    super();
  }
  
  public MedicationsDao(BaseDao baseDao) {
    super(baseDao);
  }
  
  // RPC API
  public List<Medication> getMedications(String dfn) throws Exception {
    setDefaultContext("OR CPRS GUI CHART");
    setDefaultRpcName("ORWPS ACTIVE");
    //{ Pieces: Typ^PharmID^Drug^InfRate^StopDt^RefRem^TotDose^UnitDose^OrderID^Status^LastFill  }
    List<String> list = lCall(dfn);
    List<Medication> medications = new ArrayList<Medication>();
    int i = 0;
    String x = null;
    while(i < list.size()) {
      x = (String)list.get(i);
      if ((x.length() > 0) && (x.charAt(0) == '~')) {
        String type = StringUtils.piece(x, 1).substring(1);
        String pharmId = StringUtils.piece(x, 2);
        String name = StringUtils.mixedCase(StringUtils.piece(x, 3));
        String infRate = StringUtils.piece(x, 4);
        String totDose = StringUtils.piece(x, 7);
        String unitDose = StringUtils.piece(x, 8);
        String status = StringUtils.mixedCase(StringUtils.piece(x, 10));
        int refills = StringUtils.toInt(StringUtils.piece(x, 6), 0);
        String orderId = StringUtils.piece(x, 9);
        String expires = null;
        Date expiresDate = null;
        String lastFilled = null;
        Date lastFilledDate = null;
        try {
          expiresDate = FMDateUtils.fmDateTimeToDate(StringUtils.toInt(StringUtils.piece(x, 5), 0));
          expires = DateUtils.toEnglishDate(expiresDate);
        } catch(ParseException pe) {}
        try {
          lastFilledDate = FMDateUtils.fmDateTimeToDate(StringUtils.toInt(StringUtils.piece(x, 11), 0));
          lastFilled = DateUtils.toEnglishDate(lastFilledDate);
        } catch(ParseException pe) {}
        StringBuffer sb = new StringBuffer();
        x = (String)list.get(++i);
        boolean isSig = false;
        while((i < list.size()) && (x.length() > 0) && (x.charAt(0) != '~')) {
          isSig = isSig || x.charAt(0) == '\\';
          if (isSig) {
            sb.append(x.substring(1, x.length()));
            sb.append(" ");
          }
          if (++i < list.size())
            x = (String)list.get(i);
        }
        String sig = sb.toString();
        Medication medication = new Medication();
        medication.setDfn(dfn);
        medication.setType(type);
        medication.setName(name);
        medication.setStatus(status);
        medication.setRefills(refills);
        medication.setPharmId(pharmId);
        medication.setOrderId(orderId);
        medication.setTotalDose(totDose);
        medication.setUnitDose(unitDose);
        medication.setInfRate(infRate);
        medication.setSig(sig);
        medication.setDateExpires(expiresDate);
        medication.setDateLastFilled(lastFilledDate);
        medication.setDateExpiresStr(expires);
        medication.setDateLastFilledStr(lastFilled);
        medications.add(medication);
      } else
        i++;
    }
    return medications;
  }
  
  public List<Medication> getCoverSheetMeds(String dfn) throws Exception {
    setDefaultContext("OR CPRS GUI CHART");
    setDefaultRpcName("ORWPS COVER");
    // { Pieces: PharmID^Drug^OrderID^Status  }
    List<String> list = lCall(dfn);
    List<Medication> medications = new ArrayList<Medication>();
    for (String s : list) {
      if (s.length() > 0) {
        String pharmId = StringUtils.piece(s, 1);
        String name = StringUtils.mixedCase(StringUtils.piece(s, 2));
        String orderId = StringUtils.piece(s, 3);
        String status = StringUtils.mixedCase(StringUtils.piece(s, 4));
        Medication medication = new Medication();
        medication.setName(name);
        medication.setStatus(status);
        medication.setPharmId(pharmId);
        medication.setOrderId(orderId);
        medications.add(medication);
      } 
    }
    return medications;
  }
  
  public String getMedDetails(String dfn, String id) throws Exception {
    setDefaultContext("OR CPRS GUI CHART");
    setDefaultRpcName("ORWPS DETAIL");
      Object[] params = {dfn, id.toUpperCase()};
      List<String> list = lCall(params);
      StringBuffer sb = new StringBuffer();
      for(String s : list)
        sb.append(s + "\n");
      return sb.toString().trim();
  }
  
  public String getMedAdminHistory(String dfn, String id) throws Exception {
    setDefaultContext("OR CPRS GUI CHART");
    setDefaultRpcName("ORWPS MEDHIST");
    Object[] params = {dfn, id.toUpperCase()};
    List<String> list = lCall(params);
    StringBuffer sb = new StringBuffer();
    for(String s : list)
      sb.append(s + "\n");
    return sb.toString().trim();
  }  
  
}
