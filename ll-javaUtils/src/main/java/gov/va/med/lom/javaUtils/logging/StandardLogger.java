package gov.va.med.lom.javaUtils.logging;

import java.util.Hashtable;
import java.io.*;

/**
 * @author Rob Durkin
 * 
 * Standard implementation of the Logger.  This is 
 * general-purpose logging facility.  A client that needs additional
 * functionality can either extend this class or provide its own
 * implementationm of Logger. 
 * 
 * Currently this is a bare-bones class that writes INFO and above
 * levels to stderr and all others to a log file.
 */
public class StandardLogger extends Logger {
  
  /**
   * Table of level names to level numbers.  While level configuration
   * is local to a facility, a global table is kept assigning numbers
   * to each level name.
   */
  private Hashtable levelNumbers = new Hashtable();

  /**
   * Table translating level number to name and the largest entry in the
   * array that is valid.  Will be expanded if needed.
   */
  protected String[] levelNames = new String[MAX_STD_LEVEL*2];
  protected int numLevels = 0;

  /**
   * Table of levels that are to be enabled.
   * Accessed directly by the channel.  If null, ignored.
   */
  protected boolean[] enabledLevelFlags = null;

  /**
   * Table of levels that are to be written to the log file.
   * Accessed directly by the channel.  If null, ignored.
   */
  protected boolean[] logFileLevelFlags = null;

  /**
   * 
   * Table of levels that are to be written to stderr
   * Accessed directly by the channel.  If null, then
   * the default behavior of writing serious standard
   * levels to stderr is in affect.
   */
  protected boolean[] stderrLevelFlags = null;

  /**
   * Log file name.
   */ 
  File activeLogFile;

  /**
   * Log file writter.  Use directly by channels.
   */ 
  PrintWriter logFileStream;

  /**
   * Stderr writter.  Use directly by channels.
   */ 
  PrintWriter stderrStream;

  /**
   * Table of StandardLogChannel objects, indexed by facility name.
   */ 
  private Hashtable logChannels = new Hashtable();

  /**
   * Construct a new logger.  Configuration is not done now, to allow
   * the logger to be created very early. 
   */ 
  public StandardLogger() {
    this(false);
  }

  public StandardLogger(boolean makeCentral) {
    int level;
    for (level = 0; level <= MAX_STD_LEVEL; level++) {
      String name = standardLevelNames[level];
      levelNumbers.put(name, new Integer(level));
      levelNames[level] = name;
    }
    numLevels = level;
    if (makeCentral) {
      centralLogger = this;
    }
  }

  /**
   * Get maximum level number in a set of level names.
   */
  private int getMaxLevel(String[] levels) {
    int levelNum;
    int maxLevelNum = 0;
    for (int idx = 0; idx < levels.length; idx++) {
      levelNum = getLevel(levels[idx]);
      if (levelNum > maxLevelNum) {
        maxLevelNum = levelNum;
      }
    }
    return maxLevelNum;
  }

  /**
   * Generate a boolean array for all of the listed levels, indicating
   * if they are enabled.
   */
  private boolean[] getLevelStateArray(String[] levels, int maxLevelNum) {
    // Initialize the stated.
    boolean[] levelNums = new boolean[maxLevelNum+1];
    for (int idx = 0; idx < levels.length; idx++) {
      levelNums[getLevel(levels[idx])] = true;
    }
    return levelNums;
  }

  /**
   * Switch a log file; replacing the old one with a new one.
   */
  public synchronized File switchLogFile(File logFile) throws java.io.IOException {
    PrintWriter oldLogFileStream = logFileStream;
    File oldActiveLogFile = activeLogFile;

    // Append output stream without auto-flush (we do it explictly).
    FileOutputStream out = new FileOutputStream(logFile.getPath(), true);
    logFileStream = new PrintWriter(out, false);
    activeLogFile = logFile;

    // Close old, if it exists.  Waiting for any accessors.
    if (oldLogFileStream != null) {
      synchronized (oldLogFileStream) {
        oldLogFileStream.close();
      }
    }
    return oldActiveLogFile;
  }

