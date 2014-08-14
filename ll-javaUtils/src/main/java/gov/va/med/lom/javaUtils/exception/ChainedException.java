package gov.va.med.lom.javaUtils.exception;

import java.io.*;

/*
 * Exception used as a base for creating an exception that has a chain of
 * exceptions that lead to the derived exception.  Very useful for interfaces
 * where the implementation exception is not known.
 */
public class ChainedException extends Exception implements ChainedThrowable {
  private Throwable cause;

  // Construct an exception without a specified cause.
  public ChainedException(String msg) {
    super(msg);
    cause = null;
  }

  // Construct an exception with an associated causing exception.
  public ChainedException(String msg,Throwable cause) {
    super(msg);
    this.cause = cause;
  }

  // Construct an exception from a causing exception.
  public ChainedException(Throwable cause) {
    super(ChainedThrowableUtil.makeMessage(cause));
    this.cause = cause;
  }

  // Return the message associated with this exception.  If causes
  // are included, they will be appended to the message.
  public String getMessage() {
    return ChainedThrowableUtil.getMessage(this, super.getMessage());
  }

  // Get the causing exception associated with this exception.
  public Throwable getCause() {
    return cause;
  }

  // Prints this ChainedException and its backtrace, and the causes
  // and their stack traces to the standard error stream. 
  public void printStackTrace() {
    super.printStackTrace();
    ChainedThrowableUtil.printCauseTrace(this);
  }

  // Prints this ChainedException and its backtrace, and the causes
  // and their stack traces to the e specified print stream. 
  public void printStackTrace(PrintStream s) {
    super.printStackTrace(s);
    ChainedThrowableUtil.printCauseTrace(this, s);
  }

  // Prints this ChainedException and its backtrace, and the causes
  // and their stack traces to the e specified print writer. 
  public void printStackTrace(PrintWriter s) {
    super.printStackTrace(s);
    ChainedThrowableUtil.printCauseTrace(this, s);
  }
}
