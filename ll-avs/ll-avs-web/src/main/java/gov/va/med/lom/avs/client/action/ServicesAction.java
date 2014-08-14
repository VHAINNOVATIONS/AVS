package gov.va.med.lom.avs.client.action;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import gov.va.med.lom.foundation.service.response.CollectionServiceResponse;
import gov.va.med.lom.foundation.service.response.ServiceResponse;
import gov.va.med.lom.json.util.JsonResponse;

import gov.va.med.lom.avs.client.model.ServiceJson;
import gov.va.med.lom.avs.web.util.AvsWebUtils;
import gov.va.med.lom.avs.model.Service;
import gov.va.med.lom.avs.service.ServiceFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ServicesAction extends BaseCardAction {

	protected static final Log log = LogFactory.getLog(ServicesAction.class);

	private static final long serialVersionUID = 0;

	private Long id;
	private String name;
	private String location;
	private String hours;
	private String phone;
	private String comment;

	@SuppressWarnings("unchecked")
	public String fetch() throws Exception {
	  usageLog("Fetch Services", ""); 
		CollectionServiceResponse<Service> serviceResponse = ServiceFactory.getSettingsService()
			.fetchServices(super.facilityNo);
		AvsWebUtils.handleServiceErrors(serviceResponse, log);
		Collection<Service> services = serviceResponse.getCollection();

		ServiceResponse<Long> serviceResponse2 = ServiceFactory.getSettingsService().fetchServicesCount(super.facilityNo);
		AvsWebUtils.handleServiceErrors(serviceResponse2, log);
		Integer totalCount = safelyConvertLongToInteger(serviceResponse2.getPayload());

		List<ServiceJson> items = new ArrayList<ServiceJson>();
		for (Service service : services) {
			items.add(new ServiceJson(service));
		}

    JsonResponse.embedRoot(response, true, totalCount, "root", false, (Object)items);
    return SUCCESS;
	}

	public String add() throws Exception {
	  usageLog("Add Service", "Name=" + this.name + ", Location=" + this.location + ", Hours=" + 
	      this.hours + ", Phone: " + phone + ", Comment=" + this.comment);
	    
	  ServiceResponse<Service> response = ServiceFactory.getSettingsService()
	     .addService(super.facilityNo, this.name, this.location, this.hours, this.phone, this.comment);

	  AvsWebUtils.handleServiceErrors(response, log);

	  Service service = response.getPayload();

	  return super.setJson(new ServiceJson(service));
	}
	
	public String update() throws Exception {
	  usageLog("Update Service", "ID=" + this.id + ", Name=" + this.name + ", Location=" + this.location + ", Hours=" + this.hours + 
	      ", Phone: " + phone + ", Comment=" + this.comment);
	  
		ServiceResponse<Service> response = ServiceFactory.getSettingsService()
			.updateService(super.facilityNo, this.id, this.name, this.location, this.hours, this.phone, this.comment);

		AvsWebUtils.handleServiceErrors(response, log);

		Service service = response.getPayload();

		return super.setJson(new ServiceJson(service));
	}
	
  public String delete() throws Exception {
    System.out.println("Delete Service: ID=" + this.id);
    usageLog("Delete Service", "ID=" + this.id);
    
    ServiceFactory.getSettingsService().deleteService(this.id);

    return SUCCESS;
  }	

  public Long getId() {
    return id;
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
