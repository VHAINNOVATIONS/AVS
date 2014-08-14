package gov.va.med.lom.avs.report.model;

import java.io.Serializable;

import gov.va.med.lom.avs.model.Service;

public class ServiceJson implements Serializable {

  private Long id;
	private String facilityNo;
	private String name;
	private String location;
	private String hours;
	private String comment;
	
	public ServiceJson() {}
	
	public ServiceJson(Service service) {
	  this.id = service.getId();
	  this.facilityNo = service.getFacilityNo();
	  this.name = service.getName();
	  this.location = service.getLocation();
	  this.hours = service.getHours();
	  this.comment = service.getComment();
	}
	
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
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

}