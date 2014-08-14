package gov.va.med.authentication.kernel;

import gov.va.med.exception.FoundationsException;

/**
 * General exception for KAAJEE internal use
 * @author VHIT Security and Other Common Services (S&OCS)
 * @version 1.1.0.007
 */
public class KaajeeException extends FoundationsException {

	/**
	 * Constructor for KaajeeException.
	 */
	public KaajeeException() {
		super();
	}

	/**
	 * Constructor forRoleCacheException.
	 * @param msg error message
	 */
	public KaajeeException(String msg) {
		super(msg);
	}

	/**
	 * Constructor for KaajeeException.
	 * @param nestedException nested exception
	 */
	public KaajeeException(Exception nestedException) {
		super(nestedException);
	}

	/**
	 * Constructor for KaajeeException.
	 * @param msg error message
	 * @param nestedException nested exception
	 */
	public KaajeeException(String msg, Exception nestedException) {
		super(msg, nestedException);
	}

}
