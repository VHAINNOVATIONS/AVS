package gov.va.med.lom.avs.enumeration;

public enum DGroupSeq {

  CONSULT("Consultations"),
  IMAGING("Imaging"),
  LAB("Lab Tests"),
  MED("Medications"), // Outpatient Meds
	OTHER("Other Orders");

	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	private DGroupSeq(String name) {
		this.name = name;
	}

	private String name;
}

