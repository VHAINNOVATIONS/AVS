package gov.va.med.lom.vistabroker.patient.dao;

import gov.va.med.lom.javaUtils.misc.DateUtils;
import gov.va.med.lom.javaUtils.misc.StringUtils;
import gov.va.med.lom.vistabroker.dao.BaseDao;
import gov.va.med.lom.vistabroker.exception.VistaBrokerException;
import gov.va.med.lom.vistabroker.patient.data.VitalSignMeasurement;
import gov.va.med.lom.vistabroker.patient.data.VitalSigns;
import gov.va.med.lom.vistabroker.util.FMDateUtils;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class VitalSignsDao extends BaseDao {
  
  // CONSTRUCTORS
  public VitalSignsDao() {
    super();
  }
  
  public VitalSignsDao(BaseDao baseDao) {
    super(baseDao);
  }
  
  // RPC API
  
  /**
   * Get Vital Signs -- Retrieve a subset of the latest vital sign measurements
   * @param dfn patient's ID number
   * @return selected vital signs measurements, if found
   * @throws VistaBrokerException
   */
  public VitalSigns getVitalSigns(String dfn) throws VistaBrokerException {
    return this.getVitalSigns(dfn, 1, 9999999);
  }

  /**
   * Get Vital Signs -- Retrieve a subset of the latest vital sign measurements taken between two dates.
   * @param dfn patient's ID number
   * @param fmFromDate start of date range to check
   * @param fmThroughDate end of date range to check
   * @return selected vital signs measurements, if found
   * @throws VistaBrokerException
   */
  public VitalSigns getVitalSigns(String dfn, double fmFromDate, double fmThroughDate) throws VistaBrokerException {
    List<String> list = this._getRawVitals(dfn, fmFromDate, fmThroughDate);

    VitalSigns vitalSigns = new VitalSigns();      
    vitalSigns.setDfn(dfn);

    String ien = null;
    String recordType = null;
    Date date = null;
    String value = null;
    String dateStr = null;
    
    for (String x : list) {

      ien = StringUtils.piece(x, 1);
      recordType = StringUtils.piece(x, 2);
      date = FMDateUtils.fmDateTimeToDate(StringUtils.piece(x, 4));
      value = StringUtils.piece(x, 5);
      try {
        dateStr = DateUtils.toEnglishDate(date);
      } catch(ParseException pe) {
        dateStr = null;
      }
      
      // TEMPERATURE
      if (recordType.equals("T")) {

        vitalSigns.setTemperatureIen(ien);
        vitalSigns.setTemperature(value);
        vitalSigns.setTemperatureDate(date);
        vitalSigns.setTemperatureDateStr(dateStr);
      
      // PULSE
      } else if (recordType.equals("P")) {

        vitalSigns.setPulseIen(ien);
        vitalSigns.setPulse(value);
        vitalSigns.setPulseDate(date);
        vitalSigns.setPulseDateStr(dateStr);

        // RESPIRATIONS
      } else if (recordType.equals("R")) {

        vitalSigns.setRespirationsIen(ien);
        vitalSigns.setRespirations(value);
        vitalSigns.setRespirationsDate(date);
        vitalSigns.setRespirationsDateStr(dateStr);

      // BLOOD PRESSURE
      } else if (recordType.equals("BP")) {

        vitalSigns.setBpIen(ien);
        vitalSigns.setBpSystolic(StringUtils.piece(value, '/', 1));
        vitalSigns.setBpDiastolic(StringUtils.piece(value, '/', 2));
        vitalSigns.setBpDate(date);
        vitalSigns.setBpDateStr(dateStr);

      // HEIGHT
      } else if (recordType.equals("HT")) {

        vitalSigns.setHeightIen(ien);
        vitalSigns.setHeight(value);
        vitalSigns.setHeightDate(date);
        vitalSigns.setHeightDateStr(dateStr);

      // WEIGHT
      } else if (recordType.equals("WT")) {

        vitalSigns.setWeightIen(ien);
        vitalSigns.setWeight(value);
        vitalSigns.setWeightDate(date);
        vitalSigns.setWeightDateStr(dateStr);

      // PAIN INDEX
      } else if (recordType.equals("PN")) {

        vitalSigns.setPainIndexIen(ien);
        vitalSigns.setPainIndex(value);
        vitalSigns.setPainIndexDate(date);
        vitalSigns.setPainIndexDateStr(dateStr);

      // O2 SATURATION
      } else if (recordType.equals("POX")) {

        vitalSigns.setPoxIen(ien);
        vitalSigns.setPox(value);
        vitalSigns.setPoxDate(date);
        vitalSigns.setPoxDateStr(dateStr);

      // BODY MASS INDEX
      } else if (recordType.equals("BMI")) {

        vitalSigns.setBMIIen(ien);
        vitalSigns.setBMI(value);
        vitalSigns.setBMIDate(date);
        vitalSigns.setBMIDateStr(dateStr);

      }
    }
    return vitalSigns;
  }
  
  /**
   * Get a complete list of the latest vital signs measurements on record
   * @param dfn patient's ID number
   * @return selected vital signs measurements, if found
   * @throws VistaBrokerException
   */
  public List<VitalSignMeasurement> getVitalSignsList(String dfn) throws VistaBrokerException {
    return this.getVitalSignsList(dfn, 1, 9999999);
  }

  /**
   * Get a complete list of all vital signs that were measured between two dates
   * @param dfn patient's ID number
   * @param fmFromDate start of date range to check
   * @param fmThroughDate end of date range to check
   * @return parsed list of measurements
   * @throws VistaBrokerException
   */
  public List<VitalSignMeasurement> getVitalSignsList(String dfn, double fmFromDate, double fmThroughDate) throws VistaBrokerException {
    List<String> list = this._getRawVitals(dfn, fmFromDate, fmThroughDate);
    List<VitalSignMeasurement> measurements = new ArrayList<VitalSignMeasurement>();
      
    for (String x : list) {
      VitalSignMeasurement measurement = new VitalSignMeasurement();        
      measurement.setIen(StringUtils.piece(x, 1));
      measurement.setTypeAbbr(StringUtils.piece(x, 2));
      measurement.setRawValue(StringUtils.piece(x, 3));
      measurement.setDate(FMDateUtils.fmDateTimeToDate(StringUtils.piece(x, 4)));
      measurement.setEnglishValue(StringUtils.piece(x, 5));
      measurement.setMetricValue(StringUtils.piece(x, 6));
      measurement.setQualifiers(StringUtils.piece(x, 7));
      measurements.add(measurement);
    }

    return measurements;
  }

  /**
   * Get the latest vital signs that were measured between the dates specified, in unparsed form straight from VistA
   * @param dfn patient's ID number
   * @param fmFromDate start of date range to check
   * @param fmThroughDate end of date range to check
   * @return raw list of vital signs records from VistA
   * @throws VistaBrokerException
   */
  private List<String> _getRawVitals(String dfn, double fmFromDate, double fmThroughDate) throws VistaBrokerException {
    setDefaultContext("OR CPRS GUI CHART");
    setDefaultRpcName("ORQQVI VITALS");
    Object[] params = {dfn, fmFromDate, fmThroughDate};
    return lCall(params);
  }
}
