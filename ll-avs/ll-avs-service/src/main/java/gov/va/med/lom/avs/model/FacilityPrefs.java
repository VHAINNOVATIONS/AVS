package gov.va.med.lom.avs.model;

import gov.va.med.lom.avs.util.SheetConfig;

import java.io.Serializable;
import javax.persistence.*;

/**
 * Facility Preferences bean
 */
@Entity
@Table(name="ckoFacilityPrefs")
public class FacilityPrefs extends BasePrefsModel implements Serializable {

  /* Data store */
  
  private static final long serialVersionUID = 0;

  private String facilityNo;
  private String timeZone;
  private String header;
  private String footer;
  private String tiuTitleIen;
  private String tiuNoteText;
  private String serviceDuz;
  private boolean kramesEnabled;
  private boolean servicesEnabled;
  private boolean medDescriptionsEnabled;
  private int refreshFrequency;
  private int upcomingAppointmentRange;
  private int encountersRange;
  private int orderTimeFrom;
  private int orderTimeThru;
  private String languages;
  
	/* Constructor */
	
	public FacilityPrefs() {}

	public FacilityPrefs(String facilityNo) {
		this.facilityNo = facilityNo;
	}

	/* Accessors */
	
	public String getFacilityNo() {
		return facilityNo;
	}

	public String getTimeZone() {
	  return timeZone;
	}
	
	public String getHeader() {
		return header;
	}
	
	public String getFooter() {
		return footer;
	}
	
	/* Mutators */
	
	public void setFacilityNo(String facilityNo) {
		this.facilityNo = facilityNo;
	}
	
	public void setTimeZone(String timeZone) {
	  this.timeZone = timeZone;
	}

	public void setHeader(String header) {
		this.header = header;
	}

	public void setFooter(String footer) {
		this.footer = footer;
	}

	public String getTiuTitleIen() {
	  return tiuTitleIen;
	}

	public void setTiuTitleIen(String tiuTitleIen) {
	  this.tiuTitleIen = tiuTitleIen;
	}
	
  public String getServiceDuz() {
    return serviceDuz;
  }

  public void setServiceDuz(String serviceDuz) {
    this.serviceDuz = serviceDuz;
  }
  
  public boolean getKramesEnabled() {
    return kramesEnabled;
  }

  public void setKramesEnabled(boolean kramesEnabled) {
    this.kramesEnabled = kramesEnabled;
  }

  public boolean getServicesEnabled() {
    return servicesEnabled;
  }

  public void setServicesEnabled(boolean servicesEnabled) {
    this.servicesEnabled = servicesEnabled;
  }
  
  public int getRefreshFrequency() {
    return refreshFrequency;
  }

  public void setRefreshFrequency(int refreshFrequency) {
    this.refreshFrequency = refreshFrequency;
  }
  
  public int getUpcomingAppointmentRange() {
    return upcomingAppointmentRange;
  }

  public void setUpcomingAppointmentRange(int upcomingAppointmentRange) {
    this.upcomingAppointmentRange = upcomingAppointmentRange;
  }  
  
  public String getTiuNoteText() {
    return tiuNoteText;
  }

  public void setTiuNoteText(String tiuNoteText) {
    this.tiuNoteText = tiuNoteText;
  }

  public int getOrderTimeFrom() {
    return orderTimeFrom;
  }

  public void setOrderTimeFrom(int orderTimeFrom) {
    this.orderTimeFrom = orderTimeFrom;
  }

  public int getOrderTimeThru() {
    return orderTimeThru;
  }

  public void setOrderTimeThru(int orderTimeThru) {
    this.orderTimeThru = orderTimeThru;
  }
  
  public String getLanguages() {
    return languages;
  }

  public void setLanguages(String languages) {
    this.languages = languages;
  }
  
  public boolean getMedDescriptionsEnabled() {
    return medDescriptionsEnabled;
  }

  public void setMedDescriptionsEnabled(boolean medDescriptionsEnabled) {
    this.medDescriptionsEnabled = medDescriptionsEnabled;
  }
  
  public int getEncountersRange() {
    return encountersRange;
  }

  public void setEncountersRange(int encountersRange) {
    this.encountersRange = encountersRange;
  }
  
  /* Transient methods */

  /**
	 * Populate a {@link SheetConfig} object
	 * @param config configuration to be populated
	 */
	public void assignSheetConfigValues(SheetConfig config) {
		if (this.getBoilerplate() != null) {
			config.setFacilityBoilerplate(this.getBoilerplate());
		}
		config.setHeader(this.header);
		config.setFooter(this.footer);
	}

}