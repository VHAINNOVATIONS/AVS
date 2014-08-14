package gov.va.med.lom.javaUtils.logging;

import java.text.*;
import java.util.Date;
import java.io.*;

/**
 * @author Rob Durkin
 * 
 * Standard implementation of a channel associated with a logging
 * facility.  All messages for the facility are written using a channel.
 * Care is take to avoid synchronization when possible for performance
 * reasons.
 */
public class StandardLogChannel implements LogChannel {
   /**
    * Symbolic name.
    */ 
  private String facility;
  
  /**
   * Format for the date.
   */
  private static final SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");

  /**
   * Logger object with which we are associated.
   */ 
  private StandardLogger logger;

  /**
   * Construct a new log channel.
   */
  protected StandardLogChannel(String chanFacility, StandardLogger loggerObj) {
    facility = chanFacility;
    logger = loggerObj;
  }

  public int getLevel(String level) {
    return logger.getLevel(level);
  }

  public String getFacility() {
    return facility;
  }

  public LogWriter getLogWriter(String level) {
    return new LogWriter(this, level);
  }

  public LogWriter getLogWriter(int level) {
    return new LogWriter(this, level);
  }

  public boolean isEnabled(int level) {
    // Copy to avoid needing to be synchronized.
    boolean[] enabledLevelFlags = logger.enabledLevelFlags;
    return (enabledLevelFlags != null) && (level >= 0 && 
           level < enabledLevelFlags.length) && 
           enabledLevelFlags[level];
  }

  public boolean isEnabled(String level) {
    return isEnabled(logger.getLevel(level));
  }

  /**
   * Do the work of writting a log message.  It should already be
   * determined if the channel is enabled; but limited synchronization
   * is required, as we ignore any error while writing.  Handles
   * multiline messages.
   */
  private void doWrite(PrintWriter out, Date date, int level, String msg) {
    try {
      // Create prefix for each line.
      StringBuffer msgBuf = new StringBuffer();
      dateFormatter.format(date, msgBuf,new FieldPosition(DateFormat.YEAR_FIELD));
      msgBuf.append(": ");
      msgBuf.append(facility);
      msgBuf.append(',');
      msgBuf.append(logger.levelNames[level]);
      // Write as individual lines with the same prefix.
      int nextIdx = 0;
      int newlineIdx;
      synchronized (out) {
        if (msg != null) {
          while (nextIdx < msg.length()) {
            out.print(msgBuf);
            if (nextIdx == 0) {
              out.print(": ");
            } else {
              out.print("+ ");
            }
            newlineIdx = msg.indexOf('\n', nextIdx);
            if (newlineIdx < 0) {
              newlineIdx = msg.length();
            }
            out.write(msg, nextIdx, (newlineIdx - nextIdx));
            out.println();
            nextIdx = newlineIdx + 1;
          }
          out.flush();
        }
      }
    } catch (Throwable ignore) {
      System.err.println("StandardLogChannel.doWrite ignored exception:");
      ignore.printStackTrace();
    }
  }

  public void write(int level, String msg) {
    if (isEnabled(level)) {
      Date date = new Date();
      if (logger.logFileLevelFlags[level]) {
        doWrite(logger.logFileStream, date, level, msg);
      }
      if (logger.stderrLevelFlags[level]) {
        doWrite(logger.stderrStream, date, level, msg);
      }
    }
  }

  public synchronized void write(String level, String msg) {
    write(getLevel(level), msg);
  }

  public synchronized void write(int level, String msg, Throwable throwable) {
    if (isEnabled(level)) {
      Date date = new Date();
      StringWriter stackBuf = new StringWriter();
      throwable.printStackTrace(new PrintWriter(stackBuf));
      stackBuf.flush();

      String errMsg = msg + ":" + " " + throwable.getMessage() + '\n' + stackBuf;

      if (logger.logFileLevelFlags[level]) {
        doWrite(logger.logFileStream, date, level, errMsg);
      }
      if (logger.stderrLevelFlags[level]) {
        doWrite(logger.stderrStream, date, level, errMsg);
      }
    }
  }

  public synchronized void write(String level, String msg, Throwable throwable) {
    write(getLevel(level), msg, throwable);
  }
}
