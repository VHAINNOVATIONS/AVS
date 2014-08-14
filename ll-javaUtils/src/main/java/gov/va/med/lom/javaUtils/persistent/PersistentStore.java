package gov.va.med.lom.javaUtils.persistent;

import java.io.*;

/*
 * Persitent storage interface.
 */
public interface PersistentStore {

  /*
   * Method to store and object (persistent).
   */
  public void store(String key, Serializable obj) throws PersistentStoreException;

  /*
   * Method to retrieve a stored object.
   */
  public Object retrieve(String key) throws PersistentStoreException;

  /*
   * Method to simultaneously retrieve and remove an
   * object from persistent store.  If an object is not
   * stored under key, then null is returned.
   */
  public Object remove(String key) throws PersistentStoreException;

  /*
   * Method to delete a a key.  Any objects stored under
   * key are also removed.  If key is not defined, then
   * this method does nothing.
   */
  public void delete(String key) throws PersistentStoreException;

  /*
   * Method to query if an an object is stored.
   */
  public boolean exists(String key) throws PersistentStoreException;

  /*
   * Method that returns an enumration of the keys
   * of this persistent store.
   */
  public java.util.Enumeration keys() throws PersistentStoreException;

}

