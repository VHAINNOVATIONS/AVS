package gov.va.med.lom.javaUtils.exception;

import java.io.StringWriter;
import java.io.PrintWriter;

/*
 * A collection of static methods that deal with Exceptions.
 *
 */
public class ExceptionUtils {
  private ExceptionUtils() {
    // can't be instantiated
  }

  public static String describeException(Throwable e) {
    StringWriter sw = new StringWriter();
    PrintWriter pw = new PrintWriter(sw);
    pw.println(e);
    e.printStackTrace(pw);
    return sw.toString();
  }
}

