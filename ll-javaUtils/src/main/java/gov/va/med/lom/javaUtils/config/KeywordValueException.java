package gov.va.med.lom.javaUtils.config;

/*
 * Exception for errors accessing a KeywordValueTable object.  The errors can
 * include invalid keyword syntax and unknown keywords.
 */

public class KeywordValueException extends Exception {
  public KeywordValueException (String msg) {
    super (msg);
  }
}
