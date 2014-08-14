package gov.va.med.lom.avs.util;

import gov.va.med.lom.avs.enumeration.SortEnum;

public class FilterProperty {

	private SortEnum property;
	private String value;
	
	public SortEnum getProperty() {
		return this.property;
	}
	
	public String getValue() {
		return this.value;
	}
	
	public void setProperty(SortEnum property) {
		this.property = property;
	}
	
	public void setValue(String value) {
		this.value = value;
	}

}