  /**
   * Configure the logger. All current configuration is discarded.
   * This is a simplistic initial implementation that just allows
   * directing to a single log file or stderr on a level basis.
   */
  public synchronized void configure(File logFile,int[] fileLevels,int[] strerrLevels) throws java.io.IOException {
    String[] fileLevelsStr = new String[fileLevels.length];
    String[] stderrLevelsStr = new String[strerrLevels.length];
    for(int i = 0; i < fileLevels.length; i++)
      fileLevelsStr[i] = standardLevelNames[fileLevels[i]];
    for(int i = 0; i < strerrLevels.length; i++)
      stderrLevelsStr[i] = standardLevelNames[strerrLevels[i]];  
    configure(logFile, fileLevelsStr, stderrLevelsStr);
  }
  
  public synchronized void configure(File logFile,String[] fileLevels,String[] stderrLevels) throws java.io.IOException {
    // Ensure that the directory exists.
    if (logFile.getParent() != null) {
      new File(logFile.getParent()).mkdirs();
    }
    // Output streams without auto-flush (we do it explictly).
    switchLogFile(logFile);
    stderrStream = new PrintWriter(System.err, false);
    /*
     * Tables must be created after streams, as they are 
     * the checked before accessing the stream.  Care is taken
     * that all three arrays that are indexed by level are of
     * the same size and never shrink on reconfigure.  This 
     * mean no synchronization is required to access them.  Also
     * enabled table must be done last.
     */
    int maxLevelNum;
    int levelNum;
    if (enabledLevelFlags != null) {
      maxLevelNum = enabledLevelFlags.length - 1;
    } else {
      maxLevelNum = MAX_STD_LEVEL;
    }
    levelNum = getMaxLevel(fileLevels);
    if (levelNum > maxLevelNum) {
      maxLevelNum = levelNum;
    }
    levelNum = getMaxLevel(stderrLevels);
    if (levelNum > maxLevelNum) {
      maxLevelNum = levelNum;
    }
      
    // Build boolean tables.
    logFileLevelFlags = getLevelStateArray(fileLevels, maxLevelNum);
    stderrLevelFlags = getLevelStateArray(stderrLevels, maxLevelNum);
    enabledLevelFlags = new boolean[maxLevelNum+1];
    for (int idx = 0; idx < logFileLevelFlags.length; idx++) {
      if (logFileLevelFlags[idx]) {
        enabledLevelFlags[idx] = true;
      }
    }
    for (int idx = 0; idx < stderrLevelFlags.length; idx++) {
      if (stderrLevelFlags[idx]) {
        enabledLevelFlags[idx] = true;
      }
    }
  }

  /**
   * Create a log channel.
   */
  private synchronized StandardLogChannel createChannel(String facility) {
    StandardLogChannel channel = (StandardLogChannel)logChannels.get(facility);
    if (channel == null) {
      channel = new StandardLogChannel(facility, this);
      logChannels.put(facility, channel);
    }
    return channel;
  }

  /**
   * Get the log channel object for a facility.  For a given facility,
   * the same object is always returned.
   */
  public LogChannel getChannel(String facility) {
    StandardLogChannel channel = (StandardLogChannel)logChannels.get(facility);
    if (channel == null) {
      // Slow path, synchronized
      channel = createChannel(facility);
    }
    return channel;
  }

  /**
   * Create a log level.
   */
  private synchronized Integer createLevel(String level) {
    Integer intLevel = (Integer)levelNumbers.get(level);
    if (intLevel == null) {
      intLevel = new Integer(numLevels);
      levelNames[numLevels] = level;
      levelNumbers.put(level, intLevel);
      numLevels++;
    }
    return intLevel;
  }

  /**
   * Convert a symbolic level to an integer identifier,
   * creating it if it doesn't exist
   */
  public synchronized int getLevel(String level) {
    Integer intLevel = (Integer)levelNumbers.get(level);
    if (intLevel == null) {
      // Slow path, synchronized
      intLevel = createLevel(level);
    }
    return intLevel.intValue();
  }

  /**
   * Convert an int to a symbolic level name.
   */
  public String getLogLevelName(int level) {
	  if ((level >= 0) && (level < numLevels)) {
	    return levelNames[level];
    } else {
	    return null;
    }
  }
}

