package gov.va.med.lom.javaUtils.exception;

import java.io.*;

/*
 * Interfaces for all of the Chained* throwables. This defines
 * the common methods that all implement.
 */
public interface ChainedThrowable {
   // Return the message associated with this exception.  If causes
   // are included, they will be appended to the message.
  public String getMessage();

  // Get the causing exception associated with this exception.
  public Throwable getCause();

  // Prints this ChainedException and its backtrace, and the causes
  // and their stack traces to the standard error stream. 
  public void printStackTrace();

  // Prints this ChainedException and its backtrace, and the causes
  // and their stack traces to the e specified print stream. 
  public void printStackTrace(PrintStream s);

  // Prints this ChainedException and its backtrace, and the causes
  // and their stack traces to the e specified print writer. 
  public void printStackTrace(PrintWriter s);
}
