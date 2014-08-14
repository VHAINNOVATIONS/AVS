package gov.va.med.lom.vistabroker.patient.data;

import java.io.Serializable;
import java.util.Date;

public class VitalSigns implements Serializable {

  private String dfn;
  private String temperatureIen;
  private String temperature;
  private Date temperatureDate;
  private String temperatureDateStr;
  private String pulseIen;
  private String pulse;
  private Date pulseDate;
  private String pulseDateStr;
  private String respirationsIen;
  private String respirations;
  private Date respirationsDate;
  private String respirationsDateStr;
  private String bpIen;
  private String bpSystolic;
  private String bpDiastolic;
  private Date bpDate;
  private String bpDateStr;
  private String heightIen;
  private String height;
  private Date heightDate;
  private String heightDateStr;
  private String weightIen;
  private String weight;
  private Date weightDate;
  private String weightDateStr;
  private String painIndexIen;
  private String painIndex;
  private Date painIndexDate;
  private String painIndexDateStr;
  private String poxIen;
  private String pox;
  private Date poxDate;
  private String poxDateStr;
  private String bmiIen;
  private String bmi;
  private Date bmiDate;
  private String bmiDateStr;
  
  public VitalSigns() {
    this.dfn = null;
    this.temperatureIen = null;
    this.temperature = null;
    this.temperatureDate = null;
    this.temperatureDateStr = null;
    this.pulseIen = null;
    this.pulse = null;
    this.pulseDate = null;
    this.pulseDateStr = null;
    this.respirationsIen = null;
    this.respirations = null;
    this.respirationsDate = null;
    this.respirationsDateStr = null;
    this.bpIen = null;
    this.bpSystolic = null;
    this.bpDiastolic = null;
    this.bpDate = null;
    this.bpDateStr = null;
    this.heightIen = null;
    this.height = null;
    this.heightDate = null;
    this.heightDateStr = null;
    this.weightIen = null;
    this.weight = null;
    this.weightDate = null;
    this.weightDateStr = null;
    this.painIndexIen = null;
    this.painIndex = null;
    this.painIndexDate = null;
    this.painIndexDateStr = null;
    this.poxIen = null;
    this.pox = null;
    this.poxDate = null;
    this.poxDateStr = null;
    this.bmiIen = null;
    this.bmi = null;
    this.bmiDate = null;
    this.bmiDateStr = null;
  }
  
  public String getDfn() {
    return dfn;
  }

  public void setDfn(String dfn) {
    this.dfn = dfn;
  }

  public Date getBpDate() {
    return bpDate;
  }
  
  public void setBpDate(Date bpDate) {
    this.bpDate = bpDate;
  }
  
  public String getBpDiastolic() {
    return bpDiastolic;
  }
  
  public void setBpDiastolic(String bpDiastolic) {
    this.bpDiastolic = bpDiastolic;
  }
  
  public String getBpIen() {
    return bpIen;
  }
  
  public void setBpIen(String bpIen) {
    this.bpIen = bpIen;
  }
  
  public String getBpSystolic() {
    return bpSystolic;
  }
  
  public void setBpSystolic(String bpSystolic) {
    this.bpSystolic = bpSystolic;
  }
  
  public String getHeight() {
    return height;
  }
  
  public void setHeight(String height) {
    this.height = height;
  }

  public Date getHeightDate() {
    return heightDate;
  }
  
  public void setHeightDate(Date heightDate) {
    this.heightDate = heightDate;
  }
  
  public String getHeightIen() {
    return heightIen;
  }
  
  public void setHeightIen(String heightIen) {
    this.heightIen = heightIen;
  }
  
  public String getPainIndex() {
    return painIndex;
  }
  
  public void setPainIndex(String painIndex) {
    this.painIndex = painIndex;
  }
  
  public Date getPainIndexDate() {
    return painIndexDate;
  }
  
  public void setPainIndexDate(Date painIndexDate) {
    this.painIndexDate = painIndexDate;
  }
  
  public String getPainIndexIen() {
    return painIndexIen;
  }
  
  public void setPainIndexIen(String painIndexIen) {
    this.painIndexIen = painIndexIen;
  }
  
  public String getPulse() {
    return pulse;
  }
  
  public void setPulse(String pulse) {
    this.pulse = pulse;
  }
  
  public Date getPulseDate() {
    return pulseDate;
  }
  
