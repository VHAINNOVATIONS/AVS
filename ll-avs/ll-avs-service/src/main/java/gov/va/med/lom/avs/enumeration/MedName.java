package gov.va.med.lom.avs.enumeration;

public enum MedName {

	// Statins
	SIMVASTATIN("Simvastatin", MedClass.STATIN, new String[] {"Statins"}),
	ROSUVASTATIN("Rosuvastatin", MedClass.STATIN, new String[] {"Statins"}),
	ZETIA("Zetia", MedClass.STATIN, new String[] {"Statins", "Ezetimibe"}),
	
	// Anti-platelets
	ASA("Aspirin", MedClass.ANTIPLATELET, null),
	CLOPIDOGREL("Clopidogrel", MedClass.ANTIPLATELET, null),

	// BP
	CHLORTHALIDONE("Chlorthalidone", MedClass.BLOODPRESSURE, null), // thiazide diuretic
	BENAZEPRIL("Benazepril", MedClass.BLOODPRESSURE, null), // ACE inhibitor 
	LOSARTAN("Losartan", MedClass.BLOODPRESSURE, null), // ARB
	
	// Glucose
	INSULIN("Insulin", MedClass.GLUCOSE, null),
	GLIPIZIDE("Glipizide", MedClass.GLUCOSE, null),
	METFORMIN("Metformin", MedClass.GLUCOSE, null),
	PIOGLITAZONE("Pioglitazone", MedClass.GLUCOSE, null);
	
	/* Accessors */
	
	public String getName() {
		return this.name;
	}

	public MedClass getMedClass() {
		return this.medClass;
	}

	public String[] getAliases() {
		return this.aliases;
	}

	/* Mutators */
	
	public void setName(String value) {
		this.name = value;
	}

	public void setMedClass(MedClass medClass) {
		this.medClass = medClass;
	}

	public void setAliases(String[] value) {
		this.aliases = value;
	}

	/* "Constructor" */
	
	private MedName(String name, MedClass medClass, String[] aliases) {
		this.name = name;
		this.medClass = medClass;
		this.aliases = aliases;
	}

	private String name;
	private String[] aliases;
	private MedClass medClass;
}

