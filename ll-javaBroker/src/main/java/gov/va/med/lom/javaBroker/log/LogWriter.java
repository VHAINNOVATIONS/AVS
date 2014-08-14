package gov.va.med.lom.javaBroker.log;


import java.io.PrintWriter;

/**
 * @author Rob Durkin
 * 
 * Class use to write log output to a particular LogChannel and level.
 * This class is PrintWriter, with println() causing a write.
 * One should use println() with this rather than write, as with a
 * LogChannel, since write doesn't write a full line.
 */
public class LogWriter extends PrintWriter {
  /*
   * Log channel and associated level.
   */
  private LogChannel channel;
  private int level;
  private boolean enabled;

  /*
   * Constructors
   */
  protected LogWriter(LogChannel logChannel,String logLevel) {
    this(logChannel, logChannel.getLevel(logLevel));
  }

  protected LogWriter(LogChannel logChannel,int logLevel) {
    // Flushing logic is in ChannelWriter, so don't buffer here
    super(new ChannelWriter(logChannel, logLevel), true);
    channel = logChannel;
    level = logLevel;
    enabled = logChannel.isEnabled(level);
  }

  /**
   * Get the associate channel.
   */
  public LogChannel getChannel() {
    return channel;
  }

  /**
   * Get the associate level.
   */
  public int getLevel() {
    return level;
  }

  /**
   * Determine if logging is enabled.  This is useful to prevent a series of
   * unnecessary logging calls, as often encountered with debug logging, or
   * a call where generating the message is expensive.
   */

  public boolean isEnabled() {
    return enabled;
  }
  
  /**
   * Write a string and exception to the log file.
   */
  public void println(String msg, Throwable throwable) {
    if (enabled) {
      flush();
      channel.write(level, msg, throwable);
    }
  }
}


