package gov.va.med.lom.vistabroker.patient.data;

import java.io.Serializable;
import java.util.Calendar;

public class Appointment implements Serializable {

  private String dfn;
  private String id;
  private String location;
  private String header;
  private Calendar datetime;
  private String datetimeStr;
  private double fmDatetime;
  private double inverseDate;
  
  public Appointment() {
    this.dfn = null;
    this.id = null;
    this.location = null;
    this.datetime = null;
    this.fmDatetime = 0.0;
    this.inverseDate = 0.0;
  }
  
  public String getDfn() {
    return dfn;
  }

  public void setDfn(String dfn) {
    this.dfn = dfn;
  }

  public String getId() {
    return id;
  }
  
  public void setId(String id) {
    this.id = id;
  }
  
  public Calendar getDatetime() {
    return datetime;
  }
  
  public void setDatetime(Calendar datetime) {
    this.datetime = datetime;
  }  
  
  public String getDatetimeStr() {
    return datetimeStr;
  }
  
  public void setDatetimeStr(String datetimeStr) {
    this.datetimeStr = datetimeStr;
  }
  
  public String getLocation() {
    return location;
  }
  
  public void setLocation(String location) {
    this.location = location;
  }
  
  public String getHeader() {
    return header;
  }

  public void setHeader(String header) {
    this.header = header;
  }
  
  public double getFmDatetime() {
    return fmDatetime;
  }

  public void setFmDatetime(double fmDatetime) {
    this.fmDatetime = fmDatetime;
  }

  public double getInverseDate() {
    return inverseDate;
  }
  
  public void setInverseDate(double inverseDate) {
    this.inverseDate = inverseDate;
  }
  
}
