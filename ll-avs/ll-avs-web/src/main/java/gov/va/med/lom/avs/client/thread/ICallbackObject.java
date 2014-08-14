package gov.va.med.lom.avs.client.thread;

public interface ICallbackObject {

  public abstract void contentCallback(String threadName, String content);
  
  public abstract void contentDataCallback(String threadName, String content, Object result);
  
  public abstract void dataCallback(String threadName, Object result);
  
}
