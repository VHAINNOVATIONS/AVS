package gov.va.med.lom.avs.model;

import gov.va.med.lom.avs.util.SheetConfig;

import java.io.Serializable;
import javax.persistence.MappedSuperclass;

/**
 * Base model for Preferences beans
 */
@MappedSuperclass
public abstract class BasePrefsModel extends BaseModel implements Serializable {

	/* Constructors */
	
	public BasePrefsModel() {}
	
	/* Accessors */
	
	/* Mutators */
	
	public String getBoilerplate() {
		return boilerplate;
	}
	
	public void setBoilerplate(String boilerplate) {
		this.boilerplate = boilerplate;
	}
	
	/* Transient methods */
	
	public abstract void assignSheetConfigValues(SheetConfig config);
	
	/* Data store */
	
	private static final long serialVersionUID = 0;

	private String boilerplate;
}
