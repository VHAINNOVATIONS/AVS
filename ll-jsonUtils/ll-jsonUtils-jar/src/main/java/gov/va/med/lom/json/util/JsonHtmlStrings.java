package gov.va.med.lom.json.util;

import java.util.ResourceBundle;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class JsonHtmlStrings {

	private static final Log log = LogFactory.getLog(JsonHtmlStrings.class);
	private final static String JSON_HTML_STRINGS = "json-html";
	
	private static ResourceBundle res = ResourceBundle.getBundle(JSON_HTML_STRINGS);
	
	public static final String JSON_RESPONSE_BASE_MULTIPLE = "json.response.base.multiple";
	public static final String JSON_RESPONSE_BASE_SINGLE = "json.response.base.single";
	public static final String JSON_RESPONSE_ERROR = "json.response.error";
	public static final String JSON_RESPONSE_NOT_ALLOWED = "json.response.not.allowed";
	public static final String JSON_RESPONSE_SUCCESS = "json.response.success";
	public static final String JSON_RESPONSE_SUCCESS_MSG = "json.response.success.msg";
	public static final String JSON_RESPONSE_UNAUTHORIZED = "json.response.unauthorized";
	public static final String JSON_RESPONSE_ALIAS = "json.response.alias";
		
	public static ResourceBundle strings(){
		return res;
	}
	
	public static String get(String key){
		if(res.getString(key) == null){
			log.warn("no key found in strings table:  " + key);
			return "";
		}
		return res.getString(key);
	}
}
