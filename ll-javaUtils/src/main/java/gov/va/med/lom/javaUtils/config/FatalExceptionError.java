package gov.va.med.lom.javaUtils.config;

/*
 * Class that encapsulates exceptions as runtime errors.  This is used when an
 * an exception is caught that really should be treated as a runtime error.
 * This should only be used for exceptions that really should be treated as
 * fatal errors; not as a way of by-passing Java error handling.
 */
public class FatalExceptionError extends Error {
  Exception except;

  public FatalExceptionError (Exception except) {
    this.except = except;
  }

  public Exception getException () {
    return except;
  }

  public String getMessage() {
	  return except.getMessage();
  }

  public String getLocalizedMessage() {
  	return except.getLocalizedMessage();
  }

  public String toString() {
  	String str = getClass().getName() + ": " +
                 except.getClass().getName();
	  String message = except.getMessage();

    if (message == null) 
      return str;
    return str + ": " + message;
  }

  public void printStackTrace() { 
    except.printStackTrace();
  }

  public void printStackTrace(java.io.PrintStream str) { 
    except.printStackTrace(str);
  }

  public void printStackTrace(java.io.PrintWriter pw) {
  	except.printStackTrace(pw);
  }
}
