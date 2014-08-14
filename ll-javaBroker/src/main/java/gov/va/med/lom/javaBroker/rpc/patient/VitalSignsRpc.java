package gov.va.med.lom.javaBroker.rpc.patient;

import java.util.ArrayList;
import java.text.ParseException;

import gov.va.med.lom.javaBroker.rpc.*;
import gov.va.med.lom.javaBroker.rpc.patient.models.*;
import gov.va.med.lom.javaBroker.util.FMDateUtils;
import gov.va.med.lom.javaBroker.util.StringUtils;
import gov.va.med.lom.javaBroker.util.DateUtils;

public class VitalSignsRpc extends AbstractRpc {
  
  // FIELDS
  private VitalSigns vitalSigns;
    
  // CONSTRUCTORS
  public VitalSignsRpc() throws BrokerException {
    super();
  }
  
  public VitalSignsRpc(RpcBroker rpcBroker) throws BrokerException {
    super(rpcBroker);
  }
  
  // RPC API    
  public synchronized VitalSigns getVitalSigns(String dfn) throws BrokerException {
    if (setContext("OR CPRS GUI CHART")) {
      setDfn(dfn);
      vitalSigns = new VitalSigns();      
      ArrayList list = lCall("ORQQVI VITALS", dfn);
      if (returnRpcResult) {
        StringBuffer sb = new StringBuffer();
        for(int i = 0; i < list.size(); i++)
          sb.append((String)list.get(i) + "\n");
        vitalSigns.setRpcResult(sb.toString().trim());
      }    
      String x = null;
      vitalSigns.setDfn(dfn);
      if (list.size() >= 1) {
        x = (String)list.get(0);
        vitalSigns.setTemperatureIen(StringUtils.piece(x, 1));
        vitalSigns.setTemperature(StringUtils.piece(x, 5));
        vitalSigns.setTemperatureDate(FMDateUtils.fmDateTimeToDate(StringUtils.piece(x, 4)));
        try {
          vitalSigns.setTemperatureDateStr(DateUtils.toEnglishDate(vitalSigns.getTemperatureDate()));
        } catch(ParseException pe) {}
      } 
      if (list.size() >= 2) {
        x = (String)list.get(1);
        vitalSigns.setPulseIen(StringUtils.piece(x, 1));
        vitalSigns.setPulse(StringUtils.piece(x, 5));
        vitalSigns.setPulseDate(FMDateUtils.fmDateTimeToDate(StringUtils.piece(x, 4)));
        try {
          vitalSigns.setPulseDateStr(DateUtils.toEnglishDate(vitalSigns.getPulseDate()));
        } catch(ParseException pe) {}      
      } 
      if (list.size() >= 3) {
        x = (String)list.get(2);
        vitalSigns.setRespirationsIen(StringUtils.piece(x, 1));
        vitalSigns.setRespirations(StringUtils.piece(x, 5));
        vitalSigns.setRespirationsDate(FMDateUtils.fmDateTimeToDate(StringUtils.piece(x, 4)));
        try {
          vitalSigns.setRespirationsDateStr(DateUtils.toEnglishDate(vitalSigns.getRespirationsDate()));
        } catch(ParseException pe) {}      
      } 
      if (list.size() >= 4) {
        x = (String)list.get(3);
        vitalSigns.setBpIen(StringUtils.piece(x, 1));
        vitalSigns.setBpSystolic(StringUtils.piece(StringUtils.piece(x,5), '/', 1));
        vitalSigns.setBpDiastolic(StringUtils.piece(StringUtils.piece(x,5), '/', 2));
        vitalSigns.setBpDate(FMDateUtils.fmDateTimeToDate(StringUtils.piece(x, 4)));
        try {
          vitalSigns.setBpDateStr(DateUtils.toEnglishDate(vitalSigns.getBpDate()));
        } catch(ParseException pe) {}      
      } 
      if (list.size() >= 5) {
        x = (String)list.get(4);
        vitalSigns.setHeightIen(StringUtils.piece(x, 1));
        vitalSigns.setHeight(StringUtils.piece(x, 5));
        vitalSigns.setHeightDate(FMDateUtils.fmDateTimeToDate(StringUtils.piece(x, 4)));
        try {
          vitalSigns.setHeightDateStr(DateUtils.toEnglishDate(vitalSigns.getHeightDate()));
        } catch(ParseException pe) {}      
      } 
      if (list.size() >= 6) {
        x = (String)list.get(5);
        vitalSigns.setWeightIen(StringUtils.piece(x, 1));
        vitalSigns.setWeight(StringUtils.piece(x, 5));
        vitalSigns.setWeightDate(FMDateUtils.fmDateTimeToDate(StringUtils.piece(x, 4)));
        try {
          vitalSigns.setWeightDateStr(DateUtils.toEnglishDate(vitalSigns.getWeightDate()));
        } catch(ParseException pe) {}      
      } 
      if (list.size() >= 7) {
        x = (String)list.get(6);
        vitalSigns.setPainIndexIen(StringUtils.piece(x, 1));
        vitalSigns.setPainIndex(StringUtils.piece(x, 5));
        vitalSigns.setPainIndexDate(FMDateUtils.fmDateTimeToDate(StringUtils.piece(x, 4)));
        try {
          vitalSigns.setPainIndexDateStr(DateUtils.toEnglishDate(vitalSigns.getPainIndexDate()));
        } catch(ParseException pe) {}      
      }
      return vitalSigns;
    } else
      throw getCreateContextException("OR CPRS GUI CHART");
  }
  
}
