package gov.va.med.lom.avs.model;

import java.util.List;
import java.util.ArrayList;

import org.mongodb.morphia.annotations.Entity;

@Entity("usageLog")
public class UsageLogMongo extends BaseEntity {	

	private String facilityNo;
	private String userDuz;
	private String patientDfn;
	private List<String> locationIens;
	private List<String> locationNames;
	private List<Double> datetimes;
	private String action;
	private String data;
	
	public UsageLogMongo() {
	  locationIens = new ArrayList<String>();
	}
	
  public String getFacilityNo() {
    return facilityNo;
  }
  public void setFacilityNo(String facilityNo) {
    this.facilityNo = facilityNo;
  }
  public String getPatientDfn() {
    return patientDfn;
  }
  public void setPatientDfn(String patientDfn) {
    this.patientDfn = patientDfn;
  }
  public String getAction() {
    return action;
  }
  public void setAction(String action) {
    this.action = action;
  }
  public String getData() {
    return data;
  }
  public void setData(String data) {
    this.data = data;
  }
  public String getUserDuz() {
    return userDuz;
  }
  public void setUserDuz(String userDuz) {
    this.userDuz = userDuz;
  }
  public List<String> getLocationIens() {
    return locationIens;
  }
  public void setLocationIens(List<String> locationIens) {
    this.locationIens = locationIens;
  }

  public List<String> getLocationNames() {
    return locationNames;
  }
  public void setLocationNames(List<String> locationNames) {
    this.locationNames = locationNames;
  }
  public List<Double> getDatetimes() {
    return datetimes;
  }
  public void setDatetimes(List<Double> datetimes) {
    this.datetimes = datetimes;
  }
  
}
