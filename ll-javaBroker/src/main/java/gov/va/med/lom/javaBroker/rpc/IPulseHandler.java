package gov.va.med.lom.javaBroker.rpc;

/*
 * Interface for class that is invoked by PulseTimer to
 * send a 'heartbeat' to the broker server.
 */
public interface IPulseHandler {
  /*
   * Method that is called by the handler to send the heartbeat pulse.
   */
  public void doPulse() throws Exception;
}
