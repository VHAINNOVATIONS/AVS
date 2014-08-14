package gov.va.med.lom.javaBroker.rpc.admin.models;

import java.io.Serializable;
import java.util.Date;

public class PpdItem implements Serializable{

	private long duz;
	private String service;
	private String name;
	private Date lastComplete;
	
	public long getDuz() {
		return duz;
	}
	public void setDuz(long duz) {
		this.duz = duz;
	}
	public String getService() {
		return service;
	}
	public void setService(String service) {
		this.service = service;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Date getLastComplete() {
		return lastComplete;
	}
	public void setLastComplete(Date lastComplete) {
		this.lastComplete = lastComplete;
	}
	
}
