package gov.va.med.lom.avs.client.model;

import gov.va.med.lom.vistabroker.patient.data.DiscreteItemData;

public class LabValueJson {

	private String labType;
	private Double fmDate;
	private String value;

	protected LabValueJson() {}

	public LabValueJson(DiscreteItemData point, String labType) {
		this.labType = labType;
		this.fmDate = point.getFmDate();
		this.value = point.getValue();
	}

	public String getLabType() {
		return this.labType;
	}

	public Double getFmDate() {
		return this.fmDate;
	}

	public String getValue() {
		return this.value;
	}

	public void setLabType(String labType) {
		this.labType = labType;
	}

	public void setFmDate(Double fmDate) {
		this.fmDate = fmDate;
	}

	public void setValue(String value) {
		this.value = value;
	}

}
