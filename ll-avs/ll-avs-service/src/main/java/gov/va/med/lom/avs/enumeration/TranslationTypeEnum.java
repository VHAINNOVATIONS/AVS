package gov.va.med.lom.avs.enumeration;

public enum TranslationTypeEnum {

	UNDEFINED(0),
	LOCATION_NAME(1),
	ORDER_TEXT(2);

	private Integer id;

	private TranslationTypeEnum(Integer id) {
		this.id = id;
	}

	public Integer getId() {
		return this.id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}


}

