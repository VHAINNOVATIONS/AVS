package gov.va.med.lom.foundation.service.response.messages;


/**
 * Enumeration class for message severities.  There is a severity enumerated
 * value available for each possible severity that a message could have.
 * Severity instances may be obtained using the static <code>getSeverity</code>
 * method, passing in one of the constant strings provided by this class.
 */
public enum Severity {

	ERROR(1L, "Error"),
    WARNING(2L,"Warning"),
	INFORMATIONAL(3L,"Informational");
	
	private Long id;
    private String name;

    private Severity(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    
    public static Severity valueOf(Long value) {
        for (Severity f : Severity.values()) {
            if (f.getId().equals(value))
                return f;
        }
        /* none found; throw error */
        throw new RuntimeException("no enum found");
    }
}
