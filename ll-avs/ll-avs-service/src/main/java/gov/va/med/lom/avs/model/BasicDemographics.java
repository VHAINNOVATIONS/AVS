package gov.va.med.lom.avs.model;

import java.io.Serializable;
import java.util.Date;

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
	private String primaryProvider;
	private String primaryTeam;
	private String dobStr;
  private String deceasedDateStr;
  private int age;
  private boolean veteran;
  private int scPct;
  private String location;
  private String roomBed;
  private boolean inpatient;
	
	
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

	public String getPrimaryProvider() {
		return this.primaryProvider;
	}

	public String getPrimaryTeam() {
		return this.primaryTeam;
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

	public void setPrimaryProvider(String primaryProvider) {
		this.primaryProvider = primaryProvider;
	}

	public void setPrimaryTeam(String primaryTeam) {
		this.primaryTeam = primaryTeam;
	}

  public String getDobStr() {
    return dobStr;
  }

  public void setDobStr(String dobStr) {
    this.dobStr = dobStr;
  }

  public String getDeceasedDateStr() {
    return deceasedDateStr;
  }

  public void setDeceasedDateStr(String deceasedDateStr) {
    this.deceasedDateStr = deceasedDateStr;
  }

  public int getAge() {
    return age;
  }

  public void setAge(int age) {
    this.age = age;
  }

  public boolean isVeteran() {
    return veteran;
  }

  public void setVeteran(boolean veteran) {
    this.veteran = veteran;
  }

  public int getScPct() {
    return scPct;
  }

  public void setScPct(int scPct) {
    this.scPct = scPct;
  }

  public String getLocation() {
    return location;
  }

  public void setLocation(String location) {
    this.location = location;
  }

  public String getRoomBed() {
    return roomBed;
  }

  public void setRoomBed(String roomBed) {
    this.roomBed = roomBed;
  }

  public boolean isInpatient() {
    return inpatient;
  }

  public void setInpatient(boolean inpatient) {
    this.inpatient = inpatient;
  }
	
}
