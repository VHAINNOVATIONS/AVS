package gov.va.med.lom.avs.model;

import gov.va.med.lom.avs.util.SheetConfig;

import java.io.Serializable;
import javax.persistence.*;

/**
 * Clinic Preferences bean
 */
@Entity
@Table(name="ckoClinicPrefs")
public class ClinicPrefs extends BasePrefsModel implements Serializable {

	/* Constructors */
	
	public ClinicPrefs() {}
	
	public ClinicPrefs(String clinicIen, String facilityNo) {
		this.clinicIen = clinicIen;
		this.facilityNo = facilityNo;
	}

	/* Accessors */
	
	public String getFacilityNo() {
		return facilityNo;
	}
	
	public String getClinicIen() {
		return clinicIen;
	}

	/* Mutators */
	
	public void setFacilityNo(String facilityNo) {
		this.facilityNo = facilityNo;
	}

	public void setClinicIen(String clinicIen) {
		this.clinicIen = clinicIen;
	}

	/* Transient methods */
	
	/**
	 * Populate a {@link SheetConfig} object
	 * @param config configuration to be populated
	 */
	public void assignSheetConfigValues(SheetConfig config) {
		if (this.getBoilerplate() != null) {
			config.setClinicBoilerplate(this.getBoilerplate());
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
	private String clinicIen;

}