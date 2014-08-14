package gov.va.med.lom.kaajee.jboss.model;

import java.io.Serializable;
import java.security.Principal;
import java.security.acl.Group;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;

public class KaajeeGroup implements Group, Serializable {

	private final String name;
	private final Set<Principal> users = new HashSet<Principal>();

	
	public KaajeeGroup(String name){
		this.name = name;
	}
	
	public String getName() {
		return name;
	}

	public boolean addMember(Principal user) {
		return users.add(user);
	}

	public boolean removeMember(Principal user) {
		return users.remove(user);
	}

	public boolean isMember(Principal member) {
		return users.contains(member);
	}

	public Enumeration<? extends Principal> members() {
		return Collections.enumeration(users);
	}

}
