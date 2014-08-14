package gov.va.med.lom.avs.client.thread;

import java.util.Hashtable;

public class CallbackObject implements ICallbackObject {

  private int numThreadsCreated;
  private int numThreadsReturned;
  private Hashtable<String, String> threadContent;
  private Hashtable<String, Object> threadData;
  private boolean timedOut;
  
  public CallbackObject() {
    reset();
  }
  
  public void reset() {
    threadContent = new Hashtable<String, String>();
    threadData = new Hashtable<String, Object>();
    numThreadsCreated = 0;
    numThreadsReturned = 0;
    timedOut = false;    
  }
  
  public int getNumThreadsCreated() {
    return numThreadsCreated;
  }

  public void setNumThreadsCreated(int numThreadsCreated) {
    this.numThreadsCreated = numThreadsCreated;
  }

  public boolean isTimedOut() {
    return timedOut;
  }

  public void setTimedOut(boolean timedOut) {
    this.timedOut = timedOut;
  }

  public Hashtable<String, String> getThreadContent() {
    return threadContent;
  }
  
  public Hashtable<String, Object> getThreadData() {
    return threadData;
  }

  public int getNumThreadsReturned() {
    return numThreadsReturned;
  }

  public synchronized void contentCallback(String threadName, String content) {
    if (!timedOut) {
      numThreadsReturned++;
      threadContent.put(threadName, content);
      if (numThreadsReturned == numThreadsCreated) {
        notify();
      }
    }
  }  
  
  public synchronized void contentDataCallback(String threadName, String content, Object data) {
    if (!timedOut) {
      numThreadsReturned++;
      threadContent.put(threadName, content);
      threadData.put(threadName, data);
      if (numThreadsReturned == numThreadsCreated) {
        notify();
      }
    }
  }  
  
  public synchronized void dataCallback(String threadName, Object data) {
    if (!timedOut) {
      numThreadsReturned++;
      threadData.put(threadName, data);
      if (numThreadsReturned == numThreadsCreated) {
        notify();
      }
    }
  }   
  
}
