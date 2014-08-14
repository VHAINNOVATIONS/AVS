package gov.va.med.lom.javaBroker.rpc;

/*
 * The pulse timer thread for RpcBroker.
 * This thread sleeps in the background, waking up periodically 
 * to send a 'heartbeat' pulse to the broker server.
 */
public class PulseTimer extends Thread {

  private IPulseHandler pulseHandler;
  private long pulseInterval;        // In milliseconds.
  private boolean alive;
  private boolean running;

  /*
   * Constructor a pulse timer object.  Sets the internal 
   * pulse handler reference and interval but doesn't start the thread.
   */
  public PulseTimer(IPulseHandler pulseHandler, long pulseInterval) {
    alive = true;
    running = true;
    this.pulseHandler = pulseHandler;
    // Convert idle timer scanning interval milliseconds.
    this.pulseInterval = pulseInterval;
    setPriority(MIN_PRIORITY); // For performance
    setDaemon(true); // Don't require this thread to exit.
  }

  /*
   * The main code body of the timer Thread.  Enters an endless
   * loop that sleeps for a configurable period, periodically waking
   * up to call the doPulse callback method on the handler.
   */
  public void run() {
    while (alive) {
      try {
        sleep(pulseInterval);
      } catch (InterruptedException e) {
        // Ignore
      }
      if (alive && running) {
        try {
          pulseHandler.doPulse();
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
  public long getPulseInterval() {
    return pulseInterval;
  }
  
  public void setPulseInterval(long pulseInterval) {
    this.pulseInterval = pulseInterval;
  }  
  
  /*
   * Shutdown the thread associated with this object.
   */
  public void shutdown() {
    setAlive(false);
  }
}

