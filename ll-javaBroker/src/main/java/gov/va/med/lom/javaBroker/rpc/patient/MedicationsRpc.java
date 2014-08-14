package gov.va.med.lom.javaBroker.rpc.patient;

import java.util.Vector;
import java.util.ArrayList;
import java.util.Date;
import java.text.ParseException;

import gov.va.med.lom.javaBroker.rpc.*;
import gov.va.med.lom.javaBroker.rpc.patient.models.*;
import gov.va.med.lom.javaBroker.util.FMDateUtils;
import gov.va.med.lom.javaBroker.util.StringUtils;
import gov.va.med.lom.javaBroker.util.DateUtils;

public class MedicationsRpc extends AbstractRpc {
  
  // FIELDS
  private MedicationsList medicationsList;
  
  // CONSTRUCTORS
  public MedicationsRpc() throws BrokerException {
    super();
  }
  
  public MedicationsRpc(RpcBroker rpcBroker) throws BrokerException {
    super(rpcBroker);
  }
  
  // RPC API
  public synchronized MedicationsList getMedications(String dfn) throws BrokerException {
    if (setContext("OR CPRS GUI CHART")) {
      setDfn(dfn);
      //{ Pieces: Typ^PharmID^Drug^InfRate^StopDt^RefRem^TotDose^UnitDose^OrderID^Status^LastFill  }
      medicationsList = new MedicationsList();
      ArrayList list = lCall("ORWPS ACTIVE", dfn);
      if (returnRpcResult) {
        StringBuffer sb = new StringBuffer();
        for(int i = 0; i < list.size(); i++)
          sb.append((String)list.get(i) + "\n");
        medicationsList.setRpcResult(sb.toString().trim());
      }       
      Vector medicationsVect = new Vector();
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
          if (returnRpcResult)
            medication.setRpcResult(x);
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
          medicationsVect.add(medication);
        } else
          i++;
      }
      Medication[] medications = new Medication[medicationsVect.size()];
      for (int j = 0; j < medications.length; j++)
        medications[j] = (Medication)medicationsVect.get(j);
      medicationsList.setMedications(medications);
      return medicationsList;
    } else
      throw getCreateContextException("OR CPRS GUI CHART");
  }
  
  public synchronized MedicationsList getCoverSheetMeds(String dfn) throws BrokerException {
    if (setContext("OR CPRS GUI CHART")) {
      setDfn(dfn);
      // { Pieces: PharmID^Drug^OrderID^Status  }
      ArrayList list = lCall("ORWPS COVER", dfn);
      medicationsList = new MedicationsList();
      if (returnRpcResult) {
        StringBuffer sb = new StringBuffer();
        for(int i = 0; i < list.size(); i++)
          sb.append((String)list.get(i) + "\n");
        medicationsList.setRpcResult(sb.toString().trim());
      }       
      Vector medicationsVect = new Vector();
      String x = null;
      for (int i = 0; i < list.size(); i++) {
        x = (String)list.get(i);
        if (x.length() > 0) {
          String pharmId = StringUtils.piece(x, 1);
          String name = StringUtils.mixedCase(StringUtils.piece(x, 2));
          String orderId = StringUtils.piece(x, 3);
          String status = StringUtils.mixedCase(StringUtils.piece(x, 4));
          Medication medication = new Medication();
          if (returnRpcResult)
            medication.setRpcResult(x);
          medication.setDfn(dfn);
          medication.setName(name);
          medication.setStatus(status);
          medication.setPharmId(pharmId);
          medication.setOrderId(orderId);
          medicationsVect.add(medication);
        } 
      }
      Medication[] medications = new Medication[medicationsVect.size()];
      for (int j = 0; j < medications.length; j++)
        medications[j] = (Medication)medicationsVect.get(j);
      medicationsList.setMedications(medications);
      return medicationsList;
    } else
      throw getCreateContextException("OR CPRS GUI CHART");
  }
  
  public synchronized String getMedDetails(String dfn, String id) throws BrokerException {
    if (setContext("OR CPRS GUI CHART")) {
      setDfn(dfn);
      Object[] params = {dfn, id.toUpperCase()};
      ArrayList list = lCall("ORWPS DETAIL", params);
      StringBuffer sb = new StringBuffer();
      for(int i = 0; i < list.size(); i++)
        sb.append((String)list.get(i) + "\n");
      return sb.toString();
    } else
      throw getCreateContextException("OR CPRS GUI CHART");
  }
  
  public synchronized String getMedAdminHistory(String dfn, String id) throws BrokerException {
    if (setContext("OR CPRS GUI CHART")) {
      setDfn(dfn);
      Object[] params = {dfn, id.toUpperCase()};
      ArrayList list = lCall("ORWPS MEDHIST", params);
      StringBuffer sb = new StringBuffer();
      for(int i = 0; i < list.size(); i++)
        sb.append((String)list.get(i) + "\n");
      return sb.toString();
    } else
      throw getCreateContextException("OR CPRS GUI CHART");
  }  
  
}
