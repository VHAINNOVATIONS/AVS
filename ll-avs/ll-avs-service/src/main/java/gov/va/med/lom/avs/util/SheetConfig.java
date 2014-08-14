package gov.va.med.lom.avs.util;

/**
 * Configuration settings for rendering an AVS.
 */
public class SheetConfig {
  
  /* Data store */
  
  //private String timeZone;
  private String layout = "today=1%seen=1^vitals=1^orders=1|notes=1%appointments=1^medchanges=1^education=1^comments=1|ongoing=1%provider=1^allergies=1^meds=1";
  private String facilityBoilerplate = "";
  private String clinicBoilerplate = "";
  private String providerBoilerplate = "";
  private String header =
    "<div style=\"float:right;margin:0 0 5px 20px\">\n"
    + "\t<img src=\"../common/images/Dept_of_VA_Affairs-greyscale.png\" width=\"205\" height=\"42\" alt=\"Department of Veterans Affairs\" />\n"
    + "</div>\n"
    + "<div style=\"font-size:1.8em;font-weight:bold\">After Visit Summary</div>\n"
    + "<div style=\"font-size:1.1em\">\n"
    + "\t%PATIENT_NAME%<br />\n"
    + "\tVisit date: %ENCOUNTER_DATE%<br />\n"
    + "\t<div style=\"font-size:0.9em\">Date generated: %CURRENT_DATETIME%</div>\n"
    + "</div>\n";
  private String footer = 
    "Access health resources. Track your health. Refill VA prescriptions. Visit www.myhealth.va.gov! "
    + "Ask your health care team about in-person authentication and begin ordering medications and " 
    + "viewing appointments through MyHealtheVet. After completing in-person authentication, click on "
    + "\"Secure Messaging\" in MyHealtheVet and select \"I would like to opt in to secure messaging\" "
    + "in order to send email messages to your providers.";  

  private String pvsFooter = footer;
  
	/* Constructors */
	
	public SheetConfig() {}
	
	/* Accessors */

	public String getLayout() {
		return this.layout;
	}
	/*
	public String getTimeZone() {
    return timeZone;
  }
  */
  public String getProviderBoilerplate() {
		return this.providerBoilerplate;
	}

	public String getClinicBoilerplate() {
		return this.clinicBoilerplate;
	}

	public String getFacilityBoilerplate() {
		return this.facilityBoilerplate;
	}
	
	public String getHeader() {
		return this.header;
	}

	public String getFooter() {
		return this.footer;
	}

	/* Mutators */
	/*
  public void setTimeZone(String timeZone) {
    this.timeZone = timeZone;
  }
  */
	public void setLayout(String value) {
		this.layout = value;
	}

	public void setProviderBoilerplate(String value) {
		this.providerBoilerplate = value;
	}

	public void setClinicBoilerplate(String value) {
		this.clinicBoilerplate = value;
	}

	public void setFacilityBoilerplate(String value) {
		this.facilityBoilerplate = value;
	}
	
	public void setHeader(String value) {
		this.header = value;
	}

	public void setFooter(String value) {
		this.footer = value;
	}

  public String getPvsFooter() {
    return pvsFooter;
  }

  public void setPvsFooter(String pvsFooter) {
    this.pvsFooter = pvsFooter;
  }

}

