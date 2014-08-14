package gov.va.med.lom.avs.client.model;

import java.io.Serializable;

/**
 * Patient Profile -- Details about the patient that were set at birth and
 * which are more or less unchanging.  In a perfect world, this would be
 * stored in VistA and accessible through VistaBroker's PatientInfo object!
 */
public class BasicDemographics implements Serializable {


	private static final long serialVersionUID = 0;

	private String facilityNo;
	private String dfn;
	private Double dob;
	private String name;
	private String sex;
	private String ssn;

	/**
	 * Disable the normal constructor since we are requiring some params in
	 * order to instantiate it
	 */
	protected BasicDemographics() {}
	
	public BasicDemographics(String facilityNo, String dfn) {
		this.setFacilityNo(facilityNo);
		this.setDfn(dfn);
	}
	
	/* Accessors */
	
	public String getFacilityNo() {
		return this.facilityNo;	
	}

	public String getDfn() {
		return this.dfn;		
	}
	
	/**
	 * @return Date of Birth in FMDate format
	 */
	public Double getDob() {
		return this.dob;		
	}

	/**
	 * @return Name of patient, in "Lastname,Firstname MI" format
	 */
	public String getName() {
		return this.name;		
	}

	/**
	 * @return "M" or "F"
	 */
	public String getSex() {
		return this.sex;		
	}

	public String getSsn() {
		return this.ssn;		
	}

	public void setFacilityNo(String facilityNo) {
		this.facilityNo = facilityNo;
	}
	
	public void setDfn(String dfn) {
		this.dfn = dfn;
	}

	public void setDob(Double dob) {
		this.dob = dob;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public void setSsn(String ssn) {
		this.ssn = ssn;
	}

}
