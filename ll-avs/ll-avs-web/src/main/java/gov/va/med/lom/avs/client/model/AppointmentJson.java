package gov.va.med.lom.avs.client.model;

import java.io.Serializable;

public class AppointmentJson implements Serializable {

    private String site;
    private String stationNo;
    private String location;
    private String datetime;
    private double fmDatetime;
    private String type;
    
    public String getLocation() {
      return location;
    }
    
    public void setLocation(String location) {
      this.location = location;
    }
    
    public double getFmDatetime() {
      return fmDatetime;
    }

    public void setFmDatetime(double fmDatetime) {
      this.fmDatetime = fmDatetime;
    }

    public String getSite() {
      return site;
    }

    public void setSite(String site) {
      this.site = site;
    }

    public String getStationNo() {
      return stationNo;
    }

    public void setStationNo(String stationNo) {
      this.stationNo = stationNo;
    }

    public String getDatetime() {
      return datetime;
    }

    public void setDatetime(String datetime) {
      this.datetime = datetime;
    }

    public String getType() {
      return type;
    }

    public void setType(String type) {
      this.type = type;
    }

}
