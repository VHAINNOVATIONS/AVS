package gov.va.med.lom.javaBroker.util;

/*
 * This thread sleeps in the background, waking up periodically 
 * to call an event handler.
 */
public class Timer extends Thread {

  private ITimerHandler timerHandler;
  private long timerInterval;        // In milliseconds.
  private boolean alive;
  private boolean running;

  /*
   * Constructor a pulse timer object.  Sets the internal 
   * timer handler reference and interval but doesn't start the thread.
   */
  public Timer(ITimerHandler timerHandler, long timerInterval) {
    alive = true;
    running = true;
    this.timerHandler = timerHandler;
    // Convert idle timer scanning interval milliseconds.
    this.timerInterval = timerInterval;
    setPriority(MIN_PRIORITY); // For performance
    setDaemon(true); // Don't require this thread to exit.
  }

  /*
   * The main code body of the timer Thread.  Enters an endless
   * loop that sleeps for a configurable period, periodically waking
   * up to call the onTimerEvent callback method on the handler.
   */
  public void run() {
    while (alive) {
      try {
        sleep(timerInterval);
      } catch (InterruptedException e) {
        // Ignore
      }
      if (running) {
        try {
          timerHandler.onTimerEvent();
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    }
  }

  /*
   * Get/set the running status
   */
  public boolean isRunning() {
    return running;
  }

  public void setRunning(boolean running) {
    this.running = running;
  }
  
  /*
   * Get/set the active status
   */
  public boolean getAlive() {
    return alive;
  }
  
  public void setAlive(boolean alive) {
    this.alive = alive;
  }  

  /*
   * Get/set the pulse interval
   */
  public long getTimerInterval() {
    return timerInterval;
  }
  
  public void setTimerInterval(long timerInterval) {
    this.timerInterval = timerInterval;
  }  
  
  /*
   * Shutdown the thread associated with this object.
   */
  public void shutdown() {
    setAlive(false);
  }
}

