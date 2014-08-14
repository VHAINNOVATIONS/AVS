package gov.va.med.lom.avs.model;

import java.util.Date;

import org.bson.types.ObjectId;

import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.PrePersist;
import org.mongodb.morphia.annotations.Version;

/**
 * Provide the BaseEntity implementation for all entities:
 * 
 * @Id, creation and last change date, version, their getters and setters
 *      (including @PrePersist), and some abstract methods we'll require in the
 *      specific entities.
 */
public abstract class BaseEntity {

	@Id
	protected ObjectId id;

	/**
	 * We'll only provide getters for these attributes, setting is done in
	 * 
	 * @PrePersist.
	 */
  private Date dateCreated;
  private Date dateModified;

	/**
	 * No getters and setters required, the version is handled internally.
	 */
	@Version
	private long version;

	public BaseEntity() {
		super();
	}

	public ObjectId getId() {
		return id;
	}

	public void setId(ObjectId id) {
		this.id = id;
	}

  public Date getDateCreated() {
    return this.dateCreated;
  }
  
  public Date getDateModified() {
    return this.dateModified;
  }

	@PrePersist
	public void prePersist() {
		this.dateCreated = (dateCreated == null) ? new Date() : dateCreated;
		this.dateModified = (dateModified == null) ? dateModified : new Date();
	}

	/*
  public long getVersion() {
    return version;
  }

  public void setVersion(long version) {
    this.version = version;
  }
*/
	
	
}
