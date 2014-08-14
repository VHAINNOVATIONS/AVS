package gov.va.med.lom.foundation.validate;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

public class SsnValidator extends SinglePropertyValidator {

	private static final Pattern[] PATTERNS;
	
	static {
		PATTERNS = new Pattern[] {Pattern.compile("^[0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9]$"),
				                  Pattern.compile("^[0-9][0-9][0-9]-[0-9][0-9]-[0-9][0-9][0-9][0-9]$")};
	}
	
	protected boolean isValid(Object value) {
		String ssn = null;
		try {
			ssn = (String) value;
		} catch (ClassCastException cce) {
			return false;
		}
		
		if (StringUtils.isNotBlank(ssn)) {
			for (int i=0; i<PATTERNS.length; i++) {
				Matcher m = PATTERNS[i].matcher(ssn);
				if (m.matches()) {
					return true;
				}
			}
		}
		return false;
	}

	protected String getMessageKey() {
		return "invalid.ssn";
	}
	

}
