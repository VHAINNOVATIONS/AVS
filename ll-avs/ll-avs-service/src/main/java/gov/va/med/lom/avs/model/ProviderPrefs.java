package gov.va.med.lom.avs.model;

import gov.va.med.lom.avs.util.SheetConfig;

import java.io.Serializable;
import javax.persistence.*;

/**
 * Provider Preferences bean
 */
@Entity
@Table(name="ckoProviderPrefs")
public class ProviderPrefs extends BasePrefsModel implements Serializable {

	/* Constructors */
	
	public ProviderPrefs() {}
	
	public ProviderPrefs(String providerDuz, String facilityNo) {
		this.providerDuz = providerDuz;
		this.facilityNo = facilityNo;
	}

	/* Accessors */
	
	public String getFacilityNo() {
		return facilityNo;
	}

	public String getProviderDuz() {
		return providerDuz;
	}

	/* Mutators */
	
	public void setFacilityNo(String facilityNo) {
		this.facilityNo = facilityNo;
	}

	public void setProviderDuz(String providerDuz) {
		this.providerDuz = providerDuz;
	}

	/* Transient methods */
	
	/**
	 * Populate a {@link SheetConfig} object
	 * @param config configuration to be populated
	 */
	public void assignSheetConfigValues(SheetConfig config) {
		if (this.getBoilerplate() != null) {
			config.setProviderBoilerplate(this.getBoilerplate());
		}
		/*
		if (this.getLayout() != null) {
			config.setLayout(this.getLayout());
		}
		*/
	}

	/* Data store */
	
	private static final long serialVersionUID = 0;

	private String facilityNo;
	private String providerDuz;
}