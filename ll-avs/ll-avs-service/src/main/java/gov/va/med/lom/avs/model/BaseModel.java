package gov.va.med.lom.avs.model;

import gov.va.med.lom.jpa.foundation.model.ModelObject;

import java.io.Serializable;

import java.util.Date;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;

/**
 * Base model that all models extend
 */
@MappedSuperclass
public abstract class BaseModel extends ModelObject implements Serializable {

	/* Constructors */
	
	public BaseModel() {
		this.dateCreated = new Date();
		this.dateModified = new Date();
	}

  /* Data store */
  
  private static final long serialVersionUID = 0;

  private Long id;
  private Date dateCreated;
  private Date dateModified;
  private boolean active = true;
  //private Long oplock;	
	
	/* Accessors */
	
	@Id 
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public Long getId() {
		return this.id;
	}
  
	public Date getDateCreated() {
		return this.dateCreated;
	}
  
	public Date getDateModified() {
		return this.dateModified;
	}

	public boolean getActive() {
		return this.active;
	}

	/*
	@Version
	public Long getOplock() {
		return this.oplock;
	}
	*/

	/* Mutators */
	
	public void setId(Long id) {
		this.id = id;
	}

	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}

	public void setDateModified(Date dateModified) {
		this.dateModified = dateModified;
	}
	
	public void setActive(boolean active) {
		this.active = active;
	}

	/*
	public void setOplock(Long oplock) {
		this.oplock = oplock;
	}
	*/

}
