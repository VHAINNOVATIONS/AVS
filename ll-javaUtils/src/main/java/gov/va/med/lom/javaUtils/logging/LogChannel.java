package gov.va.med.lom.javaUtils.logging;

/**
 * @author Rob Durkin
 * 
 * Interface of a channel associated with a logging facility.  All messages
 * for the facility are written using a channel. 
 */
public interface LogChannel {
    
   /**
    * Determine if logging is enabled for the specified level.  This
    * is useful to prevent a series of unnecessary logging calls,
    * as often encountered with debug logging, or a call where generating
    * the message is expensive.
    */
  boolean isEnabled(int level);

  /**
   * 
   * Determine if logging is enabled for the specified level.  This
   * is useful to prevent a series of unnecessary logging calls,
   * as often encountered with debug logging, or a call where generating
   * the message is expensive.
   */
  boolean isEnabled(String level);

  /**
   * Convert a symbolic level to an integer identifier.
   */
  int getLevel(String level);

  String getFacility();

  /**
   * Create a LogWrite associated with a particular level.  This
   * is often an easier object to use than a LogChannel if limited
   * levels are needed.
   */
  LogWriter getLogWriter(String level);

  /**
   * Create a LogWrite associated with a particular level.  This
   * is often an easier object to use than a LogChannel if limited
   * levels are needed.
   */
  LogWriter getLogWriter(int level);

  /**
   * Write a string to the log file.
   */
  void write(int level, String msg);
  
  /**
   * Write a string to the log file.
   */
  void write(String level, String msg);
  
  /**
   * Write a string and exception to the log file.
   */
  void write(int level, String msg, Throwable throwable);
  
  /**
   * Write a string and exception to the log file.
   */
  void write(String level, String msg, Throwable throwable);
}