  public void setPulseDate(Date pulseDate) {
    this.pulseDate = pulseDate;
  }
  
  public String getPulseIen() {
    return pulseIen;
  }
  
  public void setPulseIen(String pulseIen) {
    this.pulseIen = pulseIen;
  }
  
  public String getRespirations() {
    return respirations;
  }
  
  public void setRespirations(String respirations) {
    this.respirations = respirations;
  }
  
  public Date getRespirationsDate() {
    return respirationsDate;
  }
  
  public void setRespirationsDate(Date respirationsDate) {
    this.respirationsDate = respirationsDate;
  }
  
  public String getRespirationsIen() {
    return respirationsIen;
  }
  
  public void setRespirationsIen(String respirationsIen) {
    this.respirationsIen = respirationsIen;
  }
  
  public String getTemperature() {
    return temperature;
  }
  
  public void setTemperature(String temperature) {
    this.temperature = temperature;
  }
  
  public Date getTemperatureDate() {
    return temperatureDate;
  }
  
  public void setTemperatureDate(Date temperatureDate) {
    this.temperatureDate = temperatureDate;
  }
  
  public String getTemperatureIen() {
    return temperatureIen;
  }
  
  public void setTemperatureIen(String temperatureIen) {
    this.temperatureIen = temperatureIen;
  }
  
  public String getWeight() {
    return weight;
  }
  
  public void setWeight(String weight) {
    this.weight = weight;
  }
  
  public Date getWeightDate() {
    return weightDate;
  }
  
  public void setWeightDate(Date weightDate) {
    this.weightDate = weightDate;
  }
  
  public String getWeightIen() {
    return weightIen;
  }
  
  public void setWeightIen(String weightIen) {
    this.weightIen = weightIen;
  }
  
  public String getBpDateStr() {
    return bpDateStr;
  }

  public void setBpDateStr(String bpDateStr) {
    this.bpDateStr = bpDateStr;
  }
  
  public String getHeightDateStr() {
    return heightDateStr;
  }
  
  public void setHeightDateStr(String heightDateStr) {
    this.heightDateStr = heightDateStr;
  }
  
  public String getPainIndexDateStr() {
    return painIndexDateStr;
  }
  
  public void setPainIndexDateStr(String painIndexDateStr) {
    this.painIndexDateStr = painIndexDateStr;
  }
  
  public String getPulseDateStr() {
    return pulseDateStr;
  }
  
  public void setPulseDateStr(String pulseDateStr) {
    this.pulseDateStr = pulseDateStr;
  }
  
  public String getRespirationsDateStr() {
    return respirationsDateStr;
  }
  
  public void setRespirationsDateStr(String respirationsDateStr) {
    this.respirationsDateStr = respirationsDateStr;
  }
  
  public String getTemperatureDateStr() {
    return temperatureDateStr;
  }
  
  public void setTemperatureDateStr(String temperatureDateStr) {
    this.temperatureDateStr = temperatureDateStr;
  }
  
  public String getWeightDateStr() {
    return weightDateStr;
  }
  
  public void setWeightDateStr(String weightDateStr) {
    this.weightDateStr = weightDateStr;
  }
  
  public String getPoxDateStr() {
    return poxDateStr;
  }
  
  public void setPoxDateStr(String poxDateStr) {
    this.poxDateStr = poxDateStr;
  }

  public String getBMIDateStr() {
    return bmiDateStr;
  }
  
  public void setBMIDateStr(String bmiDateStr) {
    this.bmiDateStr = bmiDateStr;
  }

  public String getPox() {
    return pox;
  }
	  
  public void setPox(String pox) {
    this.pox = pox;
  }

  public Date getPoxDate() {
    return poxDate;
  }
  
  public void setPoxDate(Date poxDate) {
    this.poxDate = poxDate;
  }
  
  public String getPoxIen() {
    return poxIen;
  }
  
  public void setPoxIen(String poxIen) {
    this.poxIen = poxIen;
  }

  public String getBMI() {
    return bmi;
  }
	  
  public void setBMI(String bmi) {
    this.bmi = bmi;
  }

  public Date getBMIDate() {
    return bmiDate;
  }
  
  public void setBMIDate(Date bmiDate) {
    this.bmiDate = bmiDate;
  }
  
  public String getBMIIen() {
    return bmiIen;
  }
  
  public void setBMIIen(String bmiIen) {
    this.bmiIen = bmiIen;
  }
}
