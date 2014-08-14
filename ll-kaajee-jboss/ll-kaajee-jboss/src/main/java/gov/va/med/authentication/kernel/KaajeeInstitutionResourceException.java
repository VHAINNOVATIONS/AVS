package gov.va.med.authentication.kernel;

/**
 * Thrown if there's an error using the SDS Institution that points to a fundamental problem accessing the
 * SDS API/table resource. Used internally by KAAJEE.
 * @author VHIT Security and Other Common Services (S&OCS)
 * @version 1.1.0.007
 */
public class KaajeeInstitutionResourceException extends KaajeeException {

	/**
	 * Constructor forRoleCacheException.
	 * @param msg error message
	 */
	public KaajeeInstitutionResourceException(String msg) {
		super(msg);
	}

	/**
	 * Constructor for KaajeeException.
	 * @param nestedException nested exception
	 */
	public KaajeeInstitutionResourceException(Exception nestedException) {
		super(nestedException);
	}

	/**
	 * Constructor for KaajeeException.
	 * @param msg error message
	 * @param nestedException nested exception
	 */
	public KaajeeInstitutionResourceException(String msg, Exception nestedException) {
		super(msg, nestedException);
	}

}
