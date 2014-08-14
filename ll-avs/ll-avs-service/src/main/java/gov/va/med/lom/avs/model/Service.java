package gov.va.med.lom.avs.model;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="ckoServices")
public class Service extends BaseModel implements Serializable {

	private static final long serialVersionUID = 0;

	private String facilityNo;
	private String name;
	private String location;
	private String hours;
	private String phone;
	private String comment;
	
	public Service() {}

	public Service(String facilityNo, String name, String location, String hours, String phone, String comment) {
		this.facilityNo = facilityNo;
		this.name = name;
		this.location = location;
		this.hours = hours;
		this.phone = phone;
		this.comment = comment;
	}

  public String getFacilityNo() {
    return facilityNo;
  }

  public void setFacilityNo(String facilityNo) {
    this.facilityNo = facilityNo;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getLocation() {
    return location;
  }

  public void setLocation(String location) {
    this.location = location;
  }

  public String getHours() {
    return hours;
  }

  public void setHours(String hours) {
    this.hours = hours;
  }

  public String getComment() {
    return comment;
  }

  public void setComment(String comment) {
    this.comment = comment;
  }

  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

}