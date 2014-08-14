package gov.va.med.lom.jpa.foundation.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;

@MappedSuperclass
public abstract class PersistentObject extends ModelObject implements Serializable{

    private Long id;
    private Long oplock;
    private boolean active;
    private Date dateCreated;
    private Date dateModified;

    public PersistentObject() {
        this.active = true;
        dateCreated = new Date();
        dateModified = new Date();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
    	firePropertyChange("id", this.id, this.id = id);
    }
    public boolean isActive() {
        return active;
    }
    public void setActive(boolean active) {
    	firePropertyChange("active", this.active, this.active = active);
    }
    public Date getDateCreated() {
        return dateCreated;
    }
    public void setDateCreated(Date dateCreated) {
    	firePropertyChange("dateCreated", this.dateCreated, this.dateCreated = dateCreated);
    }
    public Date getDateModified() {
        return dateModified;
    }
    public void setDateModified(Date dateModified) {
    	firePropertyChange("dateModified", this.dateModified, this.dateModified = dateModified);
    }
    
    @Version
	public Long getOplock() {
		return oplock;
	}
	public void setOplock(Long oplock) {
		this.oplock = oplock;
	}

}
