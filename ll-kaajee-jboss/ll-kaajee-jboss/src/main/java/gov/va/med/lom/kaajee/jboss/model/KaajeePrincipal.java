package gov.va.med.lom.kaajee.jboss.model;

import java.io.Serializable;
import java.security.Principal;

public class KaajeePrincipal implements Principal, Serializable{

	
	private final String name;
	
	
	public KaajeePrincipal(String name){
		this.name = name;
	}
	
	
	public String getName() {
		return name;
	}

		
}
