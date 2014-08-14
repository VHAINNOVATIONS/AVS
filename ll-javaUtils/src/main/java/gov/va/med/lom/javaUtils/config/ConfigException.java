package gov.va.med.lom.javaUtils.config;

/*
 * Exception class thrown by class Config.  If a syntax
 * error is found in the configuration input stream, or if a component
 * expects a data format for which a configuration element cannot be
 * converted, then this exception is thrown to indicate the error.
 */

public class ConfigException extends KeywordValueException {
  public static final int 	UNKNOWN = 0;
  public static final int 	NOT_FOUND = 1;
  public static final int 	SYNTAX = 2;
  public static final int 	COUNT = 3;
  public static final int 	FORMAT = 4;
  private static final int 	MAX_REASON = 4;
  public int reason;

  public ConfigException() {
    super("Unknown reason");
    reason = UNKNOWN;
  }
  
  public ConfigException(String s) {
	  super(s);
	  reason = UNKNOWN;
  }
    
  public ConfigException(int r, String s) {
    super(s);
    if ((r < 0) || (r > MAX_REASON)) 
      reason = UNKNOWN;
	  else 
      reason = r;
  }
}
