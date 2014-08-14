package gov.va.med.lom.foundation.validate;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ZipCodeValidator extends SinglePropertyValidator {
	
	private static final Pattern[] PATTERNS;
	
	static {
		PATTERNS = new Pattern[] {Pattern.compile("^[0-9][0-9][0-9][0-9][0-9]$"),
				                  Pattern.compile("^[0-9][0-9][0-9][0-9][0-9]-[0-9][0-9][0-9][0-9]$"),
				                  Pattern.compile("^[0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9]$")};
	}
	protected boolean isValid(Object value) {
		String ssn = (String) value;
		for (int i=0; i<PATTERNS.length; i++) {
			Matcher m = PATTERNS[i].matcher(ssn);
			if (m.matches()) {
				return true;
			}
		}
		return false;
	}

	public static boolean validate(Object value) {
		String ssn = (String) value;
		for (int i=0; i<PATTERNS.length; i++) {
			Matcher m = PATTERNS[i].matcher(ssn);
			if (m.matches()) {
				return true;
			}
		}
		return false;
	}
	
	protected String getMessageKey() {
		return "invalid.zip.code";
	}
	
	public static String getErrorKey() {
		return "invalid.zip.code";
	}

}
