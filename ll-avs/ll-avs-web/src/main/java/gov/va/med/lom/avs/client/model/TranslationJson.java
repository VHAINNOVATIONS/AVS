package gov.va.med.lom.avs.client.model;

import gov.va.med.lom.avs.model.Translation;

public class TranslationJson {

	private Long id;
	private Integer type;
	private String source;
	private String translation;

	protected TranslationJson() {}

	public TranslationJson(Translation translation) {
		this.id = translation.getId();
		this.type = translation.getType().getId();
		this.source = translation.getSource();
		this.translation = translation.getTranslation();
	}

	public Long getId() {
		return this.id;
	}

	public Integer getType() {
		return this.type;
	}

	public String getSource() {
		return this.source;	
	}

	public String getTranslation() {
		return this.translation;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public void setSource(String source) {
		this.source = source;
	}
	
	public void setTranslation(String translation) {
		this.translation = translation;
	}

}
