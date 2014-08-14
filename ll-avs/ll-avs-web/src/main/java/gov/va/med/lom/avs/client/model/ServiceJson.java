package gov.va.med.lom.avs.client.model;

import gov.va.med.lom.avs.model.Service;

public class ServiceJson {

	private Long id;
	private String name;
	private String location;
	private String hours;
	private String phone;
	private String comment;

	protected ServiceJson() {}

	public ServiceJson(Service service) {
		this.id = service.getId();
		this.name = service.getName();
		this.location = service.getLocation();
		this.hours = service.getHours();
		this.phone = service.getPhone();
		this.comment = service.getComment();
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
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
