package gov.va.med.lom.javaUtils.exception;

import java.io.*;

/*
 * RuntimeException used as a base for creating an exception that has a chain
 * of exceptions that lead to the derived exception.  Very useful for
 * interfaces where the implementation exception is not known.
 */
public class ChainedRuntimeException extends RuntimeException implements ChainedThrowable {
  private Throwable cause;

  // Construct an exception without a specified cause.
  public ChainedRuntimeException(String msg) {
    super(msg);
    cause = null;
  }

  // Construct an exception with an associated causing exception.
  public ChainedRuntimeException(String msg,Throwable cause) {
    super(msg);
    this.cause = cause;
  }

  // Construct an exception from a causing exception.
  public ChainedRuntimeException(Throwable cause) {
    super(ChainedThrowableUtil.makeMessage(cause));
    this.cause = cause;
  }

  // Return the message associated with this exception.  If causes
  // are included, they will be appended to the message.
  public String getMessage() {
    return ChainedThrowableUtil.getMessage(this, super.getMessage());
  }

  // Get the causing exception associated with this exception.
  // @return The causing exception or null if no cause is specified.
  public Throwable getCause() {
    return cause;
  }

  // Prints this ChainedRuntimeException and its backtrace, and the causes
  // and their stack traces to the standard error stream. 
  public void printStackTrace() {
    super.printStackTrace();
    ChainedThrowableUtil.printCauseTrace(this);
  }

  // Prints this ChainedRuntimeException and its backtrace, and the causes
  // and their stack traces to the e specified print stream. 
  public void printStackTrace(PrintStream s) {
    super.printStackTrace(s);
    ChainedThrowableUtil.printCauseTrace(this, s);
  }

  // Prints this ChainedRuntimeException and its backtrace, and the causes
  // and their stack traces to the e specified print writer. 
  public void printStackTrace(PrintWriter s) {
    super.printStackTrace(s);
    ChainedThrowableUtil.printCauseTrace(this, s);
  }
}
