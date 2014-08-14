package gov.va.med.lom.avs.client.model;

import java.io.Serializable;

public class ClinicVisitedJson implements Serializable {

    private String date;
    private String time;
    private String clinic;
    private String provider;
    private String site;
    
    public String getClinic() {
      return clinic;
    }
    public void setClinic(String clinic) {
      this.clinic = clinic;
    }
    public String getProvider() {
      return provider;
    }
    public void setProvider(String provider) {
      this.provider = provider;
    }
    public String getDate() {
      return date;
    }
    public void setDate(String date) {
      this.date = date;
    }
    public String getTime() {
      return time;
    }
    public void setTime(String time) {
      this.time = time;
    }
    public String getSite() {
      return site;
    }
    public void setSite(String site) {
      this.site = site;
    }

}
