package gov.va.med.lom.javaUtils.net;

/**
 * Classes that utilize SocketConnection can handle events that a socket generates by
 * implementing this interface.
 */
public interface ISocketEventsCallback {
  
  /**
   * Occurs just after the socket connection to the remote host is established.
   */
  public void onSocketConnect();
  
  /**
   * Occurs just after the socket connection to the remote host is closed.
   */
  public void onSocketDisconnect();
  
  /**
   * Occurs when the socket throws an exception.
   * @param IOException the i/o exception that was thrown.
   */
  public void onSocketException(Exception e);
  

}
