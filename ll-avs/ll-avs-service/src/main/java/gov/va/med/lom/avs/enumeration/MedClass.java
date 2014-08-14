package gov.va.med.lom.avs.enumeration;

public enum MedClass {

	STATIN("Statins"),
	ANTIPLATELET("Antiplatelets"),
	BLOODPRESSURE("Blood Pressure"),
	GLUCOSE("Glucose");
	
	/* Accessors */
	
	public String getName() {
		return this.name;
	}

	/* Mutators */
	
	public void setName(String value) {
		this.name = value;
	}

	/* "Constructor" */
	
	private MedClass(String name) {
		this.name = name;
	}

	private String name;
}

