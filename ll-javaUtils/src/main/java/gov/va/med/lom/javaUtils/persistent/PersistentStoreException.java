package gov.va.med.lom.javaUtils.persistent;

import gov.va.med.lom.javaUtils.exception.*;

/*
 * This exception is thrown by objects that implement
 * persistent storage.
 */
public class PersistentStoreException extends ChainedException {

  /*
   * Construct a exception without a specified cause.
   */
  public PersistentStoreException(String msg) {
    super(msg);
  }

  /*
   * Construct a exception with an associated causing exception.
   */
  public PersistentStoreException(String msg, Throwable cause) {
    super(msg, cause);
  }

  /*
   * Construct a exception with an associated causing exception.
   */
  public PersistentStoreException(Throwable cause) {
    super(cause);
  }
}




