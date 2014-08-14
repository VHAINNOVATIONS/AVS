package gov.va.med.lom.avs.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Column;

@Entity
@Table(name="ckoUsageLog")
public class UsageLog extends BaseModel implements Serializable {	

	private static final long serialVersionUID = 0;

	private String facilityNo;
	private String userDuz;
	private String patientDfn;
	private String locationIens;
	private String encounterDatetimes;
	private String action;
	private String data;
	
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
  @Column(name="locationIen") 
  public String getLocationIens() {
    return locationIens;
  }
  public void setLocationIens(String locationIens) {
    this.locationIens = locationIens;
  }
  @Column(name="encounterDatetime") 
  public String getEncounterDatetimes() {
    return encounterDatetimes;
  }
  public void setEncounterDatetimes(String encounterDatetimes) {
    this.encounterDatetimes = encounterDatetimes;
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
	
}
