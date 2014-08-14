package gov.va.med.lom.avs.model;

import gov.va.med.lom.avs.enumeration.TranslationTypeEnum;
import gov.va.med.lom.avs.model.Language;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="ckoTranslations")
public class Translation extends BaseModel implements Serializable {

	private static final long serialVersionUID = 0;

	private String facilityNo;
	private String source;
	private TranslationTypeEnum type;
	private String translation;
	private Language language;
	
	public Translation() {}

	/*
	public Translation(Translation translation) {
		super.setId(translation.getId());
		super.setActive(translation.getActive());

		this.setFacilityNo(translation.getFacilityNo());
		this.setSource(translation.getSource());
		this.setTranslation(translation.getTranslation());
	}
	*/

	public Translation(String facilityNo, Language language, TranslationTypeEnum type, String source) {
		this.facilityNo = facilityNo;
		this.type = type;
		this.source = source;
		this.language = language;
	}

	/* Accessors */

	public String getFacilityNo() {
		return this.facilityNo;
	}

	public String getSource() {
		return this.source;
	}

	public TranslationTypeEnum getType() {
		return this.type;
	}

	public String getTranslation() {
		return this.translation;
	}

	/* Mutators */

	public void setFacilityNo(String facilityNo) {
		this.facilityNo = facilityNo;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public void setType(TranslationTypeEnum type) {
		this.type = type;
	}

	public void setTranslation(String translation) {
		this.translation = translation;
	}

  @ManyToOne
  @JoinColumn(name="languageId") 
  public Language getLanguage() {
    return language;
  }

  public void setLanguage(Language language) {
    this.language = language;
  }

}