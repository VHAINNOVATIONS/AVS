package gov.va.med.lom.javaBroker.log;


/**
 * @author Rob Durkin
 * 
 * A general-purpose logging facility. This is a base class from which an actual
 * implementation is derived.  It only defines how log message are written
 * by a client.  Where the log message is written and the mechanism for
 * controlling logging is left up to the implementation.  This class does
 * not define any of these mechanism and their definition is not necessary
 * for understand how to use this class.
 *
 * Each log message is associate with a facility and has a level assigned
 * to it.  A facility is a symbolic (String) name that defines a class of
 * log messages.  The levels are similar to the UNIX syslog functionality. 
 *
 * It is expected that the implementation can enable, disable and
 * direct log messages based on these attributes.  Facilities and levels
 * are defined symbolicly, with no restriction on the name, and form a tuple.
 * Several standard levels are defined as integer constants and their use
 * is expected to be higher performing than symbolic levels..
 *
 * Normally, a single,global instance of the object
 * exists and is obtainable by a static method in this class.
 *
 * Log messages are written via an object implementing LogChannel.
 * A channel is associated with a single facility, with the level being
 * specified when a message is written.  Normally, a LogChannel
 * is obtained once at initialization time and use repeatedly.  It is
 * permissible to obtain multiple references to the log channel for a facility,
 * but this is discouraged for performance reasons.
 *
 * Log messages, even debugging ones, should be defined with care.  They
 * should be terse, but clear to someone who isn't intimately familiar with
 * the code.  Permanent debugging messages should be designed with the idea
 * of use when supporting a deployed product.
 *
 * The central logging object needs to be configured very early in the startup
 * process.  If logging can't be configured, then the startup should be aborted
 * or an object created that does some simple form of logging, such as write
 * to stderr.  A client should never have to check if the global logger object exists.
 */

public abstract class Logger {

  /**
   * Config file keys
   */ 
  public static final String LOG_FILE = "LogFile";
  public static final String LOG_TO_FILE = "LogToFile";
  public static final String LOG_TO_STDERR = "LogToStderr";
  
   /**
    * Standard level for urgent condition that requires immediate attention
    * and indicates that the system is no longer functioning. 
    */ 
  public static final int EMERGENCY = 0;
  
  /**
   * A condition that should be corrected immediately
   */
  public static final int ALERT = 1;

  /**
   * Critical conditions.
   */
  public static final int CRITICAL = 2;

  /**
   * Errors that have been correctly handled.
   */
  public static final int ERROR = 3;
  
  /**
   * Warning messages.
   */
  public static final int WARNING = 4;

  /**
   * Conditions that are not error conditions, but should possibly be handled specially.
   */
  public static final int NOTICE = 5;

  /**
   * Informational messages.
   */
  public static final int INFO = 6;

  /**
   * Debugging messages.
   */
  public static final int DEBUG = 7;
  
  /**
   * Local use.
   */
  public static final int LOCAL = 8;  

  /**
   * Largest fixed logging level.
   */
  public static final int MAX_STD_LEVEL = DEBUG;


  /**
   * Global Logger object.
   */
  protected static Logger centralLogger;

  /**
   * Table of standard level names
   */
  protected static final String[] standardLevelNames = {
    "EMERGENCY", //  0
    "ALERT",     //  1
    "CRITICAL",  //  2
    "ERROR",     //  3
    "WARNING",   //  4
    "NOTICE",    //  5
    "INFO",      //  6
    "DEBUG",     //  7
    "LOCAL"      //  8
  };

  /**
   * Get the central (global) logging object.
   */
  public static Logger getCentralLogger() {
    return centralLogger;
  }
  
  /**
   * Returns the symbolic name of the logging level.
   */
  public static String getLevelName(int level) {
    if ((level >=0) && (level < standardLevelNames.length))
      return standardLevelNames[level];
    else
      return "";
  }

  /**
   * Get the log channel object for a facility.  For a given facility,
   * the same object is always returned.
   */
  abstract public LogChannel getChannel(String facility);

}
