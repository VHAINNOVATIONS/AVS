package gov.va.med.lom.javaUtils.exception;

import java.io.*;

/*
 * Utilities used to implement the Chained* throwables.
 */
class ChainedThrowableUtil {

  private ChainedThrowableUtil() {
    // can't make instances
  }

  // Generate the message to set for the exception message.
  // When not explicit message is supplied.
  protected static String makeMessage(Throwable cause) {
    String causeMsg = cause.getMessage();
    if (causeMsg == null) {
      return cause.getClass().getName();
    } else {
      return causeMsg;
    }
  }
  

  // Get the message associated with this exception.
  protected static String getMessage(ChainedThrowable except,String superMsg) {
    Throwable cause = except.getCause();
    if (cause == null) {
      return superMsg;
    } else {
      String causeMsg = cause.getMessage();
      if ((causeMsg == null) || (causeMsg.length() == 0)) {
        causeMsg = cause.getClass().getName();
      }
      return superMsg + ": " + causeMsg;
    }
  }


  // Do work of printing the causes of a chained exception.  This is
  // recursive. This attempts to following causing exceptions that are
  // chained in other types of exception objects.  If only Sun would
  // standardize a mechanism in throwable instead of letting it accumulate
  // in an ad-hoc manner.
  private static void printChainedCauses(Throwable cause,PrintWriter out) {
    if (cause != null) {
      out.println("*** Caused by:");
      cause.printStackTrace(out);
      if (cause instanceof ChainedThrowable) {
        // If cause was is a ChainedThrowable, its chain has already
        // been followed, otherwise, see what we can do.
      } else if (cause instanceof java.awt.print.PrinterIOException) {
        printChainedCauses(((java.awt.print.PrinterIOException)cause).getIOException(), out);
      } else if (cause instanceof java.io.WriteAbortedException) {
        printChainedCauses(((java.io.WriteAbortedException)cause).detail, out);
      } else if (cause instanceof java.lang.ClassNotFoundException) {
        printChainedCauses(((java.lang.ClassNotFoundException)cause).getException(), out);
      } else if (cause instanceof java.lang.ExceptionInInitializerError) {
        printChainedCauses(((java.lang.ExceptionInInitializerError)cause).getException(), out);
      } else if (cause instanceof java.lang.reflect.InvocationTargetException) {
        printChainedCauses(((java.lang.reflect.InvocationTargetException)cause).getTargetException(), out);
      } else if (cause instanceof java.rmi.RemoteException) {
        printChainedCauses(((java.rmi.RemoteException)cause).detail, out);
      } else if (cause instanceof java.rmi.activation.ActivationException) {
        printChainedCauses(((java.rmi.activation.ActivationException)cause).detail, out);
      } else if (cause instanceof java.rmi.server.ServerCloneException) {
        printChainedCauses(((java.rmi.server.ServerCloneException)cause).detail, out);
      } else if (cause instanceof java.security.PrivilegedActionException) {
        printChainedCauses(((java.security.PrivilegedActionException)cause).getException(), out);
      } else if (cause instanceof java.sql.SQLException) {
        printChainedCauses(((java.sql.SQLException)cause).getNextException(), out);
      } else if (cause instanceof org.xml.sax.SAXException) {
        printChainedCauses(((org.xml.sax.SAXException)cause).getException(), out);
      }
    }
  }

  // Prints stacktrace and cause stacktrace.
  protected static void printCauseTrace(ChainedThrowable except) {
    PrintWriter pw = new PrintWriter(System.err);
    printChainedCauses(except.getCause(), pw);
    pw.flush();
  }

  // Prints stacktrace and cause stacktrace.
  protected static void printCauseTrace(ChainedThrowable except,PrintStream s) {
    PrintWriter pw = new PrintWriter(s);
    printChainedCauses(except.getCause(), pw);
    pw.flush();
  }

  // Prints stacktrace and cause stacktrace.
  public static void printCauseTrace(ChainedThrowable except,PrintWriter out) {
    printChainedCauses(except.getCause(), out);
    out.flush();
  }
}
