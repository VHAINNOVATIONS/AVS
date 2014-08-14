package gov.va.med.lom.javaBroker.log;


import java.io.IOException;
import java.io.Writer;

/**
 * @author Rob Durkin
 * 
 * Internal Writer object that interfaces to the LogChannel.
 * Can't be inner class of LogWriter, it must be passed
 * to super constructor.
 */
class ChannelWriter extends Writer {
  // Output collected here
  private StringBuffer buffer = new StringBuffer();

  // Log channel, etc.
  private LogChannel channel;
  private int level;
  private boolean enabled;

  /**
   * Construct a new writer.
   */
  public ChannelWriter(LogChannel logChannel, int logLevel) {
    channel = logChannel;
    level = logLevel;
    enabled = logChannel.isEnabled(level);
  }
  
  /**
   * Main write method that transfers stuff to the buffer.
   */
  public void write(char[] cbuf, int off, int len) throws IOException {
    buffer.append(cbuf, off, len);
  }

  /**
   * Flush the buffer to the log file.
   */
  public void flush() throws IOException {
    if (enabled && (buffer.length() > 0)) {
      synchronized(lock) {
        channel.write(level, buffer.toString());
        buffer.setLength(0);
      }
    }
  }

  /**
   * Close is a no-op.
   */
  public void close() throws IOException {
  }
}
